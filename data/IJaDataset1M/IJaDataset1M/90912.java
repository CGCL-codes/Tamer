package themes;

import main.GradientPainter;
import java.awt.*;

/**
 * User: wallb
 */
public class Theme8 implements Theme {

    private String name = "Other";

    private Color rulerColor = GradientPainter.GRAY;

    private Color markerColor = Color.DARK_GRAY;

    private Color numberColor = Color.BLUE;

    private Color selectionBoxColor = Color.LIGHT_GRAY;

    private Color selectionBoxTextColor = Color.DARK_GRAY;

    private Color tickerColor = Color.BLACK;

    private boolean rulerColorIsGradient = true;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Color getRulerColor() {
        return rulerColor;
    }

    @Override
    public Color getMarkerColor() {
        return markerColor;
    }

    @Override
    public Color getNumberColor() {
        return numberColor;
    }

    @Override
    public void setRulerColor(Color rulerColor) {
        this.rulerColor = rulerColor;
    }

    @Override
    public void setMarkerColor(Color markerColor) {
        this.markerColor = markerColor;
    }

    @Override
    public void setNumberColor(Color numberColor) {
        this.numberColor = numberColor;
    }

    @Override
    public Color getSelectionBoxColor() {
        return selectionBoxColor;
    }

    @Override
    public void setSelectionBoxColor(Color selectionBoxColor) {
        this.selectionBoxColor = selectionBoxColor;
    }

    @Override
    public Color getSelectionBoxTextColor() {
        return selectionBoxTextColor;
    }

    @Override
    public void setSelectionBoxTextColor(Color selectionBoxTextColor) {
        this.selectionBoxTextColor = selectionBoxTextColor;
    }

    @Override
    public boolean rulerColorIsGradient() {
        return rulerColorIsGradient;
    }

    @Override
    public void setRulerColorIsGradient(boolean rulerColorIsGradient) {
        this.rulerColorIsGradient = rulerColorIsGradient;
    }

    public Color getTickerColor() {
        return tickerColor;
    }

    public void setTickerColor(Color tickerColor) {
        this.tickerColor = tickerColor;
    }

    @Override
    public String toString() {
        return name;
    }
}
