package synthlabgui.presentation.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import synthlabgui.controller.PoolController;
import synthlabgui.presentation.ModulePresentation;
import synthlabgui.presentation.PoolPresentation;

public class ModulePoolPanel extends JPanel implements PoolPresentation, MouseListener, MouseMotionListener, DropTargetListener {

    private static final long serialVersionUID = 1943028163107894975L;

    private MainWindow parentPanel;

    private PoolController controller;

    public ModulePoolPanel(PoolController controller, MainWindow parent) {
        super();
        this.controller = controller;
        this.parentPanel = parent;
        setupGeneral();
        setTransferHandler(new ModuleTransferHandler());
        try {
            getDropTarget().addDropTargetListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addModule(ModulePresentation module) {
        JPanel ui = (JPanel) module;
        add(ui);
        ui.setBounds(dropPosition_.x - 100, dropPosition_.y - 20, ui.getBounds().width, ui.getBounds().height);
    }

    public void removeModule(Module module) {
        for (PortHandler ph : module.getPorts()) {
            unlink(ph);
        }
        pool_.unregister(module.getWrapped());
        module.close();
        remove(module);
        repaint();
    }

    public void paint(Graphics g) {
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        ((Graphics2D) g).setRenderingHints(renderHints);
        g.drawImage(background_, getWidth() / 2 - background_.getWidth(null) / 2, getHeight() / 2 - background_.getHeight(null) / 2, null);
        drawLinks(g);
        if (controller.isLinking()) {
            drawLink(g, linkStart_.x, linkStart_.y, linkCurrent_.x, linkCurrent_.y, Color.yellow);
        }
        super.paint(g);
    }

    public boolean isLinking() {
        return controller.isLinking();
    }

    private void drawLinks(Graphics g) {
        selectedLink_ = null;
        for (Map.Entry<PortHandler, PortHandler> entry : controller.getlinks().entrySet()) {
            int x0 = (int) (entry.getKey().getBounds().getX() + entry.getKey().getParent().getBounds().getX());
            int y0 = (int) (entry.getKey().getBounds().getY() + entry.getKey().getParent().getBounds().getY()) + 6;
            int x1 = (int) (entry.getValue().getBounds().getX() + entry.getValue().getParent().getBounds().getX());
            int y1 = (int) (entry.getValue().getBounds().getY() + entry.getValue().getParent().getBounds().getY()) + 6;
            boolean selected = linkContains(x0, y0, x1, y1, linkCurrent_);
            drawLink(g, x0, y0, x1, y1, selected ? Color.red : Color.yellow);
            if (selected) selectedLink_ = entry;
        }
    }

    private void drawLink(Graphics g, int x0, int y0, int x1, int y1, Color c) {
        Graphics2D g2 = (Graphics2D) g;
        GeneralPath drawPath = new GeneralPath();
        drawPath.moveTo(x0, y0);
        drawPath.curveTo(x0 + 100, y0, x1 - 100, y1, x1, y1);
        g2.setStroke(new BasicStroke(5));
        g2.setColor(Color.darkGray);
        g2.draw(drawPath);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(c);
        ;
        g2.draw(drawPath);
        g.setColor(Color.black);
        g2.setStroke(new BasicStroke(1));
    }

    private boolean linkContains(int x0, int y0, int x1, int y1, Point p) {
        int marginX = 5;
        int marginY = 5;
        GeneralPath selectPathX = new GeneralPath();
        GeneralPath selectPathY = new GeneralPath();
        x0 += marginX;
        x1 += marginX;
        selectPathX.moveTo(x0, y0);
        selectPathX.curveTo(x0 + 100, y0, x1 - 100, y1, x1, y1);
        x0 -= marginX * 2;
        x1 -= marginX * 2;
        selectPathX.lineTo(x1, y1);
        selectPathX.curveTo(x1 - 100, y1, x0 + 100, y0, x0, y0);
        selectPathX.closePath();
        x0 += marginX;
        x1 += marginX;
        y0 += marginY;
        y1 += marginY;
        selectPathY.moveTo(x0, y0);
        selectPathY.curveTo(x0 + 100, y0, x1 - 100, y1, x1, y1);
        y0 -= marginY * 2;
        y1 -= marginY * 2;
        selectPathY.lineTo(x1, y1);
        selectPathY.curveTo(x1 - 100, y1, x0 + 100, y0, x0, y0);
        selectPathY.closePath();
        return selectPathX.contains(p) || selectPathY.contains(p);
    }

    private void setupGeneral() {
        setLayout(null);
        setBackground(Color.white);
        addMouseListener(this);
        addMouseMotionListener(this);
        setOpaque(false);
        background_ = new ImageIcon("background.jpg").getImage();
        setVisible(true);
    }

    public void updateLinks() {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        linkCurrent_ = e.getPoint();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (selectedLink_ != null && e.getButton() == MouseEvent.BUTTON3) {
            unlink(selectedLink_.getValue());
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        endDrag(null, null);
    }

    @Override
    public void dragEnter(DropTargetDragEvent arg0) {
    }

    @Override
    public void dragExit(DropTargetEvent arg0) {
    }

    @Override
    public void dragOver(DropTargetDragEvent e) {
        dropPosition_ = e.getLocation();
    }

    @Override
    public void drop(DropTargetDropEvent e) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent arg0) {
    }

    public void updateDrag(Module module, PortHandler port, Point e, Point p) {
        controller.setLinking(true);
        linkStart_ = e;
        linkCurrent_ = p;
        portStart_ = port;
        repaint();
    }

    public void updateDrag(Module module, PortHandler port, Point e, Point p, boolean reversed) {
        controller.setLinking(true);
        linkStart_ = e;
        linkCurrent_ = p;
        portStart_ = port;
        reversed_ = reversed;
        repaint();
    }

    public PortHandler getPortStart() {
        return portStart_;
    }

    public void endDrag(Module module, PortHandler port) {
        controller.setLinking(false);
        repaint();
        if (module == null || port == null) return;
        Component c;
        if (!reversed_) c = findComponentAt(linkCurrent_); else c = findComponentAt(linkStart_);
        if (c.getClass().equals(PortHandler.class)) {
            link(portStart_, (PortHandler) c);
        }
    }

    public void link(PortHandler output, PortHandler input) {
        if ((output.getWrapped().isInput() && input.getWrapped().isInput()) || (output.getWrapped().isOutput() && input.getWrapped().isOutput())) {
            return;
        }
        PortHandler i;
        PortHandler o;
        if (output.getWrapped().isInput()) {
            i = output;
            o = input;
        } else {
            i = input;
            o = output;
        }
        if (o.getWrapped().isLinked()) {
            String message = "The port \"" + o.getWrapped().getName() + "\" is already linked to an other port.\nRemove this previous link?";
            int result = JOptionPane.showConfirmDialog(this, message, "Linking problem", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.NO_OPTION) return; else {
                unlink(o);
            }
        }
        if (i.getWrapped().isLinked()) {
            String message = "The port \"" + i.getWrapped().getName() + "\" is already linked to an other port.\nRemove this previous link?";
            int result = JOptionPane.showConfirmDialog(this, message, "Linking problem", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.NO_OPTION) return; else {
                unlink(i);
            }
        }
        controller.getAbstraction().link(o.getWrapped(), i.getWrapped());
        controller.getlinks().put(o, i);
        ((Module) (i.getParent())).getConfigWindow().refresh();
    }

    public void unlink(PortHandler port) {
        PortHandler port2 = links_.get(port);
        if (port2 != null) {
            pool_.unlink(port.getWrapped(), links_.get(port).getWrapped());
            links_.remove(port);
            ((Module) (port2.getParent())).getConfigWindow().refresh();
        } else {
            port2 = links_.inverse().get(port);
            if (port2 != null) {
                pool_.unlink(port2.getWrapped(), links_.get(port2).getWrapped());
                links_.remove(port2);
                ((Module) (port.getParent())).getConfigWindow().refresh();
            }
        }
    }

    private Map.Entry<PortHandler, PortHandler> selectedLink_;

    private Image background_;

    private Point dropPosition_;

    private Point linkStart_;

    private Point linkCurrent_;
}
