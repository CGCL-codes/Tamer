package com.google.urchin.dataapi.soap.adminservice;

/**
 * This code sample
 *  - retrieves list of accounts for specified user
 *  - retrieves list of profiles for specified user and [optional] account
 * 
 * To build and run the sample you need to download Apache Axis 1.4.1 from 
 * http://ws.apache.org/axis2/download/1_4_1/download.cgi
 * 
 * To generate the web services stubs you should run WSDL2Java from Axis bin directory: 
 * %AXIS2_HOME%/bin/wsdl2java.sh -uri http://server[:port]/services/v1/adminservice?wsdl -d adb -p com.google.urchin.dataapi.soap.adminservice -s -S .
 */
public class AdminService {

    private static final String login = "URCHIN_LOGIN";

    private static final String password = "URCHIN_PASSWORD";

    public static void main(String[] args) {
        try {
            GetAccountList();
            GetProfileList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void GetAccountList() throws Exception {
        try {
            AdminserviceStub adminStub = new AdminserviceStub();
            AdminserviceStub.GetAccountList accReq = new AdminserviceStub.GetAccountList();
            accReq.setLogin(login);
            accReq.setPassword(password);
            AdminserviceStub.GetAccountListResponse accRsp = adminStub.getAccountList(accReq);
            if (accRsp != null) {
                AdminserviceStub.GetAccountListResponseSequence[] responseSequence = accRsp.getGetAccountListResponseSequence();
                System.out.println("AdminService::GetAccountList");
                for (AdminserviceStub.GetAccountListResponseSequence account : responseSequence) {
                    System.out.print("account id: \"" + account.localAccount.getAccountId() + "\", ");
                    System.out.print("account name: \"" + account.localAccount.getAccountName() + "\", ");
                    System.out.print("contact name: \"" + account.localAccount.getContactName() + "\", ");
                    System.out.print("e-mail: \"" + account.localAccount.getEmailAddress() + "\", ");
                    System.out.println("phone number: \"" + account.localAccount.getPhoneNumber() + "\".");
                }
            }
        } catch (ApiFaultException0 e) {
            System.out.println(e.getFaultMessage().getCode() + ": " + e.getFaultMessage().getMessage());
        }
    }

    public static void GetProfileList() throws Exception {
        try {
            final int accountId = 1;
            AdminserviceStub adminStub = new AdminserviceStub();
            AdminserviceStub.GetProfileList profileReq = new AdminserviceStub.GetProfileList();
            profileReq.setLogin(login);
            profileReq.setPassword(password);
            profileReq.setAccountId(accountId);
            AdminserviceStub.GetProfileListResponse profileRsp = adminStub.getProfileList(profileReq);
            if (profileRsp != null) {
                AdminserviceStub.GetProfileListResponseSequence[] responseSequence = profileRsp.getGetProfileListResponseSequence();
                System.out.println("AdminService::GetProfileList");
                for (AdminserviceStub.GetProfileListResponseSequence profile : responseSequence) {
                    System.out.print("profile id: \"" + profile.localProfile.getProfileId() + "\", ");
                    System.out.print("profile name: \"" + profile.localProfile.getProfileName() + "\"; ");
                    System.out.print("account id: \"" + profile.localProfile.getAccountId() + "\", ");
                    System.out.println("account name: \"" + profile.localProfile.getAccountName() + "\".");
                }
            }
        } catch (ApiFaultException0 e) {
            System.out.println(e.getFaultMessage().getCode() + ": " + e.getFaultMessage().getMessage());
        }
    }
}
