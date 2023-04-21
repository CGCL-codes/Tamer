package cards;

import java.util.List;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

public class IterationCardContainerEditPolicy extends ContainerEditPolicy {

    protected Command getCreateCommand(CreateRequest request) {
        return null;
    }

    protected Command getOrphanChildrenCommand(GroupRequest request) {
        List parts = request.getEditParts();
        CompoundCommand result = new CompoundCommand();
        for (int i = 0; i < parts.size(); i++) {
            OrphanStoryCardChildCommand orphan = new OrphanStoryCardChildCommand();
            orphan.setChild((StoryCardEditPart) ((EditPart) parts.get(i)));
            orphan.setParent((IterationCardEditPart) getHost());
            result.add(orphan);
        }
        return result.unwrap();
    }
}
