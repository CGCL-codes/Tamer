package org.governerp.resourceEvent.xxx;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ResourceEventSourceRunner {

    private static final Log logger = LogFactory.getLog(ResourceEventSourceRunner.class);

    private int pauseMilliseconds = 500;

    private ResourceEventSource resourceEventSource;

    public ResourceEventSourceRunner(ResourceEventSource resourceEventSource) {
        this.resourceEventSource = resourceEventSource;
    }

    public void start(int pauseMilliseconds) {
        this.pauseMilliseconds = pauseMilliseconds;
        new Thread(new Runnable() {

            public void run() {
                doIt();
            }
        }).start();
    }

    private Map<String, ListnerWrapper> listnerMap = new ConcurrentHashMap<String, ListnerWrapper>();

    private void doIt() {
        try {
            while (true) {
                Thread.sleep(pauseMilliseconds);
                for (String key : this.listnerMap.keySet()) {
                    ListnerWrapper dl = this.listnerMap.get(key);
                    try {
                        if (dl.listner.getAckList() != null && !dl.ackListProcessingDone) {
                            this.resourceEventSource.processAcknowledgeList(dl.listner.getListnerId(), dl.listner.getAckList());
                            dl.ackListProcessingDone = true;
                        }
                        List<ResourceEvent> events = this.findResourceEvents(dl.listner.getListnerId());
                        if ((events != null) && (events.size() > 0)) {
                            dl.listner.onResourceEvent(events);
                            this.removeListner(dl.key);
                        }
                    } catch (Exception e) {
                        dl.listner.onException(e);
                        this.removeListner(dl.key);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Damn!", e);
            throw new RuntimeException(e);
        }
    }

    private class ListnerWrapper {

        private ResourceEventListner listner;

        private boolean ackListProcessingDone = false;

        private String key;
    }

    public String addListner(ResourceEventListner l) {
        ListnerWrapper w = new ListnerWrapper();
        w.listner = l;
        w.key = UUID.randomUUID().toString();
        this.listnerMap.put(w.key, w);
        return w.key;
    }

    public void removeListner(String key) {
        this.listnerMap.remove(key);
    }

    public abstract List<ResourceEvent> findResourceEvents(String listnerId);
}
