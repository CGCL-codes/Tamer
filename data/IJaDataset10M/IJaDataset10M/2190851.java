package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import fr.soleil.archiving.gui.tools.ComponentPrinter;
import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.context.ContextDetailPrintPanel;
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
public class PrintContextDetailAction extends BensikinAction {

    private ContextDetailPrintPanel panel;

    private String title;

    /**
	 * Standard action constructor that sets the action's name, plus initializes
	 * the reference to the <code>ContextDetailPrintPanel</code> component to
	 * print on <code>actionPerformed</code>
	 * 
	 * @param name
	 *            The action name
	 * @param title
	 *            The print title
	 * @param panel
	 *            The ContextDetailPrintPanel
	 */
    public PrintContextDetailAction(String name, String title, ContextDetailPrintPanel panel) {
        super(name);
        this.putValue(Action.NAME, name);
        this.panel = panel;
        this.title = title;
    }

    public void actionPerformed(ActionEvent arg0) {
        ComponentPrinter printer = new ComponentPrinter(this.panel);
        printer.setDocumentTitle(title);
        printer.setJobName(title);
        PrintOptions options = Options.getInstance().getPrintOptions();
        printer.setFitMode(options.getFitMode());
        printer.setOrientation(options.getOrientation());
        printer.print();
        printer = null;
        options = null;
    }
}
