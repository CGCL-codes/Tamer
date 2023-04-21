package com.memoire.dt;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

public final class DtDropDisabler extends DtDropAbstract {

    private static final DtDropDisabler SINGLETON = new DtDropDisabler();

    private static final TransferHandler HANDLER = new TransferHandler() {

        protected final Transferable createTransferable(JComponent _c) {
            return null;
        }

        public final int getSourceActions(JComponent _c) {
            return DtLib.NONE;
        }

        public boolean canImport(JComponent _c, DataFlavor[] _flavors) {
            return false;
        }

        public boolean importData(JComponent _c, Transferable _t) {
            return false;
        }
    };

    private DtDropDisabler() {
    }

    public boolean checkDropContext(int _a, Component _c, DataFlavor[] _f, Point _p) {
        return false;
    }

    public Object getData(int _a, Component _c, Transferable _data, Point _p) {
        return null;
    }

    public boolean dropData(int _a, Component _c, Object _data, Point _p) {
        return false;
    }

    public static final void install(Component _c) {
        if (_c instanceof JComponent) ((JComponent) _c).setTransferHandler(HANDLER);
        new DropTarget(_c, DtLib.ALL, SINGLETON);
    }
}
