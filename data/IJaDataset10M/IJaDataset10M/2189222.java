package net.sf.dsaman.model.charakter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.dsaman.model.regelwerk.RegelKultur;
import net.sf.dsaman.model.arithmetic.ArithmeticExpression;
import net.sf.dsaman.model.relation.RelationAkzeptor;
import net.sf.dsaman.model.relation.RelationQuelle;
import net.sf.dsaman.model.relation.Relation;
import java.util.ArrayList;
import net.sf.dsaman.model.regelwerk.RegelModifikatorAuswahl;
import net.sf.dsaman.model.relation.PersonenRelationFactory;

/** Diese Klasse definiert die Charakter-Sicht auf Kulturen.
 *
 * @author Tilmann Kuhn

 */
public class PersonenKultur extends BasisPersonenCharakterFeature implements PersonenCharakterFeature, ArithmeticExpression, RelationAkzeptor, RelationQuelle {

    /** Erzeugt eine neue PersonenKultur und tr�gt diese in den �ber-
     * gebenen Charakter ein
     *
     * @param pcfGebiet das PersonenCharakterFeatureGebiet zu dem dieses Feature
     * geh�rt
     * @param referenzId die Id des RegelCharakterFeatures, das gewrappt werden
     * soll
     * @param ausgehendeRelationen eine List mit den von diesem Feature ausgehenden
     * Relationen
     */
    public PersonenKultur(PersonenCharakterFeatureGebiet pcfGebiet, String referenzId, List ausgehendeRelationen) {
        super(pcfGebiet, referenzId, ausgehendeRelationen);
    }

    /** Erzuegt eine neue PersonenKultur, tr�gt sie in das zugeh�rige
     * Gebiet und den �bergebenen Charakter ein und personaliesiert sie.
     *
     * @param charakter der Charakter, zu dem dieses Feature geh�ren soll
     * @param referenz die RegelEigenschaft, die gewrappt werden soll
     */
    public PersonenKultur(Charakter charakter, RegelKultur referenz) {
        super(charakter, referenz);
        List relationenKopie = new ArrayList();
        relationenKopie.addAll(referenz.getAusgehendeRelationen());
        setAusgehendeRelationen(relationenKopie);
    }

    /** Gibt die Generierungskosten zur�ck
     * @return die Generierungskosten
     */
    public double getKosten() {
        return ((RegelKultur) referenz).getKosten();
    }

    /** Gibt eine Liste mit allen PersonenModifikatorAuswahlen dieses Features zur�ck.
     * @return eine Liste mit allen PersonenModifikatorAuswahlen dieses Features
     */
    public List getModifikatorAuswahlen() {
        List list = new ArrayList();
        Iterator iter = ((RegelKultur) referenz).getModifikatorAuswahlen().iterator();
        while (iter.hasNext()) {
            list.add(new PersonenModifikatorAuswahl(this, (RegelModifikatorAuswahl) iter.next()));
        }
        return list;
    }

    /** Wird aufgerufen, um die ausgehenden Relationen zu personalisieren
     */
    private void relationenPersonalisieren() {
        personalisierteRelationen = new ArrayList();
        if (getAusgehendeRelationen() != null) {
            Iterator iter = getAusgehendeRelationen().iterator();
            while (iter.hasNext()) {
                personalisierteRelationen.add(((PersonenRelationFactory) iter.next()).erzeugePersonenRelation(charakter));
            }
        }
    }

    /** Gibt dieses Objekt als String zur�ck
     * @return der Stringwert dieses Objekts
     */
    public String toString() {
        StringBuffer sbuf = new StringBuffer("PersonenKultur: -------------------\n").append(super.toString()).append("Kosten: ").append(getKosten()).append("\nMoegliche Professionen:\n");
        return sbuf.append("-----------------------------------\n").toString();
    }

    /** Gibt den Wert des geforderten Attributs zur�ck
     * @param property der Name des Attributs
     * @throws ArithmeticException wenn ein Fehler im Ausdruck aufgetreten ist
     * z.B. Division durch 0
     * @throws NullPointerException wenn ein Item oder Attribut nicht gefunden
     * wurde
     * @return der Wert des Attibuts
     */
    protected double resolveMe(String property) throws ArithmeticException, NullPointerException {
        throw new NullPointerException("Property " + getId() + "." + property + " existiert nicht!");
    }
}
