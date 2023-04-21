package br.ufrj.cad.model.usuario.xmllattes.DTD;

/**
 *  Class corresponding to a <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element in XML.
 *  <p>
 *  @author Generated with jNerd's XML2Java tool Version 1.2 Preview 2 on Tue Apr 14 16:30:40 BRT 2009
 *  @author <br>Please send BugReports to <a href="mailto:xml2java@jNerd.de">xml2java@jNerd.de</a>
 */
public class DadosBasicosDoProcessosOuTecnicas implements DadosBasicosDoProcessosOuTecnicasNaturezaValues, MeioDeDivulgacaoValues, FlagRelevanciaValues, XMLElement {

    /** The name of this class' corresponding XML element. (<i>"DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS"</i>) */
    public static final String ELEMENT_NAME = "DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS";

    /** Property <code>natureza</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's
	 *  <i>NATUREZA</i> enumeration attribute.<br>
	 *  All valid values are stored in <code>DadosBasicosDoProcessosOuTecnicasNaturezaValues</code>.
	 *
 *  @see DadosBasicosDoProcessosOuTecnicasNaturezaValues
	 */
    private int natureza = -1;

    /** Property <code>tituloDoProcesso</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's
	 *  <i>TITULO-DO-PROCESSO</i> attribute.
	 */
    private String tituloDoProcesso;

    /** Property <code>ano</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's
	 *  <i>ANO</i> attribute.
	 */
    private String ano;

    /** Property <code>pais</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's
	 *  <i>PAIS</i> attribute.
	 */
    private String pais;

    /** Property <code>idioma</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's
	 *  <i>IDIOMA</i> attribute.
	 */
    private String idioma;

    /** Property <code>meioDeDivulgacao</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's
	 *  <i>MEIO-DE-DIVULGACAO</i> enumeration attribute.<br>
	 *  All valid values are stored in <code>MeioDeDivulgacaoValues</code>.
	 *
 *  @see MeioDeDivulgacaoValues
	 */
    private int meioDeDivulgacao = 7;

    /** Property <code>homePageDoTrabalho</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's
	 *  <i>HOME-PAGE-DO-TRABALHO</i> attribute.
	 */
    private String homePageDoTrabalho;

    /** Property <code>flagRelevancia</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's
	 *  <i>FLAG-RELEVANCIA</i> enumeration attribute.<br>
	 *  All valid values are stored in <code>FlagRelevanciaValues</code>.
	 *
 *  @see FlagRelevanciaValues
	 */
    private int flagRelevancia = 1;

    /** Property <code>doi</code> is corresponding
	 *  to the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's
	 *  <i>DOI</i> attribute.
	 */
    private String doi;

    /** Accesses the XML element name.
	 *  @return <i>"DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS"</i> which is this class' corresponding XML Element.
	 */
    public String getElementName() {
        return ELEMENT_NAME;
    }

    /** Accesses the <code>natureza</code> property.<br>
	 *  The property <code>natureza</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's implied <i>NATUREZA</i> 
	 *  attribute.
	 *
	 *  @return A <code>int</code> representing the value of the 
	 *          <code>natureza</code> property or <code>-1</code> if this
	 *          attribute is unspecified.
	 *
	 *  @see DadosBasicosDoProcessosOuTecnicasNaturezaValues
	 */
    public int getNatureza() {
        return this.natureza;
    }

    /** Accesses a <code>String</code> representation of 
	 *  the <code>natureza</code> property.<br>
	 *  The property <code>natureza</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's implied <i>NATUREZA</i> 
	 *  attribute.
	 *
	 *  @return One of <i>'ANALITICA'</i>, <i>'INSTRUMENTAL'</i>, <i>'PEDAGOGICA'</i>, <i>'PROCESSUAL'</i>, <i>'TERAPEUTICA'</i>, <i>'OUTRA'</i> or <i>'NAO_INFORMADO'</i>, depending on the current value of the property 
	 *          <code>natureza</code> or <code>null</code> if this 
	 *          attribute is unspecified.
	 *
	 *  @see DadosBasicosDoProcessosOuTecnicasNaturezaValues
	 */
    public String getNaturezaAsString() {
        if (this.getNatureza() == -1) {
            return null;
        }
        return DadosBasicosDoProcessosOuTecnicasNaturezaValues.values[this.getNatureza()];
    }

    /** Sets the <code>natureza</code> property.
	 *  <br>
	 *  The property <code>natureza</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>NATUREZA</i> attribute.
	 *  <br>
	 *  The enumerated attribute is representent by <code>int</code> values. Consult the 
	 *  documentation of interface <code>DadosBasicosDoProcessosOuTecnicasNaturezaValues</code> for all possible values.
	 *  <br>
	 *  To unset this attribute set a <code>int</code> value of <code>-1</code>.
	 *
	 *  @param value the new value of the <code>natureza</code> property.
	 *
	 *  @throws IllegalArgumentException if the specified value does not represent a valid
	 *          <i>NATUREZA</i> attribute value.
	 *
	 *  @see DadosBasicosDoProcessosOuTecnicasNaturezaValues
	 */
    public void setNatureza(int value) {
        if (value < -1 || value >= DadosBasicosDoProcessosOuTecnicasNaturezaValues.values.length) {
            throw new IllegalArgumentException("'" + value + "' is not a valid value for property natureza.");
        }
        this.natureza = value;
    }

    /** Sets the <code>natureza</code> property.
	 *  <br>
	 *  This classes property <code>natureza</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>NATUREZA</i> attribute.
	 *
	 *  @param value A <code>String</code> matching a valid
	 *         value of the <code>natureza</code> property or <code>null</code>
	 *         if this attribute shall be unspecified.
	 *
	 *  @throws IllegalArgumentException If the specified <code>String value</code> does not match 
	 *          a valid attribute value.
	 *
	 *  @see DadosBasicosDoProcessosOuTecnicasNaturezaValues
	 */
    public void setNatureza(String value) {
        if (value == null) {
            this.setNatureza(-1);
            return;
        }
        for (int i = 0; i < DadosBasicosDoProcessosOuTecnicasNaturezaValues.values.length; i++) {
            if (DadosBasicosDoProcessosOuTecnicasNaturezaValues.values[i].equals(value)) {
                this.setNatureza(i);
                return;
            }
        }
        throw new IllegalArgumentException("'" + value + "' is not a valid value for property natureza.");
    }

    /** Accesses the <code>tituloDoProcesso</code> property.<br>
	 *  The property <code>tituloDoProcesso</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>TITULO-DO-PROCESSO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>tituloDoProcesso</code> property.
	 */
    public String getTituloDoProcesso() {
        return this.tituloDoProcesso;
    }

    /** Sets the <code>tituloDoProcesso</code> property.<br>
	 *  The <code>tituloDoProcesso</code> property corresponds to the
	 *  <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>TITULO-DO-PROCESSO</i> attribute.
	 *
	 *  @param tituloDoProcesso the new value of the <code>tituloDoProcesso</code> property.
	 */
    public void setTituloDoProcesso(String tituloDoProcesso) {
        this.tituloDoProcesso = tituloDoProcesso;
    }

    /** Accesses the <code>ano</code> property.<br>
	 *  The property <code>ano</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>ANO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>ano</code> property.
	 */
    public String getAno() {
        return this.ano;
    }

    /** Sets the <code>ano</code> property.<br>
	 *  The <code>ano</code> property corresponds to the
	 *  <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>ANO</i> attribute.
	 *
	 *  @param ano the new value of the <code>ano</code> property.
	 */
    public void setAno(String ano) {
        this.ano = ano;
    }

    /** Accesses the <code>pais</code> property.<br>
	 *  The property <code>pais</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>PAIS</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>pais</code> property.
	 */
    public String getPais() {
        return this.pais;
    }

    /** Sets the <code>pais</code> property.<br>
	 *  The <code>pais</code> property corresponds to the
	 *  <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>PAIS</i> attribute.
	 *
	 *  @param pais the new value of the <code>pais</code> property.
	 */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /** Accesses the <code>idioma</code> property.<br>
	 *  The property <code>idioma</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>IDIOMA</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>idioma</code> property.
	 */
    public String getIdioma() {
        return this.idioma;
    }

    /** Sets the <code>idioma</code> property.<br>
	 *  The <code>idioma</code> property corresponds to the
	 *  <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>IDIOMA</i> attribute.
	 *
	 *  @param idioma the new value of the <code>idioma</code> property.
	 */
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    /** Accesses the <code>meioDeDivulgacao</code> property.<br>
	 *  The property <code>meioDeDivulgacao</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>MEIO-DE-DIVULGACAO</i> attribute.
	 *
	 *  @return A <code>int</code> representing the value of the <code>meioDeDivulgacao</code> property.
	 *
	 *  @see MeioDeDivulgacaoValues
	 */
    public int getMeioDeDivulgacao() {
        return this.meioDeDivulgacao;
    }

    /** Accesses a <code>String</code> representation of 
	 *  the <code>meioDeDivulgacao</code> property.<br>
	 *  The property <code>meioDeDivulgacao</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>MEIO-DE-DIVULGACAO</i> attribute.
	 *
	 *  @return One of <i>'IMPRESSO'</i>, <i>'MEIO_MAGNETICO'</i>, <i>'MEIO_DIGITAL'</i>, <i>'FILME'</i>, <i>'HIPERTEXTO'</i>, <i>'OUTRO'</i>, <i>'VARIOS'</i> or <i>'NAO_INFORMADO'</i>, depending on the current value of the property <code>meioDeDivulgacao</code>.
	 *
	 *  @see MeioDeDivulgacaoValues
	 */
    public String getMeioDeDivulgacaoAsString() {
        return MeioDeDivulgacaoValues.values[this.getMeioDeDivulgacao()];
    }

    /** Sets the <code>meioDeDivulgacao</code> property.
	 *  <br>
	 *  The property <code>meioDeDivulgacao</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>MEIO-DE-DIVULGACAO</i> attribute.
	 *  <br>
	 *  The enumerated attribute is representent by <code>int</code> values. Consult the 
	 *  documentation of interface <code>MeioDeDivulgacaoValues</code> for all possible values.
	 *  <br>
	 *  To unset this attribute set a <code>int</code> value of <code>-1</code>.
	 *
	 *  @param value the new value of the <code>meioDeDivulgacao</code> property.
	 *
	 *  @throws IllegalArgumentException if the specified value does not represent a valid
	 *          <i>MEIO-DE-DIVULGACAO</i> attribute value.
	 *
	 *  @see MeioDeDivulgacaoValues
	 */
    public void setMeioDeDivulgacao(int value) {
        if (value < -1 || value >= MeioDeDivulgacaoValues.values.length) {
            throw new IllegalArgumentException("'" + value + "' is not a valid value for property meioDeDivulgacao.");
        }
        this.meioDeDivulgacao = value;
    }

    /** Sets the <code>meioDeDivulgacao</code> property.
	 *  <br>
	 *  This classes property <code>meioDeDivulgacao</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>MEIO-DE-DIVULGACAO</i> attribute.
	 *
	 *  @param value A <code>String</code> representing the new 
	 *         value of the <code>meioDeDivulgacao</code> property. It has to
	 *         match a valid attribute value.
	 *
	 *  @throws IllegalArgumentException If the specified <code>String value</code> does not match 
	 *          a valid attribute value.
	 *
	 *  @see MeioDeDivulgacaoValues
	 */
    public void setMeioDeDivulgacao(String value) {
        for (int i = 0; i < MeioDeDivulgacaoValues.values.length; i++) {
            if (MeioDeDivulgacaoValues.values[i].equals(value)) {
                this.setMeioDeDivulgacao(i);
                return;
            }
        }
        throw new IllegalArgumentException("'" + value + "' is not a valid value for property meioDeDivulgacao.");
    }

    /** Accesses the <code>homePageDoTrabalho</code> property.<br>
	 *  The property <code>homePageDoTrabalho</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>HOME-PAGE-DO-TRABALHO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>homePageDoTrabalho</code> property.
	 */
    public String getHomePageDoTrabalho() {
        return this.homePageDoTrabalho;
    }

    /** Sets the <code>homePageDoTrabalho</code> property.<br>
	 *  The <code>homePageDoTrabalho</code> property corresponds to the
	 *  <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>HOME-PAGE-DO-TRABALHO</i> attribute.
	 *
	 *  @param homePageDoTrabalho the new value of the <code>homePageDoTrabalho</code> property.
	 */
    public void setHomePageDoTrabalho(String homePageDoTrabalho) {
        this.homePageDoTrabalho = homePageDoTrabalho;
    }

    /** Accesses the <code>flagRelevancia</code> property.<br>
	 *  The property <code>flagRelevancia</code> corresponds to
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>FLAG-RELEVANCIA</i> attribute.
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
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>FLAG-RELEVANCIA</i> attribute.
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
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>FLAG-RELEVANCIA</i> attribute.
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
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>FLAG-RELEVANCIA</i> attribute.
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
	 *  the <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>DOI</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>doi</code> property.
	 */
    public String getDoi() {
        return this.doi;
    }

    /** Sets the <code>doi</code> property.<br>
	 *  The <code>doi</code> property corresponds to the
	 *  <i>DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS</i> element's <i>DOI</i> attribute.
	 *
	 *  @param doi the new value of the <code>doi</code> property.
	 */
    public void setDoi(String doi) {
        this.doi = doi;
    }
}
