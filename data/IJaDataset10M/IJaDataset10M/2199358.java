package fr.soleil.mambo.components.view;

import javax.swing.tree.TreePath;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.VCTreeRenderer;
import fr.soleil.mambo.models.VCAttributesTreeModel;

public class VCAttributesSelectTree extends AttributesTree {

    private static final long serialVersionUID = 5099940571534300052L;

    public VCAttributesSelectTree(VCAttributesTreeModel newModel, ViewConfigurationBean viewConfigurationBean) {
        super(newModel);
        setCellRenderer(new VCTreeRenderer(viewConfigurationBean, true));
        setExpandsSelectedPaths(true);
        setScrollsOnExpand(true);
        setShowsRootHandles(true);
        setToggleClickCount(1);
    }

    public void setExpandedState(TreePath path, boolean state) {
        super.setExpandedState(path, state);
    }

    @Override
    public VCAttributesTreeModel getModel() {
        return (VCAttributesTreeModel) super.getModel();
    }
}
