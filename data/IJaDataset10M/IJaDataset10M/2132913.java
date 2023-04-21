package org.fao.geonet.kernel.search;

import java.util.*;
import java.text.Collator;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Log;
import jeeves.utils.Util;
import org.fao.geonet.kernel.LocaleUtil;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.thesaurus.Thesaurus;
import org.fao.geonet.kernel.thesaurus.ThesaurusManager;
import org.fao.geonet.kernel.thesaurus.skos.Concept;
import org.fao.geonet.util.LangUtils;
import org.jdom.Element;
import org.openrdf.model.BNode;
import org.openrdf.model.Graph;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.sesame.query.QueryResultsTable;
import org.openrdf.sesame.sail.StatementIterator;

public class KeywordsSearcher {

    public static final String THESAURUS = "thesaurus";

    public static final String KEYWORD = "keyword";

    public static final String DATE_TYP_CD = "DateTypCd";

    public static final String REF_DATE_TYPE = "refDateType";

    public static final String REF_DATE = "refDate";

    public static final String RES_REF_DATE = "resRefDate";

    private static final String RES_TITLE = "resTitle";

    public static final String THESAURUS_NAME = "thesaName";

    public static final String VALUE = "value";

    public static final String KEY_TYP_CD = "KeyTypCd";

    public static final String KEY_TYP = "keyTyp";

    public static final String KEYWORD_TYPE = KEY_TYP;

    public static final String NBTOT = "nbtot";

    public static final String PID_KEYWORD = "pIdKeyword";

    public static final String NB_RESULTS = "nbResults";

    public static final String DESC_KEYS = "descKeys";

    private final ThesaurusManager _tm;

    private String _query;

    private String _lang;

    private String _sortBy = "label";

    private final String _sortOrder = "ascending";

    private ArrayList<Concept> _results = new ArrayList<Concept>();

    private final int _pTypeSearch = 1;

    private int _maxResults = Integer.MAX_VALUE;

    public KeywordsSearcher(ThesaurusManager tm) {
        _tm = tm;
    }

    /**
	 *  Get a concept by a known Id.
	 */
    public Concept getConcept(String id, String sThesaurusName) throws Exception {
        _query = "CONSTRUCT * " + " FROM {id} rdf:type {skos:Concept} " + " WHERE id LIKE \"" + id + "\" IGNORE CASE " + " USING NAMESPACE skos=<" + Concept.NS_SKOS + ">, gml=<" + Concept.NS_GML + "> ";
        Thesaurus thesaurus = _tm.getThesaurusByName(sThesaurusName);
        Log.debug(Geonet.THESAURUS_MAN, "GetConcept in " + thesaurus.getFile() + ":" + _query);
        Graph c = thesaurus.performGraphRequest(_query);
        StatementIterator s = c.getStatements();
        while (s.hasNext()) {
            Statement st = s.next();
            if (st.getObject() instanceof BNode) {
                BNode node = (BNode) st.getObject();
            }
        }
        return null;
    }

    public Concept searchById(String id, String sThesaurusName, String lang, String locales) throws Exception {
        if (lang.length() > 2) lang = lang.substring(0, 2);
        _query = "SELECT prefLab, note, id, lowc, uppc, lang(prefLab) " + " FROM {id} rdf:type {skos:Concept}; " + " skos:prefLabel {prefLab};" + " [skos:scopeNote {note} WHERE lang(note) LIKE \"*\"]; " + " [gml:BoundedBy {} gml:lowerCorner {lowc}]; " + " [gml:BoundedBy {} gml:upperCorner {uppc}] " + " WHERE lang(prefLab) LIKE \"*\" " + " AND id LIKE \"" + id + "\" " + " IGNORE CASE " + " USING NAMESPACE skos=<" + Concept.NS_SKOS + ">, gml=<" + Concept.NS_GML + "> ";
        Thesaurus thesaurus = _tm.getThesaurusByName(sThesaurusName);
        QueryResultsTable resultsTable = thesaurus.performRequest(_query);
        int rowCount = resultsTable.getRowCount();
        if (rowCount == 0) {
            return null;
        } else {
            Concept kb = null;
            for (int row = 0; row < rowCount; row++) {
                Concept newKb = createConcept(lang, 0, sThesaurusName, resultsTable, row);
                if (kb == null) {
                    kb = newKb;
                } else {
                    kb.setPrefLabel(newKb.getDefaultPrefLabel(), newKb.getDefaultLocale());
                }
            }
            return kb;
        }
    }

    /**
	 * If defaultLangCode is null and pLanguage in params is not present all languages are searched
	 * @param defaultLangCode
	 * @param params
	 * @throws Exception
	 */
    public void search(String defaultLangCode, Element params) throws Exception {
        String sKeyword = "";
        if (params.getChild("pKeyword") != null) {
            sKeyword = Util.getParam(params, "pKeyword", "");
        }
        _maxResults = Util.getParam(params, "maxResults", _maxResults);
        if (params.getChild("pLanguage") != null) {
            defaultLangCode = Util.getParam(params, "pLanguage");
        }
        if (defaultLangCode != null && defaultLangCode.length() > 2) {
            defaultLangCode = defaultLangCode.substring(0, 2);
        }
        int pTypeSearch = _pTypeSearch;
        if (params.getChild("pTypeSearch") != null) {
            pTypeSearch = Util.getParamAsInt(params, "pTypeSearch");
        }
        List listThesauri = new Vector<Element>();
        String pTypeThesaurus = null;
        if (params.getChild("pType") != null) pTypeThesaurus = Util.getParam(params, "pType");
        boolean bAll = true;
        if (params.getChild("pThesauri") != null) {
            listThesauri = params.getChildren("pThesauri");
            bAll = false;
            for (Iterator<Element> it = listThesauri.iterator(); it.hasNext(); ) {
                Element th = it.next();
                if ("".equals(th.getTextTrim())) it.remove();
            }
            if (listThesauri.size() == 0) bAll = true;
        }
        if (bAll) {
            Hashtable<String, Thesaurus> tt = _tm.getThesauriTable();
            Enumeration e = tt.keys();
            boolean add = true;
            while (e.hasMoreElements()) {
                Thesaurus thesaurus = tt.get(e.nextElement());
                if (pTypeThesaurus != null) {
                    if (!thesaurus.getDname().equals(pTypeThesaurus)) add = false; else add = true;
                }
                if (add) {
                    Element el = new Element("pThesauri");
                    el.addContent(thesaurus.getKey());
                    listThesauri.add(el);
                }
            }
        }
        if (defaultLangCode != null) {
            _query = "SELECT prefLab, note, id, lowc, uppc, lang(prefLab) " + " FROM {id} rdf:type {skos:Concept}; " + " skos:prefLabel {prefLab};" + " [skos:scopeNote {note} WHERE lang(note) LIKE \"" + defaultLangCode + "\"]; " + " [gml:BoundedBy {} gml:lowerCorner {lowc}]; " + " [gml:BoundedBy {} gml:upperCorner {uppc}] " + " WHERE lang(prefLab) LIKE \"" + defaultLangCode + "\"" + " AND prefLab LIKE ";
        } else {
            _query = "SELECT prefLab, note, id, lowc, uppc " + " FROM {id} rdf:type {skos:Concept}; " + " skos:prefLabel {prefLab};" + " [skos:scopeNote {note}";
        }
        switch(pTypeSearch) {
            case 0:
                _query += "\"" + sKeyword + "*\" ";
                break;
            case 1:
                _query += "\"*" + sKeyword + "*\" ";
                break;
            case 2:
                _query += "\"" + sKeyword + "\" ";
                break;
            default:
                break;
        }
        _query += " IGNORE CASE " + " LIMIT " + _maxResults + " USING NAMESPACE skos=<" + Concept.NS_SKOS + ">, gml=<" + Concept.NS_GML + "> ";
        Log.debug(Geonet.THESAURUS_MAN, _query);
        _results = new ArrayList<Concept>();
        Map<String, Concept> bag = new HashMap<String, Concept>();
        int idKeyword = 0;
        for (int i = 0; i < listThesauri.size(); i++) {
            Element el = (Element) listThesauri.get(i);
            String sThesaurusName = el.getTextTrim();
            Thesaurus thesaurus = _tm.getThesaurusByName(sThesaurusName);
            QueryResultsTable resultsTable = thesaurus.performRequest(_query);
            int rowCount = resultsTable.getRowCount();
            for (int row = 0; row < rowCount; row++) {
                Concept kb = createConcept(defaultLangCode, idKeyword, sThesaurusName, resultsTable, row);
                if (bag.containsKey(kb.getCode())) {
                    bag.get(kb.getCode()).setPrefLabel(kb.getDefaultPrefLabel(), kb.getDefaultLocale());
                } else {
                    bag.put(kb.getCode(), kb);
                    _results.add(kb);
                }
                idKeyword++;
            }
        }
    }

    private Concept createConcept(String defaultLangCode, int idKeyword, String sThesaurusName, QueryResultsTable resultsTable, int row) {
        Value value = resultsTable.getValue(row, 0);
        String sValue = "";
        if (value != null) {
            sValue = value.toString();
        }
        Value definition = resultsTable.getValue(row, 1);
        String sDefinition = "";
        if (definition != null) {
            sDefinition = definition.toString();
        }
        Value uri = resultsTable.getValue(row, 2);
        String sUri = "";
        if (uri != null) {
            sUri = uri.toString();
        }
        Value lang = resultsTable.getValue(row, 5);
        String sLang = defaultLangCode;
        if (lang != null) {
            sLang = lang.toString();
        }
        sLang = LangUtils.two2ThreeLangCode(sLang);
        Value lowCorner = resultsTable.getValue(row, 3);
        Value upperCorner = resultsTable.getValue(row, 4);
        String sUpperCorner = "";
        String sLowCorner = "";
        String sEast = "";
        String sSouth = "";
        String sWest = "";
        String sNorth = "";
        if (lowCorner != null) {
            sLowCorner = lowCorner.toString();
            sWest = sLowCorner.substring(0, sLowCorner.indexOf(' ')).trim();
            sSouth = sLowCorner.substring(sLowCorner.indexOf(' ')).trim();
        }
        if (upperCorner != null) {
            sUpperCorner = upperCorner.toString();
            sEast = sUpperCorner.substring(0, sUpperCorner.indexOf(' ')).trim();
            sNorth = sUpperCorner.substring(sUpperCorner.indexOf(' ')).trim();
        }
        Concept kb = new Concept(idKeyword, sValue, sDefinition, sUri, sEast, sWest, sSouth, sNorth, sThesaurusName, false, sLang);
        return kb;
    }

    public void searchBN(ServiceContext srvContext, Element params, String request) throws Exception {
        String id = Util.getParam(params, "id");
        String sThesaurusName = Util.getParam(params, THESAURUS);
        Thesaurus thesaurus = _tm.getThesaurusByName(sThesaurusName);
        String _lang = srvContext.getLanguage();
        searchBN(id, sThesaurusName, request, _lang);
    }

    public void searchBN(String id, String sThesaurusName, String request, String _lang) throws Exception {
        Thesaurus thesaurus = _tm.getThesaurusByName(sThesaurusName);
        _results = new ArrayList<Concept>();
        if (_lang.length() > 2) _lang = _lang.substring(0, 2);
        String _query = "SELECT prefLab, note, id " + " from {id} rdf:type {skos:Concept};" + " skos:prefLabel {prefLab};" + " [skos:" + request + " {b}];" + " [skos:scopeNote {note} WHERE lang(note) LIKE \"" + _lang + "\"] " + " WHERE lang(prefLab) LIKE \"" + _lang + "\"" + " AND b LIKE \"*" + id + "\"" + " IGNORE CASE " + " USING NAMESPACE skos=<" + Concept.NS_SKOS + ">, gml=<" + Concept.NS_GML + "> ";
        Log.debug(Geonet.THESAURUS_MAN, _query);
        QueryResultsTable resultsTable = thesaurus.performRequest(_query);
        int rowCount = resultsTable.getRowCount();
        int idKeyword = 0;
        for (int row = 0; row < rowCount; row++) {
            Value value = resultsTable.getValue(row, 0);
            String sValue = "";
            if (value != null) {
                sValue = value.toString();
            }
            Value uri = resultsTable.getValue(row, 2);
            String sUri = "";
            if (uri != null) {
                sUri = uri.toString();
            }
            Concept kb = new Concept(idKeyword, sValue, "", sUri, "", "", "", "", sThesaurusName, false, _lang);
            _results.add(kb);
            idKeyword++;
        }
    }

    public void findEnclosedGeoKeyword(String sKeywordCode) {
        _query = "SELECT prefLab, note, id, lowc, uppc " + " FROM {id} rdf:type {skos:Concept}; " + " skos:prefLabel {prefLab};" + " [skos:scopeNote {note} WHERE lang(note) LIKE \"" + _lang + "\"]; " + " [gml:BoundedBy {} gml:lowerCorner {lowc}]; " + " [gml:BoundedBy {} gml:upperCorner {uppc}] " + " WHERE lang(prefLab) LIKE \"" + _lang + "\"" + " AND prefLab LIKE \"" + sKeywordCode + "*\" " + " USING NAMESPACE skos=<" + Concept.NS_SKOS + ">, gml=<" + Concept.NS_GML + "> ";
    }

    public int getNbResults() {
        return _results.size();
    }

    public void sortResults(final String sort, final String language) {
        _sortBy = sort;
        if ("label".equals(sort)) {
            Collections.sort((List) _results, new Comparator() {

                public int compare(final Object o1, final Object o2) {
                    final Concept kw1 = (Concept) o1;
                    final Concept kw2 = (Concept) o2;
                    if (language != null) {
                        kw1.setDefaultLang(language);
                        kw2.setDefaultLang(language);
                    }
                    String threeCharlocale1 = kw1.getDefaultLocale();
                    String threeCharlocale2 = kw2.getDefaultLocale();
                    if (language != null && threeCharlocale1.equalsIgnoreCase(language) && !threeCharlocale1.equalsIgnoreCase(language)) {
                        return 1;
                    }
                    if (language != null && !threeCharlocale1.equalsIgnoreCase(language) && threeCharlocale1.equalsIgnoreCase(language)) {
                        return -1;
                    }
                    Locale locale1 = LocaleUtil.toLocale(threeCharlocale1);
                    Locale locale2 = LocaleUtil.toLocale(threeCharlocale2);
                    String label1 = kw1.getDefaultPrefLabel().toLowerCase();
                    String label2 = kw2.getDefaultPrefLabel().toLowerCase();
                    LocalizedStringComparable cmp1 = new LocalizedStringComparable(label1.toLowerCase(locale1), locale1);
                    LocalizedStringComparable cmp2 = new LocalizedStringComparable(label2.toLowerCase(locale2), locale2);
                    return cmp1.compareTo(cmp2);
                }
            });
        }
        if ("definition".equals(sort)) {
            Collections.sort((List) _results, new Comparator() {

                public int compare(final Object o1, final Object o2) {
                    final Concept kw1 = (Concept) o1;
                    final Concept kw2 = (Concept) o2;
                    return kw1.getScopeNote().compareToIgnoreCase(kw2.getScopeNote());
                }
            });
        }
    }

    private static class LocalizedStringComparable implements Comparable<LocalizedStringComparable> {

        public final String _wrapped;

        private final Collator _comparator;

        public LocalizedStringComparable(String wrapped, Locale locale) {
            this._wrapped = wrapped;
            _comparator = java.text.Collator.getInstance(locale);
        }

        public int compareTo(LocalizedStringComparable anotherString) {
            return _comparator.compare(_wrapped, anotherString._wrapped);
        }
    }

    public Element getResults(Element params) throws Exception {
        Element elDescKeys = new Element(DESC_KEYS);
        int nbResults = 36000;
        if (params.getChild(NB_RESULTS) != null) nbResults = Util.getParam(params, NB_RESULTS, this.getNbResults());
        nbResults = (this.getNbResults() <= nbResults ? this.getNbResults() : nbResults);
        for (int i = 0; i <= nbResults - 1; i++) {
            Concept kb = _results.get(i);
            elDescKeys.addContent(kb.toElement("eng", "fra", "eng", "ita"));
        }
        return elDescKeys;
    }

    public void selectUnselectKeywords(Element params) {
        List listIdKeywordsSelected = params.getChildren(PID_KEYWORD);
        for (int i = 0; i < listIdKeywordsSelected.size(); i++) {
            Element el = (Element) listIdKeywordsSelected.get(i);
            int keywordId = Integer.decode(el.getTextTrim());
            for (int j = 0; j < _results.size(); j++) {
                if ((_results.get(j)).getId() == keywordId) {
                    (_results.get(j)).setSelected(!(_results.get(j)).isSelected());
                }
            }
        }
    }

    /**
	 * @return an element describing the list of selected keywords
	 */
    public Element getSelectedKeywords() {
        Element elDescKeys = new Element(DESC_KEYS);
        int nbSelectedKeywords = 0;
        for (int i = 0; i < this.getNbResults(); i++) {
            Concept kb = _results.get(i);
            if (kb.isSelected()) {
                elDescKeys.addContent(kb.toElement());
                nbSelectedKeywords++;
            }
        }
        Element elNbTot = new Element(NBTOT);
        elNbTot.addContent(Integer.toString(nbSelectedKeywords));
        elDescKeys.addContent(elNbTot);
        return elDescKeys;
    }

    public List getSelectedKeywordsInList() {
        ArrayList keywords = new ArrayList<Concept>();
        for (int i = 0; i < this.getNbResults(); i++) {
            Concept kb = _results.get(i);
            if (kb.isSelected()) {
                keywords.add(kb);
            }
        }
        return keywords;
    }

    public Concept existsResult(String id) {
        Concept keyword = null;
        for (int i = 0; i < this.getNbResults(); i++) {
            Concept kb = _results.get(i);
            if (kb.getId() == Integer.parseInt(id)) {
                keyword = kb;
                break;
            }
        }
        return keyword;
    }
}
