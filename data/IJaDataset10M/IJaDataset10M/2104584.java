package br.ufmg.saotome.arangiSecurity.model.dao;

import br.ufmg.saotome.arangi.commons.BasicException;
import br.ufmg.saotome.arangi.model.dao.HibernateDAO;
import br.ufmg.saotome.arangi.model.dao.QueryByExample;
import br.ufmg.saotome.arangiSecurity.dto.Role;

public class RoleDAO extends HibernateDAO<Role> {

    public RoleDAO() {
        QueryByExample productQbe = new QueryByExample();
        productQbe.setSelectClause("SELECT role FROM Role role");
        productQbe.setOrderByClause("role.name");
        productQbe.addParameter("name", "role", "like", true);
        productQbe.addParameter("definition", "role", "like", true);
        this.addQueryByExample("fullRoleQuery", productQbe);
    }

    @Override
    protected String beforeLoadAll(Role dtoArg, String alias) throws BasicException {
        String filter = "";
        Role role = (Role) dtoArg;
        if (role.getApplication() == null) {
            filter = "1 = 2";
        } else {
            filter = "role.application.id = " + role.getApplication().getId();
        }
        return filter;
    }
}
