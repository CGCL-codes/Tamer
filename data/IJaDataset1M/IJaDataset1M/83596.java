package uit.upis.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import com.esri.aims.mtier.model.acetate.Point;
import com.esri.aims.mtier.model.envelope.Envelope;
import com.esri.aims.mtier.model.map.Map;
import uit.comm.util.MRUCache;
import uit.server.FeatureServiceIF;
import uit.server.ImageServiceIF;
import uit.server.MapController;
import uit.server.arcims.ArcIMSFeatureService;
import uit.server.model.LayerDef;
import uit.server.model.Model;
import uit.server.model.RequestMapObject;
import uit.server.model.ResponseMapObject;
import uit.server.util.RequestMapHelper;
import uit.server.util.ScaleUtil;
import uit.upis.manager.MapManager;
import uit.upis.model.Layer;

public class MapManagerImpl implements MapManager {

    protected final Logger log = Logger.getLogger(getClass());

    private MapController map;

    public FeatureServiceIF featureService;

    public ImageServiceIF imageService;

    private static MRUCache cache = new MRUCache();

    /**
	 * �ʱ�ȭ �� �����´�. 
	 * 
	 */
    public Model getInitMap(Model model) throws Exception {
        model.setSerivce("UPIS3");
        model = map.getImage(model);
        return model;
    }

    private Model cacheModel(Model model) throws Exception {
        Model result = null;
        result = (Model) cache.getObject("UPIS3");
        if (result == null) {
            result = map.getImage(model);
            result.setLastModified(System.currentTimeMillis());
            cache.putObject("UPIS3", result);
        } else {
            synchronized (cache) {
                cache.removeObject("UPIS3");
                cache.putObject("UPIS3", result);
            }
        }
        return result;
    }

    /**
	 * �⺻ upis3.xml���� ������ ���̹����� ��û�Ҷ� ����Ѵ�.  
	 * ���� �񱳷����� id�� �س����� 
	 * ���� LayerDef ��ü�� 
	 * id , name , visible���� üũ�����ϵ��� �Ұ�
	 * �� �̿��� ������ ���� �ݿ��Ұ���.
	 * @param removeLayerListId
	 * @return
	 * @throws Exception
	 */
    public Model getMap(Model model) throws Exception {
        List<Layer> exceptLayer = model.getTempLayer();
        model.setSerivce("UPIS3");
        if (exceptLayer != null) {
            List<LayerDef> convertExceptLayer = convertLayerList(exceptLayer);
            model.setExceptLayer(convertExceptLayer);
        }
        return map.getImage(model);
    }

    /**
	 * ȭ��� üũ�Ǿ� �ְ�, visiable�� üũ�Ǿ� �ִ� ���̾��Ʈ�� �����.
	 * @param visibleLayer
	 * @return
	 */
    private List<LayerDef> convertLayerList(List<Layer> visibleLayer) {
        List<LayerDef> covertVisibleLayerVOlist = new ArrayList<LayerDef>();
        for (Iterator iter = visibleLayer.iterator(); iter.hasNext(); ) {
            Layer layer = (Layer) iter.next();
            LayerDef layerDef = new LayerDef();
            layerDef.setLayerId(layer.getId() + "");
            layerDef.setLayerName(layer.getName());
            layerDef.setVisible(layer.getVisible().equals("F") ? "false" : "true");
            covertVisibleLayerVOlist.add(layerDef);
        }
        return covertVisibleLayerVOlist;
    }

    /**
	 * Refactroing ������.
	 */
    public String doMap(HttpServletRequest request) throws Exception {
        RequestMapHelper helper = new RequestMapHelper();
        RequestMapObject obj = helper.setRequestMapObject(request);
        ResponseMapObject mapObject = null;
        if (obj.getAction() == RequestMapObject.DO_ZOOM_FULL_EXTENT) {
            mapObject = doZoomToFullExtent(obj.getVisibleLayerList(), obj.getWidth(), obj.getHeight());
        } else if (obj.getAction() == RequestMapObject.DO_ZOOM_ENVEOPE) {
            mapObject = doZoom(obj.getVisibleLayerList(), obj.getWidth(), obj.getHeight(), obj.getZoomEnvelope());
        } else if (obj.getAction() == RequestMapObject.DO_REFRESH_ACTION) {
            mapObject = doRefresh(obj.getVisibleLayerList());
        } else if (obj.getAction() == RequestMapObject.DO_PAN_MOVE) {
            mapObject = doPan(obj.getVisibleLayerList(), obj.getWidth(), obj.getHeight(), obj.getDirection(), 3);
        } else if (obj.getAction() == RequestMapObject.DO_SCALE_FACTOR) {
            mapObject = doZoom(obj.getVisibleLayerList(), obj.getWidth(), obj.getHeight(), obj.getLevel());
        }
        return helper.generateMapObjToXML(request, mapObject);
    }

    /**
	 * ��ü ���� .
	 * @param visibleLayerList
	 * @param width
	 * @param height
	 */
    public ResponseMapObject doZoomToFullExtent(List visibleLayerList, long width, long height) {
        ResponseMapObject mapObject = null;
        try {
            imageService.initMapSize(width, height);
            mapObject = imageService.doZoomToFullExtent(visibleLayerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapObject;
    }

    /**
	 * ������ ����.
	 * �ϳ��� ������ ���� �� EXTENT�� ���� ��ô�� ���� ZOOM�� �����Ѵ�.
	 * @param visibleLayerList
	 * @param width
	 * @param height
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 * @return
	 */
    public ResponseMapObject doZoom(List visibleLayerList, long width, long height, double minx, double miny, double maxx, double maxy) {
        Envelope zoomEnvelope = new Envelope();
        zoomEnvelope.setMinX(minx);
        zoomEnvelope.setMinY(miny);
        zoomEnvelope.setMaxX(maxx);
        zoomEnvelope.setMaxY(maxy);
        return doZoom(visibleLayerList, width, height, zoomEnvelope);
    }

    /**
	 * ������ ����.
	 * �ϳ��� ������ ���� �� EXTENT�� ���� ��ô�� ���� ZOOM�� �����Ѵ�.
	 * @param visibleLayerList
	 * @param width
	 * @param height
	 * @param zoomEnvelope
	 * @return
	 */
    public ResponseMapObject doZoom(List visibleLayerList, long width, long height, Envelope zoomEnvelope) {
        ResponseMapObject mapObject = null;
        try {
            imageService.initMapSize(width, height);
            mapObject = imageService.doZoom(visibleLayerList, zoomEnvelope);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapObject;
    }

    /**
	 * �����ٿ��� �����Ϲٷ� ��ô�ܰ踦 �����Ѵ�. 
	 * @param visibleLayerList
	 * @param width
	 * @param height
	 * @param scaleFactor
	 * @return
	 */
    public ResponseMapObject doZoom(List visibleLayerList, long width, long height, long scaleFactor) {
        ResponseMapObject mapObject = null;
        try {
            imageService.initMapSize(width, height);
            mapObject = imageService.doZoom(visibleLayerList, scaleFactor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapObject;
    }

    /**
	 * ȭ���̵�.
	 * Direction : Map.NORTH , Map.NORTHEAST , Map.EAST ,Map.SOUTHEAST Map.SOUTH Map.SOUTHWEST
	 * Map.WEST Map.NORTHWEST
	 */
    public ResponseMapObject doPan(List visibleLayerList, long width, long height, long direction, long step) {
        ResponseMapObject mapObject = null;
        try {
            imageService.initMapSize(width, height);
            mapObject = imageService.doPan(visibleLayerList, direction, step);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapObject;
    }

    /**
	 * ����˻��� �Ҷ� ���.
	 * ��˻��������� ������������̺? ���Ǹ� ���� ������ ��� ���� ��ǥ���� �� 
	 * ������ ��û�Ѵ�. 	
	 */
    public String searchPoint(String whereExpression, List<LayerDef> visibleLayerList, long width, long height) {
        System.out.println("in method search point");
        ResponseMapObject obj = null;
        RequestMapHelper helper = new RequestMapHelper();
        try {
            Point point = featureService.searchPointQuery("242", whereExpression);
            imageService.initMapSize(width, height);
            obj = imageService.doZoom(visibleLayerList, point, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return helper.generateMapObjToXML(obj);
    }

    /**
	 * ����˻��� �Ҷ� ���.
	 * ��˻��������� ������������̺? ���Ǹ� ���� ������ ��� ���� ��ǥ���� �� 
	 * ������ ��û�Ѵ�. 
	 */
    public String searchPoint(String whereExpression, HttpServletRequest request) {
        System.out.println("in method search point");
        ResponseMapObject obj = null;
        RequestMapHelper helper = new RequestMapHelper();
        RequestMapObject req = helper.setRequestMapObject(request);
        try {
            Point point = featureService.searchPointQuery("242", whereExpression);
            imageService.initMapSize(req.getWidth(), req.getHeight());
            obj = imageService.doZoom(req.getVisibleLayerList(), point, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return helper.generateMapObjToXML(request, obj);
    }

    /**
	 * ���� ���� ���ΰ�ħ - ��ȭ���� ������ ��û�� ��.
	 */
    public ResponseMapObject doRefresh(List<LayerDef> visibleLayerList) {
        ResponseMapObject mapObject = null;
        try {
            mapObject = imageService.doRefresh(visibleLayerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapObject;
    }

    /**
	 * ��˻� ��ġ ã�� 
	 * @param layerId
	 * @param whereExpression
	 * @param visibleLayerList
	 * @param width
	 * @param height
	 * @return
	 */
    public String search(String layerId, String whereExpression, List<LayerDef> visibleLayerList, long width, long height) {
        ResponseMapObject mapObject = null;
        RequestMapHelper helper = new RequestMapHelper();
        try {
            imageService.initMapSize(width, height);
            mapObject = featureService.searchShapeQuery(layerId, whereExpression);
            mapObject = imageService.doZoomSelection(visibleLayerList, 12, mapObject.getShape(), mapObject.getEnvelope());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return helper.generateMapObjToXML(mapObject);
    }

    /**
	 * ��˻� ��ġ ã�� 
	 * @param layerId
	 * @param whereExpression
	 * @param visibleLayerList
	 * @param width
	 * @param height
	 * @return
	 */
    public String search(String layerId, String whereExpression, HttpServletRequest request) {
        ResponseMapObject mapObject = null;
        RequestMapHelper helper = new RequestMapHelper();
        RequestMapObject req = helper.setRequestMapObject(request);
        try {
            imageService.initMapSize(req.getWidth(), req.getHeight());
            mapObject = featureService.searchShapeQuery(layerId, whereExpression);
            mapObject = imageService.doZoomSelection(req.getVisibleLayerList(), 12, mapObject.getShape(), mapObject.getEnvelope());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return helper.generateMapObjToXML(request, mapObject);
    }

    /**
	 * ��˻���� xml ��
	 * @param whereExpression
	 * @return
	 */
    public Document searchResultXML(String layerId, String whereExpression) {
        ResponseMapObject mapObject = null;
        Document doc = null;
        try {
            mapObject = featureService.searchShapeQuery(layerId, whereExpression);
            doc = featureService.featureQueryXML(mapObject.getShape());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
	 * ��˻���� List  ���� 
	 * @param whereExpression
	 * @return
	 */
    public List<List<HashMap<String, String>>> searchResultList(String layerId, String whereExpression) {
        ResponseMapObject mapObject = null;
        List<List<HashMap<String, String>>> list = null;
        try {
            mapObject = featureService.searchShapeQuery(layerId, whereExpression);
            list = featureService.featureQueryList(mapObject.getShape());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
	 * ���� ��ġ ã�� 
	 */
    public String searchPacel(String whereExpression, List<LayerDef> visibleLayerList, long width, long height) {
        return search("12", whereExpression, visibleLayerList, width, height);
    }

    /**
	 * ���� ��ġ ã�� 
	 */
    public String searchPacel(String whereExpression, HttpServletRequest request) {
        return search("12", whereExpression, request);
    }

    /**
	 * ���� ��˻���� xml ���� 
	 * @param whereExpression
	 * @return
	 */
    public Document searchPacelResultXML(String whereExpression) {
        return searchResultXML("12", whereExpression);
    }

    /**
	 * ���� ��˻���� List  ���� 
	 * @param whereExpression
	 * @return
	 */
    public List<List<HashMap<String, String>>> searchPacelResultList(String whereExpression) {
        ResponseMapObject mapObject = null;
        List<List<HashMap<String, String>>> list = null;
        try {
            mapObject = featureService.searchShapeQuery("12", whereExpression);
            list = featureService.featureQueryList(mapObject.getShape(), 67, 96);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
	 * ���� ��˻���� List  ���� 
	 * @param whereExpression
	 * @return
	 */
    public List<List<HashMap<String, String>>> searchPacelResultList(String whereExpression, int targetLayer) {
        ResponseMapObject mapObject = null;
        List<List<HashMap<String, String>>> list = null;
        try {
            mapObject = featureService.searchShapeQuery("12", whereExpression);
            list = featureService.featureQueryList(mapObject.getShape(), targetLayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
	 * ���� ��˻���� List  ���� 
	 * @param whereExpression
	 * @return
	 */
    public List<List<HashMap<String, String>>> searchPacelResultList(String whereExpression, int fromLayer, int toLayer) {
        ResponseMapObject mapObject = null;
        List<List<HashMap<String, String>>> list = null;
        try {
            mapObject = featureService.searchShapeQuery("12", whereExpression);
            list = featureService.featureQueryList(mapObject.getShape(), fromLayer, toLayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
	 * identify
	 */
    public void identify(double x, double y) {
        System.out.println("in method search point");
        Point point = new Point();
        point.setX(x);
        point.setY(y);
        System.out.println(point.getX() + " , " + point.getY());
        Envelope pntEnvelope = new Envelope();
        double maxX = point.getX() + 0.1;
        double maxY = point.getY() + 0.1;
        double minX = point.getX() - 0.1;
        double minY = point.getY() - 0.1;
        pntEnvelope.setMinX(minX);
        pntEnvelope.setMinY(minY);
        pntEnvelope.setMaxX(maxX);
        pntEnvelope.setMaxY(maxY);
        try {
            System.out.println(x + " , " + y);
            featureService.featureQueryList(pntEnvelope, 242);
            featureService.featureQueryList(pntEnvelope, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMapController(MapController mapController) {
        this.map = mapController;
    }

    public void setFeatureService(FeatureServiceIF featureService) {
        this.featureService = featureService;
    }

    public void setImageService(ImageServiceIF imageService) {
        this.imageService = imageService;
    }
}
