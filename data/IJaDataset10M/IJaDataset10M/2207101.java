package cc.w3d.jawos.jinn.xuid.xuidGenerator.core.structure;

import java.io.Serializable;

public abstract class Uid implements Serializable {

    private static final long serialVersionUID = -4384552209350086966L;

    @Override
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof Uid)) return false;
        return equals((Uid) arg0);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public abstract boolean equals(Uid arg0);

    public abstract byte[] toByteArray();

    public abstract String toString();

    public abstract long toLong();
}
