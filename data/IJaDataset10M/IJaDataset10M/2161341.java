package com.lagerplan.basisdienste.wissensbasis.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import com.lagerplan.basisdienste.constant.LagerplanActionConst;
import com.lagerplan.basisdienste.wissensbasis.bci.WissensbasisVerwalterImpl;
import com.lagerplan.basisdienste.wissensbasis.bci.WissensbasisVerwalterInt;

/**
 * <p>
 * Title: WissensbasisAction.java<br>
 * Description: Controller Klasse f�r Aktionen der Wissensbasis.<br>
 * Copyright: Copyright (c) 2009<br>
 * Company: LAGERPLAN Organisation
 * </p>
 * 
 * @author %author: Michael Felber%
 * @version %version: 1%
 */
public class WissensbasisAction implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 287431014682876668L;

    /**
	 * Ansicht Vertr�glichkeit TM Ein-/Auslagern -- TM Vorzone (Zuordnung TM-TM)
	 * @return
	 */
    public String initialisierenZuordTransportmittelEinAuslagernVorzone() {
        WissensbasisVerwalterInt wissenBusiness = new WissensbasisVerwalterImpl();
        ZuordnungTransportmittelEinAuslagernVorzoneForm wissenForm = new ZuordnungTransportmittelEinAuslagernVorzoneForm();
        wissenForm.setZuordnungTransportmittelEinAuslagernVorzone(wissenBusiness.getZuordnungenTransportmittelEinAuslagernVorzoneMatrix());
        wissenForm.setTransportmittelEinAuslagern(wissenBusiness.getTransportmittelKlassenGeeignetZumEinAuslagern());
        wissenForm.setTransportmittelVorzone(wissenBusiness.getTransportmittelKlassenGeeignetFuerVorzone());
        String[] columns = new String[20];
        boolean testMatrix[][] = new boolean[20][20];
        for (int i = 0; i < 20; i++) {
            testMatrix[0][i] = true;
            testMatrix[i][0] = true;
            columns[i] = "column" + i;
        }
        wissenForm.setTestMatrix(testMatrix);
        wissenForm.setColumns(columns);
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) ctx.getSession(false);
        session.setAttribute(LagerplanActionConst.WISSENSBASIS_ZUORD_TRANSPORTMITTEL_EINAUSLAGERN_VORZONE_FORM, wissenForm);
        return LagerplanActionConst.WISSENSBASIS_ZUORD_TRANSPORTMITTEL_EINAUSLAGERN_VORZONE_ANZEIGEN;
    }

    public String bearbeiten() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) ctx.getSession(false);
        ZuordnungTransportmittelEinAuslagernVorzoneForm form = (ZuordnungTransportmittelEinAuslagernVorzoneForm) session.getAttribute(LagerplanActionConst.WISSENSBASIS_ZUORD_TRANSPORTMITTEL_EINAUSLAGERN_VORZONE_FORM);
        return LagerplanActionConst.WISSENSBASIS_ZUORD_TRANSPORTMITTEL_EINAUSLAGERN_VORZONE_ANZEIGEN;
    }
}
