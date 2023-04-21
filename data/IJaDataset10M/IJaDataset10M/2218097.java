package br.ufrj.cad.model.usuario.xmllattes.DTD;

/**
 *  Class corresponding to a <i>DETALHAMENTO-DO-TRABALHO</i> element in XML.
 *  <p>
 *  @author Generated with jNerd's XML2Java tool Version 1.2 Preview 2 on Tue Apr 14 16:30:39 BRT 2009
 *  @author <br>Please send BugReports to <a href="mailto:xml2java@jNerd.de">xml2java@jNerd.de</a>
 */
public class DetalhamentoDoTrabalho implements ClassificacaoDoEventoValues, XMLElement {

    /** The name of this class' corresponding XML element. (<i>"DETALHAMENTO-DO-TRABALHO"</i>) */
    public static final String ELEMENT_NAME = "DETALHAMENTO-DO-TRABALHO";

    /** Property <code>classificacaoDoEvento</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>CLASSIFICACAO-DO-EVENTO</i> enumeration attribute.<br>
	 *  All valid values are stored in <code>ClassificacaoDoEventoValues</code>.
	 *
 *  @see ClassificacaoDoEventoValues
	 */
    private int classificacaoDoEvento = 4;

    /** Property <code>nomeDoEvento</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>NOME-DO-EVENTO</i> attribute.
	 */
    private String nomeDoEvento;

    /** Property <code>cidadeDoEvento</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>CIDADE-DO-EVENTO</i> attribute.
	 */
    private String cidadeDoEvento;

    /** Property <code>anoDeRealizacao</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>ANO-DE-REALIZACAO</i> attribute.
	 */
    private String anoDeRealizacao;

    /** Property <code>tituloDosAnaisOuProceedings</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>TITULO-DOS-ANAIS-OU-PROCEEDINGS</i> attribute.
	 */
    private String tituloDosAnaisOuProceedings;

    /** Property <code>volume</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>VOLUME</i> attribute.
	 */
    private String volume;

    /** Property <code>fasciculo</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>FASCICULO</i> attribute.
	 */
    private String fasciculo;

    /** Property <code>serie</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>SERIE</i> attribute.
	 */
    private String serie;

    /** Property <code>paginaInicial</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>PAGINA-INICIAL</i> attribute.
	 */
    private String paginaInicial;

    /** Property <code>paginaFinal</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>PAGINA-FINAL</i> attribute.
	 */
    private String paginaFinal;

    /** Property <code>isbn</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>ISBN</i> attribute.
	 */
    private String isbn;

    /** Property <code>nomeDaEditora</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>NOME-DA-EDITORA</i> attribute.
	 */
    private String nomeDaEditora;

    /** Property <code>cidadeDaEditora</code> is corresponding
	 *  to the <i>DETALHAMENTO-DO-TRABALHO</i> element's
	 *  <i>CIDADE-DA-EDITORA</i> attribute.
	 */
    private String cidadeDaEditora;

    /** Accesses the XML element name.
	 *  @return <i>"DETALHAMENTO-DO-TRABALHO"</i> which is this class' corresponding XML Element.
	 */
    public String getElementName() {
        return ELEMENT_NAME;
    }

    /** Accesses the <code>classificacaoDoEvento</code> property.<br>
	 *  The property <code>classificacaoDoEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>CLASSIFICACAO-DO-EVENTO</i> attribute.
	 *
	 *  @return A <code>int</code> representing the value of the <code>classificacaoDoEvento</code> property.
	 *
	 *  @see ClassificacaoDoEventoValues
	 */
    public int getClassificacaoDoEvento() {
        return this.classificacaoDoEvento;
    }

    /** Accesses a <code>String</code> representation of 
	 *  the <code>classificacaoDoEvento</code> property.<br>
	 *  The property <code>classificacaoDoEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>CLASSIFICACAO-DO-EVENTO</i> attribute.
	 *
	 *  @return One of <i>'INTERNACIONAL'</i>, <i>'NACIONAL'</i>, <i>'REGIONAL'</i>, <i>'LOCAL'</i> or <i>'NAO_INFORMADO'</i>, depending on the current value of the property <code>classificacaoDoEvento</code>.
	 *
	 *  @see ClassificacaoDoEventoValues
	 */
    public String getClassificacaoDoEventoAsString() {
        return ClassificacaoDoEventoValues.values[this.getClassificacaoDoEvento()];
    }

    /** Sets the <code>classificacaoDoEvento</code> property.
	 *  <br>
	 *  The property <code>classificacaoDoEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>CLASSIFICACAO-DO-EVENTO</i> attribute.
	 *  <br>
	 *  The enumerated attribute is representent by <code>int</code> values. Consult the 
	 *  documentation of interface <code>ClassificacaoDoEventoValues</code> for all possible values.
	 *  <br>
	 *  To unset this attribute set a <code>int</code> value of <code>-1</code>.
	 *
	 *  @param value the new value of the <code>classificacaoDoEvento</code> property.
	 *
	 *  @throws IllegalArgumentException if the specified value does not represent a valid
	 *          <i>CLASSIFICACAO-DO-EVENTO</i> attribute value.
	 *
	 *  @see ClassificacaoDoEventoValues
	 */
    public void setClassificacaoDoEvento(int value) {
        if (value < -1 || value >= ClassificacaoDoEventoValues.values.length) {
            throw new IllegalArgumentException("'" + value + "' is not a valid value for property classificacaoDoEvento.");
        }
        this.classificacaoDoEvento = value;
    }

    /** Sets the <code>classificacaoDoEvento</code> property.
	 *  <br>
	 *  This classes property <code>classificacaoDoEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>CLASSIFICACAO-DO-EVENTO</i> attribute.
	 *
	 *  @param value A <code>String</code> representing the new 
	 *         value of the <code>classificacaoDoEvento</code> property. It has to
	 *         match a valid attribute value.
	 *
	 *  @throws IllegalArgumentException If the specified <code>String value</code> does not match 
	 *          a valid attribute value.
	 *
	 *  @see ClassificacaoDoEventoValues
	 */
    public void setClassificacaoDoEvento(String value) {
        for (int i = 0; i < ClassificacaoDoEventoValues.values.length; i++) {
            if (ClassificacaoDoEventoValues.values[i].equals(value)) {
                this.setClassificacaoDoEvento(i);
                return;
            }
        }
        throw new IllegalArgumentException("'" + value + "' is not a valid value for property classificacaoDoEvento.");
    }

    /** Accesses the <code>nomeDoEvento</code> property.<br>
	 *  The property <code>nomeDoEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>NOME-DO-EVENTO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>nomeDoEvento</code> property.
	 */
    public String getNomeDoEvento() {
        return this.nomeDoEvento;
    }

    /** Sets the <code>nomeDoEvento</code> property.<br>
	 *  The <code>nomeDoEvento</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>NOME-DO-EVENTO</i> attribute.
	 *
	 *  @param nomeDoEvento the new value of the <code>nomeDoEvento</code> property.
	 */
    public void setNomeDoEvento(String nomeDoEvento) {
        this.nomeDoEvento = nomeDoEvento;
    }

    /** Accesses the <code>cidadeDoEvento</code> property.<br>
	 *  The property <code>cidadeDoEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>CIDADE-DO-EVENTO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>cidadeDoEvento</code> property.
	 */
    public String getCidadeDoEvento() {
        return this.cidadeDoEvento;
    }

    /** Sets the <code>cidadeDoEvento</code> property.<br>
	 *  The <code>cidadeDoEvento</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>CIDADE-DO-EVENTO</i> attribute.
	 *
	 *  @param cidadeDoEvento the new value of the <code>cidadeDoEvento</code> property.
	 */
    public void setCidadeDoEvento(String cidadeDoEvento) {
        this.cidadeDoEvento = cidadeDoEvento;
    }

    /** Accesses the <code>anoDeRealizacao</code> property.<br>
	 *  The property <code>anoDeRealizacao</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>ANO-DE-REALIZACAO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>anoDeRealizacao</code> property.
	 */
    public String getAnoDeRealizacao() {
        return this.anoDeRealizacao;
    }

    /** Sets the <code>anoDeRealizacao</code> property.<br>
	 *  The <code>anoDeRealizacao</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>ANO-DE-REALIZACAO</i> attribute.
	 *
	 *  @param anoDeRealizacao the new value of the <code>anoDeRealizacao</code> property.
	 */
    public void setAnoDeRealizacao(String anoDeRealizacao) {
        this.anoDeRealizacao = anoDeRealizacao;
    }

    /** Accesses the <code>tituloDosAnaisOuProceedings</code> property.<br>
	 *  The property <code>tituloDosAnaisOuProceedings</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>TITULO-DOS-ANAIS-OU-PROCEEDINGS</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>tituloDosAnaisOuProceedings</code> property.
	 */
    public String getTituloDosAnaisOuProceedings() {
        return this.tituloDosAnaisOuProceedings;
    }

    /** Sets the <code>tituloDosAnaisOuProceedings</code> property.<br>
	 *  The <code>tituloDosAnaisOuProceedings</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>TITULO-DOS-ANAIS-OU-PROCEEDINGS</i> attribute.
	 *
	 *  @param tituloDosAnaisOuProceedings the new value of the <code>tituloDosAnaisOuProceedings</code> property.
	 */
    public void setTituloDosAnaisOuProceedings(String tituloDosAnaisOuProceedings) {
        this.tituloDosAnaisOuProceedings = tituloDosAnaisOuProceedings;
    }

    /** Accesses the <code>volume</code> property.<br>
	 *  The property <code>volume</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>VOLUME</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>volume</code> property.
	 */
    public String getVolume() {
        return this.volume;
    }

    /** Sets the <code>volume</code> property.<br>
	 *  The <code>volume</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>VOLUME</i> attribute.
	 *
	 *  @param volume the new value of the <code>volume</code> property.
	 */
    public void setVolume(String volume) {
        this.volume = volume;
    }

    /** Accesses the <code>fasciculo</code> property.<br>
	 *  The property <code>fasciculo</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>FASCICULO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>fasciculo</code> property.
	 */
    public String getFasciculo() {
        return this.fasciculo;
    }

    /** Sets the <code>fasciculo</code> property.<br>
	 *  The <code>fasciculo</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>FASCICULO</i> attribute.
	 *
	 *  @param fasciculo the new value of the <code>fasciculo</code> property.
	 */
    public void setFasciculo(String fasciculo) {
        this.fasciculo = fasciculo;
    }

    /** Accesses the <code>serie</code> property.<br>
	 *  The property <code>serie</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>SERIE</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>serie</code> property.
	 */
    public String getSerie() {
        return this.serie;
    }

    /** Sets the <code>serie</code> property.<br>
	 *  The <code>serie</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>SERIE</i> attribute.
	 *
	 *  @param serie the new value of the <code>serie</code> property.
	 */
    public void setSerie(String serie) {
        this.serie = serie;
    }

    /** Accesses the <code>paginaInicial</code> property.<br>
	 *  The property <code>paginaInicial</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>PAGINA-INICIAL</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>paginaInicial</code> property.
	 */
    public String getPaginaInicial() {
        return this.paginaInicial;
    }

    /** Sets the <code>paginaInicial</code> property.<br>
	 *  The <code>paginaInicial</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>PAGINA-INICIAL</i> attribute.
	 *
	 *  @param paginaInicial the new value of the <code>paginaInicial</code> property.
	 */
    public void setPaginaInicial(String paginaInicial) {
        this.paginaInicial = paginaInicial;
    }

    /** Accesses the <code>paginaFinal</code> property.<br>
	 *  The property <code>paginaFinal</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>PAGINA-FINAL</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>paginaFinal</code> property.
	 */
    public String getPaginaFinal() {
        return this.paginaFinal;
    }

    /** Sets the <code>paginaFinal</code> property.<br>
	 *  The <code>paginaFinal</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>PAGINA-FINAL</i> attribute.
	 *
	 *  @param paginaFinal the new value of the <code>paginaFinal</code> property.
	 */
    public void setPaginaFinal(String paginaFinal) {
        this.paginaFinal = paginaFinal;
    }

    /** Accesses the <code>isbn</code> property.<br>
	 *  The property <code>isbn</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>ISBN</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>isbn</code> property.
	 */
    public String getIsbn() {
        return this.isbn;
    }

    /** Sets the <code>isbn</code> property.<br>
	 *  The <code>isbn</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>ISBN</i> attribute.
	 *
	 *  @param isbn the new value of the <code>isbn</code> property.
	 */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /** Accesses the <code>nomeDaEditora</code> property.<br>
	 *  The property <code>nomeDaEditora</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>NOME-DA-EDITORA</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>nomeDaEditora</code> property.
	 */
    public String getNomeDaEditora() {
        return this.nomeDaEditora;
    }

    /** Sets the <code>nomeDaEditora</code> property.<br>
	 *  The <code>nomeDaEditora</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>NOME-DA-EDITORA</i> attribute.
	 *
	 *  @param nomeDaEditora the new value of the <code>nomeDaEditora</code> property.
	 */
    public void setNomeDaEditora(String nomeDaEditora) {
        this.nomeDaEditora = nomeDaEditora;
    }

    /** Accesses the <code>cidadeDaEditora</code> property.<br>
	 *  The property <code>cidadeDaEditora</code> corresponds to
	 *  the <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>CIDADE-DA-EDITORA</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>cidadeDaEditora</code> property.
	 */
    public String getCidadeDaEditora() {
        return this.cidadeDaEditora;
    }

    /** Sets the <code>cidadeDaEditora</code> property.<br>
	 *  The <code>cidadeDaEditora</code> property corresponds to the
	 *  <i>DETALHAMENTO-DO-TRABALHO</i> element's <i>CIDADE-DA-EDITORA</i> attribute.
	 *
	 *  @param cidadeDaEditora the new value of the <code>cidadeDaEditora</code> property.
	 */
    public void setCidadeDaEditora(String cidadeDaEditora) {
        this.cidadeDaEditora = cidadeDaEditora;
    }
}
