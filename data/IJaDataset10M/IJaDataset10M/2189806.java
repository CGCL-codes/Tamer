package baus.modell;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Eine Baugruppe ist ein aus Materialien (oder anderen Baugruppen)
 * zusammengesetzter Verbund von Werkstücken.
 * @author das BAUS! team
 */
@SuppressWarnings("serial")
public class Baugruppe extends Werkstueck {

    /**
     * Eine Liste von Werkstücken aus denen die Baugruppe besteht.
     */
    private final ArrayList<Werkstueck> stuecke;

    /**
     * Erstellt eine neue Baugruppe.
     * @param name Der Name dieser Baugruppe.
     * @param stuecke Die Werkstücke die diese Baugruppe ausmachen.
     */
    public Baugruppe(final String name, final ArrayList<Werkstueck> stuecke) {
        super(name);
        if (stuecke == null) {
            throw new IllegalArgumentException("Eine Baugruppe muss immer eine Stückliste enthalten !");
        }
        this.stuecke = new ArrayList<Werkstueck>(stuecke);
        Collections.sort(this.stuecke);
    }

    /**
     * Erstellt eine neue Baugruppe.
     * @param name Der Name dieser Baugruppe.
     * @param notiz Eine Notiz zu dieser Baugruppe
     * @param stuecke Die Werkstücke die diese Baugruppe ausmachen.
     */
    public Baugruppe(final String name, final String notiz, final ArrayList<Werkstueck> stuecke) {
        this(name, stuecke);
        setNotiz(notiz);
    }

    /**
     * Liefert alle Werkstücke aus denen sich diese Baugruppe zusammensetzt.
     * @return alle Bestandteile dieser Baugruppe
     */
    public ArrayList<Werkstueck> getStuecke() {
        return new ArrayList<Werkstueck>(stuecke);
    }

    /**
     * Berechnet das (Roh-)Volumen dieser Baugruppe aus den Volumen all ihrer
     * Stücke.
     * @return Das Volumen dieses Baugruppe in mm³.
     */
    @Override
    public final int getVolumen() {
        int result = 0;
        for (final Werkstueck stueck : stuecke) {
            result += stueck.getVolumen();
        }
        return result;
    }

    /**
     * Erzeugt eine String Representation von dieser Baugruppe.
     * @return Die String Representation dieser Baugruppe.
     */
    @Override
    public final String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("\n-->\nBaugruppe:\n  Name: ").append(getName());
        buffer.append("\n  Notiz: ").append(getNotiz());
        buffer.append("\n  Stücke:\n").append(stuecke);
        buffer.append("\n<--\n");
        return buffer.toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + stuecke.hashCode();
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Baugruppe other = (Baugruppe) obj;
        if (!stuecke.equals(other.stuecke)) {
            return false;
        }
        return true;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public final int compareTo(final Werkstueck obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        if (equals(obj)) {
            return 0;
        }
        if (obj instanceof Material) {
            return -1;
        }
        final Baugruppe that = (Baugruppe) obj;
        if (stuecke.size() > that.stuecke.size()) {
            return 1;
        }
        if (stuecke.size() < that.stuecke.size()) {
            return -1;
        }
        for (int i = 0; i < stuecke.size(); i++) {
            if (stuecke.get(i).compareTo(that.stuecke.get(i)) > 0) {
                return 1;
            }
            if (stuecke.get(i).compareTo(that.stuecke.get(i)) < 0) {
                return -1;
            }
        }
        throw new IllegalStateException("Sollte unerreichbar sein!");
    }
}
