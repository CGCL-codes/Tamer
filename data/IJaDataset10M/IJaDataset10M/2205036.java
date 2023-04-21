package manager.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import manager.util.XMLString;
import manager.exceptions.UnexpectedElementException;
import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * Stellt ein Attribut da, welches alle Auspr�gungen von einem Metadaten-Element in allen Metadatendateien beinhaltet.
 * Zu jeder vorhandenen Auspr�gung/Stichwort/Headword werden alle Metadatendateien gespeichert, die diese Auspr�gung f�r das Attribut besitzen.
 *
 * @author Soenke Brummerloh
 */
public class Attribute {

    private static Logger logger = Logger.getLogger("");

    private String attributeName;

    private HashMap<String, Headword> headwordMap = new HashMap<String, Headword>();

    /**
     * Initialisiert ein neues leeres Attribut mit dem eindeutigen Pfade den das Element in einer Metadatendatei besitzt.
     *
     * @param attributName der Pfad innerhalb der XML-Datei (Pfadelemente werden durch Punkte voneinander getrennt)
     */
    public Attribute(String attributName) {
        this.attributeName = attributName;
    }

    /**
     * Initialisiert ein neues Attribute mit dem eindeutigen Pfade den das Element in einer Metadatendatei besitzt und
     * speichert die erste Auspr�gung.
     *
     * @param attributeName der Pfad innerhalb der XML-Datei (Pfadelemente werden durch Punkte voneinander getrennt)
     * @param headword      die zu speichernde Auspr�gung
     * @param file          die Metadatendatei in der die Auspr�gung vorkommt
     */
    public Attribute(String attributeName, String headword, MetadataFile file) {
        this(attributeName);
        this.store(headword, file);
    }

    /**
     * Speichert eine Auspr�gung mit zugeh�riger Datei
     *
     * @param headwordName dier Auspr�gung
     * @param file         die Datei
     */
    public synchronized void store(String headwordName, MetadataFile file) {
        Headword headword = headwordMap.get(headwordName);
        if (headword == null) {
            headword = new Headword(headwordName, file);
            headwordMap.put(headwordName, headword);
        } else {
            headword.store(file);
        }
    }

    /**
     * Liefert das letzte Pfadelement vom Attributnamen.
     *
     * @return der Attributname ohne restlichem Pfad
     */
    public String getShortAttributeName() {
        int lastIndex = attributeName.lastIndexOf(".");
        return attributeName.substring(lastIndex + 1);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setPath(String path) {
        this.attributeName = path;
    }

    /**
     * @return alle gespeicherten Auspr�gungen in einer HashMap
     */
    public HashMap<String, Headword> getHeadwordMap() {
        return headwordMap;
    }

    /**
     * Liefert die topNumber h�ufigsten Headwords.
     *
     * @param topNumber wie viele Auspr�gungen sollen maximal geliefert werden
     * @param sorted    gibt an, ob die Auspr�gungen alphabetisch sortiert werden sollen
     * @return die h�ufigsten Auspr�gungen
     */
    public ArrayList<Headword> getTopHeadwords(int topNumber, boolean sorted) {
        logger.debug("");
        if (topNumber <= 0) {
            return new ArrayList<Headword>();
        }
        Set<String> keySet = headwordMap.keySet();
        String[] keys = new String[keySet.size()];
        keys = keySet.toArray(keys);
        if (keys.length < topNumber) {
            topNumber = keys.length;
        }
        ArrayList<Headword> top = new ArrayList<Headword>(topNumber);
        for (String key : keys) {
            insertHeadword(top, topNumber, headwordMap.get(key));
        }
        if (sorted) {
            ArrayList<Headword> topSorted = new ArrayList<Headword>(top.size());
            while (top.size() > 0) {
                Headword candidate = top.get(0);
                for (Headword aTop : top) {
                    if (aTop.getName().compareToIgnoreCase(candidate.getName()) < 0) {
                        candidate = aTop;
                    }
                }
                topSorted.add(candidate);
                top.remove(candidate);
            }
            return topSorted;
        } else {
            return top;
        }
    }

    /**
     * F�gt zu einer ArrayList ein Headword-Objekt zu, falls noch nicht maxSize Headword-Objekte in der Liste sind.
     * Falls bereits maxSize HeadWord-Objekte in der Liste sind, wird das neue Headword-Objekt gegen das Attribut ausgetauscht,
     * das am seltesten vorkommt (wenn das neue Objekt �fters vorkommt).
     *
     * @param list    die Liste mit bereits vorhandenen Attributen
     * @param maxSize die maximal erlaubte Anzahl an Headword-Objekten
     * @param word    das neue Headword-Objekt
     */
    private synchronized void insertHeadword(ArrayList<Headword> list, int maxSize, Headword word) {
        logger.debug("");
        if (list.size() < maxSize) {
            list.add(word);
            return;
        }
        int minNumber = list.get(0).getNumber();
        int minIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            int newNumber = word.getNumber();
            int oldNumber = list.get(i).getNumber();
            if (list.get(i) != null && newNumber > oldNumber && (minNumber > oldNumber || minIndex == -1)) {
                minNumber = list.get(i).getNumber();
                minIndex = i;
            } else if (list.get(i) == null) {
                minIndex = i;
                break;
            }
        }
        if (minIndex >= 0) {
            list.set(minIndex, word);
        }
    }

    /**
     * Entfernt die Referenzen auf die �bergebene Metadatendatei.
     *
     * @param metadataFile die zu entfernende Metadatenreferenz
     */
    public synchronized void remove(MetadataFile metadataFile) {
        Set<String> keySet = headwordMap.keySet();
        String[] keys = new String[keySet.size()];
        keys = keySet.toArray(keys);
        for (String key : keys) {
            headwordMap.get(key).remove(metadataFile);
            if (headwordMap.get(key).getNumber() <= 0) {
                headwordMap.remove(key);
            }
        }
    }

    /**
     * Wandelt das Attribute in seine XML-Repr�sentation um
     *
     * @return die XML-Repr�sentation
     */
    public XMLString toXML() {
        logger.debug("");
        XMLString xml = new XMLString("Attribute");
        xml.add("Name", attributeName);
        XMLString headwords = new XMLString("HeadwordList");
        Set<String> keySet = headwordMap.keySet();
        String[] keys = new String[keySet.size()];
        keys = keySet.toArray(keys);
        for (String key : keys) {
            headwords.add(headwordMap.get(key).toXML());
        }
        xml.add(headwords);
        return xml;
    }

    /**
     * Erzeugt aus der XML-Repr�sentation eines Attributes ein neues Attribute-Objekt
     *
     * @param element das zu parsende XML-Element
     * @return ein neues Attribute-Objekt
     * @throws manager.exceptions.UnexpectedElementException wird geworfen, falls das Element-Objekt unerwartete Tags enth�lt
     */
    public static Attribute parseXML(Element element) throws UnexpectedElementException {
        if (!element.getName().equals("Attribute")) {
            throw new UnexpectedElementException(element.getName() + " was found but 'Attribute' was expected!");
        }
        String name = element.getChildText("Name");
        Attribute attribute = new Attribute(name);
        Element headwordList = element.getChild("HeadwordList");
        List list = headwordList.getChildren();
        for (Object aList : list) {
            Headword headword = Headword.parseXML((Element) aList);
            attribute.headwordMap.put(headword.getName(), headword);
        }
        return attribute;
    }
}
