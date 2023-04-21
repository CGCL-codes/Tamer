package br.ufrj.cad.model.usuario.xmllattes.DTD;

/**
 *  Class corresponding to a <i>DEMAIS-TIPOS-DE-PRODUCAO-BIBLIOGRAFICA</i> element in XML.
 *  <p>
 *  @author Generated with jNerd's XML2Java tool Version 1.2 Preview 2 on Tue Apr 14 16:30:41 BRT 2009
 *  @author <br>Please send BugReports to <a href="mailto:xml2java@jNerd.de">xml2java@jNerd.de</a>
 */
public class DemaisTiposDeProducaoBibliografica implements XMLElement {

    /** The name of this class' corresponding XML element. (<i>"DEMAIS-TIPOS-DE-PRODUCAO-BIBLIOGRAFICA"</i>) */
    public static final String ELEMENT_NAME = "DEMAIS-TIPOS-DE-PRODUCAO-BIBLIOGRAFICA";

    /** The nested optional list of <i>OUTRA-PRODUCAO-BIBLIOGRAFICA</i> elements
	 */
    private OutraProducaoBibliograficaList outraProducaoBibliograficaList;

    /** The nested optional list of <i>PARTITURA-MUSICAL</i> elements
	 */
    private PartituraMusicalList partituraMusicalList;

    /** The nested optional list of <i>PREFACIO-POSFACIO</i> elements
	 */
    private PrefacioPosfacioList prefacioPosfacioList;

    /** The nested optional list of <i>TRADUCAO</i> elements
	 */
    private TraducaoList traducaoList;

    /** Accesses the XML element name.
	 *  @return <i>"DEMAIS-TIPOS-DE-PRODUCAO-BIBLIOGRAFICA"</i> which is this class' corresponding XML Element.
	 */
    public String getElementName() {
        return ELEMENT_NAME;
    }

    /** Access to property <code>outraProducaoBibliograficaList</code> 
	 *  corresponding to the optional nested list of 
	 *  <i>OUTRA-PRODUCAO-BIBLIOGRAFICA</i> elements within this class.
	 *  <p>
	 *  The optional list of <i>OUTRA-PRODUCAO-BIBLIOGRAFICA</i> elements is represented by 
	 *  property <code>outraProducaoBibliograficaList</code>, an instance of
	 *  class <code>OutraProducaoBibliograficaList</code>. All containing <i>OUTRA-PRODUCAO-BIBLIOGRAFICA</i> elements are 
	 *  represented by class <code>OutraProducaoBibliografica</code>.
	 *  <p>
	 *  If the current property has a value of <code>null</code>, a new empty 
	 *  <code>OutraProducaoBibliograficaList</code> will be instantiated and set as the property before being returned.
	 *
	 *  @return Property <code>outraProducaoBibliograficaList</code>, which is the 
	 *          current optional list of nested <i>OUTRA-PRODUCAO-BIBLIOGRAFICA</i> node.
	 *
	 *  @see OutraProducaoBibliograficaList
	 *  @see OutraProducaoBibliografica
	 */
    public OutraProducaoBibliograficaList getOutraProducaoBibliograficaList() {
        if (this.outraProducaoBibliograficaList == null) {
            this.outraProducaoBibliograficaList = new OutraProducaoBibliograficaList();
        }
        return this.outraProducaoBibliograficaList;
    }

    /** Sets this class' optional list of nested <i>OUTRA-PRODUCAO-BIBLIOGRAFICA</i>
	 *  elements within this class.
	 *  <p>
	 *  The optional list of <i>OUTRA-PRODUCAO-BIBLIOGRAFICA</i> elements is represented by 
	 *  property <code>outraProducaoBibliograficaList</code>, an instance of
	 *  class <code>OutraProducaoBibliograficaList</code>. All containing <i>OUTRA-PRODUCAO-BIBLIOGRAFICA</i> elements are 
	 *  represented by class <code>OutraProducaoBibliografica</code>.
	 *
	 *  @param poutraProducaoBibliograficaList The <code>OutraProducaoBibliograficaList</code> to be
	 *         set as the property <code>outraProducaoBibliograficaList</code>
	 *
	 *  @see OutraProducaoBibliograficaList
	 *  @see OutraProducaoBibliografica
	 */
    public void setOutraProducaoBibliograficaList(OutraProducaoBibliograficaList poutraProducaoBibliograficaList) {
        this.outraProducaoBibliograficaList = poutraProducaoBibliograficaList;
    }

    /** Adds a <code>OutraProducaoBibliografica</code>, representing a <i>OUTRA-PRODUCAO-BIBLIOGRAFICA</i> element
	 *  to the optional nested list.
	 *
	 *  @param elem <code>OutraProducaoBibliografica</code> to be added to 
	 *         the optional nested list of <i>OUTRA-PRODUCAO-BIBLIOGRAFICA</i> elements.
	 *
	 *  @throws NullPointerException If the specified parameter is <code>null</code>.
	 *
	 *  @see OutraProducaoBibliografica
	 *  @see OutraProducaoBibliograficaList#add
	 */
    public void add(OutraProducaoBibliografica elem) {
        this.getOutraProducaoBibliograficaList().add(elem);
    }

    /** Access to property <code>partituraMusicalList</code> 
	 *  corresponding to the optional nested list of 
	 *  <i>PARTITURA-MUSICAL</i> elements within this class.
	 *  <p>
	 *  The optional list of <i>PARTITURA-MUSICAL</i> elements is represented by 
	 *  property <code>partituraMusicalList</code>, an instance of
	 *  class <code>PartituraMusicalList</code>. All containing <i>PARTITURA-MUSICAL</i> elements are 
	 *  represented by class <code>PartituraMusical</code>.
	 *  <p>
	 *  If the current property has a value of <code>null</code>, a new empty 
	 *  <code>PartituraMusicalList</code> will be instantiated and set as the property before being returned.
	 *
	 *  @return Property <code>partituraMusicalList</code>, which is the 
	 *          current optional list of nested <i>PARTITURA-MUSICAL</i> node.
	 *
	 *  @see PartituraMusicalList
	 *  @see PartituraMusical
	 */
    public PartituraMusicalList getPartituraMusicalList() {
        if (this.partituraMusicalList == null) {
            this.partituraMusicalList = new PartituraMusicalList();
        }
        return this.partituraMusicalList;
    }

    /** Sets this class' optional list of nested <i>PARTITURA-MUSICAL</i>
	 *  elements within this class.
	 *  <p>
	 *  The optional list of <i>PARTITURA-MUSICAL</i> elements is represented by 
	 *  property <code>partituraMusicalList</code>, an instance of
	 *  class <code>PartituraMusicalList</code>. All containing <i>PARTITURA-MUSICAL</i> elements are 
	 *  represented by class <code>PartituraMusical</code>.
	 *
	 *  @param ppartituraMusicalList The <code>PartituraMusicalList</code> to be
	 *         set as the property <code>partituraMusicalList</code>
	 *
	 *  @see PartituraMusicalList
	 *  @see PartituraMusical
	 */
    public void setPartituraMusicalList(PartituraMusicalList ppartituraMusicalList) {
        this.partituraMusicalList = ppartituraMusicalList;
    }

    /** Adds a <code>PartituraMusical</code>, representing a <i>PARTITURA-MUSICAL</i> element
	 *  to the optional nested list.
	 *
	 *  @param elem <code>PartituraMusical</code> to be added to 
	 *         the optional nested list of <i>PARTITURA-MUSICAL</i> elements.
	 *
	 *  @throws NullPointerException If the specified parameter is <code>null</code>.
	 *
	 *  @see PartituraMusical
	 *  @see PartituraMusicalList#add
	 */
    public void add(PartituraMusical elem) {
        this.getPartituraMusicalList().add(elem);
    }

    /** Access to property <code>prefacioPosfacioList</code> 
	 *  corresponding to the optional nested list of 
	 *  <i>PREFACIO-POSFACIO</i> elements within this class.
	 *  <p>
	 *  The optional list of <i>PREFACIO-POSFACIO</i> elements is represented by 
	 *  property <code>prefacioPosfacioList</code>, an instance of
	 *  class <code>PrefacioPosfacioList</code>. All containing <i>PREFACIO-POSFACIO</i> elements are 
	 *  represented by class <code>PrefacioPosfacio</code>.
	 *  <p>
	 *  If the current property has a value of <code>null</code>, a new empty 
	 *  <code>PrefacioPosfacioList</code> will be instantiated and set as the property before being returned.
	 *
	 *  @return Property <code>prefacioPosfacioList</code>, which is the 
	 *          current optional list of nested <i>PREFACIO-POSFACIO</i> node.
	 *
	 *  @see PrefacioPosfacioList
	 *  @see PrefacioPosfacio
	 */
    public PrefacioPosfacioList getPrefacioPosfacioList() {
        if (this.prefacioPosfacioList == null) {
            this.prefacioPosfacioList = new PrefacioPosfacioList();
        }
        return this.prefacioPosfacioList;
    }

    /** Sets this class' optional list of nested <i>PREFACIO-POSFACIO</i>
	 *  elements within this class.
	 *  <p>
	 *  The optional list of <i>PREFACIO-POSFACIO</i> elements is represented by 
	 *  property <code>prefacioPosfacioList</code>, an instance of
	 *  class <code>PrefacioPosfacioList</code>. All containing <i>PREFACIO-POSFACIO</i> elements are 
	 *  represented by class <code>PrefacioPosfacio</code>.
	 *
	 *  @param pprefacioPosfacioList The <code>PrefacioPosfacioList</code> to be
	 *         set as the property <code>prefacioPosfacioList</code>
	 *
	 *  @see PrefacioPosfacioList
	 *  @see PrefacioPosfacio
	 */
    public void setPrefacioPosfacioList(PrefacioPosfacioList pprefacioPosfacioList) {
        this.prefacioPosfacioList = pprefacioPosfacioList;
    }

    /** Adds a <code>PrefacioPosfacio</code>, representing a <i>PREFACIO-POSFACIO</i> element
	 *  to the optional nested list.
	 *
	 *  @param elem <code>PrefacioPosfacio</code> to be added to 
	 *         the optional nested list of <i>PREFACIO-POSFACIO</i> elements.
	 *
	 *  @throws NullPointerException If the specified parameter is <code>null</code>.
	 *
	 *  @see PrefacioPosfacio
	 *  @see PrefacioPosfacioList#add
	 */
    public void add(PrefacioPosfacio elem) {
        this.getPrefacioPosfacioList().add(elem);
    }

    /** Access to property <code>traducaoList</code> 
	 *  corresponding to the optional nested list of 
	 *  <i>TRADUCAO</i> elements within this class.
	 *  <p>
	 *  The optional list of <i>TRADUCAO</i> elements is represented by 
	 *  property <code>traducaoList</code>, an instance of
	 *  class <code>TraducaoList</code>. All containing <i>TRADUCAO</i> elements are 
	 *  represented by class <code>Traducao</code>.
	 *  <p>
	 *  If the current property has a value of <code>null</code>, a new empty 
	 *  <code>TraducaoList</code> will be instantiated and set as the property before being returned.
	 *
	 *  @return Property <code>traducaoList</code>, which is the 
	 *          current optional list of nested <i>TRADUCAO</i> node.
	 *
	 *  @see TraducaoList
	 *  @see Traducao
	 */
    public TraducaoList getTraducaoList() {
        if (this.traducaoList == null) {
            this.traducaoList = new TraducaoList();
        }
        return this.traducaoList;
    }

    /** Sets this class' optional list of nested <i>TRADUCAO</i>
	 *  elements within this class.
	 *  <p>
	 *  The optional list of <i>TRADUCAO</i> elements is represented by 
	 *  property <code>traducaoList</code>, an instance of
	 *  class <code>TraducaoList</code>. All containing <i>TRADUCAO</i> elements are 
	 *  represented by class <code>Traducao</code>.
	 *
	 *  @param ptraducaoList The <code>TraducaoList</code> to be
	 *         set as the property <code>traducaoList</code>
	 *
	 *  @see TraducaoList
	 *  @see Traducao
	 */
    public void setTraducaoList(TraducaoList ptraducaoList) {
        this.traducaoList = ptraducaoList;
    }

    /** Adds a <code>Traducao</code>, representing a <i>TRADUCAO</i> element
	 *  to the optional nested list.
	 *
	 *  @param elem <code>Traducao</code> to be added to 
	 *         the optional nested list of <i>TRADUCAO</i> elements.
	 *
	 *  @throws NullPointerException If the specified parameter is <code>null</code>.
	 *
	 *  @see Traducao
	 *  @see TraducaoList#add
	 */
    public void add(Traducao elem) {
        this.getTraducaoList().add(elem);
    }
}
