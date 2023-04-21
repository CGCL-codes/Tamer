package org.xith3d.render.jsr231;

import java.io.File;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.List;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import org.jagatoo.logging.ProfileTimer;
import org.openmali.types.twodee.Rect2i;
import org.xith3d.picking.PickRequest;
import org.xith3d.render.BackgroundRenderPass;
import org.xith3d.render.CanvasPeer;
import org.xith3d.render.Clipper;
import org.xith3d.render.ClipperInfo;
import org.xith3d.render.OpenGLCapabilities;
import org.xith3d.render.OpenGLStatesCache;
import org.xith3d.render.OpenGlExtensions;
import org.xith3d.render.RenderOptions;
import org.xith3d.render.RenderPass;
import org.xith3d.render.RenderPassConfig;
import org.xith3d.render.RenderPeer;
import org.xith3d.render.ScissorRect;
import org.xith3d.render.StateUnitPeerRegistry;
import org.xith3d.render.preprocessing.RenderAtom;
import org.xith3d.render.preprocessing.RenderBin;
import org.xith3d.render.preprocessing.RenderBinProvider;
import org.xith3d.scenegraph.RenderingAttributes;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.View;
import org.xith3d.scenegraph._SG_PrivilegedAccess;
import org.xith3d.scenegraph.View.CameraMode;
import org.xith3d.utility.logging.X3DLog;
import org.xith3d.utility.screenshots.ScreenshotCreator;
import com.jogamp.common.nio.Buffers;

/**
 * RenderPeer implementation base for JOGL (JSR-231).
 * 
 * @author David Yazel
 * @author Lilian Chamontin [jsr231 port]
 * @author Marvin Froehlich (aka Qudus)
 * @author Julien Gouesse [JOGL 2.0 port]
 */
class RenderPeerImpl extends RenderPeer {

    private GL gl;

    private final FloatBuffer openGLProjMatBuffer = Buffers.newDirectFloatBuffer(16);

    private final DoubleBuffer planeBuffer = Buffers.newDirectDoubleBuffer(4);

    private final FloatBuffer defaultLightColorBuffer = Buffers.newDirectFloatBuffer(4);

    private final FloatBuffer pickMBuffer = Buffers.newDirectFloatBuffer(16);

    private float[] openGLProjMatrix = new float[16];

    private ScissorRect lastScissorBox = null;

    private int lastClipperId = -1;

    private int activeClipperId = -1;

    private RenderOptions effectiveRenderOptions = new RenderOptions();

    private float[] defaultLightColorArray = { 0.2f, 0.2f, 0.2f, 1.0f };

    private final RenderTargetPeer renderTargetPeer = new RenderTargetPeer(this);

    private final ShadowRenderPeer shadowPeer = new ShadowRenderPeer(this);

    private ScreenshotCreator scheduledShotCreator = null;

    private ScreenshotCreator shotCreator = null;

    public RenderPeerImpl(CanvasPeerImplBase canvasPeer, StateUnitPeerRegistry shaderRegistry, OpenGLStatesCache statesCache, RenderOptions renderOptions) {
        super(canvasPeer, shaderRegistry, statesCache, renderOptions);
    }

    public RenderPeerImpl(CanvasPeerImplBase canvasPeer, StateUnitPeerRegistry shaderRegistry, OpenGLStatesCache statesCache) {
        this(canvasPeer, shaderRegistry, statesCache, new RenderOptions());
    }

    protected static void setGCRequested() {
        setGCRequested(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setCanvasPeer(CanvasPeer canvasPeer) {
        super.setCanvasPeer(canvasPeer);
    }

    public CanvasPeerImplBase getCanvasPeerBase() {
        return ((CanvasPeerImplBase) getCanvasPeer());
    }

    public GL getGL() {
        return (gl);
    }

    public final void setDefaultLightColor(float r, float g, float b, float a) {
        defaultLightColorArray[0] = r;
        defaultLightColorArray[1] = g;
        defaultLightColorArray[2] = b;
        defaultLightColorArray[3] = a;
    }

    public final float[] getClearColor() {
        return (clearColor);
    }

    @SuppressWarnings("unused")
    private final void drawFloor(GL gl, OpenGLStatesCache statesCache) {
        gl.getGL2().glMatrixMode(GL2.GL_MODELVIEW);
        gl.getGL2().glPushMatrix();
        gl.getGL2().glLoadIdentity();
        gl.glDisable(GL.GL_CULL_FACE);
        statesCache.cullFaceEnabled = false;
        gl.glDisable(GL2.GL_LIGHTING);
        statesCache.lightingEnabled = false;
        gl.glDisable(GL.GL_TEXTURE_2D);
        statesCache.texture2DEnabled[statesCache.currentServerTextureUnit] = false;
        gl.glDisable(GL2.GL_TEXTURE_3D);
        statesCache.texture3DEnabled[statesCache.currentServerTextureUnit] = false;
        gl.glDisable(GL.GL_BLEND);
        statesCache.blendingEnabled = false;
        gl.glDepthMask(false);
        gl.getGL2().glColor3f(0.2f, 0.2f, 0.7f);
        gl.getGL2().glBegin(GL2.GL_QUADS);
        gl.getGL2().glNormal3f(0.0f, 1.0f, 0.0f);
        gl.getGL2().glVertex3f(-200.0f, 0f, -200.0f);
        gl.getGL2().glVertex3f(-200.0f, 0f, +200.0f);
        gl.getGL2().glVertex3f(+200.0f, 0f, +200.0f);
        gl.getGL2().glVertex3f(+200.0f, 0f, -200.0f);
        gl.getGL2().glEnd();
        gl.getGL2().glPopMatrix();
        gl.glDepthMask(statesCache.depthWriteMask);
    }

    private final void finishScissors(GL gl, OpenGLStatesCache statesCache) {
        {
            if (!statesCache.enabled || statesCache.scissorTestEnabled) {
                gl.glDisable(GL.GL_SCISSOR_TEST);
                statesCache.scissorTestEnabled = false;
            }
        }
        lastScissorBox = null;
    }

    private final void startScissors(GL gl, OpenGLStatesCache statesCache, ScissorRect scissorBox) {
        if ((scissorBox == null) && ((lastScissorBox != null) || (gl.glIsEnabled(GL.GL_SCISSOR_TEST)))) {
            finishScissors(gl, statesCache);
            return;
        }
        if (scissorBox != null) {
            final boolean ok;
            if (!scissorBox.equals(lastScissorBox)) {
                scissorBox.clamp(getCanvasPeerBase().getCurrentViewport());
                ok = scissorBox.check(getCanvasPeerBase().getCurrentViewport());
                if (ok) gl.glScissor(scissorBox.getX(), scissorBox.getY(), scissorBox.getWidth(), scissorBox.getHeight());
            } else ok = true;
            if (ok && ((lastScissorBox == null) || (!statesCache.enabled || !statesCache.scissorTestEnabled))) {
                gl.glEnable(GL.GL_SCISSOR_TEST);
                statesCache.scissorTestEnabled = true;
            }
            lastScissorBox = scissorBox;
        }
    }

    public static final int translateClipPlaneIndex(int index) {
        switch(index) {
            case 0:
                return (GL2.GL_CLIP_PLANE0);
            case 1:
                return (GL2.GL_CLIP_PLANE1);
            case 2:
                return (GL2.GL_CLIP_PLANE2);
            case 3:
                return (GL2.GL_CLIP_PLANE3);
            case 4:
                return (GL2.GL_CLIP_PLANE4);
            case 5:
                return (GL2.GL_CLIP_PLANE5);
            default:
                throw new Error("unknown clip plane " + index);
        }
    }

    private final void finishClipper(GL gl, OpenGLStatesCache statesCache) {
        for (int i = 0; i < 6; i++) {
            if (statesCache.clipPlaneEnabled[i]) {
                gl.glDisable(translateClipPlaneIndex(i));
                statesCache.clipPlaneEnabled[i] = false;
            }
        }
        activeClipperId = -1;
    }

    private final void startClipper(GL gl, OpenGLStatesCache statesCache, ClipperInfo info, View view) {
        final Clipper clipper = (info == null) ? null : info.getClipper();
        if ((clipper != null) && (clipper.isEnabled())) {
            if (clipper.getId() == activeClipperId) return;
            activeClipperId = clipper.getId();
            if (lastClipperId != clipper.getId()) {
                gl.getGL2().glPushMatrix();
                gl.getGL2().glLoadMatrixf(_SG_PrivilegedAccess.getFloatBuffer(view.getModelViewTransform(false), true));
                if (!clipper.isWorldCoordinateSystemUsed()) {
                    final Transform3D modelView = info.getModelView();
                    if (modelView != null) {
                        float[] matrix = new float[16];
                        modelView.getColumnMajor(matrix);
                        gl.getGL2().glMultMatrixf(matrix, 0);
                    }
                }
            }
            for (int i = 0; i < 6; i++) {
                final int glI = translateClipPlaneIndex(i);
                if (clipper.isPlaneEnabled(i)) {
                    if (lastClipperId != clipper.getId()) {
                        planeBuffer.clear();
                        planeBuffer.put(clipper.getPlane(i).getA());
                        planeBuffer.put(clipper.getPlane(i).getB());
                        planeBuffer.put(clipper.getPlane(i).getC());
                        planeBuffer.put(clipper.getPlane(i).getD());
                        planeBuffer.rewind();
                        gl.getGL2().glClipPlane(glI, planeBuffer);
                    }
                    if (!statesCache.enabled || !statesCache.clipPlaneEnabled[i]) {
                        gl.glEnable(glI);
                        statesCache.clipPlaneEnabled[i] = true;
                    }
                } else {
                    if (!statesCache.enabled || statesCache.clipPlaneEnabled[i]) {
                        gl.glDisable(glI);
                        statesCache.clipPlaneEnabled[i] = false;
                    }
                }
            }
            if (lastClipperId != clipper.getId()) {
                gl.getGL2().glPopMatrix();
                lastClipperId = clipper.getId();
            }
        } else {
            finishClipper(gl, statesCache);
        }
    }

    private final int drawBin(GL gl, OpenGLStatesCache statesCache, OpenGLCapabilities glCaps, RenderOptions options, boolean isScissorEnabled, boolean isClipperEnabled, RenderBin bin, View view, long frameId, int nameOffset, long nanoTime, long nanoStep, RenderMode renderMode) {
        final CanvasPeer canvasPeer = getCanvasPeer();
        int triangles = 0;
        final int n = bin.size();
        for (int i = 0; i < n; i++) {
            RenderAtom<?> atom = bin.getAtom(i);
            try {
                if (renderMode == RenderMode.PICKING) gl.getGL2().glPushName(nameOffset + i);
                if (isScissorEnabled) startScissors(gl, statesCache, atom.getScissorRect());
                if (isClipperEnabled) startClipper(gl, statesCache, atom.getClipper(), view);
                triangles += this.renderAtom(atom, gl, canvasPeer, glCaps, statesCache, view, options, nanoTime, nanoStep, renderMode, frameId);
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(0);
            } finally {
                if (renderMode == RenderMode.PICKING) {
                    gl.getGL2().glPopName();
                }
            }
        }
        if (isClipperEnabled) finishClipper(gl, statesCache);
        return (triangles);
    }

    private final void setColorMask(GL gl, int colorMask, OpenGLStatesCache statesCache) {
        if (statesCache.enabled && colorMask == statesCache.colorWriteMask) return;
        gl.glColorMask((colorMask & 1) != 0, (colorMask & 2) != 0, (colorMask & 4) != 0, (colorMask & 8) != 0);
        statesCache.colorWriteMask = colorMask;
    }

    /**
     * setup the frame
     */
    private final void renderStart(GL gl, OpenGLStatesCache statesCache, int atomsCount, PickRequest pickRequest) {
        super.renderStart(pickRequest);
        getCanvasPeerBase().beforeRenderStart(pickRequest, forceNoSwap);
        if (pickRequest == null) {
            selectBuffer = null;
            gl.getGL2().glRenderMode(GL2.GL_RENDER);
            gl.glClearDepth(1.0f);
            if ((shotCreator != null) && (shotCreator.getFormat() == ScreenshotCreator.Format.RGBA)) gl.glClearColor(clearColor[0], clearColor[1], clearColor[2], 1f); else gl.glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
            lastScissorBox = null;
            if (!statesCache.enabled || statesCache.scissorTestEnabled) {
                gl.glDisable(GL.GL_SCISSOR_TEST);
                statesCache.scissorTestEnabled = false;
            }
            if (!disableClearBuffer) {
                if (fullOverpaint) gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT); else gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT);
            }
        } else {
            gl.getGL2().glRenderMode(GL2.GL_RENDER);
            gl.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
            gl.glClearDepth(1.0f);
            gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT);
            final int selectBufferCapacity = atomsCount * 4;
            selectBuffer = Buffers.newDirectIntBuffer(selectBufferCapacity);
            gl.getGL2().glSelectBuffer(selectBufferCapacity, selectBuffer);
            gl.getGL2().glRenderMode(GL2.GL_SELECT);
            gl.getGL2().glInitNames();
        }
        gl.glDepthFunc(GL.GL_LESS);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
        gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);
        gl.getGL2().glShadeModel(GL2.GL_SMOOTH);
        defaultLightColorBuffer.put(defaultLightColorArray);
        defaultLightColorBuffer.rewind();
        gl.getGL2().glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, defaultLightColorBuffer);
        if (OpenGlExtensions.GL_EXT_separate_specular_color) gl.getGL2().glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SEPARATE_SPECULAR_COLOR);
        gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
    }

    /**
     * Our own functionally equivalent implementation of Mesa-style gluPickMatrix.
     * We need this because of at least on some installations of Linux glu.gluPickMatrix(...) fails
     * with unknown error (Unexpected signal 11) and JVM exits.
     */
    private final void gluPickMatrix(GL gl, float x, float y, float width, float height, int[] viewport) {
        final float[] m = new float[16];
        final float sx, sy;
        final float tx, ty;
        sx = viewport[2] / width;
        sy = viewport[3] / height;
        tx = (viewport[2] + 2.0f * (viewport[0] - x)) / width;
        ty = (viewport[3] + 2.0f * (viewport[1] - y)) / height;
        m[0 + 0 * 4] = sx;
        m[0 + 1 * 4] = 0.0f;
        m[0 + 2 * 4] = 0.0f;
        m[0 + 3 * 4] = tx;
        m[1 + 0 * 4] = 0.0f;
        m[1 + 1 * 4] = sy;
        m[1 + 2 * 4] = 0.0f;
        m[1 + 3 * 4] = ty;
        m[2 + 0 * 4] = 0.0f;
        m[2 + 1 * 4] = 0.0f;
        m[2 + 2 * 4] = 1.0f;
        m[2 + 3 * 4] = 0.0f;
        m[3 + 0 * 4] = 0.0f;
        m[3 + 1 * 4] = 0.0f;
        m[3 + 2 * 4] = 0.0f;
        m[3 + 3 * 4] = 1.0f;
        pickMBuffer.put(m);
        pickMBuffer.rewind();
        gl.getGL2().glMultMatrixf(pickMBuffer);
    }

    /**
     * Initializes the OpenGL view
     */
    private final void renderStartView(GL gl, View view, PickRequest pickRequest) {
        gl.getGL2().glMatrixMode(GL2.GL_PROJECTION);
        gl.getGL2().glLoadIdentity();
        if (pickRequest != null) {
            final Rect2i currentViewport = getCanvasPeerBase().getCurrentViewport();
            final int y = getCanvasPeer().getHeight() - currentViewport.getTop() - currentViewport.getHeight();
            final int[] viewport = new int[] { currentViewport.getLeft(), y, currentViewport.getWidth(), currentViewport.getHeight() };
            gluPickMatrix(gl, pickRequest.getMouseX(), viewport[3] - pickRequest.getMouseY(), 1, 1, viewport);
        }
        view.getProjection().getColumnMajor(openGLProjMatrix);
        openGLProjMatBuffer.put(openGLProjMatrix);
        openGLProjMatBuffer.rewind();
        gl.getGL2().glMultMatrixf(openGLProjMatBuffer);
        gl.getGL2().glMatrixMode(GL2.GL_MODELVIEW);
    }

    /**
     * Sets the current GL matrix to the view adjusting it depending
     * on the mode.
     * @param view
     * @param mode VIEW_NORMAL will use the standard view, VIEW_FIXED_POSITION
     *             will use only the rotational component of the standard view
     *             (the position is left as the identity), VIEW_FIXED sets the view
     *             to the identity matrix
     */
    private final void setGLModelViewMatrix(GL gl, View view, View.CameraMode mode) {
        if (mode == null) mode = CameraMode.VIEW_NORMAL;
        gl.getGL2().glLoadMatrixf(_SG_PrivilegedAccess.getFloatBuffer(view.getModelViewTransform(mode, true), true));
    }

    /**
     * Render the frame using the definition provided in the atomsCollector object.
     * 
     * @param gl
     * @throws Throwable
     */
    private final int renderMain(GL gl, OpenGLCapabilities glCaps, OpenGLStatesCache statesCache, RenderOptions options, boolean isScissorEnabled, boolean isClipperEnabled, View view, RenderPass renderPass, long frameId, int nameOffset, long nanoTime, long nanoStep, RenderMode renderMode) throws Throwable {
        X3DLog.debug("Rendering opaque and transparent bin");
        int triangles = 0;
        ProfileTimer.startProfile(X3DLog.LOG_CHANNEL, "CanvasPeerImpl::Drawing Main Scene");
        final RenderBinProvider binProvider = renderPass.getRenderBinProvider();
        if (binProvider.getOpaqueBin().size() > 0) {
            if (!statesCache.enabled || !statesCache.depthTestEnabled) {
                gl.glEnable(GL.GL_DEPTH_TEST);
                statesCache.depthTestEnabled = true;
            }
            triangles += drawBin(gl, statesCache, glCaps, options, isScissorEnabled, isClipperEnabled, binProvider.getOpaqueBin(), view, frameId, nameOffset, nanoTime, nanoStep, renderMode);
            nameOffset += binProvider.getOpaqueBin().size();
        }
        if (binProvider.getTransparentBin().size() > 0) {
            triangles += drawBin(gl, statesCache, glCaps, options, isScissorEnabled, isClipperEnabled, binProvider.getTransparentBin(), view, frameId, nameOffset, nanoTime, nanoStep, renderMode);
            nameOffset += binProvider.getTransparentBin().size();
        }
        ProfileTimer.endProfile();
        if ((renderMode == RenderMode.PICKING)) {
            if (!statesCache.enabled || !statesCache.depthTestEnabled) {
                gl.glEnable(GL.GL_DEPTH_TEST);
                statesCache.depthTestEnabled = true;
            }
        }
        if ((renderMode != RenderMode.PICKING) && ((shotCreator == null) || (shotCreator.getFormat() == ScreenshotCreator.Format.RGB))) {
            triangles += shadowPeer.drawShadows(gl, view, renderPass.getShadowCasterLight(), binProvider.getShadowsBin(), frameId);
        }
        getCanvasPeer().addTriangles(triangles);
        return (nameOffset);
    }

    /**
     * Stop picking mode and convert select buffer to PickRenderResult[] in addition to the default renderDone() action
     */
    private final Object renderDone(GL gl, List<RenderPass> renderPasses, PickRequest pickRequest, OpenGLStatesCache statesCache, long frameId) {
        if (!statesCache.enabled || !statesCache.depthWriteMask) {
            gl.glDepthMask(true);
            statesCache.depthWriteMask = true;
        }
        if (!statesCache.enabled || statesCache.colorWriteMask != RenderingAttributes.COLOR_WRITE_MASK_ALL) {
            gl.glColorMask(true, true, true, true);
            statesCache.colorWriteMask = RenderingAttributes.COLOR_WRITE_MASK_ALL;
        }
        super.renderDone(frameId);
        if (pickRequest != null) {
            gl.glFlush();
            return (convertSelectBuffer(gl.getGL2().glRenderMode(GL2.GL_RENDER), renderPasses, pickRequest.getPickAll()));
        }
        return (null);
    }

    public final int renderRenderPass(Object glObj, OpenGLCapabilities glCaps, OpenGLStatesCache statesCache, List<RenderPass> renderPasses, RenderPass renderPass, final int rpIndex, boolean isRenderTargetMode, RenderMode renderMode, View view, boolean layeredMode, long frameId, long nanoTime, long nanoStep, PickRequest pickRequest, int nameOffset) throws Throwable {
        final RenderPassConfig passConfig = renderPass.getConfig();
        if (pickRequest == null) {
            renderPass.getRenderCallbackNotifier().notifyBeforeRenderPassIsRendered(renderPass, getCanvasPeer().getType(), glObj);
        }
        if ((passConfig == null) || (passConfig.getColorMask() == -1)) setColorMask(gl, colorMask, statesCache); else setColorMask(gl, passConfig.getColorMask(), statesCache);
        _SG_PrivilegedAccess.set(view, true, passConfig);
        setGLModelViewMatrix(gl, view, (passConfig == null) ? null : passConfig.getCameraMode());
        if (renderPass.isEnabled()) {
            if (passConfig != null) {
                if (passConfig.getRenderOptions() != null) effectiveRenderOptions.loadOptions(passConfig.getRenderOptions()); else effectiveRenderOptions.loadOptions(this.getRenderOptions());
                if (isRenderTargetMode && (renderMode != RenderMode.SHADOW_MAP_GENERATION)) getCanvasPeerBase().updateViewport(gl, passConfig.getViewport());
            } else {
                effectiveRenderOptions.loadOptions(this.getRenderOptions());
                if (isRenderTargetMode && (renderMode != RenderMode.SHADOW_MAP_GENERATION)) getCanvasPeerBase().updateViewport(gl, null);
            }
            statesCache.enabled = effectiveRenderOptions.isGLStatesCacheEnabled();
            view.getFrustum((getCanvasPeerBase().getCurrentViewport() == null) ? getCanvasPeer().getCanvas3D() : getCanvasPeerBase().getCurrentViewport());
            if (isRenderTargetMode && (renderPass.getRenderTarget() != null)) {
                renderPass.getRenderCallbackNotifier().notifyBeforeRenderTargetIsActivated(renderPass, renderPass.getRenderTarget(), getCanvasPeer().getType(), glObj);
                renderTargetPeer.setupRenderTarget(gl, glCaps, statesCache, getCanvasPeer(), renderPass.getRenderTarget());
                renderPass.getRenderCallbackNotifier().notifyAfterRenderTargetIsActivated(renderPass, renderPass.getRenderTarget(), getCanvasPeer().getType(), glObj);
            }
            renderStartView(gl, view, pickRequest);
            if (isRenderTargetMode && (renderPass.getRenderTarget() != null) && renderPass.getRenderTarget().isBackgroundRenderingEnabled()) {
                for (int i = 0; i < renderPasses.size(); i++) {
                    final RenderPass bgPass = renderPasses.get(i);
                    if (bgPass instanceof BackgroundRenderPass) {
                        nameOffset += renderRenderPass(glObj, glCaps, statesCache, renderPasses, bgPass, i, false, renderMode, view, layeredMode, frameId, nanoTime, nanoStep, pickRequest, nameOffset);
                    }
                }
                gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
                setGLModelViewMatrix(gl, view, passConfig.getCameraMode());
                view.getFrustum(getCanvasPeerBase().getCurrentViewport());
                renderStartView(gl, view, pickRequest);
            }
            if ((layeredMode && !renderPass.isUnlayeredModeForced()) || (!layeredMode && renderPass.isLayeredModeForced())) {
                if (rpIndex > 0) gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
            }
            if (pickRequest == null) {
                renderPass.getRenderCallbackNotifier().notifyAfterRenderPassIsSetUp(renderPass, getCanvasPeer().getType(), glObj);
            }
            nameOffset += renderMain(gl, glCaps, statesCache, effectiveRenderOptions, renderPass.isScissorEnabled(), renderPass.isClipperEnabled(), view, renderPass, frameId, nameOffset, nanoTime, nanoStep, (pickRequest != null) ? RenderMode.PICKING : RenderMode.NORMAL);
            if (isRenderTargetMode && (renderPass.getRenderTarget() != null)) {
                renderPass.getRenderCallbackNotifier().notifyBeforeRenderTargetIsDeactivated(renderPass, renderPass.getRenderTarget(), getCanvasPeer().getType(), glObj);
                renderTargetPeer.finishRenderTarget(gl, renderPass.getRenderTarget());
                renderPass.getRenderCallbackNotifier().notifyAfterRenderTargetIsDeactivated(renderPass, renderPass.getRenderTarget(), getCanvasPeer().getType(), glObj);
            }
        } else if (pickRequest != null) {
            nameOffset += renderPass.getRenderBinProvider().getAtomsCount();
        }
        if ((renderMode == RenderMode.SHADOW_MAP_GENERATION) || ((passConfig != null) && (passConfig.getRenderOptions() != null)) || ((rpIndex + 1 < renderPasses.size()) && (renderPasses.get(rpIndex + 1).getConfig() != null) && (renderPasses.get(rpIndex + 1).getConfig().getRenderOptions() != null))) {
            super.renderDone(frameId);
            super.resetStateUnitStateArrays();
        }
        _SG_PrivilegedAccess.set(view, false, (RenderPassConfig) null);
        if (pickRequest == null) {
            renderPass.getRenderCallbackNotifier().notifyAfterRenderPassCompleted(renderPass, getCanvasPeer().getType(), glObj);
        }
        return (nameOffset);
    }

    /**
     * Does the actual rendering. OpenGL context threading issues must be handled/solved earlier.
     */
    @Override
    public final Object render(Object glObj, View view, List<RenderPass> renderPasses, boolean layeredMode, long frameId, long nanoTime, long nanoStep, PickRequest pickRequest) {
        if (view == null) {
            return (null);
        }
        this.gl = (GL) glObj;
        lastClipperId = -1;
        shotCreator = scheduledShotCreator;
        scheduledShotCreator = null;
        final OpenGLCapabilities glCaps = getCanvasPeer().getOpenGLCapabilities();
        final OpenGLStatesCache statesCache = getStatesCache();
        statesCache.enabled = getRenderOptions().isGLStatesCacheEnabled();
        Object result = null;
        ShapeAtomPeer.reset();
        X3DLog.debug("Starting to render the frame");
        try {
            if ((renderPasses != null) && (renderPasses.size() > 0)) {
                final RenderBinProvider firstBinProvider = renderPasses.get(0).getRenderBinProvider();
                renderStart(gl, statesCache, firstBinProvider.getAtomsCount(), pickRequest);
                int nameOffset = 0;
                for (int i = 0; i < renderPasses.size(); i++) {
                    final RenderPass renderPass = renderPasses.get(i);
                    if (renderPass.isEnabled()) {
                        if ((shotCreator == null) || (shotCreator.getFormat() == ScreenshotCreator.Format.RGB)) {
                            nameOffset += shadowPeer.initShadows(gl, view, renderPass.getShadowCasterLight(), renderPass.getRenderBinProvider().getShadowsBin(), frameId);
                        }
                        nameOffset += renderRenderPass(glObj, glCaps, statesCache, renderPasses, renderPass, i, true, (pickRequest == null) ? RenderMode.NORMAL : RenderMode.PICKING, view, layeredMode, frameId, nanoTime, nanoStep, pickRequest, nameOffset);
                    }
                }
                renderDone(gl, renderPasses, pickRequest, statesCache, frameId);
                if (shotCreator != null) {
                    gl.getGL2().glReadBuffer(GL.GL_FRONT);
                    gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);
                    final int offset;
                    if (shotCreator.getFormat() == ScreenshotCreator.Format.RGBA) offset = 0; else offset = 0;
                    gl.glReadPixels(0, offset, getCanvasPeer().getWidth(), getCanvasPeer().getHeight(), shotCreator.getFormat().getIntGL(), GL.GL_UNSIGNED_BYTE, shotCreator.getBuffer());
                    shotCreator.createScreenshot();
                    shotCreator = null;
                }
            }
        } catch (Throwable terr) {
            X3DLog.print(terr);
            terr.printStackTrace();
            throw new Error(terr);
        }
        checkGCRequested();
        X3DLog.debug("Done rendering the frame");
        return (result);
    }

    public void clearViewport(GL gl, float r, float g, float b, float a) {
        gl.glClearColor(r, g, b, a);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
    }

    public void clearViewport(GL gl) {
        clearViewport(gl, clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
    }

    /**
     * Takes a screenshot of the current rendering
     * 
     * @param file the file to save the screenshot to
     * @param alpha with alpha channel?
     */
    @Override
    public final void takeScreenshot(File file, boolean alpha) {
        ScreenshotCreator.Format format;
        if (alpha) format = ScreenshotCreator.Format.RGBA; else format = ScreenshotCreator.Format.RGB;
        this.scheduledShotCreator = new ScreenshotCreator(getCanvasPeer().getWidth(), getCanvasPeer().getHeight(), format, file);
    }
}
