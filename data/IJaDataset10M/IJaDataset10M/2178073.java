package net.jankenpoi.sudokuki.view;

import net.jankenpoi.sudokuki.controller.GridController;
import net.jankenpoi.sudokuki.model.GridChangedEvent;
import net.jankenpoi.sudokuki.model.GridModel;
import net.jankenpoi.sudokuki.model.GridModel.GridValidity;

public abstract class GridView implements GridListener {

    private GridController controller = null;

    private GridModel model = null;

    public GridView(GridModel model) {
        this.model = model;
    }

    public final GridController getController() {
        return controller;
    }

    public final void setController(GridController controller) {
        this.controller = controller;
    }

    @Override
    public abstract void gridChanged(GridChangedEvent event);

    public abstract void display();

    public abstract void close();

    public byte getValueAt(int li, int co) {
        return model.getValueAt(li, co);
    }

    public boolean isCellValueSet(int li, int co, Byte value) {
        return model.isCellValueSet(li, co, value);
    }

    public boolean isCellValueSet(int li, int co) {
        for (byte v = 1; v <= 9; v++) {
            if (isCellValueSet(li, co, v)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCellReadOnly(int li, int co) {
        return model.isCellReadOnly(li, co);
    }

    public boolean isCellMemoSet(int li, int co, byte memo) {
        return model.isCellMemoSet(li, co, memo);
    }

    public boolean isGrigComplete() {
        return model.isGridComplete();
    }

    public GridValidity getGridValidity() {
        return model.getGridValidity();
    }
}
