package org.xith3d.render.lwjgl;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.jagatoo.opengl.enums.TextureFormat;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.openmali.types.twodee.Rect2i;
import org.openmali.vecmath2.Colorf;
import org.xith3d.render.CanvasPeer;
import org.xith3d.render.ImageRenderTarget;
import org.xith3d.render.OpenGLCapabilities;
import org.xith3d.render.OpenGLStatesCache;
import org.xith3d.render.RenderTarget;
import org.xith3d.render.SceneGraphOpenGLReference;
import org.xith3d.render.SceneGraphOpenGLReferences;
import org.xith3d.render.TextureRenderTarget;
import org.xith3d.scenegraph.Texture;
import org.xith3d.scenegraph.Texture2D;

/**
 * This class handles the rendering of {@link RenderTarget}s.
 * 
 * @author Marvin Froehlich (aka Qudus)
 * @author Mathias Henze (aka cylab)
 */
public class RenderTargetPeer {

    private final IntBuffer intBuffer = BufferUtils.createIntBuffer(1);

    private final RenderPeerImpl renderPeer;

    private final SceneGraphOpenGLReferences.Provider frameBufferNameProvider = new SceneGraphOpenGLReferences.Provider() {

        public SceneGraphOpenGLReference newReference(CanvasPeer canvasPeer, SceneGraphOpenGLReferences references, int numNamesPerContext) {
            return (new SceneGraphOpenGLReference(canvasPeer, references, numNamesPerContext) {

                @Override
                public void prepareObjectForDestroy() {
                    SceneGraphOpenGLReference ref = getReferences().removeReference(getContext().getCanvasID());
                    ((CanvasPeerImplBase) getContext()).addDestroyableObject(ref);
                }

                @Override
                public void destroyObject(int index, int name) {
                    intBuffer.clear();
                    intBuffer.put(name).rewind();
                    if (index == 0) {
                        EXTFramebufferObject.glDeleteFramebuffersEXT(intBuffer);
                    } else if (index > 0) {
                        EXTFramebufferObject.glDeleteRenderbuffersEXT(intBuffer);
                    }
                }
            });
        }
    };

    private final void setupRenderTarget(OpenGLCapabilities glCaps, OpenGLStatesCache statesCache, CanvasPeer canvasPeer, TextureRenderTarget renderTarget) {
        final SceneGraphOpenGLReference openGLRef = renderTarget.getOpenGLReferences().getReference(canvasPeer, frameBufferNameProvider);
        final Texture texture = renderTarget.getTexture();
        final int texWidth = texture.getWidth();
        final int texHeight = texture.getHeight();
        if ((texWidth <= 0) || (texHeight <= 0)) {
            throw new Error("The Texture \"" + texture.getName() + "\" of TextureRenderTarget \"" + renderTarget + "\" doesn't seem to have an InputComponent.");
        }
        if (!openGLRef.nameExists(0)) {
            intBuffer.clear();
            EXTFramebufferObject.glGenFramebuffersEXT(intBuffer);
            openGLRef.setName(0, intBuffer.get(0));
        }
        final int fb = openGLRef.getName(0);
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fb);
        final int tex = TextureUnitStateUnitPeer.setTextureState2(glCaps, statesCache, texture, statesCache.currentServerTextureUnit, texture.isChanged2(), canvasPeer, renderPeer.getCanvasPeer().getDepthBufferSize());
        if (texture.getFormat() == TextureFormat.DEPTH) {
            EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, GL11.GL_TEXTURE_2D, tex, 0);
            GL11.glDrawBuffer(GL11.GL_NONE);
            GL11.glReadBuffer(GL11.GL_NONE);
        } else {
            if (!openGLRef.nameExists(1)) {
                intBuffer.clear();
                EXTFramebufferObject.glGenRenderbuffersEXT(intBuffer);
                openGLRef.setName(1, intBuffer.get(0));
            }
            final int rb = openGLRef.getName(1);
            EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, rb);
            EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, texWidth, texHeight);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, rb);
            EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, tex, 0);
        }
        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
        GL11.glViewport(0, 0, texWidth, texHeight);
        final Colorf bgCol = renderTarget.getBackgroundColor();
        if (bgCol != null) {
            GL11.glClearColor(bgCol.getRed(), bgCol.getGreen(), bgCol.getBlue(), 1f - bgCol.getAlpha());
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
            final float[] clearColor = renderPeer.getClearColor();
            GL11.glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
        }
    }

    /**
     * 
     * @param renderTarget
     */
    private final void finishRenderTarget(TextureRenderTarget renderTarget) {
        if (renderTarget.isPixelReadbackEnabled()) {
            Texture2D texture = (Texture2D) renderTarget.getTexture();
            ByteBuffer bb = texture.getImage0().getDataBuffer();
            if (bb != null) {
                if (texture.getFormat() == TextureFormat.DEPTH) {
                    GL11.glReadPixels(0, 0, texture.getWidth(), texture.getHeight(), GL11.GL_DEPTH_COMPONENT, GL11.GL_UNSIGNED_BYTE, bb);
                } else {
                    GL11.glReadPixels(0, 0, texture.getWidth(), texture.getHeight(), texture.getFormat().hasAlpha() ? GL11.GL_RGBA : GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, bb);
                }
            }
        }
        GL11.glPopAttrib();
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
    }

    private final void setupRenderTarget(ImageRenderTarget renderTarget) {
        final Rect2i viewport = ((CanvasPeerImplBase) renderPeer.getCanvasPeer()).getCurrentViewport();
        BufferedImage image = renderTarget.getImage();
        if ((image == null) || (image.getWidth() != viewport.getWidth()) || (image.getHeight() != viewport.getHeight())) {
            renderTarget.setImage(new BufferedImage(viewport.getWidth(), viewport.getHeight(), BufferedImage.TYPE_INT_ARGB));
        }
    }

    private final void finishRenderTarget(ImageRenderTarget renderTarget) {
        final BufferedImage image = renderTarget.getImage();
        GL11.glReadPixels(0, 0, image.getWidth(), image.getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, renderTarget.getByteBuffer());
        renderTarget.copyBufferToImage();
    }

    public final void setupRenderTarget(OpenGLCapabilities glCaps, OpenGLStatesCache statesCache, CanvasPeer canvasPeer, RenderTarget renderTarget) {
        if (renderTarget instanceof TextureRenderTarget) {
            setupRenderTarget(glCaps, statesCache, canvasPeer, (TextureRenderTarget) renderTarget);
        } else if (renderTarget instanceof ImageRenderTarget) {
            setupRenderTarget((ImageRenderTarget) renderTarget);
        }
    }

    public final void finishRenderTarget(RenderTarget renderTarget) {
        if (renderTarget instanceof TextureRenderTarget) {
            finishRenderTarget((TextureRenderTarget) renderTarget);
        } else if (renderTarget instanceof ImageRenderTarget) {
            finishRenderTarget((ImageRenderTarget) renderTarget);
        }
    }

    public RenderTargetPeer(RenderPeerImpl renderPeer) {
        this.renderPeer = renderPeer;
    }
}
