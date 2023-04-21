package org.opencarto.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencarto.datamodel.OpenCartoFeature;
import org.opencarto.datamodel.Rep;
import org.opencarto.io.DataSource;
import org.opencarto.io.FileLoader;

/**
 * @author julien Gaffuri
 *
 */
public class ImageFileLoader extends FileLoader {

    private static Logger logger = Logger.getLogger(ImageFileLoader.class.getName());

    public ImageFileLoader(DataSource ds) {
        super(ds);
    }

    @Override
    public void loadData() {
        try {
            BufferedImage img;
            ImageFileDataSource ds = (ImageFileDataSource) this.getDataSource();
            URL url = new URL(ds.getUrl());
            if ("http".equals(url.getProtocol())) {
                img = ImageIO.read(url);
            } else if ("file".equals(url.getProtocol())) {
                img = ImageIO.read(new File(url.getPath()));
            } else {
                logger.warning("Unable to load data from " + ds.getUrl() + " - Unknown protocol:" + url.getProtocol());
                return;
            }
            Rep rep = new Image(img, ds.getLonMin(), ds.getLonMax(), ds.getLatMin(), ds.getLatMax());
            OpenCartoFeature ocf = new OpenCartoFeature(ds.getLayer(), rep, ds.getLayer().getViewer().getMinZoom(), ds.getLayer().getViewer().getMaxZoom());
            this.add(ocf);
        } catch (IOException e) {
            logger.warning("Could not open " + this.getDataSource().getUrl());
            e.printStackTrace();
        }
    }
}
