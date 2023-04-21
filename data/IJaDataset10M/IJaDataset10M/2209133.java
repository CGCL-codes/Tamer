package net.sourceforge.ondex.ovtk2.layout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.GDS;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.ovtk2.graph.ONDEXEdge;
import net.sourceforge.ondex.ovtk2.graph.ONDEXNode;
import net.sourceforge.ondex.ovtk2.graph.ONDEXSparseGraph;
import net.sourceforge.ondex.ovtk2.ui.AbstractOVTK2Viewer;
import net.sourceforge.ondex.tools.threading.monitoring.Monitorable;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.algorithms.layout.util.RandomLocationTransformer;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.algorithms.shortestpath.Distance;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.algorithms.util.IterativeContext;

/**
 * Layout based on the KKLayout from JUNG taking GDS values into account for
 * edge weights.
 *
 * @author taubertj
 * @version 18.03.2008
 */
public class GDSValueKKLayout extends OVTK2Layouter implements ActionListener, ChangeListener, IterativeContext, Monitorable {

    private AttributeName an = null;

    private int edge_length = 50;

    private int disconnected_length = 15;

    private int maxIterations = 2000;

    private boolean inverseScale = true;

    private boolean exchangeVertices = true;

    private JSlider sliderEdgeLength = null;

    private JSlider sliderDisconnectedLength = null;

    private JSlider sliderMaxIter = null;

    private JCheckBox exchangeBox = null;

    private JCheckBox inverseScaleBox = null;

    private ONDEXSparseGraph graph = null;

    private double EPSILON = 0.1d;

    private int currentIteration;

    private String status = Monitorable.STATE_IDLE;

    private double K = 1;

    private double[][] dm;

    private ONDEXNode[] vertices;

    private Point2D[] xydata;

    private boolean cancelled = false;

    protected Distance<ONDEXNode> distance;

    public String getStatus() {
        return status + this.getSize();
    }

    /**
     * This one is an incremental visualization.
     */
    public boolean isIncremental() {
        return true;
    }

    /**
     * Returns true once the current iteration has passed the maximum count.
     */
    public boolean done() {
        if (currentIteration > maxIterations) {
            return true;
        }
        return false;
    }

    public void initialize() {
        currentIteration = 0;
        if (graph != null) {
            int n = graph.getVertexCount();
            dm = new double[n][n];
            vertices = graph.getVertices().toArray(new ONDEXNode[0]);
            xydata = new Point2D[n];
            while (true) {
                try {
                    int index = 0;
                    for (ONDEXNode v : graph.getVertices()) {
                        Point2D xyd = transform(v);
                        vertices[index] = v;
                        xydata[index] = xyd;
                        index++;
                    }
                    break;
                } catch (ConcurrentModificationException cme) {
                }
            }
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    Number d_ij = distance.getDistance(vertices[i], vertices[j]);
                    Number d_ji = distance.getDistance(vertices[j], vertices[i]);
                    double dist = disconnected_length;
                    if (d_ij != null) dist = Math.min(d_ij.doubleValue(), dist);
                    if (d_ji != null) dist = Math.min(d_ji.doubleValue(), dist);
                    dm[i][j] = dm[j][i] = dist;
                }
            }
        }
    }

    public void step() {
        try {
            currentIteration++;
            if (cancelled) return;
            double energy = calcEnergy();
            status = "Kamada-Kawai V=" + getGraph().getVertexCount() + "(" + getGraph().getVertexCount() + ")" + " IT: " + currentIteration + " E=" + energy;
            int n = getGraph().getVertexCount();
            if (n == 0) return;
            double maxDeltaM = 0;
            int pm = -1;
            for (int i = 0; i < n; i++) {
                if (isLocked(vertices[i])) continue;
                double deltam = calcDeltaM(i);
                if (maxDeltaM < deltam) {
                    maxDeltaM = deltam;
                    pm = i;
                }
            }
            if (pm == -1) return;
            for (int i = 0; i < 100; i++) {
                double[] dxy = calcDeltaXY(pm);
                xydata[pm].setLocation(xydata[pm].getX() + dxy[0], xydata[pm].getY() + dxy[1]);
                double deltam = calcDeltaM(pm);
                if (deltam < EPSILON) break;
            }
            if (exchangeVertices && maxDeltaM < EPSILON) {
                energy = calcEnergy();
                for (int i = 0; i < n - 1; i++) {
                    if (isLocked(vertices[i])) continue;
                    for (int j = i + 1; j < n; j++) {
                        if (isLocked(vertices[j])) continue;
                        double xenergy = calcEnergyIfExchanged(i, j);
                        if (energy > xenergy) {
                            double sx = xydata[i].getX();
                            double sy = xydata[i].getY();
                            xydata[i].setLocation(xydata[j]);
                            xydata[j].setLocation(sx, sy);
                            return;
                        }
                    }
                }
            }
        } finally {
        }
    }

    @Override
    public void setSize(Dimension size) {
        setInitializer(new RandomLocationTransformer<ONDEXNode>(size));
        super.setSize(size);
    }

    /**
     * Enable or disable the local minimum escape technique by exchanging
     * vertices.
     */
    public void setExchangeVertices(boolean on) {
        exchangeVertices = on;
    }

    /**
     * Returns true if the local minimum escape technique by exchanging vertices
     * is enabled.
     */
    public boolean getExchangeVertices() {
        return exchangeVertices;
    }

    /**
     * Determines a step to new position of the vertex m.
     */
    private double[] calcDeltaXY(int m) {
        double dE_dxm = 0;
        double dE_dym = 0;
        double d2E_d2xm = 0;
        double d2E_dxmdym = 0;
        double d2E_dymdxm = 0;
        double d2E_d2ym = 0;
        for (int i = 0; i < vertices.length; i++) {
            if (i != m) {
                double dist = dm[m][i];
                double l_mi = edge_length * dist;
                double k_mi = K / (dist * dist);
                double dx = xydata[m].getX() - xydata[i].getX();
                double dy = xydata[m].getY() - xydata[i].getY();
                double d = Math.sqrt(dx * dx + dy * dy);
                double ddd = d * d * d;
                dE_dxm += k_mi * (1 - l_mi / d) * dx;
                dE_dym += k_mi * (1 - l_mi / d) * dy;
                d2E_d2xm += k_mi * (1 - l_mi * dy * dy / ddd);
                d2E_dxmdym += k_mi * l_mi * dx * dy / ddd;
                d2E_d2ym += k_mi * (1 - l_mi * dx * dx / ddd);
            }
        }
        d2E_dymdxm = d2E_dxmdym;
        double denomi = d2E_d2xm * d2E_d2ym - d2E_dxmdym * d2E_dymdxm;
        double deltaX = (d2E_dxmdym * dE_dym - d2E_d2ym * dE_dxm) / denomi;
        double deltaY = (d2E_dymdxm * dE_dxm - d2E_d2xm * dE_dym) / denomi;
        return new double[] { deltaX, deltaY };
    }

    /**
     * Calculates the gradient of energy function at the vertex m.
     */
    private double calcDeltaM(int m) {
        double dEdxm = 0;
        double dEdym = 0;
        for (int i = 0; i < vertices.length; i++) {
            if (i != m) {
                double dist = dm[m][i];
                double l_mi = edge_length * dist;
                double k_mi = K / (dist * dist);
                double dx = xydata[m].getX() - xydata[i].getX();
                double dy = xydata[m].getY() - xydata[i].getY();
                double d = Math.sqrt(dx * dx + dy * dy);
                double common = k_mi * (1 - l_mi / d);
                dEdxm += common * dx;
                dEdym += common * dy;
            }
        }
        return Math.sqrt(dEdxm * dEdxm + dEdym * dEdym);
    }

    /**
     * Calculates the energy function E.
     */
    private double calcEnergy() {
        double energy = 0;
        for (int i = 0; i < vertices.length - 1; i++) {
            for (int j = i + 1; j < vertices.length; j++) {
                double dist = dm[i][j];
                double l_ij = edge_length * dist;
                double k_ij = K / (dist * dist);
                double dx = xydata[i].getX() - xydata[j].getX();
                double dy = xydata[i].getY() - xydata[j].getY();
                double d = Math.sqrt(dx * dx + dy * dy);
                energy += k_ij / 2 * (dx * dx + dy * dy + l_ij * l_ij - 2 * l_ij * d);
            }
        }
        return energy;
    }

    /**
     * Calculates the energy function E as if positions of the specified
     * vertices are exchanged.
     */
    private double calcEnergyIfExchanged(int p, int q) {
        if (p >= q) throw new RuntimeException("p should be < q");
        double energy = 0;
        for (int i = 0; i < vertices.length - 1; i++) {
            for (int j = i + 1; j < vertices.length; j++) {
                int ii = i;
                int jj = j;
                if (i == p) ii = q;
                if (j == q) jj = p;
                double dist = dm[i][j];
                double l_ij = edge_length * dist;
                double k_ij = K / (dist * dist);
                double dx = xydata[ii].getX() - xydata[jj].getX();
                double dy = xydata[ii].getY() - xydata[jj].getY();
                double d = Math.sqrt(dx * dx + dy * dy);
                energy += k_ij / 2 * (dx * dx + dy * dy + l_ij * l_ij - 2 * l_ij * d);
            }
        }
        return energy;
    }

    public void reset() {
        currentIteration = 0;
    }

    /**
     * Initialises unit distance measure.
     *
     * @param viewer OVTK2Viewer
     */
    public GDSValueKKLayout(AbstractOVTK2Viewer viewer) {
        super(viewer);
        this.graph = viewer.getJUNGGraph();
        this.distance = new UnweightedShortestPath<ONDEXNode, ONDEXEdge>(graph);
    }

    @Override
    public JPanel getOptionPanel() {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);
        JComboBox box = new JComboBox();
        box.addActionListener(this);
        box.addItem("None");
        box.setSelectedIndex(0);
        for (AttributeName an : aog.getMetaData().getAttributeNames()) {
            Class<?> cl = an.getDataType();
            if (cl != null && cl.isAssignableFrom(Number.class)) {
                Set<ONDEXRelation> relations = aog.getRelationsOfAttributeName(an);
                if (relations.size() > 0) box.addItem(an.getId());
            }
        }
        panel.add(new JLabel("Select AttributeName:"));
        panel.add(box);
        inverseScaleBox = new JCheckBox("inverse scaling");
        inverseScaleBox.setSelected(inverseScale);
        inverseScaleBox.addChangeListener(this);
        panel.add(inverseScaleBox);
        sliderMaxIter = new JSlider();
        sliderMaxIter.setBorder(BorderFactory.createTitledBorder("max iterations"));
        sliderMaxIter.setMinimum(0);
        sliderMaxIter.setMaximum(5000);
        sliderMaxIter.setValue(maxIterations);
        sliderMaxIter.setMajorTickSpacing(1000);
        sliderMaxIter.setMinorTickSpacing(100);
        sliderMaxIter.setPaintTicks(true);
        sliderMaxIter.setPaintLabels(true);
        sliderMaxIter.addChangeListener(this);
        panel.add(sliderMaxIter);
        sliderEdgeLength = new JSlider();
        sliderEdgeLength.setBorder(BorderFactory.createTitledBorder("Preferred Edge Length"));
        sliderEdgeLength.setMinimum(0);
        sliderEdgeLength.setMaximum(100);
        sliderEdgeLength.setValue(edge_length);
        sliderEdgeLength.setMajorTickSpacing(20);
        sliderEdgeLength.setMinorTickSpacing(5);
        sliderEdgeLength.setPaintTicks(true);
        sliderEdgeLength.setPaintLabels(true);
        sliderEdgeLength.addChangeListener(this);
        panel.add(sliderEdgeLength);
        sliderDisconnectedLength = new JSlider();
        sliderDisconnectedLength.setBorder(BorderFactory.createTitledBorder("Disconnected Distance"));
        sliderDisconnectedLength.setMinimum(0);
        sliderDisconnectedLength.setMaximum(40);
        sliderDisconnectedLength.setValue(disconnected_length);
        sliderDisconnectedLength.setMajorTickSpacing(10);
        sliderDisconnectedLength.setMinorTickSpacing(2);
        sliderDisconnectedLength.setPaintTicks(true);
        sliderDisconnectedLength.setPaintLabels(true);
        sliderDisconnectedLength.addChangeListener(this);
        panel.add(sliderDisconnectedLength);
        exchangeBox = new JCheckBox("exchange vertices");
        exchangeBox.setSelected(exchangeVertices);
        exchangeBox.addChangeListener(this);
        panel.add(exchangeBox);
        return panel;
    }

    /**
     * Check for selection of an AttributeName.
     */
    public void actionPerformed(ActionEvent arg0) {
        JComboBox box = (JComboBox) arg0.getSource();
        String name = (String) box.getSelectedItem();
        an = aog.getMetaData().getAttributeName(name);
        if (an == null) {
            this.distance = new UnweightedShortestPath<ONDEXNode, ONDEXEdge>(graph);
            this.reset();
        } else {
            this.distance = new DijkstraDistance<ONDEXNode, ONDEXEdge>(graph, new GDSEdges(an, inverseScale), true);
            this.reset();
        }
    }

    /**
     * Class for wrapping edge weights returned from GDS.
     *
     * @author taubertj
     * @version 12.03.2008
     */
    private class GDSEdges implements Transformer<ONDEXEdge, Number> {

        Map<Integer, Number> cache;

        double minimum = Double.POSITIVE_INFINITY;

        double maximum = Double.NEGATIVE_INFINITY;

        boolean inverse = false;

        /**
         * Extract edge weights from GDS for a given AttributeName.
         *
         * @param an      AttributeName
         * @param inverse boolean
         */
        public GDSEdges(AttributeName an, boolean inverse) {
            cache = new Hashtable<Integer, Number>();
            this.inverse = inverse;
            Map<RelationType, Map<Integer, Number>> temp = new Hashtable<RelationType, Map<Integer, Number>>();
            for (ONDEXRelation r : aog.getRelationsOfAttributeName(an)) {
                RelationType rt = r.getOfType();
                GDS gds = r.getGDS(an);
                if (!temp.containsKey(rt)) temp.put(rt, new Hashtable<Integer, Number>());
                temp.get(rt).put(r.getId(), (Number) gds.getValue());
            }
            Iterator<RelationType> itrt = temp.keySet().iterator();
            while (itrt.hasNext()) {
                RelationType rtset = itrt.next();
                Iterator<Integer> itint = temp.get(rtset).keySet().iterator();
                while (itint.hasNext()) {
                    Integer key = itint.next();
                    double value = temp.get(rtset).get(key).doubleValue();
                    if (value < minimum) minimum = value;
                    if (value > maximum) maximum = value;
                }
                double diff = maximum - minimum;
                double value, newvalue;
                if (diff != 0) {
                    itint = temp.get(rtset).keySet().iterator();
                    while (itint.hasNext()) {
                        Integer key = itint.next();
                        value = temp.get(rtset).get(key).doubleValue();
                        if (inverse) newvalue = 2 - ((value - minimum) / diff); else newvalue = 1 + ((value - minimum) / diff);
                        cache.put(key, Double.valueOf(newvalue));
                    }
                }
            }
        }

        /**
         * Return transformation lookup from cache.
         *
         * @param input ONDEXEdge
         * @return Number
         */
        public Number transform(ONDEXEdge input) {
            Number number = cache.get(input.getId());
            if (number == null) return 1;
            return number;
        }
    }

    /**
     * Performs updates of layout parameters.
     */
    public void stateChanged(ChangeEvent arg0) {
        if (arg0.getSource().equals(sliderEdgeLength)) {
            edge_length = sliderEdgeLength.getValue();
            currentIteration = 0;
        } else if (arg0.getSource().equals(sliderDisconnectedLength)) {
            disconnected_length = sliderDisconnectedLength.getValue();
            currentIteration = 0;
        } else if (arg0.getSource().equals(sliderMaxIter)) {
            maxIterations = sliderMaxIter.getValue();
            currentIteration = 0;
        } else if (arg0.getSource().equals(exchangeBox)) {
            exchangeVertices = exchangeBox.isSelected();
            currentIteration = 0;
        } else if (arg0.getSource().equals(inverseScaleBox)) {
            inverseScale = inverseScaleBox.isSelected();
            currentIteration = 0;
        }
    }

    @Override
    public int getProgress() {
        return currentIteration;
    }

    @Override
    public String getState() {
        return status;
    }

    @Override
    public int getMaxProgress() {
        return maxIterations;
    }

    @Override
    public int getMinProgress() {
        return 0;
    }

    @Override
    public void setCancelled(boolean c) {
        cancelled = true;
        status = Monitorable.STATE_TERMINAL;
    }

    @Override
    public boolean isIndeterminate() {
        return false;
    }

    @Override
    public boolean isAbortable() {
        return true;
    }

    @Override
    public Throwable getUncaughtException() {
        return null;
    }
}
