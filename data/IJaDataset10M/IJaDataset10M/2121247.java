package com.c4j.component;

import static java.lang.String.format;
import java.util.Comparator;
import com.c4j.INameVerifier;
import com.c4j.linker.IAnchor;
import com.c4j.sre.C4JRuntimeException;
import com.c4j.type.IType;
import com.c4j.workspace.IPort;

public abstract class ComponentPort extends ComponentElement implements IComponentPort {

    private IType type;

    ComponentPort(final Component parent, final IType iface) {
        super(parent);
        this.type = iface;
    }

    abstract IAnchor<? extends IPort> getAnchor();

    @Override
    public final String getName() {
        return getAnchor().getAnchorString();
    }

    @Override
    public final void setName(final String name) {
        if (name.equals(getName())) return;
        final INameVerifier verifier = getNameVerifier();
        if (!verifier.isNameValid(name)) throw new C4JRuntimeException(format("Can not rename port to ‘%s’. Name does not match expression ‘%s’.", name, verifier.getNameExpression()));
        if (verifier.isNameUsed(name)) throw new C4JRuntimeException(format("Can not rename port to ‘%s’. Port with same name already exits.", name));
        final String oldID = getAnchor().getAnchorString();
        getAnchor().setAnchorString(name);
        getRoot().fireRenamedComponentPort(this, oldID);
    }

    @Override
    public final INameVerifier getNameVerifier() {
        return getRoot().getPortNameVerifier();
    }

    @Override
    public final IType getType() {
        return type;
    }

    @Override
    public final void setType(final IType portType) {
        this.type = portType;
        getRoot().fireChangedComponentPortType(this);
    }

    @Override
    public final int getCardinality() {
        return 1;
    }

    public static final Comparator<ComponentPort> COMPARATOR = new Comparator<ComponentPort>() {

        @Override
        public int compare(final ComponentPort o1, final ComponentPort o2) {
            return o1.getAnchor().getAnchorString().compareTo(o2.getAnchor().getAnchorString());
        }
    };
}
