package org.minerva.tracks;

import java.util.ArrayList;
import java.util.List;
import org.minerva.tracks.datasource.TrackDataSource;
import org.minerva.tracks.renderer.RenderContext;
import org.minerva.tracks.renderer.Renderer;

public class Track {

    private static final long serialVersionUID = 1L;

    TrackPanel trackPanel;

    double x1 = 0.0;

    double x2 = 1.0;

    int widthPx = 100;

    List<Renderer> rendererList = new ArrayList<Renderer>();

    TrackDataSource dataSource;

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public void render(RenderContext renderContext) {
        for (Renderer renderer : rendererList) {
            renderer.render(renderContext, this);
        }
    }

    public List<Renderer> getRendererList() {
        return rendererList;
    }

    public TrackDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(TrackDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TrackPanel getPanel() {
        return trackPanel;
    }

    public void setPanel(TrackPanel trackPanel) {
        this.trackPanel = trackPanel;
    }

    /**
     * Track width, in pixels, of the non- X-axis.
     */
    public int getWidthPx() {
        return widthPx;
    }

    public void setWidthPx(int widthPx) {
        this.widthPx = widthPx;
    }
}
