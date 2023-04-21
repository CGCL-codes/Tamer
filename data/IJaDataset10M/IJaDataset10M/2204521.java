package geovista.toolkitcore.history;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TestGUI extends JPanel {

    private JScrollPane historyPane = null;

    private JTable historyTable = null;

    /**
	 * This method initializes historyPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getHistoryPane() {
        if (historyPane == null) {
            historyPane = new JScrollPane();
            historyPane.setViewportView(getHistoryTable());
        }
        return historyPane;
    }

    /**
	 * This method initializes historyTable
	 * 
	 * @return javax.swing.JTable
	 */
    private JTable getHistoryTable() {
        if (historyTable == null) {
            historyTable = new JTable();
        }
        return historyTable;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        JFrame app = new JFrame("TestGUI");
        TestGUI gui = new TestGUI();
        app.add(gui);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.pack();
        app.setVisible(true);
    }

    /**
	 * This is the default constructor
	 */
    public TestGUI() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        this.setSize(300, 200);
        setLayout(new GridBagLayout());
        this.add(getHistoryPane(), gridBagConstraints);
    }
}
