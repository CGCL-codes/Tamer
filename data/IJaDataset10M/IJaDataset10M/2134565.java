package net.sf.contrail.core.utils;

import net.sf.contrail.core.Identifier;

public abstract class ContrailAction extends ContrailTask<Void> {

    public ContrailAction(Identifier id, Operation operation) {
        super(id, operation);
    }

    public ContrailAction() {
        super(Identifier.create(), null);
    }

    @Override
    public ContrailAction submit() {
        return (ContrailAction) super.submit();
    }
}
