package javax.media.j3d;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;

/**
 * Abstract pipeline class for rendering pipeline methods. All rendering
 * pipeline methods are defined here.
 */
abstract class Pipeline {

    enum Type {

        NATIVE_OGL, NATIVE_D3D, JOGL, NOOP
    }

    private static final String CLASSNAME_NATIVE = "javax.media.j3d.NativePipeline";

    private static final String CLASSNAME_JOGL = "javax.media.j3d.JoglPipeline";

    private static final String CLASSNAME_NOOP = "javax.media.j3d.NoopPipeline";

    private static Pipeline pipeline;

    private Type pipelineType = null;

    /**
     * An instance of a specific Pipeline subclass should only be constructed
     * // from the createPipeline method.
     */
    protected Pipeline() {
    }

    /**
     * Method to check whether the native OpenGL library can and should be used
     * on Windows. We will use D3D if OpenGL is unavailable or undesirable.
     */
    static boolean useNativeOgl(boolean isWindowsVista, boolean nativeOglRequested) {
        String vendorString;
        try {
            vendorString = NativePipeline.getSupportedOglVendor();
        } catch (Exception ex) {
            MasterControl.getCoreLogger().severe(ex.toString());
            return false;
        } catch (Error ex) {
            MasterControl.getCoreLogger().severe(ex.toString());
            return false;
        }
        if (vendorString == null) {
            return false;
        }
        if (nativeOglRequested) {
            return true;
        }
        return preferOgl(isWindowsVista, vendorString);
    }

    private static boolean preferOgl(boolean isWindowsVista, String vendorString) {
        if (!isWindowsVista) {
            return true;
        }
        final String[] vistaD3dList = { "microsoft", "ati" };
        final String lcVendorString = vendorString.toLowerCase();
        for (int i = 0; i < vistaD3dList.length; i++) {
            if (lcVendorString.startsWith(vistaD3dList[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Initialize the Pipeline. Called exactly once by
     * MasterControl.loadLibraries() to create the singleton
     * Pipeline object.
     */
    static void createPipeline(Type pipelineType) {
        String className = null;
        switch(pipelineType) {
            case NATIVE_OGL:
            case NATIVE_D3D:
                className = CLASSNAME_NATIVE;
                break;
            case JOGL:
                className = CLASSNAME_JOGL;
                break;
            case NOOP:
                className = CLASSNAME_NOOP;
                break;
            default:
                throw new AssertionError("missing case statement");
        }
        final String pipelineClassName = className;
        pipeline = (Pipeline) java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {

            public Object run() {
                try {
                    Class pipelineClass = Class.forName(pipelineClassName);
                    return pipelineClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pipeline.initialize(pipelineType);
    }

    /**
     * Returns the singleton Pipeline object.
     */
    static Pipeline getPipeline() {
        return pipeline;
    }

    /**
     * Initializes the pipeline object. Only called by initPipeline.
     * Pipeline subclasses may override this, but must call
     * super.initialize(pipelineType);
     */
    void initialize(Type pipelineType) {
        setPipelineType(pipelineType);
    }

    /**
     * Sets the pipeline type. Only called by initialize.
     */
    private void setPipelineType(Type pipelineType) {
        this.pipelineType = pipelineType;
    }

    /**
     * Returns the pipeline type
     */
    Type getPipelineType() {
        return pipelineType;
    }

    /**
     * Returns the pipeline name
     */
    String getPipelineName() {
        switch(pipelineType) {
            case NATIVE_OGL:
                return "NATIVE_OGL";
            case NATIVE_D3D:
                return "NATIVE_D3D";
            case JOGL:
                return "JOGL";
            case NOOP:
                return "NOOP";
            default:
                throw new AssertionError("missing case statement");
        }
    }

    /**
     * Returns the renderer name
     */
    String getRendererName() {
        switch(pipelineType) {
            case NATIVE_OGL:
            case JOGL:
                return "OpenGL";
            case NATIVE_D3D:
                return "DirectX";
            case NOOP:
                return "None";
            default:
                throw new AssertionError("missing case statement");
        }
    }

    /**
     * Load all of the required libraries
     */
    abstract void loadLibraries(int globalShadingLanguage);

    /**
     * Returns true if the Cg library is loaded and available. Note that this
     * does not necessarily mean that Cg is supported by the graphics card.
     */
    abstract boolean isCgLibraryAvailable();

    /**
     * Returns true if the GLSL library is loaded and available. Note that this
     * does not necessarily mean that GLSL is supported by the graphics card.
     */
    abstract boolean isGLSLLibraryAvailable();

    abstract void freeD3DArray(GeometryArrayRetained geo, boolean deleteVB);

    abstract void execute(Context ctx, GeometryArrayRetained geo, int geo_type, boolean isNonUniformScale, boolean useAlpha, boolean ignoreVertexColors, int startVIndex, int vcount, int vformat, int texCoordSetCount, int[] texCoordSetMap, int texCoordSetMapLen, int[] texCoordSetOffset, int numActiveTexUnitState, int vertexAttrCount, int[] vertexAttrSizes, float[] varray, float[] cdata, int cdirty);

    abstract void executeVA(Context ctx, GeometryArrayRetained geo, int geo_type, boolean isNonUniformScale, boolean ignoreVertexColors, int vcount, int vformat, int vdefined, int coordIndex, float[] vfcoords, double[] vdcoords, int colorIndex, float[] cfdata, byte[] cbdata, int normalIndex, float[] ndata, int vertexAttrCount, int[] vertexAttrSizes, int[] vertexAttrIndex, float[][] vertexAttrData, int texcoordmaplength, int[] texcoordoffset, int numActiveTexUnitState, int[] texIndex, int texstride, Object[] texCoords, int cdirty);

    abstract void executeVABuffer(Context ctx, GeometryArrayRetained geo, int geo_type, boolean isNonUniformScale, boolean ignoreVertexColors, int vcount, int vformat, int vdefined, int coordIndex, Object vcoords, int colorIndex, Object cdataBuffer, float[] cfdata, byte[] cbdata, int normalIndex, Object ndata, int vertexAttrCount, int[] vertexAttrSizes, int[] vertexAttrIndex, Object[] vertexAttrData, int texcoordmaplength, int[] texcoordoffset, int numActiveTexUnitState, int[] texIndex, int texstride, Object[] texCoords, int cdirty);

    abstract void executeInterleavedBuffer(Context ctx, GeometryArrayRetained geo, int geo_type, boolean isNonUniformScale, boolean useAlpha, boolean ignoreVertexColors, int startVIndex, int vcount, int vformat, int texCoordSetCount, int[] texCoordSetMap, int texCoordSetMapLen, int[] texCoordSetOffset, int numActiveTexUnitState, Object varray, float[] cdata, int cdirty);

    abstract void setVertexFormat(Context ctx, GeometryArrayRetained geo, int vformat, boolean useAlpha, boolean ignoreVertexColors);

    abstract void disableGlobalAlpha(Context ctx, GeometryArrayRetained geo, int vformat, boolean useAlpha, boolean ignoreVertexColors);

    abstract void buildGA(Context ctx, GeometryArrayRetained geo, int geo_type, boolean isNonUniformScale, boolean updateAlpha, float alpha, boolean ignoreVertexColors, int startVIndex, int vcount, int vformat, int texCoordSetCount, int[] texCoordSetMap, int texCoordSetMapLen, int[] texCoordSetMapOffset, int vertexAttrCount, int[] vertexAttrSizes, double[] xform, double[] nxform, float[] varray);

    abstract void buildGAForByRef(Context ctx, GeometryArrayRetained geo, int geo_type, boolean isNonUniformScale, boolean updateAlpha, float alpha, boolean ignoreVertexColors, int vcount, int vformat, int vdefined, int coordIndex, float[] vfcoords, double[] vdcoords, int colorIndex, float[] cfdata, byte[] cbdata, int normalIndex, float[] ndata, int vertexAttrCount, int[] vertexAttrSizes, int[] vertexAttrIndex, float[][] vertexAttrData, int texcoordmaplength, int[] texcoordoffset, int[] texIndex, int texstride, Object[] texCoords, double[] xform, double[] nxform);

    abstract void executeIndexedGeometry(Context ctx, GeometryArrayRetained geo, int geo_type, boolean isNonUniformScale, boolean useAlpha, boolean ignoreVertexColors, int initialIndexIndex, int indexCount, int vertexCount, int vformat, int vertexAttrCount, int[] vertexAttrSizes, int texCoordSetCount, int[] texCoordSetMap, int texCoordSetMapLen, int[] texCoordSetOffset, int numActiveTexUnitState, float[] varray, float[] cdata, int cdirty, int[] indexCoord);

    abstract void executeIndexedGeometryBuffer(Context ctx, GeometryArrayRetained geo, int geo_type, boolean isNonUniformScale, boolean useAlpha, boolean ignoreVertexColors, int initialIndexIndex, int indexCount, int vertexCount, int vformat, int texCoordSetCount, int[] texCoordSetMap, int texCoordSetMapLen, int[] texCoordSetOffset, int numActiveTexUnitState, Object varray, float[] cdata, int cdirty, int[] indexCoord);

    abstract void executeIndexedGeometryVA(Context ctx, GeometryArrayRetained geo, int geo_type, boolean isNonUniformScale, boolean ignoreVertexColors, int initialIndexIndex, int validIndexCount, int vertexCount, int vformat, int vdefined, float[] vfcoords, double[] vdcoords, float[] cfdata, byte[] cbdata, float[] ndata, int vertexAttrCount, int[] vertexAttrSizes, float[][] vertexAttrData, int texcoordmaplength, int[] texcoordoffset, int numActiveTexUnitState, int texstride, Object[] texCoords, int cdirty, int[] indexCoord);

    abstract void executeIndexedGeometryVABuffer(Context ctx, GeometryArrayRetained geo, int geo_type, boolean isNonUniformScale, boolean ignoreVertexColors, int initialIndexIndex, int validIndexCount, int vertexCount, int vformat, int vdefined, Object vcoords, Object cdataBuffer, float[] cfdata, byte[] cbdata, Object normal, int vertexAttrCount, int[] vertexAttrSizes, Object[] vertexAttrData, int texcoordmaplength, int[] texcoordoffset, int numActiveTexUnitState, int texstride, Object[] texCoords, int cdirty, int[] indexCoord);

    abstract void buildIndexedGeometry(Context ctx, GeometryArrayRetained geo, int geo_type, boolean isNonUniformScale, boolean updateAlpha, float alpha, boolean ignoreVertexColors, int initialIndexIndex, int validIndexCount, int vertexCount, int vformat, int vertexAttrCount, int[] vertexAttrSizes, int texCoordSetCount, int[] texCoordSetMap, int texCoordSetMapLen, int[] texCoordSetMapOffset, double[] xform, double[] nxform, float[] varray, int[] indexCoord);

    abstract void readRaster(Context ctx, int type, int xSrcOffset, int ySrcOffset, int width, int height, int hCanvas, int imageDataType, int imageFormat, Object imageBuffer, int depthFormat, Object depthBuffer);

    abstract ShaderError setCgUniform1i(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int value);

    abstract ShaderError setCgUniform1f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float value);

    abstract ShaderError setCgUniform2i(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int[] value);

    abstract ShaderError setCgUniform2f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float[] value);

    abstract ShaderError setCgUniform3i(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int[] value);

    abstract ShaderError setCgUniform3f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float[] value);

    abstract ShaderError setCgUniform4i(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int[] value);

    abstract ShaderError setCgUniform4f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float[] value);

    abstract ShaderError setCgUniformMatrix3f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float[] value);

    abstract ShaderError setCgUniformMatrix4f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float[] value);

    abstract ShaderError setCgUniform1iArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, int[] value);

    abstract ShaderError setCgUniform1fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError setCgUniform2iArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, int[] value);

    abstract ShaderError setCgUniform2fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError setCgUniform3iArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, int[] value);

    abstract ShaderError setCgUniform3fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError setCgUniform4iArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, int[] value);

    abstract ShaderError setCgUniform4fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError setCgUniformMatrix3fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError setCgUniformMatrix4fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError createCgShader(Context ctx, int shaderType, ShaderId[] shaderId);

    abstract ShaderError destroyCgShader(Context ctx, ShaderId shaderId);

    abstract ShaderError compileCgShader(Context ctx, ShaderId shaderId, String program);

    abstract ShaderError createCgShaderProgram(Context ctx, ShaderProgramId[] shaderProgramId);

    abstract ShaderError destroyCgShaderProgram(Context ctx, ShaderProgramId shaderProgramId);

    abstract ShaderError linkCgShaderProgram(Context ctx, ShaderProgramId shaderProgramId, ShaderId[] shaderIds);

    abstract void lookupCgVertexAttrNames(Context ctx, ShaderProgramId shaderProgramId, int numAttrNames, String[] attrNames, boolean[] errArr);

    abstract void lookupCgShaderAttrNames(Context ctx, ShaderProgramId shaderProgramId, int numAttrNames, String[] attrNames, ShaderAttrLoc[] locArr, int[] typeArr, int[] sizeArr, boolean[] isArrayArr);

    abstract ShaderError useCgShaderProgram(Context ctx, ShaderProgramId shaderProgramId);

    abstract ShaderError setGLSLUniform1i(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int value);

    abstract ShaderError setGLSLUniform1f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float value);

    abstract ShaderError setGLSLUniform2i(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int[] value);

    abstract ShaderError setGLSLUniform2f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float[] value);

    abstract ShaderError setGLSLUniform3i(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int[] value);

    abstract ShaderError setGLSLUniform3f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float[] value);

    abstract ShaderError setGLSLUniform4i(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int[] value);

    abstract ShaderError setGLSLUniform4f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float[] value);

    abstract ShaderError setGLSLUniformMatrix3f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float[] value);

    abstract ShaderError setGLSLUniformMatrix4f(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, float[] value);

    abstract ShaderError setGLSLUniform1iArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, int[] value);

    abstract ShaderError setGLSLUniform1fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError setGLSLUniform2iArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, int[] value);

    abstract ShaderError setGLSLUniform2fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError setGLSLUniform3iArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, int[] value);

    abstract ShaderError setGLSLUniform3fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError setGLSLUniform4iArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, int[] value);

    abstract ShaderError setGLSLUniform4fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError setGLSLUniformMatrix3fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError setGLSLUniformMatrix4fArray(Context ctx, ShaderProgramId shaderProgramId, ShaderAttrLoc uniformLocation, int numElements, float[] value);

    abstract ShaderError createGLSLShader(Context ctx, int shaderType, ShaderId[] shaderId);

    abstract ShaderError destroyGLSLShader(Context ctx, ShaderId shaderId);

    abstract ShaderError compileGLSLShader(Context ctx, ShaderId shaderId, String program);

    abstract ShaderError createGLSLShaderProgram(Context ctx, ShaderProgramId[] shaderProgramId);

    abstract ShaderError destroyGLSLShaderProgram(Context ctx, ShaderProgramId shaderProgramId);

    abstract ShaderError linkGLSLShaderProgram(Context ctx, ShaderProgramId shaderProgramId, ShaderId[] shaderIds);

    abstract ShaderError bindGLSLVertexAttrName(Context ctx, ShaderProgramId shaderProgramId, String attrName, int attrIndex);

    abstract void lookupGLSLShaderAttrNames(Context ctx, ShaderProgramId shaderProgramId, int numAttrNames, String[] attrNames, ShaderAttrLoc[] locArr, int[] typeArr, int[] sizeArr, boolean[] isArrayArr);

    abstract ShaderError useGLSLShaderProgram(Context ctx, ShaderProgramId shaderProgramId);

    boolean checkNativeBufferAccess(java.nio.Buffer buffer) {
        return true;
    }

    abstract void cleanupRenderer();

    abstract void updateColoringAttributes(Context ctx, float dRed, float dGreen, float dBlue, float red, float green, float blue, float alpha, boolean lEnable, int shadeModel);

    abstract void updateDirectionalLight(Context ctx, int lightSlot, float red, float green, float blue, float x, float y, float z);

    abstract void updatePointLight(Context ctx, int lightSlot, float red, float green, float blue, float ax, float ay, float az, float px, float py, float pz);

    abstract void updateSpotLight(Context ctx, int lightSlot, float red, float green, float blue, float ax, float ay, float az, float px, float py, float pz, float spreadAngle, float concentration, float dx, float dy, float dz);

    abstract void updateExponentialFog(Context ctx, float red, float green, float blue, float density);

    abstract void updateLinearFog(Context ctx, float red, float green, float blue, double fdist, double bdist);

    abstract void updateLineAttributes(Context ctx, float lineWidth, int linePattern, int linePatternMask, int linePatternScaleFactor, boolean lineAntialiasing);

    abstract void updateMaterial(Context ctx, float red, float green, float blue, float alpha, float ared, float agreen, float ablue, float ered, float egreen, float eblue, float dred, float dgreen, float dblue, float sred, float sgreen, float sblue, float shininess, int colorTarget, boolean enable);

    abstract void updateModelClip(Context ctx, int planeNum, boolean enableFlag, double A, double B, double C, double D);

    abstract void updatePointAttributes(Context ctx, float pointSize, boolean pointAntialiasing);

    abstract void updatePolygonAttributes(Context ctx, int polygonMode, int cullFace, boolean backFaceNormalFlip, float polygonOffset, float polygonOffsetFactor);

    abstract void updateRenderingAttributes(Context ctx, boolean depthBufferWriteEnableOverride, boolean depthBufferEnableOverride, boolean depthBufferEnable, boolean depthBufferWriteEnable, int depthTestFunction, float alphaTestValue, int alphaTestFunction, boolean ignoreVertexColors, boolean rasterOpEnable, int rasterOp, boolean userStencilAvailable, boolean stencilEnable, int stencilFailOp, int stencilZFailOp, int stencilZPassOp, int stencilFunction, int stencilReferenceValue, int stencilCompareMask, int stencilWriteMask);

    /**
    * This method updates the native context:
    * trans contains eyeTovworld transform in d3d
    * trans contains vworldToEye transform in ogl
    */
    abstract void updateTexCoordGeneration(Context ctx, boolean enable, int genMode, int format, float planeSx, float planeSy, float planeSz, float planeSw, float planeTx, float planeTy, float planeTz, float planeTw, float planeRx, float planeRy, float planeRz, float planeRw, float planeQx, float planeQy, float planeQz, float planeQw, double[] trans);

    abstract void updateTransparencyAttributes(Context ctx, float alpha, int geometryType, int polygonMode, boolean lineAA, boolean pointAA, int transparencyMode, int srcBlendFunction, int dstBlendFunction);

    abstract void updateTextureAttributes(Context ctx, double[] transform, boolean isIdentity, int textureMode, int perspCorrectionMode, float red, float green, float blue, float alpha, int textureFormat);

    abstract void updateRegisterCombiners(Context ctx, double[] transform, boolean isIdentity, int textureMode, int perspCorrectionMode, float red, float green, float blue, float alpha, int textureFormat, int combineRgbMode, int combineAlphaMode, int[] combineRgbSrc, int[] combineAlphaSrc, int[] combineRgbFcn, int[] combineAlphaFcn, int combineRgbScale, int combineAlphaScale);

    abstract void updateTextureColorTable(Context ctx, int numComponents, int colorTableSize, int[] colorTable);

    abstract void updateCombiner(Context ctx, int combineRgbMode, int combineAlphaMode, int[] combineRgbSrc, int[] combineAlphaSrc, int[] combineRgbFcn, int[] combineAlphaFcn, int combineRgbScale, int combineAlphaScale);

    abstract void updateTextureUnitState(Context ctx, int unitIndex, boolean enableFlag);

    abstract void bindTexture2D(Context ctx, int objectId, boolean enable);

    abstract void updateTexture2DImage(Context ctx, int numLevels, int level, int textureFormat, int imageFormat, int width, int height, int boundaryWidth, int imageDataType, Object data, boolean useAutoMipMap);

    abstract void updateTexture2DSubImage(Context ctx, int level, int xoffset, int yoffset, int textureFormat, int imageFormat, int imgXOffset, int imgYOffset, int tilew, int width, int height, int imageDataType, Object data, boolean useAutoMipMap);

    abstract void updateTexture2DLodRange(Context ctx, int baseLevel, int maximumLevel, float minimumLod, float maximumLod);

    abstract void updateTexture2DLodOffset(Context ctx, float lodOffsetX, float lodOffsetY, float lodOffsetZ);

    abstract void updateTexture2DBoundary(Context ctx, int boundaryModeS, int boundaryModeT, float boundaryRed, float boundaryGreen, float boundaryBlue, float boundaryAlpha);

    abstract void updateTexture2DFilterModes(Context ctx, int minFilter, int magFilter);

    abstract void updateTexture2DSharpenFunc(Context ctx, int numSharpenTextureFuncPts, float[] sharpenTextureFuncPts);

    abstract void updateTexture2DFilter4Func(Context ctx, int numFilter4FuncPts, float[] filter4FuncPts);

    abstract void updateTexture2DAnisotropicFilter(Context ctx, float degree);

    abstract void bindTexture3D(Context ctx, int objectId, boolean enable);

    abstract void updateTexture3DImage(Context ctx, int numLevels, int level, int textureFormat, int imageFormat, int width, int height, int depth, int boundaryWidth, int imageDataType, Object imageData, boolean useAutoMipMap);

    abstract void updateTexture3DSubImage(Context ctx, int level, int xoffset, int yoffset, int zoffset, int textureFormat, int imageFormat, int imgXoffset, int imgYoffset, int imgZoffset, int tilew, int tileh, int width, int height, int depth, int imageDataType, Object imageData, boolean useAutoMipMap);

    abstract void updateTexture3DLodRange(Context ctx, int baseLevel, int maximumLevel, float minimumLod, float maximumLod);

    abstract void updateTexture3DLodOffset(Context ctx, float lodOffsetX, float lodOffsetY, float lodOffsetZ);

    abstract void updateTexture3DBoundary(Context ctx, int boundaryModeS, int boundaryModeT, int boundaryModeR, float boundaryRed, float boundaryGreen, float boundaryBlue, float boundaryAlpha);

    abstract void updateTexture3DFilterModes(Context ctx, int minFilter, int magFilter);

    abstract void updateTexture3DSharpenFunc(Context ctx, int numSharpenTextureFuncPts, float[] sharpenTextureFuncPts);

    abstract void updateTexture3DFilter4Func(Context ctx, int numFilter4FuncPts, float[] filter4FuncPts);

    abstract void updateTexture3DAnisotropicFilter(Context ctx, float degree);

    abstract void bindTextureCubeMap(Context ctx, int objectId, boolean enable);

    abstract void updateTextureCubeMapImage(Context ctx, int face, int numLevels, int level, int textureFormat, int imageFormat, int width, int height, int boundaryWidth, int imageDataType, Object imageData, boolean useAutoMipMap);

    abstract void updateTextureCubeMapSubImage(Context ctx, int face, int level, int xoffset, int yoffset, int textureFormat, int imageFormat, int imgXOffset, int imgYOffset, int tilew, int width, int height, int imageDataType, Object imageData, boolean useAutoMipMap);

    abstract void updateTextureCubeMapLodRange(Context ctx, int baseLevel, int maximumLevel, float minimumLod, float maximumLod);

    abstract void updateTextureCubeMapLodOffset(Context ctx, float lodOffsetX, float lodOffsetY, float lodOffsetZ);

    abstract void updateTextureCubeMapBoundary(Context ctx, int boundaryModeS, int boundaryModeT, float boundaryRed, float boundaryGreen, float boundaryBlue, float boundaryAlpha);

    abstract void updateTextureCubeMapFilterModes(Context ctx, int minFilter, int magFilter);

    abstract void updateTextureCubeMapSharpenFunc(Context ctx, int numSharpenTextureFuncPts, float[] sharpenTextureFuncPts);

    abstract void updateTextureCubeMapFilter4Func(Context ctx, int numFilter4FuncPts, float[] filter4FuncPts);

    abstract void updateTextureCubeMapAnisotropicFilter(Context ctx, float degree);

    abstract long getAWT();

    abstract boolean initializeJ3D(boolean disableXinerama);

    abstract int getMaximumLights();

    abstract Context createNewContext(Canvas3D cv, long display, Drawable drawable, long fbConfig, Context shareCtx, boolean isSharedCtx, boolean offScreen, boolean glslLibraryAvailable, boolean cgLibraryAvailable);

    abstract void createQueryContext(Canvas3D cv, long display, Drawable drawable, long fbConfig, boolean offScreen, int width, int height, boolean glslLibraryAvailable, boolean cgLibraryAvailable);

    abstract Drawable createOffScreenBuffer(Canvas3D cv, Context ctx, long display, long fbConfig, int width, int height);

    abstract void destroyOffScreenBuffer(Canvas3D cv, Context ctx, long display, long fbConfig, Drawable drawable);

    abstract void readOffScreenBuffer(Canvas3D cv, Context ctx, int format, int type, Object data, int width, int height);

    abstract int swapBuffers(Canvas3D cv, Context ctx, long dpy, Drawable drawable);

    abstract int resizeD3DCanvas(Canvas3D cv, Context ctx);

    abstract int toggleFullScreenMode(Canvas3D cv, Context ctx);

    abstract void updateMaterialColor(Context ctx, float r, float g, float b, float a);

    abstract void destroyContext(long display, Drawable drawable, Context ctx);

    abstract void accum(Context ctx, float value);

    abstract void accumReturn(Context ctx);

    abstract void clearAccum(Context ctx);

    abstract int getNumCtxLights(Context ctx);

    abstract boolean decal1stChildSetup(Context ctx);

    abstract void decalNthChildSetup(Context ctx);

    abstract void decalReset(Context ctx, boolean depthBufferEnable);

    abstract void ctxUpdateEyeLightingEnable(Context ctx, boolean localEyeLightingEnable);

    abstract void setBlendColor(Context ctx, float red, float green, float blue, float alpha);

    abstract void setBlendFunc(Context ctx, int src, int dst);

    abstract void setFogEnableFlag(Context ctx, boolean enableFlag);

    abstract void setFullSceneAntialiasing(Context ctx, boolean enable);

    abstract void setGlobalAlpha(Context ctx, float alpha);

    abstract void updateSeparateSpecularColorEnable(Context ctx, boolean control);

    abstract void beginScene(Context ctx);

    abstract void endScene(Context ctx);

    abstract boolean validGraphicsMode();

    abstract void setLightEnables(Context ctx, long enableMask, int maxLights);

    abstract void setSceneAmbient(Context ctx, float red, float green, float blue);

    abstract void disableFog(Context ctx);

    abstract void disableModelClip(Context ctx);

    abstract void resetRenderingAttributes(Context ctx, boolean depthBufferWriteEnableOverride, boolean depthBufferEnableOverride);

    abstract void resetTextureNative(Context ctx, int texUnitIndex);

    abstract void activeTextureUnit(Context ctx, int texUnitIndex);

    abstract void resetTexCoordGeneration(Context ctx);

    abstract void resetTextureAttributes(Context ctx);

    abstract void resetPolygonAttributes(Context ctx);

    abstract void resetLineAttributes(Context ctx);

    abstract void resetPointAttributes(Context ctx);

    abstract void resetTransparency(Context ctx, int geometryType, int polygonMode, boolean lineAA, boolean pointAA);

    abstract void resetColoringAttributes(Context ctx, float r, float g, float b, float a, boolean enableLight);

    /**
     *  This native method makes sure that the rendering for this canvas
     *  gets done now.
     */
    abstract void syncRender(Context ctx, boolean wait);

    abstract boolean useCtx(Context ctx, long display, Drawable drawable);

    boolean releaseCtx(Context ctx, long dpy) {
        return false;
    }

    abstract void clear(Context ctx, float r, float g, float b, boolean clearStencil);

    abstract void textureFillBackground(Context ctx, float texMinU, float texMaxU, float texMinV, float texMaxV, float mapMinX, float mapMaxX, float mapMinY, float mapMaxY, boolean useBiliearFilter);

    abstract void textureFillRaster(Context ctx, float texMinU, float texMaxU, float texMinV, float texMaxV, float mapMinX, float mapMaxX, float mapMinY, float mapMaxY, float mapZ, float alpha, boolean useBiliearFilter);

    abstract void executeRasterDepth(Context ctx, float posX, float posY, float posZ, int srcOffsetX, int srcOffsetY, int rasterWidth, int rasterHeight, int depthWidth, int depthHeight, int depthType, Object depthData);

    abstract void setModelViewMatrix(Context ctx, double[] viewMatrix, double[] modelMatrix);

    abstract void setProjectionMatrix(Context ctx, double[] projMatrix);

    abstract void setViewport(Context ctx, int x, int y, int width, int height);

    abstract void newDisplayList(Context ctx, int displayListId);

    abstract void endDisplayList(Context ctx);

    abstract void callDisplayList(Context ctx, int id, boolean isNonUniformScale);

    abstract void freeDisplayList(Context ctx, int id);

    abstract void freeTexture(Context ctx, int id);

    abstract void texturemapping(Context ctx, int px, int py, int xmin, int ymin, int xmax, int ymax, int texWidth, int texHeight, int rasWidth, int format, int objectId, byte[] image, int winWidth, int winHeight);

    abstract boolean initTexturemapping(Context ctx, int texWidth, int texHeight, int objectId);

    abstract void setRenderMode(Context ctx, int mode, boolean doubleBuffer);

    abstract void setDepthBufferWriteEnable(Context ctx, boolean mode);

    abstract GraphicsConfiguration getGraphicsConfig(GraphicsConfiguration gconfig);

    abstract long getFbConfig(GraphicsConfigInfo gcInfo);

    abstract GraphicsConfiguration getBestConfiguration(GraphicsConfigTemplate3D gct, GraphicsConfiguration[] gc);

    abstract boolean isGraphicsConfigSupported(GraphicsConfigTemplate3D gct, GraphicsConfiguration gc);

    abstract boolean hasDoubleBuffer(Canvas3D cv);

    abstract boolean hasStereo(Canvas3D cv);

    abstract int getStencilSize(Canvas3D cv);

    abstract boolean hasSceneAntialiasingMultisample(Canvas3D cv);

    abstract boolean hasSceneAntialiasingAccum(Canvas3D cv);

    abstract long getDisplay();

    abstract int getScreen(GraphicsDevice graphicsDevice);

    abstract DrawingSurfaceObject createDrawingSurfaceObject(Canvas3D cv);

    abstract void freeDrawingSurface(Canvas3D cv, DrawingSurfaceObject drawingSurfaceObject);

    abstract void freeDrawingSurfaceNative(Object o);
}
