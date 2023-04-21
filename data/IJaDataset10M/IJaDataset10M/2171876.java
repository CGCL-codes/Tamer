package fca.gui.context.table.model;

import fca.core.context.binary.BinaryContext;
import fca.exception.InvalidTypeException;
import fca.exception.LMLogger;

public class BinaryContextTableModel extends ContextTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 759468877481132094L;

    /**
	 * Constructeur
	 */
    public BinaryContextTableModel(BinaryContext bc) {
        super(bc);
        setMoveRowAllowed(true);
    }

    @Override
    public void setValueAt(Object value, int rowIdx, int colIdx) {
        super.setValueAt(value, rowIdx, colIdx);
        try {
            if (((String) value).equals("X")) context.setValueAt(BinaryContext.TRUE, rowIdx, colIdx); else context.setValueAt(BinaryContext.FALSE, rowIdx, colIdx);
        } catch (InvalidTypeException e) {
            LMLogger.logWarning(e, false);
        }
    }

    @Override
    public Object getValueAt(int rowIdx, int colIdx) {
        if (context.getValueAt(rowIdx, colIdx) == BinaryContext.TRUE) return "X"; else return "";
    }

    @Override
    public boolean isCellEditable(int rowIdx, int colIdx) {
        return false;
    }
}
