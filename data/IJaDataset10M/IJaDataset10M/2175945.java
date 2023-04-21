package nhap.ai.pathfinding.simple;

import java.awt.Color;
import nhap.rep.ILocation;

public class DijkstraLevelValueVisitorAction<T extends DijkstraField> extends GraphicsLevelVisitorAction<T> {

    final int maxValue;

    public DijkstraLevelValueVisitorAction(final Region region, final int maxValue) {
        super(region);
        this.maxValue = maxValue;
    }

    @Override
    public void execute(T dijkstraField) {
        final int value = dijkstraField.getValue();
        if (value == -999999) {
            graphics2D.setColor(Color.black);
        } else {
            graphics2D.setColor(new Color(0, 0, getColorValue(value, maxValue)));
        }
        final ILocation location = dijkstraField.getLocation();
        graphics2D.fillRect(10 * (location.getColNr() - region.getFirstColNr()), 10 * (location.getRowNr() - region.getFirstRowNr()), 10, 10);
    }
}
