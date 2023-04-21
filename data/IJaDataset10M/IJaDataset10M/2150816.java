package org.timepedia.chronoscope.java2d;

import org.timepedia.chronoscope.client.Chart;
import org.timepedia.chronoscope.client.Dataset;
import org.timepedia.chronoscope.client.Datasets;
import org.timepedia.chronoscope.client.XYPlot;
import org.timepedia.chronoscope.client.data.mock.MockDatasetFactory;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Ray Cromwell &lt;ray@timepedia.org&gt;
 */
public class ChronoscopeImageUtil {

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setSize(800, 400);
        Dataset dff = new MockDatasetFactory(ServerChronoscope.get().getComponentFactory().getDatasetFactory()).getBasicDataset();
        Dataset ds[] = new Dataset[1];
        ds[0] = dff;
        double domainOrigin = 0;
        double currentDomain = 0;
        Image im = renderChronoscopeImage(ds, 400, 300, null, true, domainOrigin, currentDomain, false);
        JLabel jl = new JLabel(new ImageIcon(im));
        jf.getContentPane().add(jl);
        jf.show();
    }

    public static BufferedImage renderChronoscopeImage(Dataset[] ds, int width, int height, String css, boolean interactive, double domainOrigin, double currentDomain, boolean az) {
        StaticImageChartPanel sicp = ServerChronoscope.get().createChart(new Datasets(ds), width, height, null);
        Chart c = sicp.getChart();
        XYPlot xyPlot = c.getPlot();
        xyPlot.getRangeAxis(0).setAutoZoomVisibleRange(az);
        xyPlot.getDomain().setEndpoints(domainOrigin, currentDomain);
        c.redraw();
        BufferedImage bimg = (BufferedImage) sicp.getImage();
        return bimg;
    }
}
