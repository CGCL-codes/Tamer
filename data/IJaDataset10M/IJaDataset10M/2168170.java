package jw.bznetwork.client.ui;

import jw.bznetwork.client.BZNetwork;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that extracts the xml from the response to the specified request and
 * uses the root element as the widget element. The root element should
 * typically be something like a div.
 * 
 * @author Alexander Boyd
 * 
 */
public class ServerResponseWidget extends Widget {

    public ServerResponseWidget(JavaScriptObject xmlHttpRequest) {
        Document doc = BZNetwork.getResponseXml(xmlHttpRequest);
        if (doc == null) throw new IllegalArgumentException("An XML document could not be obtained from the request. " + "'This means that the request has not yet arrived, or " + "the response to the request was not valid xml or did " + "not have an xml mime type.");
        setElement(doc.getDocumentElement());
    }
}
