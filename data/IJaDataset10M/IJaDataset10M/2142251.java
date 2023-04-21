package net.sf.doolin.sqm.gui.toolbar;

import javax.swing.JLabel;
import javax.swing.JToolBar;
import net.sf.doolin.gui.core.View;
import net.sf.doolin.gui.core.view.ToolbarItem;
import net.sf.doolin.sqm.gui.SQMUtils;

public class ToolbarUserItem implements ToolbarItem {

    private JLabel label;

    @Override
    public void createToolbarItem(JToolBar j, View view) {
        label = new JLabel("");
        j.add(label);
        label.setText(SQMUtils.getSessionFullDescription());
    }
}
