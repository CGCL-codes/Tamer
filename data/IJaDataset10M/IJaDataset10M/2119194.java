package cards;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.CellEditorActionHandler;

public class IterationCardEditManager extends DirectEditManager {

    private IActionBars actionBars;

    private CellEditorActionHandler actionHandler;

    private int typeOfLabel;

    public IterationCardEditManager(GraphicalEditPart source, int labelType, CellEditorLocator locator) {
        super(source, null, locator);
        typeOfLabel = labelType;
    }

    protected CellEditor createCellEditorOn(Composite composite) {
        return new TextCellEditor(composite, SWT.SINGLE);
    }

    @Override
    protected void initCellEditor() {
        IterationCardFigure figure = (IterationCardFigure) getEditPart().getFigure();
        if (typeOfLabel == CardConstants.ITERATIONCARDNAMELABEL) getCellEditor().setValue(figure.getNameText()); else if (typeOfLabel == CardConstants.ITERATIONCARDDESCRIPTIONLABEL) getCellEditor().setValue(figure.getDescriptionText()); else if (typeOfLabel == CardConstants.ITERATIONCARDENDDATELABEL) getCellEditor().setValue(figure.getEndDateText()); else if (typeOfLabel == CardConstants.ITERATIONCARDAVALIABLEEFFORT) getCellEditor().setValue(figure.getAvaliableEffortText());
        actionBars = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getActionBars();
        actionHandler = new CellEditorActionHandler(actionBars);
        actionHandler.addCellEditor(getCellEditor());
        actionBars.updateActionBars();
    }
}
