package games.strategy.engine.data;

import java.io.Serializable;

/**
 * 
 * @author Sean Bridges
 * @version 1.0
 */
public class DefaultNamed extends GameDataComponent implements Named, Serializable {

    private final String m_name;

    /** Creates new DefaultNamed */
    public DefaultNamed(final String name, final GameData data) {
        super(data);
        if (name == null || name.length() == 0) throw new IllegalArgumentException("Name must not be null");
        m_name = name;
    }

    public String getName() {
        return m_name;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof Named)) return false;
        final Named other = (Named) o;
        return this.m_name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        return m_name.hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " called " + m_name;
    }
}
