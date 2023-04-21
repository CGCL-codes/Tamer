package jat.oppoc.op;

import jat.oppoc.ui.UIPanel;
import jat.oppoc.ui.UITools;
import java.awt.Dimension;
import java.awt.image.renderable.ParameterBlock;
import java.util.Enumeration;
import java.util.Vector;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
public class Threshold extends Operation {

    private JSpinner spinnerB;

    private JSpinner spinnerG;

    private JSpinner spinnerR;

    private JSpinner lowB;

    private JSpinner lowG;

    private JSpinner lowR;

    private JSpinner highB;

    private JSpinner highG;

    private JSpinner highR;

    public Threshold() {
        super(UITools.getString("o61.Name"));
        min_in = Integer.parseInt(UITools.getString("o61.MinIn"));
        max_in = Integer.parseInt(UITools.getString("o61.MaxIn"));
        setIcon(UITools.getIcon(UITools.getString("o61.Icon")));
        init();
    }

    private void init() {
        panel = new UIPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMinimumSize(new Dimension(500, 200));
        panel.setBorder(new TitledBorder(new EtchedBorder(), UITools.getString("o.Panel")));
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.setBorder(new TitledBorder(new EtchedBorder(), UITools.getString("o61.Low")));
        p1.add(Box.createHorizontalStrut(50));
        p1.add(new JLabel(UITools.getString("o61.LowR")));
        p1.add(Box.createHorizontalStrut(5));
        lowR = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(lowR);
        p1.add(Box.createHorizontalStrut(10));
        p1.add(new JLabel(UITools.getString("o61.LowG")));
        p1.add(Box.createHorizontalStrut(5));
        lowG = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(lowG);
        p1.add(Box.createHorizontalStrut(10));
        p1.add(new JLabel(UITools.getString("o61.LowB")));
        p1.add(Box.createHorizontalStrut(10));
        lowB = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(lowB);
        p1.add(Box.createHorizontalStrut(50));
        panel.add(p1);
        panel.add(Box.createVerticalStrut(5));
        p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.setBorder(new TitledBorder(new EtchedBorder(), UITools.getString("o61.High")));
        p1.add(Box.createHorizontalStrut(50));
        p1.add(new JLabel(UITools.getString("o61.HighR")));
        p1.add(Box.createHorizontalStrut(5));
        highR = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(highR);
        p1.add(Box.createHorizontalStrut(10));
        p1.add(new JLabel(UITools.getString("o61.HighG")));
        p1.add(Box.createHorizontalStrut(5));
        highG = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(highG);
        p1.add(Box.createHorizontalStrut(10));
        p1.add(new JLabel(UITools.getString("o61.HighB")));
        p1.add(Box.createHorizontalStrut(5));
        highB = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(highB);
        p1.add(Box.createHorizontalStrut(50));
        panel.add(p1);
        p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.setBorder(new TitledBorder(new EtchedBorder(), UITools.getString("o61.Constants")));
        p1.add(Box.createHorizontalStrut(50));
        p1.add(new JLabel(UITools.getString("o61.ChannelR")));
        p1.add(Box.createHorizontalStrut(5));
        spinnerR = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(spinnerR);
        p1.add(Box.createHorizontalStrut(10));
        p1.add(new JLabel(UITools.getString("o61.ChannelG")));
        p1.add(Box.createHorizontalStrut(5));
        spinnerG = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(spinnerG);
        p1.add(Box.createHorizontalStrut(10));
        p1.add(new JLabel(UITools.getString("o61.ChannelB")));
        p1.add(Box.createHorizontalStrut(5));
        spinnerB = new JSpinner(new SpinnerNumberModel(0D, 0D, 255D, 2.5D));
        p1.add(spinnerB);
        p1.add(Box.createHorizontalStrut(50));
        panel.add(p1);
    }

    public void run(Vector in1, Vector in2, Vector out) {
        ParameterBlock pb;
        double[] d = new double[3];
        d[0] = ((SpinnerNumberModel) spinnerR.getModel()).getNumber().doubleValue();
        d[1] = ((SpinnerNumberModel) spinnerG.getModel()).getNumber().doubleValue();
        d[2] = ((SpinnerNumberModel) spinnerB.getModel()).getNumber().doubleValue();
        double[] low = new double[3];
        low[0] = ((SpinnerNumberModel) lowR.getModel()).getNumber().doubleValue();
        low[1] = ((SpinnerNumberModel) lowG.getModel()).getNumber().doubleValue();
        low[2] = ((SpinnerNumberModel) lowB.getModel()).getNumber().doubleValue();
        double[] high = new double[3];
        high[0] = ((SpinnerNumberModel) highR.getModel()).getNumber().doubleValue();
        high[1] = ((SpinnerNumberModel) highG.getModel()).getNumber().doubleValue();
        high[2] = ((SpinnerNumberModel) highB.getModel()).getNumber().doubleValue();
        for (Enumeration e = in1.elements(); e.hasMoreElements(); ) {
            pb = new ParameterBlock();
            pb.addSource((PlanarImage) e.nextElement());
            pb.add(low);
            pb.add(high);
            pb.add(d);
            out.add(run(pb));
        }
        for (Enumeration e = in2.elements(); e.hasMoreElements(); ) {
            pb = new ParameterBlock();
            pb.addSource((PlanarImage) e.nextElement());
            pb.add(low);
            pb.add(high);
            pb.add(d);
            out.add(run(pb));
        }
    }

    public PlanarImage run(ParameterBlock pb) {
        return JAI.create("threshold", pb, null);
    }
}
