package ac.jp.u_tokyo.SyncLib.references;

public class NewReference<T> extends Reference {

    private static long g_nextUID = 0;

    long _uid;

    T _type;

    public NewReference(T typeInfo) {
        _uid = g_nextUID;
        g_nextUID++;
        _type = typeInfo;
    }

    public NewReference(NewReference clone) {
        _uid = clone._uid;
    }

    public T getType() {
        return _type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (_uid ^ (_uid >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final NewReference other = (NewReference) obj;
        if (_uid != other._uid) return false;
        return true;
    }
}
