package sourceforge.shinigami.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;
import java.util.LinkedList;
import sourceforge.shinigami.graphics.Texture;

public abstract class SWindow extends SComponent implements MouseListener, MouseMotionListener, KeyListener {

    private static SWindow focus = null;

    private Texture bar = new Texture("trashcan/Bar.bmp");

    private int last_x = 10000, last_y = 10000, x, y;

    private boolean moving = false;

    private boolean hasBar = true;

    private SButton close;

    private final int barHeight = 15;

    private boolean startExit = false;

    protected boolean exit = false;

    private WindowButtonListener btnlstn = new WindowButtonListener();

    private boolean hasClose = true;

    private final int FOCUS_MODE;

    static final int NORMAL_FOCUS_MODE = 0;

    static final int TOP_ALWAYS_MODE = 1;

    static final int BOTTOM_ALWAYS_MODE = 2;

    protected boolean active = false;

    private LinkedList<SComponent> components = new LinkedList<SComponent>();

    private LinkedList<MouseListener> mouseListeners = new LinkedList<MouseListener>();

    private LinkedList<MouseMotionListener> mouseMotionListeners = new LinkedList<MouseMotionListener>();

    private LinkedList<KeyListener> keyListeners = new LinkedList<KeyListener>();

    public SWindow() {
        this(NORMAL_FOCUS_MODE);
    }

    public SWindow(int focusMode) {
        this.FOCUS_MODE = focusMode;
    }

    public void hasBar(boolean hasBar) {
        this.hasBar = hasBar;
    }

    public void hasClose(boolean hasClose) {
        this.hasClose = hasClose;
    }

    public void initGraphics() {
        if (hasClose) {
            close = new SButton(new Texture("trashcan/Close.bmp"), null);
            close.setSize(getX() + barHeight - 2, getY() + barHeight - 2);
            close.setPosition(getWidth() - (barHeight - 1), 1);
            close.hasBorder(false);
            close.addButtonListener(btnlstn);
            add(close);
            mouseListeners.add(close);
        }
        if (hasBar) {
            mouseListeners.add(this);
            mouseMotionListeners.add(this);
            bar = new Texture(bar.getImage().getScaledInstance(getWidth(), barHeight, Image.SCALE_DEFAULT));
        }
        keyListeners.add(this);
    }

    public boolean getActive() {
        return active;
    }

    public boolean getExit() {
        return exit;
    }

    public void processedExit() {
        startExit = false;
        exit = false;
    }

    public void add(SComponent component) {
        components.add(component);
    }

    public void remove(SComponent component) {
        components.remove(component);
    }

    public void addListener(EventListener listener) {
        if (listener.getClass() == MouseListener.class) {
        }
    }

    public void addMouseListener(MouseListener mouseListener) {
        mouseListeners.add(mouseListener);
    }

    public void addKeyListener(KeyListener keyListener) {
        keyListeners.add(keyListener);
    }

    public void addMouseMotionListener(MouseMotionListener mouseMotionListener) {
        mouseMotionListeners.add(mouseMotionListener);
    }

    public abstract void setListeners();

    public void removeMouseListener(MouseListener mouseListener) {
        mouseListeners.remove(mouseListener);
    }

    public void removeKeyListener(KeyListener keyListener) {
        keyListeners.remove(keyListener);
    }

    public void removeMouseMotionListener(MouseMotionListener mouseMotionListener) {
        mouseMotionListeners.remove(mouseMotionListener);
    }

    public LinkedList<MouseListener> getMouseListeners() {
        return mouseListeners;
    }

    public LinkedList<KeyListener> getKeyListeners() {
        return keyListeners;
    }

    public LinkedList<MouseMotionListener> getMouseMotionListeners() {
        return mouseMotionListeners;
    }

    @Override
    public void render(Graphics2D g, int adjustX, int adjustY) {
        if (hasBar) bar.render(g, adjustX, adjustY);
        for (SComponent c : components) {
            c.render(g, adjustX, adjustY);
        }
        if (hasClose) close.render(g, adjustX, adjustY);
    }

    @Override
    public void update() {
        for (SComponent c : components) {
            c.update();
        }
    }

    boolean isTopAlways() {
        return FOCUS_MODE == TOP_ALWAYS_MODE;
    }

    boolean isBottomAlways() {
        return FOCUS_MODE == BOTTOM_ALWAYS_MODE;
    }

    public boolean hasFocus() {
        return (this == SWindow.focus);
    }

    public void grabFocus() {
        SWindow.giveFocus(this);
    }

    /**
	 * This method transfers the focus to the parameter window. It alerts the
	 * current focused window that it's losing it's focus, and alerts the new
	 * window that it's gaining focus
	 * 
	 * @param window The window which must get focus
	 */
    static void giveFocus(SWindow window) {
        if (window != focus) {
            if (focus != null) focus.sys_onLoseFocus();
            SWindow.focus = window;
            window.sys_onGainFocus();
        }
    }

    /** This system method is called only when the window gains focus */
    protected void sys_onGainFocus() {
    }

    /** This system method is called only when the window loses focus */
    protected void sys_onLoseFocus() {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() >= getX() && e.getX() <= (getX() + getWidth())) if (e.getY() >= getY() && e.getY() <= (getY() + getHeight())) if (active) grabFocus();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (hasBar) {
            moving = false;
            last_x = 10000;
            last_y = 10000;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (active) if (((e.getY() <= getY() + 15 && e.getY() > getY()) || moving) && hasBar) {
            if (!moving) {
                moving = true;
            }
            if (last_x >= 8000 && last_y >= 8000) {
                last_x = e.getX();
                last_y = e.getY();
                x = last_x;
                y = last_y;
            } else {
                x = e.getX();
                y = e.getY();
                setPosition(getX() + (x - last_x), getY() + y - last_y);
                for (SComponent c : components) {
                    c.setPosition(c.getX() + (x - last_x), c.getY() + y - last_y);
                }
                last_x = x;
                last_y = y;
            }
        }
    }

    @Override
    public void on() {
        active = true;
    }

    @Override
    public void off() {
        active = false;
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
            exit = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    public class WindowButtonListener implements IButtonListener {

        @Override
        public void buttonDown(SButton sender) {
            if (sender == close && active && hasClose) {
                startExit = true;
            }
        }

        @Override
        public void buttonEntered(SButton sender) {
        }

        @Override
        public void buttonExited(SButton sender) {
        }

        @Override
        public void buttonPressed(SButton sender) {
        }

        @Override
        public void buttonUp(SButton sender) {
            if (sender == close && active && hasClose) {
                if (startExit) exit = true;
            }
        }
    }
}
