package org.fao.geonet.kernel.thesaurus.skos;

import java.util.*;
import org.jdom.Element;

/**
 * A class for a SKOS  concept identified by either a URI or an
 * external non-URI identifier (descriptor). These are usually
 * related to some particular thesaurus.
 *
 * FIXME : scopeNote could be multilingual (not used in metadata)
 *
 * @author fxprunayre
 *
 */
public class Concept {

    public static final String DEFAULT_LANG = "eng";

    public static final String NS_GML = "http://www.opengis.net/gml#";

    public static final String GML_BOUNDED_BY = "BoundedBy";

    public static final String GML_ENVELOPE = "Envelope";

    public static final String GML_SRS_NAME = "srsName";

    public static final String URI_EPSG_4326 = "http://www.opengis.net/gml/srs/epsg.xml#epsg:4326";

    public static final String GML_LOWER_CORNER = "lowerCorner";

    public static final String GML_UPPER_CORNER = "upperCorner";

    public static final String NS_SKOS = "http://www.w3.org/2004/02/skos/core#";

    public static final String SKOS_CONCEPT = "Concept";

    public static final String SKOS_PREF_LABEL = "prefLabel";

    public static final String SKOS_SCOPE_NOTE = "scopeNote";

    private int id;

    private final HashMap<String, String> prefLabel;

    private String scopeNote;

    private String definition;

    private String rdf_about;

    private String gml_coordEast;

    private String gml_coordWest;

    private String gml_coordSouth;

    private String gml_coordNorth;

    private String thesaurus;

    private boolean selected;

    private String defaultLang = DEFAULT_LANG;

    /**
	 * @param id
	 * @param value
	 * @param definition
	 * @param code
	 * @param coordEast
	 * @param coordWest
	 * @param coordSouth
	 * @param coordNorth
	 * @param thesaurus
	 * @param selected
	 */
    public Concept(int id, String value, String definition, String code, String coordEast, String coordWest, String coordSouth, String coordNorth, String thesaurus, boolean selected, String lang) {
        super();
        this.id = id;
        if (lang != null) {
            defaultLang = lang;
        }
        this.prefLabel = new HashMap<String, String>();
        this.prefLabel.put(lang, value);
        this.scopeNote = definition;
        this.rdf_about = code;
        this.gml_coordEast = coordEast;
        this.gml_coordWest = coordWest;
        this.gml_coordSouth = coordSouth;
        this.gml_coordNorth = coordNorth;
        this.thesaurus = thesaurus;
        this.selected = selected;
    }

    /**
	 * @param id
	 * @param value
	 * @param definition
	 * @param thesaurus
	 * @param selected
	 * @param lang TODO
	 */
    public Concept(int id, String value, String definition, String thesaurus, boolean selected, String lang) {
        super();
        if (lang != null) {
            defaultLang = lang;
        }
        this.id = id;
        this.prefLabel = new HashMap<String, String>();
        this.prefLabel.put(lang, value);
        this.scopeNote = definition;
        this.thesaurus = thesaurus;
        this.selected = selected;
    }

    /**
	 * @param value
	 * @param definition
	 * @param thesaurus
	 * @param selected
	 * @param lang TODO
	 */
    public Concept(String value, String definition, String thesaurus, boolean selected, String lang) {
        super();
        if (lang != null) {
            defaultLang = lang;
        }
        this.prefLabel = new HashMap<String, String>();
        this.prefLabel.put(lang, value);
        this.scopeNote = definition;
        this.thesaurus = thesaurus;
        this.selected = selected;
    }

    public String getScopeNote() {
        return scopeNote;
    }

    public void setScopeNote(String scopeNote) {
        this.scopeNote = scopeNote;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getThesaurus() {
        return thesaurus;
    }

    public void setDefaultLang(String defaultLang) {
        this.defaultLang = defaultLang;
    }

    public void setThesaurus(String thesaurus) {
        this.thesaurus = thesaurus;
    }

    public Set<String> getLanguages() {
        return prefLabel.keySet();
    }

    public String getPrefLabel(String lang) {
        return prefLabel.get(lang);
    }

    public void setPrefLabel(String value, String lang) {
        this.prefLabel.put(lang, value);
    }

    /**
	 * Return prefLabel in default language or the first
	 * prefLabel of the keyword.
	 *
	 * @return
	 */
    public String getDefaultPrefLabel() {
        if (prefLabel.containsKey(defaultLang)) return prefLabel.get(defaultLang); else {
            Iterator iter = prefLabel.entrySet().iterator();
            Map.Entry<String, String> e = (Map.Entry<String, String>) iter.next();
            return e.getValue();
        }
    }

    /**
     * Return locale name
     *
     * @return
     */
    public String getDefaultLocale() {
        if (prefLabel.containsKey(defaultLang)) return defaultLang; else {
            Iterator iter = prefLabel.entrySet().iterator();
            Map.Entry<String, String> e = (Map.Entry<String, String>) iter.next();
            if (!e.getKey().matches("\\w\\w+")) {
                return defaultLang;
            }
            return e.getKey();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
	 * return the URI of the keyword concept
	 */
    public String getCode() {
        return rdf_about;
    }

    /**
	 * If no #id element relative code is URL
	 * with no namespace.
	 *
	 * @return
	 */
    public String getRelativeCode() {
        if (rdf_about.contains("#")) return rdf_about.split("#")[1]; else return rdf_about;
    }

    /**
	 * Namespace is only defined if code pattern is
	 * using ID. For example, GEMET is not using it.
	 *
	 * @return
	 */
    public String getNameSpaceCode() {
        if (rdf_about.contains("#")) return rdf_about.split("#")[0] + "#"; else return "";
    }

    public void setCode(String code) {
        this.rdf_about = code;
    }

    public String getCoordEast() {
        return gml_coordEast;
    }

    public void setCoordEast(String coordEast) {
        this.gml_coordEast = coordEast;
    }

    public String getCoordNorth() {
        return gml_coordNorth;
    }

    public void setCoordNorth(String coordNorth) {
        this.gml_coordNorth = coordNorth;
    }

    public String getCoordWest() {
        return gml_coordWest;
    }

    public void setCoordWest(String coordWest) {
        this.gml_coordWest = coordWest;
    }

    public String getCoordSouth() {
        return gml_coordSouth;
    }

    public void setCoordSouth(String coordSouth) {
        this.gml_coordSouth = coordSouth;
    }

    /**
	 * Create a xml node for the current Keyword
	 *
	 * @return
	 */
    public Element toElement(String... langs) {
        List<String> prioritizedList = Arrays.asList(langs);
        TreeSet<String> languages = new TreeSet<String>(new PrioritizedLangComparator(defaultLang, prioritizedList));
        languages.addAll(prefLabel.keySet());
        Element elKeyword = new Element("keyword");
        Element elId = new Element("id");
        elId.addContent(Integer.toString(this.getId()));
        Element elCode = new Element("code");
        String code = this.getRelativeCode();
        Element elSelected = new Element("selected");
        if (this.isSelected()) {
            elSelected.addContent("true");
        } else {
            elSelected.addContent("false");
        }
        elKeyword.addContent(elId);
        elKeyword.addContent(elCode);
        for (String language : languages) {
            Element elValue = new Element("value");
            elValue.addContent(prefLabel.get(language));
            elValue.setAttribute("lang", language.toUpperCase());
            elKeyword.addContent(elValue);
        }
        Element elDefiniton = new Element("definition");
        elDefiniton.addContent(this.getScopeNote());
        Element elUri = new Element("uri");
        elUri.addContent(this.getCode());
        String thesaurusType = this.getThesaurus();
        thesaurusType = thesaurusType.replace('.', '-');
        if (thesaurusType.contains("-")) thesaurusType = thesaurusType.split("-")[1];
        elKeyword.setAttribute("type", thesaurusType);
        Element elthesaurus = new Element("thesaurus").setText(this.getThesaurus());
        if (this.getCoordEast() != null && this.getCoordWest() != null && this.getCoordSouth() != null && this.getCoordNorth() != null) {
            Element elBbox = new Element("geo");
            Element elEast = new Element("east");
            elEast.addContent(this.getCoordEast());
            Element elWest = new Element("west");
            elWest.addContent(this.getCoordWest());
            Element elSouth = new Element("south");
            elSouth.addContent(this.getCoordSouth());
            Element elNorth = new Element("north");
            elNorth.addContent(this.getCoordNorth());
            elBbox.addContent(elEast);
            elBbox.addContent(elWest);
            elBbox.addContent(elSouth);
            elBbox.addContent(elNorth);
            elKeyword.addContent(elBbox);
        }
        elKeyword.addContent(elthesaurus);
        elKeyword.addContent(elDefiniton);
        elKeyword.addContent(elSelected);
        elKeyword.addContent(elUri);
        return elKeyword;
    }

    /**
	 *
	 * @return a skos representation of the concept
	 */
    public Element toSkosConcept() {
        return null;
    }

    private class PrioritizedLangComparator implements Comparator<String> {

        private String defaultLang;

        private List<String> prioritizedList;

        public PrioritizedLangComparator(String defaultLang, List<String> prioritizedList) {
            this.defaultLang = defaultLang;
            this.prioritizedList = prioritizedList;
        }

        @Override
        public int compare(String lang1, String lang2) {
            int value = val(lang2) - val(lang1);
            if (value == 0) {
                return lang1.compareToIgnoreCase(lang2);
            }
            return value;
        }

        private int val(String lang) {
            if (lang.equalsIgnoreCase(defaultLang)) {
                return 1000;
            }
            int index = prioritizedList.indexOf(lang);
            if (index > 0) {
                return prioritizedList.size() - index;
            }
            return -1;
        }
    }
}
