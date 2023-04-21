package issrg.dis;

import issrg.ac.AttributeValue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 *
 * @author anhnt
 */
public class TestDIS {

    private static final String key_path = "keypath.cfg";

    private static Vector combineRoles = new Vector();

    static final String out = "/mnt/c/anhnt/out.txt";

    static BufferedWriter writer;

    /** Creates a new instance of TestDIS */
    public TestDIS() {
        String keyStore = "";
        String trustStore = "";
        String keyStorePassword = "";
        String endpoint = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(key_path)));
            String line;
            String left;
            while ((line = in.readLine()) != null) {
                line = line.intern();
                if (line == "") continue;
                int i = line.indexOf("=");
                left = line.substring(0, i);
                if (left.equals("trustStore")) {
                    trustStore = line.substring(i + 1).trim();
                }
                if (left.equals("keyStore")) {
                    keyStore = line.substring(i + 1);
                }
                if (left.equals("keyStorePassword")) {
                    keyStorePassword = line.substring(i + 1).trim();
                }
            }
            System.setProperty("javax.net.ssl.trustStore", trustStore);
            System.setProperty("javax.net.ssl.keyStore", keyStore);
            System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
        } catch (IOException ioe) {
            System.out.println("Could not find the config file" + key_path);
        }
    }

    private String[] convert(Vector rolesValues) {
        String[] ret = new String[rolesValues.size()];
        for (int i = 0; i < rolesValues.size(); i++) {
            ret[i] = (String) rolesValues.get(i);
        }
        return ret;
    }

    private void doJob(int count, String issuer, String holder, String[] roleValues, String roleType, String from, String to, String _assert, String depth) {
        try {
            org.apache.axis.client.Service service = new org.apache.axis.client.Service();
            org.apache.axis.client.Call call = (org.apache.axis.client.Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL("https://localhost:8443/axis/services/DISTest"));
            call.setOperationName(new javax.xml.namespace.QName("http://soapinterop.org/", "signForMe"));
            String[] ret = (String[]) call.invoke(new Object[] { issuer, holder, roleValues, roleType, from, to, _assert, depth });
            writer.write("=======================================================================");
            writer.newLine();
            writer.write("Test number:  " + count);
            writer.newLine();
            writer.write("Requester: " + issuer + "    Holder:   " + holder);
            writer.newLine();
            String rolevl = "";
            for (int i = 0; i < roleValues.length; i++) {
                rolevl = rolevl.concat(roleValues[i]);
                rolevl = rolevl + ", ";
            }
            writer.write("RoleType:  " + roleType + "  RoleValues:  " + rolevl);
            writer.newLine();
            writer.write("From:  " + from + "  To:  " + to);
            writer.newLine();
            writer.write("Assertion:  " + _assert + "  Depth:  " + depth);
            writer.newLine();
            writer.write("Result:");
            writer.newLine();
            String inform = ret[0];
            if (inform.indexOf(Comm.PUBLISH) < 0) {
                writer.write(ret[0]);
            } else {
                byte[] ac = issrg.utils.Base64.decode(ret[1]);
                issrg.ac.AttributeCertificate acc = issrg.ac.AttributeCertificate.guessEncoding(ac);
                String issuerResult = issrg.ac.Util.generalNamesToString(acc.getACInfo().getIssuer().getV2Form().getIssuerName());
                String holderResult = issrg.ac.Util.generalNamesToString(acc.getACInfo().getHolder().getEntityName());
                writer.write("Issuer:  " + issuerResult + "  Holder:  " + holderResult);
                writer.newLine();
                String notBefore = acc.getACInfo().getValidityPeriod().getNotBefore().toString();
                String notAfter = acc.getACInfo().getValidityPeriod().getNotAfter().toString();
                writer.write("From:  " + notBefore + "  To:  " + notAfter);
                writer.newLine();
                writer.write("Serial number:  " + acc.getACInfo().getSerialNumber().toString());
                writer.newLine();
                Vector attributes = acc.getACInfo().getAttributes();
                for (int i = 0; i < attributes.size(); i++) {
                    issrg.ac.Attribute a = (issrg.ac.Attribute) attributes.get(i);
                    String roleTypeResult = a.getType();
                    String roleValuesResult = "";
                    Vector values = a.getValues();
                    for (int j = 0; j < values.size(); j++) {
                        issrg.ac.attributes.PermisRole v = (issrg.ac.attributes.PermisRole) values.get(j);
                        roleValuesResult = roleValuesResult.concat(v.getRoleValue());
                        roleValuesResult = roleValuesResult + ", ";
                    }
                    writer.write("RoleType: " + roleTypeResult + " RoleValues:  " + roleValuesResult);
                    writer.newLine();
                }
                Vector exts = acc.getACInfo().getExtensions().getValues();
                for (int i = 0; i < exts.size(); i++) {
                    issrg.ac.Extension e = (issrg.ac.Extension) exts.get(i);
                    if (e instanceof issrg.ac.attributes.AttributeAuthorityInformationAccess) {
                        writer.write("AAIA: " + ((issrg.ac.attributes.AttributeAuthorityInformationAccess) e).toString());
                        writer.newLine();
                    }
                    if (e instanceof issrg.ac.attributes.AuthorityAttributeIdentifier) {
                        writer.write("AAI: " + ((issrg.ac.attributes.AuthorityAttributeIdentifier) e).toString());
                        writer.newLine();
                    }
                    if (e instanceof issrg.ac.attributes.BasicAttConstraint) {
                        int depthResult = ((issrg.ac.attributes.BasicAttConstraint) e).getExtensionIntegerValue() + 1;
                        writer.write("BAC: " + depthResult + ((issrg.ac.attributes.BasicAttConstraint) e).toString());
                        writer.newLine();
                    }
                    if (e instanceof issrg.ac.attributes.IssuedOnBehalfOf) {
                        writer.write("IOBO:  " + ((issrg.ac.attributes.IssuedOnBehalfOf) e).toString());
                        writer.newLine();
                    }
                    if (e instanceof issrg.ac.attributes.NoAssertion) {
                        writer.write("NoAssertion: " + ((issrg.ac.attributes.NoAssertion) e).toString());
                        writer.newLine();
                    }
                }
            }
            writer.newLine();
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void register() {
        issrg.ac.attributes.AttributeAuthorityInformationAccess.registerMe();
        issrg.ac.attributes.AuthorityAttributeIdentifier.registerMe();
        issrg.ac.attributes.BasicAttConstraint.registerMe();
        issrg.ac.attributes.IssuedOnBehalfOf.registerMe();
        issrg.ac.attributes.NoAssertion.registerMe();
        issrg.ac.attributes.PermisRole.registerMe("1.2.826.0.1.3344810.1.1.14");
    }

    /**
     * @param args the command line arguments
     */
    public void printPolicy() {
        try {
            writer.write("Testing policy");
            writer.newLine();
            writer.newLine();
            BufferedReader in = new BufferedReader(new java.io.FileReader("/home/anhnt/code/policy.xml"));
            String t;
            while ((t = in.readLine()) != null) {
                writer.write(t);
                writer.newLine();
            }
            writer.newLine();
            writer.write("DIS's AC");
            writer.newLine();
            writer.newLine();
            java.io.DataInputStream reader = new java.io.DataInputStream(new java.io.FileInputStream("/home/anhnt/code/disac.ace"));
            byte[] ac = new byte[reader.available()];
            reader.read(ac);
            issrg.ac.AttributeCertificate acc = issrg.ac.AttributeCertificate.guessEncoding(ac);
            String issuerResult = issrg.ac.Util.generalNamesToString(acc.getACInfo().getIssuer().getV2Form().getIssuerName());
            String holderResult = issrg.ac.Util.generalNamesToString(acc.getACInfo().getHolder().getEntityName());
            writer.write("Issuer:  " + issuerResult + "  Holder:  " + holderResult);
            writer.newLine();
            String notBefore = acc.getACInfo().getValidityPeriod().getNotBefore().toString();
            String notAfter = acc.getACInfo().getValidityPeriod().getNotAfter().toString();
            writer.write("From:  " + notBefore + "  To:  " + notAfter);
            writer.newLine();
            writer.write("Serial number:  " + acc.getACInfo().getSerialNumber().toString());
            writer.newLine();
            Vector attributes = acc.getACInfo().getAttributes();
            for (int i = 0; i < attributes.size(); i++) {
                issrg.ac.Attribute a = (issrg.ac.Attribute) attributes.get(i);
                String roleTypeResult = a.getType();
                String roleValuesResult = "";
                Vector values = a.getValues();
                for (int j = 0; j < values.size(); j++) {
                    issrg.ac.attributes.PermisRole v = (issrg.ac.attributes.PermisRole) values.get(j);
                    roleValuesResult = roleValuesResult.concat(v.getRoleValue());
                    roleValuesResult = roleValuesResult + ", ";
                }
                writer.write("RoleType: " + roleTypeResult + " RoleValues:  " + roleValuesResult);
                writer.newLine();
            }
            Vector exts = acc.getACInfo().getExtensions().getValues();
            for (int i = 0; i < exts.size(); i++) {
                issrg.ac.Extension e = (issrg.ac.Extension) exts.get(i);
                if (e instanceof issrg.ac.attributes.AttributeAuthorityInformationAccess) {
                    writer.write("AAIA: " + ((issrg.ac.attributes.AttributeAuthorityInformationAccess) e).toString());
                    writer.newLine();
                }
                if (e instanceof issrg.ac.attributes.AuthorityAttributeIdentifier) {
                    writer.write("AAI: " + ((issrg.ac.attributes.AuthorityAttributeIdentifier) e).toString());
                    writer.newLine();
                }
                if (e instanceof issrg.ac.attributes.BasicAttConstraint) {
                    int depthResult = ((issrg.ac.attributes.BasicAttConstraint) e).getExtensionIntegerValue() + 1;
                    writer.write("BAC: " + depthResult + ((issrg.ac.attributes.BasicAttConstraint) e).toString());
                    writer.newLine();
                }
                if (e instanceof issrg.ac.attributes.IssuedOnBehalfOf) {
                    writer.write("IOBO:  " + ((issrg.ac.attributes.IssuedOnBehalfOf) e).toString());
                    writer.newLine();
                }
                if (e instanceof issrg.ac.attributes.NoAssertion) {
                    writer.write("NoAssertion: " + ((issrg.ac.attributes.NoAssertion) e).toString());
                    writer.newLine();
                }
            }
            writer.newLine();
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void expected(String errorMessage, String roles, String valid, String assertion, String depth) {
        try {
            writer.write("Exptected result: ");
            writer.newLine();
            if (errorMessage == null) {
                writer.write("Roles:  " + roles);
                writer.newLine();
                writer.write("Validity time:  " + valid);
                writer.newLine();
                writer.write("Can assert:  " + assertion);
                writer.newLine();
                writer.write("Delegation depth:  " + depth);
                writer.newLine();
                writer.newLine();
            } else {
                writer.write("Error:  " + errorMessage);
                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            writer = new BufferedWriter(new FileWriter(out));
            register();
            int count = 0;
            TestDIS test = new TestDIS();
            test.printPolicy();
            String[] roles = new String[] { "Admin" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=dis,ou=admin,o=permis,c=gb", roles, "permisRole", "2004.01.01 00:00:00", "2010.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(Comm.HOLDER_CAN_NOT_BE_DIS_OR_SOA, null, null, null, null);
            roles = new String[] { "Admin", "Professor" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(null, "Admin", "2004.01.01 00:00:00 to 2010.01.01 00:00:00", "cannot", "Unlimited");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2020.01.01 00:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2010.01.01 00:00:00", "can", "No delegate");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2001.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Researcher", "2004.01.01 00:00:00 to 2006.01.01 00:00:00", "can", "2");
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "perRole", "2005.01.01 00:00:00", "2007.01.01 00:00:00", "can", "2");
            count++;
            test.expected(Comm.ROLETYPE_IS_NOT_SUPPORTED_IN_POLICY, null, null, null, null);
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=soa1, o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(Comm.HOLDER_OUT_OF_SUBJECTDOMAIN, null, null, null, null);
            roles = new String[] { "Professor", "Researcher" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=aa1,ou=staff,o=permis,c=gb", roles, "permisRole", "2004.06.01 12:00:00", "2007.08.27 12:00:00", "can", "-1");
            count++;
            test.expected(null, "Professor, Researcher", "2004.06.01 12:00:00 to 2007.08.27 00:00:00", "can", "No delegate");
            roles = new String[] { "Professor", "Staff" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=aa1,ou=staff,o=permis,c=gb", roles, "permisRole", "2004.06.01 12:00:00", "2007.08.27 12:00:00", "can", "0");
            count++;
            test.expected(null, "Professor", "2004.06.01 12:00:00 to 2007.08.27 00:00:00", "can", "Unlimited");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=aa1,ou=staff,o=permis,c=gb", roles, "permisRole", "2004.06.01 12:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Researcher", "2004.06.01 12:00:00 to 2007.08.27 00:00:00", "can", "2");
            roles = new String[] { "Admin", "Researcher" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=aa1,ou=staff,o=permis,c=gb", roles, "permisRole", "2004.06.01 12:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Professor, Researcher", "2004.06.01 12:00:00 to 2007.08.27 00:00:00", "can", "2");
            roles = new String[] { "Professor", "Researcher" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=aa1,ou=staff,o=permis,c=gb", roles, "permisRole", "2003.06.01 12:00:00", "2008.08.27 12:00:00", "cannot", "-1");
            count++;
            test.expected(null, "Professor, Researcher", "2004.06.01 00:00:00 to 2007.08.27 00:00:00", "cannot", "No delegate");
            roles = new String[] { "Professor", "Staff" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=aa1,ou=staff,o=permis,c=gb", roles, "permisRole", "2005.06.01 12:00:00", "2006.08.27 12:00:00", "cannot", "0");
            count++;
            test.expected(null, "Professor", "2005.06.01 12:00:00 to 2006.08.27 12:00:00", "cannot", "Unlimited");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=aa1,ou=staff,o=permis,c=gb", roles, "permisRole", "2003.06.01 12:00:00", "2005.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Researcher", "2004.06.01 12:00:00 to 2005.08.27 12:00:00", "can", "2");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2004.06.10 12:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 12:00:00 to 2007.08.27 00:00:00", "can", "2");
            roles = new String[] { "Professor", "Student" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2003.06.01 12:00:00", "2008.08.27 12:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2007.08.27 00:00:00", "can", "No Delegate");
            roles = new String[] { "Staff", "Student" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2003.06.01 12:00:00", "2006.08.27 12:00:00", "can", "0");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2006.08.27 12:00:00", "can", "Unlimited");
            roles = new String[] { "Researcher", "Professor", "Staff" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2005.06.01 12:00:00", "2009.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2005.06.01 12:00:00 to 2007.08.27 00:00:00", "can", "2");
            roles = new String[] { "Admin" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2004.06.10 00:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2007.08.27 00:00:00", "can", "2");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2005.06.01 12:00:00", "2006.08.27 12:00:00", "cannot", "-1");
            count++;
            test.expected(null, "Student", "2005.06.01 12:00:00 to 2006.08.27 12:00:00", "cannot", "No delegate");
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2009.06.01 12:00:00", "2004.08.27 12:00:00", "cannot", "0");
            count++;
            test.expected(Comm.ERROR_WITH_VALIDITY_TIME, null, null, null, null);
            roles = new String[] { "Student", "Staff" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2000.06.01 12:00:00", "2010.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2007.08.27 00:00:00", "can", "2");
            roles = new String[] { "Researcher" };
            test.doJob(count, "cn=soa,ou=admin,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2000.06.01 12:00:00", "2000.08.27 12:00:00", "can", "2");
            count++;
            test.expected(Comm.ERROR_WITH_VALIDITY_TIME, null, null, null, null);
            roles = new String[] { "Admin" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2004.01.01 12:00:00", "2010.01.01 12:00:00", "cannot", "0");
            count++;
            test.expected(null, "Admin", "2004.01.01 12:00:00 to 2010.01.01 00:00:00", "cannot", "Unlimited");
            roles = new String[] { "Admin", "Professor" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(null, "Admin", "2004.01.01 00:00:00 to 2010.01.01 00:00:00", "cannot", "Unlimited");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2008.01.01 00:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2008.01.01 00:00:00", "can", "No delegate");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2001.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Researcher", "2004.01.01 00:00:00 to 2006.01.01 00:00:00", "can", "2");
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "perRole", "2005.01.01 00:00:00", "2012.01.01 00:00:00", "can", "2");
            count++;
            test.expected(Comm.ROLETYPE_IS_NOT_SUPPORTED_IN_POLICY, null, null, null, null);
            roles = new String[] { "Admin", "Professor" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=dis,ou=admin,o=permis,c=gb", roles, "permisRole", "2006.01.01 00:00:00", "2009.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(Comm.HOLDER_CAN_NOT_BE_DIS_OR_SOA, null, null, null, null);
            roles = new String[] { "Professor", "Researcher" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2004.06.01 12:00:00", "2007.08.27 12:00:00", "cannot", "0");
            count++;
            test.expected(null, "Researcher, Professor", "2004.06.01 12:00:00 to 2007.08.27 00:00:00", "cannot", "Unlimited");
            roles = new String[] { "Admin", "Professor" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(null, "Researcher, Professor", "2004.06.01 00:00:00 to 2007.08.27 00:00:00", "cannot", "Unlimited");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2006.01.01 00:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2006.01.01 00:00:00", "can", "No Delegate");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2001.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Researcher", "2004.06.01 00:00:00 to 2006.01.01 00:00:00", "can", "2");
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2012.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Staff", "2005.01.01 00:00:00 to 2007.08.27 00:00:00", "can", "2");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=student2,ou=student,o=permis,c=gb", roles, "permisRole", "2004.06.10 12:00:00", "2007.08.27 12:00:00", "cannot", "0");
            count++;
            test.expected(null, "Student", "2004.06.10 12:00:00 to 2007.08.27 00:00:00", "cannot", "Unlimited");
            roles = new String[] { "Admin", "Professor" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=student2,ou=student,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2007.08.27 00:00:00", "cannot", "Unlimited");
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=student2,ou=student,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2006.01.01 00:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2006.01.01 00:00:00", "can", "No delegate");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=student2,ou=student,o=permis,c=gb", roles, "permisRole", "2001.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2006.01.01 00:00:00", "can", "2");
            roles = new String[] { "Professor" };
            test.doJob(count, "cn=dis,ou=admin,o=permis,c=gb", "cn=student2,ou=student,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2012.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2007.08.27 00:00:00", "can", "2");
            roles = new String[] { "Admin" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(Comm.HOLDER_OUT_OF_SUBJECTDOMAIN, null, null, null, null);
            roles = new String[] { "Student" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2006.01.01 00:00:00", "2007.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(Comm.HOLDER_OUT_OF_SUBJECTDOMAIN, null, null, null, null);
            roles = new String[] { "Admin" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=dis,ou=admin,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(Comm.HOLDER_CAN_NOT_BE_DIS_OR_SOA, null, null, null, null);
            roles = new String[] { "Student" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=dis,ou=admin,o=permis,c=gb", roles, "permisRole", "2006.01.01 00:00:00", "2007.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(Comm.HOLDER_CAN_NOT_BE_DIS_OR_SOA, null, null, null, null);
            roles = new String[] { "Professor", "Researcher" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2004.06.01 12:00:00", "2007.08.27 12:00:00", "cannot", "0");
            count++;
            test.expected(null, "Professor, Researcher", "2004.06.01 12:00:00 to 2007.08.27 00:00:00", "cannot", "Unlimited");
            roles = new String[] { "Admin", "Professor" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(null, "Professor, Researcher", "2004.06.01 00:00:00 to 2007.08.27 00:00:00", "cannot", "Unlimited");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2006.01.01 00:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2006.01.01 00:00:00", "can", "No delegation");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2001.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Researcher", "2004.01.01 00:00:00 to 2006.01.01 00:00:00", "can", "1");
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2012.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Staff", "2005.01.01 00:00:00 to 2006.01.01 00:00:00", "can", "1");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=student2,ou=student,o=permis,c=gb", roles, "permisRole", "2004.06.10 12:00:00", "2007.08.27 12:00:00", "cannot", "0");
            count++;
            test.expected(null, "Student", "2004.06.10 12:00:00 to 2006.01.01 00:00:00", "cannot", "1");
            roles = new String[] { "Admin", "Professor" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=student2,ou=student,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2006.01.01 00:00:00", "cannot", "1");
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=student2,ou=student,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2006.01.01 00:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2006.01.01 00:00:00", "can", "No delegation");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=student2,ou=student,o=permis,c=gb", roles, "permisRole", "2001.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2006.01.01 00:00:00", "can", "1");
            roles = new String[] { "Professor" };
            test.doJob(count, "cn=admin1,ou=admin,o=permis,c=gb", "cn=student2,ou=student,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2012.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2006.01.01 00:00:00", "can", "1");
            roles = new String[] { "Admin" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2004.01.01 12:00:00", "2010.01.01 12:00:00", "cannot", "0");
            count++;
            test.expected(null, "Professor, Researcher", "2004.06.01 12:00:00 to 2007.08.27 00:00:00", "cannot", "1");
            roles = new String[] { "Admin", "Professor" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(null, "Professor, Researcher", "2004.06.01 12:00:00 to 2007.08.27 00:00:00", "cannot", "1");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2008.01.01 00:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2001.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Researcher", "2004.06.01 12:00:00 to 2005.08.27 12:00:00", "can", "1");
            roles = new String[] { "Staff", "Presenter" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2012.01.01 00:00:00", "can", "2");
            count++;
            test.expected(Comm.ERROR_ROLEVALUE_DO_NOT_EXIST, null, null, null, null);
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=aa1, ou=staff,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(Comm.HOLDER_OUT_OF_SUBJECTDOMAIN, null, null, null, null);
            roles = new String[] { "Professor", "Researcher" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2004.06.01 12:00:00", "2007.08.27 12:00:00", "cannot", "0");
            count++;
            test.expected(null, "Professor, Researcher", "2004.06.01 12:00:00 to 2007.08.27 00:00:00", "cannot", "1");
            roles = new String[] { "Admin", "Professor" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(null, "Professor, Researcher", "2004.06.01 12:00:00 to 2007.08.27 00:00:00", "cannot", "1");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2006.01.01 00:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2001.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Researcher", "2004.06.01 12:00:00 to 2005.08.27 12:00:00", "can", "1");
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2012.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Staff", "2005.01.01 00:00:00 to 2005.08.27 12:00:00", "can", "1");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2004.06.10 12:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 12:00:00 to 2005.08.27 12:00:00", "can", "1");
            roles = new String[] { "Professor", "Student" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2003.06.01 12:00:00", "2008.08.27 12:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Staff", "Student" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2003.06.01 12:00:00", "2006.08.27 12:00:00", "can", "0");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "can", "1");
            roles = new String[] { "Researcher", "Professor", "Staff" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2005.06.01 12:00:00", "2009.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2005.06.01 12:00:00 to 2005.08.27 12:00:00", "can", "1");
            roles = new String[] { "Admin" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2004.06.10 00:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "can", "1");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2005.06.01 12:00:00", "2006.08.27 12:00:00", "cannot", "-1");
            count++;
            test.expected(null, "Student", "2005.06.01 12:00:00 to 2005.08.27 12:00:00", "cannot", "No delegate");
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2009.06.01 12:00:00", "2004.08.27 12:00:00", "cannot", "0");
            count++;
            test.expected(Comm.ERROR_WITH_VALIDITY_TIME, null, null, null, null);
            roles = new String[] { "Student", "Staff" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2000.06.01 12:00:00", "2010.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "can", "1");
            roles = new String[] { "Researcher" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2000.06.01 12:00:00", "2000.08.27 12:00:00", "can", "2");
            count++;
            test.expected(Comm.ERROR_WITH_VALIDITY_TIME, null, null, null, null);
            roles = new String[] { "Student" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=student5,ou=student,o=permis,c=gb", roles, "permisRole", "2004.06.10 12:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(Comm.HOLDER_OUT_OF_SUBJECTDOMAIN, null, null, null, null);
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=aa1,ou=staff,o=permis,c=gb", "cn=student5,ou=student,o=permis,c=gb", roles, "permisRole", "2006.06.10 12:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(Comm.HOLDER_OUT_OF_SUBJECTDOMAIN, null, null, null, null);
            roles = new String[] { "Admin" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2004.01.01 12:00:00", "2010.01.01 12:00:00", "cannot", "0");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "cannot", "No delegate");
            roles = new String[] { "Admin", "Professor" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "cannot", "No delegate");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2008.01.01 00:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2001.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Staff", "Presenter" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2012.01.01 00:00:00", "can", "2");
            count++;
            test.expected(Comm.ERROR_ROLEVALUE_DO_NOT_EXIST, null, null, null, null);
            roles = new String[] { "Professor", "Researcher" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2004.06.01 12:00:00", "2007.08.27 12:00:00", "cannot", "0");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "cannot", "No delegate");
            roles = new String[] { "Admin", "Professor" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2003.01.01 00:00:00", "2011.01.01 00:00:00", "cannot", "0");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "cannot", "No delegate");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2006.01.01 00:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2001.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=aa2,ou=staff,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2012.01.01 00:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2005.01.01 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Student" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student1,ou=student,o=permis,c=gb", roles, "permisRole", "2005.01.01 00:00:00", "2006.01.01 00:00:00", "can", "2");
            count++;
            test.expected(Comm.HOLDER_OUT_OF_SUBJECTDOMAIN, null, null, null, null);
            roles = new String[] { "Student" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student5,ou=student,o=permis,c=gb", roles, "permisRole", "2004.06.10 12:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(Comm.HOLDER_OUT_OF_SUBJECTDOMAIN, null, null, null, null);
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student5,ou=student,o=permis,c=gb", roles, "permisRole", "2006.06.10 12:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(Comm.HOLDER_OUT_OF_SUBJECTDOMAIN, null, null, null, null);
            roles = new String[] { "Student" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student3,ou=student,o=permis,c=gb", roles, "permisRole", "2004.06.10 12:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 12:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Professor", "Student" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student3,ou=student,o=permis,c=gb", roles, "permisRole", "2003.06.01 12:00:00", "2008.08.27 12:00:00", "can", "-1");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Staff", "Student" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student3,ou=student,o=permis,c=gb", roles, "permisRole", "2003.06.01 12:00:00", "2006.08.27 12:00:00", "can", "0");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Researcher", "Professor", "Staff" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student3,ou=student,o=permis,c=gb", roles, "permisRole", "2005.06.01 12:00:00", "2009.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2005.06.01 12:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Admin" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student3,ou=student,o=permis,c=gb", roles, "permisRole", "2004.06.10 00:00:00", "2007.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Student", "Researcher" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student3,ou=student,o=permis,c=gb", roles, "permisRole", "2005.06.01 12:00:00", "2006.08.27 12:00:00", "cannot", "-1");
            count++;
            test.expected(null, "Student", "2005.06.01 12:00:00 to 2005.08.27 12:00:00", "cannot", "No delegate");
            roles = new String[] { "Staff" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student3,ou=student,o=permis,c=gb", roles, "permisRole", "2009.06.01 12:00:00", "2004.08.27 12:00:00", "cannot", "0");
            count++;
            test.expected(Comm.ERROR_WITH_VALIDITY_TIME, null, null, null, null);
            roles = new String[] { "Student", "Staff" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student3,ou=student,o=permis,c=gb", roles, "permisRole", "2000.06.01 12:00:00", "2010.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Student", "2004.06.10 00:00:00 to 2005.08.27 12:00:00", "can", "No delegate");
            roles = new String[] { "Researcher" };
            test.doJob(count, "cn=student1,ou=student,o=permis,c=gb", "cn=student3,ou=student,o=permis,c=gb", roles, "permisRole", "2000.06.01 12:00:00", "2000.08.27 12:00:00", "can", "2");
            count++;
            test.expected(Comm.ERROR_WITH_VALIDITY_TIME, null, null, null, null);
            roles = new String[] { "Researcher" };
            test.doJob(count, "cn=soa1,o=permis,c=gb", "cn=student3,ou=student,o=permis,c=gb", roles, "permisRole", "2000.06.01 12:00:00", "2000.08.27 12:00:00", "can", "2");
            count++;
            test.expected(Comm.ISSUER_OUT_OF_DOMAIN, null, null, null, null);
            roles = new String[] { "Researcher", "Professor" };
            test.doJob(count, "cn=aa2,ou=staff, o=permis,c=gb", "cn=admin1,ou=admin,o=permis,c=gb", roles, "permisRole", "2000.06.01 12:00:00", "2009.08.27 12:00:00", "can", "2");
            count++;
            test.expected(null, "Researcher, Professor", "2004.06.01 12:00:00 to 2007.08.27 00:00:00", "can", "No delegate");
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
