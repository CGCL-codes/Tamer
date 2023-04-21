package com.xtech.common.ui;

import java.util.Enumeration;
import com.xtech.common.entities.*;
import com.xtech.xerp.DataModel;

/**
 * 
 * @author jscruz
 * @since XERP
 */
public class PersonsModel extends EntitiesModel {

    String companyID;

    public PersonsModel(DataModel model) {
        super(model);
        getTitles().addElement("Nombre");
        getTitles().addElement("Telefono");
        getTitles().addElement("EMail");
        getTitles().addElement("Descripcion");
    }

    /**
	 * @return
	 */
    public String getCompanyID() {
        return companyID;
    }

    public Object getModelValueAt(int rowIndex, int columnIndex) {
        Person p = (Person) getEntities().elementAt(rowIndex);
        String columna = getTitles().elementAt(columnIndex).toString();
        if (columna.equals("ID")) return p.getID(); else if (columna.equals("Nombre")) return p.getName(); else if (columna.equals("Empresa")) return getEntity(p.getCompany(), Company.class).getName(); else if (columna.equals("Telefono")) {
            Enumeration e = p.getPhones();
            if (e.hasMoreElements()) {
                Phone ph = (Phone) getDataModel().getEntity(e.nextElement().toString(), Phone.class);
                if (ph == null) return "";
                return ph.getNumber();
            } else return "N/A";
        } else if (columna.equals("Descripcion")) return p.getDescription(); else if (columna.equals("EMail")) return p.getEMail(); else return "N/A";
    }

    public void refreshData() {
        setEntities(getDataModel().getEntities(Person.class));
    }

    /**
	 * @param string
	 */
    public void setCompanyID(String string) {
        companyID = string;
        addFilter(new CompanyFilter(companyID));
        setFiltered(true);
        refreshData();
    }
}
