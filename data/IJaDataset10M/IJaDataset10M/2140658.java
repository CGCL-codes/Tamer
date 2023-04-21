package net.sf.ghost4j.display;

import java.util.ArrayList;
import java.util.List;
import net.sf.ghost4j.GhostscriptException;

/**
 * Display callback in charge of extracting raw page rasters (PageRaster)
 * @author Gilles Grousset (gi.grousset@gmail.com)
 *
 */
public class PageRasterDisplayCallback implements DisplayCallback {

    private List<PageRaster> rasters;

    /**
	 * Constructor
	 */
    public PageRasterDisplayCallback() {
        rasters = new ArrayList<PageRaster>();
    }

    public void displayClose() throws GhostscriptException {
    }

    public void displayOpen() throws GhostscriptException {
    }

    public void displayPage(int width, int height, int raster, int format, int copies, int flush, byte[] imageData) throws GhostscriptException {
        PageRaster pageRaster = new PageRaster();
        pageRaster.setWidth(width);
        pageRaster.setHeight(height);
        pageRaster.setRaster(raster);
        pageRaster.setFormat(format);
        pageRaster.setData(imageData);
        rasters.add(pageRaster);
    }

    public void displayPreClose() throws GhostscriptException {
    }

    public void displayPreSize(int width, int height, int raster, int format) throws GhostscriptException {
    }

    public void displaySize(int width, int height, int raster, int format) throws GhostscriptException {
    }

    public void displaySync() throws GhostscriptException {
    }

    public void displayUpdate(int x, int y, int width, int height) throws GhostscriptException {
    }

    public List<PageRaster> getRasters() {
        return rasters;
    }
}
