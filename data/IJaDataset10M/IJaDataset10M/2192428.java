package net.sourceforge.jruntimedesigner.common;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;
import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.actions.EditWidgetAction;
import net.sourceforge.jruntimedesigner.actions.LockAction;
import net.sourceforge.jruntimedesigner.actions.RemoveWidgetAction;
import net.sourceforge.jruntimedesigner.actions.UnlockAction;
import net.sourceforge.jruntimedesigner.actions.alignment.AlignBottomAction;
import net.sourceforge.jruntimedesigner.actions.alignment.AlignHeightAction;
import net.sourceforge.jruntimedesigner.actions.alignment.AlignLeftAction;
import net.sourceforge.jruntimedesigner.actions.alignment.AlignRightAction;
import net.sourceforge.jruntimedesigner.actions.alignment.AlignSizeAction;
import net.sourceforge.jruntimedesigner.actions.alignment.AlignTopAction;
import net.sourceforge.jruntimedesigner.actions.alignment.AlignWidthAction;
import net.sourceforge.jruntimedesigner.actions.layer.MoveBackAction;
import net.sourceforge.jruntimedesigner.actions.layer.MoveBackwardsAction;
import net.sourceforge.jruntimedesigner.actions.layer.MoveForwardsAction;
import net.sourceforge.jruntimedesigner.actions.layer.MoveFrontAction;
import net.sourceforge.jruntimedesigner.actions.layout.RedoAction;
import net.sourceforge.jruntimedesigner.actions.layout.UndoAction;
import net.sourceforge.jruntimedesigner.actions.templates.SaveTemplateAction;
import net.sourceforge.jruntimedesigner.events.DetachableMouseInputListener;
import net.sourceforge.jruntimedesigner.events.DetachableMouseListener;
import net.sourceforge.jruntimedesigner.undo.MoveWidgetUndoableEdit;
import net.sourceforge.jruntimedesigner.undo.ResizeWidgetUndoableEdit;
import net.sourceforge.jruntimedesigner.undo.UndoRedoProgress;
import net.sourceforge.jruntimedesigner.undo.UndoableModelSupport;
import net.sourceforge.jruntimedesigner.widgets.IWidget;
import net.sourceforge.jruntimedesigner.widgets.IWidgetProvider;
import net.sourceforge.jruntimedesigner.zoom.WidgetZoomLayout;

/**
 * Hosting component for any widget in a runtime designer. Makes any JComponent
 * resizable and movable on the screen by the mouse and keyboard. <br>
 * Based on the implementation by Santosh Kumar T released under GNU Lesser
 * General Public License.
 * 
 * @author ikunin
 */
@SuppressWarnings("serial")
public class WidgetHolder extends JComponent implements IWidgetHolder {

    public static final int BORDER_WIDTH = 6;

    private final ResizableBorder resizableBorder = new ResizableBorder(BORDER_WIDTH);

    private static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);

    private MouseInputListener resizeListener;

    private JPopupMenu popup;

    private JRuntimeDesignerController controller;

    private JComponent realComponent;

    private IWidget widget;

    private Point startPos;

    private UndoableModelSupport undoableModelSupport;

    private UndoRedoProgress progress;

    private boolean isSelected;

    private boolean isGuide;

    private JMenu addWidgetsSubMenu;

    private final boolean isComposite;

    private Dimension newPreferredSize;

    public WidgetHolder(final IWidget widget, final JRuntimeDesignerController controller) {
        this.undoableModelSupport = new UndoableModelSupport();
        this.progress = new UndoRedoProgress();
        this.controller = controller;
        this.widget = widget;
        this.realComponent = widget.getComponent();
        this.setFocusable(false);
        this.isComposite = widget.getWidgetProvider().isComposite();
        resizeListener = new DetachableMouseInputListener(new ResizeListener(), controller);
        widget.addPropertyChangeListener(IWidget.PROPERTY_RESIZABLE, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                boolean resizable = ((Boolean) evt.getNewValue()).booleanValue();
                resizableBorder.setResizable(resizable);
            }
        });
        widget.addPropertyChangeListener(IWidget.PROPERTY_DESIGNMODE, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                boolean value = ((Boolean) event.getNewValue()).booleanValue();
                setDesignMode(value);
            }
        });
        widget.addPropertyChangeListener(IWidget.PROPERTY_LOCKED, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                boolean value = ((Boolean) event.getNewValue()).booleanValue();
                setLocked(value);
            }
        });
        setLayout(new BorderLayout());
        add(realComponent, BorderLayout.CENTER);
        initPopupMenu();
        resizableBorder.setResizable(widget.isResizable());
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                Point location = getLocation();
                widget.setLocation(location);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = getSize();
                widget.setSize(adjustSize(size, -BORDER_WIDTH * 2));
            }
        });
        realComponent.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = realComponent.getPreferredSize();
                if (newPreferredSize != null && newPreferredSize.equals(size)) {
                    return;
                }
                setSize(adjustSize(size, BORDER_WIDTH * 2));
                didResized();
            }
        });
        addUndoableEditListener(controller.getUndoManager());
    }

    private void initPopupMenu() {
        popup = new JPopupMenu();
        popup.add(new EditWidgetAction(this, controller.getBaseDialogsFactory()));
        popup.add(new RemoveWidgetAction(this));
        if (isComposite) {
            popup.add(new SaveTemplateAction(this));
        }
        popup.addSeparator();
        if (isComposite) {
            buildAddWidgetsSubMenu();
        }
        popup.add(new AlignTopAction(controller));
        popup.add(new AlignBottomAction(controller));
        popup.add(new AlignLeftAction(controller));
        popup.add(new AlignRightAction(controller));
        popup.addSeparator();
        popup.add(new AlignSizeAction(controller));
        popup.add(new AlignWidthAction(controller));
        popup.add(new AlignHeightAction(controller));
        popup.addSeparator();
        popup.add(new MoveFrontAction(this));
        popup.add(new MoveBackAction(this));
        popup.add(new MoveForwardsAction(this));
        popup.add(new MoveBackwardsAction(this));
        popup.addSeparator();
        popup.add(new LockAction(this));
        popup.add(new UnlockAction(this));
        popup.addSeparator();
        popup.add(new UndoAction(controller));
        popup.add(new RedoAction(controller));
        addMouseListener(new DetachableMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {
                showPopup(me);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                showPopup(me);
            }

            private void showPopup(MouseEvent me) {
                if (me.isPopupTrigger() && controller.isDesignMode()) {
                    if (isComposite) {
                        controller.getPanel().updateLocation(me.getPoint());
                        controller.getPanel().changeAddActionDesination(realComponent);
                    }
                    popup.show(WidgetHolder.this, me.getX(), me.getY());
                }
            }
        }, controller));
    }

    private void buildAddWidgetsSubMenu() {
        IWidgetProvider widgetProvider = widget.getWidgetProvider();
        addWidgetsSubMenu = controller.createAddWidgetsSubMenu(widgetProvider);
        popup.add(addWidgetsSubMenu);
        popup.addSeparator();
    }

    /**
   * @see net.sourceforge.jruntimedesigner.common.IWidgetHolder#recalcSize()
   */
    public void recalcSize() {
        Point location = getLocation();
        Dimension bounds = realComponent.getPreferredSize();
        realComponent.setSize(bounds.getSize());
        setBounds(location.x, location.y, bounds.width + BORDER_WIDTH * 2, bounds.height + BORDER_WIDTH * 2);
        widget.setSize(bounds.getSize());
        widget.setLocation(getLocation());
        updateInitialComponentState();
    }

    public static Dimension adjustSize(Dimension size, int borderSize) {
        return new Dimension(size.width + borderSize, size.height + borderSize);
    }

    public WidgetHolder(Component comp, IResizableBorder border) {
        this.isComposite = false;
        setLayout(new BorderLayout());
        add(comp, BorderLayout.CENTER);
        setBorder(border);
    }

    public WidgetHolder(IWidget widget, IResizableBorder border) {
        this.isComposite = false;
        this.widget = widget;
        this.realComponent = widget.getComponent();
        this.setFocusable(false);
        setLayout(new BorderLayout());
        add(realComponent, BorderLayout.CENTER);
        setBorder(border);
    }

    @Override
    public void setBorder(Border border) {
        removeMouseListener(resizeListener);
        removeMouseMotionListener(resizeListener);
        if (border instanceof IResizableBorder) {
            addMouseListener(resizeListener);
            addMouseMotionListener(resizeListener);
        }
        super.setBorder(border);
    }

    private void didResized() {
        if (getParent() != null) {
            getParent().repaint();
            invalidate();
            ((JComponent) getParent()).revalidate();
        }
    }

    private void updateInitialComponentState() {
        LayoutManager layout = controller.getPanel().getZoomLayout();
        WidgetZoomLayout zoomLayout = (WidgetZoomLayout) layout;
        zoomLayout.setInitialComponentState(this, true);
        zoomLayout.setInitialComponentState(realComponent, true);
    }

    private class ResizeListener extends MouseInputAdapter {

        @Override
        public void mouseMoved(MouseEvent me) {
            IResizableBorder border = (IResizableBorder) getBorder();
            setCursor(Cursor.getPredefinedCursor(border.getResizeCursor(me)));
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            setCursor(Cursor.getDefaultCursor());
        }

        private int cursor;

        @Override
        public void mousePressed(MouseEvent me) {
            IResizableBorder border = (IResizableBorder) getBorder();
            cursor = border.getResizeCursor(me);
            startPos = me.getPoint();
        }

        /**
     * Moves and resizes this component. The new location of the top-left corner
     * is specified by <code>x</code> and <code>y</code>, and the new size is
     * specified by <code>width</code> and <code>height</code>.
     * 
     * @param x
     *          the new <i>x</i>-coordinate of this component
     * @param y
     *          the new <i>y</i>-coordinate of this component
     * @param width
     *          the new <code>width</code> of this component
     * @param height
     *          the new <code>height</code> of this component
     * @see #getBounds
     * @see #setLocation(int, int)
     * @see #setLocation(Point)
     * @see #setSize(int, int)
     * @see #setSize(Dimension)
     * @since JDK1.1
     */
        @Override
        public void mouseDragged(MouseEvent me) {
            if (startPos != null) {
                int dx = me.getX() - startPos.x;
                int dy = me.getY() - startPos.y;
                switch(cursor) {
                    case Cursor.N_RESIZE_CURSOR:
                        adjustComponentBounds(getX(), getY() + dy, getWidth(), getHeight() - dy);
                        didResized();
                        break;
                    case Cursor.S_RESIZE_CURSOR:
                        adjustComponentBounds(getX(), getY(), getWidth(), getHeight() + dy);
                        startPos = me.getPoint();
                        didResized();
                        break;
                    case Cursor.W_RESIZE_CURSOR:
                        adjustComponentBounds(getX() + dx, getY(), getWidth() - dx, getHeight());
                        didResized();
                        break;
                    case Cursor.E_RESIZE_CURSOR:
                        adjustComponentBounds(getX(), getY(), getWidth() + dx, getHeight());
                        startPos = me.getPoint();
                        didResized();
                        break;
                    case Cursor.NW_RESIZE_CURSOR:
                        adjustComponentBounds(getX() + dx, getY() + dy, getWidth() - dx, getHeight() - dy);
                        didResized();
                        break;
                    case Cursor.NE_RESIZE_CURSOR:
                        adjustComponentBounds(getX(), getY() + dy, getWidth() + dx, getHeight() - dy);
                        startPos = new Point(me.getX(), startPos.y);
                        didResized();
                        break;
                    case Cursor.SW_RESIZE_CURSOR:
                        adjustComponentBounds(getX() + dx, getY(), getWidth() - dx, getHeight() + dy);
                        startPos = new Point(startPos.x, me.getY());
                        didResized();
                        break;
                    case Cursor.SE_RESIZE_CURSOR:
                        adjustComponentBounds(getX(), getY(), getWidth() + dx, getHeight() + dy);
                        startPos = me.getPoint();
                        didResized();
                        break;
                    case Cursor.MOVE_CURSOR:
                        moveWidget(dx, dy);
                }
                setCursor(Cursor.getPredefinedCursor(cursor));
            }
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            startPos = null;
        }
    }

    ;

    private void adjustComponentBounds(int x, int y, int width, int height) {
        Rectangle currentBounds = getBounds();
        setBounds(x, y, width, height);
        newPreferredSize = adjustSize(new Dimension(width, height), -BORDER_WIDTH * 2);
        realComponent.setPreferredSize(newPreferredSize);
        updateInitialComponentState();
        controller.setDirty();
        if (!progress.isInProgress()) {
            fireUndoableEditHappened(new ResizeWidgetUndoableEdit(progress, this, currentBounds, new Rectangle(x, y, width, height)));
        }
    }

    /**
   * @see net.sourceforge.jruntimedesigner.common.IWidgetHolder#moveWidget(int,
   *      int)
   */
    public void moveWidget(int dx, int dy) {
        moveSingleWidget(dx, dy);
        if (isSelected()) {
            for (IWidgetHolder widget : controller.getSelectionManager().getSelectedWidgets()) {
                if (widget.isLocked()) {
                    continue;
                }
                if (widget == this) {
                    continue;
                }
                widget.moveSingleWidget(dx, dy);
            }
        }
    }

    /**
   * @see net.sourceforge.jruntimedesigner.common.IWidgetHolder#moveSingleWidget(int,
   *      int)
   */
    public void moveSingleWidget(int dx, int dy) {
        if (widget.isLocked()) {
            return;
        }
        Rectangle bounds = getBounds();
        bounds.translate(dx, dy);
        setBounds(bounds);
        updateInitialComponentState();
        didResized();
        controller.setDirty();
        if (!progress.isInProgress()) {
            fireUndoableEditHappened(new MoveWidgetUndoableEdit(progress, this, dx, dy));
        }
    }

    /**
   * @see net.sourceforge.jruntimedesigner.common.IWidgetHolder#moveWidget(java.awt.Point)
   */
    public void moveWidget(Point point) {
        if (widget.isLocked()) {
            return;
        }
        Point currentLocation = getBounds().getLocation();
        setBounds(new Rectangle(point, getBounds().getSize()));
        updateInitialComponentState();
        didResized();
        controller.setDirty();
        if (!progress.isInProgress()) {
            fireUndoableEditHappened(new MoveWidgetUndoableEdit(progress, this, currentLocation, point));
        }
    }

    /**
   * @see net.sourceforge.jruntimedesigner.common.IWidgetHolder#resizeWidget(int,
   *      int)
   */
    public void resizeWidget(int dx, int dy) {
        if (widget.isLocked()) {
            return;
        }
        adjustComponentBounds(getX(), getY(), getWidth() + dx, getHeight() + dy);
        didResized();
        controller.setDirty();
        if (!progress.isInProgress()) {
            fireUndoableEditHappened(new ResizeWidgetUndoableEdit(progress, this, dx, dy));
        }
    }

    /**
   * @see net.sourceforge.jruntimedesigner.common.IWidgetHolder#resizeWidget(java.awt.Dimension)
   */
    public void resizeWidget(Rectangle bounds) {
        adjustComponentBounds((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getSize().getWidth(), (int) bounds.getSize().getHeight());
        didResized();
    }

    /**
   * @see net.sourceforge.jruntimedesigner.common.IWidgetHolder#resizeWidget(java.awt.Dimension)
   */
    public void resizeWidget(Dimension size) {
        adjustComponentBounds(getX(), getY(), (int) size.getWidth(), (int) size.getHeight());
        didResized();
    }

    /**
   * Returns true if widget is in design mode.
   */
    public final boolean isDesignMode() {
        return controller != null ? controller.isDesignMode() : false;
    }

    /**
   * Controls the state whether the widget is in design mode.
   */
    public final void setDesignMode(boolean isDesignMode) {
        if (isDesignMode) {
            setBorder(resizableBorder);
        } else {
            setBorder(DEFAULT_BORDER);
        }
    }

    /**
   * Controls the state whether the widget is selected in the runtime designer.
   * 
   * @param isSelected
   */
    public final void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (!isSelected) {
            setGuide(false);
        }
        resizableBorder.setSelected(isSelected);
        repaint();
    }

    /**
   * Returns true if the widget is selected in the runtime designer.
   */
    public final boolean isSelected() {
        return isSelected;
    }

    /**
   * Controls the state whether the widget is to be used as a reference widget
   * during the group operation like alignment. Only one selected widget is
   * supposed to be a guide at any time. To assure it is a task of a selection
   * manager.
   * 
   * @param isGuide
   */
    public final void setGuide(boolean isGuide) {
        this.isGuide = isGuide;
        resizableBorder.setGuide(isGuide);
        repaint();
    }

    /**
   * Return true if the widget is to be used as a reference widget during the
   * group operation like alignment.
   */
    public final boolean isGuide() {
        return isGuide;
    }

    /**
   * @see net.sourceforge.jruntimedesigner.common.IWidgetHolder#setLocked(boolean)
   */
    public final void setLocked(boolean isLocked) {
        resizableBorder.setLocked(isLocked);
        repaint();
    }

    public boolean isLocked() {
        return widget.isLocked();
    }

    /**
   * @see net.sourceforge.jruntimedesigner.common.IWidgetHolder#getController()
   */
    public final JRuntimeDesignerController getController() {
        return controller;
    }

    /**
   * @see net.sourceforge.jruntimedesigner.common.IWidgetHolder#getWidget()
   */
    public IWidget getWidget() {
        return widget;
    }

    public Point getWidgetLocation() {
        return widget.getLocation();
    }

    public Dimension getWidgetSize() {
        return widget.getSize();
    }

    public boolean isResizable() {
        return widget.isResizable();
    }

    public int getLayer() {
        return widget.getLayer();
    }

    public void setLayer(int layer) {
        widget.setLayer(layer);
    }

    public void addUndoableEditListener(UndoableEditListener listener) {
        undoableModelSupport.addUndoableEditListener(listener);
    }

    public void removeUndoableEditListener(UndoableEditListener listener) {
        undoableModelSupport.removeUndoableEditListener(listener);
    }

    private void fireUndoableEditHappened(UndoableEdit edit) {
        undoableModelSupport.fireUndoableEditHappened(edit);
    }
}
