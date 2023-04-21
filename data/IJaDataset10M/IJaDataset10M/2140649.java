package jws.commons.config;

import jws.commons.util.Observable;
import jws.commons.swing.ICellEditable;

/**
 * Provides an observable placeholder for a property value
 * @param <T> Type of value this property holds
 */
public final class ConfigProperty<T> extends Observable<T> implements ICellEditable {

    private ConfigSection _section;

    private String _name;

    /**
     * Creates new {@link ConfigProperty} object with a given name and <code>null</code> value
     *   inside the scope of a given {@link ConfigSection}
     * @param section The parent {@link ConfigSection}
     * @param name Unqualified property name
     */
    ConfigProperty(ConfigSection section, String name) {
        _section = section;
        _name = name;
    }

    /**
     * Creates new {@link ConfigProperty} object with a given name and value
     *   inside the scope of a given {@link ConfigSection}
     * @param section The parent {@link ConfigSection}
     * @param name Unqualified property name
     * @param value Initial value of the proeprty
     */
    ConfigProperty(ConfigSection section, String name, T value) {
        super(value);
        _section = section;
        _name = name;
    }

    public ConfigPropertyCellEditor getCellEditor() {
        return null;
    }

    /**
     * Gets the parent config section
     * @return The parent {@link ConfigSection} object
     */
    public final ConfigSection getSection() {
        return _section;
    }

    /**
     * Gets this property unqualified name
     * @return Unqualified name of this property
     */
    public final String getName() {
        return _name;
    }

    /**
     * Gets qualified unique name of this property
     * @return Qualified name of this property
     */
    public final String getFullName() {
        return _section == null ? _name : _section.getFullName() + "." + _name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ConfigProperty that = (ConfigProperty) obj;
        return (_name.equals(that._name) && _section == null ? that._section == null : _section.equals(that._section));
    }

    @Override
    public int hashCode() {
        int result = _section.hashCode();
        result = 31 * result + _name.hashCode();
        return result;
    }
}
