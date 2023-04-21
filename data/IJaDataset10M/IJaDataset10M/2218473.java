package sk.tuke.ess.editor.base.document.gui;

import sk.tuke.ess.editor.base.commongui.canvas.Canvas;
import sk.tuke.ess.editor.base.commongui.canvas.CanvasChangeListener;
import sk.tuke.ess.editor.base.commongui.canvas.Grid;
import sk.tuke.ess.editor.base.commongui.canvas.State;
import sk.tuke.ess.editor.base.commongui.icons.IconManager;
import sk.tuke.ess.editor.base.commongui.menu.items.ColorMenuItem;
import sk.tuke.ess.editor.base.commongui.menu.items.SpinnerMenuItem;
import sk.tuke.ess.editor.base.commongui.windows.WindowManager;
import sk.tuke.ess.editor.base.document.DocumentWithHistorySupport;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: zladovan
 * Date: 16.5.2011
 * Time: 12:43
 * To change this template use File | Settings | File Templates.
 */
public abstract class DrawingDocumentWindow extends DocumentWindowWithHistorySupport {

    private Canvas canvas;

    private JLabel mousePosLabel;

    private JLabel zoomLabel;

    protected JToolBar toolBar;

    private ButtonGroup defaultToolbarButtonGroup;

    public DrawingDocumentWindow(WindowManager windowManager, DocumentWithHistorySupport document) {
        super(windowManager, document);
        configutreCanvasDrop();
    }

    @Override
    protected void initContent() {
        canvas = new Canvas(new Canvas.CanvasDrawer() {

            public void draw(Graphics2D graphics) {
                drawOnCanvas(graphics);
            }
        });
        initHeader();
        initFooter();
        contentPane.setViewportView(canvas);
    }

    protected void configutreCanvasDrop() {
        getCanvas().setDropTarget(new DropTarget(getCanvas(), new DropTargetAdapter() {

            private Point lastDragPoint = new Point(0, 0);

            public void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(dtde.getDropAction());
                boolean success = document.getDocumentBaseEditSupport().paste(dtde.getTransferable());
                dtde.dropComplete(success);
                documentChanged();
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                canvas.fireMousePositionChange(lastDragPoint, dtde.getLocation());
            }
        }));
    }

    protected abstract void drawOnCanvas(Graphics2D graphics);

    protected void initHeader() {
        defaultToolbarButtonGroup = new ButtonGroup();
        toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.setRollover(true);
        addToolbarActions(toolBar);
        headerPanel.add(toolBar);
    }

    protected abstract void addToolbarActions(JToolBar toolBar);

    protected void addButtonToDefaultGroup(JToolBar toolbar, AbstractButton button) {
        defaultToolbarButtonGroup.add(button);
        toolbar.add(button);
    }

    protected void selectFirstToolbarButton() {
        defaultToolbarButtonGroup.setSelected(defaultToolbarButtonGroup.getElements().nextElement().getModel(), true);
    }

    protected void initFooter() {
        mousePosLabel = new JLabel();
        updateMousePositionLabel(new Point(0, 0));
        mousePosLabel.setOpaque(false);
        mousePosLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        mousePosLabel.setPreferredSize(new Dimension(150, 18));
        mousePosLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mousePosLabel.setVerticalAlignment(SwingConstants.CENTER);
        zoomLabel = new JLabel();
        zoomLabel.setOpaque(false);
        updateZoomLabel();
        zoomLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        zoomLabel.setPreferredSize(new Dimension(100, 18));
        zoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
        zoomLabel.setVerticalAlignment(SwingConstants.CENTER);
        footerPanel.add(mousePosLabel);
        footerPanel.add(zoomLabel);
        canvas.addCanvasChangeListener(new CanvasChangeListener() {

            public void zoomChanged(double oldZoom, double newZoom) {
                updateZoomLabel();
            }

            public void mousePositionChanged(Point oldPosition, Point newPosition) {
                updateMousePositionLabel(newPosition);
            }
        });
    }

    @Override
    protected boolean enableHeader() {
        return true;
    }

    @Override
    protected boolean enableFooter() {
        return true;
    }

    /**
     * Nastavenie ukazovateľa aktuálneho pribíženia
     *
     */
    public void updateZoomLabel() {
        if (zoomLabel != null) zoomLabel.setText(String.format("zoom: %.0f%%", canvas.getZoom() * 100));
    }

    public void updateMousePositionLabel(Point position) {
        Point2D.Double tp = canvas.getTransformedPoint(position);
        mousePosLabel.setText(String.format("[ x: % 4.2f; y: % 4.2f ]", tp.x, tp.y));
    }

    public Canvas getCanvas() {
        return canvas;
    }

    protected JToggleButton createChangeStateButton(String name, String iconName, State newState) {
        return createToggleButton((new ChangeStateAction(name, IconManager.getInstance().get(iconName), newState)));
    }

    protected JToggleButton createToggleButton(Action action) {
        JToggleButton toggleButton = new JToggleButton(action);
        toggleButton.setToolTipText(toggleButton.getText());
        toggleButton.setText(null);
        return toggleButton;
    }

    protected JToggleButton createGridButton() {
        final JToggleButton button = createToggleButton(new ToggleGridAction());
        final JPopupMenu gridMenu = createGridMenu();
        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
                    gridMenu.show(button, e.getX(), e.getY());
                }
            }
        });
        return button;
    }

    private JPopupMenu createGridMenu() {
        JPopupMenu gridMenu = new JPopupMenu();
        gridMenu.add(new ColorMenuItem(canvas.getGrid().getColor(), new ColorMenuItem.ColorChangeListener() {

            public void onColorChange(Color previousColor, Color newColor) {
                canvas.getGrid().setColor(newColor);
                canvas.repaint();
            }
        }));
        SpinnerModel spinnerModel = new SpinnerNumberModel(canvas.getGrid().getSize(), Grid.MIN_SIZE, Grid.MAX_SIZE, 1);
        spinnerModel.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                canvas.getGrid().setSize(((Number) ((SpinnerModel) e.getSource()).getValue()).intValue());
                canvas.repaint();
            }
        });
        gridMenu.add(new SpinnerMenuItem(spinnerModel));
        return gridMenu;
    }

    protected class ChangeStateAction extends AbstractAction {

        private State newState;

        public ChangeStateAction(String name, Icon icon, State newState) {
            super(name, icon);
            this.newState = newState;
        }

        public void actionPerformed(ActionEvent e) {
            canvas.setState(newState);
        }

        public void setNewState(State newState) {
            this.newState = newState;
        }
    }

    private class ToggleGridAction extends AbstractAction {

        boolean isVisible;

        public ToggleGridAction() {
            super("Mriežka - pravý klik pre nastavenia", IconManager.getInstance().get("icon-toolbar-grid.png"));
            isVisible = getCanvas().getGrid().isVisible();
        }

        public void actionPerformed(ActionEvent e) {
            isVisible = !isVisible;
            getCanvas().getGrid().setVisible(isVisible);
            getCanvas().repaint();
        }
    }

    @Override
    public void updateDocumentWindow() {
        canvas.repaint();
        super.updateDocumentWindow();
    }

    public void fireDocumentChangeWithoutWindowUpdate() {
        windowManager.getDocumentWindowsPane().fireDocumentChanged(document);
    }
}
