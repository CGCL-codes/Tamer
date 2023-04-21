package org.musicnotation.gef.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.musicnotation.gef.commands.MusicElementDeleteCommand;
import org.musicnotation.model.MusicElement;

public class MusicElementComponentEditPolicy extends ComponentEditPolicy {

    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        final Object model = getHost().getModel();
        if (MusicElement.class.isInstance(model)) {
            MusicElementDeleteCommand result = new MusicElementDeleteCommand();
            MusicElement musicElement = (MusicElement) model;
            result.setChild(musicElement);
            result.setParent(musicElement.getPart());
            return result;
        } else {
            return null;
        }
    }
}
