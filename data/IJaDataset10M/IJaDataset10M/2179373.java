package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import fr.soleil.archiving.gui.tools.ComponentPrinter;
import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotCompareTable;
import fr.soleil.bensikin.models.SnapshotCompareTablePrintModel;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.options.sub.PrintOptions;

/**
 * An action that prints the current snapshot comparison's table to a printer.
 * <UL>
 * <LI>Uses DTPrinter to print its SnapshotCompareTable attribute
 * </UL>
 * 
 * @author CLAISSE
 */
public class PrintSnapshotComparisonAction extends BensikinAction {

    private SnapshotCompareTable compareTable;

    /**
	 * Standard action constructor that sets the action's name, plus initializes
	 * the reference to the <code>SnapshotCompareTable</code> component to print
	 * on <code>actionPerformed</code>
	 * 
	 * @param name
	 *            The action name
	 * @param _compareTable
	 */
    public PrintSnapshotComparisonAction(String name, SnapshotCompareTable _compareTable) {
        super(name);
        this.putValue(Action.NAME, name);
        this.compareTable = _compareTable;
    }

    public void actionPerformed(ActionEvent arg0) {
        SnapshotCompareTablePrintModel model = null;
        if (compareTable != null && compareTable.getModel() instanceof SnapshotCompareTablePrintModel) {
            model = (SnapshotCompareTablePrintModel) compareTable.getModel();
        }
        model.setTitlesDisplayed(true);
        ComponentPrinter printer = new ComponentPrinter(this.compareTable);
        printer.setDocumentTitle(getValue(Action.NAME).toString());
        printer.setJobName(getValue(Action.NAME).toString());
        PrintOptions options = Options.getInstance().getPrintOptions();
        printer.setFitMode(options.getFitMode());
        printer.setOrientation(options.getOrientation());
        printer.print();
        printer = null;
        options = null;
        model.setTitlesDisplayed(false);
        model = null;
    }
}
