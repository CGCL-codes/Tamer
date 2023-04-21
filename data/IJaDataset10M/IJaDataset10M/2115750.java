package org.riverock.portlet.manager.holding;

import java.io.Serializable;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 20:53:12
 *         $Id: CompanyBean.java,v 1.4 2006/06/13 17:37:53 serg_main Exp $
 */
public class CompanyBean implements Serializable {

    private static final long serialVersionUID = 2043005504L;

    private String name = null;

    private Long id = null;

    public CompanyBean() {
    }

    public CompanyBean(Company company) {
        this.name = company.getName();
        this.id = company.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = FacesTools.convertParameter(name);
    }
}
