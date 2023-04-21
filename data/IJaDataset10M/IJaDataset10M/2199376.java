package net.sf.dsaman.model.regelwerk;

import net.sf.dsaman.data.RegelwerkReader;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import net.sf.dsaman.model.charakter.Charakter;

/**
 * Diese Klasse definiert DSA-Manager Regelwerke, die einfach alles enthalten,
 * was eine DSA-Welt ausmacht.
 *
 * @author Tilmann Kuhn
 */
public class Regelwerk {

    /** Hier kommen die Module her */
    private static RegelwerkReader regelwerkReader = null;

    /** Hier werden sie verwaltet */
    private static Map regelwerke = new HashMap();

    /** Speicher die Features dieses Moduls */
    private Map features = new HashMap();

    private String id = null;

    private String name = null;

    private String beschreibung = null;

    private Set noetigeRegelwerke = null;

    /** Erzeugt ein neues Regelwerk
     * @param noetigeRegelwerke die Regelwerke, die dieses Regelwerk ben�tigt
     * @param id die Id dieses Regelwerks
     * @param name der Name dieses Regelwerks
     * @param beschreibung die Beschreibung dieses Regelwerks
     */
    public Regelwerk(String id, String name, String beschreibung, Set noetigeRegelwerke) {
        this.id = id;
        this.name = name;
        this.beschreibung = beschreibung;
        this.noetigeRegelwerke = noetigeRegelwerke;
        regelwerke.put(id, this);
    }

    /** Initialisiert die statischen Felder dieses Regelwerks
     * @param regelwerkReader der RegelwerkReader, der dazu verwendet wird neue
     * Regelwerke zu laden
     */
    public static void initialisieren(RegelwerkReader regelwerkReader) {
        Regelwerk.regelwerkReader = regelwerkReader;
    }

    /** Gibt das Regelwerk mit der gegebenen Id zur�ck, falls existent
     * @param id die Id des verlangten Regelwerks
     * @return das verlangte Regelwerk
     * @throws RegelwerkLoadException wenn das Regelwerk nicht gefunden wurde
     */
    public static Regelwerk getRegelwerk(String id) throws RegelwerkLoadException {
        if (regelwerke.containsKey(id)) {
            return (Regelwerk) regelwerke.get(id);
        } else {
            if (regelwerkReader.getVorhandeneRegelwerke().contains(id)) {
                Regelwerk regelwerk = regelwerkReader.read(id);
                Iterator iter = regelwerk.getNoetigeRegelwerke().iterator();
                while (iter.hasNext()) {
                    try {
                        Regelwerk.getRegelwerk((String) iter.next());
                    } catch (RegelwerkLoadException e) {
                        regelwerke.remove(regelwerk.getId());
                        throw e;
                    }
                }
                return regelwerk;
            } else {
                throw new RegelwerkLoadException("Regelwerk " + id + " konnte nicht gefunden werden!");
            }
        }
    }

    /** Gibt eine Menge mit den Ids der vorhandenen Regelwerke zur�ck
     * @return eine Menge mit den Ids der vorhandenen Regelwerke
     */
    public static Set getVorhandeneRegelwerke() {
        return regelwerke.keySet();
    }

    /** Gibt alle RegelFeatures in diesem Regelwerk zur�ck
     * @return eine Map mit allen RegelFeatures in diesem Regelwerk
     */
    public Map getFeatures() {
        return features;
    }

    /** Gibt alle RegelFeatures eines bestimmten Klassen-Typs zur�ck
     * @param typ der verlangte Klassen-Typ
     * @return eine Map mit allen RegelFeatures des verlangten Typs
     */
    public Map getFeaturesVomTyp(Class typ) {
        Map feats = new HashMap();
        Iterator iter = features.values().iterator();
        while (iter.hasNext()) {
            RegelFeature feat = (RegelFeature) iter.next();
            if (typ.isInstance(feat)) feats.put(feat.getId(), feat);
        }
        return feats;
    }

    /** Gibt alle top-level Gebiete in diesem Regelwerk zur�ck.
     * @return alle RegelFeatureGebiete, die kein �bergebiet haben
     */
    public Map getUnterGebiete() {
        Map feats = new HashMap();
        Iterator iter = features.values().iterator();
        while (iter.hasNext()) {
            RegelFeature feat = (RegelFeature) iter.next();
            if ((feat instanceof RegelFeatureGebiet) && feat.getUeberGebiet() == null) {
                feats.put(feat.getId(), feat);
            }
        }
        return feats;
    }

    /** Gibt ein einzelnes Feature mit der gegebenen Id zur�ck
     * @param id die Id des verlangen Features
     * @return das Feature mit der Id oder null wenn es nicht vorhanden ist
     */
    public RegelFeature getFeature(String id) {
        return (RegelFeature) features.get(id);
    }

    /** L�scht das Feature mit der gegebenen id aus diesem Regelwerk
     * @param id die Id des Features, das gel�scht werden soll
     */
    void removeFeature(String id) {
        features.remove(id);
    }

    /** F�gt ein neues Feature in dieses Regelwerk ein
     * @param feature das Feature, das eingef�gt werden soll
     */
    void addFeature(RegelFeature feature) {
        if (feature.getRegelwerk() == this) {
            features.put(feature.getId(), feature);
        } else {
        }
    }

    /** Gibt die Id dieses Regelwerks zur�ck
     * @return die Id dieses Regelwerks
     */
    public String getId() {
        return id;
    }

    /** Gibt den Namen dieses Regelwerks zur�ck
     * @return der Name dieses Regelwerks
     */
    public String getName() {
        return name;
    }

    /** Gibt die Beschreibung dieses Regelwerks zur�ck
     * @return die Beschreibung dieses Regelwerks
     */
    public String getBeschreibung() {
        return beschreibung;
    }

    /** �ndert die Beschreibung dieses Regelwerks
     * @param beschreibung die neue Beschreibung f�r dieses Regelwerk
     */
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    /** Gibt die Ids aller von diesem Regelwerk ben�tigten Regelwerke zur�ck
     * @return ein Set mit den Ids aller ben�tigter Regelwerke
     */
    public Set getNoetigeRegelwerke() {
        return noetigeRegelwerke;
    }

    /** �ndert die von diesem Regelwerk ben�tigten Regelwerke
     * @param noetigeRegelwerke ein Set mit der neuen Liste an Ids ben�tigter
     * Regelwerke
     */
    public void setNoetigeRegelwerke(Set noetigeRegelwerke) {
        this.noetigeRegelwerke = noetigeRegelwerke;
    }

    /** Gibt dieses Objekt als String zur�ck
     * @return der Stringwert dieses Objekts
     */
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("Regelwerk ========================================\nName: ").append(name).append("\nId: ").append(id).append("\nBeschreibung: ").append(beschreibung).append("\n");
        sbuf.append(gebietBaum());
        sbuf.append(featureListe());
        return sbuf.append("\n=================================================\n\n").toString();
    }

    /** Gibt eine Liste mit allen aktuell geladenen Regelwerken aus
     * @return ein String der die Liste enth�lt
     */
    public static String regelwerkListe() {
        Iterator iter = regelwerke.values().iterator();
        StringBuffer sbuf = new StringBuffer("Geladene Regelwerke:\n");
        while (iter.hasNext()) {
            Regelwerk regelwerk = (Regelwerk) iter.next();
            sbuf.append("o  Name: ").append(regelwerk.getName()).append("\n   Id: ").append(regelwerk.getId()).append("\n   Beschreibung: ").append(regelwerk.getBeschreibung()).append("\n");
        }
        return sbuf.toString();
    }

    private String gebietBaumTeil(RegelFeatureGebiet rfg, int tiefe) {
        StringBuffer prebuf = new StringBuffer();
        for (int i = 0; i < tiefe; i++) prebuf.append("   ");
        String prefix = prebuf.toString();
        StringBuffer outbuf = new StringBuffer();
        Iterator iter = rfg.getUnterGebiete().values().iterator();
        while (iter.hasNext()) {
            RegelFeatureGebiet unterrfg = (RegelFeatureGebiet) iter.next();
            outbuf.append(prefix).append(unterrfg.getName()).append("\n").append(gebietBaumTeil(unterrfg, tiefe + 1));
        }
        iter = rfg.getFeatures().values().iterator();
        while (iter.hasNext()) {
            RegelFeature rf = (RegelFeature) iter.next();
            outbuf.append(prefix).append("o ").append(rf.getName()).append("\n");
        }
        return outbuf.toString();
    }

    /** Gibt eine Baum-Struktur mit allen Gebieten und Features dieses Regelwerkes
     * aus.
     * @return ein String mit der Baum-Struktur
     */
    public String gebietBaum() {
        Map gebiete = getUnterGebiete();
        Iterator iter = gebiete.values().iterator();
        StringBuffer sbuf = new StringBuffer("Gebiete und Features im Regelwerk ").append(getName()).append(":\n");
        while (iter.hasNext()) {
            RegelFeatureGebiet rfg = (RegelFeatureGebiet) iter.next();
            sbuf.append(rfg.getName()).append("\n").append(gebietBaumTeil(rfg, 1));
        }
        return sbuf.toString();
    }

    /** Gibt eine Liste mit den Details aller Features in diesem Regelwerk aus
     * @return ein String mit der Liste
     */
    public String featureListe() {
        StringBuffer sbuf = new StringBuffer("Gebiete und Features im Regelwerk ").append(getName()).append(":\n");
        Iterator iter = features.values().iterator();
        while (iter.hasNext()) {
            RegelFeature feature = (RegelFeature) iter.next();
            if (feature instanceof Charakter) {
                sbuf.append("Charakter: --------------------------\nName: ").append(feature.getName()).append("\n-------------------------------------\n");
            } else {
                sbuf.append(feature.toString());
            }
        }
        return sbuf.toString();
    }
}
