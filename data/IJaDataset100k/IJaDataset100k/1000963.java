package net.sourceforge.nattable.listener;

import net.sourceforge.nattable.NatTable;

public class DefaultUpdateListener implements IUpdateListener {

    private NatTable natTable;

    public DefaultUpdateListener(NatTable natTable) {
        this.natTable = natTable;
    }

    public void updateRowAdded(int from, int to) {
        natTable.updateResize();
    }

    public void updateRowUpdated(int from, int to) {
        natTable.redrawUpdatedBodyRow(from, to);
    }

    public void updateRowRemoved(int from, int to) {
        natTable.updateResize();
    }
}
