package de.grogra.imp3d.glsl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import de.grogra.imp3d.Camera;
import de.grogra.imp3d.LineSegmentizationCache;
import de.grogra.imp3d.glsl.light.LightPos;
import de.grogra.imp3d.glsl.light.shadow.GLSLShadowCube;
import de.grogra.imp3d.glsl.light.shadow.GLSLShadowMap;
import de.grogra.imp3d.glsl.light.shadow.GLSLShadowParallel;
import de.grogra.imp3d.glsl.light.shadow.GLSLShadowPerspective;
import de.grogra.imp3d.glsl.light.shadow.ShadowMapCollection;
import de.grogra.imp3d.glsl.material.MaterialConfiguration;
import de.grogra.imp3d.glsl.material.SkyMaterialConfiguration;
import de.grogra.imp3d.glsl.material.SkyPreviewConfiguration;
import de.grogra.imp3d.glsl.material.SkyReflectionMaterialConfiguration;
import de.grogra.imp3d.glsl.material.TranspMaterialConfiguration;
import de.grogra.imp3d.glsl.renderable.PlaneMaterialConfiguration;
import de.grogra.imp3d.glsl.renderable.RenderableCollection;
import de.grogra.imp3d.glsl.renderable.vbo.VBOManager;
import de.grogra.imp3d.glsl.renderpass.PresentDebugImagePass;
import de.grogra.imp3d.glsl.utility.CachedShaderCollection;
import de.grogra.imp3d.glsl.utility.DrawableContainer;
import de.grogra.imp3d.glsl.utility.FrameBufferObject;
import de.grogra.imp3d.glsl.utility.FrustumCullingTester;
import de.grogra.imp3d.glsl.utility.GLSLOpenGLObject;
import de.grogra.imp3d.glsl.utility.GLSLSkyCube;
import de.grogra.imp3d.glsl.utility.RenderBuffer;
import de.grogra.imp3d.glsl.utility.ShaderConfiguration;
import de.grogra.imp3d.glsl.utility.TextureRenderTarget;
import de.grogra.imp3d.objects.DirectionalLight;
import de.grogra.imp3d.objects.PointLight;
import de.grogra.imp3d.objects.SpotLight;
import de.grogra.imp3d.shading.Shader;
import de.grogra.imp3d.shading.SunSkyLight;
import de.grogra.pf.boot.Main;

/**
 * OpenGLState stores all relevant information about the used OpenGL Context.
 * This includes active shaders, FBOs and the state of tests like the alpha test,
 * the z-test. It also stores the state of culling and blending. This is done
 * to prevent unnecessary state changes. This class also gives access to the used FBOs
 * through an instance of the {@link GLSLFBOManager} class. It stores all data shared
 * by the different classes of OpenGL (Proteus).
 * 
 * @author Konni Hartmann
 */
public class OpenGLState {

    private RenderableCollection shapeManager = new RenderableCollection();

    public RenderableCollection getShapeManager() {
        return shapeManager;
    }

    private PresentDebugImagePass pip = new PresentDebugImagePass();

    public boolean debugDrawn = false;

    public void presentDebugScreen(GLSLDisplay disp) {
        pip.process(disp, this, null);
    }

    public String currentPassName;

    public int renderPass = 0;

    public int width = 512;

    public int height = 512;

    public void initSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int occlusionQuery[] = null;

    private GLSLFBOManager fboManager = new GLSLFBOManager();

    public ShadowMapCollection SM_Manager = new ShadowMapCollection();

    public VBOManager VBO_Manager = new VBOManager();

    public Vector<LightPos> defLight = new Vector<LightPos>();

    public LineSegmentizationCache lineCache;

    public Vector<GLSLOpenGLObject> staticObjects = new Vector<GLSLOpenGLObject>();

    public void initLineCache(GLDisplay disp) {
        if (lineCache == null) lineCache = new LineSegmentizationCache(disp.getRenderGraphState(), 1);
    }

    public DrawableContainer deferredSolidRenderable = new DrawableContainer();

    public DrawableContainer deferredToolRenderable = new DrawableContainer();

    public DrawableContainer deferredLabelRenderable = new DrawableContainer();

    public DrawableContainer deferredTranspRenderable = new DrawableContainer();

    public GLSLVolumeBuilder volume = new GLSLVolumeBuilder();

    /**
	 * Vector of LightPos-objects. Is filled by the first renderpass with lights
	 * from the scenegraph, including there transformation.
	 */
    public Vector<LightPos> lights = new Vector<LightPos>();

    /**
	 * ShaderCollection buffers all compiled Shaders used to simulate
	 * scene-shaders (like different Phong combinations, RGBAShader ...) and
	 * light-Shaders (like Spot-Light, SunSkyLight ...)
	 */
    public CachedShaderCollection csc = new CachedShaderCollection();

    protected static final int RT_NORM_POS_SHINE = 0;

    protected static final int RT_DIFF_SPEC = 1;

    protected static final int RT_EMIS_FREE = 2;

    protected static final int RT_TRANSP_SPECTRANSP = 3;

    protected static final int RT_FIRSTHDR = 0;

    protected static final int RT_SECONDHDR = 1;

    protected static final int DEFERRED_FBO = 0;

    protected static final int HDR_FBO = 1;

    protected static final int SHADOWMAP_FBO = 2;

    public static final int attachPoints[] = { GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_COLOR_ATTACHMENT1_EXT, GL.GL_COLOR_ATTACHMENT2_EXT, GL.GL_COLOR_ATTACHMENT3_EXT, GL.GL_COLOR_ATTACHMENT4_EXT, GL.GL_COLOR_ATTACHMENT5_EXT, GL.GL_COLOR_ATTACHMENT6_EXT, GL.GL_COLOR_ATTACHMENT7_EXT };

    final Logger logger = Main.getLogger();

    public void info(String txt) {
        logger.info(txt);
    }

    public void warning(String txt) {
        logger.warning(txt);
    }

    GL context = null;

    public void setGL(GL glcontext) {
        this.context = glcontext;
    }

    public GL getGL() {
        return context;
    }

    public static final char ALPHA_TEST = 0;

    public static final char STENCIL_TEST = 1;

    public static final char DEPTH_TEST = 2;

    public static final char LIGHTING = 3;

    public static final char TEXTURE_2D = 4;

    public static final char BLEND = 5;

    public static final char CULLING = 6;

    public static final char FREE2 = 7;

    public static final char ALPHA_TEST_BIT = 0x01;

    public static final char STENCIL_TEST_BIT = 0x02;

    public static final char DEPTH_TEST_BIT = 0x04;

    public static final char LIGHTING_BIT = 0x08;

    public static final char TEXTURE_2D_BIT = 0x10;

    public static final char BLEND_BIT = 0x20;

    public static final char CULLING_BIT = 0x40;

    public static final char FREE2_BIT = 0x80;

    private static final char[] stateToInternalState = { ALPHA_TEST_BIT, STENCIL_TEST_BIT, DEPTH_TEST_BIT, LIGHTING_BIT, TEXTURE_2D_BIT, BLEND_BIT, CULLING_BIT, FREE2_BIT };

    private static final int[] stateToGLState = { GL.GL_ALPHA_TEST, GL.GL_STENCIL_TEST, GL.GL_DEPTH_TEST, GL.GL_LIGHTING, GL.GL_TEXTURE_2D, GL.GL_BLEND, GL.GL_CULL_FACE, 0 };

    char state = (ALPHA_TEST_BIT | DEPTH_TEST_BIT | TEXTURE_2D_BIT | BLEND_BIT);

    int activeShader = 0;

    public int getActiveShader() {
        return activeShader;
    }

    public void setActiveProgram(int program) {
        if (program != activeShader) {
            getGL().glUseProgram(program);
            activeShader = program;
        }
    }

    public void enable(char state) {
        if (state > 5) return;
        if ((this.state & stateToInternalState[state]) == 0) {
            this.state |= stateToInternalState[state];
            getGL().glEnable(stateToGLState[state]);
        }
    }

    public void disable(char state) {
        if (state > 5) return;
        if ((this.state & stateToInternalState[state]) != 0) {
            this.state &= 0xFF - stateToInternalState[state];
            getGL().glDisable(stateToGLState[state]);
        }
    }

    public boolean getState(char state) {
        return (this.state & stateToInternalState[state]) != 0;
    }

    /**
	 * Set the GL-Statemachine to the state given.
	 * @param state The State to be set. Each Bit corresponds to a certain Parameter.
	 */
    public void setState(char state) {
        char different = (char) (this.state ^ state);
        for (char i = 0; i < 6; i++) if ((different & (1 << i)) > 0) {
            if ((state & (1 << i)) > 0) getGL().glEnable(stateToGLState[i]); else getGL().glDisable(stateToGLState[i]);
        }
        this.state = state;
    }

    int matrixMode = GL.GL_MODELVIEW;

    public void setMatrixMode(int matrixMode) {
        if (this.matrixMode != matrixMode) {
            this.matrixMode = matrixMode;
            getGL().glMatrixMode(matrixMode);
        }
    }

    boolean depthMask = true;

    public void setDepthMask(boolean maskEnable) {
        if (this.depthMask != maskEnable) {
            this.depthMask = maskEnable;
            getGL().glDepthMask(maskEnable);
        }
    }

    int currentFBO = 0;

    public void setFBO(int fboNo) {
        if (currentFBO != fboNo) {
            getGL().glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fboNo);
            currentFBO = fboNo;
        }
    }

    int face_culling = GL.GL_BACK;

    public void setFaceCullingMode(int faceCulling) {
        if (face_culling != faceCulling) {
            getGL().glCullFace(faceCulling);
            face_culling = faceCulling;
        }
    }

    public int getFaceCullingMode() {
        return face_culling;
    }

    private double[] matrixArray = new double[16];

    /**
	 * @param m
	 * @return a one dimensional array containing the data of m 
	 * ordered such that it may be used in OpenGL-Methods.
	 */
    public double[] toGLMatrix4(Matrix4d m) {
        matrixArray[0] = m.m00;
        matrixArray[1] = m.m10;
        matrixArray[2] = m.m20;
        matrixArray[3] = m.m30;
        matrixArray[4] = m.m01;
        matrixArray[5] = m.m11;
        matrixArray[6] = m.m21;
        matrixArray[7] = m.m31;
        matrixArray[8] = m.m02;
        matrixArray[9] = m.m12;
        matrixArray[10] = m.m22;
        matrixArray[11] = m.m32;
        matrixArray[12] = m.m03;
        matrixArray[13] = m.m13;
        matrixArray[14] = m.m23;
        matrixArray[15] = m.m33;
        return matrixArray;
    }

    private float[] matrixArray3 = new float[9];

    /**
	 * @param mat
	 * @return
	 */
    public float[] toGLMatrix3f(Matrix3f mat) {
        matrixArray3[0] = mat.m00;
        matrixArray3[1] = mat.m10;
        matrixArray3[2] = mat.m20;
        matrixArray3[3] = mat.m01;
        matrixArray3[4] = mat.m11;
        matrixArray3[5] = mat.m21;
        matrixArray3[6] = mat.m02;
        matrixArray3[7] = mat.m12;
        matrixArray3[8] = mat.m22;
        return matrixArray3;
    }

    public void loadMatrixd(Matrix4d m) {
        getGL().glLoadMatrixd(toGLMatrix4(m), 0);
    }

    public void cleanUp(boolean javaonly) {
        getFboManager().deleteAll(this, javaonly);
        csc.deleteAll(this, javaonly);
        SM_Manager.cleanUp(this, javaonly);
        Iterator<GLSLOpenGLObject> it = staticObjects.iterator();
        while (it.hasNext()) it.next().cleanup(this, javaonly);
        skyCube.delete(getGL(), javaonly);
        skyDiffuseCube.delete(getGL(), javaonly);
    }

    public GLSLSkyCube skyCube = new GLSLSkyCube();

    public GLSLSkyCube skyDiffuseCube = new GLSLSkyCube();

    Shader bgShader = null;

    public boolean BGFound = false;

    public void setupBGShader(Shader shader) {
        if (BGFound) return;
        bgShader = shader;
        if (shader != null) BGFound = true;
    }

    public Shader getBGShader() {
        return bgShader;
    }

    float bgPowerDensity = 50.f;

    public void setupBGPowerDensity(float powerDensity) {
        bgPowerDensity = powerDensity;
    }

    public float getBgPowerDensity() {
        return bgPowerDensity;
    }

    public int floatRT = 0;

    public void switchFloatRT() {
        floatRT = (floatRT + 1) % 2;
    }

    public int getFloatRT() {
        return floatRT;
    }

    public int getFloatRTLast() {
        return (floatRT + 1) % 2;
    }

    int shaderConfSwitch = 0;

    public void setShaderConfSwitch(int shaderConfSwitch) {
        this.shaderConfSwitch = shaderConfSwitch;
    }

    MaterialConfiguration matConf = new MaterialConfiguration();

    MaterialConfiguration skyMatConf = new SkyMaterialConfiguration();

    MaterialConfiguration skyReflMatConf = new SkyReflectionMaterialConfiguration();

    MaterialConfiguration matTranspConf = new TranspMaterialConfiguration();

    MaterialConfiguration infPlaneMat = new PlaneMaterialConfiguration();

    MaterialConfiguration skyPrevMat = new SkyPreviewConfiguration();

    public static final int DEFAULT_MATERIAL = 0;

    public static final int SKY_MATERIAL = DEFAULT_MATERIAL + 1;

    public static final int SKY_REFLECTION_MATERIAL = SKY_MATERIAL + 1;

    public static final int TRANSP_DEPTH_ONLY_MATERIAL = SKY_REFLECTION_MATERIAL + 1;

    public static final int DEPTH_PEELIG_MATERIAL = TRANSP_DEPTH_ONLY_MATERIAL + 1;

    public static final int INFINITY_PLANE_MATERIAL = DEPTH_PEELIG_MATERIAL + 1;

    public static final int SKY_PREVIEW_MATERIAL = INFINITY_PLANE_MATERIAL + 1;

    ShaderConfiguration getCurrentShaderConfiguration() {
        switch(shaderConfSwitch) {
            case DEFAULT_MATERIAL:
                return matConf;
            case SKY_MATERIAL:
                return skyMatConf;
            case SKY_REFLECTION_MATERIAL:
                return skyReflMatConf;
            case TRANSP_DEPTH_ONLY_MATERIAL:
                return matTranspConf;
            case INFINITY_PLANE_MATERIAL:
                return infPlaneMat;
            case SKY_PREVIEW_MATERIAL:
                return skyPrevMat;
            default:
                return null;
        }
    }

    public void testFBO() {
        if (GLSLDisplay.DEBUG) {
            switch(getGL().glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT)) {
                case GL.GL_FRAMEBUFFER_COMPLETE_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer complete");
                    break;
                case GL.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer incomplete format");
                    break;
                case GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer incomplete attachment");
                    break;
                case GL.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer incomplete dimensions");
                    break;
                case GL.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer incomplete draw buffer");
                    break;
                case GL.GL_FRAMEBUFFER_INCOMPLETE_DUPLICATE_ATTACHMENT_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer incomplete duplicate attachment");
                    break;
                case GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer incomplete missing attachment");
                    break;
                case GL.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer incomplete multisample");
                    break;
                case GL.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer incomplete missing attachment");
                    break;
                case GL.GL_FRAMEBUFFER_INCOMPLETE_LAYER_COUNT_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer incomplete layer count");
                    break;
                case GL.GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer incomplete layer targets");
                    break;
                case GL.GL_FRAMEBUFFER_UNSUPPORTED_EXT:
                    GLSLDisplay.printDebugInfoN("Framebuffer unsupported");
                    break;
                default:
                    GLSLDisplay.printDebugInfoN("ERROR!!!!");
            }
        }
    }

    GLU glu = new GLU();

    public boolean testGLError() {
        return testGLError("");
    }

    public boolean testGLError(String prefix) {
        boolean ok = true;
        int error = GL.GL_NO_ERROR;
        boolean first = true;
        while ((error = getGL().glGetError()) != GL.GL_NO_ERROR) {
            ok = false;
            if (first) {
                GLSLDisplay.printDebugInfo(prefix);
                first = false;
            }
            GLSLDisplay.printDebugInfo(glu.gluErrorString(error) + " ");
        }
        if (!ok) GLSLDisplay.printDebugInfoN("");
        return ok;
    }

    Stack<Long> timerStack = new Stack<Long>();

    public void startClock() {
        Long startTime = new Long(System.nanoTime());
        timerStack.add(startTime);
    }

    public long printClock(String pre) {
        long stopTime = System.nanoTime();
        long startTime = timerStack.peek();
        long diffinmikroseconds = (stopTime - startTime) / 1000;
        System.out.printf(pre + "<%d �s\n", diffinmikroseconds);
        return diffinmikroseconds;
    }

    public long popClock() {
        return System.nanoTime() - timerStack.pop();
    }

    int sceneGraphStamp = -1;

    boolean sceneGraphChanged = true;

    void updateStamp(int Stamp) {
        if (sceneGraphStamp != Stamp) {
            sceneGraphChanged = true;
            sceneGraphStamp = Stamp;
        } else sceneGraphChanged = false;
    }

    public int getGraphStamp() {
        return sceneGraphStamp;
    }

    public boolean hasGraphChanged() {
        return sceneGraphChanged;
    }

    private boolean assumeTranspMaterials = true;

    public boolean isAssumeTranspMaterials() {
        return assumeTranspMaterials;
    }

    public void setAssumeTranspMaterials(boolean assumeTranspMaterials) {
        this.assumeTranspMaterials = assumeTranspMaterials;
    }

    Matrix4d curWorldToView = new Matrix4d();

    Matrix4d invWorldToView = new Matrix4d();

    Matrix4d viewToClip = new Matrix4d();

    Matrix4d invViewToClip = new Matrix4d();

    public void updateTransformations(GLSLDisplay disp) {
        Camera c = disp.getView3D().getCamera();
        this.curWorldToView.set(c.getWorldToViewTransformation());
        this.invWorldToView.invert(this.curWorldToView);
        c.getViewToClipTransformation(viewToClip);
        this.invViewToClip.invert(this.viewToClip);
    }

    public Matrix4d getWorldToView() {
        return curWorldToView;
    }

    public Matrix4d getInvWorldToView() {
        return invWorldToView;
    }

    public Matrix4d getViewToClip() {
        return viewToClip;
    }

    public Matrix4d getInvViewToClip() {
        return invViewToClip;
    }

    public boolean resetCache = true;

    public FrustumCullingTester frustumCullingTester = new FrustumCullingTester();

    public void printGPUMemoryUsage(GLSLDisplay disp) {
        long memoryUsage = 0;
        int tmp = 0;
        tmp += getDeferredShadingFBO().estimateSizeInByteForColor();
        tmp += getDepthRB().estimateSizeInByte();
        memoryUsage += tmp;
        System.out.println("Deferred Shading: " + tmp + " Bytes");
        tmp = getHDRFBO().estimateSizeInByteForColor();
        memoryUsage += tmp;
        System.out.println("HDR: " + tmp + " Bytes");
        tmp = skyDiffuseCube.estimateSizeInByte() + skyCube.estimateSizeInByte();
        memoryUsage += tmp;
        System.out.println("SkyLight: " + tmp + " Bytes");
        tmp = getAlphaFBO().estimateSizeInByteForColor();
        tmp += getPeelingFarDepthTRT().estimateSizeInByte();
        tmp += getPeelingNearDepthTRT().estimateSizeInByte();
        memoryUsage += tmp;
        System.out.println("Depth Peeling: " + tmp + " Bytes");
        tmp = SM_Manager.getShadowMapMemoryConsumption();
        memoryUsage += tmp;
        System.out.println("Shadow Maps: " + tmp + " Bytes");
        tmp = disp.getTextureManager().estimateSizeInByte();
        memoryUsage += tmp;
        System.out.println("Textures: " + tmp + " Bytes");
        System.out.println("Total: ~" + memoryUsage / (1024 * 1024) + " MBytes");
    }

    public void invalidateCache() {
        resetCache = true;
    }

    /**
	 * @return the deferredShadingFBO
	 */
    public FrameBufferObject getDeferredShadingFBO() {
        return fboManager.deferredShadingFBO;
    }

    /**
	 * @return the HDRFBO
	 */
    public FrameBufferObject getHDRFBO() {
        return fboManager.HDRFBO;
    }

    /**
	 * @return the alphaFBO
	 */
    public FrameBufferObject getAlphaFBO() {
        return fboManager.alphaFBO;
    }

    /**
	 * @return the shadowFBO
	 */
    public FrameBufferObject getShadowFBO() {
        return fboManager.shadowFBO;
    }

    /**
	 * @return the dualDepthFBO
	 */
    public FrameBufferObject getDualDepthFBO() {
        return fboManager.dualDepthFBO;
    }

    /**
	 * @return the cubeFBO
	 */
    public FrameBufferObject getCubeFBO() {
        return fboManager.cubeFBO;
    }

    /**
	 * @return the depthRB
	 */
    public RenderBuffer getDepthRB() {
        return fboManager.depthRB;
    }

    /**
	 * @return the peelingFarDepthTRT
	 */
    public TextureRenderTarget getPeelingFarDepthTRT() {
        return fboManager.peelingFarDepthTRT;
    }

    /**
	 * @return the peelingRB
	 */
    public RenderBuffer getPeelingRB() {
        return fboManager.peelingRB;
    }

    /**
	 * @return the peelingNearDepthTRT
	 */
    public TextureRenderTarget getPeelingNearDepthTRT() {
        return fboManager.peelingNearDepthTRT;
    }

    TextureRenderTarget dsDTRT = null;

    public void setDeferredShadingDepthTRT(TextureRenderTarget dsDTRT) {
        this.dsDTRT = dsDTRT;
    }

    public TextureRenderTarget getDeferredShadingDepthTRT() {
        return dsDTRT;
    }

    /**
	 * @return the fboManager
	 */
    public GLSLFBOManager getFboManager() {
        return fboManager;
    }

    Vector3f paraLight[] = new Vector3f[5];

    public void setPoint(Vector3f v, int i) {
        if (paraLight[i] == null) paraLight[i] = new Vector3f();
        paraLight[i].set(v);
    }

    public void printPoints() {
        GL gl = getGL();
        gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glDisable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        loadMatrixd(curWorldToView);
        gl.glPointSize(8);
        gl.glBegin(GL.GL_POINTS);
        {
            gl.glColor3f(1, 1, 1);
            for (int i = 3; i >= 0; i--) {
                gl.glVertex3f(paraLight[i].x, paraLight[i].y, paraLight[i].z);
                System.out.println(paraLight[i]);
            }
        }
        gl.glEnd();
        gl.glEnable(GL.GL_DEPTH_TEST);
    }
}
