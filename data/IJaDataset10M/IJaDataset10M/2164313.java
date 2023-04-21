package org.jouvieje.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import org.jouvieje.font.IFont;
import org.jouvieje.math.Vector3f;
import org.jouvieje.opengl.IGL;
import org.jouvieje.opengl.IGLApi;
import org.jouvieje.opengl.helper.ShapeUtil;
import org.jouvieje.opengl.helper.ViewingVolumeHelper;
import org.jouvieje.view.Viewport;

/**
 * <H2></H2>
 * <HR>
 * Description goes here. If you see this message, please contact me and the description will be filled.<BR>
 * <BR>
 * @author J�r�me JOUVIE (Jouvieje)
 * @mail <A HREF="mailto:jerome.jouvie@gmail.com">jerome.jouvie@gmail.com</A>
 * @site <A HREF="http://jerome.jouvie.free.fr/">http://jerome.jouvie.free.fr/</A>
 * @project   Java OpenGl BaseCode
 * @homepage  <A HREF="http://jerome.jouvie.free.fr/OpenGl/BaseCode/BaseCode.php">http://jerome.jouvie.free.fr/OpenGl/BaseCode/BaseCode.php</A>
 * @copyright Copyright � 2004-2008 J�r�me JOUVIE (Jouvieje)
 */
public class GLTextBox extends GlPrintableComponent implements IRenderable {

    protected static final int DISPLAY_CARET_TIME = 500;

    protected boolean focus = false;

    protected int caretPosition = 0;

    protected StringBuffer text = new StringBuffer("Text field");

    protected String texto[];

    protected int indiceMaiorLinha = 0;

    protected float FONTSIZE = 0.034f;

    protected float[] color = new float[] { 1.0f, 1.0f, 1.0f };

    private int texture = 0;

    /**
	 * Determine if the texture is blending
	 */
    private boolean pictureHaveAlphaLayer = false;

    protected float[] backColor;

    protected float[] borderColor;

    protected int borderWidth = 2;

    private boolean agudo, circumflexo, til, grave;

    public GLTextBox() {
        super();
    }

    private char letra;

    private int numLinha;

    public void render(IGLApi glRenderer) {
        if (isVisible()) {
            IGL gl = glRenderer.getGL();
            Viewport viewport = glRenderer.getSceneTarget().getScene().getViewport();
            if (isPictureHaveAlphaLayer()) gl.glPushAttrib(IGL.GL_COLOR_BUFFER_BIT);
            ViewingVolumeHelper.beginOrtho(glRenderer);
            if (isPictureHaveAlphaLayer()) {
                gl.glBlendFunc(IGL.GL_SRC_ALPHA, IGL.GL_ONE_MINUS_SRC_ALPHA);
                gl.glEnable(IGL.GL_BLEND);
            }
            boolean textureActive = gl.glIsEnabled(IGL.GL_TEXTURE_2D);
            if (!textureActive) gl.glEnable(IGL.GL_TEXTURE_2D);
            if (texture != 0) {
                gl.glBindTexture(IGL.GL_TEXTURE_2D, texture);
                ShapeUtil.drawRectangle_ZPlane_VtxUv(gl, (float) getSize().width, (float) getSize().height, 1.0f, 1.0f, new Vector3f(getLocation().x, viewport.height - getLocation().y, 0));
            }
            if (isPictureHaveAlphaLayer()) gl.glPopAttrib();
            gl.glPushAttrib(IGL.GL_CURRENT_BIT);
            float tempColor[] = getColor();
            if (!isEnable() && getDisabledColor() != null) tempColor = getDisabledColor();
            if (tempColor != null) gl.glColor(tempColor[0], tempColor[1], tempColor[2]);
            int i = 0;
            while (i < numLinha) {
                getFontRenderer().drawTextAt(texto[i], IFont.FontAlign.Left, (int) FONTSIZE, new Vector3f(getLocation().x * 1.05f + 2 * borderWidth, viewport.height - getCenter().y * 0.90f - (i * FONTSIZE), 0.0f));
                i++;
            }
            ViewingVolumeHelper.endOrtho(glRenderer);
            gl.glPopAttrib();
        }
    }

    /**
	 * Define the center of the component.<BR>
	 * You MUST call setText before this method.<BR>
	 * <B><U>WARNING :</U></B>The (0, 0) position id the top-left point.
	 * @param center the center position of the component.
	 */
    public void setCenter(Point center) {
        pack();
        setLocation(center.x - getSize().width / 2, center.y + getSize().height / 2);
    }

    /**
	 * Define the center of the component.<BR>
	 * You MUST call setText before this method.<BR>
	 * <B><U>WARNING :</U></B>The (0, 0) position id the top-left point.
	 * @param x x position of the center
	 * @param y y position of the center
	 */
    public void setCenter(int x, int y) {
        setCenter(new Point(x, y));
    }

    /**
	 * Set the location of the bottom left point of the component
	 * <B><U>WARNING :</U></B> the (0, 0) point is the top left point !
	 * @param location position of the bottom left point
	 */
    public void setLocation(Point location) {
        super.setLocation(location);
        pack();
    }

    /**
	 * Set the location of the bottom left point of the component
	 * <B><U>WARNING :</U></B> the (0, 0) point is the top left point !
	 * @param x x position of the bottom left point
	 * @param y y position of the bottom left point
	 */
    public void setLocation(int x, int y) {
        setLocation(new Point(x, y));
    }

    /**
	 * Set the size of the component
	 * @param width width of the component
	 * @param height height of the component
	 */
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    /**
	 * Set the size of the component
	 * @param size size of the component
	 */
    public void setSize(Dimension size) {
        super.setSize(size);
    }

    public void pack() {
    }

    protected Dimension calculateDimension(String string) {
        Dimension newSize = new Dimension();
        newSize.width = (int) ((int) getFontRenderer().getTextSize(string, (int) FONTSIZE).getX() * 1.39f);
        newSize.height = (int) ((int) getFontRenderer().getTextSize(string, (int) FONTSIZE).getY() * numLinha * 1.6f);
        return new Dimension(newSize.width + 2 * borderWidth, newSize.height + 2 * borderWidth);
    }

    public boolean tratarAcentos(KeyEvent ke) {
        if (agudo) {
            switch(ke.getKeyChar()) {
                case 'A':
                    letra = '�';
                    break;
                case 'a':
                    letra = '�';
                    break;
                case 'e':
                    letra = '�';
                    break;
                case 'E':
                    letra = '�';
                    break;
                case 'i':
                    letra = '�';
                    break;
                case 'I':
                    letra = '�';
                    break;
                case 'o':
                    letra = '�';
                    break;
                case 'O':
                    letra = '�';
                    break;
                case 'u':
                    letra = '�';
                    break;
                case 'U':
                    letra = '�';
                    break;
                default:
                    return agudo = false;
            }
        }
        if (til) {
            switch(ke.getKeyChar()) {
                case 'a':
                    letra = '�';
                    break;
                case 'A':
                    letra = '�';
                    break;
                case 'o':
                    letra = '�';
                    break;
                case 'O':
                    letra = '�';
                    break;
                default:
                    return til = false;
            }
        }
        if (grave) {
            switch(ke.getKeyCode()) {
                case 'A':
                    letra = '�';
                    break;
                case 'a':
                    letra = '�';
                    break;
                case 'e':
                    letra = '�';
                    break;
                case 'E':
                    letra = '�';
                    break;
                case 'i':
                    letra = '�';
                    break;
                case 'I':
                    letra = '�';
                    break;
                case 'o':
                    letra = '�';
                    break;
                case 'O':
                    letra = '�';
                    break;
                case 'u':
                    letra = '�';
                    break;
                case 'U':
                    letra = '�';
                    break;
                default:
                    return grave = false;
            }
        }
        if (circumflexo) {
            switch(ke.getKeyCode()) {
                case 'A':
                    letra = '�';
                    break;
                case 'a':
                    letra = '�';
                    break;
                case 'e':
                    letra = '�';
                    break;
                case 'E':
                    letra = '�';
                    break;
                case 'o':
                    letra = '�';
                    break;
                case 'O':
                    letra = '�';
                    break;
                default:
                    return circumflexo = false;
            }
        }
        return true;
    }

    protected void onKeyTyped(KeyEvent ke) {
    }

    protected void onKeyReleased(KeyEvent ke) {
    }

    protected void onKeyPressed(KeyEvent ke) {
        if ((agudo || til || circumflexo || grave) && (ke.getKeyCode() != KeyEvent.VK_SHIFT)) {
            if (tratarAcentos(ke)) {
                text.insert(caretPosition + 1, letra);
                caretPosition++;
                agudo = til = circumflexo = grave = false;
            } else {
                text.insert(caretPosition + 1, ke.getKeyChar());
                caretPosition++;
                agudo = til = circumflexo = grave = false;
            }
            return;
        } else {
            switch(ke.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                case KeyEvent.VK_A:
                case KeyEvent.VK_B:
                case KeyEvent.VK_C:
                case KeyEvent.VK_D:
                case KeyEvent.VK_E:
                case KeyEvent.VK_F:
                case KeyEvent.VK_G:
                case KeyEvent.VK_H:
                case KeyEvent.VK_I:
                case KeyEvent.VK_J:
                case KeyEvent.VK_K:
                case KeyEvent.VK_L:
                case KeyEvent.VK_M:
                case KeyEvent.VK_N:
                case KeyEvent.VK_O:
                case KeyEvent.VK_P:
                case KeyEvent.VK_Q:
                case KeyEvent.VK_R:
                case KeyEvent.VK_S:
                case KeyEvent.VK_T:
                case KeyEvent.VK_U:
                case KeyEvent.VK_V:
                case KeyEvent.VK_W:
                case KeyEvent.VK_X:
                case KeyEvent.VK_Y:
                case KeyEvent.VK_Z:
                case KeyEvent.VK_EQUALS:
                case KeyEvent.VK_MULTIPLY:
                case KeyEvent.VK_ADD:
                case KeyEvent.VK_SUBTRACT:
                case KeyEvent.VK_DECIMAL:
                case KeyEvent.VK_DIVIDE:
                case KeyEvent.VK_OPEN_BRACKET:
                case KeyEvent.VK_BACK_SLASH:
                case KeyEvent.VK_CLOSE_BRACKET:
                case KeyEvent.VK_COMMA:
                case KeyEvent.VK_MINUS:
                case KeyEvent.VK_PERIOD:
                case KeyEvent.VK_SLASH:
                case KeyEvent.VK_BACK_QUOTE:
                case KeyEvent.VK_QUOTE:
                case KeyEvent.VK_AMPERSAND:
                case KeyEvent.VK_ASTERISK:
                case KeyEvent.VK_QUOTEDBL:
                case KeyEvent.VK_LESS:
                case KeyEvent.VK_GREATER:
                case KeyEvent.VK_BRACELEFT:
                case KeyEvent.VK_BRACERIGHT:
                case KeyEvent.VK_AT:
                case KeyEvent.VK_SEMICOLON:
                case KeyEvent.VK_COLON:
                case KeyEvent.VK_CIRCUMFLEX:
                case KeyEvent.VK_DOLLAR:
                case KeyEvent.VK_EURO_SIGN:
                case KeyEvent.VK_EXCLAMATION_MARK:
                case KeyEvent.VK_INVERTED_EXCLAMATION_MARK:
                case KeyEvent.VK_LEFT_PARENTHESIS:
                case KeyEvent.VK_NUMBER_SIGN:
                case KeyEvent.VK_PLUS:
                case KeyEvent.VK_RIGHT_PARENTHESIS:
                case KeyEvent.VK_UNDERSCORE:
                case KeyEvent.VK_0:
                case KeyEvent.VK_1:
                case KeyEvent.VK_2:
                case KeyEvent.VK_3:
                case KeyEvent.VK_4:
                case KeyEvent.VK_5:
                case KeyEvent.VK_6:
                case KeyEvent.VK_7:
                case KeyEvent.VK_8:
                case KeyEvent.VK_9:
                case KeyEvent.VK_NUMPAD0:
                case KeyEvent.VK_NUMPAD1:
                case KeyEvent.VK_NUMPAD2:
                case KeyEvent.VK_NUMPAD3:
                case KeyEvent.VK_NUMPAD4:
                case KeyEvent.VK_NUMPAD5:
                case KeyEvent.VK_NUMPAD6:
                case KeyEvent.VK_NUMPAD7:
                case KeyEvent.VK_NUMPAD8:
                case KeyEvent.VK_NUMPAD9:
                    text.insert(caretPosition + 1, ke.getKeyChar());
                    caretPosition++;
                    break;
                case KeyEvent.VK_BACK_SPACE:
                case KeyEvent.VK_CANCEL:
                case KeyEvent.VK_CLEAR:
                    if (text.length() > 0 && caretPosition >= 0) {
                        text.delete(caretPosition, caretPosition + 1);
                        caretPosition--;
                    }
                    break;
                case KeyEvent.VK_DELETE:
                    if (text.length() > 0 && caretPosition < text.length() - 1) {
                        text.delete(caretPosition + 1, caretPosition + 2);
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (caretPosition >= 0) caretPosition--;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (caretPosition < text.length() - 1) caretPosition++;
                    break;
            }
        }
        if (ke.getKeyChar() == '�') {
            text.insert(caretPosition + 1, ke.getKeyChar());
            caretPosition++;
        }
        if (ke.getKeyChar() == '�') agudo = true;
        if (ke.getKeyChar() == '`') grave = true;
        if (ke.getKeyChar() == '~') til = true;
        if (ke.getKeyChar() == '^') circumflexo = true;
    }

    protected void onMouseClicked(MouseEvent me) {
    }

    protected void onMouseEntered(MouseEvent me) {
        focus = true;
    }

    protected void onMouseExited(MouseEvent me) {
        focus = false;
    }

    protected void onMousePressed(MouseEvent me) {
    }

    protected void onMouseReleased(MouseEvent me) {
    }

    protected void onMouseMoved(MouseEvent me) {
    }

    protected void onMouseDragged(MouseEvent me) {
    }

    /**
	 * @param text text to be drawn into the component
	 */
    public void setText(String text) {
        int i = 0;
        numLinha = 0;
        while (i < text.length()) {
            if (text.charAt(i) == '\n') numLinha++;
            i++;
        }
        texto = new String[numLinha];
        i = 0;
        int lastEndLine = -1;
        int linhaAtual = 0;
        while (i < text.length()) {
            if (text.charAt(i) == '\n') {
                texto[linhaAtual] = new String();
                texto[linhaAtual] = text.substring(lastEndLine + 1, i);
                if ((i - lastEndLine) > lastEndLine) indiceMaiorLinha = linhaAtual;
                linhaAtual++;
                lastEndLine = i;
            }
            i++;
        }
        setSize(calculateDimension(texto[indiceMaiorLinha]));
        if (getOwner() != null && ownerOwnRegion) getOwner().notifyEvent(NotifyEventEnums.PACK_EVENT); else pack();
    }

    /**
	 * @return the text that is drawn into the component
	 */
    public String getText() {
        return text.toString();
    }

    /**
	 * Color of the text
	 * @param color must be a rgb color in a float[3] array.<BR>
	 * Each values must be include into the interval [0.f, 1.f]
	 */
    public void setColor(float[] color) {
        if (color.length == 3) this.color = color;
    }

    /**
	 * @return the color of the text of the label
	 */
    public float[] getColor() {
        return color;
    }

    public float[] getBackColor() {
        return backColor;
    }

    public void setBackColor(float[] backColor) {
        this.backColor = backColor;
    }

    public float[] getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(float[] borderColor) {
        this.borderColor = borderColor;
    }

    /**
	 * Set the texture of the component
	 * @param texture reference of the texture of the component
	 */
    public void setTexture(int texture) {
        this.texture = texture;
    }

    /**
	 * Get the texture used by the component.
	 * @return the reference of the texture
	 */
    public int getTexture() {
        return texture;
    }

    /**
	 * @param blended defines if the textures must be blended
	 */
    public void setPictureHaveAlphaLayer(boolean blended) {
        this.pictureHaveAlphaLayer = blended;
    }

    /**
	 * @return true if textures are blended
	 */
    public boolean isPictureHaveAlphaLayer() {
        return pictureHaveAlphaLayer;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public float getFONTSIZE() {
        return FONTSIZE;
    }

    public void setFONTSIZE(float fontsize) {
        FONTSIZE = fontsize;
        setSize(calculateDimension(texto[indiceMaiorLinha]));
    }
}
