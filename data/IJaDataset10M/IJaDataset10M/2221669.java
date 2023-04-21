package jasci.ui;

import java.util.*;
import jasci.util.*;
import jasci.ui.event.*;
import jasci.ui.prefs.UserPrefs;
import jasci.ui.prefs.DefaultUserPrefs;

public class Session {

    SessionController controller;

    OutputDevice dev;

    LinkedList<Window> windows;

    Vector<Rectangle> repaint;

    Window activeWindow;

    UserPrefs prefs;

    Vector<WindowSegment> segments;

    Vector<Widget> invalidWidgets;

    class Listener extends SessionAdapter {

        public void windowResized(SessionEvent e) {
            for (Window w : windows) if (w.maximized) w.rect.resize(dev.getScreenWidth(), dev.getScreenHeight());
            dev.clear();
            repaintAll();
        }
    }

    public Session(SessionController controller) {
        this.controller = controller;
        this.dev = controller.getOutputDevice();
        this.windows = new LinkedList<Window>();
        this.activeWindow = null;
        this.repaint = new Vector<Rectangle>();
        this.segments = null;
        this.prefs = new DefaultUserPrefs();
        this.invalidWidgets = new Vector<Widget>();
        this.controller.addListener(new Listener());
    }

    public UserPrefs getUserPrefs() {
        return prefs;
    }

    public void setUserPrefs(UserPrefs prefs) {
        this.prefs = prefs;
    }

    public void requestRevalidation(Widget wid) {
        if (invalidWidgets.contains(wid)) return;
        invalidWidgets.add(wid);
    }

    public void revalidateWidgets() {
        Widget w;
        while (invalidWidgets.size() > 0) {
            w = invalidWidgets.firstElement();
            w = invalidWidgets.remove(0);
            w.organize();
            w.requestRepaint();
        }
    }

    public void repaintRegion(Rectangle rect) {
        int i;
        if (rect.isEmpty()) return;
        for (i = 0; i < repaint.size(); i++) {
            if (repaint.get(i).contains(rect)) return;
        }
        repaint.add((Rectangle) rect.clone());
    }

    public void repaintAll() {
        for (Window w : windows) {
            repaintRegion(w.rect);
            w.root.revalidate();
        }
    }

    public int getScreenWidth() {
        return dev.getScreenWidth();
    }

    public int getScreenHeight() {
        return dev.getScreenHeight();
    }

    public void paintAsNeeded() {
        revalidateWidgets();
        if (segments == null) calculateSegments();
        for (Rectangle rect : repaint) {
            for (WindowSegment seg : segments) {
                if (seg.rect.contains(rect) && seg.win != null) {
                    seg.win.paint(new SessionPainter(seg.win.getRectangle(), rect));
                    break;
                } else {
                    Rectangle i = rect.intersection(seg.rect);
                    if (!i.isEmpty()) {
                        if (seg.win != null) seg.win.paint(new SessionPainter(seg.win.getRectangle(), i)); else dev.fillArea(' ', i.x, i.y, i.x + i.width - 1, i.y + i.height - 1, new Color(Color.WHITE, Color.BLACK));
                    }
                }
            }
        }
        repaint.clear();
        if (activeWindow != null) {
            Widget focus = activeWindow.getFocus();
            if (focus != null) {
                Point c = focus.getDesiredCursorPosition();
                if (c == null) {
                    dev.cursorSet(1, 1);
                } else {
                    c.translate(focus.getAbsoluteX() + activeWindow.rect.x, focus.getAbsoluteY() + activeWindow.rect.y);
                    dev.cursorSet(c.x + 1, c.y + 1);
                }
            }
        }
    }

    void keyEvent(int code, char keychar, int fn) {
        if (activeWindow == null) return;
        Widget w = activeWindow.getFocus();
        if (w == null) return;
        KeyEvent e = new KeyEvent(w, code, keychar, fn);
        w.processKeyEvent(e);
    }

    void keyEvent(int code, char keychar) {
        this.keyEvent(code, keychar, 0);
    }

    public void invalidateWindowSegments() {
        segments = null;
    }

    public Window getActiveWindow() {
        return activeWindow;
    }

    public void setActiveWindow() {
        for (Window w : windows) if (w.isVisible()) {
            setActiveWindow(w);
            return;
        }
    }

    public void setActiveWindow(Window w) {
        if (windows.contains(w) && activeWindow != w) {
            Window lastActive = activeWindow;
            activeWindow = w;
            if (lastActive != null) lastActive.deactivate();
            activeWindow.activate();
        }
    }

    public void receiveKeyChar(char c) {
        keyEvent(KeyEvent.KEY_CHAR, c);
    }

    public void receiveCtrlKeyChar(char c) {
        keyEvent(KeyEvent.KEY_CTRL_CHAR, c);
    }

    public void receiveFuncKey(int fn) {
        keyEvent(KeyEvent.KEY_FN, '\0', fn);
    }

    public void receiveEnter() {
        keyEvent(KeyEvent.KEY_ENTER, '\0');
    }

    public void receiveBackspace() {
        keyEvent(KeyEvent.KEY_BACKSPACE, '\0');
    }

    public void receiveUpArrow() {
        keyEvent(KeyEvent.KEY_UP, '\0');
    }

    public void receiveDownArrow() {
        keyEvent(KeyEvent.KEY_DOWN, '\0');
    }

    public void receiveRightArrow() {
        keyEvent(KeyEvent.KEY_RIGHT, '\0');
    }

    public void receiveLeftArrow() {
        keyEvent(KeyEvent.KEY_LEFT, '\0');
    }

    public void receiveTab() {
        keyEvent(KeyEvent.KEY_TAB, '\0');
    }

    public void addWindow(Window win) {
        if (win.session != null) throw new IllegalStateException("Window already in a session.");
        windows.addFirst(win);
        win.session = this;
        if (win.maximized) {
            win.rect = new Rectangle(1, 1, dev.getScreenWidth(), dev.getScreenHeight());
            win.root.revalidate();
        }
    }

    public void removeWindow(Window win) {
        if (windows.contains(win)) {
            if (win.visible) repaintRegion(win.rect);
            windows.remove(win);
            win.session = null;
            segments = null;
        }
    }

    private class WindowSegment {

        public Rectangle rect;

        public Window win;

        public WindowSegment(Rectangle rect, Window win) {
            this.rect = rect;
            this.win = win;
        }
    }

    private void calculateSegments() {
        segments = new Vector<WindowSegment>();
        LinkedList<Rectangle> parts;
        LinkedList<Rectangle> new_parts;
        for (Window win : windows) {
            if (win.isVisible() == false) continue;
            parts = new LinkedList<Rectangle>();
            parts.add(win.getRectangle());
            for (WindowSegment seg : segments) {
                new_parts = new LinkedList<Rectangle>();
                for (Rectangle part : parts) new_parts.addAll(part.minus(seg.rect));
                parts = new_parts;
            }
            for (Rectangle part : parts) segments.add(new WindowSegment(part, win));
        }
        parts = new LinkedList<Rectangle>();
        parts.add(new Rectangle(0, 0, Dimension.INFINITE, Dimension.INFINITE));
        for (WindowSegment seg : segments) {
            new_parts = new LinkedList<Rectangle>();
            for (Rectangle part : parts) new_parts.addAll(part.minus(seg.rect));
            parts = new_parts;
        }
        for (Rectangle part : parts) segments.add(new WindowSegment(part, null));
    }

    private class SessionPainter implements Painter {

        private Rectangle shape, clip;

        private int offx, offy;

        public SessionPainter(Rectangle shape, Rectangle clip) {
            this(shape, clip, 0, 0);
        }

        public SessionPainter(Rectangle shape, Rectangle clip, int offx, int offy) {
            this.shape = shape;
            this.clip = clip;
            this.offx = offx;
            this.offy = offy;
        }

        public Painter clip(Rectangle rect) {
            return this.clip(rect, 0, 0);
        }

        public Painter clip(Rectangle rect, int ox, int oy) {
            Rectangle newshape = (Rectangle) rect.clone();
            newshape.translate(shape.x - offx, shape.y - offy);
            Rectangle newclip = (Rectangle) rect.clone();
            newclip.translate(shape.x - offx, shape.y - offy);
            newclip = this.clip.intersection(newclip);
            return new SessionPainter(newshape, newclip, ox, oy);
        }

        public void drawChar(char c, int x, int y, Color color) {
            if (x < 0 || y < 0) return;
            if (x >= shape.getWidth() || y >= shape.getHeight()) return;
            x += shape.getX() - offx;
            y += shape.getY() - offy;
            if (clip.inside(x, y) == false) return;
            dev.drawChar(c, x, y, color);
        }

        public Rectangle getAreaToPaint() {
            Rectangle r = shape.intersection(clip);
            r.translate(offx - shape.x, offy - shape.y);
            return r;
        }

        public void drawHorizLine(char c, int x, int y, int length, Color color) {
            if (y < 0 || y >= shape.getHeight()) return;
            if (x < 0) {
                length += x;
                x = 0;
            }
            if (x + length > shape.getWidth()) {
                length = shape.getWidth() - x;
            }
            if (length < 1) return;
            x += shape.getX() - offx;
            y += shape.getY() - offy;
            if (y < clip.getY() || y >= clip.getY() + clip.getHeight()) return;
            if (x < clip.getX()) {
                length -= clip.getX() - x;
                x = clip.getX();
            }
            if (x + length > clip.getX2()) {
                length = clip.getX2() - x + 1;
            }
            if (length < 1) return;
            dev.drawHorizLine(c, x, y, length, color);
        }

        public void drawText(String text, int x, int y, Color color) {
            try {
                if (y < 0 || y >= shape.getHeight()) return;
                if (x < 0) {
                    text = text.substring(-x);
                    x = 0;
                }
                if (x + text.length() > shape.getWidth()) {
                    text = text.substring(0, shape.getWidth() - x - 1);
                }
                x += shape.getX() - offx;
                y += shape.getY() - offy;
                if (y < clip.getY() || y >= clip.getY() + clip.getHeight()) return;
                if (x < clip.getX()) {
                    text = text.substring(clip.getX() - x);
                    x = clip.getX();
                }
                if (x + text.length() > clip.getX2()) {
                    text = text.substring(0, clip.getX2() - x + 1);
                }
                dev.drawText(text, x, y, color);
            } catch (IndexOutOfBoundsException e) {
            }
        }

        public void drawVertLine(char c, int x, int y, int length, Color color) {
            if (x < 0 || x >= shape.getWidth()) return;
            if (y < 0) {
                length += y;
                y = 0;
            }
            if (y + length > shape.getHeight()) {
                length = shape.getHeight() - y;
            }
            if (length < 1) return;
            x += shape.getX() - offx;
            y += shape.getY() - offy;
            if (x < clip.getX() || x >= clip.getX() + clip.getWidth()) return;
            if (y < clip.getY()) {
                length += clip.getY() - y;
                y = clip.getY();
            }
            if (y + length > clip.getY2()) {
                length = clip.getY2() - y + 1;
            }
            if (length < 1) return;
            dev.drawVertLine(c, x, y, length, color);
        }

        public void fillArea(char c, int x1, int y1, int x2, int y2, Color color) {
            if (x1 < 0) x1 = 0;
            if (y1 < 0) y1 = 0;
            if (x2 >= shape.getWidth()) x2 = shape.getWidth() - 1;
            if (y2 >= shape.getHeight()) y2 = shape.getHeight() - 1;
            if (x1 > x2 || y1 > y2) return;
            x1 += shape.x - offx;
            x2 += shape.x - offx;
            y1 += shape.y - offy;
            y2 += shape.y - offy;
            x1 = Math.max(x1, clip.x);
            x2 = Math.min(x2, clip.getX2());
            y1 = Math.max(y1, clip.y);
            y2 = Math.min(y2, clip.getY2());
            dev.fillArea(c, x1, y1, x2, y2, color);
        }
    }
}
