package br.ufrj.cad.model.usuario.xmllattes.DTD;

/**
 *  Class corresponding to a <i>DETALHAMENTO-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO</i> element in XML.
 *  <p>
 *  @author Generated with jNerd's XML2Java tool Version 1.2 Preview 2 on Tue Apr 14 16:30:41 BRT 2009
 *  @author <br>Please send BugReports to <a href="mailto:xml2java@jNerd.de">xml2java@jNerd.de</a>
 */
public class DetalhamentoDaBancaJulgadoraParaConcursoPublico implements XMLElement {

    /** The name of this class' corresponding XML element. (<i>"DETALHAMENTO-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO"</i>) */
    public static final String ELEMENT_NAME = "DETALHAMENTO-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO";

    /** Property <code>codigoInstituicao</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO</i> element's
	 *  <i>CODIGO-INSTITUICAO</i> attribute.
	 */
    private String codigoInstituicao;

    /** Property <code>nomeInstituicao</code> is corresponding
	 *  to the <i>DETALHAMENTO-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO</i> element's
	 *  <i>NOME-INSTITUICAO</i> attribute.
	 */
    private String nomeInstituicao;

    /** Accesses the XML element name.
	 *  @return <i>"DETALHAMENTO-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO"</i> which is this class' corresponding XML Element.
	 */
    public String getElementName() {
        return ELEMENT_NAME;
    }

    /** Accesses the <code>codigoInstituicao</code> property.<br>
	 *  The property <code>codigoInstituicao</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO</i> element's <i>CODIGO-INSTITUICAO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>codigoInstituicao</code> property.
	 */
    public String getCodigoInstituicao() {
        return this.codigoInstituicao;
    }

    /** Sets the <code>codigoInstituicao</code> property.<br>
	 *  The <code>codigoInstituicao</code> property corresponds to the
	 *  <i>DETALHAMENTO-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO</i> element's <i>CODIGO-INSTITUICAO</i> attribute.
	 *
	 *  @param codigoInstituicao the new value of the <code>codigoInstituicao</code> property.
	 */
    public void setCodigoInstituicao(String codigoInstituicao) {
        this.codigoInstituicao = codigoInstituicao;
    }

    /** Accesses the <code>nomeInstituicao</code> property.<br>
	 *  The property <code>nomeInstituicao</code> corresponds to
	 *  the <i>DETALHAMENTO-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO</i> element's <i>NOME-INSTITUICAO</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>nomeInstituicao</code> property.
	 */
    public String getNomeInstituicao() {
        return this.nomeInstituicao;
    }

    /** Sets the <code>nomeInstituicao</code> property.<br>
	 *  The <code>nomeInstituicao</code> property corresponds to the
	 *  <i>DETALHAMENTO-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO</i> element's <i>NOME-INSTITUICAO</i> attribute.
	 *
	 *  @param nomeInstituicao the new value of the <code>nomeInstituicao</code> property.
	 */
    public void setNomeInstituicao(String nomeInstituicao) {
        this.nomeInstituicao = nomeInstituicao;
    }
}
