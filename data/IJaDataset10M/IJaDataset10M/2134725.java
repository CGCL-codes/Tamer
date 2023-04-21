package br.com.jops.cih.form;

import br.com.jops.cci.NoticiaPesquisaForm;
import br.com.jops.cci.CtrlJopsServlet;
import br.com.jops.cci.NoticiaForm;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: Welton
 * Date: 20/08/2007
 * Time: 19:40:09
 * Define formulario e escopo de
 * edicao ou criacao de noticia
 */
public class NoticiaEditorFormTag extends FormTag {

    private NoticiaForm form;

    public NoticiaForm obterForm() {
        return form;
    }

    public int doStartTag() throws JspException {
        form = NoticiaForm.obterForm((HttpServletRequest) pageContext.getRequest());
        if (form == null) {
            form = new NoticiaForm();
            form.popular((HttpServletRequest) pageContext.getRequest());
            form.armazenaForm((HttpServletRequest) pageContext.getRequest());
        }
        return super.doStartTag();
    }

    public int doEndTag() throws JspException {
        form = null;
        return super.doEndTag();
    }

    public void release() {
        super.release();
        form = null;
    }

    protected String obterFormId() {
        return null;
    }

    protected boolean renderizarForm() {
        return true;
    }

    protected String obterAction() {
        return CtrlJopsServlet.REQ_SALVAR_NOTICIA;
    }
}
