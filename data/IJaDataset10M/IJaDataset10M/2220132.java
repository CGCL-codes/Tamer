package net.sf.refactorit.ui.module.where;

import net.sf.refactorit.options.GlobalOptions;
import net.sf.refactorit.query.usage.filters.SearchFilter;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Filter options for all searches.
 *
 * @author Vladislav Vislogubov
 * @author Anton Safonov
 */
public class CommonPanel extends JPanel {

    private JCheckBox groupSameLineUsages = new JCheckBox("Group usages located on the same line", true);

    private JCheckBox goToSingle = new JCheckBox("Go to source in case of single usage", true);

    private JCheckBox runWithDefaultSettings = new JCheckBox("Run with default filter settings next time", true);

    public CommonPanel(SearchFilter filter) {
        super();
        if (filter != null) {
            groupSameLineUsages.setSelected(!filter.isShowDuplicates());
            goToSingle.setSelected(filter.isGoToSingleUsage());
            runWithDefaultSettings.setSelected(filter.isRunWithDefaultSettings());
        } else {
            groupSameLineUsages.setSelected(!"true".equals(GlobalOptions.getOption("where.used.show_duplicates", "false")));
            goToSingle.setSelected("true".equals(GlobalOptions.getOption("where.used.go_to_single_usage", "true")));
            runWithDefaultSettings.setSelected("true".equals(GlobalOptions.getOption("where.used.run_with_default_settings", "false")));
        }
        init();
    }

    private void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(5, 10, 3, 5);
        constraints.gridy = 1;
        add(groupSameLineUsages, constraints);
        constraints.insets = new Insets(0, 10, 5, 5);
        constraints.gridy = 2;
        add(goToSingle, constraints);
        constraints.insets = new Insets(0, 10, 5, 5);
        constraints.gridy = 3;
        add(runWithDefaultSettings, constraints);
    }

    public boolean isShowDuplicates() {
        GlobalOptions.setOption("where.used.show_duplicates", (!groupSameLineUsages.isSelected() ? "true" : "false"));
        return !groupSameLineUsages.isSelected();
    }

    public boolean isGoToSingleUsage() {
        GlobalOptions.setOption("where.used.go_to_single_usage", (goToSingle.isSelected() ? "true" : "false"));
        return goToSingle.isSelected();
    }

    public boolean isRunWithDefaultSettings() {
        GlobalOptions.setOption("where.used.run_with_default_settings", (runWithDefaultSettings.isSelected() ? "true" : "false"));
        return runWithDefaultSettings.isSelected();
    }
}
