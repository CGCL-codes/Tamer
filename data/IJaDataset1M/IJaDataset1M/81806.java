package net.sf.vex.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.vex.dom.Element;
import net.sf.vex.widget.IVexWidget;

/**
 * Inserts a single table column before the current one.
 */
public class InsertColumnBeforeAction extends AbstractVexAction {

    public void run(final IVexWidget vexWidget) {
        vexWidget.doWork(new Runnable() {

            public void run() {
                final int indexToDup = ActionUtils.getCurrentColumnIndex(vexWidget);
                if (indexToDup == -1) {
                    return;
                }
                final List cellsToDup = new ArrayList();
                ActionUtils.iterateTableCells(vexWidget, new TableCellCallback() {

                    public void startRow(Object row, int rowIndex) {
                    }

                    public void onCell(Object row, Object cell, int rowIndex, int cellIndex) {
                        if (cellIndex == indexToDup && cell instanceof Element) {
                            cellsToDup.add(cell);
                        }
                    }

                    public void endRow(Object row, int rowIndex) {
                    }
                });
                int finalOffset = -1;
                for (Iterator it = cellsToDup.iterator(); it.hasNext(); ) {
                    Element element = (Element) it.next();
                    if (finalOffset == -1) {
                        finalOffset = element.getStartOffset() + 1;
                    }
                    vexWidget.moveTo(element.getStartOffset());
                    vexWidget.insertElement((Element) element.clone());
                }
                if (finalOffset != -1) {
                    vexWidget.moveTo(finalOffset);
                }
            }
        });
    }

    public boolean isEnabled(IVexWidget vexWidget) {
        return ActionUtils.getCurrentColumnIndex(vexWidget) != -1;
    }
}
