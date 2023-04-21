package net.sf.dsaman.model.relation;

/** Diese Relation wird als Nachricht an den Charakter verwendet und fordert
 * diesen auf seine Relationen neu zu verteilen.
 *
 * @author Tilmann Kuhn
 */
public class RelationenNeuzuweisung implements Relation {

    /** Erzeugt eine neue RelationenNeuzuweisung */
    public RelationenNeuzuweisung() {
    }

    /** Gibt zur�ck ob dieses Objekt einer gegebenen Relation-Klasse entspricht.
     * @param relation die Relation auf die �berpr�ft werden soll
     * @return true wenn dieses Objekt der gegebenen Relation entspricht
     */
    public boolean istRelationVomTyp(Class relation) {
        return relation.isInstance(this);
    }

    /** Gibt die Id des Ziel-Features zur�ck
     * @return die Id des Ziel-Features
     */
    public String getZielId() {
        return null;
    }
}
