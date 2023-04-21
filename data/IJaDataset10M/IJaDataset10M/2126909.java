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
import diet.server.conversationhistory.ui.JTurnsListTable;

/**
 *
 * @author user
 */
public class JExperimentTurnsPanel extends JPanel implements FileTreeNodeAndRevisedDataProvider {

    JTurnsListTable jtlt;

    JScrollPane jScrollPaneTable;

    BorderLayout bl = new BorderLayout();

    FileTreeNode ftn;

    JTemplateAndSetupRightTabbedPanel jtasrt;

    JPanel fillerPanel = new JPanel();

    public JExperimentTurnsPanel(FileTreeNode ftn) {
        super();
        this.ftn = ftn;
        this.setLayout(bl);
        Vector v = SavedExperimentsAndSettingsFile.readVectorOfObjectsFromFile((File) ftn.getUserObject());
        jtlt = new JTurnsListTable(v);
        jtlt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jScrollPaneTable = new javax.swing.JScrollPane();
        jScrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPaneTable.getViewport().add(jtlt);
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
