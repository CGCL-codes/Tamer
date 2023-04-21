package rbsla.gui.util.infoareas;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JTextField;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class DescriptionWindowsListener implements WindowFocusListener {

    private Description owner;

    private JTextField moduleName = null;

    private DefaultTreeModel model = null;

    private TreePath path = null;

    public DescriptionWindowsListener(Description owner, JTextField module, DefaultTreeModel model, TreePath path) {
        this.owner = owner;
        this.moduleName = module;
        this.model = model;
        this.path = path;
    }

    public void windowLostFocus(WindowEvent e) {
        if (this.moduleName != null && this.model != null && this.path != null) {
            if (!"".equals(this.moduleName.getText().trim())) model.valueForPathChanged(this.path, this.moduleName.getText());
        }
        this.owner.setVisible(false);
        this.owner.dispose();
    }

    public void windowGainedFocus(WindowEvent e) {
    }
}
