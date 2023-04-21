package es.gva.cit.gvsig.gazetteer.loaders;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.core.FPoint2D;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.core.v02.FConstant;
import com.iver.cit.gvsig.fmap.core.v02.FSymbol;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.GraphicLayer;
import com.iver.cit.gvsig.fmap.rendering.FGraphicLabel;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import es.gva.cit.gazetteer.querys.Feature;
import es.gva.cit.gazetteer.querys.GazetteerQuery;
import es.gva.cit.gvsig.gazetteer.DeleteSearchesExtension;

/**
 * This class is used to load a new feature like a layer in gvSIG
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class FeatureLoader {

    /**
	 * Coordinates Transformer
	 */
    private ICoordTrans coordTrans;

    /**
	 * @param projection
	 * Server projection
	 */
    public FeatureLoader(String sProjection) {
        IWindow window = PluginServices.getMDIManager().getActiveWindow();
        if (window instanceof BaseView) {
            BaseView activeView = (BaseView) PluginServices.getMDIManager().getActiveWindow();
            IProjection projection = CRSFactory.getCRS(sProjection);
            if (projection == null) {
                projection = activeView.getMapControl().getViewPort().getProjection();
            }
            coordTrans = projection.getCT(activeView.getMapControl().getViewPort().getProjection());
        }
    }

    /**
	 * It makes a zoom in gvSIG
	 * @param
	 * feature
	 * @param
	 * query
	 * Query that contains advanced options to search and
	 * to show the results
	 * @return
	 * true or false if fail
	 */
    public boolean load(Feature feature, GazetteerQuery query) {
        IWindow window = PluginServices.getMDIManager().getActiveWindow();
        if (window instanceof BaseView) {
            addAndDrawLabel(feature, query.getOptions().getAspect().isKeepOld(), query.getOptions().getAspect().isPaintCurrent());
            if (query.getOptions().getAspect().isGoTo()) {
                focusCenter(feature);
            }
        } else {
            return false;
        }
        return true;
    }

    /**
	 * This method focus the toponim in the center of the view 
	 * @param feature
	 * Feature that contains the coordinates
	 */
    private void focusCenter(Feature feature) {
        BaseView activeView = (BaseView) PluginServices.getMDIManager().getActiveWindow();
        IProjection projection = activeView.getProjection();
        ViewPort viewPort = activeView.getMapControl().getViewPort();
        Point2D point = getReprojectedPoint(feature.getCoordinates());
        Rectangle2D zoomExtent = null;
        if (viewPort.getAdjustedExtent() == null) {
        } else {
            Toolkit kit = Toolkit.getDefaultToolkit();
            double dpi = kit.getScreenResolution();
            Rectangle2D extent = projection.getExtent(viewPort.getAdjustedExtent(), new Double(25000).doubleValue(), new Double(viewPort.getImageWidth()).doubleValue(), new Double(viewPort.getImageHeight()).doubleValue(), MapContext.CHANGE[viewPort.getMapUnits()], MapContext.CHANGE[viewPort.getDistanceUnits()], dpi);
            if (extent != null) {
                zoomExtent = new Rectangle2D.Double(point.getX() - extent.getWidth() / 2, point.getY() - extent.getHeight() / 2, extent.getWidth(), extent.getHeight());
                activeView.getMapControl().getMapContext().zoomToExtent(zoomExtent);
            }
        }
    }

    /**
	 * It adds a new Label to the current view
	 * @param feature
	 * To obtain the coordinates and the toponim name
	 * @param isRemoveOldClicked
	 * To remove or keep the old searches
	 */
    private void addAndDrawLabel(Feature feature, boolean isRemoveOldClicked, boolean isMarkedPlaceClicked) {
        BaseView activeView = (BaseView) PluginServices.getMDIManager().getActiveWindow();
        GraphicLayer lyr = activeView.getMapControl().getMapContext().getGraphicsLayer();
        if (isRemoveOldClicked) {
            lyr.clearAllGraphics();
        }
        if (isMarkedPlaceClicked) {
            int idSymbol = lyr.addSymbol(getSymbol());
            IGeometry geom = ShapeFactory.createPoint2D(new FPoint2D(getReprojectedPoint(feature.getCoordinates())));
            FGraphicLabel theLabel = new FGraphicLabel(geom, idSymbol, feature.getName());
            lyr.addGraphic(theLabel);
            DeleteSearchesExtension.setVisible();
            PluginServices.getMainFrame().enableControls();
        }
        activeView.getMapControl().drawGraphics();
        activeView.getMapControl().getViewPort().setExtent(activeView.getMapControl().getViewPort().getExtent());
    }

    /**
	 * Creates a FSymbol
	 * @return
	 * FSymbol
	 */
    private FSymbol getSymbol() {
        FSymbol theSymbol = new FSymbol(FConstant.SYMBOL_TYPE_TEXT);
        theSymbol.setColor(Color.RED);
        theSymbol.setStyle(FConstant.SYMBOL_STYLE_MARKER_CIRCLE);
        theSymbol.setFontColor(Color.BLACK);
        theSymbol.setSizeInPixels(true);
        theSymbol.setSize(10);
        return theSymbol;
    }

    /**
	 * Reprojects the new point
	 * @param ptOrig
	 * Origin point
	 * @return
	 * FPoint2D
	 */
    private Point2D getReprojectedPoint(Point2D ptOrigin) {
        Point2D ptDest = null;
        return getCoordTrans().convert(ptOrigin, ptDest);
    }

    /**
	 * @return the coordTrans
	 */
    public ICoordTrans getCoordTrans() {
        return coordTrans;
    }
}
