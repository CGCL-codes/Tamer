package com.mgensystems.jarindexer.listeners;

import java.util.ArrayList;
import java.util.List;
import com.mgensystems.eventmanager.EMEvent;
import com.mgensystems.eventmanager.EMListener;
import com.mgensystems.jarindexer.events.CacheAddedEvent;
import com.mgensystems.jarindexer.events.EntryIndexedEvent;
import com.mgensystems.jarindexer.events.FileIndexedFinishedEvent;
import com.mgensystems.jarindexer.events.FileIndexedStartedEvent;
import com.mgensystems.jarindexer.events.IndexFinishedEvent;
import com.mgensystems.jarindexer.events.IndexStartedEvent;
import com.mgensystems.jarindexer.events.SearchPathFinishedEvent;
import com.mgensystems.jarindexer.events.SearchPathStartedEvent;

public abstract class IndexListener extends EMListener {

    public IndexListener() {
        super();
    }

    public abstract void indexStarted(IndexStartedEvent event);

    public abstract void indexFinished(IndexFinishedEvent event);

    public abstract void searchPathFinished(SearchPathFinishedEvent event);

    public abstract void searchPathStarted(SearchPathStartedEvent event);

    public abstract void cacheAdded(CacheAddedEvent event);

    public abstract void fileIndexedStarted(FileIndexedStartedEvent event);

    public abstract void fileIndexedFinished(FileIndexedFinishedEvent event);

    public abstract void entryIndexed(EntryIndexedEvent event);

    public void indexStarted(EMEvent event) {
        indexStarted((IndexStartedEvent) event);
    }

    public void indexFinished(EMEvent event) {
        indexFinished((IndexFinishedEvent) event);
    }

    public void searchPathStarted(EMEvent event) {
        searchPathStarted((SearchPathStartedEvent) event);
    }

    public void searchPathFinished(EMEvent event) {
        searchPathFinished((SearchPathFinishedEvent) event);
    }

    public void cacheAdded(EMEvent event) {
        cacheAdded((CacheAddedEvent) event);
    }

    public void fileIndexedStarted(EMEvent event) {
        fileIndexedStarted((FileIndexedStartedEvent) event);
    }

    public void fileIndexedFinished(EMEvent event) {
        fileIndexedFinished((FileIndexedFinishedEvent) event);
    }

    public void entryIndexed(EMEvent event) {
        entryIndexed((EntryIndexedEvent) event);
    }
}
