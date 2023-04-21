package be.lassi.ui.group;

import javax.swing.JComponent;
import be.lassi.context.ShowContext;
import be.lassi.ui.base.BasicFrame;
import be.lassi.util.Help;
import be.lassi.util.NLS;

public class GroupsFrame extends BasicFrame {

    private final GroupsPresentationModel model;

    public GroupsFrame(ShowContext context) {
        super(context, NLS.get("groups.window.title"));
        model = new GroupsPresentationModel(context);
        Help.enable(this, "groups");
        init();
    }

    protected JComponent createPanel() {
        JComponent panel = new GroupsView(model).build();
        return panel;
    }
}
