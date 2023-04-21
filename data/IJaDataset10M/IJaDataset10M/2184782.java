package viewer.geometry;

import geometry.speedup.index.GeometryTesselationMap;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viewer.Searcher;
import viewer.core.MapWindow;
import viewer.geometry.manage.GeometryContainer;
import viewer.geometry.manage.StyledGeometry;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class GeometryMouseAdapter extends MouseAdapter {

    static final Logger logger = LoggerFactory.getLogger(GeometryMouseAdapter.class);

    private final Searcher searcher;

    private GeometryTesselationMap<GeometryContainer> map;

    /**
	 * @param searcher
	 *            the seasrcher.
	 * @param sg
	 *            the set of geometries to monitor.
	 */
    public GeometryMouseAdapter(Searcher searcher, StyledGeometry sg) {
        this.searcher = searcher;
        map = new GeometryTesselationMap<GeometryContainer>();
        if (sg == null) {
            return;
        }
        Collection<GeometryContainer> geometries = sg.getGeometries();
        for (GeometryContainer gc : geometries) {
            map.add(gc.getGeometry().getGeometry(), gc);
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (!searcher.isShowGeometryInfo()) {
            return;
        }
        logger.debug("checking mouse click for layer " + this);
        MapWindow mapWindow = searcher.getViewer().getMapWindow();
        double lon = mapWindow.getPositionLon(event.getX());
        double lat = mapWindow.getPositionLat(event.getY());
        Point point = new GeometryFactory().createPoint(new Coordinate(lon, lat));
        Set<GeometryContainer> intersect = map.test(point);
        List<GeometryContainer> intersectionContainers = new ArrayList<GeometryContainer>();
        for (GeometryContainer gc : intersect) {
            System.out.println("intersection with: " + gc.getSource().getInfo());
            intersectionContainers.add(gc);
        }
        if (intersectionContainers.size() > 0) {
            showInfo(intersectionContainers);
        }
    }

    private void showInfo(List<GeometryContainer> intersectionContainers) {
        String newLine = System.getProperty("line.separator");
        StringBuilder strb = new StringBuilder();
        Iterator<GeometryContainer> iterator = intersectionContainers.iterator();
        while (iterator.hasNext()) {
            GeometryContainer gc = iterator.next();
            strb.append(gc.getSource().getInfo());
            if (iterator.hasNext()) {
                strb.append(newLine);
            }
        }
        String message = strb.toString();
        JOptionPane.showMessageDialog(searcher, message, "Interesection", JOptionPane.INFORMATION_MESSAGE);
    }
}
