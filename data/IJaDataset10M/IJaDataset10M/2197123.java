package com.tucows.oxrs.epp0402.rtk.example;

import java.util.*;
import com.tucows.oxrs.epp0402.rtk.*;
import com.tucows.oxrs.epp0402.rtk.xml.*;
import org.openrtk.idl.epp0402.*;
import org.openrtk.idl.epp0402.domain.*;
import org.openrtk.idl.epp0402.host.*;
import org.openrtk.idl.epp0402.contact.*;

/**
 * Example code for the EPP Host object.  Also shows creation of
 * EPPClient instance, login, logout and disconnect.
 *
 * @author Daniel Manley
 * @version $Revision: 1.1 $ $Date: 2003/03/21 16:35:35 $
 * @see com.tucows.oxrs.epp0402.rtk.EPPClient
 * @see com.tucows.oxrs.epp0402.rtk.xml.EPPGreeting
 * @see com.tucows.oxrs.epp0402.rtk.xml.EPPHostCheck
 * @see com.tucows.oxrs.epp0402.rtk.xml.EPPHostInfo
 * @see com.tucows.oxrs.epp0402.rtk.xml.EPPHostCreate
 * @see com.tucows.oxrs.epp0402.rtk.xml.EPPHostUpdate
 * @see com.tucows.oxrs.epp0402.rtk.xml.EPPHostDelete
**/
public class HostExample {

    private static String USAGE = "Usage: com.tucows.oxrs.epp0402.rtk.example.HostExample epp_host_name epp_host_port epp_client_id epp_password epp_domain_name";

    /**
     * Main of the example.  Performs Host check, info, create, update and delete.
     */
    public static void main(String args[]) {
        System.out.println("Start of the Host example");
        int current_expiration_year = 2002;
        epp_Command command_data = null;
        try {
            if (args.length < 5) {
                System.err.println(USAGE);
                System.exit(1);
            }
            RTKBase.setDebugLevel();
            String epp_host_name = args[0];
            String epp_host_port_string = args[1];
            String epp_client_id = args[2];
            String epp_password = args[3];
            String epp_domain_name = args[4];
            int epp_host_port = Integer.parseInt(epp_host_port_string);
            EPPClient epp_client = new EPPClient(epp_host_name, epp_host_port, epp_client_id, epp_password);
            epp_client.setLang("en");
            System.out.println("Connecting to the EPP Server and getting the greeting");
            epp_Greeting greeting = epp_client.connectAndGetGreeting();
            System.out.println("greeting's server: [" + greeting.m_server_id + "]");
            System.out.println("greeting's server-date: [" + greeting.m_server_date + "]");
            if (greeting.m_versions != null && greeting.m_versions.length > 0) {
                System.out.println("greeting's version: [" + greeting.m_versions[0] + "]");
            }
            if (greeting.m_langs != null && greeting.m_langs.length > 0) {
                System.out.println("greeting's lang: [" + greeting.m_langs[0] + "]");
            }
            if (greeting.m_services != null && greeting.m_services.length > 0) {
                System.out.println("services provided by epp: ");
                for (int i = 0; i < greeting.m_services.length; i++) {
                    System.out.println((epp_Service) greeting.m_services[i]);
                }
            }
            if (greeting.m_unspec_services != null && greeting.m_unspec_services.length > 0) {
                System.out.println("\nunspecified services provided by epp: ");
                for (int i = 0; i < greeting.m_unspec_services.length; i++) {
                    System.out.println((epp_Service) greeting.m_unspec_services[i]);
                }
            }
            System.out.println();
            String client_trid = getClientTrid(epp_client_id);
            System.out.println("Logging into the EPP Server");
            epp_client.login(client_trid);
            try {
                System.out.println("Creating the Host Check command");
                epp_HostCheckReq host_check_request = new epp_HostCheckReq();
                command_data = new epp_Command();
                command_data.m_client_trid = getClientTrid(epp_client_id);
                host_check_request.m_cmd = command_data;
                List host_list = (List) new ArrayList();
                host_list.add("ns1." + epp_client_id + ".info");
                host_list.add("ns2." + epp_client_id + ".info");
                host_list.add("dns.host.info");
                host_check_request.m_names = EPPXMLBase.convertListToStringArray(host_list);
                EPPHostCheck host_check = new EPPHostCheck();
                host_check.setRequestData(host_check_request);
                host_check = (EPPHostCheck) epp_client.processAction(host_check);
                epp_HostCheckRsp host_check_response = host_check.getResponseData();
                epp_Response response = host_check_response.m_rsp;
                epp_Result[] results = response.m_results;
                System.out.println("HostCheck results: [" + results[0].m_code + "] [" + results[0].m_msg + "]");
                epp_CheckResult[] check_results = host_check_response.m_results;
                System.out.println("HostCheck results: host [ns1." + epp_client_id + ".info] exists? [" + EPPXMLBase.getCheckResultFor(check_results, "ns1." + epp_client_id + ".info") + "]");
                System.out.println("HostCheck results: host [ns2." + epp_client_id + ".info] exists? [" + EPPXMLBase.getCheckResultFor(check_results, "ns2." + epp_client_id + ".info") + "]");
                System.out.println("HostCheck results: host [dns.host.info] exists? [" + EPPXMLBase.getCheckResultFor(check_results, "dns.host.info") + "]");
            } catch (epp_XMLException xcp) {
                System.err.println("epp_XMLException! [" + xcp.m_error_message + "]");
            } catch (epp_Exception xcp) {
                System.err.println("epp_Exception!");
                epp_Result[] results = xcp.m_details;
                System.err.println("\tresults: " + results[0] + "]");
            } catch (Exception xcp) {
                System.err.println("EPP Host Check failed! [" + xcp.getClass().getName() + "] [" + xcp.getMessage() + "]");
                xcp.printStackTrace();
            }
            System.out.println("Creating the Contact Create command");
            epp_ContactCreateReq contact_create_request = new epp_ContactCreateReq();
            try {
                command_data = new epp_Command();
                command_data.m_client_trid = getClientTrid(epp_client_id);
                contact_create_request.m_cmd = command_data;
                contact_create_request.m_id = epp_client_id + (System.currentTimeMillis() / 1000);
                epp_ContactNameAddress name_address = new epp_ContactNameAddress();
                name_address.m_name = "John Doe";
                name_address.m_address = new epp_ContactAddress();
                name_address.m_address.m_street1 = "100 Centre St";
                name_address.m_address.m_city = "Townsville";
                name_address.m_address.m_state_province = "County Derry";
                name_address.m_address.m_postal_code = "Z1Z1Z1";
                name_address.m_address.m_country_code = "CA";
                contact_create_request.m_ascii_address = name_address;
                contact_create_request.m_email = "jdoe@company.info";
                epp_AuthInfo contact_auth_info = new epp_AuthInfo();
                contact_auth_info.m_value = "changeme";
                contact_auth_info.m_type = epp_AuthInfoType.PW;
                contact_create_request.m_auth_info = contact_auth_info;
                EPPContactCreate contact_create = new EPPContactCreate();
                contact_create.setRequestData(contact_create_request);
                contact_create = (EPPContactCreate) epp_client.processAction(contact_create);
                epp_ContactCreateRsp contact_create_response = contact_create.getResponseData();
                epp_Response response = contact_create_response.m_rsp;
                epp_Result[] results = response.m_results;
                System.out.println("ContactCreate results: [" + results[0].m_code + "] [" + results[0].m_msg + "]");
                System.out.println("ContactCreate results: contact id [" + contact_create_response.m_id + "]");
            } catch (epp_XMLException xcp) {
                System.err.println("epp_XMLException! [" + xcp.m_error_message + "]");
            } catch (epp_Exception xcp) {
                System.err.println("epp_Exception!");
                epp_Result[] results = xcp.m_details;
                System.err.println("\tresult: [" + results[0] + "]");
            } catch (Exception xcp) {
                System.err.println("Contact Create failed! [" + xcp.getClass().getName() + "] [" + xcp.getMessage() + "]");
                xcp.printStackTrace();
            }
            try {
                System.out.println("Creating the Domain Create command");
                epp_DomainCreateReq domain_create_request = new epp_DomainCreateReq();
                command_data = new epp_Command();
                command_data.m_client_trid = getClientTrid(epp_client_id);
                domain_create_request.m_cmd = command_data;
                domain_create_request.m_name = epp_domain_name;
                epp_AuthInfo domain_auth_info = new epp_AuthInfo();
                domain_auth_info.m_value = "exam123";
                domain_create_request.m_auth_info = domain_auth_info;
                Vector contacts = new Vector();
                contacts.add(new epp_DomainContact(epp_DomainContactType.TECH, "ABC-contact"));
                contacts.add(new epp_DomainContact(epp_DomainContactType.ADMIN, "ABC-contact"));
                contacts.add(new epp_DomainContact(epp_DomainContactType.BILLING, "ABC-contact"));
                domain_create_request.m_contacts = (epp_DomainContact[]) contacts.toArray(new epp_DomainContact[1]);
                domain_create_request.m_registrant = contact_create_request.m_id;
                EPPDomainCreate domain_create = new EPPDomainCreate();
                domain_create.setRequestData(domain_create_request);
                domain_create = (EPPDomainCreate) epp_client.processAction(domain_create);
                epp_DomainCreateRsp domain_create_response = domain_create.getResponseData();
                epp_Response response = domain_create_response.m_rsp;
            } catch (epp_Exception xcp) {
                epp_Result[] results = xcp.m_details;
                if (results[0].m_code != epp_Session.EPP_OBJECT_EXISTS) {
                    System.out.println("Domain already exists!  Let's continue...");
                } else {
                    System.err.println("epp_Exception!");
                    System.err.println("\tresult: [" + results[0] + "]");
                }
            } catch (Exception xcp) {
                System.err.println("Failed to create domain required for host example [" + xcp.getMessage() + "]");
                System.exit(1);
            }
            try {
                System.out.println("Creating the Host Create command");
                epp_HostCreateReq host_create_request = new epp_HostCreateReq();
                command_data = new epp_Command();
                command_data.m_client_trid = getClientTrid(epp_client_id);
                host_create_request.m_cmd = command_data;
                List ip_list = (List) new ArrayList();
                ip_list.add(new epp_HostAddress(epp_HostAddressType.IPV4, "130.111.111.200"));
                host_create_request.m_addresses = (epp_HostAddress[]) EPPXMLBase.convertListToArray((new epp_HostAddress()).getClass(), ip_list);
                host_create_request.m_name = "ns1." + epp_domain_name;
                EPPHostCreate host_create = new EPPHostCreate();
                host_create.setRequestData(host_create_request);
                host_create = (EPPHostCreate) epp_client.processAction(host_create);
                epp_HostCreateRsp host_create_response = host_create.getResponseData();
                epp_Response response = host_create_response.m_rsp;
                epp_Result[] results = response.m_results;
                System.out.println("HostCreate results: [" + results[0].m_code + "] [" + results[0].m_msg + "]");
                System.out.println("HostCreate results: host name [" + host_create_response.m_name + "]");
            } catch (epp_XMLException xcp) {
                System.err.println("epp_XMLException! [" + xcp.m_error_message + "]");
            } catch (epp_Exception xcp) {
                System.err.println("epp_Exception!");
                epp_Result[] results = xcp.m_details;
                System.err.println("\tresult: [" + results[0] + "]");
            } catch (Exception xcp) {
                System.err.println("EPP Host Create failed! [" + xcp.getClass().getName() + "] [" + xcp.getMessage() + "]");
                xcp.printStackTrace();
            }
            try {
                System.out.println("Creating the Host Info command");
                epp_HostInfoReq host_info_request = new epp_HostInfoReq();
                command_data = new epp_Command();
                command_data.m_client_trid = getClientTrid(epp_client_id);
                host_info_request.m_cmd = command_data;
                host_info_request.m_name = "ns1." + epp_domain_name;
                EPPHostInfo host_info = new EPPHostInfo();
                host_info.setRequestData(host_info_request);
                host_info = (EPPHostInfo) epp_client.processAction(host_info);
                epp_HostInfoRsp host_info_response = host_info.getResponseData();
                epp_Response response = host_info_response.m_rsp;
                epp_Result[] results = response.m_results;
                System.out.println("HostInfo results: [" + results[0].m_code + "] [" + results[0].m_msg + "]");
                System.out.println("HostInfo results: clID [" + host_info_response.m_client_id + "] crID [" + host_info_response.m_created_by + "]");
                System.out.println("HostInfo results: crDate [" + host_info_response.m_created_date + "] upDate [" + host_info_response.m_updated_date + "]");
                System.out.println("HostInfo results: number of ipaddresses [" + (host_info_response.m_addresses == null ? 0 : host_info_response.m_addresses.length) + "]");
            } catch (epp_XMLException xcp) {
                System.err.println("epp_XMLException! [" + xcp.m_error_message + "]");
            } catch (epp_Exception xcp) {
                System.err.println("epp_Exception!");
                epp_Result[] results = xcp.m_details;
                System.err.println("\tresult: [" + results[0] + "]");
            } catch (Exception xcp) {
                System.err.println("EPP Host Info failed! [" + xcp.getClass().getName() + "] [" + xcp.getMessage() + "]");
                xcp.printStackTrace();
            }
            try {
                System.out.println("Creating the Host Update command");
                epp_HostUpdateReq host_update_request = new epp_HostUpdateReq();
                command_data = new epp_Command();
                command_data.m_client_trid = getClientTrid(epp_client_id);
                host_update_request.m_cmd = command_data;
                host_update_request.m_name = "ns1." + epp_domain_name;
                epp_HostUpdateAddRemove add = new epp_HostUpdateAddRemove();
                List ip_list = (List) new ArrayList();
                ip_list.add(new epp_HostAddress(epp_HostAddressType.IPV4, "130.111.111.201"));
                add.m_addresses = (epp_HostAddress[]) EPPXMLBase.convertListToArray((new epp_HostAddress()).getClass(), ip_list);
                add.m_status = new epp_HostStatus[1];
                add.m_status[0] = new epp_HostStatus();
                add.m_status[0].m_type = epp_HostStatusType.CLIENT_UPDATE_PROHIBITED;
                add.m_status[0].m_lang = "en";
                add.m_status[0].m_value = "Want to prevent accidental change of IP";
                host_update_request.m_add = add;
                epp_HostUpdateAddRemove remove = new epp_HostUpdateAddRemove();
                ip_list = (List) new ArrayList();
                ip_list.add(new epp_HostAddress(epp_HostAddressType.IPV4, "130.111.111.200"));
                remove.m_addresses = (epp_HostAddress[]) EPPXMLBase.convertListToArray((new epp_HostAddress()).getClass(), ip_list);
                host_update_request.m_remove = remove;
                host_update_request.m_change = new epp_HostUpdateChange();
                host_update_request.m_change.m_name = "ns1." + epp_domain_name;
                EPPHostUpdate host_update = new EPPHostUpdate();
                host_update.setRequestData(host_update_request);
                host_update = (EPPHostUpdate) epp_client.processAction(host_update);
                epp_HostUpdateRsp host_update_response = host_update.getResponseData();
                epp_Response response = host_update_response.m_rsp;
                epp_Result[] results = response.m_results;
                System.out.println("HostUpdate results: [" + results[0].m_code + "] [" + results[0].m_msg + "]");
            } catch (epp_XMLException xcp) {
                System.err.println("epp_XMLException! [" + xcp.m_error_message + "]");
            } catch (epp_Exception xcp) {
                System.err.println("epp_Exception!");
                epp_Result[] results = xcp.m_details;
                System.err.println("\tresult: [" + results[0] + "]");
            } catch (Exception xcp) {
                System.err.println("Host Update failed! [" + xcp.getClass().getName() + "] [" + xcp.getMessage() + "]");
                xcp.printStackTrace();
            }
            try {
                System.out.println("Creating the Host Delete command");
                epp_HostDeleteReq host_delete_request = new epp_HostDeleteReq();
                command_data = new epp_Command();
                command_data.m_client_trid = getClientTrid(epp_client_id);
                host_delete_request.m_cmd = command_data;
                host_delete_request.m_name = "ns1." + epp_domain_name;
                EPPHostDelete host_delete = new EPPHostDelete();
                host_delete.setRequestData(host_delete_request);
                host_delete = (EPPHostDelete) epp_client.processAction(host_delete);
                epp_HostDeleteRsp host_delete_response = host_delete.getResponseData();
                epp_Response response = host_delete_response.m_rsp;
                epp_Result[] results = response.m_results;
                System.out.println("HostDelete results: [" + results[0].m_code + "] [" + results[0].m_msg + "]");
            } catch (epp_XMLException xcp) {
                System.err.println("epp_XMLException! [" + xcp.m_error_message + "]");
            } catch (epp_Exception xcp) {
                System.err.println("epp_Exception!");
                epp_Result[] results = xcp.m_details;
                System.err.println("\tresult: [" + results[0] + "]");
            } catch (Exception xcp) {
                System.err.println("EPP Host Delete failed! [" + xcp.getClass().getName() + "] [" + xcp.getMessage() + "]");
                xcp.printStackTrace();
            }
            System.out.println("Logging out from the EPP Server");
            epp_client.logout(client_trid);
            System.out.println("Disconnecting from the EPP Server");
            epp_client.disconnect();
        } catch (epp_XMLException xcp) {
            System.err.println("epp_XMLException! [" + xcp.m_error_message + "]");
        } catch (epp_Exception xcp) {
            System.err.println("epp_Exception!");
            epp_Result[] results = xcp.m_details;
            System.err.println("\tresult: [" + results[0] + "]");
        } catch (Exception xcp) {
            System.err.println("Exception! [" + xcp.getClass().getName() + "] [" + xcp.getMessage() + "]");
            xcp.printStackTrace();
        }
    }

    protected static String getClientTrid(String epp_client_id) {
        return "ABC:" + epp_client_id + ":" + System.currentTimeMillis();
    }
}
