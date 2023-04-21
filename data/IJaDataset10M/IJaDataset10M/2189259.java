package synthlabgui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import synthlab.api.Port;

public class PortHandler extends JPanel implements MouseMotionListener, MouseListener {

    public PortHandler(Port port) {
        port_ = port;
        isInput_ = port_.isInput();
        setupGeneral();
    }

    public void setupGeneral() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.white);
        addMouseListener(this);
        addMouseMotionListener(this);
        setBounds(0, 0, 16, 14);
        setMinimumSize(new Dimension(16, 14));
        setMaximumSize(new Dimension(16, 14));
        setLayout(null);
        setOpaque(false);
        setVisible(true);
        String tipText = new String();
        tipText += "<html>";
        tipText += "<b>Name:</b> " + port_.getName() + "<br/>";
        tipText += "<b>Type:</b> " + port_.getValueType() + "<br/>";
        tipText += "<b>Unit:</b> " + port_.getValueUnit() + "<br/>";
        tipText += "<b>Range: </b> [" + port_.getValueRange().minimum + "; " + port_.getValueRange().maximum + "]<br/>";
        tipText += "<b>Description: </b><br/><i>" + port_.getDescription() + "</i>";
        tipText += "</html>";
        setToolTipText(tipText);
    }

    @Override
    public void paint(Graphics g) {
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        ((Graphics2D) g).setRenderingHints(renderHints);
        if (port_.isLinked()) {
            g.setColor(Color.yellow);
            g.fillOval(5, 3, 6, 6);
            g.setColor(Color.black);
        } else if (mouseOver) {
            g.setColor(Color.green.darker());
            g.fillOval(5, 3, 6, 6);
            g.setColor(Color.black);
        }
        ModulePoolPanel poolPanel = ((ModulePoolPanel) getParent().getParent());
        PortHandler selectedPort = poolPanel.getPortStart();
        if (poolPanel.isLinking() && !port_.isLinked() && ((selectedPort.getWrapped().isInput() && port_.isOutput()) || (selectedPort.getWrapped().isOutput() && port_.isInput()))) {
            if ((selectedPort.getWrapped().getValueType() == getWrapped().getValueType()) && (selectedPort.getWrapped().getValueUnit() == getWrapped().getValueUnit())) {
                g.setColor(Color.green.darker().darker().darker());
            } else {
                g.setColor(Color.green.darker());
            }
            ((Graphics2D) g).setStroke(new BasicStroke(1.5f));
            g.drawOval(5, 3, 6, 6);
            ((Graphics2D) g).setStroke(new BasicStroke(1));
            g.setColor(Color.black);
        } else {
            g.drawOval(5, 3, 6, 6);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseOver = true;
        MainWindow window = (MainWindow) getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent();
        window.getInformationPanel().monitor(this);
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseOver = false;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    public Port getWrapped() {
        return port_;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ((ModulePoolPanel) getParent().getParent()).endDrag((Module) getParent(), this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point start = new Point((int) (getBounds().getX() + getParent().getBounds().getX()), (int) (6 + getParent().getBounds().getY() + getBounds().getY()));
        Point end = new Point((int) (e.getPoint().getX() + start.getX()), (int) (e.getY() + start.getY() - 6));
        if (!isInput_) ((ModulePoolPanel) getParent().getParent()).updateDrag((Module) getParent(), this, start, end, false); else ((ModulePoolPanel) getParent().getParent()).updateDrag((Module) getParent(), this, end, start, true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        repaint();
    }

    private Port port_;

    private static final long serialVersionUID = 6479523243400160278L;

    private boolean mouseOver = false;

    boolean isInput_;
}
