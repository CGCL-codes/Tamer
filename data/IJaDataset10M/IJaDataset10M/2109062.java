package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.components.view.VCAttributesSelectTree;
import fr.soleil.mambo.components.view.VCPossibleAttributesTree;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.models.VCAttributesTreeModel;

public class AddSelectedVCAttributesAction extends AbstractAction {

    private static final long serialVersionUID = -5268580903604795617L;

    private ViewConfigurationBean viewConfigurationBean;

    private VCAttributesSelectTree selectTree;

    private VCAttributesPropertiesTree propertiesTree;

    private VCPossibleAttributesTree possibleAttributesTree;

    /**
	 * @param name
	 */
    public AddSelectedVCAttributesAction(String name, ViewConfigurationBean viewConfigurationBean, VCAttributesSelectTree selectTree, VCAttributesPropertiesTree propertiesTree, VCPossibleAttributesTree possibleAttributesTree) {
        super();
        this.putValue(Action.NAME, name);
        this.viewConfigurationBean = viewConfigurationBean;
        this.selectTree = selectTree;
        this.propertiesTree = propertiesTree;
        this.possibleAttributesTree = possibleAttributesTree;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (viewConfigurationBean != null) {
            Vector<TreePath> listToAdd = possibleAttributesTree.getListOfAttributesTreePathUnderSelectedNodes(false);
            if (listToAdd != null && listToAdd.size() != 0) {
                VCAttributesTreeModel model = viewConfigurationBean.getEditingModel();
                model.setTree(selectTree);
                try {
                    model.addSelectedAttibutes(listToAdd);
                    model.reload();
                    selectTree.expandAll(true);
                    if (propertiesTree != null) {
                        propertiesTree.expandAll(true);
                    }
                    TreeMap<String, ViewConfigurationAttribute> attrs = model.getAttributes();
                    ViewConfiguration currentViewConfiguration = viewConfigurationBean.getEditingViewConfiguration();
                    if (currentViewConfiguration != null) {
                        currentViewConfiguration.getAttributes().addAttributes(attrs);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
