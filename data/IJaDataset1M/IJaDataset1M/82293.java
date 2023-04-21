package be.lassi.ui.util.dialog;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import be.lassi.util.Help;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class DialogPanel {

    public JComponent build(final JComponent centerPanel, final JComponent buttonBar, final String title, final String subTitle) {
        JComponent titlePanel = new DialogTitlePanel(title, subTitle);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buildButtonPanel(buttonBar), BorderLayout.SOUTH);
        return panel;
    }

    private JComponent buildButtonPanel(final JComponent buttonBar) {
        FormLayout layout = new FormLayout("4dlu, pref, 4dlu:grow, pref, 8dlu", "pref, 4dlu, pref, 4dlu, 4px");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(new JSeparator(), cc.xyw(1, 1, 5));
        builder.add(Help.createHelpButton(), cc.xy(2, 3));
        builder.add(buttonBar, cc.xy(4, 3));
        return builder.getPanel();
    }
}
