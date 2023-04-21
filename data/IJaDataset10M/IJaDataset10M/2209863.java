package org.jactr.modules.pm.visual.memory.impl.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commonreality.modalities.visual.Color;
import org.jactr.core.chunk.IChunk;
import org.jactr.core.chunk.ISymbolicChunk;
import org.jactr.core.chunktype.IChunkType;
import org.jactr.core.chunktype.event.ChunkTypeEvent;
import org.jactr.core.chunktype.event.IChunkTypeListener;
import org.jactr.core.concurrent.ExecutorServices;
import org.jactr.core.event.IParameterEvent;
import org.jactr.core.model.IModel;
import org.jactr.core.slot.BasicSlot;
import org.jactr.modules.pm.visual.IVisualModule;

public class ColorChunkCache implements IChunkTypeListener {

    /**
   * logger definition
   */
    public static final Log LOGGER = LogFactory.getLog(ColorChunkCache.class);

    private IModel _model;

    Map<Color, IChunk> _colorToChunk;

    Map<IChunk, Color> _chunkToColor;

    IChunkType _colorChunkType;

    public ColorChunkCache(IModel model) {
        _model = model;
        _colorToChunk = new HashMap<Color, IChunk>();
        _chunkToColor = new HashMap<IChunk, Color>();
    }

    protected IChunkType getColorChunkType() {
        if (_colorChunkType == null) initialize();
        return _colorChunkType;
    }

    public void clear() {
        _chunkToColor.clear();
        _colorToChunk.clear();
    }

    public void dispose() {
        clear();
        _model = null;
        _colorToChunk = null;
        _chunkToColor = null;
        _colorChunkType = null;
    }

    protected final void initialize() {
        try {
            _colorChunkType = _model.getDeclarativeModule().getChunkType(IVisualModule.COLOR_CHUNK_TYPE).get();
            _colorChunkType.addListener(this, ExecutorServices.INLINE_EXECUTOR);
            Collection<IChunk> chunks = _colorChunkType.getSymbolicChunkType().getChunks();
            for (IChunk color : chunks) update(color);
        } catch (Exception e) {
            LOGGER.debug("Could not access color chunktype", e);
        }
    }

    protected void update(IChunk colorChunk) {
        ISymbolicChunk sc = colorChunk.getSymbolicChunk();
        int red = ((Number) sc.getSlot("red").getValue()).intValue();
        int green = ((Number) sc.getSlot("green").getValue()).intValue();
        int blue = ((Number) sc.getSlot("blue").getValue()).intValue();
        Color awtColor = new Color(red, green, blue);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Creating new color " + colorChunk + " " + awtColor);
        _colorToChunk.put(awtColor, colorChunk);
        _chunkToColor.put(colorChunk, awtColor);
    }

    public void chunkAdded(IChunk chunk) {
        if (!chunk.hasBeenDisposed() && chunk.isA(getColorChunkType())) update(chunk);
    }

    public synchronized IChunk getColorChunk(Color awtColor) {
        IChunkType colorType = getColorChunkType();
        IChunk chunk = _colorToChunk.get(awtColor);
        if (chunk == null) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Couldn't find a color chunk for " + awtColor + " creating");
            try {
                chunk = _model.getDeclarativeModule().createChunk(colorType, null).get();
                ISymbolicChunk sc = chunk.getSymbolicChunk();
                sc.addSlot(new BasicSlot("red", awtColor.getRed()));
                sc.addSlot(new BasicSlot("green", awtColor.getGreen()));
                sc.addSlot(new BasicSlot("blue", awtColor.getBlue()));
                chunk = _model.getDeclarativeModule().addChunk(chunk).get();
                _colorToChunk.put(awtColor, chunk);
                _chunkToColor.put(chunk, awtColor);
            } catch (Exception e) {
                LOGGER.error("Could not create color chunk for " + awtColor, e);
            }
        }
        return chunk;
    }

    public synchronized Color getColor(IChunk chunk) {
        return _chunkToColor.get(chunk);
    }

    public void childAdded(ChunkTypeEvent cte) {
    }

    public void chunkAdded(ChunkTypeEvent cte) {
        chunkAdded(cte.getChunk());
    }

    public void chunkTypeEncoded(ChunkTypeEvent cte) {
    }

    public void slotAdded(ChunkTypeEvent cte) {
    }

    public void slotChanged(ChunkTypeEvent cte) {
    }

    public void slotRemoved(ChunkTypeEvent cte) {
    }

    public void parameterChanged(IParameterEvent pe) {
    }
}
