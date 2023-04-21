package sdljavax.guichan.test.ffdemo;

import sdljavax.guichan.GUIException;
import sdljavax.guichan.evt.Key;
import sdljavax.guichan.evt.KeyListener;
import sdljavax.guichan.gfx.Graphics;
import sdljavax.guichan.gfx.Rectangle;
import sdljavax.guichan.widgets.ScrollArea;

public class FFScrollArea extends ScrollArea implements KeyListener {

    public FFScrollArea() throws GUIException {
        super();
        setScrollPolicy(SHOW_NEVER, SHOW_ALWAYS);
        addKeyListener(this);
        setFocusable(false);
        setBorderSize(0);
    }

    public void draw(Graphics graphics) throws GUIException {
        graphics.pushClipArea(getContent().getDimension());
        getContent().draw(graphics);
        graphics.popClipArea();
        if (0 != getVerticalMaxScroll()) {
            int y = ((getHeight() - 32) * getVerticalScrollAmount()) / getVerticalMaxScroll();
            graphics.setColor(0x000000);
            graphics.drawRectangle(new Rectangle(getWidth() - 11, y, 8, 32));
            graphics.drawRectangle(new Rectangle(getWidth() - 10, y + 1, 8, 32));
            graphics.setColor(0xffffff);
            graphics.fillRectangle(new Rectangle(getWidth() - 10, y + 1, 6, 30));
        }
    }

    public void keyPress(Key key) throws GUIException {
        if (key.getValue() == Key.DOWN) {
            setVerticalScrollAmount(getVerticalScrollAmount() + 16);
        } else if (key.getValue() == Key.UP) {
            setVerticalScrollAmount(getVerticalScrollAmount() - 16);
        }
    }

    public void keyRelease(Key key) throws GUIException {
    }
}
