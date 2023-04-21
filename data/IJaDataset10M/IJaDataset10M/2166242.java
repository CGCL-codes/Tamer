package br.cin.ufpe.corba.namingservice;

import java.io.Serializable;

public class NameComponent implements Serializable {

    private static final long serialVersionUID = 3128223874900250731L;

    private String id;

    private String kind;

    public NameComponent(String string) {
        String[] parts = string.split(".");
        if (parts.length == 0 || parts.length == 1) {
            this.id = string;
        } else {
            if (parts.length != 2) {
                throw new InvalidName();
            }
            this.id = parts[0];
            this.kind = parts[1];
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        String result = id;
        if (kind != null) {
            result = "." + kind;
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NameComponent) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
