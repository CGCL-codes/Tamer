package gestalt.shape.material;

import static gestalt.Gestalt.*;
import gestalt.render.Disposable;
import gestalt.shape.Color;
import gestalt.texture.Bitmap;
import gestalt.texture.bitmap.ByteBitmap;
import gestalt.texture.bitmap.ByteBufferBitmap;
import gestalt.texture.bitmap.IntegerBitmap;
import gestalt.util.ImageUtil;
import mathematik.Vector2f;
import mathematik.Vector3f;

public abstract class TexturePlugin implements MaterialPlugin, Disposable, TextureInfo {

    protected final boolean _myHintFlipYAxis;

    protected boolean _myIsInitalized;

    protected boolean _myWrapModeChanged;

    protected boolean _myFilterTypeChanged;

    protected boolean _myBorderColorChanged;

    protected Bitmap _myScheduledBitmap;

    protected Bitmap _myBitmap;

    protected Color _myBorderColor;

    private Vector2f _myNPOTReScale;

    private Vector2f _myPosition;

    private Vector3f _myScale;

    private Vector3f _myRotation;

    private int _myWrapMode;

    private int _myFilterType;

    private int _myTextureTarget;

    private int _myTextureUnit;

    protected int _myOpenGLTextureID;

    public TexturePlugin() {
        this(true);
    }

    public TexturePlugin(final boolean theHintFlipYAxis) {
        _myPosition = new Vector2f(0, 0);
        _myHintFlipYAxis = theHintFlipYAxis;
        if (_myHintFlipYAxis) {
            _myScale = new Vector3f(1, -1, 1);
        } else {
            _myScale = new Vector3f(1, 1, 1);
        }
        _myNPOTReScale = new Vector2f(1, 1);
        _myRotation = new Vector3f();
        _myOpenGLTextureID = UNDEFINED;
        _myWrapMode = TEXTURE_WRAPMODE_REPEAT;
        _myFilterType = TEXTURE_FILTERTYPE_LINEAR;
        _myIsInitalized = false;
        _myBorderColor = new Color(0, 0, 0, 0);
        _myWrapModeChanged = true;
        _myFilterTypeChanged = true;
        _myBorderColorChanged = true;
    }

    public final boolean hint_flip_axis() {
        return _myHintFlipYAxis;
    }

    public final boolean isInitialized() {
        return _myIsInitalized;
    }

    public final Vector2f position() {
        _myBorderColorChanged = true;
        return _myPosition;
    }

    public Color bordercolor() {
        return _myBorderColor;
    }

    public int getPixelWidth() {
        if (_myScheduledBitmap != null) {
            return _myScheduledBitmap.getWidth();
        } else if (_myBitmap != null) {
            return _myBitmap.getWidth();
        }
        return 1;
    }

    public int getPixelHeight() {
        if (_myScheduledBitmap != null) {
            return _myScheduledBitmap.getHeight();
        } else if (_myBitmap != null) {
            return _myBitmap.getHeight();
        }
        return 1;
    }

    public final Vector3f scale() {
        return _myScale;
    }

    public final Vector3f rotation() {
        return _myRotation;
    }

    public final Vector2f nonpoweroftwotexturerescale() {
        return _myNPOTReScale;
    }

    public final void setTextureTarget(int theTextureTarget) {
        _myTextureTarget = theTextureTarget;
    }

    public int getTextureTarget() {
        return _myTextureTarget;
    }

    public final void setTextureUnit(int theTextureUnit) {
        _myTextureUnit = theTextureUnit;
    }

    public final int getTextureUnit() {
        return _myTextureUnit;
    }

    public final int getFilterType() {
        return _myFilterType;
    }

    /**
     * @deprecated
     * @return
     */
    public int getOpenGLTextureID() {
        return getTextureID();
    }

    /**
     * @deprecated
     * @param theOpenGLTextureID
     */
    public void setOpenGLTextureID(int theOpenGLTextureID) {
        setTextureID(theOpenGLTextureID);
    }

    public int getTextureID() {
        return _myOpenGLTextureID;
    }

    public void setTextureID(int theOpenGLTextureID) {
        _myOpenGLTextureID = theOpenGLTextureID;
    }

    public final int getWrapMode() {
        return _myWrapMode;
    }

    public abstract int getMaxTextureSize();

    public boolean validateBitmapSize(final Bitmap theBitmap) {
        if (theBitmap.getWidth() > getMaxTextureSize() || theBitmap.getHeight() > getMaxTextureSize()) {
            System.err.println("### WARNING @ " + getClass().getName() + " / texture size (" + theBitmap.getWidth() + ", " + theBitmap.getHeight() + ") exceeds maximum size of " + getMaxTextureSize() + ".");
            return false;
        } else if (theBitmap.getWidth() <= 0 || theBitmap.getHeight() <= 0) {
            System.err.println("### WARNING @ " + getClass().getName() + " / texture size is too small.");
        }
        return true;
    }

    public void setWrapMode(final int theWrapMode) {
        if (theWrapMode == _myWrapMode) {
            return;
        }
        _myWrapMode = theWrapMode;
        _myWrapModeChanged = true;
    }

    public void setFilterType(final int theFilterType) {
        if (theFilterType == _myFilterType) {
            return;
        }
        if (_myIsInitalized && theFilterType == TEXTURE_FILTERTYPE_MIPMAP) {
            System.err.println("### WARNING @ " + getClass().getName() + " / currently MIPMAP only works if set in the beginning.");
        }
        _myFilterType = theFilterType;
        _myFilterTypeChanged = true;
    }

    public void load(final Bitmap theBitmap) {
        _myScheduledBitmap = theBitmap;
    }

    public void reload() {
        if (_myBitmap == null) {
            return;
        }
        _myScheduledBitmap = _myBitmap;
    }

    protected void setNPOTTextureScale() {
        final int myWidth = _myBitmap.getWidth();
        final int myHeight = _myBitmap.getHeight();
        final float myPowerOf2Width = ImageUtil.getNextPowerOf2(myWidth);
        final float myPowerOf2Height = ImageUtil.getNextPowerOf2(myHeight);
        if (myWidth == myPowerOf2Width) {
            _myNPOTReScale.x = 1f;
        } else {
            _myNPOTReScale.x = myWidth / myPowerOf2Width;
        }
        if (myHeight == myPowerOf2Height) {
            _myNPOTReScale.y = 1f;
        } else {
            _myNPOTReScale.y = myHeight / myPowerOf2Height;
        }
    }

    public static final int ERROR_EXCEEDED_SIZE = 0;

    public static final int ERROR_BITMAP_REFERANCE_NULL = 1;

    protected Bitmap getErrorBitmap(int theErrorColor) {
        byte _one = (byte) 255;
        byte zero = (byte) 0;
        ByteBitmap myBitmap = null;
        byte[] myPixels = null;
        switch(theErrorColor) {
            case ERROR_EXCEEDED_SIZE:
                myPixels = new byte[] { _one, zero, zero, _one, _one, _one, zero, _one, _one, _one, zero, _one, _one, zero, zero, _one };
                myBitmap = new ByteBitmap(myPixels, 2, 2, BITMAP_COMPONENT_ORDER_RGBA);
                break;
            case ERROR_BITMAP_REFERANCE_NULL:
                myPixels = new byte[] { zero, zero, _one, _one, _one, zero, _one, _one, _one, zero, _one, _one, zero, zero, _one, _one };
                myBitmap = new ByteBitmap(myPixels, 2, 2, BITMAP_COMPONENT_ORDER_RGBA);
                break;
        }
        return myBitmap;
    }

    public Bitmap bitmap() {
        if (_myScheduledBitmap != null) {
            return _myScheduledBitmap;
        } else {
            return _myBitmap;
        }
    }

    public void setBitmapRef(final Bitmap theBitmap) {
        if (!(theBitmap instanceof ByteBitmap) && !(theBitmap instanceof IntegerBitmap) && !(theBitmap instanceof ByteBufferBitmap)) {
            System.err.println("### WARNING @ " + getClass().getName() + " / bitmap is of unsupported type.");
        }
        _myBitmap = theBitmap;
    }
}
