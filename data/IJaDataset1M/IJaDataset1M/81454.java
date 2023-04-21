package org.alcibiade.sculpt.gui.viewport;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.alcibiade.sculpt.gui.action.ActionViewChangePainter;
import org.alcibiade.sculpt.gui.action.ActionViewChangeProjection;
import org.alcibiade.sculpt.gui.action.ActionViewChangeTarget;
import org.alcibiade.sculpt.gui.action.ActionViewChangeViewpoint;
import org.alcibiade.sculpt.gui.action.ActionViewReset;
import org.alcibiade.sculpt.gui.action.ActionViewResetTarget;
import org.alcibiade.sculpt.gui.i18n.ResourceManager;
import org.alcibiade.sculpt.gui.viewport.painter.AnnotationsPainter;
import org.alcibiade.sculpt.gui.viewport.painter.BackgroundPainter;
import org.alcibiade.sculpt.gui.viewport.painter.BasicAnnotationsPainter;
import org.alcibiade.sculpt.gui.viewport.painter.InformationsPainter;
import org.alcibiade.sculpt.gui.viewport.painter.MeshPainter;
import org.alcibiade.sculpt.gui.viewport.painter.SolidPainter;
import org.alcibiade.sculpt.gui.viewport.painter.WireframePainter;

/**
 * Actual viewport component implementation.
 * 
 * @author Yannick Kirschhoffer
 * 
 */
public class ManagedViewport extends AnnotatedSceneViewport implements ActionViewChangeTarget, ActionViewResetTarget, MouseWheelListener, MouseMotionListener, MouseListener {

    /**
	 * This is the origin of the current mouse drag, if one is currently
	 * happening.
	 */
    private Point view_drag_origin = null;

    private ManageableViewpoint originalPoint = null;

    private ManageableViewpoint viewPoint = null;

    private ManageableProjection viewProjection = null;

    private BackgroundPainter backgroundPainter = null;

    private MeshPainter viewPainter = null;

    private InformationsPainter informationsPainter = null;

    private AnnotationsPainter annotationsPainter = null;

    /**
	 * Create a new viewport instance.
	 * 
	 * @param projection
	 *            The viewport's projection type.
	 * @param viewpoint
	 *            The viewport's viewpoint.
	 * @param painter
	 *            The painter used for rendering.
	 */
    public ManagedViewport(ManageableProjection projection, ManageableViewpoint viewpoint, MeshPainter painter, boolean editable) {
        if (editable) {
            JPopupMenu popupmenu = buildPopupMenu();
            setComponentPopupMenu(popupmenu);
        }
        setProjection(projection);
        setViewpoint(viewpoint);
        setPainter(painter);
        backgroundPainter = new BackgroundPainter();
        annotationsPainter = new BasicAnnotationsPainter();
        informationsPainter = new InformationsPainter();
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
    }

    public void setViewpoint(ManageableViewpoint view) {
        if (!view.equals(viewPoint)) {
            originalPoint = view;
            viewPoint = new ViewportViewpoint(originalPoint);
            repaint();
        }
    }

    public void setProjection(ManageableProjection projection) {
        if (!projection.equals(viewProjection)) {
            viewProjection = projection;
            repaint();
        }
    }

    public void setPainter(MeshPainter painter) {
        if (!painter.equals(viewPainter)) {
            viewPainter = painter;
            repaint();
        }
    }

    public void resetView() {
        viewPoint = new ViewportViewpoint(originalPoint);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension size = getSize();
        backgroundPainter.paint(g2, size);
        viewPainter.paint(g2, getSize(), getMesh(), viewProjection, viewPoint);
        annotationsPainter.paint(g2, getSize(), getAnnotations(), viewProjection, viewPoint);
        informationsPainter.paint(g2, size, viewProjection, viewPoint);
    }

    public void mouseWheelMoved(MouseWheelEvent arg0) {
        int units = arg0.getUnitsToScroll();
        if (units < -9) {
            units = -9;
        }
        viewPoint.zoom(units);
        viewProjection.zoom(units);
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
        int mods = e.getModifiersEx();
        if ((mods & InputEvent.BUTTON2_DOWN_MASK) != 0) {
            Point pos = e.getPoint();
            if (view_drag_origin != null) {
                int delta_x = pos.x - view_drag_origin.x;
                int delta_y = view_drag_origin.y - pos.y;
                if ((mods & InputEvent.SHIFT_DOWN_MASK) != 0) {
                    viewPoint.rotate(delta_x, delta_y);
                    viewProjection.rotate(delta_x, delta_y);
                    repaint();
                } else {
                    viewPoint.move(delta_x, delta_y);
                    viewProjection.move(delta_x, delta_y);
                    repaint();
                }
            }
            view_drag_origin = pos;
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent e) {
        view_drag_origin = e.getPoint();
        viewPainter.setFast(true);
    }

    public void mouseReleased(MouseEvent arg0) {
        viewPainter.setFast(false);
        repaint();
    }

    /**
	 * Create this component's popup menu.
	 * 
	 * @return the popup menu instance.
	 */
    private JPopupMenu buildPopupMenu() {
        ResourceManager resourceManager = ResourceManager.getResourceManager();
        JMenu menu_projection = new JMenu(resourceManager.getLabel("menu.projection"));
        JMenuItem proj_orthographic = new JMenuItem(new ActionViewChangeProjection(this, "projection.orthographic", new ViewportOrthographicProjection()));
        menu_projection.add(proj_orthographic);
        JMenuItem proj_perspective = new JMenuItem(new ActionViewChangeProjection(this, "projection.perspective", new ViewportPerspectiveProjection()));
        menu_projection.add(proj_perspective);
        JMenu menu_view = new JMenu(resourceManager.getLabel("menu.view"));
        menu_view.add(new JMenuItem(new ActionViewChangeViewpoint(this, ViewportViewpoint.FRONT)));
        menu_view.add(new JMenuItem(new ActionViewChangeViewpoint(this, ViewportViewpoint.TOP)));
        menu_view.add(new JMenuItem(new ActionViewChangeViewpoint(this, ViewportViewpoint.RIGHT)));
        menu_view.add(new JMenuItem(new ActionViewChangeViewpoint(this, ViewportViewpoint.PERSPECTIVE)));
        JMenu menu_painter = new JMenu(resourceManager.getLabel("menu.painter"));
        menu_painter.add(new JMenuItem(new ActionViewChangePainter(this, "painter.wireframe", new WireframePainter())));
        menu_painter.add(new JMenuItem(new ActionViewChangePainter(this, "painter.solid", new SolidPainter())));
        JPopupMenu popupmenu = new JPopupMenu();
        popupmenu.add(menu_view);
        popupmenu.add(menu_projection);
        popupmenu.add(menu_painter);
        popupmenu.add(new JMenuItem(new ActionViewReset(this)));
        return popupmenu;
    }
}
