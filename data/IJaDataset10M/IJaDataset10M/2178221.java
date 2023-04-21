package es.aeat.eett.rubik.core;

import org.jdom.Document;
import org.jdom.Element;

public class RubikXmlaInfo {

    public static final String RUBIK_XMLA_INFO = "RubikXmlaInfo";

    public static final String RUBIK_URI = "uri";

    public static final String RUBIK_CATALOG = "catalog";

    private String uri = "";

    private String catalog = "";

    public RubikXmlaInfo() {
    }

    public RubikXmlaInfo(String uri, String catalog) {
        this.uri = uri;
        this.catalog = catalog;
    }

    /**
	 * @return Returns the catalog.
	 */
    public String getCatalog() {
        return catalog;
    }

    /**
	 * @param catalog The catalog to set.
	 */
    public void setCatalog(String catalog) {
        if (catalog == null) catalog = "";
        this.catalog = catalog;
    }

    /**
	 * @return Returns the uri.
	 */
    public String getUri() {
        return uri;
    }

    /**
	 * @param uri The uri to set.
	 */
    public void setUri(String uri) {
        if (uri == null) uri = "";
        this.uri = uri;
    }

    public Object clone() {
        return new RubikXmlaInfo(uri, catalog);
    }

    /**
	 * @return Element 'RubikXmlaInfo' (Root Element) con los atributos de RubikXmlaInfo
	 */
    public Element getRootElement() {
        Element eRubikXmlaInfo = new Element(RUBIK_XMLA_INFO);
        eRubikXmlaInfo.setAttribute(RUBIK_URI, uri);
        eRubikXmlaInfo.setAttribute(RUBIK_CATALOG, catalog);
        return eRubikXmlaInfo;
    }

    public Document getDocument() {
        return new Document(getRootElement());
    }

    /**
     * Carga los atributos de RubikXmlaInfo.
     * Si el Element no es del tipo RubikXmlaInfo no hace nada y devuelve false.
     * @param rootElement Element RubikXmlaInfo
     * @return true si todo fue bien. false en caso de fallo
     */
    public boolean loadRootElement(Element rootElement) {
        if (rootElement != null && rootElement.getName().equals(RUBIK_XMLA_INFO)) {
            uri = rootElement.getAttribute(RUBIK_URI).getValue();
            catalog = rootElement.getAttribute(RUBIK_CATALOG).getValue().trim();
            return true;
        }
        return false;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RubikXmlaInfo)) {
            return false;
        }
        RubikXmlaInfo rx = (RubikXmlaInfo) obj;
        if (!rx.uri.equals(uri)) return false;
        if (!rx.catalog.equals(catalog)) return false;
        return true;
    }
}
