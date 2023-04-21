package PlateArrayer.undospread;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import javax.swing.JTable;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import PlateArrayer.Converter;
import PlateArrayer.ModelPlate;
import PlateArrayer.ModelSourceSorted;
import PlateArrayer.SeqReaction;

/**
* The Auto Arrange Edit class record the changes occured to the
* spreadsheet after performing an auto arrange action.
*
* @author Peter
*/
public class ArrangeEdit extends AbstractUndoableEdit {

    private ModelPlate modelDest;

    private ModelSourceSorted modelSource;

    private Object arrayTemp[][];

    private Object arrayPaste[][];

    private boolean auto;

    private String presentationName;

    private boolean validResult = true;

    public ArrangeEdit(JTable tableSource, JTable tableDest, boolean auto) {
        modelSource = (ModelSourceSorted) tableSource.getModel();
        modelDest = (ModelPlate) tableDest.getModel();
        this.auto = auto;
        presentationName = (auto) ? "Auto Arrange" : "Manual Arrange";
        int numCols = modelDest.getColumnCount(), numRows = modelDest.getRowCount();
        arrayPaste = new Object[numRows][numCols];
        arrayTemp = new Object[numRows][numCols];
        if (!auto) {
            HashSet hs = new HashSet(tableSource.getRowCount());
            for (int i = 0; i < tableSource.getRowCount(); i++) {
                SeqReaction sr = modelSource.getSeqReaction(i);
                String posPlate = (String) sr.getPlatePosManual();
                if (posPlate.equals("empty") || hs.contains(posPlate) || !isValidWellPosition(posPlate)) {
                    validResult = false;
                    return;
                }
                hs.add(posPlate);
            }
        }
        for (int i = 0; i < tableSource.getRowCount(); i++) {
            SeqReaction sr = modelSource.getSeqReaction(i);
            String posPlate = (String) ((auto) ? sr.getInitPosition() : sr.getPlatePosManual());
            int r = Converter.getRow(posPlate), c = Converter.getColumn(posPlate);
            sr.setPositioned(Boolean.TRUE);
            arrayTemp[r][c] = modelDest.getValueAt(r, c + 1);
            arrayPaste[r][c] = sr;
            modelDest.setValueAt(sr, r, c + 1);
            modelSource.makeUnavailable(sr, r, c + 1);
        }
    }

    /**
	 * @param posPlate
	 * @return
	 */
    private boolean isValidWellPosition(String posPlate) {
        if (posPlate.length() == 3 || posPlate.length() == 2) {
            char row = posPlate.charAt(0);
            int col = Integer.valueOf(posPlate.substring(1)).intValue();
            return (row >= 'A' && row <= 'H') && (col >= 1 && col <= 12);
        } else return false;
    }

    public void undo() throws CannotUndoException {
        for (int i = 0; i < modelDest.getRowCount(); i++) {
            for (int j = 0; j < modelDest.getColumnCount() - 1; j++) {
                modelDest.setValueAt(arrayTemp[i][j], i, j + 1);
                modelSource.makeAvailable(arrayPaste[i][j]);
                modelSource.makeUnavailable(arrayTemp[i][j], i, j + 1);
            }
        }
    }

    public void redo() throws CannotRedoException {
        for (int i = 0; i < modelDest.getRowCount(); i++) {
            for (int j = 0; j < modelDest.getColumnCount() - 1; j++) {
                modelDest.setValueAt(arrayPaste[i][j], i, j + 1);
                modelSource.makeUnavailable(arrayPaste[i][j], i, j + 1);
            }
        }
    }

    public boolean canUndo() {
        return true;
    }

    public boolean canRedo() {
        return true;
    }

    public String getPresentationName() {
        return presentationName;
    }

    /**
	 * @return
	 */
    public boolean isValidResult() {
        return validResult;
    }
}
