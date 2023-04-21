package de.tum.in.botl.util;

import de.tum.in.botl.metamodel.interfaces.ClassAssociationInterface;

public interface ModelVerificator {

    public abstract boolean isLbConform(InstanceModel m);

    public abstract boolean isLbConform(InstanceObject o);

    public abstract boolean isMMConform(InstanceModel m);

    public abstract boolean isUbConform(InstanceModel m);

    public abstract boolean isUbConform(InstanceModel m, ClassAssociationInterface ca);

    public abstract boolean isValidFragment(InstanceModel m);

    public abstract boolean isUbConform(InstanceObject o, ClassAssociationInterface ca);

    public abstract boolean isCoherent(InstanceModel m);

    public abstract boolean hasCycles(InstanceModel m);
}
