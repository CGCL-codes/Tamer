package de.haumacher.timecollect.report.ods.model.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import de.haumacher.timecollect.report.ods.model.Coordinate;
import de.haumacher.timecollect.report.ods.model.Table;

public class CoordinateImpl implements Coordinate {

    private final Table owner;

    private final CoordinateImpl superCoordinate;

    private final List<CoordinateImpl> subCoordinates = new ArrayList<CoordinateImpl>();

    private int index;

    CoordinateImpl(Table owner, CoordinateImpl superCoordinate) {
        this.owner = owner;
        this.superCoordinate = superCoordinate;
    }

    @Override
    public Table getOwner() {
        return owner;
    }

    @Override
    public Coordinate getSuperCoordinate() {
        return superCoordinate;
    }

    @Override
    public List<Coordinate> getSubCoordinates() {
        return Collections.<Coordinate>unmodifiableList(subCoordinates);
    }

    @Override
    public Coordinate addSubCoordinate(Coordinate before) {
        return CoordinateImpl.insertCoordinate(owner, this, subCoordinates, (CoordinateImpl) before);
    }

    static Coordinate insertCoordinate(Table table, CoordinateImpl parent, List<CoordinateImpl> coordinates, CoordinateImpl before) {
        if (before == null) {
            return CoordinateImpl.insertCoordinate(table, parent, coordinates, coordinates.size());
        } else {
            if (before.getSuperCoordinate() != parent) {
                throw new IllegalArgumentException("Not a sub-coordinate of this coordinate.");
            }
            return CoordinateImpl.insertCoordinate(table, parent, coordinates, before.index);
        }
    }

    static CoordinateImpl insertCoordinate(Table table, CoordinateImpl parent, List<CoordinateImpl> coordinates, int insertIndex) {
        CoordinateImpl result = new CoordinateImpl(table, parent);
        coordinates.add(insertIndex, result);
        for (int n = coordinates.size() - 1; n >= insertIndex; n--) {
            coordinates.get(n).index = n;
        }
        return result;
    }
}
