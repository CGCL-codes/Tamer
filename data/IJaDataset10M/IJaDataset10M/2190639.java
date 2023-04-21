package org.gstreamer.lowlevel;

import org.gstreamer.Structure;
import org.gstreamer.interfaces.Navigation;
import com.sun.jna.Library;

public interface GstNavigationAPI extends Library {

    GstNavigationAPI GSTNAVIGATION_API = GstNative.load("gstinterfaces", GstNavigationAPI.class);

    GType gst_navigation_get_type();

    void gst_navigation_send_event(Navigation navigation, Structure structure);

    void gst_navigation_send_key_event(Navigation navigation, String event, String key);

    void gst_navigation_send_mouse_event(Navigation navigation, String event, int button, double x, double y);
}
