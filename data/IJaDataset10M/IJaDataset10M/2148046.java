package viewer.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 * @param <T>
 *            type argument
 */
public class ImageManagerMapsforge<T> extends AbstractImageManager<Tile> {

    static final Logger logger = LoggerFactory.getLogger(ImageManagerMapsforge.class);

    MemoryCache<Tile, BufferedImage> memoryCache = new MemoryCache<Tile, BufferedImage>(150);

    private ImageProviderMapsforge imageProvider;

    /**
	 * Create a new ImageManager that serves tiles from a mapsforge SwingRenderer.
	 * 
	 * @param databaseFile
	 *            the database to use.
	 * @param configFile
	 *            the style configuration to use.
	 */
    public ImageManagerMapsforge(String databaseFile, String configFile) {
        imageProvider = null;
        try {
            imageProvider = new ImageProviderMapsforge(databaseFile, configFile);
            imageProvider.addLoadListener(new LoadListenerMapsforge());
        } catch (ParserConfigurationException e) {
            logger.debug("unable to parse configuration");
        } catch (SAXException e) {
            logger.debug("unable to parse configuration");
        } catch (IOException e) {
            logger.debug("IO error while creating renderer");
        }
    }

    @Override
    public BufferedImage get(Tile thing) {
        BufferedImage image = memoryCache.get(thing);
        if (image != null) {
            return image;
        }
        if (imageProvider != null) {
            imageProvider.provide(thing);
        }
        return null;
    }

    private class LoadListenerMapsforge implements LoadListener<Tile, BufferedImage> {

        public LoadListenerMapsforge() {
        }

        @Override
        public void loaded(Tile thing, BufferedImage image) {
            memoryCache.put(thing, image);
            notifyListeners(thing, image);
        }

        @Override
        public void loadFailed(Tile thing) {
        }
    }

    @Override
    public void destroy() {
    }
}
