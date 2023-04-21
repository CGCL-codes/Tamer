package net.sf.dsaman.model.regelwerk;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import net.sf.dsaman.model.charakter.Charakter;
import net.sf.dsaman.model.charakter.PersonenCharakterFeature;
import net.sf.dsaman.model.charakter.PersonenRasse;
import java.util.Set;
import java.util.HashSet;

/**
 * Diese Klasse steht f�r die allgemeine Regelbeschreibung einer Rasse.
 *
 * @author Tilmann Kuhn
 */
public class RegelRasse extends BasisRegelCharakterFeature {

    private double minGroesse = 0;

    private double maxGroesse = 0;

    private List modifikatorAuswahlen = null;

    /** Erzeugt eine neue RegelRasse und f�gt diese dem �bergebenen Gebiet und
     * dessen Regelwerk hinzu.
     *
     * @param gebiet das RegelCharakterFeatureGebiet zu dem dieses Feature geh�rt
     * @param id die Id dieses Features
     * @param name der Name dieses Features
     * @param beschreibung die Beschreibung dieses Features
     * @param minGroesse die Mindestgr��e eines Charakters dieser Rasse
     * @param maxGroesse die Maximalgr��e eines Charakters dieser Rasse
     * @param ausgehendeRelationen die von diesem Feature ausgehenden Relationen
     * @param modifikatorAuswahlen eine Liste von RegelModifikatorAuswahlen,
     *        die vom Spieler ausw�hlbar sind
     * @param vorteile die Vor- und Nachteile, die dieses Feature bietet
     * @param empfohleneVorteile die empfohlenen Vor- und Nachteile,
     *        die zu diesem Feature geeignet sind
     * @param verboteneVorteile die Vorteile, die mit dieser Rasse nicht vereinbar sind
     *        
     */
    public RegelRasse(RegelCharakterFeatureGebiet gebiet, String id, String name, String beschreibung, double minGroesse, double maxGroesse, List modifikatorAuswahlen, List ausgehendeRelationen) {
        super(gebiet, id, name, beschreibung, ausgehendeRelationen);
        this.maxGroesse = maxGroesse;
        this.minGroesse = minGroesse;
        this.modifikatorAuswahlen = (modifikatorAuswahlen == null) ? new ArrayList() : modifikatorAuswahlen;
    }

    /** Liefert die Mindestgr��e eines Charakters dieser Rasse
     * @return die Mindestgr��e eines Charakters dieser Rasse
     */
    public double getMinGroesse() {
        return minGroesse;
    }

    /** Liefert die Maximalgr��e eines Charakters dieser Rasse
     * @return die Maximalgr��e eines Charakters dieser Rasse
     */
    public double getMaxGroesse() {
        return maxGroesse;
    }

    /** Gibt die RegelModifikatorAuswahlen zur�ck
     * @return eine Liste mit den RegelModifikatorAuswahlen
     */
    public List getModifikatorAuswahlen() {
        return modifikatorAuswahlen;
    }

    /** Gibt dieses Objekt als String zur�ck
     * @return der Stringwert dieses Objekts
     */
    public String toString() {
        StringBuffer sbuf = new StringBuffer("RegelRasse: -----------------------\n").append(super.toString()).append("Modifikator-Auswahlen\n");
        Iterator iter = modifikatorAuswahlen.iterator();
        while (iter.hasNext()) {
            sbuf.append(iter.next().toString());
        }
        sbuf.append("Gr��e: ").append(minGroesse).append(" bis ").append(maxGroesse);
        return sbuf.append("\n-----------------------------------\n").toString();
    }

    /** Erzeugt eine neue Instanz eines PersonenCharakterFeatures, die dieses
     * CharakterFeature referenziert und tr�gt diese in den �bergebenen Charakter ein
     * @return eine Referenz auf das neu erzeugte PersonenCharakterFeature
     * @param charakter der Charakter f�r den das neue Feature erzeugt werden soll
     */
    public PersonenCharakterFeature erzeugeNeuesPersonenFeature(Charakter charakter) {
        return new PersonenRasse(charakter, this);
    }
}
