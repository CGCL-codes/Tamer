package org.gstreamer.lowlevel;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import org.gstreamer.URIType;
import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.annotations.CallerOwnsReturn;

/**
 * The URIHandler is an interface that is implemented by Source and Sink GstElement to simplify then handling of URI.
 * An application can use the following functions to quickly get an element that handles the given URI for reading or 
 * writing (gst_element_make_from_uri()).
 * Source and Sink plugins should implement this interface when possible.
 */
public interface GstURIAPI extends Library {

    GstURIAPI GSTURI_API = GstNative.load(GstURIAPI.class);

    boolean gst_uri_protocol_is_valid(String protocol);

    boolean gst_uri_protocol_is_supported(URIType type, String protocol);

    boolean gst_uri_is_valid(String uri);

    boolean gst_uri_has_protocol(String uri, String protocol);

    @CallerOwnsReturn
    Pointer gst_element_make_from_uri(URIType type, String uri, String name);
}
