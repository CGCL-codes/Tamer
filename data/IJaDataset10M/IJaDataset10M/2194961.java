package net.sourceforge.skatmanager.ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import net.sourceforge.skatmanager.exception.SkatmanagerException;
import net.sourceforge.skatmanager.exception.SystemException;
import net.sourceforge.skatmanager.i18n.GuiMessageHandler;
import net.sourceforge.skatmanager.workflow.WorkflowManagerIf;

public class MainView extends JFrame implements MainViewIf {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = -7439522957629266850L;

    /** das aktuelle Panel */
    private JPanel jContentPane = null;

    /** Der WorkflowManager �bernimmt die Steuerung des Workflows */
    private WorkflowManagerIf workflowManager;

    /**
	 * Der Konstruktor...
	 * @param workflowManager
	 */
    public MainView(WorkflowManagerIf workflowManager) {
        super();
        this.workflowManager = workflowManager;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.setSize(800, 600);
        this.setTitle(GuiMessageHandler.getInstance().getMessage(GuiMessageHandler.TITLE_MANEFRAME));
        this.setContentPane(getJContentPane());
        JMenuBar mainMenu = null;
        try {
            mainMenu = new MenuCreator().getMenu(workflowManager);
        } catch (SystemException e) {
            this.showErrorMessage(e);
        }
        setJMenuBar(mainMenu);
        this.setVisible(true);
    }

    /**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
        }
        return jContentPane;
    }

    /**
	 * @see net.sourceforge.skatmanager.ui.MainViewIf#setView(java.lang.String)
	 */
    @Override
    public void setView(String viewName) {
    }

    /**
	 * @see net.sourceforge.skatmanager.ui.MainViewIf#showErrorMessage(net.sourceforge.skatmanager.exception.SkatmanagerException)
	 */
    @Override
    public void showErrorMessage(SkatmanagerException exception) {
        MessageTextDialog mtd = new MessageTextDialog(this, "Fehler", exception.getMessage());
        mtd.setVisible(true);
    }
}
