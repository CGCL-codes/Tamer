package de.esoco.ewt.impl.j2me;

import de.esoco.ewt.UserInterfaceContext;
import de.esoco.ewt.component.Button;
import de.esoco.ewt.component.Component;
import de.esoco.ewt.component.Container;
import de.esoco.ewt.component.Label;
import de.esoco.ewt.component.Panel;
import de.esoco.ewt.component.View;
import de.esoco.ewt.component.menu.MenuBar;
import de.esoco.ewt.event.EWTEvent;
import de.esoco.ewt.event.EWTEventHandler;
import de.esoco.ewt.event.EventType;
import de.esoco.ewt.event.KeyCode;
import de.esoco.ewt.layout.EdgeLayout;
import de.esoco.ewt.layout.GenericLayout;
import de.esoco.ewt.layout.GridLayout;
import de.esoco.ewt.skin.Skin;
import de.esoco.ewt.style.AlignedPosition;
import de.esoco.ewt.style.StyleData;
import de.esoco.ewt.style.ViewStyle;
import java.util.Vector;

/********************************************************************
 * A composite container subclass that is used by View implementations to manage
 * their components. While CanvasView uses it as a delegate child views subclass
 * it directly.
 *
 * @author eso
 */
public class ViewContainer extends CompositeContainer implements Container {

    /** for conditional compilation of child view dragging */
    private static final boolean CHILD_VIEW_DRAGGING = true;

    private static GenericLayout aViewLayout;

    final CanvasView rCanvasView;

    final View rView;

    private MenuBar rMenuBar;

    private MenuPanel aMenuPanel;

    private String sTitle;

    private Label aTitleLabel;

    Button aCloseButton;

    Button aMaxButton;

    private AbstractComponent rFocusedComponent;

    private AbstractComponent rDragComponent;

    private boolean bFocusTransferEnabled = true;

    int nDragX;

    int nDragY;

    long nDragTime;

    /***************************************
	 * Creates a new instance for a certain parent view. The parent view can
	 * either be the {@link CanvasView} that this view container manages the
	 * components of or a child view that is the parent of the child view
	 * instance that is subclassed from this view container.
	 *
	 * @param rParent    The parent view of this view container
	 * @param rViewStyle The view style
	 */
    protected ViewContainer(View rParent, ViewStyle rViewStyle) {
        if (rParent instanceof ChildViewImpl) {
            ChildViewImpl rChildViewImpl = (ChildViewImpl) rParent;
            rCanvasView = (rChildViewImpl).getCanvasView();
        } else {
            rCanvasView = (CanvasView) rParent;
        }
        if (this instanceof View) {
            rView = (View) this;
        } else {
            rView = rParent;
        }
        initViewDecorations(rViewStyle);
    }

    /***************************************
	 * Returns the CanvasView that this view container is rendered on.
	 *
	 * @return   The canvas view of this view container
	 *
	 * @category mEWT
	 */
    public final CanvasView getCanvasView() {
        return rCanvasView;
    }

    /***************************************
	 * @see Component#getContext()
	 */
    public UserInterfaceContext getContext() {
        return rCanvasView.getContext();
    }

    /***************************************
	 * Returns the title to be displayed in this view container.
	 *
	 * @return The container's title (NULL for none)
	 */
    public final String getTitle() {
        return sTitle;
    }

    /***************************************
	 * @see Component#getView()
	 */
    public View getView() {
        return rCanvasView;
    }

    /***************************************
	 * Overridden to return this instance as the view container for all child
	 * components.
	 *
	 * @see      AbstractComponent#getViewContainer()
	 * @category mEWT
	 */
    public final ViewContainer getViewContainer() {
        return this;
    }

    /***************************************
	 * Initializes this container and performs the first layout.
	 *
	 * @see View#pack()
	 */
    public void pack() {
        initFocus();
        validate();
        getCanvasView().repaint();
    }

    /***************************************
	 * @see View#setMenuBar(MenuBar)
	 */
    public void setMenuBar(MenuBar rNewMenuBar) {
        if (isVisible()) {
            if (rMenuBar != null && rCanvasView.getCurrentMenu() == rMenuBar) {
                rCanvasView.applyMenu(rMenuBar, false);
                rCanvasView.applyMenu(rNewMenuBar, true);
            }
            if (aMenuPanel != null) {
                aMenuPanel.setVisible(rNewMenuBar != null && rCanvasView.isMaximized());
            }
        }
        rMenuBar = rNewMenuBar;
    }

    /***************************************
	 * Sets the title that shall be displayed at the top of this container.
	 *
	 * @param sTitle The new title (NULL for none)
	 */
    public void setTitle(String sTitle) {
        this.sTitle = sTitle;
        if (this instanceof View || rCanvasView.isMaximized()) {
            updateTitleLabel(sTitle);
        }
    }

    /***************************************
	 * Overridden to set the preferred size to the current size and to perform
	 * the layout if necessary.
	 *
	 * @see AbstractContainer#validate()
	 */
    public void validate() {
        if (!isValid()) {
            setPreferredSize(getWidth(), getHeight());
            layout();
        }
    }

    /***************************************
	 * Creates a Panel as the content container.
	 *
	 * @see CompositeContainer#addContentContainer(GenericLayout)
	 */
    protected AbstractContainer addContentContainer() {
        Panel aContentContainer = new Panel();
        addCompositeElement(aContentContainer, AlignedPosition.CENTER);
        return aContentContainer;
    }

    /***************************************
	 * Returns the component that currently has the input focus.
	 *
	 * @return The focused component
	 */
    protected Component getFocusedComponent() {
        return rFocusedComponent;
    }

    /***************************************
	 * Returns the menu bar of this view.
	 *
	 * @return The current menu bar (may be NULL)
	 */
    protected final MenuBar getMenuBar() {
        return rMenuBar;
    }

    /***************************************
	 * Handles keyboard input on components. Will be invoked from the class
	 * {@link CanvasView} on key events.
	 *
	 * @see CanvasListener#handleKeyEvent(int, int)
	 */
    protected void handleKeyEvent(int nKeyCode, int nEventType) {
        if (rFocusedComponent != null) {
            switch(nKeyCode) {
                case KeyCode.LEFT:
                case KeyCode.RIGHT:
                case KeyCode.UP:
                case KeyCode.DOWN:
                    if (!rFocusedComponent.handleNavigation(nKeyCode, nEventType)) {
                        if (nEventType == CanvasListener.KEY_RELEASED) {
                            transferFocus(nKeyCode);
                        }
                    }
                    break;
                case KeyCode.ENTER:
                    rFocusedComponent.handleSelection(Integer.MIN_VALUE, Integer.MIN_VALUE, nEventType);
                    break;
                default:
                    if (rFocusedComponent.hasStyle(EDITABLE) && rFocusedComponent.isSelected()) {
                        rFocusedComponent.handleInput(nKeyCode, nEventType);
                    }
            }
            if (nEventType == CanvasListener.KEY_PRESSED) {
                rFocusedComponent.notifyEventListeners(EventType.KEY_PRESSED, null, Integer.MIN_VALUE, Integer.MIN_VALUE, nKeyCode);
            }
        }
    }

    /***************************************
	 * Handles pointer events that are received from the parent CanvasView.
	 * Checks for a focusable component at the given pointer location. If one
	 * can be found, it will receive the input focus and it's {@link
	 * AbstractComponent#handleSelection(int, int, int)} method will be invoked
	 * with the coordinates of the pointer.
	 *
	 * <p>If the component under the pointer when releasing it is the same as
	 * when the pointer has bee pressed the {@link
	 * AbstractComponent#handleSelection(int, int, int)} method will be invoked
	 * again. Else the component's {@link AbstractComponent#cancelSelect()}
	 * method will be invoked.</p>
	 *
	 * @see CanvasListener#handlePointerEvent(int, int, int)
	 */
    protected void handlePointerEvent(int x, int y, int nEventType) {
        AbstractComponent rComponent = null;
        EventType rEventType = null;
        switch(nEventType) {
            case CanvasListener.POINTER_DRAGGED:
                rEventType = EventType.POINTER_DRAGGED;
                if (rDragComponent != null) {
                    rComponent = rDragComponent;
                } else if (rFocusedComponent != null) {
                    rFocusedComponent.performDrag(x, y);
                    rComponent = rFocusedComponent;
                }
                break;
            case CanvasListener.POINTER_PRESSED:
                rEventType = EventType.POINTER_PRESSED;
                rComponent = getComponent(x, y);
                if (rComponent != null) {
                    if (rComponent.canReceiveFocus()) {
                        requestFocus(rComponent);
                        rFocusedComponent.handleSelection(x, y, CanvasListener.KEY_PRESSED);
                    } else {
                        rDragComponent = rComponent;
                    }
                }
                break;
            case CanvasListener.POINTER_RELEASED:
                rEventType = EventType.POINTER_RELEASED;
                if (rDragComponent != null) {
                    rComponent = rDragComponent;
                    rDragComponent = null;
                } else if (rFocusedComponent != null) {
                    rComponent = getComponent(x, y);
                    if (rComponent != null && rComponent == rFocusedComponent) {
                        rFocusedComponent.handleSelection(x, y, CanvasListener.KEY_RELEASED);
                    } else {
                        rFocusedComponent.cancelSelect();
                    }
                    rComponent = rFocusedComponent;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown pointer event: " + nEventType);
        }
        if (rComponent != null) {
            rComponent.notifyEventListeners(rEventType, null, x, y, 0);
        }
    }

    /***************************************
	 * Initializes this container's focus handling by setting the focus to the
	 * first focusable component in the given container.
	 */
    protected void initFocus() {
        if (rFocusedComponent == null) {
            Vector aComponents = new Vector();
            getFocusableComponents(this, aComponents);
            if (aComponents.size() > 0) {
                requestFocus((Component) aComponents.elementAt(0));
            }
        }
    }

    /***************************************
	 * Returns the state of the event-driven focus transfer. If FALSE the
	 * event-driven transfer of the input focus will not be performed. This can
	 * be used by subclasses that handle focus transfers differently.
	 *
	 * @return TRUE if the event-driven focus transfer is enabled
	 */
    protected final boolean isFocusTransferEnabled() {
        return bFocusTransferEnabled;
    }

    /***************************************
	 * Will be invoked by children if their {@link Component#requestFocus()}
	 * method is invoked. Sets the focus to the given component if it can
	 * currently receive the input focus. Focus transfers through this method
	 * are available even if event-driven focus transfers are disabled.
	 *
	 * @param rComponent The component that shall receive the focus
	 */
    protected void requestFocus(Component rComponent) {
        AbstractComponent c = (AbstractComponent) rComponent;
        if (c != rFocusedComponent && c.canReceiveFocus()) {
            if (rFocusedComponent != null) {
                rFocusedComponent.changeFocus(false);
            }
            rFocusedComponent = c;
            rFocusedComponent.changeFocus(true);
        }
    }

    /***************************************
	 * Enables or disables the event-driven focus transfer. If FALSE the
	 * event-driven transfer of the input focus will not be performed. This can
	 * used by subclasses that handle focus transfers differently.
	 *
	 * @param bFocusTransferEnabled TRUE to enable the event-driven focus
	 *                              transfer
	 */
    protected final void setFocusTransferEnabled(boolean bFocusTransferEnabled) {
        this.bFocusTransferEnabled = bFocusTransferEnabled;
    }

    /***************************************
	 * Internal method to recursively collect the focusable components of a
	 * container hierarchy into the argument Vector.
	 *
	 * @param rContainer  The container to collect the focusable children of
	 * @param rIntoVector The vector to add the focusable components to
	 */
    static void getFocusableComponents(AbstractContainer rContainer, Vector rIntoVector) {
        int nCount = rContainer.getComponentCount();
        for (int i = 0; i < nCount; i++) {
            AbstractComponent rComponent = rContainer.getComponent(i);
            if (rComponent.canReceiveFocus()) {
                rIntoVector.addElement(rComponent);
            }
            if (rComponent instanceof AbstractContainer) {
                getFocusableComponents((AbstractContainer) rComponent, rIntoVector);
            }
        }
    }

    /***************************************
	 * Package-internal method to access the menu panel of this view container.
	 *
	 * @return The menu panel or NULL for none
	 */
    final MenuPanel getMenuPanel() {
        return aMenuPanel;
    }

    /***************************************
	 * Returns the skin of this view container's parent view.
	 *
	 * @see AbstractComponent#getParentSkin()
	 */
    Skin getParentSkin() {
        return rCanvasView.getSkin();
    }

    /***************************************
	 * Package-internal method to return the view layout and initialize it if
	 * necessary. This default implementation returns an edge layout with the
	 * gaps defined in the view skin. May be overridden by subclasses to return
	 * a different layout.
	 *
	 * @param  rViewStyle The view style to return the layout for
	 *
	 * @return The view layout instance
	 */
    GenericLayout getViewLayout(ViewStyle rViewStyle) {
        if (aViewLayout == null) {
            Skin rSkin = getSkin();
            int nHGap = rSkin.getLayoutGap(true);
            int nVGap = rSkin.getLayoutGap(false);
            if (nHGap > 0 || nVGap > 0) {
                aViewLayout = new EdgeLayout(nHGap, nVGap);
            } else {
                aViewLayout = EdgeLayout.NO_GAP_LAYOUT;
            }
        }
        return aViewLayout;
    }

    /***************************************
	 * If the parent view is currently maximized and a menu is displayed this
	 * method checks whether the given key code represents a soft key that is
	 * associated with one of the full-screen menu buttons and performs the
	 * corresponding menu function if necessary.
	 *
	 * @param  nKeyCode The key code to check
	 *
	 * @return TRUE if the key code is associated with a menu button and has
	 *         been processed
	 */
    boolean handleMenuKey(int nKeyCode) {
        if (rCanvasView.isMaximized() && aMenuPanel != null) {
            return aMenuPanel.processMenuKey(nKeyCode);
        } else {
            return false;
        }
    }

    /***************************************
	 * Internal method to initialize the view decoration elements (i.e. the view
	 * buttons). Also sets the view layout to the layout instance returned by
	 * {@link #getViewLayout(ViewStyle)}.
	 *
	 * @param rViewStyle The view style that defines the view appearance
	 */
    void initViewDecorations(ViewStyle rViewStyle) {
        setCompositeLayout(getViewLayout(rViewStyle));
        if (!rViewStyle.hasFlag(ViewStyle.UNDECORATED_STYLE)) {
            EWTEventHandler aButtonHandler = new ViewButtonHandler();
            Panel aViewButtonPanel = new Panel(new GridLayout(2, true, 2, 2));
            aViewButtonPanel.changeStyle(VIEW_DECORATION_STYLE, true);
            addCompositeElement(aViewButtonPanel, AlignedPosition.TOP_RIGHT);
            if (!rViewStyle.hasFlag(ViewStyle.FIXED_SIZE_STYLE)) {
                aMaxButton = (Button) aViewButtonPanel.addComponent(Button.class, StyleData.DEFAULT);
                aMaxButton.setText("M");
                aMaxButton.changeStyle(VIEW_DECORATION_STYLE, true);
                aMaxButton.addEventListener(aButtonHandler, EventType.ACTION);
            }
            aCloseButton = (Button) aViewButtonPanel.addComponent(Button.class, StyleData.DEFAULT);
            aCloseButton.setText("X");
            aCloseButton.changeStyle(VIEW_DECORATION_STYLE, true);
            aCloseButton.addEventListener(aButtonHandler, EventType.ACTION);
        }
    }

    /***************************************
	 * Repaints a certain child component of this view container. Will be
	 * invoked from the implementation of the {@link Component#repaint()}
	 * method.
	 *
	 * @param rComponent The component to repaint
	 */
    void repaint(AbstractComponent rComponent) {
        if (rComponent.isValid()) {
            rCanvasView.repaint(rComponent.getScreenX(), rComponent.getScreenY(), rComponent.getWidth(), rComponent.getHeight());
        } else {
            rCanvasView.repaint();
        }
    }

    /***************************************
	 * Initializes the full screen display mode. Depending on the internal state
	 * this will either enable or disable the title label and menu panel
	 * components.
	 *
	 * @param bFullScreen TRUE for enable full screen mode, FALSE to disable
	 */
    void setFullScreenMode(boolean bFullScreen) {
        if (bFullScreen) {
            updateTitleLabel(sTitle);
            if (aMenuPanel == null) {
                aMenuPanel = new MenuPanel(rCanvasView);
                addCompositeElement(aMenuPanel, AlignedPosition.BOTTOM);
            }
        } else {
            updateTitleLabel(null);
        }
        if (aMenuPanel != null) {
            aMenuPanel.setVisible(bFullScreen);
        }
    }

    /***************************************
	 * Determines a candidate for focus transfer by comparing the coordinates of
	 * two components. This method can be used for both horizontal and vertical
	 * navigation. Therefore the coordinate values need to be specified
	 * explicitly in terms of primary and secondary coordinates. Primary
	 * coordinates are that of the navigation direction and the secondary
	 * coordinates are perpendicular to them. The sign of the last two
	 * difference arguments is arbitrary, the values will be normalized with
	 * {@link Math#abs(int)}.
	 *
	 * @param  rCandidate The current candidate
	 * @param  rNew       A possible new candidate to be checked
	 * @param  an         The primary coordinate of the new candidate
	 * @param  bn         The secondary coordinate of the new candidate
	 * @param  af         The primary coordinate of the currently focused
	 *                    component
	 * @param  bf         The secondary coordinate of the currently focused
	 *                    component
	 * @param  acdiff     The difference between the primary coordinates of the
	 *                    currently focused component and the current candidate
	 * @param  bcdiff     The difference between the primary coordinates of the
	 *                    currently focused component and the current candidate
	 *
	 * @return The resulting candidate (either rCurrent or rNew)
	 */
    AbstractComponent testFocusCandidate(AbstractComponent rCandidate, AbstractComponent rNew, int an, int bn, int af, int bf, int acdiff, int bcdiff) {
        if (an < af) {
            if (rCandidate == null) {
                rCandidate = rNew;
            } else {
                int ad1 = af - an;
                int bd1 = Math.abs(bf - bn);
                int ad2 = Math.abs(acdiff);
                int bd2 = Math.abs(bcdiff);
                if (bd1 < bd2 || (bd1 == bd2 && ad1 < ad2)) {
                    rCandidate = rNew;
                }
            }
        }
        return rCandidate;
    }

    /***************************************
	 * Internal method for the event-driven focus transfer. It transfers the
	 * focus to another component based on the component coordinates.
	 *
	 * @param nDirection The direction of the focus transfer as one of the
	 *                   constants from the {@link AbstractComponent} class
	 */
    void transferFocus(int nDirection) {
        if (bFocusTransferEnabled) {
            Vector aComponents = new Vector();
            AbstractComponent rCandidate = null;
            int xf1 = rFocusedComponent.getScreenX();
            int yf1 = rFocusedComponent.getScreenY();
            int xf2 = xf1 + rFocusedComponent.getWidth() - 1;
            int yf2 = yf1 + rFocusedComponent.getHeight() - 1;
            getFocusableComponents(this, aComponents);
            for (int i = 0; i < aComponents.size(); i++) {
                AbstractComponent rNew = (AbstractComponent) aComponents.elementAt(i);
                int xn1 = rNew.getScreenX();
                int yn1 = rNew.getScreenY();
                int xn2 = xn1 + rNew.getWidth() - 1;
                int yn2 = yn1 + rNew.getHeight() - 1;
                int xc1 = 0;
                int yc1 = 0;
                int xc2 = 0;
                int yc2 = 0;
                if (rNew != rFocusedComponent) {
                    if (rCandidate != null) {
                        xc1 = rCandidate.getScreenX();
                        yc1 = rCandidate.getScreenY();
                        xc2 = xc1 + rCandidate.getWidth() - 1;
                        yc2 = yc1 + rCandidate.getHeight() - 1;
                    }
                    switch(nDirection) {
                        case KeyCode.LEFT:
                            rCandidate = testFocusCandidate(rCandidate, rNew, xn1, yn1, xf1, yf1, xf1 - xc1, yf1 - yc1);
                            break;
                        case KeyCode.RIGHT:
                            rCandidate = testFocusCandidate(rCandidate, rNew, xf2, yf1, xn2, yn1, xf2 - xc2, yf1 - yc1);
                            break;
                        case KeyCode.UP:
                            rCandidate = testFocusCandidate(rCandidate, rNew, yn1, xn1, yf1, xf1, yf1 - yc1, xf1 - xc1);
                            break;
                        case KeyCode.DOWN:
                            rCandidate = testFocusCandidate(rCandidate, rNew, yf2, xf1, yn2, xn1, yf2 - yc2, xf1 - xc1);
                            break;
                    }
                }
            }
            if (rCandidate != null) {
                requestFocus(rCandidate);
            }
        }
    }

    /***************************************
	 * Internal method to update the title label according to the given title
	 * string. If the string is not NULL it will be set as the title and the
	 * label will be displayed (and created if necessary). If the string is NULL
	 * the label will be hidden.
	 *
	 * @param sTitle The new title string
	 */
    void updateTitleLabel(String sTitle) {
        if (sTitle != null) {
            if (aTitleLabel == null) {
                aTitleLabel = new Label();
                aTitleLabel.changeStyle(VIEW_DECORATION_STYLE | Label.VIEW_TITLE_STYLE, true);
                addCompositeElement(aTitleLabel, AlignedPosition.TOP);
                if (CHILD_VIEW_DRAGGING && this instanceof View) {
                    EWTEventHandler aDragHandler = new ViewDragHandler();
                    aTitleLabel.addEventListener(aDragHandler, EventType.POINTER_PRESSED);
                    aTitleLabel.addEventListener(aDragHandler, EventType.POINTER_DRAGGED);
                }
            }
            aTitleLabel.setText(sTitle);
            aTitleLabel.setVisible(true);
        } else if (aTitleLabel != null) {
            aTitleLabel.setVisible(false);
        }
    }

    /********************************************************************
	 * An event handler implementation that processes events from view buttons.
	 *
	 * @author eso
	 */
    class ViewButtonHandler implements EWTEventHandler {

        /***************************************
		 * Handles the events from the view close and maximize buttons.
		 *
		 * @param rEvent The event that occurred
		 */
        public void handleEvent(EWTEvent rEvent) {
            Object rSource = rEvent.getSource();
            if (rSource == aMaxButton) {
                rView.setMaximized(!rView.isMaximized());
            } else if (rSource == aCloseButton) {
                notifyEventListeners(EventType.VIEW_CLOSING);
                if (getEventListener(EventType.VIEW_CLOSING) == null) {
                    rView.setVisible(false);
                }
            }
        }
    }

    /********************************************************************
	 * An event handler implementation that handles view dragging.
	 *
	 * @author eso
	 */
    class ViewDragHandler implements EWTEventHandler {

        /***************************************
		 * Handles the dragging of the associated views with the pointer after
		 * the pointer has been pressed on the view title.
		 *
		 * @param rEvent The event that occurred
		 */
        public void handleEvent(EWTEvent rEvent) {
            long nTime = System.currentTimeMillis();
            int dx = rEvent.getPointerX();
            int dy = rEvent.getPointerY();
            if (rEvent.getType() == EventType.POINTER_PRESSED) {
                nDragX = dx;
                nDragY = dy;
                nDragTime = nTime;
            } else {
                dx -= nDragX;
                dy -= nDragY;
                int x = getX();
                int y = getY();
                int w = getWidth();
                int h = getHeight();
                int cx = rCanvasView.getX();
                int cy = rCanvasView.getY();
                int cw = rCanvasView.getWidth();
                int ch = rCanvasView.getHeight();
                int nx = checkCoordinate(x + dx, w, cx, cw);
                int ny = checkCoordinate(y + dy, h, cy, ch);
                if (nx != x || ny != y) {
                    setLocation(nx, ny);
                    getCanvasView().paintAsynchronously();
                    nDragX += nx - x;
                    nDragY += ny - y;
                    nDragTime = nTime;
                }
            }
        }

        /***************************************
		 * Internal method to update a drag coordinate if it is still within the
		 * valid bounds.
		 *
		 * @param  nValue The coordinate value to update
		 * @param  nSize  The size to add to the coordinate
		 * @param  nMin   The minimum extent to compare against
		 * @param  nMax   The maximum extent (relative to nMin) to compare
		 *                against
		 *
		 * @return The checked coordinate, modified if necessary
		 */
        final int checkCoordinate(int nValue, int nSize, int nMin, int nMax) {
            nMax += nMin;
            if (nValue < nMin) {
                nValue = nMin;
            } else if (nValue + nSize > nMax) {
                nValue = nMax - nSize;
            }
            return nValue;
        }
    }
}
