package org.blueoxygen.papaje.employer;

import java.util.ArrayList;
import java.util.List;
import org.blueoxygen.cimande.role.RoleSite;
import org.blueoxygen.cimande.security.usermanager.UserSite;
import org.blueoxygen.papaje.entity.Country;
import org.blueoxygen.papaje.entity.Employer;
import org.blueoxygen.papaje.entity.Experience;

public class DeleteEmployer extends EmployerForm {

    public String execute() {
        if (!"".equals(getId())) {
            setEmployer((Employer) getManager().getById(Employer.class, getId()));
            setCountries(getManager().getList("FROM " + Country.class.getName(), null, null));
        }
        List<UserSite> uss = getManager().getList("FROM " + UserSite.class.getName() + " p  WHERE p.user.id='" + getEmployer().getUser().getId() + "'", null, null);
        for (UserSite us : uss) {
            getManager().remove(us);
        }
        getManager().remove(getEmployer().getUser());
        getManager().remove(getEmployer());
        setMsg("Delete");
        return SUCCESS;
    }
}
