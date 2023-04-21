package uk.co.graydon.GraydonDataService.examples;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import uk.co.graydon.GraydonDataService.*;
import uk.co.graydon.ws.GraydonDataService.types.*;

/**
 * @author denisreilly
 *
 * Runnable axis 1.3 java client for Graydon Company Data Webservice
 * 
 * Operation : GetDemoCompanies
 * 
 * Argument0 = Partner Userid
 * Argument1 = Partner Password
 * Argument2 = Country
 */
public class DemoCompanies {

    private static SimpleDateFormat SIMPLE_DATE = new SimpleDateFormat("dd/MM/yyyy");

    private static TimeZone GMT = TimeZone.getTimeZone("GMT");

    public static void main(String[] args) {
        try {
            String partnerUserid = null;
            String partnerPassword = null;
            String country = null;
            SIMPLE_DATE.setTimeZone(GMT);
            if (args.length == 0) throw new Exception("Invalid arguments: Please specify match arguments");
            partnerUserid = args[0].trim();
            partnerPassword = args[1].trim();
            if (args.length > 2) country = args[2].trim();
            if (partnerUserid == null || partnerPassword == null) throw new Exception("Invalid arguments: Please specify match arguments");
            GraydonCompanyData_BindingStub binding = (GraydonCompanyData_BindingStub) new GraydonCompanyDataLocator().getSOAPPort();
            binding.setTimeout(120000);
            Authentication_ParametersType authentication_parameters = new Authentication_ParametersType();
            authentication_parameters.setPartnerUserId(partnerUserid);
            authentication_parameters.setPartnerPassword(partnerPassword);
            GetDemoCompanies_ParametersType getDemoCompanies_Parameters = new GetDemoCompanies_ParametersType();
            getDemoCompanies_Parameters.setCountry(country);
            getDemoCompanies_Parameters.setAuthentication_Parameters(authentication_parameters);
            try {
                GetDemoCompanies_ResultType getDemoCompanies_Result = binding.getDemoCompanies(getDemoCompanies_Parameters);
                if (getDemoCompanies_Result.getService_Log() != null) serviceLog(getDemoCompanies_Result.getService_Log());
                if (getDemoCompanies_Result.getDemoCompanies() != null) {
                    CountryAndCompanyType countryAndCompany[] = getDemoCompanies_Result.getDemoCompanies();
                    for (int x = 0; x < countryAndCompany.length; x++) {
                        System.out.println("Demo Companies:");
                        System.out.println("Country\t\t\t: " + countryAndCompany[x].getCountry());
                        CompanyType company[] = countryAndCompany[x].getCompanies();
                        for (int y = 0; y < company.length; y++) {
                            System.out.println("  Company: ");
                            System.out.println("  Name\t\t\t: " + company[y].getName());
                            if (company[y].getRegisteredAddress() != null) System.out.println("  Registered Address\t: " + company[y].getRegisteredAddress().getEntireAddress());
                            if (company[y].getTradingAddress() != null) System.out.println("  Trading Address\t: " + company[y].getTradingAddress().getEntireAddress());
                            System.out.println("  Legal Form\t\t: " + company[y].getLegalForm());
                            System.out.println("  Email\t\t\t: " + company[y].getEmail());
                            System.out.println("  Telephone\t\t: " + company[y].getTelephone());
                            System.out.println("  Fax\t\t\t: " + company[y].getFacsimile());
                            System.out.println("  Company Match Id\t: " + company[y].getCompanyMatchIdentifier());
                            if (company[y].getCompanyIdentifiers() != null) {
                                CompanyIdentifierType companyIdentifier[] = company[y].getCompanyIdentifiers();
                                for (int z = 0; z < companyIdentifier.length; z++) {
                                    System.out.println("  Identifier: ");
                                    System.out.println("    Type\t\t: " + companyIdentifier[z].getType());
                                    System.out.println("    Identifier\t\t: " + companyIdentifier[z].getIdentifier());
                                }
                            }
                            if (company[y].getActivites() != null) {
                                ActivityType activity[] = company[y].getActivites();
                                for (int z = 0; z < activity.length; z++) {
                                    System.out.println("  Activity: ");
                                    System.out.println("    Type\t\t: " + activity[z].getType());
                                    System.out.println("    Code\t\t: " + activity[z].getCode());
                                    System.out.println("    Description\t\t: " + activity[z].getDescription());
                                }
                            }
                        }
                        System.out.println(" ");
                    }
                }
            } catch (GraydonCompanyData_FaultType fault) {
                if (fault.getFaultType() == FaultTypeType.Expected) {
                    if (fault.getFaultReturnCode().equals("CWS5000")) System.err.println("My Message: " + "No match methods for country selected");
                }
                System.out.println("Graydon Message: " + fault.getFaultMessage());
            }
        } catch (Exception e) {
            System.err.println("Exception : " + e);
        }
    }

    /**
	* Service log output
	*
	*/
    private static void serviceLog(Service_LogType service_Log) {
        System.out.println("Service_Log :");
        System.out.println("  PartnerUserId\t\t: " + service_Log.getPartnerUserId());
        System.out.println("  SessionId\t\t: " + service_Log.getSessionID());
        System.out.println("  Transaction Identifier: " + service_Log.getTransactionIdentifier());
        System.out.println("  Request Date\t\t: " + SIMPLE_DATE.format(service_Log.getRequestTimestamp().getDate()));
        System.out.println("  Request Time\t\t: " + service_Log.getRequestTimestamp().getTime());
        System.out.println("  Response Date\t\t: " + SIMPLE_DATE.format(service_Log.getResponseTimestamp().getDate()));
        System.out.println("  Response Time\t\t: " + service_Log.getResponseTimestamp().getTime());
        System.out.println(" ");
    }
}
