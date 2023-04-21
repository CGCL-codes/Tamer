package co.edu.unal.ungrid.services.client.applet.bimler.view;

import javax.swing.JFrame;
import co.edu.unal.ungrid.image.RgbPlane;
import co.edu.unal.ungrid.image.util.JointHistogram;

public class JointHistogramFrame extends JFrame {

    public static final long serialVersionUID = 200609220000001L;

    public JointHistogramFrame(final String sTitle) {
        super(sTitle);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public void setHistogram(final JointHistogram<RgbPlane> jh) {
        JointHistogramView hv = new JointHistogramView(jh);
        add(hv);
    }
}
