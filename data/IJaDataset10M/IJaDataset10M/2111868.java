package net.sourceforge.freejava.arch.context;

import net.sourceforge.freejava.util.Nullables;

public abstract class AbstractContext implements IContext {

    private final String contextName;

    public AbstractContext(String contextName) {
        if (contextName == null) throw new NullPointerException("contextName");
        this.contextName = contextName;
    }

    public String getContextName() {
        return contextName;
    }

    @Override
    public boolean isPenetrated() {
        return false;
    }

    public boolean equals(Object obj) {
        if (obj instanceof AbstractContext) return equals((AbstractContext) obj);
        return false;
    }

    protected boolean equals(AbstractContext obj) {
        IContext parent = getParentContext();
        IContext oParent = obj.getParentContext();
        return Nullables.equals(parent, oParent);
    }

    @Override
    public String toString() {
        return contextName + "@" + getClass().getSimpleName();
    }
}
