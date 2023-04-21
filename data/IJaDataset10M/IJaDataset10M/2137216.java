package controller;

import java.util.Map;

/**
 * The Notification class encapsulates name, sender and an optional Map of arbitrary user info.
 * Classes that subscribe to a particular Notification may query the Notification
 * for its name. The sender instance variable allows interaction with the object
 * that issued this Notification. Additional data may optionally be delivered via
 * the userInfo Map.
 * @author nils
 *
 */
public class Notification {

    public static final String didFindEdgeIn = "did find edgeIn";

    public static final String didFinishMWT = "did finish MWT";

    public static final String didTriangulatePolygon = "did triangulate polygon";

    public static final String didTerminateTriangulator = "did terminate triangulator";

    public static final String willStartProcessing = "will start processing";

    public static final String didProcessXPercent = "did process x percent";

    public static final String didFindCircles = "did find circles";

    public String name;

    public Object sender;

    public Map<String, Object> userInfo;

    public Notification(String name, Object sender, Map<String, Object> userInfo) {
        this.name = name;
        this.sender = sender;
        this.userInfo = userInfo;
    }
}
