package be.vds.jtbtaskplanner.client.view.core.preferences;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbtaskplanner.client.core.UserPreferences;

public class FormattingPreferrencePanel extends AbstractPreferrencePanel {

    private static final long serialVersionUID = -3689046291861107714L;

    private JComboBox dateFormattingCb;

    private DefaultComboBoxModel dateFormattingModel;

    public FormattingPreferrencePanel() {
        super();
    }

    @Override
    protected Component createContentPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 3, 5, 3);
        int y = 0;
        GridBagLayoutManager.addComponent(p, createDateLabel(), c, 0, y, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        GridBagLayoutManager.addComponent(p, createDateFormattingComponent(), c, 1, y++, 1, 1, 1, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        GridBagLayoutManager.addComponent(p, Box.createVerticalGlue(), c, 0, y, 1, 1, 0, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        return p;
    }

    private Component createDateLabel() {
        return new I18nLabel("date.hours");
    }

    private Component createDateFormattingComponent() {
        String[] formats = { "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss", "MM-dd-yyyy HH:mm:ss" };
        dateFormattingModel = new DefaultComboBoxModel(formats);
        dateFormattingCb = new JComboBox(dateFormattingModel);
        dateFormattingCb.setEditable(true);
        return dateFormattingCb;
    }

    @Override
    public void adaptUserPreferences() {
        UserPreferences up = UserPreferences.getInstance();
        up.setDateHoursFormat(new SimpleDateFormat((String) dateFormattingCb.getSelectedItem()));
    }

    @Override
    public void setUserPreferences() {
        UserPreferences up = UserPreferences.getInstance();
        SimpleDateFormat sdf = up.getPreferredDateHoursFormat();
        String pattern = sdf.toPattern();
        int index = dateFormattingModel.getIndexOf(sdf.toPattern());
        if (index > -1) {
            dateFormattingCb.setSelectedIndex(index);
        } else {
            dateFormattingModel.addElement(pattern);
        }
    }
}
