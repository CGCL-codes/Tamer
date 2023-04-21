package ca.kooki.codesnippet.core;

public class Identifier {

    private Class _type;

    private boolean _hasId;

    private String _id;

    public Identifier(Class type) {
        _type = type;
        _hasId = false;
        _id = "-1";
    }

    public boolean isOfficial() {
        return _hasId;
    }

    public String getId() {
        if (!_hasId) {
            throw new UnsupportedOperationException(_type + ":Id not set yet");
        }
        return _id;
    }

    public void setId(String newId) {
        if (_hasId && !_id.equals(newId)) {
            throw new UnsupportedOperationException(_type + ":Id already set to " + _id + " and cannot be set to " + newId);
        }
        _hasId = true;
        _id = newId;
    }

    public String toString() {
        return _hasId ? _id : "-";
    }

    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(Identifier.class)) {
            return false;
        }
        Identifier compareTo = (Identifier) obj;
        return this.toString().equals(compareTo.toString());
    }
}
