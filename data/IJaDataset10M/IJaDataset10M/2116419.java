package org.isi.monet.core.model;

import java.util.Date;

public class LogBookNode extends LogBook {

    public LogBookNode() {
        super();
    }

    public LogEntry[] getEntries(String idNode, Integer iStartPos, Integer iLimit) {
        DataRequest oDataRequest = new DataRequest();
        oDataRequest.setCode(idNode);
        oDataRequest.setStartPos(iStartPos);
        oDataRequest.setLimit(iLimit);
        return this.getDataLink().requestLogBookNodeItems(oDataRequest).get().values().toArray(new LogEntry[0]);
    }

    public LogEntry[] getEntries(Integer iStartPos, Integer iLimit) {
        return this.getEntries(null, iStartPos, iLimit);
    }

    public Integer getEntriesCount(String idNode) {
        return this.getDataLink().requestLogBookNodeItemsCount(idNode);
    }

    public Integer getEntriesCount() {
        return this.getEntriesCount(null);
    }

    public LogEntryList search(Integer iEventType, Date dtFrom, Date dtTo) {
        return this.getDataLink().searchLogBookNodeItems(iEventType, dtFrom, dtTo);
    }
}
