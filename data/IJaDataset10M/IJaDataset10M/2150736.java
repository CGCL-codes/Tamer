package ua.od.lonewolf.Crow.View.Structure;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import ua.od.lonewolf.Crow.Controller.MainFrameController;
import ua.od.lonewolf.Crow.Util.ResourceManager;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class DockableStructureView extends JPanel implements Dockable {

    private static final long serialVersionUID = 1L;

    private DockKey key = new DockKey("Structure");

    private StructureTreeView treeView;

    public DockableStructureView(MainFrameController ctrl) {
        super();
        treeView = new StructureTreeView(ctrl);
        ctrl.getRegisterFactory().getTreeModel().addTreeModelListener(treeView);
        ToolTipManager.sharedInstance().registerComponent(treeView);
        JScrollPane scp = new JScrollPane(treeView);
        setLayout(new BorderLayout());
        treeView.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        add(scp, BorderLayout.CENTER);
        key.setCloseEnabled(false);
        key.setResizeWeight(0);
        key.setIcon(ResourceManager.getIconStructure());
    }

    public StructureTreeView getTreeView() {
        return treeView;
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public DockKey getDockKey() {
        return key;
    }
}
