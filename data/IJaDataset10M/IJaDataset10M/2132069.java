package jat.oppoc.op;

import jat.oppoc.ui.UIPanel;
import jat.oppoc.ui.UITools;
import java.awt.Dimension;
import java.awt.image.renderable.ParameterBlock;
import java.util.Enumeration;
import java.util.Vector;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * 
 * @author Jacek A. Teska
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Rescale extends Operation {

    private JCheckBox amplitudeRescale;

    private JSpinner spinnerB;

    private JSpinner spinnerG;

    private JSpinner spinnerR;

    private JSpinner offB;

    private JSpinner offG;

    private JSpinner offR;

    public Rescale() {
        super(UITools.getString("o60.Name"));
        min_in = Integer.parseInt(UITools.getString("o60.MinIn"));
        max_in = Integer.parseInt(UITools.getString("o60.MaxIn"));
        setIcon(UITools.getIcon(UITools.getString("o60.Icon")));
        init();
    }

    private void init() {
        panel = new UIPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMinimumSize(new Dimension(500, 160));
        panel.setBorder(new TitledBorder(new EtchedBorder(), UITools.getString("o.Panel")));
        amplitudeRescale = new JCheckBox(UITools.getString("o60.Amplitude"));
        panel.add(amplitudeRescale);
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.setBorder(new TitledBorder(new EtchedBorder(), UITools.getString("o60.Constants")));
        p1.add(Box.createHorizontalStrut(50));
        p1.add(new JLabel(UITools.getString("o60.ChannelR")));
        p1.add(Box.createHorizontalStrut(5));
        spinnerR = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(spinnerR);
        p1.add(Box.createHorizontalStrut(10));
        p1.add(new JLabel(UITools.getString("o60.ChannelG")));
        p1.add(Box.createHorizontalStrut(5));
        spinnerG = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(spinnerG);
        p1.add(Box.createHorizontalStrut(10));
        p1.add(new JLabel(UITools.getString("o60.ChannelB")));
        p1.add(Box.createHorizontalStrut(10));
        spinnerB = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(spinnerB);
        p1.add(Box.createHorizontalStrut(50));
        panel.add(p1);
        panel.add(Box.createVerticalStrut(5));
        p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.setBorder(new TitledBorder(new EtchedBorder(), UITools.getString("o60.Offsets")));
        p1.add(Box.createHorizontalStrut(50));
        p1.add(new JLabel(UITools.getString("o60.OffsetR")));
        p1.add(Box.createHorizontalStrut(5));
        offR = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(offR);
        p1.add(Box.createHorizontalStrut(10));
        p1.add(new JLabel(UITools.getString("o60.OffsetG")));
        p1.add(Box.createHorizontalStrut(5));
        offG = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(offG);
        p1.add(Box.createHorizontalStrut(10));
        p1.add(new JLabel(UITools.getString("o60.OffsetB")));
        p1.add(Box.createHorizontalStrut(5));
        offB = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(offB);
        p1.add(Box.createHorizontalStrut(50));
        panel.add(p1);
    }

    public void run(Vector in1, Vector in2, Vector out) {
        ParameterBlock pb;
        PlanarImage pi;
        double[] constants = new double[3];
        double[] off = new double[3];
        double[][] extrema;
        if (!amplitudeRescale.isSelected()) {
            constants[0] = ((SpinnerNumberModel) spinnerR.getModel()).getNumber().doubleValue();
            constants[1] = ((SpinnerNumberModel) spinnerG.getModel()).getNumber().doubleValue();
            constants[2] = ((SpinnerNumberModel) spinnerB.getModel()).getNumber().doubleValue();
            off[0] = ((SpinnerNumberModel) offR.getModel()).getNumber().doubleValue();
            off[1] = ((SpinnerNumberModel) offG.getModel()).getNumber().doubleValue();
            off[2] = ((SpinnerNumberModel) offB.getModel()).getNumber().doubleValue();
        }
        for (Enumeration e = in1.elements(); e.hasMoreElements(); ) {
            pb = new ParameterBlock();
            pi = (PlanarImage) e.nextElement();
            pb.addSource(pi);
            if (amplitudeRescale.isSelected()) {
                extrema = getExtrema(pi);
                pb.add(getConstants(extrema));
                pb.add(getOffsets(extrema));
            } else {
                pb.add(constants);
                pb.add(off);
            }
            out.add(run(pb));
        }
        for (Enumeration e = in2.elements(); e.hasMoreElements(); ) {
            pb = new ParameterBlock();
            pi = (PlanarImage) e.nextElement();
            pb.addSource(pi);
            if (amplitudeRescale.isSelected()) {
                extrema = getExtrema(pi);
                pb.add(getConstants(extrema));
                pb.add(getOffsets(extrema));
            } else {
                pb.add(constants);
                pb.add(off);
            }
            out.add(run(pb));
        }
    }

    public PlanarImage run(ParameterBlock pb) {
        return JAI.create("rescale", pb, null);
    }

    private double[] getConstants(double[][] extrema) {
        double[] constants = new double[3];
        for (int i = 0; i < constants.length; i++) constants[i] = 255D / (extrema[1][i] - extrema[0][i]);
        return constants;
    }

    private double[] getOffsets(double[][] extrema) {
        double[] offsets = new double[3];
        for (int i = 0; i < offsets.length; i++) offsets[i] = 255D * extrema[0][i] / (extrema[0][i] - extrema[1][i]);
        return offsets;
    }

    private double[][] getExtrema(PlanarImage pi) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(pi);
        RenderedOp op = JAI.create("extrema", pb, null);
        return (double[][]) op.getProperty("extrema");
    }
}
