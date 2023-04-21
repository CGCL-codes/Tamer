package jpcsp.graphics.RE.software;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import jpcsp.Memory;
import jpcsp.graphics.VertexInfo;
import jpcsp.graphics.VertexState;
import jpcsp.graphics.VideoEngine;
import jpcsp.graphics.RE.BaseRenderingEngine;
import jpcsp.graphics.RE.IRenderingEngine;
import jpcsp.memory.IMemoryReader;
import jpcsp.memory.ImageReader;
import jpcsp.util.DurationStatistics;
import jpcsp.util.Utilities;

/**
 * @author gid15
 *
 * This RenderingEngine class implements a software-based rendering,
 * not using OpenGL or any GPU.
 * This is probably the most accurate implementation but also the slowest one.
 */
public class RESoftware extends BaseRenderingEngine {

    private static final boolean useTextureCache = true;

    protected int genTextureId;

    protected int bindTexture;

    protected VertexState v1 = new VertexState();

    protected VertexState v2 = new VertexState();

    protected VertexState v3 = new VertexState();

    protected VertexState v4 = new VertexState();

    protected VertexState v5 = new VertexState();

    protected VertexState v6 = new VertexState();

    protected RendererExecutor rendererExecutor;

    protected HashMap<Integer, CachedTextureResampled> cachedTextures = new HashMap<Integer, CachedTextureResampled>();

    protected int textureBufferWidth;

    protected static DurationStatistics drawArraysStatistics = new DurationStatistics("RESoftware drawArrays");

    public static DurationStatistics triangleRender3DStatistics = new DurationStatistics("RESoftware TriangleRender3D");

    public static DurationStatistics triangleRender2DStatistics = new DurationStatistics("RESoftware TriangleRender2D");

    public static DurationStatistics spriteRenderStatistics = new DurationStatistics("RESoftware SpriteRender");

    protected static DurationStatistics cachedTextureStatistics = new DurationStatistics("RESoftware CachedTexture");

    public static DurationStatistics textureResamplingStatistics = new DurationStatistics("RESftware Texture resampling");

    protected BoundingBoxRenderer boundingBoxRenderer;

    protected boolean boundingBoxVisible;

    protected BufferVertexReader bufferVertexReader;

    protected boolean useVertexTexture;

    public RESoftware() {
        log.info("Using SoftwareRenderer");
    }

    @Override
    public void exit() {
        if (DurationStatistics.collectStatistics) {
            log.info(drawArraysStatistics);
            log.info(triangleRender3DStatistics);
            log.info(triangleRender2DStatistics);
            log.info(spriteRenderStatistics);
            log.info(cachedTextureStatistics);
            log.info(textureResamplingStatistics);
        }
    }

    @Override
    public void startDirectRendering(boolean textureEnabled, boolean depthWriteEnabled, boolean colorWriteEnabled, boolean setOrthoMatrix, boolean orthoInverted, int width, int height) {
    }

    @Override
    public void endDirectRendering() {
    }

    @Override
    public void startDisplay() {
        context = VideoEngine.getInstance().getContext();
        rendererExecutor = RendererExecutor.getInstance();
    }

    @Override
    public void endDisplay() {
    }

    @Override
    public void enableFlag(int flag) {
    }

    @Override
    public void disableFlag(int flag) {
    }

    @Override
    public void setMorphWeight(int index, float value) {
    }

    @Override
    public void setPatchDiv(int s, int t) {
    }

    @Override
    public void setPatchPrim(int prim) {
    }

    @Override
    public void setMatrixMode(int type) {
    }

    @Override
    public void setMatrix(float[] values) {
    }

    @Override
    public void multMatrix(float[] values) {
    }

    @Override
    public void endModelViewMatrixUpdate() {
    }

    @Override
    public void setViewport(int x, int y, int width, int height) {
    }

    @Override
    public void setDepthRange(float zpos, float zscale, float near, float far) {
    }

    @Override
    public void setDepthFunc(int func) {
    }

    @Override
    public void setShadeModel(int model) {
    }

    @Override
    public void setMaterialEmissiveColor(float[] color) {
    }

    @Override
    public void setMaterialAmbientColor(float[] color) {
    }

    @Override
    public void setMaterialDiffuseColor(float[] color) {
    }

    @Override
    public void setMaterialSpecularColor(float[] color) {
    }

    @Override
    public void setMaterialShininess(float shininess) {
    }

    @Override
    public void setLightModelAmbientColor(float[] color) {
    }

    @Override
    public void setLightMode(int mode) {
    }

    @Override
    public void setLightPosition(int light, float[] position) {
    }

    @Override
    public void setLightDirection(int light, float[] direction) {
    }

    @Override
    public void setLightSpotExponent(int light, float exponent) {
    }

    @Override
    public void setLightSpotCutoff(int light, float cutoff) {
    }

    @Override
    public void setLightConstantAttenuation(int light, float constant) {
    }

    @Override
    public void setLightLinearAttenuation(int light, float linear) {
    }

    @Override
    public void setLightQuadraticAttenuation(int light, float quadratic) {
    }

    @Override
    public void setLightAmbientColor(int light, float[] color) {
    }

    @Override
    public void setLightDiffuseColor(int light, float[] color) {
    }

    @Override
    public void setLightSpecularColor(int light, float[] color) {
    }

    @Override
    public void setLightType(int light, int type, int kind) {
    }

    @Override
    public void setBlendFunc(int src, int dst) {
    }

    @Override
    public void setBlendColor(float[] color) {
    }

    @Override
    public void setLogicOp(int logicOp) {
    }

    @Override
    public void setDepthMask(boolean depthWriteEnabled) {
    }

    @Override
    public void setColorMask(int redMask, int greenMask, int blueMask, int alphaMask) {
    }

    @Override
    public void setColorMask(boolean redWriteEnabled, boolean greenWriteEnabled, boolean blueWriteEnabled, boolean alphaWriteEnabled) {
    }

    @Override
    public void setTextureWrapMode(int s, int t) {
    }

    @Override
    public void setTextureMipmapMinLevel(int level) {
    }

    @Override
    public void setTextureMipmapMaxLevel(int level) {
    }

    @Override
    public void setTextureMipmapMinFilter(int filter) {
    }

    @Override
    public void setTextureMipmapMagFilter(int filter) {
    }

    @Override
    public void setColorMaterial(boolean ambient, boolean diffuse, boolean specular) {
    }

    @Override
    public void setTextureMapMode(int mode, int proj) {
    }

    @Override
    public void setTextureEnvironmentMapping(int u, int v) {
    }

    @Override
    public void setUniform(int id, int value) {
    }

    @Override
    public void setUniform(int id, int value1, int value2) {
    }

    @Override
    public void setUniform(int id, float value) {
    }

    @Override
    public void setUniform2(int id, int[] values) {
    }

    @Override
    public void setUniform3(int id, int[] values) {
    }

    @Override
    public void setUniform3(int id, float[] values) {
    }

    @Override
    public void setUniform4(int id, int[] values) {
    }

    @Override
    public void setUniform4(int id, float[] values) {
    }

    @Override
    public void setUniformMatrix4(int id, int count, float[] values) {
    }

    @Override
    public void setColorTestFunc(int func) {
    }

    @Override
    public void setColorTestReference(int[] values) {
    }

    @Override
    public void setColorTestMask(int[] values) {
    }

    @Override
    public void setTextureFunc(int func, boolean alphaUsed, boolean colorDoubled) {
    }

    @Override
    public int setBones(int count, float[] values) {
        return count;
    }

    @Override
    public void setTexEnv(int name, int param) {
    }

    @Override
    public void setTexEnv(int name, float param) {
    }

    @Override
    public int createShader(int type) {
        return 0;
    }

    @Override
    public boolean compilerShader(int shader, String source) {
        return false;
    }

    @Override
    public int createProgram() {
        return 0;
    }

    @Override
    public void useProgram(int program) {
    }

    @Override
    public void attachShader(int program, int shader) {
    }

    @Override
    public boolean linkProgram(int program) {
        return false;
    }

    @Override
    public boolean validateProgram(int program) {
        return false;
    }

    @Override
    public int getUniformLocation(int program, String name) {
        return 0;
    }

    @Override
    public int getAttribLocation(int program, String name) {
        return 0;
    }

    @Override
    public void bindAttribLocation(int program, int index, String name) {
    }

    @Override
    public String getShaderInfoLog(int shader) {
        return null;
    }

    @Override
    public String getProgramInfoLog(int program) {
        return null;
    }

    @Override
    public boolean isExtensionAvailable(String name) {
        return false;
    }

    protected void render(IRenderer renderer) {
        if (renderer.prepare(context)) {
            rendererExecutor.render(renderer);
        }
    }

    protected void drawSprite(SpriteRenderer spriteRenderer, VertexState v1, VertexState v2) {
        spriteRenderer.setVertex(v1, v2);
        render(spriteRenderer);
    }

    protected CachedTextureResampled getCachedTexture() {
        CachedTextureResampled cachedTexture = cachedTextures.get(bindTexture);
        if (cachedTexture != null) {
            cachedTexture.setClut();
        }
        return cachedTexture;
    }

    protected void drawArraysSprites(int first, int count) {
        CachedTextureResampled cachedTexture = getCachedTexture();
        SpriteRenderer spriteRenderer = new SpriteRenderer(context, cachedTexture, useVertexTexture);
        boolean readTexture = context.textureFlag.isEnabled() && !context.clearMode;
        Memory mem = Memory.getInstance();
        for (int i = first; i < count - 1; i += 2) {
            int addr1 = context.vinfo.getAddress(mem, i);
            int addr2 = context.vinfo.getAddress(mem, i + 1);
            context.vinfo.readVertex(mem, addr1, v1, readTexture);
            context.vinfo.readVertex(mem, addr2, v2, readTexture);
            drawSprite(spriteRenderer, v1, v2);
        }
    }

    protected void drawTriangle(TriangleRenderer triangleRenderer, VertexState v1, VertexState v2, VertexState v3, boolean invertedFrontFace) {
        triangleRenderer.setVertex(v1, v2, v3);
        if (!triangleRenderer.isCulled(invertedFrontFace)) {
            render(triangleRenderer);
        }
    }

    protected boolean isSprite(VertexInfo vinfo, VertexState tv1, VertexState tv2, VertexState tv3, VertexState tv4) {
        if (!vinfo.transform2D) {
            return false;
        }
        if (!context.clearMode && context.cullFaceFlag.isEnabled()) {
            return false;
        }
        if (vinfo.normal != 0) {
            return false;
        }
        if (context.textureColorDoubled) {
            return false;
        }
        if (vinfo.color != 0) {
            if (!Utilities.sameColor(tv1.c, tv2.c, tv3.c, tv4.c)) {
                return false;
            }
        }
        if (tv1.p[0] == tv2.p[0] && tv1.p[1] == tv3.p[1] && tv4.p[0] == tv3.p[0] && tv4.p[1] == tv2.p[1]) {
            if (tv1.p[2] == tv2.p[2] && tv1.p[2] == tv3.p[2] && tv1.p[2] == tv3.p[2]) {
                if (vinfo.texture == 0) {
                    return true;
                }
                if (tv1.t[0] == tv2.t[0] && tv1.t[1] == tv3.t[1] && tv4.t[0] == tv3.t[0] && tv4.t[1] == tv2.t[1]) {
                    return true;
                }
            }
        }
        if (tv1.p[1] == tv2.p[1] && tv1.p[0] == tv3.p[0] && tv4.p[1] == tv3.p[1] && tv4.p[0] == tv2.p[0]) {
            if (tv1.p[2] == tv2.p[2] && tv1.p[2] == tv3.p[2] && tv1.p[2] == tv3.p[2]) {
                if (vinfo.texture == 0) {
                    return true;
                }
                if (tv1.t[1] == tv2.t[1] && tv1.t[0] == tv3.t[0] && tv4.t[1] == tv3.t[1] && tv4.t[0] == tv2.t[0]) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void resetBufferVertexReader() {
        bufferVertexReader = null;
    }

    protected void readVertex(Memory mem, int index, VertexState v, boolean readTexture) {
        if (bufferVertexReader == null) {
            int addr = context.vinfo.getAddress(mem, index);
            context.vinfo.readVertex(mem, addr, v, readTexture);
        } else {
            bufferVertexReader.readVertex(index, v);
        }
        if (context.vinfo.weight != 0) {
            VideoEngine.doSkinning(context.bone_uploaded_matrix, context.vinfo, v);
        }
    }

    protected void drawArraysTriangleStrips(int first, int count) {
        Memory mem = Memory.getInstance();
        CachedTextureResampled cachedTexture = getCachedTexture();
        TriangleRenderer triangleRenderer = new TriangleRenderer(context, cachedTexture, useVertexTexture);
        SpriteRenderer spriteRenderer = null;
        VertexState tv1 = null;
        VertexState tv2 = null;
        VertexState tv3 = null;
        VertexState tv4 = v1;
        boolean readTexture = context.textureFlag.isEnabled() && !context.clearMode;
        for (int i = 0; i < count; i++) {
            readVertex(mem, first + i, tv4, readTexture);
            if (tv3 != null) {
                if (isSprite(context.vinfo, tv1, tv2, tv3, tv4)) {
                    if (spriteRenderer == null) {
                        spriteRenderer = new SpriteRenderer(context, cachedTexture, useVertexTexture);
                    }
                    drawSprite(spriteRenderer, tv1, tv4);
                    v5.copy(tv3);
                    v6.copy(tv4);
                    v1.copy(v5);
                    v2.copy(v6);
                    tv1 = v1;
                    tv2 = v2;
                    tv3 = null;
                    tv4 = v3;
                } else {
                    drawTriangle(triangleRenderer, tv1, tv2, tv3, ((i - 3) & 1) != 0);
                    VertexState v = tv1;
                    tv1 = tv2;
                    tv2 = tv3;
                    tv3 = tv4;
                    tv4 = v;
                }
            } else if (tv1 == null) {
                tv1 = tv4;
                tv4 = v2;
            } else if (tv2 == null) {
                tv2 = tv4;
                tv4 = v3;
            } else {
                tv3 = tv4;
                tv4 = v4;
            }
        }
        if (tv3 != null) {
            drawTriangle(triangleRenderer, tv1, tv2, tv3, (count & 1) == 0);
        }
    }

    protected void drawArraysTriangles(int first, int count) {
        Memory mem = Memory.getInstance();
        CachedTextureResampled cachedTexture = getCachedTexture();
        TriangleRenderer triangleRenderer = new TriangleRenderer(context, cachedTexture, useVertexTexture);
        boolean readTexture = context.textureFlag.isEnabled() && !context.clearMode;
        for (int i = 0; i < count; i += 3) {
            readVertex(mem, first + i, v1, readTexture);
            readVertex(mem, first + i + 1, v2, readTexture);
            readVertex(mem, first + i + 2, v3, readTexture);
            drawTriangle(triangleRenderer, v1, v2, v3, false);
        }
    }

    protected void drawArraysTriangleFan(int first, int count) {
        Memory mem = Memory.getInstance();
        CachedTextureResampled cachedTexture = getCachedTexture();
        TriangleRenderer triangleRenderer = new TriangleRenderer(context, cachedTexture, useVertexTexture);
        VertexState tv1 = null;
        VertexState tv2 = null;
        VertexState tv3 = v1;
        boolean readTexture = context.textureFlag.isEnabled() && !context.clearMode;
        for (int i = 0; i < count; i++) {
            readVertex(mem, first + i, tv3, readTexture);
            if (tv2 != null) {
                drawTriangle(triangleRenderer, tv1, tv2, tv3, false);
                VertexState v = tv2;
                tv2 = tv3;
                tv3 = v;
            } else if (tv1 == null) {
                tv1 = tv3;
                tv3 = v2;
            } else {
                tv2 = tv3;
                tv3 = v3;
            }
        }
    }

    @Override
    public void drawArrays(int primitive, int first, int count) {
        drawArraysStatistics.start();
        switch(primitive) {
            case IRenderingEngine.GU_SPRITES:
                drawArraysSprites(first, count);
                break;
            case IRenderingEngine.GU_TRIANGLE_STRIP:
                drawArraysTriangleStrips(first, count);
                break;
            case IRenderingEngine.GU_TRIANGLES:
                drawArraysTriangles(first, count);
                break;
            case IRenderingEngine.GU_TRIANGLE_FAN:
                drawArraysTriangleFan(first, count);
                break;
        }
        drawArraysStatistics.end();
    }

    @Override
    public int genBuffer() {
        return 0;
    }

    @Override
    public void deleteBuffer(int buffer) {
    }

    @Override
    public void setBufferData(int target, int size, Buffer buffer, int usage) {
    }

    @Override
    public void setBufferSubData(int target, int offset, int size, Buffer buffer) {
    }

    @Override
    public void bindBuffer(int target, int buffer) {
    }

    @Override
    public void enableClientState(int type) {
    }

    @Override
    public void disableClientState(int type) {
    }

    @Override
    public void enableVertexAttribArray(int id) {
    }

    @Override
    public void disableVertexAttribArray(int id) {
    }

    @Override
    public void setTexCoordPointer(int size, int type, int stride, long offset) {
    }

    @Override
    public void setTexCoordPointer(int size, int type, int stride, int bufferSize, Buffer buffer) {
        if (bufferVertexReader == null) {
            bufferVertexReader = new BufferVertexReader();
        }
        bufferVertexReader.setTextureComponentInfo(size, type, stride, bufferSize, buffer);
    }

    @Override
    public void setColorPointer(int size, int type, int stride, long offset) {
    }

    @Override
    public void setColorPointer(int size, int type, int stride, int bufferSize, Buffer buffer) {
        if (bufferVertexReader == null) {
            bufferVertexReader = new BufferVertexReader();
        }
        bufferVertexReader.setColorComponentInfo(size, type, stride, bufferSize, buffer);
    }

    @Override
    public void setVertexPointer(int size, int type, int stride, long offset) {
    }

    @Override
    public void setVertexPointer(int size, int type, int stride, int bufferSize, Buffer buffer) {
        if (bufferVertexReader == null) {
            bufferVertexReader = new BufferVertexReader();
        }
        bufferVertexReader.setVertexComponentInfo(size, type, stride, bufferSize, buffer);
    }

    @Override
    public void setNormalPointer(int type, int stride, long offset) {
    }

    @Override
    public void setNormalPointer(int type, int stride, int bufferSize, Buffer buffer) {
        if (bufferVertexReader == null) {
            bufferVertexReader = new BufferVertexReader();
        }
        bufferVertexReader.setNormalComponentInfo(type, stride, bufferSize, buffer);
    }

    @Override
    public void setWeightPointer(int size, int type, int stride, long offset) {
    }

    @Override
    public void setWeightPointer(int size, int type, int stride, int bufferSize, Buffer buffer) {
        if (bufferVertexReader == null) {
            bufferVertexReader = new BufferVertexReader();
        }
        bufferVertexReader.setWeightComponentInfo(size, type, stride, bufferSize, buffer);
    }

    @Override
    public void setVertexAttribPointer(int id, int size, int type, boolean normalized, int stride, long offset) {
    }

    @Override
    public void setVertexAttribPointer(int id, int size, int type, boolean normalized, int stride, int bufferSize, Buffer buffer) {
    }

    @Override
    public void setPixelStore(int rowLength, int alignment) {
        textureBufferWidth = rowLength;
    }

    @Override
    public int genTexture() {
        return genTextureId++;
    }

    @Override
    public void bindTexture(int texture) {
        bindTexture = texture;
    }

    @Override
    public void deleteTexture(int texture) {
        cachedTextures.remove(texture);
    }

    @Override
    public void setCompressedTexImage(int level, int internalFormat, int width, int height, int compressedSize, Buffer buffer) {
        if (useTextureCache) {
            cachedTextureStatistics.start();
            if (level == 0) {
                IMemoryReader imageReader = ImageReader.getImageReader(context.texture_base_pointer[level], width, height, context.texture_buffer_width[level], internalFormat, false, 0, 0, 0, 0, 0, 0, null, null);
                CachedTexture cachedTexture = CachedTexture.getCachedTexture(width, height, internalFormat, imageReader);
                CachedTextureResampled cachedTextureResampled = new CachedTextureResampled(cachedTexture);
                cachedTextures.put(bindTexture, cachedTextureResampled);
            }
            cachedTextureStatistics.end();
        }
    }

    @Override
    public void setTexImage(int level, int internalFormat, int width, int height, int format, int type, int textureSize, Buffer buffer) {
        if (useTextureCache) {
            cachedTextureStatistics.start();
            if (level == 0) {
                CachedTexture cachedTexture = null;
                if (buffer instanceof IntBuffer) {
                    cachedTexture = CachedTexture.getCachedTexture(textureBufferWidth, height, format, ((IntBuffer) buffer).array(), buffer.arrayOffset(), textureSize >> 2);
                } else if (buffer instanceof ShortBuffer) {
                    cachedTexture = CachedTexture.getCachedTexture(textureBufferWidth, height, format, ((ShortBuffer) buffer).array(), buffer.arrayOffset(), textureSize >> 1);
                }
                CachedTextureResampled cachedTextureResampled = new CachedTextureResampled(cachedTexture);
                cachedTextures.put(bindTexture, cachedTextureResampled);
            }
            cachedTextureStatistics.end();
        }
    }

    @Override
    public void setTexSubImage(int level, int xOffset, int yOffset, int width, int height, int format, int type, int textureSize, Buffer buffer) {
    }

    @Override
    public void getTexImage(int level, int format, int type, Buffer buffer) {
    }

    @Override
    public void copyTexSubImage(int level, int xOffset, int yOffset, int x, int y, int width, int height) {
    }

    @Override
    public void setStencilOp(int fail, int zfail, int zpass) {
    }

    @Override
    public void setStencilFunc(int func, int ref, int mask) {
    }

    @Override
    public void setAlphaFunc(int func, int ref) {
    }

    @Override
    public void setFogHint() {
    }

    @Override
    public void setFogColor(float[] color) {
    }

    @Override
    public void setFogDist(float start, float end) {
    }

    @Override
    public void setTextureEnvColor(float[] color) {
    }

    @Override
    public void setFrontFace(boolean cw) {
    }

    @Override
    public void setScissor(int x, int y, int width, int height) {
    }

    @Override
    public void setBlendEquation(int mode) {
    }

    @Override
    public void setLineSmoothHint() {
    }

    @Override
    public boolean hasBoundingBox() {
        return true;
    }

    @Override
    public void beginBoundingBox(int numberOfVertexBoundingBox) {
        boundingBoxRenderer = new BoundingBoxRenderer(context);
        boundingBoxVisible = true;
    }

    @Override
    public void drawBoundingBox(float[][] values) {
        if (boundingBoxVisible) {
            boundingBoxRenderer.drawBoundingBox(values);
            if (!boundingBoxRenderer.prepare(context)) {
                boundingBoxVisible = false;
            }
        }
    }

    @Override
    public void endBoundingBox(VertexInfo vinfo) {
    }

    @Override
    public boolean isBoundingBoxVisible() {
        return boundingBoxVisible;
    }

    @Override
    public int genQuery() {
        return 0;
    }

    @Override
    public void beginQuery(int id) {
    }

    @Override
    public void endQuery() {
    }

    @Override
    public boolean getQueryResultAvailable(int id) {
        return false;
    }

    @Override
    public int getQueryResult(int id) {
        return 0;
    }

    @Override
    public void clear(float red, float green, float blue, float alpha) {
    }

    @Override
    public boolean canAllNativeVertexInfo() {
        return true;
    }

    @Override
    public boolean canNativeSpritesPrimitive() {
        return true;
    }

    @Override
    public void setVertexInfo(VertexInfo vinfo, boolean allNativeVertexInfo, boolean useVertexColor, boolean useTexture, int type) {
        this.useVertexTexture = useTexture;
        resetBufferVertexReader();
    }

    @Override
    public void setProgramParameter(int program, int parameter, int value) {
    }

    @Override
    public boolean isQueryAvailable() {
        return false;
    }

    @Override
    public boolean isShaderAvailable() {
        return false;
    }

    @Override
    public int getUniformBlockIndex(int program, String name) {
        return 0;
    }

    @Override
    public void bindBufferBase(int target, int bindingPoint, int buffer) {
    }

    @Override
    public void setUniformBlockBinding(int program, int blockIndex, int bindingPoint) {
    }

    @Override
    public int getUniformIndex(int program, String name) {
        return 0;
    }

    @Override
    public int[] getUniformIndices(int program, String[] names) {
        return null;
    }

    @Override
    public int getActiveUniformOffset(int program, int uniformIndex) {
        return 0;
    }

    @Override
    public boolean isFramebufferObjectAvailable() {
        return false;
    }

    @Override
    public int genFramebuffer() {
        return 0;
    }

    @Override
    public int genRenderbuffer() {
        return 0;
    }

    @Override
    public void deleteFramebuffer(int framebuffer) {
    }

    @Override
    public void deleteRenderbuffer(int renderbuffer) {
    }

    @Override
    public void bindFramebuffer(int target, int framebuffer) {
    }

    @Override
    public void bindRenderbuffer(int renderbuffer) {
    }

    @Override
    public void setRenderbufferStorage(int internalFormat, int width, int height) {
    }

    @Override
    public void setFramebufferRenderbuffer(int target, int attachment, int renderbuffer) {
    }

    @Override
    public void setFramebufferTexture(int target, int attachment, int texture, int level) {
    }

    @Override
    public int genVertexArray() {
        return 0;
    }

    @Override
    public void bindVertexArray(int id) {
    }

    @Override
    public void deleteVertexArray(int id) {
    }

    @Override
    public boolean isVertexArrayAvailable() {
        return false;
    }

    @Override
    public void multiDrawArrays(int primitive, IntBuffer first, IntBuffer count) {
    }

    @Override
    public void drawArraysBurstMode(int primitive, int first, int count) {
    }

    @Override
    public void setPixelTransfer(int parameter, int value) {
    }

    @Override
    public void setPixelTransfer(int parameter, float value) {
    }

    @Override
    public void setPixelTransfer(int parameter, boolean value) {
    }

    @Override
    public void setPixelMap(int map, int mapSize, Buffer buffer) {
    }

    @Override
    public boolean canNativeClut(int textureAddress) {
        if (VideoEngine.isVRAM(textureAddress)) {
            return true;
        }
        return !useTextureCache;
    }

    @Override
    public void setActiveTexture(int index) {
    }

    @Override
    public void setTextureFormat(int pixelFormat, boolean swizzle) {
    }

    @Override
    public void bindActiveTexture(int index, int texture) {
    }

    @Override
    public void setTextureAnisotropy(float value) {
    }

    @Override
    public float getMaxTextureAnisotropy() {
        return 0;
    }

    @Override
    public String getShadingLanguageVersion() {
        return null;
    }

    @Override
    public void setBlendSFix(int sfix, float[] color) {
    }

    @Override
    public void setBlendDFix(int dfix, float[] color) {
    }

    @Override
    public void startClearMode(boolean color, boolean stencil, boolean depth) {
    }

    @Override
    public void endClearMode() {
    }

    @Override
    public void waitForRenderingCompletion() {
        rendererExecutor.waitForRenderingCompletion();
    }

    @Override
    public boolean canReadAllVertexInfo() {
        return true;
    }
}
