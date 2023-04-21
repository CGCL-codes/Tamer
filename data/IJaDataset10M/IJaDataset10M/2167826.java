package org.jactr.core.module.retrieval.buffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.buffer.IActivationBuffer;
import org.jactr.core.chunktype.IChunkType;
import org.jactr.core.module.retrieval.IRetrievalModule;
import org.jactr.core.production.request.ChunkTypeRequest;
import org.jactr.core.production.request.IRequest;
import org.jactr.core.slot.ISlot;
import org.jactr.modules.pm.common.buffer.AbstractRequestDelegate;

/**
 * reset the visual system - merely calls IVisualModule.reset()
 * 
 * @author developer
 */
public class ClearRequestDelegate extends AbstractRequestDelegate {

    /**
   * logger definition
   */
    public static final Log LOGGER = LogFactory.getLog(ClearRequestDelegate.class);

    public ClearRequestDelegate(IChunkType clearChunkType) {
        super(clearChunkType);
        setUseBlockingTimedEvents(false);
    }

    @Override
    protected double computeCompletionTime(double startTime, IRequest request, IActivationBuffer buffer) {
        return startTime;
    }

    @Override
    protected boolean isValid(IRequest request, IActivationBuffer buffer) throws IllegalArgumentException {
        return true;
    }

    @Override
    protected Object startRequest(IRequest request, IActivationBuffer buffer, double requestTime) {
        return null;
    }

    @Override
    protected void finishRequest(IRequest request, IActivationBuffer buffer, Object startValue) {
        ((IRetrievalModule) buffer.getModule()).reset(isFullReset(request));
    }

    private boolean isFullReset(IRequest request) {
        ChunkTypeRequest ctr = (ChunkTypeRequest) request;
        for (ISlot slot : ctr.getSlots()) if (slot.getName().equalsIgnoreCase("all") && slot.getValue() instanceof Boolean && (Boolean) slot.getValue()) return true;
        return false;
    }
}
