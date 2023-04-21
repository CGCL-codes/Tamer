package org.jactr.core.runtime.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author developer
 *
 */
public class DecoratedACTRRuntimeListener implements IACTRRuntimeListener {

    /**
   logger definition
   */
    private static final Log LOGGER = LogFactory.getLog(DecoratedACTRRuntimeListener.class);

    private IACTRRuntimeListener _listener;

    public DecoratedACTRRuntimeListener() {
        this(null);
    }

    public DecoratedACTRRuntimeListener(IACTRRuntimeListener listener) {
        _listener = listener;
    }

    /** 
   * @see org.jactr.core.runtime.event.IACTRRuntimeListener#modelAdded(org.jactr.core.runtime.event.ACTRRuntimeEvent)
   */
    public void modelAdded(ACTRRuntimeEvent event) {
        if (_listener != null) _listener.modelAdded(event);
    }

    /** 
   * @see org.jactr.core.runtime.event.IACTRRuntimeListener#modelRemoved(org.jactr.core.runtime.event.ACTRRuntimeEvent)
   */
    public void modelRemoved(ACTRRuntimeEvent event) {
        if (_listener != null) _listener.modelRemoved(event);
    }

    /** 
   * @see org.jactr.core.runtime.event.IACTRRuntimeListener#runtimeResumed(org.jactr.core.runtime.event.ACTRRuntimeEvent)
   */
    public void runtimeResumed(ACTRRuntimeEvent event) {
        if (_listener != null) _listener.runtimeResumed(event);
    }

    /** 
   * @see org.jactr.core.runtime.event.IACTRRuntimeListener#runtimeStarted(org.jactr.core.runtime.event.ACTRRuntimeEvent)
   */
    public void runtimeStarted(ACTRRuntimeEvent event) {
        if (_listener != null) _listener.runtimeStarted(event);
    }

    /** 
   * @see org.jactr.core.runtime.event.IACTRRuntimeListener#runtimeStopped(org.jactr.core.runtime.event.ACTRRuntimeEvent)
   */
    public void runtimeStopped(ACTRRuntimeEvent evnet) {
        if (_listener != null) _listener.runtimeStopped(evnet);
    }

    /** 
   * @see org.jactr.core.runtime.event.IACTRRuntimeListener#runtimeSuspended(org.jactr.core.runtime.event.ACTRRuntimeEvent)
   */
    public void runtimeSuspended(ACTRRuntimeEvent event) {
        if (_listener != null) _listener.runtimeSuspended(event);
    }
}
