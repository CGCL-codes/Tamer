package de.mnit.basis.daten.struktur.gruppe;

/**
 * @author Michael Nitsche
 */
public class Gruppe1<TA> {

    private TA o1;

    protected Gruppe1(TA o1) {
        this.o1 = o1;
    }

    public static <TA> Gruppe1<TA> neu(TA o1) {
        return new Gruppe1<TA>(o1);
    }

    @SuppressWarnings("unchecked")
    public static <T1> Class<Gruppe1<T1>> gKlasse(Class<T1> c1) {
        return (Class<Gruppe1<T1>>) Gruppe1.neu(null).getClass();
    }

    public TA g1() {
        return o1;
    }

    public String toString() {
        return "Gruppe1(" + o1 + ")";
    }
}
