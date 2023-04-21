package de.sambalmueslie.geocache_planer.map.elements;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import de.sambalmueslie.geocache_planer.common.Geocache;
import de.sambalmueslie.geocache_planer.common.img.GeocacheImages;

/**
 * Draw a {@link Geocache} on the map.
 * 
 * @author Sambalmueslie
 * 
 * @date 25.04.2009
 * 
 */
public class GeocacheMapElement extends MapElement {

    /**
	 * constructor.
	 * 
	 * @param cache
	 *            the geocache.
	 */
    public GeocacheMapElement(final Geocache cache) {
        geocache = cache;
        final double lat = cache.getLatitude();
        final double lon = cache.getLongitude();
        final GeoPosition coordinate = new GeoPosition(lat, lon);
        setPosition(coordinate);
    }

    /**
	 * paint the cache.
	 * 
	 * @param g
	 *            the {@link Graphics2D}
	 * @param map
	 *            the {@link JXMapViewers}
	 */
    @Override
    public void paintElement(final Graphics2D g, final JXMapViewer map) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final ImageIcon icon = GeocacheImages.getInstance().getImage(geocache.getType());
        final int iw = icon.getIconWidth();
        final int ih = icon.getIconHeight();
        g.drawImage(icon.getImage(), -(iw / 2), -(ih / 2), null);
    }

    /** the geocache . */
    private Geocache geocache = null;
}
