package sdljavax.guichan.widgets;

import sdljavax.guichan.GUIException;
import sdljavax.guichan.evt.Key;
import sdljavax.guichan.evt.KeyListener;
import sdljavax.guichan.evt.MouseInput;
import sdljavax.guichan.evt.MouseListener;
import sdljavax.guichan.gfx.Color;
import sdljavax.guichan.gfx.Graphics;
import sdljavax.guichan.gfx.Rectangle;

/**
 * This is a normal CheckBox. It can be checked and unchecked, and that is basicly it.
 * 
 * @author Rainer Koschnick <a href="mailto:arkay@gmx.net"><arkay@gmx.net></a>
 */
public class CheckBox extends Widget implements MouseListener, KeyListener {

    protected boolean m_bMarked;

    protected String m_strCaption;

    /**
	 * Contructor.
	 * @throws GUIException 
	 */
    public CheckBox() throws GUIException {
        this(null, false);
    }

    /**
	 * Constructor.
	 * 
	 * @param caption
	 *            the caption of the CheckBox.
	 * @throws GUIException 
	 */
    public CheckBox(String caption) throws GUIException {
        this(caption, false);
    }

    /**
	 * Constructor.
	 * 
	 * @param caption
	 *            the caption of the CheckBox.
	 * @param marked
	 *            true if the CheckBox should be marked (default is false)
	 * @throws GUIException 
	 */
    public CheckBox(String caption, boolean marked) throws GUIException {
        super();
        setCaption(caption);
        setMarked(marked);
        setFocusable(true);
        addMouseListener(this);
        addKeyListener(this);
        adjustSize();
    }

    /**
	 * Adjusts the CheckBox size to fit the font size.
	 */
    public void adjustSize() {
        int height = getFont().getHeight();
        setHeight(height);
        setWidth(getFont().getWidth(m_strCaption) + height + height / 2);
    }

    public void draw(Graphics graphics) throws GUIException {
        drawBox(graphics);
        graphics.setFont(getFont());
        graphics.setColor(getForegroundColor());
        int h = getHeight() + getHeight() / 2;
        graphics.drawText(getCaption(), h - 2, 0);
        if (hasFocus()) {
            graphics.drawRectangle(new Rectangle(h - 4, 0, getWidth() - h + 3, getHeight()));
        }
    }

    public void drawBorder(Graphics graphics) throws GUIException {
        Color faceColor = getBaseColor();
        Color highlightColor, shadowColor;
        int alpha = getBaseColor().a;
        int width = getWidth() + getBorderSize() * 2 - 1;
        int height = getHeight() + getBorderSize() * 2 - 1;
        highlightColor = faceColor.add(0x303030);
        highlightColor.a = alpha;
        shadowColor = faceColor.subtract(0x303030);
        shadowColor.a = alpha;
        for (int i = 0; i < getBorderSize(); ++i) {
            graphics.setColor(shadowColor);
            graphics.drawLine(i, i, width - i, i);
            graphics.drawLine(i, i, i, height - i);
            graphics.setColor(highlightColor);
            graphics.drawLine(width - i, i, width - i, height - i);
            graphics.drawLine(i, height - i, width - i, height - i);
        }
    }

    /**
	 * Draws the box i.a not the caption.
	 * 
	 * @param graphics
	 *            a graphics object.
	 */
    protected void drawBox(Graphics graphics) throws GUIException {
        int h = getHeight() - 1;
        int alpha = getBaseColor().a;
        Color faceColor = getBaseColor();
        faceColor.a = alpha;
        Color highlightColor = faceColor.add(0x303030);
        highlightColor.a = alpha;
        Color shadowColor = faceColor.subtract(0x303030);
        shadowColor.a = alpha;
        graphics.setColor(shadowColor);
        graphics.drawLine(0, 0, h, 0);
        graphics.drawLine(0, 1, 0, h);
        graphics.setColor(highlightColor);
        graphics.drawLine(h, 1, h, h);
        graphics.drawLine(1, h, h - 1, h);
        graphics.setColor(getBackgroundColor());
        graphics.fillRectangle(new Rectangle(1, 1, h - 1, h - 1));
        graphics.setColor(getForegroundColor());
        if (m_bMarked) {
            graphics.drawLine(3, 5, 3, h - 3);
            graphics.drawLine(4, 5, 4, h - 3);
            graphics.drawLine(5, h - 4, h - 2, 3);
            graphics.drawLine(5, h - 5, h - 4, 4);
        }
    }

    /**
	 * @return the caption of the CheckBox.
	 */
    public String getCaption() {
        return m_strCaption;
    }

    /**
	 * @return true if the CheckBox is marked.
	 */
    public boolean isMarked() {
        return m_bMarked;
    }

    public void keyPress(Key key) throws GUIException {
        if (Key.ENTER == key.getValue() || Key.SPACE == key.getValue()) {
            toggle();
        }
    }

    public void keyRelease(Key key) throws GUIException {
    }

    public void mouseClick(int x, int y, int button, int count) throws GUIException {
        if (MouseInput.LEFT == button) {
            toggle();
        }
    }

    public void mouseIn() {
    }

    public void mouseMotion(int x, int y) throws GUIException {
    }

    public void mouseOut() {
    }

    public void mousePress(int x, int y, int button) throws GUIException {
    }

    public void mouseRelease(int x, int y, int button) throws GUIException {
    }

    public void mouseWheelDown(int x, int y) throws GUIException {
    }

    public void mouseWheelUp(int x, int y) throws GUIException {
    }

    /**
	 * Sets the caption of the CheckBox.
	 * 
	 * @param caption
	 *            the CheckBox caption.
	 */
    public void setCaption(String caption) {
        m_strCaption = caption;
    }

    /**
	 * Set the CheckBox marked.
	 * 
	 * @param marked
	 *            true if the CheckBox should be marked.
	 */
    public void setMarked(boolean marked) {
        m_bMarked = marked;
    }

    /**
	 * Toggle between marked and unmarked.
	 */
    protected void toggle() throws GUIException {
        m_bMarked = !m_bMarked;
        generateAction();
    }
}
