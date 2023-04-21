package de.acyrance.nb.sqlgenerator.Fields;

/**
 *
 * @author Yves Hoppe <info at yves-hoppe.de>
 */
public class FieldObj {

    private int _id;

    private String _name;

    private String _modifier;

    private String _modifier2;

    private String _modifier3;

    private String _kind;

    private String _sqlType = null;

    private String _default = null;

    private int _notNull = -1;

    private boolean _autoIncrement = false;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getKind() {
        return _kind;
    }

    public void setKind(String _kind) {
        this._kind = _kind;
    }

    public String getModifier() {
        return _modifier;
    }

    public void setModifier(String _modifier) {
        this._modifier = _modifier;
    }

    public String getModifier2() {
        return _modifier2;
    }

    public void setModifier2(String _modifier2) {
        this._modifier2 = _modifier2;
    }

    public String getModifier3() {
        return _modifier3;
    }

    public void setModifier3(String _modifier3) {
        this._modifier3 = _modifier3;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getSqlType() {
        return _sqlType;
    }

    public void setSqlType(String _sqlType) {
        this._sqlType = _sqlType;
    }

    public int getNotNull() {
        return _notNull;
    }

    public void setNotNull(int _notNull) {
        this._notNull = _notNull;
    }

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

    public boolean isAutoIncrement() {
        return _autoIncrement;
    }

    public void setAutoIncrement(boolean _autoIncrement) {
        this._autoIncrement = _autoIncrement;
    }

    @Override
    public String toString() {
        return _name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FieldObj other = (FieldObj) obj;
        if ((this._name == null) ? (other._name != null) : !this._name.equals(other._name)) {
            return false;
        }
        if ((this._modifier == null) ? (other._modifier != null) : !this._modifier.equals(other._modifier)) {
            return false;
        }
        if ((this._modifier2 == null) ? (other._modifier2 != null) : !this._modifier2.equals(other._modifier2)) {
            return false;
        }
        if ((this._modifier3 == null) ? (other._modifier3 != null) : !this._modifier3.equals(other._modifier3)) {
            return false;
        }
        if ((this._kind == null) ? (other._kind != null) : !this._kind.equals(other._kind)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this._name != null ? this._name.hashCode() : 0);
        hash = 83 * hash + (this._modifier != null ? this._modifier.hashCode() : 0);
        hash = 83 * hash + (this._modifier2 != null ? this._modifier2.hashCode() : 0);
        hash = 83 * hash + (this._modifier3 != null ? this._modifier3.hashCode() : 0);
        hash = 83 * hash + (this._kind != null ? this._kind.hashCode() : 0);
        return hash;
    }
}
