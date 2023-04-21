package org.spantus.chart.bean;

/**
 * 
 * 
 * 
 * @author Mindaugas Greibus
 *
 * @since 0.0.1
 * 
 * Created May 21, 2008
 *
 */
public class ChartInfo {

    private boolean playable;

    private boolean exportable;

    private boolean printable;

    private boolean selfZoomable = true;

    private Boolean grid = null;

    private String colorSchema = VectorSeriesColorEnum.blackWhite.name();

    public boolean isPlayable() {
        return playable;
    }

    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    public boolean isExportable() {
        return exportable;
    }

    public void setExportable(boolean exportable) {
        this.exportable = exportable;
    }

    public boolean isPrintable() {
        return printable;
    }

    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

    public boolean isSelfZoomable() {
        return selfZoomable;
    }

    public void setSelfZoomable(boolean selfZoomable) {
        this.selfZoomable = selfZoomable;
    }

    public Boolean getGrid() {
        return grid;
    }

    public void setGrid(Boolean grid) {
        this.grid = grid;
    }

    public String getColorSchema() {
        return colorSchema;
    }

    public void setColorSchema(String colorSchema) {
        this.colorSchema = colorSchema;
    }
}
