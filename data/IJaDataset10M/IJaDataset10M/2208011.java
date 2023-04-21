package edu.ufasta.webproxy.persistencia;

public class ObjectID {

    private Integer id;

    public ObjectID(Integer oid) {
        id = oid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int value) {
        this.id = value;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
