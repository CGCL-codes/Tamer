package br.ufrj.cad.model.usuario.xmllattes.DTD;

/**
 *  Class corresponding to a <i>INFORMACOES-ADICIONAIS</i> element in XML.
 *  <p>
 *  @author Generated with jNerd's XML2Java tool Version 1.2 Preview 2 on Tue Apr 14 16:30:41 BRT 2009
 *  @author <br>Please send BugReports to <a href="mailto:xml2java@jNerd.de">xml2java@jNerd.de</a>
 */
public class InformacoesAdicionais implements XMLElement {

    /** The name of this class' corresponding XML element. (<i>"INFORMACOES-ADICIONAIS"</i>) */
    public static final String ELEMENT_NAME = "INFORMACOES-ADICIONAIS";

    /** Property <code>descricaoInformacoesAdicionais</code> is corresponding
	 *  to the <i>INFORMACOES-ADICIONAIS</i> element's
	 *  <i>DESCRICAO-INFORMACOES-ADICIONAIS</i> attribute.
	 */
    private String descricaoInformacoesAdicionais;

    /** Accesses the XML element name.
	 *  @return <i>"INFORMACOES-ADICIONAIS"</i> which is this class' corresponding XML Element.
	 */
    public String getElementName() {
        return ELEMENT_NAME;
    }

    /** Accesses the <code>descricaoInformacoesAdicionais</code> property.<br>
	 *  The property <code>descricaoInformacoesAdicionais</code> corresponds to
	 *  the <i>INFORMACOES-ADICIONAIS</i> element's <i>DESCRICAO-INFORMACOES-ADICIONAIS</i> attribute.
	 *
	 *  @return A <code>String</code> representing the value of the <code>descricaoInformacoesAdicionais</code> property.
	 */
    public String getDescricaoInformacoesAdicionais() {
        return this.descricaoInformacoesAdicionais;
    }

    /** Sets the <code>descricaoInformacoesAdicionais</code> property.<br>
	 *  The <code>descricaoInformacoesAdicionais</code> property corresponds to the
	 *  <i>INFORMACOES-ADICIONAIS</i> element's <i>DESCRICAO-INFORMACOES-ADICIONAIS</i> attribute.
	 *
	 *  @param descricaoInformacoesAdicionais the new value of the <code>descricaoInformacoesAdicionais</code> property.
	 */
    public void setDescricaoInformacoesAdicionais(String descricaoInformacoesAdicionais) {
        this.descricaoInformacoesAdicionais = descricaoInformacoesAdicionais;
    }
}
