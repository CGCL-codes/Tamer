package rbsla.gui.wizard;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.SwingUtilities;
import rbsla.gui.MainWindow;

public class StartNewWizard extends NewWizard {

    /**
	 * Constructor.
	 * @param initialProperties some properties passed to the first wizard page
	 * optional (can be null)
	 */
    public StartNewWizard() {
        super();
    }

    /**
	 * Constructor.
	 * @param owner the owner component
	 * @param initialProperties some properties passed to the first wizard page
	 * optional (can be null)
	 */
    public StartNewWizard(java.awt.Dialog owner) {
        super(owner);
    }

    /**
	 * Constructor.
	 * @param owner the owner component
	 * @param title the dialog title
	 * @param initialProperties some properties passed to the first wizard page
	 * optional (can be null)
	 */
    public StartNewWizard(java.awt.Dialog owner, String title) {
        super(owner, title);
    }

    public StartNewWizard(MainWindow owner, String title) {
        super(owner, title);
    }

    /**
	 * Get the first page for this wizard.
	 * @return a wizard page
	 */
    public NewWizardPage getFirstPageW(MainWindow owner) {
        return new NewWizardPageSelectKnowledgeType(owner);
    }

    private static Window getParentWindow(Component c) {
        Component top = SwingUtilities.getRoot(c);
        return top instanceof Window ? (Window) top : new Frame();
    }

    /**
	 * Show this wizard.
	 * @param parent the parent component
	 * @param bpmgr the blueprint manager used to display the clause sets
	 * @param dd the domain description
	 * @return a clause set or null if the wizard has been canceled
	 */
    public static void show(Component parent) {
        Window win = getParentWindow(parent);
        StartNewWizard wizard = win instanceof Dialog ? new StartNewWizard((Dialog) win, "Add new knowledge") : new StartNewWizard((MainWindow) win, "Add new knowledge");
        wizard.setSize(500, 300);
        wizard.setLocationRelativeTo(parent);
        wizard.setVisible(true);
    }
}
