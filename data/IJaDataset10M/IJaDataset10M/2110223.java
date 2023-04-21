package org.isistan.flabot.edit.ucmeditor.dialogs.responsibilitynode.editionitem;

import org.isistan.flabot.coremodel.ResponsibilityNode;
import org.isistan.flabot.edit.componenteditor.dialogs.responsibilitymaterialization.ResponsibilityMaterialization;
import org.isistan.flabot.edit.componenteditor.dialogs.responsibilitymaterialization.ResponsibilityMaterializationEditionItem;
import org.isistan.flabot.edit.componenteditor.dialogs.responsibilitymaterialization.ResponsibilityMaterializationImpl;
import org.isistan.flabot.edit.ucmeditor.dialogs.responsibilitynode.ResponsibilityNodeEditionItem;
import org.isistan.flabot.messages.Messages;
import org.isistan.flabot.util.edition.DataAdapter;
import org.isistan.flabot.util.edition.tab.EditionTabItemContainer;
import org.isistan.flabot.util.problems.log.LoggerMessageAccumulator;

/**
 * This edition item wrappes all edition tabs contributed for
 * to responsibility node's edition extension point
 * @author $Author: franco $
 *
 */
public class ResponsibilityMaterializationEditionItemContainer extends EditionTabItemContainer<ResponsibilityNode, ResponsibilityMaterialization> implements ResponsibilityNodeEditionItem {

    private static DataAdapter<ResponsibilityNode, ResponsibilityMaterialization> dataArapter = new DataAdapter<ResponsibilityNode, ResponsibilityMaterialization>() {

        public ResponsibilityMaterialization adapt(ResponsibilityNode data) {
            return new ResponsibilityMaterializationImpl(data.getResponsibility(), data.getRole().getComponent());
        }
    };

    public ResponsibilityMaterializationEditionItemContainer() {
        super(Messages.getString("org.isistan.flabot.edit.ucmeditor.dialogs.responsibilitynode.editionitem.ResponsibilityMaterializationEditionItemContainer.tabName"), Messages.getString("org.isistan.flabot.edit.ucmeditor.dialogs.responsibilitynode.editionitem.ResponsibilityMaterializationEditionItemContainer.commandLabel"), dataArapter, ResponsibilityMaterializationEditionItem.LOADER.getEditionItems(new LoggerMessageAccumulator()));
    }

    public boolean accepts(ResponsibilityNode element) {
        return element.getRole() != null;
    }
}
