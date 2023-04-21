package eu.digmap.thumbnails;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class HeatMapThumbnailMaker extends AbstractThumbnailMaker {

    private HeatThumbnailParams params = null;

    public HeatThumbnailParams getParams() {
        return params;
    }

    public void setParams(HeatThumbnailParams params) {
        super.setParams(params);
        this.params = params;
    }

    protected BufferedImage getImage() throws IOException {
        HeatMapPanel panel = new HeatMapPanel(params.data, HeatMapPanel.GRADIENT_WHITE_TO_RED);
        panel.setCoordinateBounds(0, 360, 0, 180);
        panel.setSize(params.width, params.height);
        panel.setVisible(true);
        panel.setOpaque(false);
        return panel.bufferedImage;
    }
}
