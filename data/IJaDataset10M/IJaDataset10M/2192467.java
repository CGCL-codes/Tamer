package jaga.external.bucanon;

class Atom implements Cloneable, java.io.Serializable {

    private String identifier;

    Atom(String s) {
        identifier = s;
    }

    String identifier() {
        return identifier;
    }

    int compareTo(Atom a) {
        if (identifier.length() < a.identifier.length()) {
            return -2;
        } else if (identifier.length() > a.identifier.length()) {
            return 2;
        } else {
            int n = identifier.compareTo(a.identifier);
            if (n < 0) {
                return -1;
            } else if (n > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public String toString() {
        return identifier;
    }

    Atom copy() {
        return new Atom(identifier());
    }
}
