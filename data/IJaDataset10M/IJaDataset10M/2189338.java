package fr.jussieu.gla.wasa.monitor.gui.detail;

import fr.jussieu.gla.wasa.monitor.model.RunNode;
import java.util.Date;
import javax.swing.*;
import java.awt.*;

/**
 * Summary part of a {@link RunDetailPanel}.
 * @author Florent Selva
 * @version $Revision: 1.7 $ $Date: 2002/04/03 17:59:55 $
 */
public class RunSummaryPanel extends DetailPanelPart {

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel jLabelStepCountTitle = new JLabel();

    private JLabel jLabelStepCount = new JLabel();

    private JLabel jLabelSloutionFoundTitle = new JLabel();

    private JLabel jLabelSloutionFound = new JLabel();

    private JLabel jLabelDateTitle = new JLabel();

    private JLabel jLabelDate = new JLabel();

    private JButton jButtonAbsoluteBestConfiguration = new JButton();

    private JLabel jLabelRandomCountTitle = new JLabel();

    private JLabel jLabelRandomCount = new JLabel();

    private JLabel jLabelLiveTitle = new JLabel();

    private JLabel jLabelLive = new JLabel();

    public RunSummaryPanel() {
    }

    public RunSummaryPanel(DetailPanelContainer container) {
        super(container);
        jbInit();
    }

    protected String getPartName() {
        return "Summary";
    }

    protected void doRefreshData() {
        RunNode runNode = (RunNode) getApplicationContext().getSelectionManager().getSelectedNode();
        jLabelStepCount.setText(Integer.toString(runNode.getStepNodeCount()));
        jLabelRandomCount.setText(Integer.toString(runNode.getRandomCount()));
        jLabelSloutionFound.setText(runNode.isSolutionFound() ? "yes" : "no");
        Date runDate = runNode.getRunDate();
        jLabelDate.setText((runDate == null) ? "unassigned" : runDate.toString());
        jLabelLive.setText(runNode.isLive() ? "yes" : "no");
    }

    private void jbInit() {
        jLabelStepCountTitle.setText("Step count");
        this.setLayout(gridBagLayout1);
        jLabelStepCount.setText("10");
        jLabelSloutionFoundTitle.setText("Solution found");
        jLabelSloutionFound.setText("yes");
        jLabelDateTitle.setText("Date of the run");
        jLabelDate.setText("2002-02-12");
        jButtonAbsoluteBestConfiguration.setText("Go to Absolute best configuration");
        jLabelRandomCountTitle.setText("Random count");
        jLabelRandomCount.setText("12");
        jLabelLiveTitle.setText("Live Run");
        jLabelLive.setText("?");
        this.add(jLabelStepCountTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabelStepCount, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabelSloutionFoundTitle, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(20, 5, 5, 5), 0, 0));
        this.add(jLabelSloutionFound, new GridBagConstraints(1, 1, 2, 2, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(20, 5, 5, 5), 0, 0));
        this.add(jLabelLiveTitle, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 5, 5, 5), 0, 0));
        this.add(jLabelLive, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(20, 5, 5, 5), 0, 0));
    }
}
