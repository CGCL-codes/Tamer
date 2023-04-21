package worldwind.kml.model;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: tgleason
 * Date: Sep 3, 2008
 * Time: 9:30:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class KMLStyle {

    Map<String, Object> iconStyle = new HashMap<String, Object>();

    Map<String, Object> lineStyle = new HashMap<String, Object>();

    Map<String, Object> polyStyle = new HashMap<String, Object>();

    Map<String, Object> bubbleStyle = new HashMap<String, Object>();

    public void setIconStyle(String name, Object val) {
        iconStyle.put(name, val);
    }

    public Object getIconStyle(String name) {
        return iconStyle.get(name);
    }

    public void setLineStyle(String name, Object val) {
        lineStyle.put(name, val);
    }

    public Object getLineStyle(String name) {
        return lineStyle.get(name);
    }

    public void setPolyStyle(String name, Object val) {
        polyStyle.put(name, val);
    }

    public Object getPolyStyle(String name) {
        return polyStyle.get(name);
    }

    public void setBubbleStyle(String name, Object val) {
        bubbleStyle.put(name, val);
    }

    public Object getBubbleStyle(String name) {
        return bubbleStyle.get(name);
    }
}
