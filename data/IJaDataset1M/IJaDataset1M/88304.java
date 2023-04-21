package simtools.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.UIManager;

/**
 * Type 
 * <br><b>Summary:</b><br>
 * Display a wizzard in a JDialog. Let the related WizardManager deals with 
 * action performed such as next, prev, finish, cancel or help
 * 
 * To implement a JSynoptic wizard :
 * <ul>
 *		<li> Implement a WizardManger with its abstract methods
 *		<li> Implement a list of WizardPages  with their abstract methods
 *		<li> Launch wizard : new WizardDisplayer(wizardManager).show();
 * </ul>
 * 
 */
public class WizardDisplayer extends JDialog implements ActionListener, WizardPageListener {

    public static MenuResourceBundle resources = ResourceFinder.getMenu(WizardDisplayer.class);

    protected static final Insets STANDARD_INSETS = new Insets(5, 5, 5, 5);

    /**<b>(WizardPage)</b> currentPage: The currentPage.*/
    protected WizardPage currentPage;

    /**<b>(WizardManager)</b> manager: The manager.*/
    protected WizardManager manager;

    protected JButton next, prev, finish, cancel, help;

    protected JPanel inner;

    protected SummaryPanel summary;

    protected HeaderPanel headerPanel;

    public WizardDisplayer(Frame parent, String title, WizardManager manager) {
        super(parent, title, true);
        this.manager = manager;
        createContent();
        if (JDialog.isDefaultLookAndFeelDecorated()) {
            boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
                setUndecorated(true);
                getRootPane().setWindowDecorationStyle(JRootPane.QUESTION_DIALOG);
            }
        }
        setModal(true);
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    protected void createContent() {
        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        summary = new SummaryPanel();
        content.add(summary, BorderLayout.WEST);
        headerPanel = new HeaderPanel(true);
        content.add(headerPanel, BorderLayout.NORTH);
        inner = new JPanel(new BorderLayout());
        JPanel navigationBarPanel = new JPanel(new BorderLayout());
        JPanel navigationBar = new JPanel();
        prev = resources.getButton("prev", this);
        next = resources.getButton("next", this);
        finish = resources.getButton("finish", this);
        cancel = resources.getButton("cancel", this);
        help = resources.getButton("help", this);
        navigationBar.add(prev);
        navigationBar.add(next);
        navigationBar.add(finish);
        navigationBar.add(cancel);
        navigationBar.add(help);
        navigationBarPanel.add(navigationBar, BorderLayout.EAST);
        inner.add(navigationBarPanel, BorderLayout.SOUTH);
        Dimension pageSize = manager.getMaximumnPageSize();
        Dimension navigationSize = navigationBarPanel.getPreferredSize();
        inner.setPreferredSize(new Dimension(pageSize.width + navigationSize.width, pageSize.height + navigationSize.height));
        content.add(inner, BorderLayout.CENTER);
        displayNewPage();
    }

    protected void updateSummaryPanel() {
        summary.displayTitle(currentPage.getTitle());
    }

    protected void updateNavigationBar() {
        next.setEnabled((currentPage.getProblem() == null) && manager.canNext());
        finish.setEnabled((currentPage.getProblem() == null) && manager.canFinish());
        prev.setEnabled(manager.getPreviousStep() != null);
    }

    /**
	 * Method updateCurrentWizzardPanel
	 * <br><b>Summary:</b><br>
	 * If current page has changed, update display and sucribe to new page notifications
	 */
    protected void updateCurrentWizzardPanel() {
        WizardPage newPage = manager.getCurrentStep();
        if (newPage != currentPage) {
            if (currentPage != null) {
                inner.remove(currentPage);
                currentPage.removeWizardPageListener(this);
            }
            currentPage = newPage;
            if (currentPage != null) {
                currentPage.addWizardPageListener(this);
                inner.add(currentPage, BorderLayout.NORTH);
                inner.invalidate();
                inner.revalidate();
                inner.repaint();
                currentPage.requestFocus();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == next) {
            manager.processToNextStep();
            displayNewPage();
        } else if (e.getSource() == prev) {
            manager.processToPreviousStep();
            displayNewPage();
        } else if (e.getSource() == finish) {
            manager.finish();
            dispose();
        } else if (e.getSource() == cancel) {
            dispose();
        } else if (e.getSource() == help) {
        }
    }

    protected void displayNewPage() {
        if (manager.getCurrentStep() == null) {
            System.err.println("new page is null");
            return;
        }
        updateCurrentWizzardPanel();
        updateNavigationBar();
        updateSummaryPanel();
        headerPanel.setTitle(currentPage.getTitle());
        headerPanel.displayInfo(currentPage.getDescription());
    }

    public void wizardPageHasChanged() {
        String problem = currentPage.getProblem();
        if (problem != null) {
            headerPanel.displayWarning(problem);
        } else {
            headerPanel.displayInfo(currentPage.getDescription());
        }
        updateNavigationBar();
    }

    public class SummaryPanel extends JPanel {

        JLabel[] labels;

        public SummaryPanel() {
            String[] titles = WizardDisplayer.this.manager.getStepsTitles();
            labels = new JLabel[titles.length];
            setLayout(new GridLayout(titles.length, 1));
            for (int i = 0; i < titles.length; i++) {
                labels[i] = new JLabel(titles[i]);
                labels[i].setFont(new Font("Dialog", Font.PLAIN, 17));
                labels[i].setOpaque(true);
                labels[i].setBackground(Color.WHITE);
                add(labels[i]);
            }
        }

        /**
		 * Method displayTitle
		 * <br><b>Summary:</b><br>
		 * Hightlight this title in the summary list of titles
		 * @param title
		 */
        public void displayTitle(String title) {
            for (int i = 0; i < labels.length; i++) {
                if (labels[i].getText().equals(title)) {
                    labels[i].setBackground(new Color(220, 220, 220));
                } else {
                    labels[i].setBackground(Color.WHITE);
                }
            }
        }
    }
}
