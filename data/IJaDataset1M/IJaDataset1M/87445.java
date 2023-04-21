package net.sf.btw.ibtu.ui;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * This class defines an initial appliction canvas -- the splash screen.
 * 
 * @author Jan Tomka
 * TODO Remove OK command and dismiss at any key pressed.
 */
public class SplashScreen extends Canvas {

    /**
	 * Reference to the {@link javax.microedition.lcdui.Display} the splash
	 * screen is displayed on.
	 */
    private Display display;

    /**
	 * Reference to the {@link javax.microedition.lcdui.Displayable} which is to
	 * be set as current after splash screen is OK'ed.
	 */
    private Displayable nextDisplayable;

    /**
	 * Color used to draw background of a splash screen.
	 */
    private final int backgroundColor = 0xffffff;

    /**
	 * Color used to draw the text of a splash screen.
	 */
    private final int textColor = 0xbbbbbb;

    /**
	 * Small font used to draw text of a splash screen.
	 */
    private final Font smallFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_ITALIC, Font.SIZE_SMALL);

    /**
	 * Large font used to draw text of a splash screen.
	 */
    private final Font largeFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_ITALIC, Font.SIZE_LARGE);

    /**
	 * Creates new SplashScreen instance.
	 * 
	 * @param display
	 *            Display the splash screen is painted on.
	 * @param nextDisplayable
	 *            Displayable to be switched to after splash screen is OK'ed.
	 */
    public SplashScreen(Display display, Displayable nextDisplayable) {
        super();
        this.display = display;
        this.nextDisplayable = nextDisplayable;
    }

    public void paint(Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(textColor);
        g.setFont(largeFont);
        String str[] = { "I BlueTooth You", "(c) 2007 Moto & Judas", "http://btw.sf.net" };
        int yOffset[] = { -largeFont.getHeight(), 0, smallFont.getHeight() };
        g.drawString(str[0], getWidth() / 2, getHeight() / 2 + yOffset[0], Graphics.BASELINE | Graphics.HCENTER);
        g.setFont(smallFont);
        g.drawString(str[1], getWidth() / 2, getHeight() / 2 + yOffset[1], Graphics.BASELINE | Graphics.HCENTER);
        g.drawString(str[2], getWidth() / 2, getHeight() / 2 + yOffset[2], Graphics.BASELINE | Graphics.HCENTER);
    }

    public void keyPressed(int keyCode) {
        display.setCurrent(nextDisplayable);
    }
}
