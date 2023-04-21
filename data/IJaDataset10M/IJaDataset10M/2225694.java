package sdl4java.video;

import java.nio.Buffer;

/**
 * A Surface is an area of graphical memory which can be manipulated or blitted upon by other surfaces.
 * 
 * If the depth is 4 or 8 bits, an empty palette is allocated for the Surface.
 * If the depth is greater than 8 bits, the pixel format is set using the RGB masks.
 * 
 * The 'flags' tell what kind of surface to create.
 * These have to be "OR"'d in to the constructor if there is more than one.
 * 
 * If the Surface is created in video memory blits will be much faster
 * (HWSURFACE) but the Surface's PixelFormat must be identical to the Display PixelFormat.
 */
public class Surface extends sdl4java.CStruct {

    /**
	 * Surface has exclusive palette.
	 */
    public static final int HWPALETTE = 0x20000000;

    /**
	 * Create the Surface in system memory.
	 */
    public static final int SWSURFACE = 0x00000000;

    /**
	 * Surface will be placed in
	 * video memory if possible.  Note you may have to call
	 * lock() in order to access the raw framebuffer.  This is useful for Surfaces that will not change much, to take advantage of hardware acceleration when being blitted to the Display surface.
	 */
    public static final int HWSURFACE = 0x00000001;

    /**
	 * SDL4Java will try to perform asynchronous blits with this Surface, but you must always <i>lock()</i> it before accessing the pixels.  SDL4Java will wait for current blits to finish before returning from the lock.
	 */
    public static final int ASYNCBLIT = 0x00000004;

    /**
	 * Hardware accelerate blits.
	 */
    public static final int HWACCEL = 0x00000100;

    /**
	 * Surface will be used for colorkey blits.  If the hardware supports acceleration
	 * of colorkey blits between two Surfaces in video memory, SDL4Java will try to place the
	 * Surface in video memory. If this isn't possible or if there is no hardware acceleration available, the Surface will be placed in system memory.
	 */
    public static final int SRCCOLORKEY = 0x00001000;

    public static final int RLEACCELOK = 0x00002000;

    /**
	 * Colorkey blitting is accelerated with RLE
	 */
    public static final int RLEACCEL = 0x00004000;

    /**
	 * Surface will be used for alpha blits and if the hardware supports hardware acceleration
	 * of alpha blits between two surfaces in video memory to place the Surface in video memory
	 * if possible, otherwise it will be placed in system memory.
	 */
    public static final int SRCALPHA = 0x00010000;

    /**
	 * Surface uses preallocated memory.
	 */
    public static final int PREALLOC = 0x01000000;

    /**
	 * Flag for <i>setPalette()</i> function.  Sets logical palette, which
	 * controls how blits are mapped to/from the Surface.
	 */
    public static final int LOGPAL = 0x01;

    /**
	 * Flag for <i>setPalette()</i> function.  Sets physical palette, which
	 * controls how pixels look on the screen.
	 */
    public static final int PHYSPAL = 0x02;

    /**
	 * This constructor is only here to allow Display to extend this class.  It should
	 * not be used outside SDL4Java.
	 */
    protected Surface() {
    }

    /**
	 * Create an empty Surface.
	 * 
	 * @param flags Surface flags.
	 * @param width Width of Surface.
	 * @param height Height of Surface.
	 * @param depth If depth is 8 bits an empty  palette is allocated for the
	 *      Surface,  otherwise a 'packed-pixel' PixelFormat is  created using   
	 *      the  [RGBA]mask's provided.
	 * @param rmask Red mask.
	 * @param gmask Green mask.
	 * @param bmask Blue mask.
	 * @param amask Alpha mask.
	 */
    public Surface(int flags, int width, int height, int depth, long rmask, long gmask, long bmask, long amask) {
        jstruct = _surface(flags, width, height, depth, rmask, gmask, bmask, amask);
    }

    private native int _surface(int flags, int width, int height, int depth, long rmask, long gmask, long bmask, long amask);

    /**
	 * Creates a Surface from the provided pixel data.
	 * The data stored in pixels is assumed to be of the depth specified in the parameter list.  The pixel data is not copied into the Surface so it should not be finalized
	 * until the Surface has been finalized.
	 * 
	 * @param pixels Image data in pixels.
	 * @param width Width the Surface should be.
	 * @param height Height of the Surface.
	 * @param depth Pixel bit depth.
	 * @param pitch The length of each scanline  in bytes.
	 * @param rmask Red mask.
	 * @param gmask Green mask.
	 * @param bmask Blue mask.
	 * @param amask Alpha mask.
	 */
    public Surface(Buffer pixels, int width, int height, int depth, int pitch, long rmask, long gmask, long bmask, long amask) {
        jstruct = _surface(pixels, width, height, depth, pitch, rmask, gmask, bmask, amask);
    }

    private native int _surface(Buffer pixels, int width, int height, int depth, int pitch, long rmask, long gmask, long bmask, long amask);

    /**
	 * This creates a Surface object from a BMP image file.
	 * 
	 * @param imageFile Image file name with path information (if any).
	 * @exception sdl4java.SDLException Throws this  if the image file cannot be 
	 *     found or the  format is not supported.
	 */
    public Surface(String imageFile) throws sdl4java.SDLException {
        jstruct = _surface(imageFile);
    }

    private native int _surface(String file);

    /**
	 * This creates a Surface object from a BMP image file.
	 * 
	 * 
	 * @param file Bitmap image file to create Surface from.
	 * @exception sdl4java.SDLException 
	 * @exception SDLException If the file is not found this will be thrown.
	 */
    public Surface(java.io.File file) throws sdl4java.SDLException {
        this(file.getAbsolutePath());
    }

    /**
	 * Blits a Surface onto this Surface at position (0,0).
	 * 
	 * @param srcSurface Surface to blit onto this surface.
	 * @return True if it works, false otherwise.
	 */
    public boolean blit(Surface srcSurface) {
        return blit(srcSurface.jstruct, this.jstruct);
    }

    private native boolean blit(int srcSurface, int dstSurface);

    /**
	 * Blits a Surface onto this Surface at position (dstX, dstY).
	 * 
	 * @param srcSurface Surface to blit onto this Surface.
	 * @param dstX Desination X location.
	 * @param dstY Desination Y location.
	 * @return True if it works, false otherwise.
	 */
    public boolean blit(Surface srcSurface, int dstX, int dstY) {
        return blit(srcSurface.jstruct, this.jstruct, dstX, dstY);
    }

    private native boolean blit(int srcSurface, int dstSurface, int dstX, int dstY);

    /**
	 * Blits a Surface to this Surface at a defined location.
	 * 
	 * @param srcSurface Surface to blit to this Surface..
	 * @param dstRect Destination coordinates.
	 * @return True if it works, false otherwise.
	 */
    public boolean blit(Surface srcSurface, Rect dstRect) {
        return blit(srcSurface.jstruct, this.jstruct, dstRect.jstruct);
    }

    private native boolean blit(int srcSurface, int dstSurface, int dstRect);

    /**
	 * Blits a defined part of a Surface to a deinfed part of this Surface.
	 * 
	 * @param srcSurface Surface to blit to this Surface.
	 * @param srcX X coordinate of source Surface to blit from.
	 * @param srcY Y coordinate of source Surface to blit from.
	 * @param srcWidth Width of source Surface to blit.
	 * @param srcHeight Height of source Surface to blit.
	 * @param dstX X coordinate to blit to.
	 * @param dstY Y coordinate to blit to.
	 * @param dstWidth Width of destination to blit to.
	 * @param dstHeight Height of destination to blit to.
	 * @return True if it works, false otherwise.
	 */
    public boolean blit(Surface srcSurface, int srcX, int srcY, int srcWidth, int srcHeight, int dstX, int dstY, int dstWidth, int dstHeight) {
        return blit(srcSurface.jstruct, srcX, srcY, srcWidth, srcHeight, this.jstruct, dstX, dstY, dstWidth, dstHeight);
    }

    private native boolean blit(int srcSurface, int srcX, int srcY, int srcWidth, int srcHeight, int dstSurface, int dstX, int dstY, int dstWidth, int dstHeight);

    /**
	 * Stretch one Surface onto another.
	 * 
	 * @param srcSurface Surface that will be stretched.
	 * @param srcRect Rectangular area of source  Surface to stretch.
	 * @param dstSurface Destination Surface for  stretched Surface.
	 * @param dstRect Rectangular area of  destination Surface where stretch 
	 *      Surface will appear.
	 * @return TRUE if the stretched worked.  FALSE  otherwise.
	 */
    public static boolean stretch(Surface srcSurface, Rect srcRect, Surface dstSurface, Rect dstRect) {
        return stretch(srcSurface.jstruct, srcRect.jstruct, dstSurface.jstruct, dstRect.jstruct);
    }

    private static native boolean stretch(int srcSurface, int srcRect, int dstSurface, int dstRect);

    /**
	 * Creates a new Surface of the specified format, and then copies and maps
	 * the given Surface to it so the blit of the converted Surface will be as
	 * fast as possible.If this function fails, it returns null.
	 * 
	 * You can also pass RLEACCEL in the flags parameter and SDL will try to RLE
	 * accelerate colorkey and alpha blits in the resulting Surface.
	 * 
	 * @param src Surface to convert from.
	 * @param format New PixelFormat to convert  with.
	 * @param flags Either Surface flags for new  Surface.
	 * @return A resulting Surface with specified  <i>flags</i> and PixelFormat.
	 */
    public static Surface convert(Surface src, PixelFormat format, int flags) {
        Surface surface = new Surface();
        surface.jstruct = convert(src.jstruct, format.jstruct, flags);
        return surface;
    }

    private static native int convert(int surface, int format, int flags);

    /**
	 * Returns a the PixelFormat for the Surface.
	 * 
	 * @return PixelFormat for the Surface.
	 */
    public PixelFormat getFormat() {
        PixelFormat format = new PixelFormat();
        format.jstruct = getFormat(this.jstruct);
        return format;
    }

    private native int getFormat(int surface);

    /**
	 * Returns width of Surface.
	 * 
	 * @return Width of the Surface.
	 */
    public int getWidth() {
        return getWidth(this.jstruct);
    }

    private native int getWidth(int surface);

    /**
	 * Set the width of the Surface.
	 * 
	 * @param width Width of Surface.
	 */
    public void setWidth(int width) {
        setWidth(this.jstruct, width);
    }

    private native void setWidth(int surface, int width);

    /**
	 * Returns height of Surface.
	 * 
	 * @return Height of Surface.
	 */
    public int getHeight() {
        return getWidth(this.jstruct);
    }

    private native int getHeight(int surface);

    /**
	 * Sets the Surface's height.
	 * 
	 * @param height Surface's height.
	 */
    public void setHeight(int height) {
        setHeight(this.jstruct, height);
    }

    private native void setHeight(int surface, int height);

    /**
	 * Return the pitch of the Surface.
	 * 
	 * @return Pitch of Surface.
	 */
    public int getPitch() {
        return getPitch(this.jstruct);
    }

    private native int getPitch(int surface);

    /**
	 * Sets the Surface's pitch.
	 * 
	 * @param pitch Surface pitch.
	 */
    public void setPitch(int pitch) {
        setPitch(this.jstruct, pitch);
    }

    private native void setPitch(int surface, int pitch);

    /**
	 * Return the flags used in the creation of the Surface.
	 * 
	 * @return Flags set during creation of Surface.
	 */
    public int getFlags() {
        return getFlags(this.jstruct);
    }

    private native int getFlags(int surface);

    /**
	 * Sets the Surface flags.
	 * 
	 * @param flags Surface flags.
	 */
    public void setFlags(int flags) {
        setFlags(this.jstruct, flags);
    }

    private native void setFlags(int surface, int flags);

    /**
	 * Returns the Pixels of the Surface.
	 * 
	 * @return Pixels of the Surface.
	 */
    public Buffer getPixels() {
        return getPixels(this.jstruct);
    }

    private native Buffer getPixels(int surface);

    /**
	 * Sets the pixels for the Surface.
	 * 
	 * @param pixels Buffer of pixel data.
	 */
    public void setPixels(Buffer pixels) {
        setPixels(this.jstruct, pixels);
    }

    private native void setPixels(int surface, Buffer pixels);

    /**
	 * This function performs a fast fill of the given rectangle with 'color'.
	 * If the 'rect' parameter is null, the whole surface will be filled with
	 * 'color'.
	 * 
	 * @param rect Retangular clipping area of Surface where this function will 
	 *     create  the color filled rectangle.
	 * @param color Color to fill with
	 * @return True on success, false otherwise.
	 */
    public boolean fill(Rect rect, Color color) {
        return fill(this.jstruct, rect.jstruct, color.jstruct);
    }

    private native boolean fill(int surface, int rect, int color);

    /**
	 * This function takes a Surface and copies it to a new Surface of the
	 * pixel format and colors of the video framebuffer, suitable for fast blitting onto the Display surface.
	 * 
	 * If you want to take advantage of hardware colorkey or alpha blit
	 * acceleration, you should set the colorkey and alpha value before calling this function.
	 * 
	 * If the conversion fails or runs out of memory, it returns null.
	 * 
	 * @return Fast blitting Surface or null if there is an error.
	 */
    public Surface displayFormat() {
        Surface surface = new Surface();
        surface.jstruct = displayFormat(this.jstruct);
        return surface;
    }

    private native int displayFormat(int surface);

    /**
	 * This function takes a Surface and copies it to a new Surface of the
	 * pixel format and colors of the video framebuffer (if possible),
	 * suitable for fast alpha blitting onto the Display surface.
	 * The new surface will always have an alpha channel.
	 * 
	 * If you want to take advantage of hardware colorkey or alpha blit
	 * acceleration, you should set the colorkey and alpha value before
	 * calling this function.
	 * 
	 * If the conversion fails or runs out of memory, it returns NULL
	 * 
	 * @return Fast alpha blitting Surface or null if there is an error.
	 */
    public Surface displayFormatAlpha() {
        Surface surface = new Surface();
        surface.jstruct = displayFormatAlpha(this.jstruct);
        return surface;
    }

    private native int displayFormatAlpha(int surface);

    /**
	 * Sets a portion of the colormap for a given 8-bit surface.
	 * 
	 * setColors() function is equivalent to calling this function with
	 * flags = (LOGPAL|PHYSPAL).
	 * 
	 * @param flags Is either one or both of: LOGPAL or PHYSPAL.
	 * @param colors Array of Colors to set the  Surface's Palette to.
	 * @param firstcolor First color index.
	 * @param ncolors Number of colors.
	 * @return If Surface is not a palettized surface, this function does 
	 *  nothing, returning 0. If all of the colors were set as passed to 
	 *  setPalette, it will return 1. If not all the color entries were set 
	 *  exactly as given, it will return 0, and you should look at the Surface 
	 *  palette to determine the actual color palette.
	 */
    public int setPalette(int flags, Color colors[], int firstcolor, int ncolors) {
        int jcolors[] = new int[colors.length];
        for (int i = 0; i < colors.length; i++) jcolors[i] = colors[i].jstruct;
        return setPalette(this.jstruct, flags, jcolors, firstcolor, ncolors);
    }

    private native int setPalette(int surface, int flags, int[] colors, int firstcolor, int ncolors);

    /**
	 * Sets a portion of the colormap for a 8-bit Surface.
	 * 
	 * @param colors Array of color objects to set the colors to.
	 * @param firstcolor First color to set?
	 * @param ncolors Number of colors to set?
	 * @return If the Surface is not a palettized Surface, 
	 * 	this function does nothing, returning 0. If all of the colors were set as 
	 * 	passed to SDL_SetColors, it will return 1. If not all the color entries 
	 *  were set exactly as given, it will return 0, and you should look at the 
	 *  Surface palette to determine the actual color palette.
	 */
    public int setColors(Color colors[], int firstcolor, int ncolors) {
        return this.setPalette((Surface.LOGPAL | Surface.PHYSPAL), colors, firstcolor, ncolors);
    }

    /**
	 * Sets up a surface for directly accessing the pixels.
	 * Between calls to lock()/unlock(), you can write to and read from the Surface's pixels.  Once you are done accessing the surface you should use unlock() to release it.
	 * 
	 * Not all Surfaces require locking.  In particular, if the
	 * HWSURFACE flag is not passed to Display, you
	 * will not need to lock the display surface before accessing it.
	 * 
	 * No operating system or library calls should be made between lock/unlock
	 * pairs, as critical system locks may be held during this time.
	 * 
	 * lock() returns true, or false if the Surface couldn't be locked.
	 * 
	 * @return True if it works, false otherwise.
	 */
    public boolean lock() {
        return lock(this.jstruct);
    }

    private native boolean lock(int surface);

    /**
	 * Surfaces that were previously locked using lock() must be unlocked with
	 * this function.Surfaces should be unlocked as soon as possible.
	 */
    public void unlock() {
        unlock(this.jstruct);
    }

    private native void unlock(int surface);

    /**
	 * Saves a Surface as a BMP image file.
	 * 
	 * @param file Absolute path and filename of bitmap file.
	 * @return True if it works, false otherwise.
	 */
    public boolean save(String file) {
        return save(this.jstruct, file);
    }

    private native boolean save(int surface, String file);

    /**
	 * Sets the color key (transparent pixel) in a blittable surface and RLE acceleration.
	 * 
	 * RLE acceleration can substantially speed up blitting of images with large horizontal runs of transparent pixels (i.e., pixels that match the key value). The key must be of the same pixel format as the Surface, mapRGB() is often useful for obtaining an acceptable value.
	 * 
	 * If flag is SRCCOLORKEY then key is the transparent pixel value in the source image of a blit.
	 * 
	 * If flag is OR'd with RLEACCEL then the surface will be draw using RLE acceleration when drawn with SDL_BlitSurface. The surface will actually be encoded for RLE acceleration the first time SDL_BlitSurface or SDL_DisplayFormat is called on the surface.
	 * 
	 * If flag is 0, this function clears any current color key.
	 * 
	 * 
	 * @param flag SRCCOLORKEY, RLEACCEL, or 0.  Values can be OR'ed.
	 * @param key Transparent color.
	 * @return True if it works, false otherwise.
	 */
    public boolean setColorKey(int flag, long key) {
        return setColorKey(this.jstruct, flag, key);
    }

    private native boolean setColorKey(int surface, int flag, long key);

    /**
	 * This function sets the alpha value for the entire Surface, as opposed to
	 * using the alpha component of each pixel.
	 * 
	 * Flag measures the range of transparency of the surface, 0 being completely
	 * transparent to 255 being completely opaque.  An 'alpha' value of 255 causes blits to be opaque, the source pixels copied to the destination (the default).
	 * 
	 * Note that per-Surface alpha can be combined with colorkey transparency.
	 * 
	 * @param flag Is used to specify whether alpha blending should be used 
	 *     (SRCALPHA) and whether the surface should use RLE acceleration for    
	 *     blitting (RLEACCEL). flags can be an OR'd combination of these two    
	 *     options, one of these options or 0.
	 * @param alpha Value from 0 to 255 where 0 is considered opaque and 255 
	 *      is considered transparent.
	 * @return True if it works, false otherwise.
	 */
    public boolean setAlpha(int flag, int alpha) {
        return setAlpha(this.jstruct, flag, alpha);
    }

    private native boolean setAlpha(int surface, int flag, int alpha);

    /**
	 * Returns the clipping Rectangle for the Surface.
	 * 
	 * @return Clipping area of the Surface.
	 */
    public Rect getClipRect() {
        Rect rect = new Rect();
        rect.jstruct = getClipRect(this.jstruct);
        return rect;
    }

    private native int getClipRect(int surface);

    /**
	 * Sets the clipping rectangle for the destination surface in a blit.
	 * If the clip rectangle is NULL, clipping will be disabled.
	 * 
	 * @param rect Clipping rectangular area.
	 */
    public void setClipRect(Rect rect) {
        setClipRect(this.jstruct, rect.jstruct);
    }

    private native void setClipRect(int surface, int rect);

    protected native void freeStruct();
}
