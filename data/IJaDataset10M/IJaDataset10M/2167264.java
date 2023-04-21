package viewer.geometry.list;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SwingHelper;
import viewer.geometry.action.ExportAction;
import viewer.geometry.action.InspectCollectionAction;
import viewer.tools.preview.GeometryPreview;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class PreviewMouseAdapter extends MouseAdapter {

    static final Logger logger = LoggerFactory.getLogger(PreviewMouseAdapter.class);

    private final GeomList geomList;

    /**
	 * Public constructor for adapter.
	 * 
	 * @param geomList
	 *            the list to react on.
	 */
    public PreviewMouseAdapter(GeomList geomList) {
        this.geomList = geomList;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        logger.debug("click");
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.getClickCount() == 2) {
                int index = geomList.locationToIndex(e.getPoint());
                Geometry geometry = geomList.getModel().getElementAt(index);
                GeometryPreview geometryPreview = new GeometryPreview();
                geometryPreview.showViewerWithFile(geomList, geometry, "Preview");
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            int index = geomList.locationToIndex(e.getPoint());
            showContext(index, e.getX(), e.getY());
        }
    }

    void showContext(int index, int x, int y) {
        JPopupMenu popup = new JPopupMenu();
        Geometry geometry = geomList.getModel().getElementAt(index);
        ExportAction exportAction = new ExportAction(geometry);
        JMenuItem itemExport = new JMenuItem(exportAction);
        popup.add(itemExport);
        if (geometry instanceof GeometryCollection) {
            JFrame frame = SwingHelper.getContainingFrame(geomList);
            InspectCollectionAction inspectAction = new InspectCollectionAction(frame, geometry);
            JMenuItem itemInspect = new JMenuItem(inspectAction);
            popup.add(itemInspect);
        }
        popup.show(geomList, x, y);
    }
}
