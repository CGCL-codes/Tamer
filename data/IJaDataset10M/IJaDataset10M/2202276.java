package edu.upmc.opi.caBIG.caTIES.jess;

import gov.nih.nci.cadsr.domain.DataElement;
import gov.nih.nci.cadsr.domain.EnumeratedValueDomain;
import gov.nih.nci.cadsr.domain.NonenumeratedValueDomain;
import gov.nih.nci.cadsr.domain.PermissibleValue;
import gov.nih.nci.cadsr.domain.ValueDomainPermissibleValue;
import gov.nih.nci.cadsr.domain.impl.DataElementImpl;
import gov.nih.nci.cadsr.domain.impl.ValueDomainImpl;
import gov.nih.nci.common.util.PrintUtils;
import gov.nih.nci.evs.domain.DescLogicConcept;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.evs.query.EVSQueryImpl;
import gov.nih.nci.system.applicationservice.ApplicationService;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The Class CDEDownLoader reads the OWL version of the NCIT into a Jess Facts
 * suitable for caTIES inference.
 */
public class CDEDownLoader {

    /**
     * The proxyhostip.
     */
    private static String proxyhostip = "10.66.0.1";

    /**
     * The vocabulary name.
     */
    private static String vocabularyName = "NCI_Thesaurus";

    /**
     * The proxyport.
     */
    private static String proxyport = "80";

    /**
     * The dbserver.
     */
    private static String dbserver = "http://cabio.nci.nih.gov/cacore30/server/HTTPServer";

    /**
     * The public id1.
     */
    private static String publicId1 = "106";

    /**
     * The public id.
     */
    private static String publicId = "2431690";

    /**
     * The app service.
     */
    private static ApplicationService appService;

    /**
     * Load CDE.
     * 
     * @param vocabularyName the vocabulary name
     * @param cdePublicId the cde public id
     * 
     * @throws Exception the exception
     */
    public static void loadCDE(String cdePublicId, String vocabularyName) throws Exception {
        appService = ApplicationService.getRemoteInstance(dbserver);
        retrieveDataElement(new Long(cdePublicId));
    }

    /**
     * Retrieve data element.
     * 
     * @param CDEPublicID the CDE public ID
     * 
     * @throws Exception the exception
     */
    private static void retrieveDataElement(Long CDEPublicID) throws Exception {
        DataElement dataElement = new DataElementImpl();
        dataElement.setPublicID(CDEPublicID);
        dataElement.setLatestVersionIndicator("Yes");
        System.out.println("DE Created");
        List resultList = appService.search("gov.nih.nci.cadsr.domain.DataElement", dataElement);
        if (resultList == null) System.out.println("null returned");
        if (resultList != null && !resultList.isEmpty()) {
            dataElement = (DataElement) resultList.get(0);
            List lstValueDomain = retrieveValueDomain(dataElement);
            List respvvdpv = retrievePermissibleValueForValueDomainPermissibleValue(lstValueDomain);
            dataElement.getPublicID().toString();
            dataElement.getPreferredDefinition();
            dataElement.getLongName();
            dataElement.getVersion().toString();
            dataElement.getPreferredName();
        }
    }

    /**
     * Retrieve value domain.
     * 
     * @param dataElement the data element
     * 
     * @return the list
     * 
     * @throws Exception the exception
     */
    private static List retrieveValueDomain(DataElement dataElement) throws Exception {
        List resValueDomain = null;
        resValueDomain = appService.search("gov.nih.nci.cadsr.domain.ValueDomain", dataElement);
        return resValueDomain;
    }

    /**
     * Retrieve permissible value for value domain permissible value.
     * 
     * @param lstValueDomain the lst value domain
     * 
     * @return the list
     * 
     * @throws Exception the exception
     */
    private static List retrievePermissibleValueForValueDomainPermissibleValue(List lstValueDomain) throws Exception {
        List pvList = new ArrayList();
        for (int i = 0; i < lstValueDomain.size(); i++) {
            ValueDomainImpl vdi = (ValueDomainImpl) lstValueDomain.get(i);
            String valdomnm = vdi.toString();
            int z = valdomnm.lastIndexOf('.') + 1;
            String valDomType = valdomnm.substring(z, z + 1);
            if (valDomType.equals("E")) {
                EnumeratedValueDomain evd = (EnumeratedValueDomain) lstValueDomain.get(i);
                Collection pvColl = evd.getValueDomainPermissibleValueCollection();
                int index = 1;
                Iterator it = pvColl.iterator();
                List permissibleValueList = null;
                while (it.hasNext()) {
                    ValueDomainPermissibleValue valDomPerVal = (ValueDomainPermissibleValue) it.next();
                    permissibleValueList = appService.search("gov.nih.nci.cadsr.domain.PermissibleValue", valDomPerVal);
                    for (int cnt = 0; cnt < permissibleValueList.size(); cnt++) {
                        PermissibleValue pv = (PermissibleValue) permissibleValueList.get(cnt);
                        System.out.println(cnt + " : " + pv.getValue() + " : " + evsDescLogicConcepts(pv.getValue(), 10));
                        pvList.add(pv);
                    }
                }
            } else {
                NonenumeratedValueDomain nonEnumValDom = (NonenumeratedValueDomain) lstValueDomain.get(i);
                System.out.println(nonEnumValDom.getPreferredName());
                System.out.println(nonEnumValDom.getPreferredDefinition());
                System.out.println(nonEnumValDom.getLongName());
                pvList.add(nonEnumValDom);
            }
        }
        return pvList;
    }

    /**
     * proxy method is a method which accepts the local username,password, proxy
     * host and proxy port to establish the connection. It generates an
     * exception if not able to establish the connection.
     * 
     * @param password the password
     * @param username the username
     * @param proxyport the proxyport
     * @param proxyhost the proxyhost
     * 
     * @throws Exception the exception
     */
    private static void proxy(String proxyhost, String proxyport, String username, String password) throws Exception {
        final String localusername = username;
        final String localpassword = password;
        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(localusername, localpassword.toCharArray());
            }
        };
        Authenticator.setDefault(authenticator);
        boolean validnum = checknum(proxyport, 0, 0, 0);
        if (validnum == false) {
            System.out.println("Invalid Proxy Port: " + proxyport);
            throw new Exception("Invalid ProxyPort");
        }
        boolean validip = isvalidIP(proxyhost);
        if (validip == false) {
            System.out.println("Invalid Proxy Host: " + proxyhost);
            throw new Exception("Invalid ProxyHost");
        }
        System.setProperty("proxyHost", proxyhost);
        System.setProperty("proxyPort", proxyport);
    }

    /**
     * Checknum.
     * 
     * @param num The String value that is to be used as a number
     * @param min Indicates the minimum value for the number.
     * @param chk To check for the range or in general. 1 indicates check for
     * the number to be in range of min and max. Any other number
     * will beconsidered as general number validation.
     * @param max Indicates the maximum value for the number.
     * 
     * @return It returns a Boolean value depending on the value passed as a num
     * parameter.
     */
    private static boolean checknum(String num, int chk, int min, int max) {
        try {
            Integer i = new Integer(num);
            if (chk == 1) {
                int z = i.intValue();
                if (!(z >= min) && !(z <= max)) {
                    throw new Exception("Number out of Range");
                }
            }
        } catch (NumberFormatException nfe) {
            return false;
        } catch (Exception e1) {
            return false;
        }
        return true;
    }

    /**
     * Isvalid IP.
     * 
     * @param ipadd It contains the IPAddress in String format.This value will be
     * verified for IPAddress. IPAddress will be in the format:
     * A.B.C.D where A,B,C,D all are integers in the range 0 to 255.
     * 
     * @return Returns boolean value depending on the ipaddress provided.
     */
    private static boolean isvalidIP(String ipadd) {
        StringTokenizer st = new StringTokenizer(ipadd, ".");
        int c = st.countTokens();
        if (c == 4) {
            String a[] = new String[4];
            int i = 0;
            while (st.hasMoreTokens()) {
                a[i] = st.nextToken();
                i++;
            }
            for (int z = 0; z < a.length; z++) {
                if (checknum(a[z], 1, 0, 255) == false) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Evs desc logic concepts.
     * 
     * @param limit the limit
     * @param searchTerm the search term
     * 
     * @return the list
     */
    private static List evsDescLogicConcepts(String searchTerm, int limit) {
        try {
            EVSQuery aEVSQuery = new EVSQueryImpl();
            List resultList = null;
            aEVSQuery.searchDescLogicConcepts(vocabularyName, searchTerm, limit);
            resultList = appService.evsSearch(aEVSQuery);
            PrintUtils print = new PrintUtils();
            print.printResults(resultList);
            for (int i = 0; i < resultList.size(); i++) {
                DescLogicConcept aDescLogicConcept = (DescLogicConcept) resultList.get(i);
            }
            return resultList;
        } catch (Exception e1) {
            return null;
        }
    }

    /**
     * The main method.
     * 
     * @param args the args
     * 
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        loadCDE(publicId, vocabularyName);
    }
}
