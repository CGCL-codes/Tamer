package org.pcorp.space.presentation.bean.equipement;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.pcorp.space.metier.domaine.Equipement;
import org.pcorp.space.metier.service.ServiceEquipement;

public class InfoAjaxEquipementBean {

    private String info;

    private Equipement equip;

    private int id;

    public InfoAjaxEquipementBean() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String sessionID = session.getId();
        String id_string = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(sessionID);
        if (id_string != null && id_string != "") id = Integer.parseInt(id_string);
        ServiceEquipement service = new ServiceEquipement();
        equip = service.getEquipementComplet(id);
        info = "ceci est une description pourrite " + id_string + " " + sessionID + "";
    }

    public Equipement getEquip() {
        return equip;
    }

    public void setEquip(Equipement equip) {
        this.equip = equip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
