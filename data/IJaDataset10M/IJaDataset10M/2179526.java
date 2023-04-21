package jfun.yan.xml;

final class ReferentialId {

    private Object name;

    ReferentialId(Object name) {
        this.name = name;
    }

    public String toString() {
        return "" + name;
    }
}
