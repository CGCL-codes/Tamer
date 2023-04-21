package org.mati.geotech.model.cellcover;

public interface CellCoverListener {

    public void gridSizeChanged(int n, int m);

    public void gridPositionChanged(double x, double y, double cw, double ch, int n, int m);

    public void levelChanged(int newLvl, int prevLvl);
}
