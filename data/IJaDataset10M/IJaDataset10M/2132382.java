package org.hmaciel.descop.otros.Mail;

import java.util.List;
import org.hmaciel.descop.otros.Sala;
import org.richfaces.model.selection.Selection;

public interface IGestionCtasMailAction {

    public void nuevo();

    public void destroy();

    public String eliminar();

    public String modificarCta();

    public void cancelarModCta();

    public String altaCta();

    public void setear1();

    public void setear2();

    public void setear3();

    public String getMail();

    public void setMail(String mail);

    public String getTitular();

    public void setTitular(String titular);

    public List<String> getGruposDisponibles();

    public void setGruposDisponibles(List<String> gruposDisponibles);

    public List<String> getGruposAsociados();

    public void setGruposAsociados(List<String> gruposAsociados);

    public List<CtaMail> getListMails();

    public void setListMails(List<CtaMail> listMails);

    public Selection getSelectionMail();

    public void setSelectionMail(Selection selectionMail);

    public Object getTableStateMail();

    public void setTableStateMail(Object tableStateMail);

    public String getParam_Mail();

    public void setParam_Mail(String param_Mail);

    public boolean isNuevaBool();

    public void setNuevaBool(boolean nuevaBool);

    public boolean isModificarBool();

    public void setModificarBool(boolean modificarBool);

    public CtaMail getCm();

    public void setCm(CtaMail cm);
}
