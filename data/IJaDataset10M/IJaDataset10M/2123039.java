package br.ufrj.cad.model.usuario.xmllattes.DTD;

/**
 *  Class corresponding to a <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element in XML.
 *  <p>
 *  @author Generated with jNerd's XML2Java tool Version 1.2 Preview 2 on Tue Apr 14 16:30:42 BRT 2009
 *  @author <br>Please send BugReports to <a href="mailto:xml2java@jNerd.de">xml2java@jNerd.de</a>
 */
public class DetalhamentoDaApresentacaoDeObraArtistica implements DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues, AtividadeDosAutoresValues, XMLElement {

    /** The name of this class' corresponding XML element. (<i>"DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA"</i>) */
    public static final String ELEMENT_NAME = "DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA";

    /** Property <code>tipoDeEvento</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's
	 *  <i>TIPO-DE-EVENTO</i> enumeration attribute.<br>
	 *  All valid values are stored in <code>DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues</code>.
	 *
 *  @see DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues
	 */
    private int tipoDeEvento = -1;

    /** Property <code>atividadeDosAutores</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's
	 *  <i>ATIVIDADE-DOS-AUTORES</i> enumeration attribute.<br>
	 *  All valid values are stored in <code>AtividadeDosAutoresValues</code>.
	 *
 *  @see AtividadeDosAutoresValues
	 */
    private int atividadeDosAutores = -1;

    /** Property <code>flagIneditismoDaObra</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's
	 *  <i>FLAG-INEDITISMO-DA-OBRA</i> attribute.
	 */
    private String flagIneditismoDaObra;

    /** Property <code>premiacao</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's
	 *  <i>PREMIACAO</i> attribute.
	 */
    private String premiacao;

    /** Property <code>obraDeReferencia</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's
	 *  <i>OBRA-DE-REFERENCIA</i> attribute.
	 */
    private String obraDeReferencia;

    /** Property <code>autorDaObraDeReferencia</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's
	 *  <i>AUTOR-DA-OBRA-DE-REFERENCIA</i> attribute.
	 */
    private String autorDaObraDeReferencia;

    /** Property <code>anoDaObraDeReferencia</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's
	 *  <i>ANO-DA-OBRA-DE-REFERENCIA</i> attribute.
	 */
    private String anoDaObraDeReferencia;

    /** Property <code>duracaoEmMinutos</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's
	 *  <i>DURACAO-EM-MINUTOS</i> attribute.
	 */
    private String duracaoEmMinutos;

    /** Property <code>instituicaoPromotoraDoEvento</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's
	 *  <i>INSTITUICAO-PROMOTORA-DO-EVENTO</i> attribute.
	 */
    private String instituicaoPromotoraDoEvento;

    /** Property <code>localDoEvento</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's
	 *  <i>LOCAL-DO-EVENTO</i> attribute.
	 */
    private String localDoEvento;

    /** Property <code>cidade</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's
	 *  <i>CIDADE</i> attribute.
	 */
    private String cidade;

    /** Accesses the XML element name.
	 *  @return <i>"DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA"</i> which is this class' corresponding XML Element.
	 */
    public String getElementName() {
        return ELEMENT_NAME;
    }

    /** Accesses the <code>tipoDeEvento</code> property.<br>
	 *  The property <code>tipoDeEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's implied <i>TIPO-DE-EVENTO</i> 
	 *  attribute.
	 *
	 *  @return A <code>int</code> representing the value of the 
	 *          <code>tipoDeEvento</code> property or <code>-1</code> if this
	 *          attribute is unspecified.
	 *
	 *  @see DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues
	 */
    public int getTipoDeEvento() {
        return this.tipoDeEvento;
    }

    /** Accesses a <code>String</code> representation of 
	 *  the <code>tipoDeEvento</code> property.<br>
	 *  The property <code>tipoDeEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's implied <i>TIPO-DE-EVENTO</i> 
	 *  attribute.
	 *
	 *  @return One of <i>'CONCERTO'</i>, <i>'CONCURSO'</i>, <i>'FESTIVAL'</i>, <i>'GRAVACAO'</i>, <i>'RECITAL'</i>, <i>'OUTRO'</i> or <i>'NAO_INFORMADO'</i>, depending on the current value of the property 
	 *          <code>tipoDeEvento</code> or <code>null</code> if this 
	 *          attribute is unspecified.
	 *
	 *  @see DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues
	 */
    public String getTipoDeEventoAsString() {
        if (this.getTipoDeEvento() == -1) {
            return null;
        }
        return DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues.values[this.getTipoDeEvento()];
    }

    /** Sets the <code>tipoDeEvento</code> property.
	 *  <br>
	 *  The property <code>tipoDeEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>TIPO-DE-EVENTO</i> attribute.
	 *  <br>
	 *  The enumerated attribute is representent by <code>int</code> values. Consult the 
	 *  documentation of interface <code>DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues</code> for all possible values.
	 *  <br>
	 *  To unset this attribute set a <code>int</code> value of <code>-1</code>.
	 *
	 *  @param value the new value of the <code>tipoDeEvento</code> property.
	 *
	 *  @throws IllegalArgumentException if the specified value does not represent a valid
	 *          <i>TIPO-DE-EVENTO</i> attribute value.
	 *
	 *  @see DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues
	 */
    public void setTipoDeEvento(int value) {
        if (value < -1 || value >= DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues.values.length) {
            throw new IllegalArgumentException("'" + value + "' is not a valid value for property tipoDeEvento.");
        }
        this.tipoDeEvento = value;
    }

    /** Sets the <code>tipoDeEvento</code> property.
	 *  <br>
	 *  This classes property <code>tipoDeEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>TIPO-DE-EVENTO</i> attribute.
	 *
	 *  @param value A <code>String</code> matching a valid
	 *         value of the <code>tipoDeEvento</code> property or <code>null</code>
	 *         if this attribute shall be unspecified.
	 *
	 *  @throws IllegalArgumentException If the specified <code>String value</code> does not match 
	 *          a valid attribute value.
	 *
	 *  @see DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues
	 */
    public void setTipoDeEvento(String value) {
        if (value == null) {
            this.setTipoDeEvento(-1);
            return;
        }
        for (int i = 0; i < DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues.values.length; i++) {
            if (DetalhamentoDaApresentacaoDeObraArtisticaTipoDeEventoValues.values[i].equals(value)) {
                this.setTipoDeEvento(i);
                return;
            }
        }
        throw new IllegalArgumentException("'" + value + "' is not a valid value for property tipoDeEvento.");
    }

    /** Accesses the <code>atividadeDosAutores</code> property.<br>
	 *  The property <code>atividadeDosAutores</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's implied <i>ATIVIDADE-DOS-AUTORES</i> 
	 *  attribute.
	 *
	 *  @return A <code>int</code> representing the value of the 
	 *          <code>atividadeDosAutores</code> property or <code>-1</code> if this
	 *          attribute is unspecified.
	 *
	 *  @see AtividadeDosAutoresValues
	 */
    public int getAtividadeDosAutores() {
        return this.atividadeDosAutores;
    }

    /** Accesses a <code>String</code> representation of 
	 *  the <code>atividadeDosAutores</code> property.<br>
	 *  The property <code>atividadeDosAutores</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's implied <i>ATIVIDADE-DOS-AUTORES</i> 
	 *  attribute.
	 *
	 *  @return One of <i>'CANTO'</i>, <i>'CRIACAO'</i>, <i>'DANCA'</i>, <i>'DIRECAO'</i>, <i>'ENCENACAO'</i>, <i>'INSTRUMENTO_MUSICAL'</i>, <i>'REGENCIA'</i>, <i>'ROTEIRO'</i>, <i>'OUTRA'</i>, <i>'VARIAS'</i> or <i>'NAO_INFORMADO'</i>, depending on the current value of the property 
	 *          <code>atividadeDosAutores</code> or <code>null</code> if this 
	 *          attribute is unspecified.
	 *
	 *  @see AtividadeDosAutoresValues
	 */
    public String getAtividadeDosAutoresAsString() {
        if (this.getAtividadeDosAutores() == -1) {
            return null;
        }
        return AtividadeDosAutoresValues.values[this.getAtividadeDosAutores()];
    }

    /** Sets the <code>atividadeDosAutores</code> property.
	 *  <br>
	 *  The property <code>atividadeDosAutores</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>ATIVIDADE-DOS-AUTORES</i> attribute.
	 *  <br>
	 *  The enumerated attribute is representent by <code>int</code> values. Consult the 
	 *  documentation of interface <code>AtividadeDosAutoresValues</code> for all possible values.
	 *  <br>
	 *  To unset this attribute set a <code>int</code> value of <code>-1</code>.
	 *
	 *  @param value the new value of the <code>atividadeDosAutores</code> property.
	 *
	 *  @throws IllegalArgumentException if the specified value does not represent a valid
	 *          <i>ATIVIDADE-DOS-AUTORES</i> attribute value.
	 *
	 *  @see AtividadeDosAutoresValues
	 */
    public void setAtividadeDosAutores(int value) {
        if (value < -1 || value >= AtividadeDosAutoresValues.values.length) {
            throw new IllegalArgumentException("'" + value + "' is not a valid value for property atividadeDosAutores.");
        }
        this.atividadeDosAutores = value;
    }

    /** Sets the <code>atividadeDosAutores</code> property.
	 *  <br>
	 *  This classes property <code>atividadeDosAutores</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>ATIVIDADE-DOS-AUTORES</i> attribute.
	 *
	 *  @param value A <code>String</code> matching a valid
	 *         value of the <code>atividadeDosAutores</code> property or <code>null</code>
	 *         if this attribute shall be unspecified.
	 *
	 *  @throws IllegalArgumentException If the specified <code>String value</code> does not match 
	 *          a valid attribute value.
	 *
	 *  @see AtividadeDosAutoresValues
	 */
    public void setAtividadeDosAutores(String value) {
        if (value == null) {
            this.setAtividadeDosAutores(-1);
            return;
        }
        for (int i = 0; i < AtividadeDosAutoresValues.values.length; i++) {
            if (AtividadeDosAutoresValues.values[i].equals(value)) {
                this.setAtividadeDosAutores(i);
                return;
            }
        }
        throw new IllegalArgumentException("'" + value + "' is not a valid value for property atividadeDosAutores.");
    }

    /** Accesses the <code>flagIneditismoDaObra</code> property.<br>
	 *  The property <code>flagIneditismoDaObra</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>FLAG-INEDITISMO-DA-OBRA</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>flagIneditismoDaObra</code> property.
	 */
    public String getFlagIneditismoDaObra() {
        return this.flagIneditismoDaObra;
    }

    /** Sets the <code>flagIneditismoDaObra</code> property.<br>
	 *  The <code>flagIneditismoDaObra</code> property corresponds to the
	 *  <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>FLAG-INEDITISMO-DA-OBRA</i> attribute.
	 *
	 *  @param flagIneditismoDaObra the new value of the <code>flagIneditismoDaObra</code> property.
	 */
    public void setFlagIneditismoDaObra(String flagIneditismoDaObra) {
        this.flagIneditismoDaObra = flagIneditismoDaObra;
    }

    /** Accesses the <code>premiacao</code> property.<br>
	 *  The property <code>premiacao</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>PREMIACAO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>premiacao</code> property.
	 */
    public String getPremiacao() {
        return this.premiacao;
    }

    /** Sets the <code>premiacao</code> property.<br>
	 *  The <code>premiacao</code> property corresponds to the
	 *  <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>PREMIACAO</i> attribute.
	 *
	 *  @param premiacao the new value of the <code>premiacao</code> property.
	 */
    public void setPremiacao(String premiacao) {
        this.premiacao = premiacao;
    }

    /** Accesses the <code>obraDeReferencia</code> property.<br>
	 *  The property <code>obraDeReferencia</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>OBRA-DE-REFERENCIA</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>obraDeReferencia</code> property.
	 */
    public String getObraDeReferencia() {
        return this.obraDeReferencia;
    }

    /** Sets the <code>obraDeReferencia</code> property.<br>
	 *  The <code>obraDeReferencia</code> property corresponds to the
	 *  <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>OBRA-DE-REFERENCIA</i> attribute.
	 *
	 *  @param obraDeReferencia the new value of the <code>obraDeReferencia</code> property.
	 */
    public void setObraDeReferencia(String obraDeReferencia) {
        this.obraDeReferencia = obraDeReferencia;
    }

    /** Accesses the <code>autorDaObraDeReferencia</code> property.<br>
	 *  The property <code>autorDaObraDeReferencia</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>AUTOR-DA-OBRA-DE-REFERENCIA</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>autorDaObraDeReferencia</code> property.
	 */
    public String getAutorDaObraDeReferencia() {
        return this.autorDaObraDeReferencia;
    }

    /** Sets the <code>autorDaObraDeReferencia</code> property.<br>
	 *  The <code>autorDaObraDeReferencia</code> property corresponds to the
	 *  <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>AUTOR-DA-OBRA-DE-REFERENCIA</i> attribute.
	 *
	 *  @param autorDaObraDeReferencia the new value of the <code>autorDaObraDeReferencia</code> property.
	 */
    public void setAutorDaObraDeReferencia(String autorDaObraDeReferencia) {
        this.autorDaObraDeReferencia = autorDaObraDeReferencia;
    }

    /** Accesses the <code>anoDaObraDeReferencia</code> property.<br>
	 *  The property <code>anoDaObraDeReferencia</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>ANO-DA-OBRA-DE-REFERENCIA</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>anoDaObraDeReferencia</code> property.
	 */
    public String getAnoDaObraDeReferencia() {
        return this.anoDaObraDeReferencia;
    }

    /** Sets the <code>anoDaObraDeReferencia</code> property.<br>
	 *  The <code>anoDaObraDeReferencia</code> property corresponds to the
	 *  <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>ANO-DA-OBRA-DE-REFERENCIA</i> attribute.
	 *
	 *  @param anoDaObraDeReferencia the new value of the <code>anoDaObraDeReferencia</code> property.
	 */
    public void setAnoDaObraDeReferencia(String anoDaObraDeReferencia) {
        this.anoDaObraDeReferencia = anoDaObraDeReferencia;
    }

    /** Accesses the <code>duracaoEmMinutos</code> property.<br>
	 *  The property <code>duracaoEmMinutos</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>DURACAO-EM-MINUTOS</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>duracaoEmMinutos</code> property.
	 */
    public String getDuracaoEmMinutos() {
        return this.duracaoEmMinutos;
    }

    /** Sets the <code>duracaoEmMinutos</code> property.<br>
	 *  The <code>duracaoEmMinutos</code> property corresponds to the
	 *  <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>DURACAO-EM-MINUTOS</i> attribute.
	 *
	 *  @param duracaoEmMinutos the new value of the <code>duracaoEmMinutos</code> property.
	 */
    public void setDuracaoEmMinutos(String duracaoEmMinutos) {
        this.duracaoEmMinutos = duracaoEmMinutos;
    }

    /** Accesses the <code>instituicaoPromotoraDoEvento</code> property.<br>
	 *  The property <code>instituicaoPromotoraDoEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>INSTITUICAO-PROMOTORA-DO-EVENTO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>instituicaoPromotoraDoEvento</code> property.
	 */
    public String getInstituicaoPromotoraDoEvento() {
        return this.instituicaoPromotoraDoEvento;
    }

    /** Sets the <code>instituicaoPromotoraDoEvento</code> property.<br>
	 *  The <code>instituicaoPromotoraDoEvento</code> property corresponds to the
	 *  <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>INSTITUICAO-PROMOTORA-DO-EVENTO</i> attribute.
	 *
	 *  @param instituicaoPromotoraDoEvento the new value of the <code>instituicaoPromotoraDoEvento</code> property.
	 */
    public void setInstituicaoPromotoraDoEvento(String instituicaoPromotoraDoEvento) {
        this.instituicaoPromotoraDoEvento = instituicaoPromotoraDoEvento;
    }

    /** Accesses the <code>localDoEvento</code> property.<br>
	 *  The property <code>localDoEvento</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>LOCAL-DO-EVENTO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>localDoEvento</code> property.
	 */
    public String getLocalDoEvento() {
        return this.localDoEvento;
    }

    /** Sets the <code>localDoEvento</code> property.<br>
	 *  The <code>localDoEvento</code> property corresponds to the
	 *  <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>LOCAL-DO-EVENTO</i> attribute.
	 *
	 *  @param localDoEvento the new value of the <code>localDoEvento</code> property.
	 */
    public void setLocalDoEvento(String localDoEvento) {
        this.localDoEvento = localDoEvento;
    }

    /** Accesses the <code>cidade</code> property.<br>
	 *  The property <code>cidade</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>CIDADE</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>cidade</code> property.
	 */
    public String getCidade() {
        return this.cidade;
    }

    /** Sets the <code>cidade</code> property.<br>
	 *  The <code>cidade</code> property corresponds to the
	 *  <i>DETALHAMENTO-DA-APRESENTACAO-DE-OBRA-ARTISTICA</i> element's <i>CIDADE</i> attribute.
	 *
	 *  @param cidade the new value of the <code>cidade</code> property.
	 */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
