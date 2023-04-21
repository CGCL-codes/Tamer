package net.sf.gateway.mef.configuration.batchjobs.ETINRetrieval;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.sf.gateway.mef.configuration.ConfigGUIJFrame;

public class ETINRetrievalBatchJobPriorityJPanel extends JPanel {

    /**
     * Serial Version ID
     */
    private static final long serialVersionUID = 1L;

    private ConfigGUIJFrame myParent;

    private ETINRetrievalBatchJobPriorityJTextField value;

    public ETINRetrievalBatchJobPriorityJPanel(ConfigGUIJFrame myParent) {
        super();
        this.setMyParent(myParent);
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(Box.createHorizontalGlue());
        this.add(new JLabel("Priority"));
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        value = new ETINRetrievalBatchJobPriorityJTextField(myParent);
        value.setText(myParent.getConfig().getBatchJobs().getETINRetrieval().getPriority().toString());
        this.add(value);
    }

    public void refreshConfig() {
        value.setText(myParent.getConfig().getBatchJobs().getETINRetrieval().getPriority().toString());
        this.repaint();
    }

    /**
     * @param myParent
     *        the myParent to set
     */
    public void setMyParent(ConfigGUIJFrame myParent) {
        this.myParent = myParent;
    }

    /**
     * @return the myParent
     */
    public ConfigGUIJFrame getMyParent() {
        return myParent;
    }
}
