package bigraph.biged.ui.commands;

import java.util.Collection;
import bigraph.biged.model.BigraphEvent.Type;
import bigraphspace.model.signaturexml.Control;
import bigraphspace.model.signaturexml.Definitions;

public class SetSignatureControlStatusCommand extends SignatureCommand {

    private final Control control;

    private final String newName;

    private String oldName;

    public SetSignatureControlStatusCommand(final Definitions definitions, final Control control, final String newName) {
        super(definitions, "Set Signature Control Status to " + newName);
        this.control = control;
        if (newName == null || newName.equals("")) {
            this.newName = null;
        } else {
            this.newName = newName;
        }
        this.oldName = control.getStatus();
    }

    @Override
    public boolean canExecute() {
        return oldName != newName && (oldName == null || !oldName.equals(newName));
    }

    @Override
    public Collection<Object> getAffectedObjects() {
        final Collection<Object> result = super.getAffectedObjects();
        result.add(control);
        return result;
    }

    @Override
    public Type getType(final boolean undo) {
        return Type.CHANGE;
    }

    @Override
    protected void doExecute() {
        oldName = control.getStatus();
        control.setStatus(newName);
    }

    @Override
    protected void doUndo() {
        control.setStatus(oldName);
    }
}
