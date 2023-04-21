package nacad.lemm.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement utility methods that may help on the simple and repetitve tasks
 * over the system
 * @author jonas dias
 */
public class Utility {

    public static String XML_PREFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /**
     * Fix a given URL adding the ending slash and the http:// if it is not
     * present on the given string.
     * @param url The URL to be fixed
     * @return the fixed URL
     */
    public static String fixURL(String url) {
        char b = url.charAt(url.length() - 1);
        if (b != '/') {
            url = url + "/";
        }
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }

    public static List<LemmWidget> getClusterWidgets() {
        List<LemmWidget> widgets = new ArrayList<LemmWidget>();
        LemmWidget w = new LemmWidget("Cluster Information", "/widgets/clusterInfo.zul");
        widgets.add(w);
        w = new LemmWidget("Cluster Load", "/widgets/clusterLoad.zul");
        widgets.add(w);
        w = new LemmWidget("Cluster Resources", "/widgets/clusterResources.zul");
        widgets.add(w);
        w = new LemmWidget("Cluster CPU Usage", "/widgets/cpuUsage.zul");
        widgets.add(w);
        return widgets;
    }
}
