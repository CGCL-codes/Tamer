package org.nakedobjects.object.testobject;

import org.nakedobjects.application.valueholder.TextString;
import org.nakedobjects.object.InternalCollection;
import org.nakedobjects.object.NakedCollection;

public class ConcreteEmployer extends Employer {

    private InternalCollection employees = createInternalCollection(Employee.class);

    private NakedCollection team;

    private TextString name = new TextString();

    private Employee ceo;

    public Employee getCEO() {
        return ceo;
    }

    public TextString getCompanyName() {
        return name;
    }

    public InternalCollection getEmployees() {
        return employees;
    }

    public NakedCollection getIntraCompanyTeam() {
        return team;
    }

    public void setCEO(Employee newCeo) {
        ceo = newCeo;
        objectChanged();
    }

    public void setIntraCompanyTeam(NakedCollection newIntraCompanyTeam) {
        team = newIntraCompanyTeam;
        objectChanged();
    }
}
