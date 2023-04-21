package net.javaseminar;

public class Mathe {

    public boolean tuWas(double d) {
        return false;
    }

    public boolean tuWas(Integer i) {
        return true;
    }

    public boolean zweiBoolescheVariablen(Boolean b1, Boolean b2) {
        boolean variante1 = b1.booleanValue() == b2.booleanValue();
        boolean variante2 = b1.equals(b2);
        return b1 == b2;
    }
}
