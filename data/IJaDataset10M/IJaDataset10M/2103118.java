package com.safi.workshop.actionpak1.policy;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import com.safi.workshop.edit.commands.SetInputItemValueCommand;
import com.safi.workshop.edit.parts.InputItemEditPart;
import com.safi.workshop.edit.policies.EditInputItemPolicy;

public class InvokeSafletEditInputItemPolicy extends EditInputItemPolicy {

    @Override
    protected Command getOpenCommand(Request request) {
        InputItemEditPart ciep = (InputItemEditPart) getHost();
        return new ICommandProxy(new SetInputItemValueCommand(ciep, false));
    }
}
