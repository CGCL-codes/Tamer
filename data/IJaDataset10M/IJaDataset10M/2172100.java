package maltcms.commands.distances.dtwng;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import maltcms.datastructures.array.ArrayFactory;
import maltcms.datastructures.array.IArrayD2Double;
import maltcms.datastructures.array.IFeatureVector;
import maltcms.tools.ArrayTools;
import org.apache.commons.configuration.Configuration;
import ucar.ma2.ArrayDouble;
import cross.Factory;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tools.FragmentTools;

/**
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 * 
 */
public class DTW implements IAlignment, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4958589994859792653L;

    private String leftHandSideId, rightHandSideId;

    private TwoFeatureVectorOperation similarity = new FeatureVectorDtwSimilarity();

    private List<Point> alignmentMap = Collections.emptyList();

    private Area area = null;

    private double defaultValue = Double.NEGATIVE_INFINITY;

    private IOptimizationFunction optimizationFunction = new ThreePredecessorsOptimization();

    @Override
    public Double apply(List<IFeatureVector> l1, List<IFeatureVector> l2) {
        final ArrayFactory f = Factory.getInstance().getObjectFactory().instantiate(ArrayFactory.class);
        final IArrayD2Double alignment = f.create(l1.size(), l2.size(), this.defaultValue, this.area);
        final IArrayD2Double pwvalues = f.createSharedLayout(alignment);
        optimizationFunction.init(l1, l2, alignment, pwvalues, this.similarity);
        double percentDone = 0;
        long elemCnt = 0;
        long elements = alignment.getNumberOfStoredElements();
        long partCnt = 0;
        int[] point = new int[] { 0, 0 };
        for (int i = 0; i < alignment.rows(); i++) {
            final int[] bounds = alignment.getColumnBounds(i);
            for (int j = bounds[0]; j < bounds[0] + bounds[1]; j++) {
                percentDone = ArrayTools.calcPercentDone(elements, elemCnt);
                partCnt = ArrayTools.printPercentDone(percentDone, 10, partCnt, System.out);
                elemCnt++;
                point[0] = i;
                point[1] = j;
                optimizationFunction.apply(point);
            }
        }
        percentDone = ArrayTools.calcPercentDone(elements, elemCnt);
        ArrayTools.printPercentDone(percentDone, 10, partCnt, System.out);
        this.alignmentMap = optimizationFunction.getTrace();
        System.out.println("Number of Points in trace: " + this.alignmentMap.size());
        List<double[]> l = new ArrayList<double[]>();
        for (int i = 0; i < this.alignmentMap.size() - 1; i++) {
            Point p = this.alignmentMap.get(i);
            l.add(new double[] { p.x, p.y, pwvalues.get(p.x, p.y) });
        }
        ContinuityArgmaxNodeBuilder canb10 = new ContinuityArgmaxNodeBuilder(10);
        List<Point> interp10 = canb10.eval(l, 3);
        ContinuityArgmaxNodeBuilder canb20 = new ContinuityArgmaxNodeBuilder(20);
        List<Point> interp20 = canb20.eval(l, 3);
        ContinuityArgmaxNodeBuilder canb100 = new ContinuityArgmaxNodeBuilder(100);
        List<Point> interp100 = canb100.eval(l, 3);
        BufferedImage bi = createImage(alignment);
        addAlignmentMap(bi, this.alignmentMap, Color.WHITE);
        addAlignmentMap(bi, interp10, Color.RED);
        addAlignmentMap(bi, interp20, Color.BLUE);
        addAlignmentMap(bi, interp100, Color.GREEN);
        saveImage(this.leftHandSideId + "-" + this.rightHandSideId + "_interpolatedLayoutWithTrace", bi);
        BufferedImage rp = createRecurrencePlot(pwvalues, 0.99);
        saveImage("recurrencePlot-" + this.leftHandSideId + "-" + this.rightHandSideId, rp);
        saveImage(this.leftHandSideId + "-" + this.rightHandSideId + "_layoutWithTrace", alignment, this.alignmentMap);
        return optimizationFunction.getOptimalValue();
    }

    private void saveImage(String name, IArrayD2Double alignment, List<Point> l, Color mapColor) {
        BufferedImage bi = createImage(alignment);
        addAlignmentMap(bi, l, mapColor);
        saveImage(name, bi);
    }

    private void saveImage(String name, BufferedImage bi) {
        try {
            ImageIO.write(bi, "PNG", new File(Factory.getInstance().getConfiguration().getString("output.basedir"), name + ".png"));
        } catch (IOException ex) {
            Logger.getLogger(DTW.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private BufferedImage createImage(IArrayD2Double alignment) {
        final ArrayFactory f = Factory.getInstance().getObjectFactory().instantiate(ArrayFactory.class);
        BufferedImage bi = f.createLayoutImage(alignment);
        return bi;
    }

    private BufferedImage createRecurrencePlot(IArrayD2Double pwd, double threshold) {
        BufferedImage bi = createImage(pwd);
        Graphics2D g2 = bi.createGraphics();
        Color hit = Color.BLACK;
        Color miss = Color.WHITE;
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                if (pwd.get(y, x) > threshold) {
                    g2.setColor(hit);
                    g2.fillRect(x, y, 1, 1);
                } else {
                    g2.setColor(miss);
                    g2.fillRect(x, y, 1, 1);
                }
            }
        }
        return bi;
    }

    private void addAlignmentMap(BufferedImage bi, List<Point> l, Color mapColor) {
        Color c = mapColor;
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2.setColor(c);
        Point last = null;
        for (Point p : l) {
            if (last == null) {
                last = p;
            } else {
                g2.fillRect(last.y, last.x, p.y - last.y, p.x - last.x);
                last = p;
            }
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        for (Point p : l) {
            gp.lineTo(p.getY(), p.getX());
        }
        g2.setColor(c);
        g2.draw(gp);
    }

    private void saveImage(String name, IArrayD2Double alignment, List<Point> l) {
        saveImage(name, alignment, l, Color.WHITE);
    }

    @Override
    public List<Point> getMap() {
        return this.alignmentMap;
    }

    @Override
    public String getLeftHandSideId() {
        return this.leftHandSideId;
    }

    @Override
    public String getRightHandSideId() {
        return this.rightHandSideId;
    }

    @Override
    public void setLeftHandSideId(String lhsid) {
        this.leftHandSideId = lhsid;
    }

    @Override
    public void setRightHandSideId(String rhsid) {
        this.rightHandSideId = rhsid;
    }

    @Override
    public void setPairwiseFeatureVectorOperation(TwoFeatureVectorOperation tfvo) {
        this.similarity = tfvo;
    }

    @Override
    public void setConstraints(Area a) {
        this.area = a;
    }

    @Override
    public void setDefaultValue(double d) {
        this.defaultValue = d;
    }

    @Override
    public void setOptimizationFunction(IOptimizationFunction iof) {
        this.optimizationFunction = iof;
    }

    @Override
    public IOptimizationFunction getOptimizationFunction() {
        return this.optimizationFunction;
    }

    @Override
    public TwoFeatureVectorOperation getPairwiseFeatureVectorOperation() {
        return this.similarity;
    }

    @Override
    public void modify(IFileFragment iff) {
        String arrayComparatorVariableName = Factory.getInstance().getConfiguration().getString("var.alignment.pairwise_distance.class", "pairwise_distance_class");
        String arrayDistanceClassName = Factory.getInstance().getConfiguration().getString("var.alignment.cumulative_distance.class", "cumulative_distance_class");
        String alignmentClassVariableName = Factory.getInstance().getConfiguration().getString("var.alignment.class", "alignment_class");
        FragmentTools.createString(iff, arrayComparatorVariableName, this.similarity.getClass().getName());
        FragmentTools.createString(iff, arrayDistanceClassName, this.optimizationFunction.getClass().getName());
        FragmentTools.createString(iff, alignmentClassVariableName, this.getClass().getName());
        ArrayDouble.D0 result = new ArrayDouble.D0();
        result.set(getOptimizationFunction().getOptimalValue());
        final String distvar = Factory.getInstance().getConfiguration().getString("var.alignment.distance", "distance");
        final IVariableFragment dvar = new VariableFragment(iff, distvar);
        dvar.setArray(result);
        this.optimizationFunction.modify(iff);
    }

    @Override
    public void configure(Configuration cfg) {
    }

    @Override
    public Area getConstraints() {
        return area;
    }

    @Override
    public double getDefaultValue() {
        return defaultValue;
    }
}
