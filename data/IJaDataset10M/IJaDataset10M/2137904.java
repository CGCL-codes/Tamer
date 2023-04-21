package org.opennms.dashboard.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
class GMapsView extends HTML {

    String xmlData;

    GMapsView(Dashlet dashlet) {
        super("<div></div>");
    }

    public void setXMLData(String data) {
        xmlData = data;
        setGMapPoints(xmlData);
    }

    public native String setGMapPoints(String xmlData);
}
