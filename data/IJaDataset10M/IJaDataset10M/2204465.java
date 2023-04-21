package org.libreplan.web.externalcompanies;

import org.libreplan.business.externalcompanies.entities.ExternalCompany;

/**
 * DTO for ExternalCompany
 * @author Susana Montes Pedreira <smontes@wirelessgalicia.com>
 */
public class ExternalCompanyDTO {

    private ExternalCompany company;

    public ExternalCompany getCompany() {
        return company;
    }

    public ExternalCompanyDTO(ExternalCompany company) {
        this.company = company;
    }

    public String getName() {
        return company.getName();
    }

    public String getNif() {
        return company.getNif();
    }

    public Boolean getClient() {
        return company.isClient();
    }

    public Boolean getSubcontractor() {
        return company.isSubcontractor();
    }

    public String getCompanyUser() {
        return (company.getCompanyUser() != null) ? company.getCompanyUser().getLoginName() : "---";
    }
}
