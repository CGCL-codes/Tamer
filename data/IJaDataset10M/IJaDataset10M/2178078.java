package net.cevnx.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;
import net.cevnx.gui.event.ButtonEvent;
import net.cevnx.gui.event.ButtonListener;
import net.cevnx.gui.event.MouseButtonEvent;
import net.cevnx.gui.event.MouseMoveEvent;
import net.cevnx.gui.event.WidgetEvent;
import net.cevnx.gui.event.WindowEvent;
import net.cevnx.gui.event.WindowListener;
import net.cevnx.gui.style.AbstractStyle;
import net.cevnx.gui.style.Style;
import net.cevnx.gui.style.StyleManager;
import com.jme.renderer.Renderer;
import com.jme.system.DisplaySystem;

/**
 * The <code>Window</code> class is an internal dialog that can be moved and dragged around the screen
 * from within the native parent window. Use the static methods in this class to efficiently create
 * windows.
 * 
 * @author Christopher Field <cfield2@gmail.com>
 * @version
 * @since 0.0.1
 */
public class Window extends AbstractWidget {

    /**
	 * The logger.
	 */
    private static Logger logger = Logger.getLogger(Window.class.getName());

    /**
	 * The number of buttons in the title bar.
	 */
    public static final int NUM_BUTTONS = 2;

    /**
	 * The <code>TitleBarButton</code> enumeration are all the buttons allowed in the
	 * title bar. 
	 * 
	 * @author Christopher Field <cfield2@gmail.com>
	 * @version
	 * @since 0.0.2
	 */
    public enum TitleBarButtons {

        MINIMIZE, CLOSE, ALL, NONE
    }

    /**
	 * The <code>Styles</code> enumeration are all the styles to be set for a window.
	 * 
	 * @author Christopher Field <cfield2@gmail.com>
	 * @version
	 * @since 0.0.2
	 */
    public enum Styles {

        WINDOW, TITLE_BAR, BUTTON
    }

    /**
	 * The <code>Position</code> enumeration are all the positions the window can be placed
	 * on the screen.
	 * 
	 * @author Christopher Field <cfield2@gmail.com>
	 * @version
	 * @since 0.0.1
	 *
	 */
    public enum Positions {

        LEFT, CENTER, RIGHT, TOP, MIDDLE, BOTTOM, TOP_LEFT, TOP_CENTER, TOP_RIGHT, MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    }

    /**
	 * The <code>EventID</code> enumeration are all the events that a window can fire.
	 * 
	 * @author Christopher Field <cfield2@gmail.com>
	 * @version
	 * @since 0.0.2
	 */
    private enum EventID {

        MINIMIZED, CLOSED, MOVED
    }

    /**
	 * The content panel.
	 */
    private Container contentPanel = new Panel();

    /**
	 * The title bar.
	 */
    private TitleBar titleBar = null;

    /**
	 * The listeners for window events.
	 */
    private ArrayList<WindowListener> listeners = new ArrayList<WindowListener>();

    /**
	 * <code>true</code> window is moveable by dragging title bar; otherwise, <code>false</code>
	 * window can only be moved with a call to the setLocation and setX, setY methods.
	 */
    private boolean moveable = true;

    /**
	 * Creates a new <code>Window</code> instance. Use the static factory methods in this class
	 * to create windows more effectively.
	 */
    private Window() {
        logger.info("Creating a Window");
    }

    /**
	 * Sets the content panel. The content panel is the space below
	 * the title bar.
	 * 
	 * @param contentPanel The content panel.
	 */
    public void setContentPanel(final Container contentPanel) {
        this.contentPanel = contentPanel;
        this.contentPanel.setParent(this);
    }

    /**
	 * Gets the content panel.
	 * 
	 * @return The content panel.
	 */
    public Container getContentPanel() {
        return contentPanel;
    }

    /**
	 * Sets the title.
	 * 
	 * @param title The title.
	 */
    public void setTitle(final String title) {
        titleBar.setTitle(title);
    }

    /**
	 * Layouts the the title bar and content panel within the window based on position of
	 * window and size of content panel after layout. Should be called before first
	 * render.
	 */
    public void pack() {
        contentPanel.setLocation(0, 0);
        contentPanel.layout();
        final int contentPanelWidth = contentPanel.getWidth();
        final int contentPanelHeight = contentPanel.getHeight();
        int titleBarHeight = 0;
        if (titleBar.isVisible()) {
            titleBar.setWidth(contentPanelWidth);
            titleBar.setY(contentPanelHeight);
            titleBarHeight = titleBar.getHeight();
        }
        setWidth(style.getHPadding() + style.getHPadding() + contentPanelWidth);
        setHeight(style.getVPadding() + style.getVPadding() + contentPanelHeight + titleBarHeight);
    }

    /**
	 * Moves this window from its current position by X and Y amounts.
	 * 
	 * @param deltaX The amount of change to move this window in the X direction.
	 * @param deltaY The amount of change to move this window ni the Y direction.
	 */
    public void move(final int deltaX, final int deltaY) {
        if (isMoveable()) {
            x += deltaX;
            y += deltaY;
            limitPosition();
            fireEvent(EventID.MOVED);
        }
    }

    /**
	 * Gets the title bar.
	 * 
	 * @return The title bar.
	 */
    public TitleBar getTitleBar() {
        return titleBar;
    }

    /**
	 * Adds a window listener.
	 * 
	 * @param listener A window listener.
	 */
    public void addWindowListener(final WindowListener listener) {
        listeners.add(listener);
    }

    /**
	 * Removes a window listener.
	 * 
	 * @param listener A window listener.
	 */
    public void removeWindowListener(final WindowListener listener) {
        listeners.remove(listener);
    }

    /**
	 * Closes this window.
	 */
    public void close() {
        setVisible(false);
        setEnabled(false);
        fireEvent(EventID.CLOSED);
    }

    /**
	 * Sets the moveable state. <code>true</code> this window can be moved by dragging the title bar.
	 * <code>false</code> this window can only be moved with calls to the <code>setX(int)</code>,
	 * <code>setY(int)</code>, or <code>setLocation(int, int)</code> methods.
	 * 
	 * @param moveable <code>true</code> window can be moved by dragging the title bar; otherwise,
	 * 					<code>false</code> title bar dragging is disabled.
	 */
    public void setMoveable(final boolean moveable) {
        this.moveable = moveable;
    }

    /**
	 * Checks if this window can be moved by dragging the title bar.
	 * 
	 * @return <code>true</code> window can be moved by dragging the title bar; otherwise, <code>false</code>.
	 */
    public boolean isMoveable() {
        return moveable;
    }

    /**
	 * Sets the undecorated state. An undecorated window has no title bar. If <code>true</code>, then
	 * the title bar is hidden and disabled and the window is re-packed.
	 * 
	 * @param undecorated <code>true</code> hide and disable title bar; otherwise, <code>false</code> show
	 * 					  title bar.
	 */
    public void setUndecorated(final boolean undecorated) {
        if (undecorated) {
            titleBar.setVisible(false);
            titleBar.setEnabled(false);
        } else {
            titleBar.setVisible(true);
            titleBar.setEnabled(true);
        }
        pack();
    }

    /**
	 * Sets the position of this window to specific point on screen.
	 * 
	 * @param position The position.
	 */
    public void setPosition(final Positions position) {
        final int screenWidth = DisplaySystem.getDisplaySystem().getWidth();
        final int screenHeight = DisplaySystem.getDisplaySystem().getHeight();
        switch(position) {
            case LEFT:
                setX(0);
                break;
            case CENTER:
                setX((screenWidth - width) / 2);
                break;
            case RIGHT:
                setX(screenWidth);
                break;
            case TOP:
                setY(screenHeight);
                break;
            case MIDDLE:
                setY((screenHeight - height) / 2);
                break;
            case BOTTOM:
                setY(0);
                break;
            case TOP_LEFT:
                setX(0);
                setY(screenHeight);
                break;
            case TOP_CENTER:
                setX((screenWidth - width) / 2);
                setY(screenHeight);
                break;
            case TOP_RIGHT:
                setX(screenWidth);
                setY(screenHeight);
                break;
            case MIDDLE_LEFT:
                setX(0);
                setY((screenHeight - height) / 2);
                break;
            case MIDDLE_CENTER:
                setX((screenWidth - width) / 2);
                setY((screenHeight - height) / 2);
                break;
            case MIDDLE_RIGHT:
                setX(screenWidth);
                setY((screenHeight - height) / 2);
                break;
            case BOTTOM_LEFT:
                setX(0);
                setY(0);
                break;
            case BOTTOM_CENTER:
                setX((screenWidth - width) / 2);
                setY(0);
                break;
            case BOTTOM_RIGHT:
                setX(screenWidth);
                setY(0);
                break;
            default:
                throw new IllegalArgumentException("The position must be either LEFT, CENTER, RIGHT, TOP, MIDDLE, or BOTTOM");
        }
    }

    @Override
    public void setX(final int x) {
        super.setX(x);
        limitPosition();
    }

    @Override
    public void setY(final int y) {
        super.setY(y);
        limitPosition();
    }

    @Override
    public void setLocation(final int x, final int y) {
        super.setLocation(x, y);
        limitPosition();
    }

    @Override
    public void render(Renderer r) {
        super.render(r);
        if (isVisible()) {
            titleBar.render(r);
            contentPanel.render(r);
        }
    }

    @Override
    public Widget getWidget(final int x, final int y) {
        Widget widget = null;
        if (isVisible() && isEnabled()) {
            widget = titleBar.getWidget(x, y);
            if (widget == null) {
                widget = contentPanel.getWidget(x, y);
            }
            if (widget == null) {
                widget = super.getWidget(x, y);
            }
        }
        return widget;
    }

    @Override
    public String toString() {
        return "Window: " + super.toString();
    }

    /**
	 * Minimizes this window.
	 */
    private void minimize() {
        setVisible(false);
        setEnabled(false);
        fireEvent(EventID.MINIMIZED);
    }

    /**
	 * Limits the window positions to within the screen dimensions.
	 */
    private void limitPosition() {
        final int screenHeight = DisplaySystem.getDisplaySystem().getHeight();
        final int screenWidth = DisplaySystem.getDisplaySystem().getWidth();
        final float upperLeftCorner = y + height;
        final float lowerRightCorner = x + width;
        if (upperLeftCorner > screenHeight) {
            setY(screenHeight - height);
        } else if (y < 0) {
            setY(0);
        }
        if (lowerRightCorner > screenWidth) {
            setX(screenWidth - width);
        } else if (x < 0) {
            setX(0);
        }
    }

    /**
	 * Fires a window event to all listeners based on the event ID.
	 * 
	 * @param eventID The event ID.
	 * @throws IllegalArgumentException If the event ID is not <code>MINIMIZED</code>, </code>CLOSED</code>, or <code>
	 */
    private void fireEvent(final EventID id) {
        Iterator<WindowListener> i = listeners.iterator();
        WindowEvent event = new WindowEvent(this);
        WindowListener listener = null;
        while (i.hasNext()) {
            listener = i.next();
            switch(id) {
                case MINIMIZED:
                    listener.windowMinimized(event);
                    break;
                case CLOSED:
                    listener.windowClosed(event);
                    break;
                case MOVED:
                    listener.windowMoved(event);
                    break;
                default:
                    throw new IllegalArgumentException("The event ID must be either MINIMIZED, CLOSED, or MOVED");
            }
        }
    }

    /**
	 * Creates a new <code>Window</code> instance with a title. This window will have both the minimize and
	 * close button with no background, no border, and white font color.
	 * 
	 * @param title The title.
	 * @return A new <code>Window</code> instance.
	 */
    public static Window createWindow(final String title) {
        Window window = new Window();
        window.titleBar = window.new TitleBar(TitleBarButtons.ALL);
        window.setTitle(title);
        return window;
    }

    /**
	 * Creates a new <code>Window</code> instance with a title and set of styles for each sub component. This
	 * window will have both the minimize and close buttons.
	 * 
	 * @param title The title.
	 * @param styles The styles where the index is one of the <code>Styles</code> enumeration oridinal.
	 * @return A new <code>Window</code> instance.
	 */
    public static Window createWindow(final String title, final Style[] styles) {
        Window window = createWindow(title, Window.TitleBarButtons.ALL, styles);
        return window;
    }

    /**
	 * Creates a new <code>Window</code> instance with a title, style, button configuration, and button style.
	 * 
	 * @param title The title.
	 * @param button The button configuration.
	 * @param styles The styles where the index is one of the <code>Styles</code> enumeration oridinal.
	 * @return A new <code>Window</code> instance.
	 */
    public static Window createWindow(final String title, final TitleBarButtons button, final Style[] styles) {
        Window window = new Window();
        window.titleBar = window.new TitleBar(button);
        if (styles[Styles.WINDOW.ordinal()] != null) {
            window.setStyle(styles[Styles.WINDOW.ordinal()]);
        }
        if (styles[Styles.TITLE_BAR.ordinal()] != null) {
            window.titleBar.setStyle(styles[Styles.TITLE_BAR.ordinal()]);
        }
        if (styles[Styles.BUTTON.ordinal()] != null) {
            window.titleBar.setStyle(styles[Styles.BUTTON.ordinal()], TitleBarButtons.ALL);
        }
        window.setTitle(title);
        return window;
    }

    /**
	 * Creates an array with each index is one of the <code>Styles</code> enumeration oridinals. This is a helper
	 * method for creating the <code>Style[]</code> array to use with the factory methods in this class. For
	 * example, use this method to create<code>Style[]</code> before calling the 
	 * <code>createWindow(String, Style[])</code> factory method.
	 * 
	 * @param windowStyleID The window style.
	 * @param titleBarStyleID The title bar style.
	 * @param buttonStyleID The button style.
	 * @return An array of style with each index as the original from the <code>Styles</code> enumeration.
	 */
    public static Style[] createWindowStyle(final Style windowStyle, final Style titleBarStyle, final Style buttonStyle) {
        logger.info("Creating window style array");
        Style[] styles = new Style[Styles.values().length];
        styles[Styles.WINDOW.ordinal()] = windowStyle;
        styles[Styles.TITLE_BAR.ordinal()] = titleBarStyle;
        styles[Styles.BUTTON.ordinal()] = buttonStyle;
        return styles;
    }

    /**
	 * Creates an array with each index is one of the <code>Styles</code> enumeration oridinals. This is a helper
	 * method for creating the <code>Style[]</code> array to use with the factory methods in this class. For
	 * example, use this method to create<code>Style[]</code> before calling the 
	 * <code>createWindow(String, Style[])</code> factory method.
	 * 
	 * @param windowStyleID The window style ID.
	 * @param titleBarStyleID The title bar style ID.
	 * @param buttonStyleID The button style ID.
	 * @return An array of style with each index as the original from the <code>Styles</code> enumeration.
	 */
    public static Style[] createWindowStyle(final String windowStyleID, final String titleBarStyleID, final String buttonStyleID) {
        StyleManager styles = StyleManager.getInstance();
        return createWindowStyle(styles.getStyle(windowStyleID), styles.getStyle(titleBarStyleID), styles.getStyle(buttonStyleID));
    }

    /**
	 * The <code>TitleBar</code> class is the bar above the content panel in a window.
	 * 
	 * @author Christopher Field <cfield2@gmail.com>
	 * @version
	 * @since 0.0.2
	 */
    public class TitleBar extends AbstractWidget implements ButtonListener {

        /**
		 * The title.
		 */
        private String title = new String();

        /**
		 * The buttons.
		 */
        private Button[] buttons;

        /**
		 * The spacing between buttons.
		 */
        private int buttonSpacing = 5;

        /**
		 * <code>true</code> if the title bar, and window, are being mouse dragged; otherwise,
		 * <code>false</code>.
		 */
        private boolean drag = false;

        /**
		 * The title text X position in pixels.
		 */
        private int textX = 0;

        /**
		 * The title text Y position in pixels.
		 */
        private int textY = 0;

        /**
		 * Creates a new <code>TitleBar</code> instance.
		 */
        public TitleBar(final TitleBarButtons button) {
            super();
            setParent(Window.this);
            initButtons(button);
        }

        /**
		 * Sets the title.
		 * 
		 * @param title The title.
		 */
        public void setTitle(final String title) {
            this.title = title;
            setTitlePosition();
        }

        /**
		 * Sets the button spacing. This is the space between buttons in pixels.
		 * 
		 * @param spacing The button spacing in pixels.
		 */
        public void setButtonSpacing(final int spacing) {
            this.buttonSpacing = spacing;
        }

        /**
		 * Sets the style for a title bar button.
		 * 
		 * @param style The style.
		 * @param button The title bar button.
		 */
        public void setStyle(final Style style, final TitleBarButtons button) {
            switch(button) {
                case MINIMIZE:
                case CLOSE:
                    if (buttons[button.ordinal()] != null) {
                        buttons[button.ordinal()].setStyle(style);
                    }
                    break;
                case ALL:
                    for (int i = 0; i < NUM_BUTTONS; i++) {
                        if (buttons[i] != null) {
                            buttons[i].setStyle(AbstractStyle.getInstance(style));
                        }
                    }
                    break;
                case NONE:
                    break;
                default:
                    throw new IllegalArgumentException("The button must be MINIMIZE, CLOSE, ALL, or NONE");
            }
        }

        /**
		 * Gets a title bar button. Will return <code>null</code> if that button is not used.
		 * 
		 * @param button The title bar button.
		 * @return A title bar button.
		 */
        public Button getButton(final TitleBarButtons button) {
            Button b = null;
            switch(button) {
                case MINIMIZE:
                case CLOSE:
                    b = buttons[button.ordinal()];
                    break;
                case ALL:
                case NONE:
                default:
                    throw new IllegalArgumentException("Cannot get all or none buttons");
            }
            return b;
        }

        /**
		 * Gets the button spacing. This is the space between the buttons.
		 * 
		 * @return The button spacing in pixels.
		 */
        public int getButtonSpacing() {
            return buttonSpacing;
        }

        @Override
        public void setWidth(final int width) {
            super.setWidth(width);
            setSize();
            setButtonPositions();
        }

        @Override
        public void setHeight(final int height) {
            super.setHeight(height);
            setSize();
        }

        @Override
        public void render(final Renderer r) {
            super.render(r);
            if (isVisible()) {
                if (state.hasFont()) {
                    state.renderText(r, getGlobalX() + textX, getGlobalY() + textY, title);
                }
                for (int i = 0; i < NUM_BUTTONS; i++) {
                    if (buttons[i] != null) {
                        buttons[i].render(r);
                    }
                }
            }
        }

        @Override
        public boolean dispatchEvent(final WidgetEvent event) {
            boolean dispatched = super.dispatchEvent(event);
            if (event instanceof MouseButtonEvent) {
                handleMouseButtonEvent((MouseButtonEvent) event);
                dispatched = true;
            } else if (event instanceof MouseMoveEvent) {
                if (drag) {
                    handleDragEvent((MouseMoveEvent) event);
                } else {
                    handleMouseMoveEvent((MouseMoveEvent) event);
                }
                dispatched = true;
            }
            return dispatched;
        }

        @Override
        public Widget getWidget(final int x, final int y) {
            Widget widget = super.getWidget(x, y);
            if (widget != null) {
                for (int i = 0; i < NUM_BUTTONS; i++) {
                    if (buttons[i] != null) {
                        widget = buttons[i].getWidget(x, y);
                    }
                    if (widget != null) {
                        break;
                    }
                }
                if (widget == null) {
                    widget = this;
                }
            }
            return widget;
        }

        @Override
        public void buttonPressed(final ButtonEvent event) {
            if (event.getSource().equals(buttons[TitleBarButtons.MINIMIZE.ordinal()])) {
                minimize();
            } else if (event.getSource().equals(buttons[TitleBarButtons.CLOSE.ordinal()])) {
                close();
            }
        }

        @Override
        public void buttonReleased(final ButtonEvent event) {
        }

        /**
		 * Initializes the buttons for this title bar. If buttons were already created,
		 * this will delete the old ones and reinitialize new buttons.
		 * 
		 * @param button The button configuration.
		 */
        private void initButtons(final TitleBarButtons button) {
            buttons = new Button[NUM_BUTTONS];
            switch(button) {
                case MINIMIZE:
                    createMinimizeButton();
                    break;
                case CLOSE:
                    createCloseButton();
                    break;
                case ALL:
                    createMinimizeButton();
                    createCloseButton();
                    break;
                case NONE:
                    break;
                default:
                    throw new IllegalArgumentException("Title bar button must be either MINIMIZE, CLOSE, ALL, or NONE");
            }
        }

        /**
		 * Creates the minimize button.
		 */
        private void createMinimizeButton() {
            buttons[TitleBarButtons.MINIMIZE.ordinal()] = new Button("_");
            buttons[TitleBarButtons.MINIMIZE.ordinal()].setParent(this);
            buttons[TitleBarButtons.MINIMIZE.ordinal()].addButtonListener(this);
        }

        /**
		 * Creates the close button.
		 */
        private void createCloseButton() {
            buttons[TitleBarButtons.CLOSE.ordinal()] = new Button("X");
            buttons[TitleBarButtons.CLOSE.ordinal()].setParent(this);
            buttons[TitleBarButtons.CLOSE.ordinal()].addButtonListener(this);
        }

        /**
		 * Handles a mouse drag event. This is usually to move the window.
		 * 
		 * @param event The moust move event.
		 */
        private void handleDragEvent(final MouseMoveEvent event) {
            if (isInBounds(event.getX(), event.getY())) {
                move(event.getDeltaX(), event.getDeltaY());
            } else {
                drag = false;
            }
        }

        /**
		 * Handles the mouse button event.
		 * 
		 * @param event The mouse button event.
		 */
        private void handleMouseButtonEvent(final MouseButtonEvent event) {
            if (event.isPressed() && event.isLeftButton()) {
                drag = true;
            } else if (!event.isPressed() && event.isLeftButton()) {
                drag = false;
            }
        }

        /**
		 * Sets the title position in the X and Y directions relative to this widgets width
		 * and height, respectively.
		 */
        private void setTitlePosition() {
            textX = (width - (getContentWidth() - state.getFont().getTextWidth(title))) / 2;
            textY = height / 2;
        }

        /**
		 * Sets the size of this title bar based on the maximum height of all sub components. The
		 * width is set by the window window.
		 */
        private void setSize() {
            int maxHeight = Math.max(state.getFont().getCharHeight(), this.height);
            int buttonWidths = 0;
            for (int i = 0; i < NUM_BUTTONS; i++) {
                if (buttons[i] != null) {
                    maxHeight = Math.max(maxHeight, buttons[i].getHeight());
                    buttonWidths += buttons[i].getWidth();
                }
            }
            buttonWidths = buttonWidths + (NUM_BUTTONS - 1) * buttonSpacing;
            super.setHeight(maxHeight + style.getVPadding() + style.getVPadding());
            setTitlePosition();
        }

        /**
		 * Sets the positions of the buttons.
		 */
        private void setButtonPositions() {
            int buttonX = getContentWidth();
            for (int i = NUM_BUTTONS - 1; i >= 0; i--) {
                if (buttons[i] != null) {
                    if (buttons[i].isVisible()) {
                        buttonX = buttonX - buttons[i].getWidth();
                        buttons[i].setX(buttonX);
                        buttonX = buttonX - buttonSpacing;
                    }
                }
            }
        }
    }
}
