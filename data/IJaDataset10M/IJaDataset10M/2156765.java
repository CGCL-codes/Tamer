package jocode;

import java.nio.*;
import javax.media.opengl.*;

/**
 *  Wrap the functionality of a FrameBufferObject.  A FrameBufferObject is an
 *  offscreen canvas that can be rendered to just like the screen, but can
 *  be any dimensions.  When the FBO is activated, gl rendering commands will
 *  draw into the FBO instead of the screen.  This rendering can be stored in
 *  a texture. Textures can be attached and detached from the FBO, so one FBO
 *  can be used to draw to many textures if desired.
 *  <P>
 *  FBOs can have depth and stencil buffers as well just like the default
 *  frame buffer.
 *  <P>
 *  By default this class creates a FBO with a renderBuffer attached to provide
 *  depth test, and one texture to hold the rendered image. You can attach other
 *  textures by calling addTexture(<texture handle>).  Textures must be the same
 *  dimensions as the FBO.  Call the JOFrameBuffer.makeTexture() functions to
 *  create textures with the correct settings for use in the FBO.
 *  <PRE>
 *  Example:
 *		JOFrameBuffer FBO;
 *
 *		setup() {
 *			FBO = new JOFrameBuffer(); // make a depth buffer and texture same size as display
 *		}
 *
 *		draw() {
 *			FBO.activate();   // rendering goes into FBO texture...
 *    		...               // draw something
 *			FBO.deactivate(); // rendering goes to screen
 *
 *          FBO.makeMipMap(); // regen mipmaps after texture has been modified (see note below)
 *
 *			JOApp.activateTexture(FBO.getTexture());
 *
 *			//... do more rendering with FBO texture...
 *		}
 *  </PRE>
 *
 *  NOTE: strange things happen if you draw to a FBO texture and don't call
 *        FBO.makeMipMap() when done.  The mipmaps will still have the prior
 *        texture images in them, so you may not see what you expect.  For
 *        example as your geometry moves further from the camera the texture
 *        may fade out or revert back to the previous texture image).
 */
public class JOFrameBuffer {

    public static final int FBO_RENDER_TO_TEXTURE = 1;

    public static final int FBO_SHADOWMAP = 2;

    public static final int FBO_STENCIL = 3;

    public static final int FBO_FULL = 4;

    public static int ActiveFBO = 0;

    public int config = FBO_RENDER_TO_TEXTURE;

    public int FBOhandle = 0;

    public int RBhandle = 0;

    public int TXhandle = 0;

    public int width;

    public int height;

    int status = 0;

    /**
	 *  Create a framebuffer the same dimensions as current screen
	 */
    public JOFrameBuffer() {
        width = JOApp.getWidth();
        height = JOApp.getHeight();
        config = FBO_RENDER_TO_TEXTURE;
        setupFBO();
    }

    /**
	 *  Create a framebuffer with the given dimensions
	 */
    public JOFrameBuffer(int w, int h) {
        width = w;
        height = h;
        config = FBO_RENDER_TO_TEXTURE;
        setupFBO();
    }

    /**
	 *  Create a framebuffer with the given dimensions and the given configuration.
	 *  Configuration must be one of the constants:
	 *  <PRE>
	 *     JOFrameBuffer.FBO_RENDER_TO_TEXTURE  (has a depth buffer, draws into a texture)
	 *     JOFrameBuffer.FBO_SHADOWMAP          (uses a DEPTH texture as depth buffer, doesn't draw anywhere)
	 *     JOFrameBuffer.FBO_STENCIL            (has depth and stencil buffers, draws into a texture)
	 *  </PRE>
	 */
    public JOFrameBuffer(int w, int h, int FBO_configuration) {
        width = w;
        height = h;
        config = FBO_configuration;
        setupFBO();
    }

    /**
	 *  Prepare framebuffer for use.  Width and height should be set to rational values
	 *  before calling this function.  Creates framebuffer with depth buffer and one texture.
	 */
    public int setupFBO() {
        FBOhandle = allocateFBO();
        if (config == FBO_RENDER_TO_TEXTURE) {
            addDepthBuffer();
            addTexture();
        } else if (config == FBO_SHADOWMAP) {
            addTextureDepthMap();
        } else if (config == FBO_STENCIL) {
            addDepthAndStencilBuffer();
            addTexture();
        } else {
            addDepthAndStencilBuffer();
            addTexture();
        }
        checkStatus();
        return status;
    }

    /**
	 * return handle of last texture attached to framebuffer
	 */
    public int getTexture() {
        return TXhandle;
    }

    /**
	 * Call glGenerateMipmapEXT() to regenerate mipmaps on an FBO texture.  Mipmaps
	 * are not automatically generated on textures renderd from an FBO, so you need
	 * to explicitly call glGenerateMipmapEXT() before using the texture in draw().
	 */
    public void makeMipMap() {
        if (TXhandle > 0) {
            JOFrameBuffer.makeMipMap(TXhandle);
        }
    }

    /**
	 * Make a texture (same size as FBO) and add it to the COLOR attachment point.
	 * @return the texture handle
	 */
    public int addTexture() {
        TXhandle = makeTexture();
        attachTexture(TXhandle);
        return TXhandle;
    }

    /**
	 *  Attach a texture to framebuffer.  Texture must be same dimensions as framebuffer.
	 *  To render to different textures, create one FrameBufferObject and attach/detach
	 *  the textures to render to each.  It's faster to swap textures than to bind/unbind
	 *  FrameBuffers.
	 *
	 *  @see makeTexture
	 */
    public void attachTexture(int textureId) {
        JOApp.msg("FBO attach texture " + textureId);
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, FBOhandle);
        JOApp.gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_TEXTURE_2D, textureId, 0);
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, ActiveFBO);
        TXhandle = textureId;
    }

    /**
	 * Make a DEPTH_COMPONENT texture and attach it to DEPTH_ATTACHMENT.
	 * Don't attach a depth renderbuffer (the texture is attached to DEPTH attach point).
	 * Turn off drawing to the color buffer with gldrawbuffer() (there is no color buffer
	 * in this scenario, only depth values are stored).
	 * <P>
	 * This setup is useful for Shadow Mapping.  Depth values are written to a texture,
	 * which can later be used to map shadows onto a scene.
	 * @return the texture handle
	 */
    public int addTextureDepthMap() {
        TXhandle = makeTextureDepth(width, height);
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, FBOhandle);
        JOApp.gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_DEPTH_ATTACHMENT_EXT, GL.GL_TEXTURE_2D, TXhandle, 0);
        JOApp.gl.glDrawBuffer(GL.GL_NONE);
        JOApp.gl.glReadBuffer(GL.GL_NONE);
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, ActiveFBO);
        return TXhandle;
    }

    /**
	 *  Remove a texture from the framebuffer.  Texture still exists, but will no
	 *  longer be rendered to by the FBO.
	 */
    public void removeTexture() {
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, FBOhandle);
        JOApp.gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_TEXTURE_2D, 0, 0);
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, ActiveFBO);
        TXhandle = 0;
    }

    /**
	 *  Return a handle to a new texture the same size as the framebuffer.
	 *  Mipmaps, anisotropic filtering, and tiling are enabled, so texture is good
	 *  for general use.
	 */
    public int makeTexture() {
        return makeTexture(width, height, true, true, true);
    }

    /**
	 *  Return a handle to a new texture the same size as the framebuffer.
	 *  Mipmaps, anisotropic filtering, and tiling can be enabled as needed.
	 */
    public int makeTexture(boolean mipmap, boolean anisotropic, boolean tile) {
        return makeTexture(width, height, mipmap, anisotropic, tile);
    }

    /**
     * Create an empty texture with the given width and height. The
     * FBO will provide the data space, so last arg to glTexImage2D() is null.
     * <P>
     * if mipmap arg is true, GL_GENERATE_MIPMAP is enabled.  Call FBO.makeMipMap()
     * to regenerate the mipmaps after rendering to the texture.
     * <P>
     * if anisotropic arg is true then anisotropic filtering will be enabled.
     * <P>
     * if tile arg is true then texture will GL_REPEAT in both directions, otherwise
     * texture will GL_CLAMP_TO_EDGE in both directions (good for projective textures).
     *
     * @return  the texture handle
     * @see makeMipMap()
     */
    public static int makeTexture(int w, int h, boolean mipmap, boolean anisotropic, boolean tile) {
        int textureId = JOApp.allocateTexture();
        JOApp.msg("FBO made txtrid=" + textureId);
        JOApp.gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
        if (mipmap) {
            JOApp.gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
            JOApp.gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
            JOApp.gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_GENERATE_MIPMAP, GL.GL_TRUE);
        }
        if (tile) {
            JOApp.gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            JOApp.gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        } else {
            JOApp.gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
            JOApp.gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
        }
        if (anisotropic && JOApp.extensionExists("GL_EXT_texture_filter_anisotropic")) {
            FloatBuffer max_a = JOApp.allocFloats(1);
            JOApp.gl.glGetFloatv(GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, max_a);
            JOApp.gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAX_ANISOTROPY_EXT, max_a.get(0));
        }
        JOApp.gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, w, h, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
        JOApp.gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        return textureId;
    }

    /**
	 * Create a texture containing GL_DEPTH_COMPONENT data only, to hold depth information.
	 * This texture can be attached to the depth attachment point to render depth values
	 * to the texture, for use in shadow mapping.
	 */
    public static int makeTextureDepth(int w, int h) {
        int textureId = JOApp.allocateTexture();
        JOApp.msg("made depth-only txtrid=" + textureId);
        JOApp.gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
        JOApp.gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        JOApp.gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        JOApp.gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
        JOApp.gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
        JOApp.gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_DEPTH_COMPONENT, w, h, 0, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, null);
        JOApp.gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        return textureId;
    }

    /**
	 * Call glGenerateMipmapEXT() to regenerate mipmaps on an FBO texture.  Mipmaps
	 * are not automatically generated on textures renderd from an FBO, so you need
	 * to explicitly call glGenerateMipmapEXT() before using the texture in draw().
	 */
    public static void makeMipMap(int textureId) {
        JOApp.gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
        JOApp.gl.glGenerateMipmapEXT(GL.GL_TEXTURE_2D);
        JOApp.gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    }

    /**
	 *  Create a renderBuffer configured as a depth buffer and attach it to the FBO
	 */
    public void addDepthBuffer() {
        IntBuffer rboId = JOApp.allocInts(1);
        JOApp.gl.glGenRenderbuffersEXT(1, rboId);
        RBhandle = rboId.get(0);
        JOApp.msg("made rboid=" + RBhandle);
        JOApp.gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, RBhandle);
        JOApp.gl.glRenderbufferStorageEXT(GL.GL_RENDERBUFFER_EXT, GL.GL_DEPTH_COMPONENT, width, height);
        JOApp.msg("about to attach rboId");
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, FBOhandle);
        JOApp.gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_DEPTH_ATTACHMENT_EXT, GL.GL_RENDERBUFFER_EXT, RBhandle);
        JOApp.gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, 0);
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, ActiveFBO);
    }

    /**
	 *  Create a renderBuffer configured as a depth and stencil buffer and attach it to the FBO
	 */
    public void addDepthAndStencilBuffer() {
        IntBuffer rboId = JOApp.allocInts(1);
        JOApp.gl.glGenRenderbuffersEXT(1, rboId);
        RBhandle = rboId.get(0);
        JOApp.msg("made rboid=" + RBhandle);
        JOApp.gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, RBhandle);
        JOApp.gl.glRenderbufferStorageEXT(GL.GL_RENDERBUFFER_EXT, GL.GL_DEPTH_STENCIL_EXT, width, height);
        JOApp.msg("about to attach rboId");
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, FBOhandle);
        JOApp.gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_DEPTH_ATTACHMENT_EXT, GL.GL_RENDERBUFFER_EXT, RBhandle);
        JOApp.gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_STENCIL_ATTACHMENT_EXT, GL.GL_RENDERBUFFER_EXT, RBhandle);
        JOApp.gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, 0);
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, ActiveFBO);
    }

    /**
	 *  Once activated() all further rendering will go to the framebuffer object.
	 *  This function sets the viewport to the width and height of the FBO. The
	 *  deactivate() function will return the viewport to it's previous setting
	 *  and turn off the FBO.
	 *  To use:
	 *  <PRE>
	 *      FBO.activate();
	 *      .....   // draw something here
	 *      FBO.deactivate();
	 *  </PRE>
	 *
	 *  @see deactivate()
	 */
    public void activate() {
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, FBOhandle);
        JOApp.gl.glPushAttrib(GL.GL_VIEWPORT_BIT);
        JOApp.gl.glViewport(0, 0, width, height);
        JOFrameBuffer.ActiveFBO = FBOhandle;
    }

    /**
	 *  Once deactivated all further rendering goes to the screen.  The viewport
	 *  is returned to its previous setting (before activate() was called).
	 *
	 *  @see activate()
	 */
    public void deactivate() {
        JOApp.gl.glPopAttrib();
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
        JOFrameBuffer.ActiveFBO = 0;
    }

    /**
	 *  Return the width of the FBO
	 */
    public int getWidth() {
        return width;
    }

    /**
	 *  Return the height of the FBO
	 */
    public int getHeight() {
        return height;
    }

    /**
	 *  NOT USED YET.  Pick which render target to draw to.  This could be used
	 *  to control rendering to multiple textures.  Right now there is only one
	 *  texture and one render target at a time.
	 */
    public void drawTo(int targetNumber) {
        int[] attachmentPoint = new int[] { GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_COLOR_ATTACHMENT1_EXT, GL.GL_COLOR_ATTACHMENT2_EXT, GL.GL_COLOR_ATTACHMENT3_EXT };
        if (targetNumber >= 0 && targetNumber < attachmentPoint.length) {
            JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, FBOhandle);
            JOApp.gl.glDrawBuffer(attachmentPoint[targetNumber]);
            JOApp.gl.glReadBuffer(attachmentPoint[targetNumber]);
            JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, ActiveFBO);
        }
    }

    public ByteBuffer getFramePixels(ByteBuffer pixels) {
        return null;
    }

    public ByteBuffer getFramePixels() {
        return null;
    }

    /**
	 *  delete the framebufferobject and renderbufferobject
	 */
    public void cleanup() {
        deleteFBO(FBOhandle);
        deleteRenderBuffer(RBhandle);
    }

    /**
	 *  Return the error code from the FBO
	 */
    public int checkStatus() {
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, FBOhandle);
        status = JOApp.gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT);
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, ActiveFBO);
        JOApp.msg("JOFrameBufferObject.checkStatus(): framebuffer status is " + status + " " + framebuffer_status_string(status));
        return status;
    }

    /**
	 *  return a new framebufferObject handle
	 */
    public static int allocateFBO() {
        IntBuffer handle = JOApp.allocInts(1);
        JOApp.gl.glGenFramebuffersEXT(1, handle);
        return handle.get(0);
    }

    /**
     * De-allocate the given FrameBufferObject.
     */
    public static void deleteFBO(int handle) {
        IntBuffer handleBuff = JOApp.allocInts(1).put(handle);
        JOApp.gl.glDeleteFramebuffersEXT(1, handleBuff);
    }

    /**
     * De-allocate the given RenderBuffer.
     */
    public static void deleteRenderBuffer(int handle) {
        IntBuffer handleBuff = JOApp.allocInts(1).put(handle);
        JOApp.gl.glDeleteRenderbuffersEXT(1, handleBuff);
    }

    /**
	 * Return a string representing the given fbo status code
	 */
    public static String framebuffer_status_string(int statcode) {
        switch(statcode) {
            case GL.GL_FRAMEBUFFER_COMPLETE_EXT:
                return ("complete!");
            case GL.GL_FRAMEBUFFER_UNSUPPORTED_EXT:
                return ("GL_FRAMEBUFFER_UNSUPPORTED_EXT");
            case GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
                return ("INCOMPLETE_ATTACHMENT");
            case GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
                return ("FRAMEBUFFER_MISSING_ATTACHMENT");
            case GL.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
                return ("FRAMEBUFFER_DIMENSIONS");
            case GL.GL_FRAMEBUFFER_INCOMPLETE_DUPLICATE_ATTACHMENT_EXT:
                return ("INCOMPLETE_DUPLICATE_ATTACHMENT");
            case GL.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
                return ("INCOMPLETE_FORMATS");
            case GL.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
                return ("INCOMPLETE_DRAW_BUFFER");
            case GL.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
                return ("INCOMPLETE_READ_BUFFER");
            case GL.GL_FRAMEBUFFER_BINDING_EXT:
                return ("BINDING_EXT");
            default:
                return ("Unknown status code " + statcode);
        }
    }

    /**
     * Save the current FrameBufferObject to a PNG image. Same as JOApp.screenShot()
     * but the FBO will be saved instead of the default framebuffer, and the
     * screenshot filename will be set to <applicationClassName>-<timestamp>.png
     * <P>
     * You can also call the regular JOApp.screenshot() function to save FBO pixels
     * to a file, but that will capture only up to the size of the display.  The
     * JOFrameBuffer.screenshot() function will capture the full FBO size, which
     * can be larger than the screen.
     */
    public void screenShot() {
        screenShot(0, 0, getWidth(), getHeight(), JOApp.rootClass.getName() + "_" + JOApp.makeTimestamp() + ".png");
    }

    public void screenShot(String filename) {
        screenShot(0, 0, getWidth(), getHeight(), filename);
    }

    /**
     * Save a region of the current FrameBufferObject to a PNG image. Same as
     * JOApp.screenShot(x,y,w,h,filename) but the FBO will be saved instead of
     * the default framebuffer.
     * <P>
     * You can also call the regular JOApp.screenshot() function to save FBO pixels
     * to a file, but that will capture only up to the size of the display.  The
     * JOFrameBuffer.screenshot() functions can capture the full FBO size, which
     * may be larger than the screen.
     */
    public void screenShot(int x, int y, int w, int h, String imageFilename) {
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, FBOhandle);
        screenShot(x, y, w, h, imageFilename, true);
        JOApp.gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, ActiveFBO);
    }

    /**
     * Save a region of the current render buffer to a PNG image.  This is
     * the same function as the JOApp screenshot(), but this version forces
     * the alpha byte to 255 (fully opaque).  The pixels from the FBO contain
     * alpha values and the screenshot image will appear transparent anywhere
     * the geometry was drawn with alpha less than 1.  To insure that the
     * screenshot is a fully opaque image, alpha is forced to opaque by default.
     * This default can be overridden to allow the screen image to have
     * transparent areas.
     * <P>
     * WARNING: this function hogs memory!  Call java with more memory
     * (java -Xms128m -Xmx128m)
     * <P>
     */
    public void screenShot(int x, int y, int width, int height, String imageFilename, boolean forceOpaque) {
        ByteBuffer framebytes = JOApp.allocBytes(width * height * JOApp.SIZE_INT);
        int[] pixels = new int[width * height];
        if (forceOpaque) {
            makeTextureOpaque();
        }
        JOApp.gl.glReadPixels(x, y, width, height, GL.GL_BGRA, GL.GL_UNSIGNED_INT_8_8_8_8_REV, framebytes);
        framebytes.asIntBuffer().get(pixels, 0, pixels.length);
        framebytes = null;
        JOImage.savePixelsToPNG(pixels, width, height, imageFilename, true);
    }

    /**
	 * Alpha values are handled differently in FBO textures vs. the color buffer.
	 * When you render-to-texture thru an FBO with blending enabled, the
	 * alpha values of colors are preserved in the texture. In the default
	 * render-to-screen behavior the alpha values in the color buffer are set
	 * to 1. In effect, the screen is always treated as opaque, while a texture
	 * can have transparency.
	 *
	 * So glCopyTexSubImage() or glReadPixels() from the default color buffer
	 * will produce an opaque texture or image, but the same commands with an
	 * FBO can result in a texture or image with translucent areas.
	 *
	 * For consistent behavior across FBO and default color buffer, force the
	 * alpha values to 1 before reading pixels from the FBO.

		// force the texture alpha channel to 1 (fully opaque)
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, FBO);
		gl.glPushAttrib(GL.GL_COLOR_BUFFER_BIT);
		gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
		gl.glColorMask(false, false, false, true);
		gl.glClearColor(0.0f,0.0f,0.0f,1.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// return to normal
		gl.glPopAttrib();
		//gl.glBlendFunc(CC_BLEND_SRC, CC_BLEND_DST);
		gl.glColorMask(true, true, true, true);
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, oldFBO);
	*/
    public void makeTextureOpaque() {
        JOApp.gl.glPushAttrib(GL.GL_COLOR_BUFFER_BIT);
        JOApp.gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
        JOApp.gl.glColorMask(false, false, false, true);
        JOApp.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        JOApp.gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        JOApp.gl.glPopAttrib();
        JOApp.gl.glColorMask(true, true, true, true);
    }
}
