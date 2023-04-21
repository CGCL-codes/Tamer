package org.weasis.core.api.image.op;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.DataBuffer;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.weasis.core.api.gui.util.JMVUtils;

public class EnhancementOp extends JPanel {

    public static final String[] histoLabels = { "None", "Manual Enhancement", "Automatic Levels", "Equalized Levels", "Background subtraction" };

    private Contrast contrast;

    private JPanel jPanel1 = new JPanel();

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JSlider jSlider1 = new JSlider();

    private TitledBorder title1 = new TitledBorder("Luminosity offset : 0");

    private JButton jButtonArtificial = new JButton();

    private EnhancementOp() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        jPanel1.setLayout(gridBagLayout1);
        jSlider1.setMinimum(0);
        jSlider1.setMaximum(255);
        jSlider1.setValue(127);
        jSlider1.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                updateValues();
            }
        });
        jSlider1.setBorder(title1);
        JMVUtils.setPreferredWidth(jSlider1, 80);
        jPanel1.add(jSlider1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        jPanel1.add(jButtonArtificial, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 5, 0), 0, 0));
    }

    private void updateValues() {
        title1.setTitle("Luminosity offset : " + jSlider1.getValue() + " ");
        jSlider1.repaint();
    }

    private Contrast getContrastPanel() {
        if (contrast == null) {
            contrast = new Contrast();
        }
        return contrast;
    }

    public String getHelpEntry() {
        return "Enhancement";
    }

    public static PlanarImage backgroundSubstraction(PlanarImage image, PlanarImage background, int offset) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.addSource(background);
        pb.add(offset);
        return JAI.create("BackgroundSubstract", pb, null);
    }

    public static int[] getHistogram(PlanarImage image) {
        int[] bins = { 256 };
        double[] low = { 0.0D };
        double[] high = { 256.0D };
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(1);
        pb.add(1);
        pb.add(bins);
        pb.add(low);
        pb.add(high);
        RenderedOp op = JAI.create("histogram", pb, null);
        Histogram histogram = (Histogram) op.getProperty("histogram");
        int[] local_array = new int[histogram.getNumBins(0)];
        for (int i = 0; i < histogram.getNumBins(0); i++) {
            local_array[i] = histogram.getBinSize(0, i);
        }
        return local_array;
    }

    public PlanarImage equalize(PlanarImage image) {
        int sum = 0;
        byte[] cumulative = new byte[256];
        int array[] = getHistogram(image);
        float scale = 255.0F / (image.getWidth() * image.getHeight());
        for (int i = 0; i < 256; i++) {
            sum += array[i];
            cumulative[i] = (byte) ((sum * scale) + .5F);
        }
        LookupTableJAI lookup = new LookupTableJAI(cumulative);
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(lookup);
        return JAI.create("lookup", pb, null);
    }

    public static PlanarImage normalizeAllTypeOfImage(PlanarImage image) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        PlanarImage dst = JAI.create("extrema", pb, null);
        double[][] extrema = (double[][]) dst.getProperty("extrema");
        int numBands = dst.getSampleModel().getNumBands();
        double[] slopes = new double[numBands];
        double[] y_ints = new double[numBands];
        for (int i = 0; i < numBands; i++) {
            double range = extrema[1][i] - extrema[0][i];
            if (range < 1.0) {
                range = 1.0;
            }
            slopes[i] = 255.0D / range;
            y_ints[i] = 255.0D - slopes[i] * extrema[1][i];
        }
        pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(slopes);
        pb.add(y_ints);
        dst = JAI.create("rescale", pb, null);
        pb = new ParameterBlock();
        pb.addSource(dst);
        pb.add(DataBuffer.TYPE_BYTE);
        return JAI.create("format", pb, null);
    }

    public PlanarImage piecewise(PlanarImage image) {
        float[][][] bp = new float[1][2][];
        bp[0][0] = new float[] { 0.0F, 32.0F, 64.0F, 255.0F };
        bp[0][1] = new float[] { 0.0F, 128.0F, 112.0F, 255.0F };
        return JAI.create("piecewise", image, bp);
    }

    private PlanarImage updateManualEnhancement(PlanarImage image) {
        return getContrastPanel().updateSlider(image);
    }
}
