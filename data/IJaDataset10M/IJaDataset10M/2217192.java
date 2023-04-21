package prodocUI.forms;

import html.*;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jhierrot
 */
public class FMessageQuestion extends FFormBase {

    /** Creates a new instance of FormularioLogin
 * @param Req 
 * @param pTitulo
 * @param pCabecera
 * @param Message
 * @param Url  
 */
    public FMessageQuestion(HttpServletRequest Req, String pTitulo, String pCabecera, String Message, String Url) {
        super(Req, pTitulo, ADDMOD, null);
        Table FormTab = new Table(2, 4, 0);
        FormTab.setCSSClass("FFormularios");
        FormTab.getCelda(0, 0).setWidth(-40);
        FormTab.getCelda(1, 0).AddElem(new Element(Message));
        FormTab.getCelda(1, 2).AddElem(OkButton);
        FormTab.getCelda(1, 2).AddElem(Element.getEspacio2());
        FormTab.getCelda(1, 2).AddElem(CancelButton);
        Form LoginForm = new Form(Url, "FormVal");
        LoginForm.AddElem(FormTab);
        AddElem(LoginForm);
    }
}
