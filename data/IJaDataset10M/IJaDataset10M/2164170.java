package netgest.bo.xwc.xeo.beans;

import netgest.bo.xwc.components.classic.GridPanel;
import netgest.bo.xwc.components.classic.GridRowRenderClass;
import netgest.bo.xwc.components.connectors.DataRecordConnector;
import netgest.bo.xwc.components.connectors.XEOObjectConnector;

public class XEOGridRowClassRenderer implements GridRowRenderClass {

    public String getRowClass(GridPanel grid, DataRecordConnector record) {
        if (record instanceof XEOObjectConnector) {
            try {
                return ((XEOObjectConnector) record).getXEOObject().userReadThis() ? "" : "gridRowUnRead";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }
}
