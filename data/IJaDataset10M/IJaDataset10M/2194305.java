package org.easyway.objects.text;

import static org.easyway.objects.text.EWFont.COLOR_CHAR;
import java.awt.Color;
import org.easyway.annotations.Optimized;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.interfaces.sprites.IFont;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.objects.BaseObject;
import org.easyway.objects.Camera;
import org.easyway.system.StaticRef;
import org.lwjgl.opengl.GL11;

@Optimized
public class Text extends BaseObject implements IDrawing, ILayerID, IPlain2D {

    private static final long serialVersionUID = 3166077429305328097L;

    private int layer;

    /**
	 * the drawing sheet
	 */
    private int idLayer = -1;

    protected String text;

    protected int width;

    protected int numberOfLines = 1;

    protected int height;

    protected int size;

    protected int x;

    protected int y;

    protected IFont font;

    protected int alignH;

    protected int alignV;

    /**
	 * indicates if the coordinates are relative to the screen or to the
	 * monitor.<br>
	 * 
	 * if true the coordinates are relative to the monitor else the coordinates
	 * are relative to the screen
	 */
    public boolean fixedOnScreen = true;

    /**
	 * indicates if the zoomFactor of the Camera will have affect on the Text or
	 * not<br>
	 */
    public boolean fixedDimension = true;

    public Text(int x, int y, String text, IFont font) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.font = (font == null) ? IFont.defaultFont : font;
        alignH = TextAlign.LEFT;
        alignV = TextAlign.TOP;
        width = calculateMaxString();
        height = (size = this.font.getSize()) * numberOfLines;
        type.set("$_TEXT");
        if (autoAddToLists) setIdLayer(StaticRef.layers.length - 1);
    }

    public void render() {
        if (text == null || font == null) return;
        Camera camera = StaticRef.getCamera();
        boolean mustchange = false;
        if (camera.is3D()) {
            mustchange = true;
            camera.set2D();
        }
        float oldZoom = camera.getZoom2D();
        if (fixedDimension) {
            camera.zoom2D(1.0f);
        }
        GL11.glColor4f(1, 1, 1, 1);
        font.setSize(size);
        int nx = (int) getXOnScreen(), ny = (int) getYOnScreen();
        if (!fixedOnScreen) {
            nx = (int) getX();
            ny = (int) getY();
        }
        if (alignH == TextAlign.MIDDLE) {
            nx -= getWidth() / 2;
        } else if (alignH == TextAlign.RIGHT) {
            nx -= getWidth();
        }
        if (alignV == TextAlign.BOTTOM) {
            ny -= getHeight();
        } else if (alignV == TextAlign.MIDDLE) {
            ny -= getHeight() / 2;
        }
        font.writeString(text, text.length(), nx, ny);
        if (mustchange) camera.set3D();
        if (fixedDimension) camera.zoom2D(oldZoom);
        assert camera == StaticRef.getCamera();
        StaticRef.getCamera().objectsAtScreen.add(this);
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
        readdToDrawingLists();
    }

    public int getAlignH() {
        return alignH;
    }

    public void setAlignH(int alignH) {
        this.alignH = alignH;
    }

    public int getAlignV() {
        return alignV;
    }

    public void setAlignV(int alignV) {
        this.alignV = alignV;
    }

    public IFont getFont() {
        return font;
    }

    public void setFont(IFont font) {
        this.font = font;
        width = calculateMaxString();
        if (!font.canChangeSize()) {
            size = font.getSize();
            height = size * numberOfLines;
        }
    }

    /**
	 * returns the Height of the text in pixels
	 * 
	 * @see #getWidth()
	 * @see #getLength()
	 */
    public int getHeight() {
        return height;
    }

    /**
	 * returns the message of the text
	 */
    public String getText() {
        return text;
    }

    /**
	 * append a string to the Text's message<br>
	 */
    public void append(String text) {
        this.text += text;
        if (font == null) return;
        width = calculateMaxString();
        height = numberOfLines * size;
    }

    /**
	 * change the text's message
	 */
    public void setText(String text) {
        this.text = text;
        if (font == null) return;
        width = calculateMaxString();
        height = numberOfLines * size;
    }

    /** not implemented */
    public void setHeight(int height) {
        return;
    }

    /** not implemented */
    public void setSize(int width, int height) {
        return;
    }

    /** not implemented */
    public void setWidth(int width) {
        return;
    }

    /**
	 * returns the x coordinate relative to WORLD
	 * 
	 */
    public float getX() {
        if (fixedOnScreen) {
            return x + StaticRef.getCamera().getx();
        } else {
            return x;
        }
    }

    /**
	 * returns the y coordinate relative to WORLD
	 * 
	 */
    public float getY() {
        if (fixedOnScreen) {
            return y + StaticRef.getCamera().gety();
        } else {
            return y;
        }
    }

    /**
	 * returns the x coordinate relative to SCREEN
	 * 
	 */
    public float getXOnScreen() {
        if (fixedOnScreen) {
            return x;
        } else {
            return x - StaticRef.getCamera().getx();
        }
    }

    /**
	 * returns the y coordinate relative to SCREEN
	 * 
	 */
    public float getYOnScreen() {
        if (fixedOnScreen) {
            return y;
        } else {
            return y - StaticRef.getCamera().gety();
        }
    }

    /**
	 * return the length of the text<br>
	 * it's the same of getWidth()
	 * 
	 * @see #getWidth()
	 * @see #getHeight()
	 */
    public int getLength() {
        return width;
    }

    /**
	 * returns the length in pixel of the text<br>
	 * it's the same of getLength
	 * 
	 * @see #getLength()
	 * @see #getHeight()
	 */
    public int getWidth() {
        return width;
    }

    public void setX(float x) {
        this.x = (int) x;
    }

    public void setXY(float x, float y) {
        this.x = (int) x;
        this.y = (int) y;
    }

    public void setY(float y) {
        this.y = (int) y;
    }

    private String[] split(char split) {
        int l = text.length() - 1;
        char tchar;
        int colorState = 0;
        for (int i = 0; i < l; ++i) {
            tchar = text.charAt(i);
            if (tchar == COLOR_CHAR) {
            }
        }
        return null;
    }

    private int calculateMaxString() {
        if (text == null || font == null || text.equals("")) {
            return 0;
        }
        String texts[] = text.split("\n");
        numberOfLines = texts.length;
        int max = 0;
        int temp = 0;
        for (String mtext : texts) {
            if (max < (temp = font.getLenght(mtext))) max = temp;
        }
        return max;
    }

    @SuppressWarnings("unused")
    private int calculateNumberOfLines() {
        if (text == null) return 0;
        String texts[] = text.split("\n");
        return (numberOfLines = texts.length);
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (font.canChangeSize()) this.size = size;
    }

    /**
	 * creates a new color
	 * 
	 * @param red
	 *            red component (0-255)
	 * @param green
	 *            green component (0-255)
	 * @param blue
	 *            blue component (0-255)
	 * @return returns a special string that can be used for change the color of
	 *         the text<br>
	 *         "COLOR_CHAR(char)red(char)green(char)blue"
	 */
    public static String createColor(int red, int green, int blue) {
        if (red == COLOR_CHAR || red == '\n') --red;
        if (green == COLOR_CHAR || green == '\n') --green;
        if (blue == COLOR_CHAR || blue == '\n') --blue;
        return "" + COLOR_CHAR + (char) red + (char) green + (char) blue;
    }

    /**
	 * creates a gradient color text
	 * 
	 * @param inputText
	 *            the text to color
	 * @param startColor
	 *            the start color of the gradient
	 * @param endColor
	 *            the end color of the gradient
	 * @return the gradient color text
	 */
    public static String createGradientText(String inputText, Color startColor, Color endColor) {
        if (inputText == null || inputText.length() == 0) return "";
        String temp = "";
        int inLength = inputText.length();
        float rstep = (endColor.getRed() - startColor.getRed()) / inLength;
        float gstep = (endColor.getGreen() - startColor.getGreen()) / inLength;
        float bstep = (endColor.getBlue() - startColor.getBlue()) / inLength;
        float cred = startColor.getRed(), cgreen = startColor.getGreen(), cblue = startColor.getBlue();
        --inLength;
        for (int i = 0; i < inLength; i++) {
            temp += Text.createColor((char) cred, (char) cgreen, (char) cblue);
            temp += inputText.charAt(i);
            cred += rstep;
            cgreen += gstep;
            cblue += bstep;
        }
        temp += inputText.charAt(inLength);
        return temp;
    }

    public int getIdLayer() {
        return idLayer;
    }

    public void setIdLayer(int id) {
        if (idLayer != -1) {
            StaticRef.layers[idLayer].remove(this);
        }
        if (id < 0) {
            id = 0;
        } else if (id > StaticRef.layers.length) {
            id = StaticRef.layers.length;
        }
        idLayer = id;
        StaticRef.layers[idLayer].add(this);
    }

    /**
	 * returns if the size changes in relation of the zoomfactor of the game
	 * engine's camera
	 */
    public boolean isFixedOnScreen() {
        return fixedOnScreen;
    }

    /**
	 * indicates if the coordinates are relative to the screen or to the
	 * monitor.<br>
	 * 
	 * @param fixedOnScren
	 *            if true the coordinates are relative to the monitor else the
	 *            coordinates are relative to the screen
	 */
    public void setFixedOnScreen(boolean fixedOnScreen) {
        this.fixedOnScreen = fixedOnScreen;
    }

    /**
	 * returns if the coordinates are relative to the screen or to the monitor
	 */
    public boolean isFixedDimension() {
        return fixedDimension;
    }

    /**
	 * indicates if the text change the size in relation of the zoomFactor of
	 * the Game engine's camera
	 * 
	 * @param fixedDimension
	 *            true: don't change dimension, else change it
	 */
    public void setFixedDimension(boolean fixedDimension) {
        this.fixedDimension = fixedDimension;
    }
}
