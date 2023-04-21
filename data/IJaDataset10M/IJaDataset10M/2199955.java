package it.uniromadue.portaleuni.manager;

import java.util.ArrayList;
import it.uniromadue.portaleuni.base.BaseForm;
import it.uniromadue.portaleuni.dao.EsamiDAO;
import it.uniromadue.portaleuni.form.DateEsamiForm;

public class DateEsamiManager implements Manager {

    private EsamiDAO esamiDAO;

    public void deleteData(BaseForm form) throws Exception {
    }

    public void loadData(BaseForm form) throws Exception {
        DateEsamiForm dateEsamiForm = (DateEsamiForm) form;
        ArrayList listaDateEsami = (ArrayList) esamiDAO.findAll();
        dateEsamiForm.setListaDateEsami(listaDateEsami);
    }

    public void saveData(BaseForm form) throws Exception {
    }

    public EsamiDAO getEsamiDAO() {
        return esamiDAO;
    }

    public void setEsamiDAO(EsamiDAO esamiDAO) {
        this.esamiDAO = esamiDAO;
    }
}
