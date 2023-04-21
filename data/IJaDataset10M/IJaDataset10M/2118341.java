package org.deft.extension.decoration.modify;

import java.util.List;
import org.deft.repository.ast.TokenNode;
import org.deft.repository.ast.decoration.GroupListBuilder;

/**
 * 
 * @author Martin Heinzerling
 * @see GroupListBuilder
 */
public class ModifiedGroupListBuilder extends GroupListBuilder<ModifiedGroup> {

    public ModifiedGroupListBuilder(List<TokenNode> nodes) {
        super(nodes);
    }

    @Override
    public boolean belongsToGroup(ModifiedGroup group, TokenNode tn) {
        ModifiedInformation mi = (ModifiedInformation) tn.getInformation(ModifiedInformation.IDENT);
        if (group.isModified()) {
            if (mi == null) {
                return false;
            }
            return true;
        } else {
            if (mi == null) {
                return true;
            }
            return false;
        }
    }

    @Override
    public ModifiedGroup createNewGroup(TokenNode tn) {
        ModifiedInformation mi = (ModifiedInformation) tn.getInformation(ModifiedInformation.IDENT);
        if (mi == null) {
            return new ModifiedGroup(false);
        }
        return new ModifiedGroup(true);
    }
}
