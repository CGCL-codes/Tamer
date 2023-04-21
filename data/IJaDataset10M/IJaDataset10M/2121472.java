package br.ufrj.cad.model.usuario.xmllattes.DTD;

/**
 *  Class corresponding to a <i>PREMIOS-TITULOS</i> element in XML.
 *  <p>
 *  @author Generated with jNerd's XML2Java tool Version 1.2 Preview 2 on Tue Apr 14 16:30:41 BRT 2009
 *  @author <br>Please send BugReports to <a href="mailto:xml2java@jNerd.de">xml2java@jNerd.de</a>
 */
public class PremiosTitulos implements XMLElement {

    /** The name of this class' corresponding XML element. (<i>"PREMIOS-TITULOS"</i>) */
    public static final String ELEMENT_NAME = "PREMIOS-TITULOS";

    /** The nested optional list of <i>PREMIO-TITULO</i> elements
	 */
    private PremioTituloList premioTituloList;

    /** Accesses the XML element name.
	 *  @return <i>"PREMIOS-TITULOS"</i> which is this class' corresponding XML Element.
	 */
    public String getElementName() {
        return ELEMENT_NAME;
    }

    /** Access to property <code>premioTituloList</code> 
	 *  corresponding to the optional nested list of 
	 *  <i>PREMIO-TITULO</i> elements within this class.
	 *  <p>
	 *  The optional list of <i>PREMIO-TITULO</i> elements is represented by 
	 *  property <code>premioTituloList</code>, an instance of
	 *  class <code>PremioTituloList</code>. All containing <i>PREMIO-TITULO</i> elements are 
	 *  represented by class <code>PremioTitulo</code>.
	 *  <p>
	 *  If the current property has a value of <code>null</code>, a new empty 
	 *  <code>PremioTituloList</code> will be instantiated and set as the property before being returned.
	 *
	 *  @return Property <code>premioTituloList</code>, which is the 
	 *          current optional list of nested <i>PREMIO-TITULO</i> node.
	 *
	 *  @see PremioTituloList
	 *  @see PremioTitulo
	 */
    public PremioTituloList getPremioTituloList() {
        if (this.premioTituloList == null) {
            this.premioTituloList = new PremioTituloList();
        }
        return this.premioTituloList;
    }

    /** Sets this class' optional list of nested <i>PREMIO-TITULO</i>
	 *  elements within this class.
	 *  <p>
	 *  The optional list of <i>PREMIO-TITULO</i> elements is represented by 
	 *  property <code>premioTituloList</code>, an instance of
	 *  class <code>PremioTituloList</code>. All containing <i>PREMIO-TITULO</i> elements are 
	 *  represented by class <code>PremioTitulo</code>.
	 *
	 *  @param ppremioTituloList The <code>PremioTituloList</code> to be
	 *         set as the property <code>premioTituloList</code>
	 *
	 *  @see PremioTituloList
	 *  @see PremioTitulo
	 */
    public void setPremioTituloList(PremioTituloList ppremioTituloList) {
        this.premioTituloList = ppremioTituloList;
    }

    /** Adds a <code>PremioTitulo</code>, representing a <i>PREMIO-TITULO</i> element
	 *  to the optional nested list.
	 *
	 *  @param elem <code>PremioTitulo</code> to be added to 
	 *         the optional nested list of <i>PREMIO-TITULO</i> elements.
	 *
	 *  @throws NullPointerException If the specified parameter is <code>null</code>.
	 *
	 *  @see PremioTitulo
	 *  @see PremioTituloList#add
	 */
    public void add(PremioTitulo elem) {
        this.getPremioTituloList().add(elem);
    }
}
