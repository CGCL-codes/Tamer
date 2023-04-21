package onepoint.express;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.MemoryImageSource;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import onepoint.log.XLog;
import onepoint.log.XLogFactory;
import onepoint.service.XClient;

/**
 * Class that handles the common behaviour for all the concrete viewers of the application.
 *
 * @author horia.chiorean
 */
public class XExpressViewerWrapper implements XViewer {

    /**
    * where we are running...
    */
    static final String osName = System.getProperty("os.name").intern();

    static final String OS_MAC_OS_X = "Mac OS X".intern();

    /**
    * This class's logger.
    */
    private static final XLog logger = XLogFactory.getLogger(XExpressViewerWrapper.class);

    /**
    * Constants used by subclasses
    */
    public static final String SESSION_EXPIRED_PARAMETER = "sessionExpired";

    public static final String SESSION_INVALIDATED_PARAMETER = "sessionInvalidated";

    public static final String DEFAULT_HOST = "localhost";

    /**
    * The AWT component this class is linked to.
    */
    protected XViewer wrappedViewer = null;

    /**
    * This class display.
    */
    private XDisplay display = null;

    /**
    * The double buffer used for painting operations.
    */
    private Image doubleBuffer = null;

    /**
    * Boolean that indicates whether the wrappedViewer is focused or not.
    */
    private boolean focused = false;

    /**
    * A list of exit handlers.
    */
    private java.util.Set exitHandlers;

    /**
    * An image loader used for loading images.
    */
    private XImageLoader imageLoader = null;

    /**
    * A map of client variables.
    */
    private Map clientVariables = new HashMap();

    private static final int CLICK_AREA = 3;

    private Integer clickX;

    private Integer clickY;

    /**
    * Creates a new instance of this class and wraps it around the given viewer
    *
    * @param wrappedViewer a <code>XViewer</code> instance which *must* extend java.awt.Container.
    */
    protected XExpressViewerWrapper(XViewer wrappedViewer) {
        this.wrappedViewer = wrappedViewer;
    }

    /**
    * Creates a new viewer instance, wrapping it around another viewer.
    *
    * @param viewer a <code>XViewer</code> instance which *must* extend java.awt.Container
    * @return an <code>XExpressViewerWrapper</code> instance.
    * @throws IllegalArgumentException if the given viewer is not an instance of java.awt.Container.
    */
    public static XExpressViewerWrapper createExpressViewerWrapper(XViewer viewer) {
        if (!(viewer instanceof Container)) {
            throw new IllegalArgumentException("Cannot create a new ExpressViewerWrapper for the given viewer class: " + viewer.getClass() + ". Expected an instance of java.awt.Container");
        }
        return new XExpressViewerWrapper(viewer);
    }

    /**
    * Initializes the wrapped viewer. A value of -1 for either the width or the height indicates that the width and the
    * height of the display should not be changed.
    *
    * @param width  a <code>int</code> representing the width of the wrappedViewer.
    * @param height a <code>int</code> representing the height of the wrappedViewer.
    */
    public void initWrappedViewer(int width, int height) {
        int MOUSE_WHEEL = 0;
        try {
            MOUSE_WHEEL = MouseEvent.class.getField("MOUSE_WHEEL").getInt(null);
        } catch (Exception e) {
            logger.error("Cannot find mouse wheel event", e);
        }
        if (MOUSE_WHEEL != 0) {
            try {
                wrappedViewer.getClass().getMethod("setFocusTraversalKeysEnabled", new Class[] { Boolean.TYPE }).invoke(wrappedViewer, new Object[] { Boolean.FALSE });
            } catch (Exception e) {
                logger.error("Cannot set focus traversal", e);
            }
        }
        try {
            Long eventsMask = new Long(AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK | AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK);
            Class componentClass = wrappedViewer.getClass();
            while (!componentClass.getName().equals(Component.class.getName())) {
                componentClass = componentClass.getSuperclass();
            }
            Method enableEventsMethod = componentClass.getDeclaredMethod("enableEvents", new Class[] { Long.TYPE });
            enableEventsMethod.setAccessible(true);
            enableEventsMethod.invoke(wrappedViewer, new Object[] { eventsMask });
        } catch (Exception e) {
            logger.error("Cannot enable wrappedViewer awt events", e);
            return;
        }
        this.display = new XDisplay(this.wrappedViewer);
        if (width != -1 && height != -1) {
            Insets insets = ((Container) wrappedViewer).getInsets();
            Rectangle displayBounds = new Rectangle();
            displayBounds.x = insets.left;
            displayBounds.y = insets.top;
            displayBounds.width = width - insets.left - insets.right;
            displayBounds.height = height - insets.top - insets.bottom;
            this.display.setBounds(displayBounds);
        }
        this.imageLoader = new XImageLoader((Component) wrappedViewer);
        this.exitHandlers = new HashSet();
    }

    /**
    * Paints the given wrappedViewer using the given graphics object.
    *
    * @param g <code>Graphics</code> objects used to do the painting.
    */
    public void paintWrappedViewer(Graphics g) {
        Rectangle frameBounds = ((Container) wrappedViewer).getBounds();
        Insets insets = ((Container) wrappedViewer).getInsets();
        Rectangle bounds = new Rectangle();
        bounds.width = frameBounds.width - insets.left - insets.right;
        bounds.height = frameBounds.height - insets.top - insets.bottom;
        if (doubleBuffer == null) {
            doubleBuffer = createImage(bounds.width, bounds.height);
        }
        Graphics2D dg = (Graphics2D) doubleBuffer.getGraphics();
        dg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle clipArea;
        if (display.getDirtyArea() != null) {
            clipArea = display.getDirtyArea();
        } else {
            clipArea = g.getClipBounds();
            if (clipArea == null) {
                clipArea = display.getBounds();
            }
        }
        Rectangle childClipArea = new Rectangle(clipArea);
        if (display.getDirtyArea() == null) {
            childClipArea.x -= insets.left;
            childClipArea.y -= insets.top;
        }
        dg.setClip(childClipArea);
        display.paint(dg, childClipArea);
        display.setDirtyArea(null);
        dg.dispose();
        g.drawImage(doubleBuffer, insets.left, insets.top, null);
    }

    /**
    * Sets the client variables on the client of the wrappedViewer.
    */
    public void setWrappedViewerClientVariables() {
        for (Iterator iterator = clientVariables.keySet().iterator(); iterator.hasNext(); ) {
            String varName = (String) iterator.next();
            wrappedViewer.getClient().setVariable(varName, clientVariables.get(varName));
        }
    }

    /**
    * Notifies all the exit handlers that the viewer is about to exit.
    */
    public void beforeExit() {
        display.removeAllLayers();
        Iterator it = exitHandlers.iterator();
        while (it.hasNext()) {
            XExitHandler handler = (XExitHandler) it.next();
            handler.processExitEvent();
        }
        exitHandlers.clear();
    }

    /**
    * Processes an AWT event.
    *
    * @param awtEvent a <code>AWTEvent</code> instance.
    */
    public void processWrappedViewerAWTEvent(AWTEvent awtEvent) {
        Container wrapperContainer = (Container) wrappedViewer;
        Insets insets = wrapperContainer.getInsets();
        int eventId = awtEvent.getID();
        HashMap event = null;
        switch(eventId) {
            case MouseEvent.MOUSE_PRESSED:
                {
                    MouseEvent mouse_event = (MouseEvent) awtEvent;
                    clickX = new Integer(mouse_event.getX());
                    clickY = new Integer(mouse_event.getY());
                    event = new HashMap();
                    event.put(XComponent.TYPE, new Integer(XComponent.POINTER_EVENT));
                    event.put(XComponent.ACTION, new Integer(XComponent.POINTER_DOWN));
                    event.put(XComponent.X, new Integer(mouse_event.getX() - insets.left));
                    event.put(XComponent.Y, new Integer(mouse_event.getY() - insets.top));
                    event.put(XComponent.MOUSE_BUTTON_ID, new Integer(mouse_event.getButton()));
                    event.put(XComponent.MODIFIERS, new Integer(processModifiers(mouse_event.getModifiers())));
                    break;
                }
            case MouseEvent.MOUSE_RELEASED:
                {
                    MouseEvent mouse_event = (MouseEvent) awtEvent;
                    event = new HashMap();
                    event.put(XComponent.TYPE, new Integer(XComponent.POINTER_EVENT));
                    event.put(XComponent.ACTION, new Integer(XComponent.POINTER_UP));
                    event.put(XComponent.X, new Integer(mouse_event.getX() - insets.left));
                    event.put(XComponent.Y, new Integer(mouse_event.getY() - insets.top));
                    event.put(XComponent.MOUSE_BUTTON_ID, new Integer(mouse_event.getButton()));
                    event.put(XComponent.MODIFIERS, new Integer(processModifiers(mouse_event.getModifiers())));
                    if (pointerInClickArea(mouse_event)) {
                        display.processEvent(event);
                        event = processMouseClickedEvent(mouse_event, insets);
                    }
                    break;
                }
            case MouseEvent.MOUSE_CLICKED:
                {
                    MouseEvent mouse_event = (MouseEvent) awtEvent;
                    event = processMouseClickedEvent(mouse_event, insets);
                    break;
                }
            case MouseEvent.MOUSE_MOVED:
                {
                    MouseEvent mouse_event = (MouseEvent) awtEvent;
                    event = new HashMap();
                    event.put(XComponent.TYPE, new Integer(XComponent.POINTER_EVENT));
                    event.put(XComponent.ACTION, new Integer(XComponent.POINTER_MOVE));
                    event.put(XComponent.X, new Integer(mouse_event.getX() - insets.left));
                    event.put(XComponent.Y, new Integer(mouse_event.getY() - insets.top));
                    event.put(XComponent.MODIFIERS, new Integer(processModifiers(mouse_event.getModifiers())));
                    break;
                }
            case MouseEvent.MOUSE_DRAGGED:
                {
                    MouseEvent mouse_event = (MouseEvent) awtEvent;
                    if (!(pointerInClickArea(mouse_event))) {
                        clickX = null;
                        clickY = null;
                        event = new HashMap();
                        event.put(XComponent.TYPE, new Integer(XComponent.POINTER_EVENT));
                        event.put(XComponent.ACTION, new Integer(XComponent.POINTER_DRAG));
                        event.put(XComponent.X, new Integer(mouse_event.getX() - insets.left));
                        event.put(XComponent.Y, new Integer(mouse_event.getY() - insets.top));
                        event.put(XComponent.MODIFIERS, new Integer(processModifiers(mouse_event.getModifiers())));
                    }
                    break;
                }
            case MouseEvent.MOUSE_ENTERED:
                {
                    MouseEvent mouse_event = (MouseEvent) awtEvent;
                    event = new HashMap();
                    event.put(XComponent.TYPE, new Integer(XComponent.POINTER_EVENT));
                    event.put(XComponent.ACTION, new Integer(XComponent.POINTER_ENTERED));
                    event.put(XComponent.X, new Integer(mouse_event.getX() - insets.left));
                    event.put(XComponent.Y, new Integer(mouse_event.getY() - insets.top));
                    event.put(XComponent.MODIFIERS, new Integer(processModifiers(mouse_event.getModifiers())));
                    break;
                }
            case MouseEvent.MOUSE_EXITED:
                {
                    MouseEvent mouse_event = (MouseEvent) awtEvent;
                    event = new HashMap();
                    event.put(XComponent.TYPE, new Integer(XComponent.POINTER_EVENT));
                    event.put(XComponent.ACTION, new Integer(XComponent.POINTER_EXITED));
                    event.put(XComponent.X, new Integer(mouse_event.getX() - insets.left));
                    event.put(XComponent.Y, new Integer(mouse_event.getY() - insets.top));
                    event.put(XComponent.MODIFIERS, new Integer(processModifiers(mouse_event.getModifiers())));
                    break;
                }
            case MouseEvent.MOUSE_WHEEL:
                {
                    MouseWheelEvent mouse_event = (MouseWheelEvent) awtEvent;
                    event = new HashMap();
                    event.put(XComponent.TYPE, new Integer(XComponent.MOUSE_WHEEL_EVENT));
                    if (mouse_event.getWheelRotation() > 0) {
                        event.put(XComponent.ACTION, new Integer(XComponent.MOUSE_WHEEL_ROTATION_DOWN));
                    } else {
                        event.put(XComponent.ACTION, new Integer(XComponent.MOUSE_WHEEL_ROTATION_UP));
                    }
                    event.put(XComponent.X, new Integer(mouse_event.getX() - insets.left));
                    event.put(XComponent.Y, new Integer(mouse_event.getY() - insets.top));
                    event.put(XComponent.MODIFIERS, new Integer(processModifiers(mouse_event.getModifiers())));
                    event.put(XComponent.MOUSE_WHEEL_SCROLL_UNITS, new Integer(mouse_event.getScrollAmount()));
                    break;
                }
            case FocusEvent.FOCUS_GAINED:
                {
                    if (!focused) {
                        event = new HashMap();
                        event.put(XComponent.TYPE, new Integer(XComponent.FOCUS_EVENT));
                        event.put(XComponent.ACTION, new Integer(XComponent.FOCUS_GAINED));
                    }
                    focused = true;
                    break;
                }
            case FocusEvent.FOCUS_LOST:
                {
                    if (focused) {
                        event = new HashMap();
                        event.put(XComponent.TYPE, new Integer(XComponent.FOCUS_EVENT));
                        event.put(XComponent.ACTION, new Integer(XComponent.FOCUS_LOST));
                    }
                    focused = false;
                    break;
                }
            case KeyEvent.KEY_PRESSED:
                {
                    KeyEvent key_event = (KeyEvent) awtEvent;
                    event = new HashMap();
                    event.put(XComponent.TYPE, new Integer(XComponent.KEYBOARD_EVENT));
                    event.put(XComponent.ACTION, new Integer(XComponent.KEY_DOWN));
                    event.put(XComponent.KEY_CODE, new Integer(key_event.getKeyCode()));
                    event.put(XComponent.KEY_CHAR, new Character(key_event.getKeyChar()));
                    event.put(XComponent.MODIFIERS, new Integer(processModifiers(key_event.getModifiers())));
                    break;
                }
            case KeyEvent.KEY_RELEASED:
                {
                    KeyEvent key_event = (KeyEvent) awtEvent;
                    event = new HashMap();
                    event.put(XComponent.TYPE, new Integer(XComponent.KEYBOARD_EVENT));
                    event.put(XComponent.ACTION, new Integer(XComponent.KEY_UP));
                    event.put(XComponent.KEY_CODE, new Integer(key_event.getKeyCode()));
                    event.put(XComponent.KEY_CHAR, new Character(key_event.getKeyChar()));
                    event.put(XComponent.MODIFIERS, new Integer(processModifiers(key_event.getModifiers())));
                    break;
                }
            case KeyEvent.KEY_TYPED:
                {
                    KeyEvent key_event = (KeyEvent) awtEvent;
                    event = new HashMap();
                    event.put(XComponent.TYPE, new Integer(XComponent.KEYBOARD_EVENT));
                    event.put(XComponent.ACTION, new Integer(XComponent.KEY_TYPED));
                    event.put(XComponent.KEY_CODE, new Integer(key_event.getKeyCode()));
                    event.put(XComponent.KEY_CHAR, new Character(key_event.getKeyChar()));
                    event.put(XComponent.MODIFIERS, new Integer(processModifiers(key_event.getModifiers())));
                    break;
                }
            case ComponentEvent.COMPONENT_SHOWN:
            case ComponentEvent.COMPONENT_RESIZED:
                {
                    if (doubleBuffer != null) {
                        doubleBuffer.flush();
                        doubleBuffer = null;
                    }
                    XDisplay.closeAllPopUps();
                    Rectangle frame_bounds = wrapperContainer.getBounds();
                    Rectangle bounds = new Rectangle();
                    bounds.width = frame_bounds.width - insets.left - insets.right;
                    bounds.height = frame_bounds.height - insets.top - insets.bottom;
                    display.setBounds(bounds);
                    if (!focused) {
                        wrapperContainer.requestFocus();
                    }
                    wrapperContainer.repaint();
                    break;
                }
        }
        if (event != null) {
            display.processEvent(event);
        }
    }

    private boolean pointerInClickArea(MouseEvent mouse_event) {
        if (clickX == null || clickY == null) {
            return false;
        }
        if (clickX.intValue() == mouse_event.getX() && clickY.intValue() == mouse_event.getY()) {
            return false;
        }
        return Math.abs(clickX.intValue() - mouse_event.getX()) <= CLICK_AREA && Math.abs(clickY.intValue() - mouse_event.getY()) <= CLICK_AREA;
    }

    private HashMap processMouseClickedEvent(MouseEvent mouse_event, Insets insets) {
        HashMap event;
        event = new HashMap();
        event.put(XComponent.TYPE, new Integer(XComponent.POINTER_EVENT));
        if (mouse_event.getClickCount() == 1) {
            event.put(XComponent.ACTION, new Integer(XComponent.POINTER_TAP));
        } else if (mouse_event.getClickCount() > 1) {
            event.put(XComponent.ACTION, new Integer(XComponent.POINTER_DOUBLE_TAP));
        } else if (mouse_event.getClickCount() == 0) {
            event.put(XComponent.ACTION, new Integer(XComponent.POINTER_TAP));
        }
        event.put(XComponent.X, new Integer(mouse_event.getX() - insets.left));
        event.put(XComponent.Y, new Integer(mouse_event.getY() - insets.top));
        event.put(XComponent.MOUSE_BUTTON_ID, new Integer(mouse_event.getButton()));
        event.put(XComponent.MODIFIERS, new Integer(processModifiers(mouse_event.getModifiers())));
        return event;
    }

    private int processModifiers(int mod) {
        if (osName == OS_MAC_OS_X) {
            boolean metaSet = ((mod & InputEvent.META_MASK) == InputEvent.META_MASK);
            boolean metaDownSet = ((mod & InputEvent.META_DOWN_MASK) == InputEvent.META_DOWN_MASK);
            boolean ctrlSet = ((mod & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK);
            boolean ctrlDownSet = ((mod & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK);
            mod = mod & (0x7FFFFFFF - (InputEvent.CTRL_MASK | InputEvent.META_MASK | InputEvent.CTRL_DOWN_MASK | InputEvent.META_DOWN_MASK));
            if (metaSet) {
                mod = mod | InputEvent.CTRL_MASK;
            }
            if (ctrlSet) {
                mod = mod | InputEvent.META_MASK;
            }
            if (metaDownSet) {
                mod = mod | InputEvent.CTRL_DOWN_MASK;
            }
            if (ctrlDownSet) {
                mod = mod | InputEvent.META_DOWN_MASK;
            }
        }
        return mod;
    }

    /**
    * @see onepoint.express.XViewer#getDisplay()
    */
    public XDisplay getDisplay() {
        return this.display;
    }

    /**
    * @see XViewer#repaintDisplay(java.awt.Rectangle)
    */
    public void repaintDisplay(Rectangle bounds) {
        Insets insets = ((Container) wrappedViewer).getInsets();
        ((Container) wrappedViewer).repaint(bounds.x + insets.left, bounds.y + insets.top, bounds.width, bounds.height);
    }

    /**
    * @see XViewer#createImage(int,int)
    */
    public Image createImage(int width, int height) {
        return ((Component) wrappedViewer).createImage(width, height);
    }

    /**
    * @see XViewer#createImage(int,int,int[],int,int)
    */
    public Image createImage(int width, int height, int[] pixels, int offset, int scan_line) {
        return ((Component) wrappedViewer).createImage(new MemoryImageSource(width, height, pixels, offset, scan_line));
    }

    /**
    * @see onepoint.express.XViewer#getClient()
    */
    public XClient getClient() {
        return wrappedViewer.getClient();
    }

    /**
    * @see XViewer#trackImage(java.awt.Image)
    */
    public void trackImage(Image image) {
        imageLoader.trackImage(image);
    }

    /**
    * @see onepoint.express.XViewer#waitForImages()
    */
    public void waitForImages() {
        if (imageLoader != null) {
            imageLoader.waitForImages();
        }
    }

    /**
    * @see XViewer#registerExitHandler(XExitHandler)
    */
    public void registerExitHandler(XExitHandler handler) {
        exitHandlers.add(handler);
    }

    /**
    * @see onepoint.express.XViewer#getFrame()
    */
    public Frame getFrame() {
        return wrappedViewer.getFrame();
    }

    public Component getFocusableView() {
        return wrappedViewer.getFocusableView();
    }

    /**
    * @see XViewer#showURL(String,String)
    */
    public void showURL(String url, String title) {
        wrappedViewer.showURL(url, title);
    }

    /**
    * @see XViewer#showURL(String,String)
    */
    public void showURL(URL url, String title) {
        wrappedViewer.showURL(url, title);
    }

    /**
    * @see XViewer#setCursor(java.awt.Cursor)
    */
    public void setCursor(Cursor cursor) {
        ((Component) wrappedViewer).setCursor(cursor);
    }

    /**
    * @see onepoint.express.XViewer#getCursor()
    */
    public Cursor getCursor() {
        return ((Component) wrappedViewer).getCursor();
    }

    /**
    * @see XViewer#showDocument(String,String)
    */
    public void showDocument(String documentUrl, String documentId) {
        wrappedViewer.showDocument(documentUrl, documentId);
    }

    /**
    * Set a client-side variable.
    *
    * @param name  name of the variable
    * @param value value of the variable
    */
    public void setClientVariable(String name, Object value) {
        if (wrappedViewer.getClient() != null) {
            wrappedViewer.getClient().setVariable(name, value);
        } else {
            clientVariables.put(name, value);
        }
    }

    /**
    * @see java.awt.Component#update(java.awt.Graphics)
    */
    public void update(Graphics g) {
        this.paintWrappedViewer(g);
    }

    /**
    * @see XViewer#clientSessionInvalidated()
    */
    public void clientSessionInvalidated() {
    }

    /**
    * Opens the start form.
    *
    * @param parameters a <code>HashMap</code> of parameters.
    */
    public void showStartForm(Map parameters) {
        wrappedViewer.showStartForm(parameters);
    }
}
