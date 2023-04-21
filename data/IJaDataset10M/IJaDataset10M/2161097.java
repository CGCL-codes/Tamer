package tico.editor.actions;

import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import java.util.Map;
import org.jgraph.graph.AttributeMap;
import tico.board.TBoardConstants;
import tico.board.components.TComponent;
import tico.board.components.TGrid;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich align the top of all the editor's current selected
 * components.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TAlignTopAction extends TEditorAbstractAction {

    /**
	 * Constructor of the TAlignTopAction.
	 * 
	 * @param editor The boards' editor
	 */
    public TAlignTopAction(TEditor editor) {
        super(editor, TLanguage.getString("TAlignTopAction.NAME"), TResourceManager.getImageIcon("align-top-22.png"));
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        Map nested = new Hashtable();
        Map attributeMap;
        int selectedCellsCount;
        TComponent referenceCell, currentCell;
        Rectangle2D referenceBounds, currentBounds, newBounds;
        Object[] selectedCells = getEditor().getCurrentBoard().getSelectionCells();
        for (int i = 0; i < selectedCells.length; i++) if (selectedCells[i] instanceof TGrid) getEditor().getCurrentBoard().removeSelectionCell(selectedCells[i]);
        selectedCellsCount = getEditor().getCurrentBoard().getSelectionCount();
        if (selectedCellsCount < 2) return;
        referenceCell = (TComponent) getEditor().getCurrentBoard().getSelectionCells()[0];
        referenceBounds = (Rectangle2D) TBoardConstants.getBounds(referenceCell.getAttributes());
        for (int i = 1; i < selectedCellsCount; i++) {
            attributeMap = new AttributeMap();
            currentCell = (TComponent) getEditor().getCurrentBoard().getSelectionCells()[i];
            currentBounds = (Rectangle2D) TBoardConstants.getBounds(currentCell.getAttributes());
            newBounds = new Rectangle2D.Double(currentBounds.getX(), referenceBounds.getY(), currentBounds.getWidth(), currentBounds.getHeight());
            TBoardConstants.setBounds(attributeMap, newBounds);
            nested.put(currentCell, attributeMap);
        }
        getEditor().getCurrentBoard().getGraphLayoutCache().edit(nested, null, null, null);
    }
}
