package org.spantus.core.io;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author Mindaugas Greibus
 * 
 * @since 0.2
 * 
 *        Created 2010.03.26
 *
 */
public abstract class AbstractSignalReader implements ProcessedFrameLinstener, SignalReader {

    Set<ProcessedFrameLinstener> listeners;

    public void registerProcessedFrameLinstener(ProcessedFrameLinstener linstener) {
        getListeners().add(linstener);
    }

    public Set<ProcessedFrameLinstener> getListeners() {
        if (listeners == null) {
            listeners = new LinkedHashSet<ProcessedFrameLinstener>();
        }
        return listeners;
    }

    public void processed(Long current, Long total) {
        for (ProcessedFrameLinstener linstener : getListeners()) {
            linstener.processed(current, total);
        }
    }

    public void started(Long total) {
        for (ProcessedFrameLinstener linstener : getListeners()) {
            linstener.started(total);
        }
    }

    public void ended() {
        for (ProcessedFrameLinstener linstener : getListeners()) {
            linstener.ended();
        }
    }
}
