package com.google.appsforyourdomain.provisioning;

import org.jdom.Document;
import java.util.List;

/**
 * The MailingList class represents a mailing list of a hosted domain.
 * This class contains static methods to generate all XML requests
 * pertaining to a MailingList.
 * 
 * Sample usage of this class can be found in ProvisioningClient.java
 */
public class MailingList {

    /**
   * ListOperation represents the two operations that can be performed on a
   * mailing list.
   */
    public enum ListOperation {

        add, remove
    }

    protected String mailingListName;

    protected List<String> emailAddresses;

    /**
   * Constructs a MailingList object with values
   * 
   * @param mailingListName the name of the mailing list
   * @param emailAddresses the email addresses in the mailing list
   */
    public MailingList(String mailingListName, List<String> emailAddresses) {
        this.mailingListName = mailingListName;
        this.emailAddresses = emailAddresses;
    }

    public String getMailingListName() {
        return mailingListName;
    }

    public void setMailingListName(String mailingListName) {
        this.mailingListName = mailingListName;
    }

    public List<String> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    /**
   * Generates create mailing list request
   * 
   * @param mailingListName the name of the mailing list
   * @param domainName domain name of the hosted account
   * @param authToken authentication token
   * @return the create mailing list xml
   */
    protected static String generateCreateMailingListRequest(String mailingListName, String domainName, String authToken) {
        StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buffer.append("<hs:rest xmlns:hs=\"google:accounts:rest:protocol\" " + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
        buffer.append("<hs:type>MailingList</hs:type>");
        buffer.append("<hs:token>" + authToken + "</hs:token>");
        buffer.append("<hs:domain>" + domainName + "</hs:domain>");
        buffer.append("<hs:CreateSection>");
        buffer.append("<hs:mailingListName>" + mailingListName + "</hs:mailingListName>");
        buffer.append("</hs:CreateSection>");
        buffer.append("</hs:rest>");
        return buffer.toString();
    }

    /**
   * Generates the update mailing list request
   * 
   * @param mailingListName name of the mailing list
   * @param userName username to be added or removed
   * @param operation add or remove
   * @param domainName domain name of hosted account
   * @param authToken authentication token
   * @return the update mailing list xml 
   */
    protected static String generateUpdateMailingListRequest(String mailingListName, String userName, ListOperation operation, String domainName, String authToken) {
        StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buffer.append("<hs:rest xmlns:hs=\"google:accounts:rest:protocol\" " + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
        buffer.append("<hs:type>MailingList</hs:type>");
        buffer.append("<hs:token>" + authToken + "</hs:token>");
        buffer.append("<hs:domain>" + domainName + "</hs:domain>");
        buffer.append("<hs:queryKey>mailingListName</hs:queryKey>");
        buffer.append("<hs:queryData>" + mailingListName + "</hs:queryData>");
        buffer.append("<hs:UpdateSection>");
        buffer.append("<hs:userName>" + userName + "</hs:userName>");
        buffer.append("<hs:listOperation>" + operation + "</hs:listOperation>");
        buffer.append("</hs:UpdateSection>");
        buffer.append("</hs:rest>");
        return buffer.toString();
    }

    /**
   * Generates a general mailing list request. This request can be used to
   * retrieve a mailing list or delete a mailing list.
   * 
   * @param mailingListName name of the mailing list
   * @param domainName domain name of the hosted account
   * @param authToken authentication token
   * @return a general mailing request xml
   */
    protected static String generateMailingListRequest(String mailingListName, String domainName, String authToken) {
        StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buffer.append("<hs:rest xmlns:hs=\"google:accounts:rest:protocol\" " + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
        buffer.append("<hs:type>MailingList</hs:type>");
        buffer.append("<hs:token>" + authToken + "</hs:token>");
        buffer.append("<hs:domain>" + domainName + "</hs:domain>");
        buffer.append("<hs:queryKey>mailingListName</hs:queryKey>");
        buffer.append("<hs:queryData>" + mailingListName + "</hs:queryData>");
        buffer.append("</hs:rest>");
        return buffer.toString();
    }
}
