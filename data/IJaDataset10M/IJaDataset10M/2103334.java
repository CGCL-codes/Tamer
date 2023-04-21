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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
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
public class BoxFilter extends Operation {

    private JCheckBox cb_default;

    private JSpinner spinnerWidth;

    private JSpinner spinnerHeight;

    private JSpinner spinnerX;

    private JSpinner spinnerY;

    public BoxFilter() {
        super(UITools.getString("o43.Name"));
        min_in = Integer.parseInt(UITools.getString("o43.MinIn"));
        max_in = Integer.parseInt(UITools.getString("o43.MaxIn"));
        setIcon(UITools.getIcon(UITools.getString("o43.Icon")));
        init();
    }

    private void init() {
        panel = new UIPanel();
        panel.setMinimumSize(new Dimension(500, 60));
        panel.setBorder(new TitledBorder(new EtchedBorder(), UITools.getString("o.Panel")));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        cb_default = new JCheckBox(UITools.getString("o43.Default"));
        panel.add(cb_default);
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel(UITools.getString("o43.Width")));
        spinnerWidth = new JSpinner(new SpinnerNumberModel(3, 0, 1024, 5));
        panel.add(spinnerWidth);
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel(UITools.getString("o43.Height")));
        spinnerHeight = new JSpinner(new SpinnerNumberModel(3, 0, 1024, 5));
        panel.add(spinnerHeight);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(new JLabel(UITools.getString("o43.X")));
        spinnerX = new JSpinner(new SpinnerNumberModel(1, 0, 1024, 5));
        panel.add(spinnerX);
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel(UITools.getString("o43.Y")));
        spinnerY = new JSpinner(new SpinnerNumberModel(1, 0, 1024, 5));
        panel.add(spinnerY);
    }

    public void run(Vector in1, Vector in2, Vector out) {
        ParameterBlock pb;
        int width = 0;
        int height = 0;
        int x = 0;
        int y = 0;
        PlanarImage pi;
        if (!cb_default.isSelected()) {
            width = ((SpinnerNumberModel) spinnerWidth.getModel()).getNumber().intValue();
            height = ((SpinnerNumberModel) spinnerHeight.getModel()).getNumber().intValue();
            x = ((SpinnerNumberModel) spinnerX.getModel()).getNumber().intValue();
            y = ((SpinnerNumberModel) spinnerY.getModel()).getNumber().intValue();
        }
        for (Enumeration e = in1.elements(); e.hasMoreElements(); ) {
            pb = new ParameterBlock();
            pi = (PlanarImage) e.nextElement();
            pb.addSource(pi);
            if (cb_default.isSelected()) {
                pb.add(pi.getWidth());
                pb.add(pi.getHeight());
            } else {
                pb.add(width);
                pb.add(height);
                pb.add(x);
                pb.add(y);
            }
            out.add(run(pb));
        }
        for (Enumeration e = in2.elements(); e.hasMoreElements(); ) {
            pb = new ParameterBlock();
            pi = (PlanarImage) e.nextElement();
            pb.addSource(pi);
            if (cb_default.isSelected()) {
                pb.add(pi.getWidth());
                pb.add(pi.getHeight());
            } else {
                pb.add(width);
                pb.add(height);
                pb.add(x);
                pb.add(y);
            }
            out.add(run(pb));
        }
    }

    public PlanarImage run(ParameterBlock pb) {
        return JAI.create("boxfilter", pb, null);
    }
}
