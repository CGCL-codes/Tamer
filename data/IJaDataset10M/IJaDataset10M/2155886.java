package v201203.companyservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201203.Company;
import com.google.api.ads.dfp.v201203.CompanyServiceInterface;

/**
 * This example gets a company by its ID. To determine which companies
 * exist, run GetAllCompaniesExample.java.
 *
 * Tags: CompanyService.getCompany
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetCompanyExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            CompanyServiceInterface companyService = user.getService(DfpService.V201203.COMPANY_SERVICE);
            Long companyId = Long.parseLong("INSERT_COMPANY_ID_HERE");
            Company company = companyService.getCompany(companyId);
            if (company != null) {
                System.out.println("Company with ID \"" + company.getId() + "\", name \"" + company.getName() + "\", and type \"" + company.getType() + "\" was found.");
            } else {
                System.out.println("No company found for this ID.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
