package fr.jussieu.gla.wasa.monitor.gui.detail;

import fr.jussieu.gla.wasa.monitor.model.ConfigurationNode;
import javax.swing.*;
import java.awt.*;

/**
 * Summary part of a {@link ConfigurationDetailPanel}.
 * @author Florent Selva
 * @version $Revision: 1.3 $ $Date: 2002/03/28 10:00:48 $
 */
public class ConfigurationSummaryPanel extends DetailPanelPart {

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel jLabelSireTitle = new JLabel();

    private JLabel jLabelSire = new JLabel();

    private JLabel jLabelOverallErrorTitle = new JLabel();

    private JLabel jLabelOverallError = new JLabel();

    public ConfigurationSummaryPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConfigurationSummaryPanel(DetailPanelContainer container) {
        super(container);
        jbInit();
    }

    protected String getPartName() {
        return "Summary";
    }

    protected void doRefreshData() {
        ConfigurationNode configurationNode = (ConfigurationNode) getApplicationContext().getSelectionManager().getSelectedNode();
        jLabelSire.setText((configurationNode.getSire() == null) ? "unassigned" : configurationNode.getSire().toString());
        jLabelOverallError.setText(Double.toString(configurationNode.getOverallError()));
    }

    private void jbInit() {
        jLabelSireTitle.setText("Sire");
        this.setLayout(gridBagLayout1);
        jLabelSire.setText("21");
        jLabelOverallErrorTitle.setText("Overall error");
        jLabelOverallError.setText("100");
        this.add(jLabelSireTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabelSire, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabelOverallErrorTitle, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabelOverallError, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    }
}
