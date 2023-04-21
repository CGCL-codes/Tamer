package com.lambelly.lambnes.gui;

import javax.swing.SwingWorker;
import org.apache.log4j.*;
import com.lambelly.lambnes.platform.ppu.NesTileCache;

public class PatternTableWorker extends SwingWorker<Void, Void> {

    private Logger logger = Logger.getLogger(PatternTableWorker.class);

    private PatternTableVisualization patternTableVisualization = null;

    private NesTileCache tileCache;

    public PatternTableWorker(PatternTableVisualization ptv) {
        this.setPatternTableVisualization(ptv);
    }

    @Override
    protected Void doInBackground() throws Exception {
        for (int x = 0; x < this.getPatternTableVisualization().getPatternTableIcons().length; x++) {
            if (this.getPatternTableVisualization().getPatternTableVisualizationType() == PatternTableVisualization.PATTERN_TABLE_VISUALIZATION_SPRITE) {
                this.getPatternTableVisualization().getPatternTableIcon(x).setIcon(this.getTileCache().getSpriteTile(x).getBufferedImage());
            } else {
                this.getPatternTableVisualization().getPatternTableIcon(x).setIcon(this.getTileCache().getBackgroundTile(x).getBufferedImage());
            }
        }
        return null;
    }

    @Override
    protected void done() {
    }

    public PatternTableVisualization getPatternTableVisualization() {
        return patternTableVisualization;
    }

    public void setPatternTableVisualization(PatternTableVisualization patternTableVisualization) {
        this.patternTableVisualization = patternTableVisualization;
    }

    public NesTileCache getTileCache() {
        return tileCache;
    }

    public void setTileCache(NesTileCache tileCache) {
        this.tileCache = tileCache;
    }
}
