package org.vikamine.swing.subgroup.view.knowledge.compare;

import java.util.List;
import javax.swing.JPopupMenu;
import org.vikamine.swing.subgroup.view.knowledge.SubgroupKnowledgeModel;
import org.vikamine.swing.subgroup.view.workspace.SGPopupContextMenuFactory;
import org.vikamine.swing.subgroup.view.workspace.SubgroupsTreetableController;
import org.vikamine.swing.subgroup.view.workspace.SubgroupsTreetableModel;
import org.vikamine.swing.subgroup.visualization.VisualizationUtils;

public class SubgroupKnowledgeTreetableController extends SubgroupsTreetableController {

    public SubgroupKnowledgeTreetableController() {
        super();
        popupFactory = new SGPopupContextMenuFactory() {

            @Override
            public JPopupMenu createSGsContextMenu(List values) {
                JPopupMenu menu = new JPopupMenu();
                addCommonSGsContextMenuItems(values, menu, true);
                menu.addSeparator();
                VisualizationUtils.addVisualizationActions(menu, values);
                return menu;
            }
        };
    }

    @Override
    protected int getPreferredColumnWidth(int col) {
        switch(getModel().getColumnType(col)) {
            case SubgroupsTreetableModel.VERBALISATION:
                return 40;
            case SubgroupsTreetableModel.SG_STAT_COMPONENT:
                return 6;
            case SubgroupsTreetableModel.BAR_VISUALISATION:
                return 40;
            case SubgroupKnowledgeTreetableModel.SIMILARITY:
                return 10;
            case SubgroupKnowledgeModel.COMPARE:
                return 8;
            default:
                return 0;
        }
    }
}
