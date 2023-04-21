package diet.server.experimentmanager;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;
import diet.server.ui.JTableLogDisplay;

/**
 *
 * @author user
 */
public class JExperimentMessagesPanel extends JPanel implements FileTreeNodeAndRevisedDataProvider {

    JTableLogDisplay jtld;

    JScrollPane jScrollPaneTable;

    BorderLayout bl = new BorderLayout();

    FileTreeNode ftn;

    JTemplateAndSetupRightTabbedPanel jtasrt;

    JPanel fillerPanel = new JPanel();

    public JExperimentMessagesPanel(FileTreeNode ftn) {
        super();
        this.ftn = ftn;
        this.setLayout(bl);
        Vector v = SavedExperimentsAndSettingsFile.readVectorOfObjectsFromFile((File) ftn.getUserObject());
        jtld = new JTableLogDisplay(v);
        jtld.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jScrollPaneTable = new javax.swing.JScrollPane();
        jScrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPaneTable.getViewport().add(jtld);
        fillerPanel.setLayout(new BorderLayout());
        this.fillerPanel.add(jScrollPaneTable, BorderLayout.CENTER);
        this.add(fillerPanel, BorderLayout.CENTER);
        this.validate();
        this.setVisible(true);
    }

    public Object getData() {
        return null;
    }

    public FileTreeNode getTreeNode() {
        return ftn;
    }

    public String getSuffix() {
        return "DAT";
    }

    public void setTextOfDescription(final String s) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
            }
        });
    }

    public void addRecipientOfComponentChanges(JTemplateAndSetupRightTabbedPanel jtasrt) {
        this.jtasrt = jtasrt;
    }

    public void informRecipientOfComponentChange() {
        if (jtasrt != null) {
            jtasrt.displayLabelInRed(this);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
    }
}
