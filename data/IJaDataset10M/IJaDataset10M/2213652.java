package com.google.api.data.calendar.v2.model;

import com.google.api.client.util.Key;
import com.google.api.data.gdata.v2.model.batch.BatchFeed;
import com.google.api.data.gdata.v2.model.batch.BatchOperation;
import java.util.List;

/**
 * Base FreeBusy feed.
 * 
 * @author Alain Vongsouvanh (alainv@google.com)
 */
public class FreeBusyList extends BatchFeed {

    @Key("batch:operation")
    public BatchOperation batchOperation;

    @Key("entry")
    public List<FreeBusy> entries;
}
