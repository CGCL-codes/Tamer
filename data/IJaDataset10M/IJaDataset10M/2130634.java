package info.reflectionsofmind.vijual.library.type.template;

import info.reflectionsofmind.vijual.core.kind.IKind;
import info.reflectionsofmind.vijual.core.kind.KDefined;
import info.reflectionsofmind.vijual.core.type.IType;
import info.reflectionsofmind.vijual.core.type.TDefined;
import info.reflectionsofmind.vijual.core.type.TVariable;

public abstract class TPrimitive extends TDefined {

    public TPrimitive(String name) {
        super(name);
    }

    @Override
    public <KVar extends IKind> IType<KDefined> substitute(TVariable<KVar> variable, IType<KVar> substitution) {
        return this;
    }
}
