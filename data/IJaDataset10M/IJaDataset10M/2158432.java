package com.jchapman.jempire.gui.gamepanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractListModel;
import com.jchapman.jempire.units.Unit;

/**
 *
 * @author Jeff Chapman
 * @version 1.0
 */
public class UnitsToMoveListModel extends AbstractListModel {

    private final List unitsToMove = new ArrayList();

    public UnitsToMoveListModel() {
        super();
    }

    Unit removeFirstUnit() {
        Unit oldUnit = null;
        synchronized (unitsToMove) {
            oldUnit = (Unit) unitsToMove.remove(0);
        }
        fireIntervalRemoved(this, 0, 0);
        return oldUnit;
    }

    void setUnitsToMove(Unit[] units) {
        int endIndex = 0;
        synchronized (unitsToMove) {
            unitsToMove.clear();
            if (units != null) {
                unitsToMove.addAll(Arrays.asList(units));
                endIndex = unitsToMove.size();
            }
        }
        fireContentsChanged(this, 0, endIndex);
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at <code>index</code>
     */
    public Object getElementAt(int index) {
        Object unit = null;
        synchronized (unitsToMove) {
            unit = unitsToMove.get(index);
        }
        return unit;
    }

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    public int getSize() {
        int unitCnt = 0;
        synchronized (unitsToMove) {
            unitCnt = unitsToMove.size();
        }
        return unitCnt;
    }
}
