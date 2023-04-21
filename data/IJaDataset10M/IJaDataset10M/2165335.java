package br.ufrj.cad.model.usuario.xmllattes.DTD;

/**
 *  Class corresponding to a <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element in XML.
 *  <p>
 *  @author Generated with jNerd's XML2Java tool Version 1.2 Preview 2 on Tue Apr 14 16:30:39 BRT 2009
 *  @author <br>Please send BugReports to <a href="mailto:xml2java@jNerd.de">xml2java@jNerd.de</a>
 */
public class DadosBasicosDaApresentacaoDeTrabalho implements DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues, FlagRelevanciaValues, XMLElement {

    /** The name of this class' corresponding XML element. (<i>"DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO"</i>) */
    public static final String ELEMENT_NAME = "DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO";

    /** Property <code>natureza</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's
	 *  <i>NATUREZA</i> enumeration attribute.<br>
	 *  All valid values are stored in <code>DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues</code>.
	 *
 *  @see DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues
	 */
    private int natureza = -1;

    /** Property <code>titulo</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's
	 *  <i>TITULO</i> attribute.
	 */
    private String titulo;

    /** Property <code>ano</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's
	 *  <i>ANO</i> attribute.
	 */
    private String ano;

    /** Property <code>pais</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's
	 *  <i>PAIS</i> attribute.
	 */
    private String pais;

    /** Property <code>idioma</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's
	 *  <i>IDIOMA</i> attribute.
	 */
    private String idioma;

    /** Property <code>flagRelevancia</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's
	 *  <i>FLAG-RELEVANCIA</i> enumeration attribute.<br>
	 *  All valid values are stored in <code>FlagRelevanciaValues</code>.
	 *
 *  @see FlagRelevanciaValues
	 */
    private int flagRelevancia = 1;

    /** Property <code>doi</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's
	 *  <i>DOI</i> attribute.
	 */
    private String doi;

    /** Accesses the XML element name.
	 *  @return <i>"DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO"</i> which is this class' corresponding XML Element.
	 */
    public String getElementName() {
        return ELEMENT_NAME;
    }

    /** Accesses the <code>natureza</code> property.<br>
	 *  The property <code>natureza</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's implied <i>NATUREZA</i> 
	 *  attribute.
	 *
	 *  @return A <code>int</code> representing the value of the 
	 *          <code>natureza</code> property or <code>-1</code> if this
	 *          attribute is unspecified.
	 *
	 *  @see DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues
	 */
    public int getNatureza() {
        return this.natureza;
    }

    /** Accesses a <code>String</code> representation of 
	 *  the <code>natureza</code> property.<br>
	 *  The property <code>natureza</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's implied <i>NATUREZA</i> 
	 *  attribute.
	 *
	 *  @return One of <i>'COMUNICACAO'</i>, <i>'CONFERENCIA'</i>, <i>'CONGRESSO'</i>, <i>'SEMINARIO'</i>, <i>'SIMPOSIO'</i>, <i>'OUTRA'</i> or <i>'NAO_INFORMADO'</i>, depending on the current value of the property 
	 *          <code>natureza</code> or <code>null</code> if this 
	 *          attribute is unspecified.
	 *
	 *  @see DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues
	 */
    public String getNaturezaAsString() {
        if (this.getNatureza() == -1) {
            return null;
        }
        return DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues.values[this.getNatureza()];
    }

    /** Sets the <code>natureza</code> property.
	 *  <br>
	 *  The property <code>natureza</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>NATUREZA</i> attribute.
	 *  <br>
	 *  The enumerated attribute is representent by <code>int</code> values. Consult the 
	 *  documentation of interface <code>DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues</code> for all possible values.
	 *  <br>
	 *  To unset this attribute set a <code>int</code> value of <code>-1</code>.
	 *
	 *  @param value the new value of the <code>natureza</code> property.
	 *
	 *  @throws IllegalArgumentException if the specified value does not represent a valid
	 *          <i>NATUREZA</i> attribute value.
	 *
	 *  @see DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues
	 */
    public void setNatureza(int value) {
        if (value < -1 || value >= DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues.values.length) {
            throw new IllegalArgumentException("'" + value + "' is not a valid value for property natureza.");
        }
        this.natureza = value;
    }

    /** Sets the <code>natureza</code> property.
	 *  <br>
	 *  This classes property <code>natureza</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>NATUREZA</i> attribute.
	 *
	 *  @param value A <code>String</code> matching a valid
	 *         value of the <code>natureza</code> property or <code>null</code>
	 *         if this attribute shall be unspecified.
	 *
	 *  @throws IllegalArgumentException If the specified <code>String value</code> does not match 
	 *          a valid attribute value.
	 *
	 *  @see DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues
	 */
    public void setNatureza(String value) {
        if (value == null) {
            this.setNatureza(-1);
            return;
        }
        for (int i = 0; i < DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues.values.length; i++) {
            if (DadosBasicosDaApresentacaoDeTrabalhoNaturezaValues.values[i].equals(value)) {
                this.setNatureza(i);
                return;
            }
        }
        throw new IllegalArgumentException("'" + value + "' is not a valid value for property natureza.");
    }

    /** Accesses the <code>titulo</code> property.<br>
	 *  The property <code>titulo</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>TITULO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>titulo</code> property.
	 */
    public String getTitulo() {
        return this.titulo;
    }

    /** Sets the <code>titulo</code> property.<br>
	 *  The <code>titulo</code> property corresponds to the
	 *  <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>TITULO</i> attribute.
	 *
	 *  @param titulo the new value of the <code>titulo</code> property.
	 */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /** Accesses the <code>ano</code> property.<br>
	 *  The property <code>ano</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>ANO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>ano</code> property.
	 */
    public String getAno() {
        return this.ano;
    }

    /** Sets the <code>ano</code> property.<br>
	 *  The <code>ano</code> property corresponds to the
	 *  <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>ANO</i> attribute.
	 *
	 *  @param ano the new value of the <code>ano</code> property.
	 */
    public void setAno(String ano) {
        this.ano = ano;
    }

    /** Accesses the <code>pais</code> property.<br>
	 *  The property <code>pais</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>PAIS</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>pais</code> property.
	 */
    public String getPais() {
        return this.pais;
    }

    /** Sets the <code>pais</code> property.<br>
	 *  The <code>pais</code> property corresponds to the
	 *  <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>PAIS</i> attribute.
	 *
	 *  @param pais the new value of the <code>pais</code> property.
	 */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /** Accesses the <code>idioma</code> property.<br>
	 *  The property <code>idioma</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>IDIOMA</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>idioma</code> property.
	 */
    public String getIdioma() {
        return this.idioma;
    }

    /** Sets the <code>idioma</code> property.<br>
	 *  The <code>idioma</code> property corresponds to the
	 *  <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>IDIOMA</i> attribute.
	 *
	 *  @param idioma the new value of the <code>idioma</code> property.
	 */
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    /** Accesses the <code>flagRelevancia</code> property.<br>
	 *  The property <code>flagRelevancia</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>FLAG-RELEVANCIA</i> attribute.
	 *
	 *  @return A <code>int</code> representing the value of the <code>flagRelevancia</code> property.
	 *
	 *  @see FlagRelevanciaValues
	 */
    public int getFlagRelevancia() {
        return this.flagRelevancia;
    }

    /** Accesses a <code>String</code> representation of 
	 *  the <code>flagRelevancia</code> property.<br>
	 *  The property <code>flagRelevancia</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>FLAG-RELEVANCIA</i> attribute.
	 *
	 *  @return One of <i>'SIM'</i> or <i>'NAO'</i>, depending on the current value of the property <code>flagRelevancia</code>.
	 *
	 *  @see FlagRelevanciaValues
	 */
    public String getFlagRelevanciaAsString() {
        return FlagRelevanciaValues.values[this.getFlagRelevancia()];
    }

    /** Sets the <code>flagRelevancia</code> property.
	 *  <br>
	 *  The property <code>flagRelevancia</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>FLAG-RELEVANCIA</i> attribute.
	 *  <br>
	 *  The enumerated attribute is representent by <code>int</code> values. Consult the 
	 *  documentation of interface <code>FlagRelevanciaValues</code> for all possible values.
	 *  <br>
	 *  To unset this attribute set a <code>int</code> value of <code>-1</code>.
	 *
	 *  @param value the new value of the <code>flagRelevancia</code> property.
	 *
	 *  @throws IllegalArgumentException if the specified value does not represent a valid
	 *          <i>FLAG-RELEVANCIA</i> attribute value.
	 *
	 *  @see FlagRelevanciaValues
	 */
    public void setFlagRelevancia(int value) {
        if (value < -1 || value >= FlagRelevanciaValues.values.length) {
            throw new IllegalArgumentException("'" + value + "' is not a valid value for property flagRelevancia.");
        }
        this.flagRelevancia = value;
    }

    /** Sets the <code>flagRelevancia</code> property.
	 *  <br>
	 *  This classes property <code>flagRelevancia</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>FLAG-RELEVANCIA</i> attribute.
	 *
	 *  @param value A <code>String</code> representing the new 
	 *         value of the <code>flagRelevancia</code> property. It has to
	 *         match a valid attribute value.
	 *
	 *  @throws IllegalArgumentException If the specified <code>String value</code> does not match 
	 *          a valid attribute value.
	 *
	 *  @see FlagRelevanciaValues
	 */
    public void setFlagRelevancia(String value) {
        for (int i = 0; i < FlagRelevanciaValues.values.length; i++) {
            if (FlagRelevanciaValues.values[i].equals(value)) {
                this.setFlagRelevancia(i);
                return;
            }
        }
        throw new IllegalArgumentException("'" + value + "' is not a valid value for property flagRelevancia.");
    }

    /** Accesses the <code>doi</code> property.<br>
	 *  The property <code>doi</code> corresponds to
	 *  the <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>DOI</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>doi</code> property.
	 */
    public String getDoi() {
        return this.doi;
    }

    /** Sets the <code>doi</code> property.<br>
	 *  The <code>doi</code> property corresponds to the
	 *  <i>DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO</i> element's <i>DOI</i> attribute.
	 *
	 *  @param doi the new value of the <code>doi</code> property.
	 */
    public void setDoi(String doi) {
        this.doi = doi;
    }
}
