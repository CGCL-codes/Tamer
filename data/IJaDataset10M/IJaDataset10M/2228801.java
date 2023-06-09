package org.jactr.modules.pm.visual.six;

import java.util.concurrent.Future;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commonreality.identifier.IIdentifier;
import org.jactr.core.chunk.IChunk;
import org.jactr.core.concurrent.ModelCycleExecutor;
import org.jactr.core.logging.Logger;
import org.jactr.core.module.IllegalModuleStateException;
import org.jactr.core.production.request.ChunkTypeRequest;
import org.jactr.modules.pm.common.memory.IPerceptualEncoder;
import org.jactr.modules.pm.common.memory.PerceptualSearchResult;
import org.jactr.modules.pm.visual.AbstractVisualModule;
import org.jactr.modules.pm.visual.IVisualModule;
import org.jactr.modules.pm.visual.buffer.IVisualActivationBuffer;
import org.jactr.modules.pm.visual.buffer.IVisualLocationBuffer;
import org.jactr.modules.pm.visual.buffer.six.DefaultVisualActivationBuffer6;
import org.jactr.modules.pm.visual.buffer.six.DefaultVisualLocationBuffer6;
import org.jactr.modules.pm.visual.delegate.VisualEncodingDelegate;
import org.jactr.modules.pm.visual.delegate.VisualSearchDelegate;
import org.jactr.modules.pm.visual.event.VisualModuleEvent;
import org.jactr.modules.pm.visual.memory.IVisualMemory;
import org.jactr.modules.pm.visual.memory.impl.DefaultPerceptListener;
import org.jactr.modules.pm.visual.memory.impl.DefaultVisualMemory;

/**
 * DefaultVisualModule6 provides most of the visual functionality found within
 * the lisp equivalent. It provides a fully extensible system for adding new
 * feature maps (for visual searches), visual encoders (for converting percepts
 * into chunks), and filters (for search time prioritizing). The time it takes
 * to find or encode visual objects is also configurable.
 * 
 * @see http://jactr.org/node/137
 * @author harrison
 */
public class DefaultVisualModule6 extends AbstractVisualModule implements IVisualModule {

    /**
   * logger definition
   */
    public static final Log LOGGER = LogFactory.getLog(DefaultVisualModule6.class);

    protected IChunk _trackedVisualChunk;

    private VisualSearchDelegate _visualScanDelegate;

    private VisualEncodingDelegate _visualEncodingDelegate;

    private boolean _resetFINSTS = true;

    private DefaultPerceptListener _perceptListener;

    public DefaultVisualModule6() {
        super();
    }

    @Override
    public void dispose() {
        super.dispose();
        _trackedVisualChunk = null;
    }

    @Override
    protected void disconnectFromCommonReality() {
        super.disconnectFromCommonReality();
        _visualScanDelegate = null;
        _visualEncodingDelegate = null;
    }

    @Override
    protected void connectToCommonReality() {
        super.connectToCommonReality();
        _visualScanDelegate = new VisualSearchDelegate(this);
        _visualEncodingDelegate = new VisualEncodingDelegate(this);
    }

    @Override
    protected IVisualActivationBuffer createVisualActivationBuffer() {
        return new DefaultVisualActivationBuffer6(this);
    }

    @Override
    protected IVisualLocationBuffer createVisualLocationBuffer(IVisualActivationBuffer buffer) {
        return new DefaultVisualLocationBuffer6(buffer, this);
    }

    public void reset(boolean resetFINSTs) {
        if (resetFINSTs) getVisualMemory().getFINSTFeatureMap().reset();
        getVisualLocationBuffer().clear();
        getVisualActivationBuffer().clear();
        setTrackedVisualChunk(null);
        if (Logger.hasLoggers(getModel())) Logger.log(getModel(), Logger.Stream.VISUAL, "Reset visual");
        if (hasListeners()) dispatch(new VisualModuleEvent(this, VisualModuleEvent.Type.RESET));
    }

    public void reset() {
        reset(false);
    }

    /**
   * snag the create the visual chunk at the visual location - actual encoding
   * is taken care of when the chunk is removed from the visual buffer
   * 
   * @see org.jactr.modules.pm.visual.IVisualModule#encodeVisualChunkAt(org.jactr.core.chunk.IChunk)
   */
    public Future<IChunk> attendTo(PerceptualSearchResult result, double requestTime) {
        if (_visualEncodingDelegate == null) throw new IllegalModuleStateException("Cannot encode visual chunks until connected to common reality");
        return _visualEncodingDelegate.process(result != null ? result.getRequest() : null, requestTime, result);
    }

    public Future<PerceptualSearchResult> search(ChunkTypeRequest pattern, double requestTime, final boolean isStuffRequest) {
        if (_visualScanDelegate == null) throw new IllegalModuleStateException("Cannot scan visual field until connected to common reality");
        return _visualScanDelegate.process(pattern, requestTime, isStuffRequest);
    }

    /**
   * enable the tracking of this chunk. if visualChunk is null, it will turn off
   * tracking.
   * 
   * @see org.jactr.modules.pm.visual.IVisualModule#setTrackedVisualChunk(org.jactr.core.chunk.IChunk)
   */
    public void setTrackedVisualChunk(IChunk visualChunk) {
        if (visualChunk == _trackedVisualChunk) return;
        if (_trackedVisualChunk != null) {
            IIdentifier identifier = (IIdentifier) _trackedVisualChunk.getMetaData(IPerceptualEncoder.COMMONREALITY_IDENTIFIER_META_KEY);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Turning off tracking of " + _trackedVisualChunk + " aka " + identifier);
            _perceptListener.removeTrackedIdentifier(identifier);
            if (hasListeners()) dispatch(new VisualModuleEvent(this, VisualModuleEvent.Type.STOP_TRACKING, _trackedVisualChunk));
        }
        _trackedVisualChunk = visualChunk;
        if (visualChunk != null) {
            IIdentifier identifier = (IIdentifier) _trackedVisualChunk.getMetaData(IPerceptualEncoder.COMMONREALITY_IDENTIFIER_META_KEY);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Turning on tracking of " + _trackedVisualChunk + " aka " + identifier);
            if (identifier == null) {
                if (LOGGER.isWarnEnabled()) LOGGER.warn("common reality identifier for " + visualChunk + " could not be found, aborting tracking");
                getVisualActivationBuffer().addSourceChunk(getModel().getDeclarativeModule().getErrorChunk());
                _trackedVisualChunk = null;
            } else {
                _trackedVisualChunk = visualChunk;
                _perceptListener.addTrackedIdentifier(identifier);
                if (hasListeners()) dispatch(new VisualModuleEvent(this, VisualModuleEvent.Type.START_TRACKING, _trackedVisualChunk));
            }
        }
    }

    @Override
    protected IVisualMemory createVisualMemory() {
        IVisualMemory memory = new DefaultVisualMemory(this);
        _perceptListener = new DefaultPerceptListener(this);
        memory.addListener(_perceptListener, new ModelCycleExecutor(getModel(), ModelCycleExecutor.When.BEFORE));
        return memory;
    }
}
