package org.vikamine.swing.util;

import javax.swing.tree.DefaultMutableTreeNode;
import org.vikamine.app.DMManager;
import org.vikamine.kernel.data.Population;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.target.SGTarget;
import org.vikamine.swing.subgroup.AllSubgroupPluginController;

public class Utils {

    public static SGTarget getCurrentTarget() {
        SGTarget target = (SGTarget) ((DefaultMutableTreeNode) AllSubgroupPluginController.getInstance().getSubgroupTreeController().getModel().getRoot()).getUserObject();
        return target;
    }

    public static Population getCurrentPopulation() {
        return DMManager.getInstance().getOntology().getPopulation();
    }

    public static SG getCurrentSG() {
        return AllSubgroupPluginController.getInstance().getSubgroupTreeController().getModel().getSubgroup();
    }
}
