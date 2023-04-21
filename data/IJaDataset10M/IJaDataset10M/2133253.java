package net.bull.javamelody;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalToolTipUI;
import net.bull.javamelody.swing.table.MTable;

/**
 * Tableau de requêtes avec graphiques en toolTip
 * @author Emeric Vernat
 */
class CounterRequestTable extends MTable<CounterRequest> {

    private static final long serialVersionUID = 1L;

    private static final int IMAGE_HEIGHT = 50;

    private static final int IMAGE_WIDTH = 100;

    @SuppressWarnings("all")
    private final RemoteCollector remoteCollector;

    @SuppressWarnings("all")
    private final Map<String, CounterRequest> counterRequestByRequestName = new HashMap<String, CounterRequest>();

    @SuppressWarnings("all")
    private final Map<String, BufferedImage> requestChartByRequestName = new HashMap<String, BufferedImage>();

    private class ImageToolTip extends JToolTip {

        private static final long serialVersionUID = 1L;

        ImageToolTip() {
            super();
            setUI(new ImageToolTipUI());
        }
    }

    private class ImageToolTipUI extends MetalToolTipUI {

        ImageToolTipUI() {
            super();
            UIManager.put("ToolTip.background", UIManager.getColor("info"));
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            try {
                final String tipText = ((JToolTip) c).getTipText();
                final BufferedImage image = getRequestChartByRequestName(tipText);
                if (image != null) {
                    g.drawImage(image, 0, 0, c);
                } else {
                    super.paint(g, c);
                }
            } catch (final IOException e) {
                super.paint(g, c);
            }
        }

        @Override
        public Dimension getPreferredSize(JComponent c) {
            try {
                final String tipText = ((JToolTip) c).getTipText();
                final BufferedImage image = getRequestChartByRequestName(tipText);
                if (image != null) {
                    return new Dimension(image.getWidth(), image.getHeight());
                }
                return super.getPreferredSize(c);
            } catch (final IOException e) {
                return super.getPreferredSize(c);
            }
        }
    }

    CounterRequestTable(RemoteCollector remoteCollector) {
        super();
        this.remoteCollector = remoteCollector;
    }

    static boolean isRequestGraphDisplayed(Counter parentCounter) {
        return !(parentCounter.isErrorCounter() && !parentCounter.isJobCounter()) && !parentCounter.isJspOrStrutsCounter();
    }

    @Override
    public JToolTip createToolTip() {
        final ImageToolTip imageToolTip = new ImageToolTip();
        imageToolTip.setComponent(this);
        return imageToolTip;
    }

    @Override
    public void setList(List<CounterRequest> requests) {
        super.setList(requests);
        requestChartByRequestName.clear();
        counterRequestByRequestName.clear();
    }

    BufferedImage getRequestChartByRequestName(String requestName) throws IOException {
        BufferedImage requestChart;
        if (requestChartByRequestName.containsKey(requestName)) {
            requestChart = requestChartByRequestName.get(requestName);
        } else {
            requestChart = null;
            final CounterRequest counterRequest = getCounterRequestByRequestName(requestName);
            if (counterRequest != null) {
                final byte[] imageData = remoteCollector.collectJRobin(counterRequest.getId(), IMAGE_WIDTH, IMAGE_HEIGHT);
                if (imageData != null) {
                    requestChart = ImageIO.read(new ByteArrayInputStream(imageData));
                }
            }
            requestChartByRequestName.put(requestName, requestChart);
        }
        return requestChart;
    }

    private CounterRequest getCounterRequestByRequestName(String requestName) {
        if (counterRequestByRequestName.isEmpty()) {
            final List<CounterRequest> requests = getList();
            for (final CounterRequest request : requests) {
                counterRequestByRequestName.put(request.getName(), request);
            }
        }
        return counterRequestByRequestName.get(requestName);
    }
}
