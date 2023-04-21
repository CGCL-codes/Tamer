package html.page;

import html.basic.HTMLBody;
import html.basic.HTMLDiv;
import html.form.AbstractHTMLForm;
import html.form.HTMLDialog;

public abstract class AbstractPageDialog extends AbstractPage {

    String m_titlePageDialog;

    public AbstractPageDialog(String title, String fileCSS) {
        super(title, fileCSS);
        m_titlePageDialog = title;
    }

    protected abstract AbstractHTMLForm form();

    protected final HTMLBody body() {
        HTMLBody body;
        HTMLDialog dialogo;
        AbstractHTMLForm formulario;
        HTMLDiv capaDialogo;
        formulario = form();
        dialogo = new HTMLDialog(m_titlePageDialog, formulario);
        capaDialogo = new HTMLDiv("posicionaDialogoCentro");
        capaDialogo.add(dialogo);
        body = new HTMLBody();
        body.add(capaDialogo);
        return body;
    }
}
