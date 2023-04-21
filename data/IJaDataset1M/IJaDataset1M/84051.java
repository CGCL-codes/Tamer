package finderXMLObject;

import javax.accessibility.AccessibleRole;

/**
 * Estende XpathCriteriaSearch. Effettua la ricerca andando ad intercettare
 * l'oggetto il cui nome si trova nella descrizione o nel tooltip. Si tratta di
 * una ricerca non precisa ma con un margine di errore.
 * 
 * @author Riccardo Costa
 * 
 */
public class SearchToolTipPartialMatch extends XpathCriteriaSearch {

    /**
	 * Costruttore per la classe.
	 * 
	 * @param name
	 *            Nome del criterio.
	 */
    public SearchToolTipPartialMatch(String name) {
        super(name);
        weight = 5;
    }

    /**
	 * Costruisce la stringa da utilizzare nella ricerca dell'oggetto.
	 * 
	 * @param name
	 *            Nome dell'elemento da cercare.
	 * @param type
	 *            Tipo dell'elemento da cercare.
	 * @return Stringa di ricerca.
	 */
    protected String buildString(String name, AccessibleRole type) {
        String cSens = getProperty(CriteriaSearch.CASE_SENSITIVE);
        String searchStringTool = null;
        searchStringTool = "//accessible";
        String searchType = null;
        if (name != null) {
            if (cSens != null && cSens.compareToIgnoreCase(CriteriaSearch.CASE_SENSITIVE_FALSE_VALUE) == 0) searchStringTool = searchStringTool.concat("//accessibleComponentProperty[contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'" + name.toLowerCase() + "')][@name='toolTipText']/../.."); else searchStringTool = searchStringTool.concat("//accessibleComponentProperty[contains(@value,'" + name + "')][@name='toolTipText']/../..");
        }
        if (type != null) searchType = "[@accessibleRole='" + type.toDisplayString(localLanguage) + "']";
        if (searchType != null) searchStringTool = searchStringTool.concat(searchType);
        String searchStringDesc = null;
        searchStringDesc = "//accessible";
        if (name != null) {
            if (cSens != null && cSens.compareToIgnoreCase(CriteriaSearch.CASE_SENSITIVE_FALSE_VALUE) == 0) searchStringDesc = searchStringDesc.concat("[contains(translate(@accessibleDescription,'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'" + name + "')]"); else searchStringDesc = searchStringDesc.concat("[contains(@accessibleDescription,'" + name + "')]");
        }
        if (searchType != null) searchStringDesc = searchStringDesc.concat(searchType);
        searchStringTool = searchStringTool.concat(" | " + searchStringDesc);
        return searchStringTool;
    }
}
