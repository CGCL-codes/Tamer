package ldapbeans.util.cache;

import java.util.Arrays;

public class GenericKey {

    private final Object[] m_Keys;

    /**
     * Construct a key base on an array of object
     * 
     * @param p_Keys
     *            Object that constitute the key
     */
    public GenericKey(Object... p_Keys) {
        if (p_Keys == null) {
            throw new IllegalArgumentException();
        }
        m_Keys = p_Keys;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        if (m_Keys != null) {
            for (Object obj : m_Keys) {
                hash = hash ^ 53 + obj.hashCode();
            }
        }
        return hash;
    }

    @Override
    public boolean equals(Object p_Obj) {
        boolean equals = false;
        if ((p_Obj != null) && (p_Obj instanceof GenericKey)) {
            Object[] keys = ((GenericKey) p_Obj).m_Keys;
            if (keys.length == m_Keys.length) {
                equals = true;
                for (int i = 0; i < m_Keys.length; i++) {
                    if (m_Keys[i] != null) {
                        if (!m_Keys[i].equals(keys[i])) {
                            equals = false;
                        }
                    } else if (keys[i] != null) {
                        equals = false;
                    }
                }
            }
        }
        return equals;
    }

    /**
     * {@inheritDoc}
     * 
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return Arrays.asList(m_Keys).toString();
    }
}
