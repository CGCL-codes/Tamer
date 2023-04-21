package mipt.gui.graph.plot.output;

import mipt.gui.graph.*;
import mipt.gui.graph.plot.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import mipt.gui.DoubleTextField;

/**
 * Panel containing Plot and editable fields of min/max, current position and so on
 * Note: this class was initially generated visually so it has a plenty of repeatable code  
 */
public class OutputPlot extends JPanel implements GraphListener, MouseMotionListener {

    private JLabel ivjCurX = null;

    private JLabel ivjCurY = null;

    private DoubleTextField ivjMaxX = null;

    private DoubleTextField ivjMaxY = null;

    private DoubleTextField ivjMinY = null;

    private DoubleTextField ivjMinX = null;

    public class LimitListener extends FocusAdapter implements ActionListener {

        public void focusLost(FocusEvent e) {
            limitChanged(e);
        }

        public void actionPerformed(ActionEvent e) {
            limitChanged(e);
        }
    }

    protected static final String blank = "         ";

    protected static final String preferredString = "12345.976";

    protected int mmReady = 0;

    protected int tcx = 0, tcy = 0, x = 0, y = 0, tcx1 = 0, tcy1 = 0, x1 = 0, y1 = 0;

    protected GraphRegion rgn = null;

    protected boolean preferredSize = false;

    protected Plot plot = null;

    protected double xFactor = 1.0, yFactor = 1.0;

    /**
 * 
 */
    public OutputPlot() {
        super();
        initialize();
    }

    /**
 * 
 * @return java.lang.String
 * @param value double
 */
    protected String doubleToString(double value) {
        return plot.doubleToStringX(value, Double.NaN);
    }

    /**
 * 
 */
    protected void initialize() {
        try {
            plot = new Plot();
            plot.setGraphListener(this);
            initListeners();
            initLayout();
            setSize(426, 240);
        } catch (Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    protected void initListeners() {
        LimitListener listener = new LimitListener();
        getMaxX().addActionListener(listener);
        getMaxY().addActionListener(listener);
        getMinX().addActionListener(listener);
        getMinY().addActionListener(listener);
        getMaxX().addFocusListener(listener);
        getMaxY().addFocusListener(listener);
        getMinX().addFocusListener(listener);
        getMinY().addFocusListener(listener);
        plot.addMouseMotionListener(this);
        ((Component) (this)).addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                x = y = tcx = tcy = x1 = y1 = tcx1 = tcy1 = 0;
            }
        });
    }

    /**
 *
 */
    protected void initLayout() {
        try {
            setLayout(new GridBagLayout());
            GridBagConstraints con = new GridBagConstraints();
            con.gridx = 1;
            con.gridy = 0;
            con.gridwidth = 3;
            con.gridheight = 3;
            con.fill = GridBagConstraints.BOTH;
            con.weightx = 1.0;
            con.weighty = 1.0;
            con.insets = new Insets(4, 4, 4, 4);
            add(plot, con);
            con = new GridBagConstraints();
            con.gridx = 1;
            con.gridy = 3;
            con.fill = GridBagConstraints.HORIZONTAL;
            con.weightx = 0.4;
            con.insets = new Insets(4, 4, 4, 4);
            add(getMinX(), con);
            con = new GridBagConstraints();
            con.gridx = 3;
            con.gridy = 3;
            con.fill = GridBagConstraints.HORIZONTAL;
            con.weightx = 0.4;
            con.insets = new Insets(4, 4, 4, 4);
            add(getMaxX(), con);
            con = new GridBagConstraints();
            con.gridx = 0;
            con.gridy = 2;
            con.fill = GridBagConstraints.HORIZONTAL;
            con.anchor = GridBagConstraints.SOUTH;
            con.weightx = 0.1;
            con.insets = new Insets(4, 4, 4, 4);
            add(getMinY(), con);
            con = new GridBagConstraints();
            con.gridx = 0;
            con.gridy = 0;
            con.fill = GridBagConstraints.HORIZONTAL;
            con.anchor = GridBagConstraints.NORTH;
            con.weightx = 0.1;
            con.insets = new Insets(4, 4, 4, 4);
            add(getMaxY(), con);
            con = new GridBagConstraints();
            con.gridx = 0;
            con.gridy = 1;
            con.fill = GridBagConstraints.HORIZONTAL;
            con.weighty = 1.0;
            con.weightx = 0.0;
            con.insets = new Insets(4, 4, 4, 4);
            add(getCurY(), con);
            con = new GridBagConstraints();
            con.gridx = 2;
            con.gridy = 3;
            con.weightx = 1.0;
            con.weighty = 0.0;
            con.insets = new Insets(4, 4, 4, 4);
            add(getCurX(), con);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * 
 */
    protected JLabel createLabel() {
        JLabel label = new JLabel(" ");
        label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        return label;
    }

    protected final JLabel getCurX() {
        if (ivjCurX == null) ivjCurX = createLabel();
        return ivjCurX;
    }

    protected final JLabel getCurY() {
        if (ivjCurY == null) ivjCurY = createLabel();
        return ivjCurY;
    }

    protected DoubleTextField createField() {
        DoubleTextField field = new DoubleTextField(true);
        field.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        return field;
    }

    protected final DoubleTextField getMaxX() {
        if (ivjMaxX == null) ivjMaxX = createField();
        return ivjMaxX;
    }

    protected final DoubleTextField getMaxY() {
        if (ivjMaxY == null) ivjMaxY = createField();
        return ivjMaxY;
    }

    protected final DoubleTextField getMinX() {
        if (ivjMinX == null) ivjMinX = createField();
        return ivjMinX;
    }

    protected final DoubleTextField getMinY() {
        if (ivjMinY == null) ivjMinY = createField();
        return ivjMinY;
    }

    /**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
    protected void handleException(Throwable exception) {
    }

    /**
 * 
 */
    public final Plot getPlot() {
        return plot;
    }

    /**
 * 
 * @return java.lang.String
 * @param x double
 * @param n int
 */
    public static String getPos(double x, int n) {
        String pos = Double.toString(x);
        int i;
        if ((i = pos.length()) >= 9) return pos.substring(0, 9);
        return blank.substring(0, 9 - i) + pos;
    }

    /**
 *  
 */
    protected void limitChanged(AWTEvent e) {
        try {
            if (e.getSource() == getMaxX()) {
                double newMaxX = getMaxX().getDoubleValue();
                setXLimits(rgn.minX, newMaxX * xFactor);
            } else if (e.getSource() == getMaxY()) {
                double newMaxY = getMaxY().getDoubleValue();
                setYLimits(rgn.minY, newMaxY * yFactor);
            } else if (e.getSource() == getMinY()) {
                double newMinY = getMinY().getDoubleValue();
                setYLimits(newMinY * yFactor, rgn.maxY);
            } else if (e.getSource() == getMinX()) {
                double newMinX = getMinX().getDoubleValue();
                setXLimits(newMinX * xFactor, rgn.maxX);
            } else return;
            this.repaint();
        } catch (Throwable exc) {
        }
    }

    /**
 * 
 */
    public void mouseDragged(MouseEvent me) {
    }

    /**
 * 
 */
    public void mouseMoved(MouseEvent me) {
        if (rgn != null && mmReady == 3) {
            double posX = rgn.minX + (me.getX() - rgn.x) / rgn.scaleX;
            double posY = rgn.minY - (me.getY() - rgn.y) / rgn.scaleY;
            Graphics gr = this.getGraphics();
            gr.setColor(Color.lightGray);
            gr.fillRect(x, y - tcy, tcx, tcy);
            gr.fillRect(x1, y1 - tcy1, tcx1, tcy1);
            String sPosX = doubleToString(posX / xFactor);
            String sPosY = doubleToString(posY / yFactor);
            Dimension win = this.getSize(), minX = getMinX().getSize(), maxX = getMaxX().getSize(), minY = getMinY().getSize(), maxY = getMaxY().getSize();
            FontMetrics fm = gr.getFontMetrics();
            tcy = fm.getHeight();
            tcx = fm.stringWidth(sPosX);
            x = minX.width + minY.width + ((win.width - minX.width - minY.width - maxX.width - tcx) >> 1);
            y = win.height - (tcy >> 1) - 3;
            tcy1 = fm.getHeight();
            tcx1 = fm.stringWidth(sPosY);
            x1 = ((minY.width - tcx) >> 1);
            y1 = win.height - minX.height - ((win.height - maxY.height - minX.height - minY.height + tcy) >> 1);
            gr.setColor(Color.black);
            gr.drawString(sPosX, x, y);
            gr.drawString(sPosY, x1, y1);
        }
    }

    /**
 * 
 * @param m double
 * @param M double
 */
    public void setXLimits(double m, double M) throws GraphException {
        if (rgn != null) {
            if (m == rgn.minX && M == rgn.maxX) return;
            rgn.minX = m = m >= rgn.maxX ? rgn.minX : m;
            rgn.maxX = M = M <= rgn.minX ? rgn.maxX : M;
        } else {
            if (m >= M) return;
            rgn = new GraphRegion();
            rgn.minX = m;
            rgn.maxX = M;
        }
        mmReady |= 1;
        getMaxX().setText(doubleToString(M / xFactor));
        getMinX().setText(doubleToString(m / xFactor));
        plot.setXLimits(m, M);
    }

    /**
 * 
 * @param m double
 * @param M double
 */
    public void setYLimits(double m, double M) throws GraphException {
        if (rgn != null) {
            if (m == rgn.minY && M == rgn.maxY) return;
            rgn.minY = m = m >= rgn.maxY ? rgn.minY : m;
            rgn.maxY = M = M <= rgn.minY ? rgn.maxY : M;
        } else {
            if (m >= M) return;
            rgn = new GraphRegion();
            rgn.minY = m;
            rgn.maxY = M;
        }
        mmReady |= 2;
        getMaxY().setText(doubleToString(M / yFactor));
        getMinY().setText(doubleToString(m / yFactor));
        plot.setYLimits(m, M);
    }

    /**
 * 
 * @param rgn mipt.gui.graph.GraphRegion
 */
    public void updateGraphRegion(GraphRegion _rgn) {
        if (!preferredSize) {
            preferredSize = true;
            FontMetrics fm = _rgn.gr.getFontMetrics();
            Dimension dim;
            getMaxY().setPreferredSize(dim = new Dimension(fm.stringWidth(preferredString), fm.getHeight()));
            getMaxY().setPreferredSize(dim);
        }
        if (rgn == null || (rgn.maxX != _rgn.maxX || rgn.minX != _rgn.minX)) ;
        if (rgn == null || (rgn.maxY != _rgn.maxY || rgn.minY != _rgn.minY)) ;
        rgn = _rgn;
        mmReady = 3;
    }
}
