package issrg.test.isSufficient;

import issrg.pba.AuthzTokenParser;
import issrg.pba.Subject;
import issrg.pba.rbac.*;
import issrg.pba.rbac.x509.RepositoryACPolicyFinder;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.TimeZone;

/**
 *
 * @author anhnt
 */
public class TestIt {

    private issrg.utils.repository.VirtualRepository repository;

    private issrg.pba.repository.AuthzTokenRepository tokenRepository;

    private iaik.x509.X509Certificate x509;

    private issrg.security.DefaultSecurity ds;

    private issrg.pba.rbac.SignatureVerifier sv;

    private RoleHierarchyPolicy roleHierarchyPolicy;

    private issrg.pba.rbac.policies.AllocationPolicy allocationPolicy;

    private AuthzTokenParser parser;

    private RepositoryACPolicyFinder finder;

    private PermisRBAC rbac;

    private String separator;

    /** Creates a new instance of TestIt */
    public TestIt(String[] args) {
        try {
            separator = System.getProperty("file.separator");
            CustomisePERMIS.setAttributeCertificateAttribute("attributeCertificateAttribute");
            CustomisePERMIS.configureX509Flavour();
            repository = new issrg.utils.repository.VirtualRepository();
            java.io.InputStream io = new java.io.FileInputStream(args[1].concat(separator).concat("policy.ace"));
            byte[] ac = new byte[io.available()];
            io.read(ac);
            repository.populate("cn=soa,ou=admin,o=permis,c=gb", CustomisePERMIS.getAttributeCertificateAttribute(), ac);
            finder = new issrg.pba.rbac.x509.RepositoryACPolicyFinder(repository, "19.04.2005", new LDAPDNPrincipal("cn=soa,ou=admin,o=permis,c=gb"), null);
            parser = CustomisePERMIS.getAuthzTokenParser();
            parser.setAuthzTokenParsingRules(finder.getParsedPolicy().getAuthzTokenParsingRules());
            tokenRepository = new issrg.simplePERMIS.SimplePERMISAuthzTokenRepository(repository, parser);
            rbac = new PermisRBAC(finder, repository, parser);
        } catch (Exception e) {
        }
    }

    public void doTest1(String[] args) {
        try {
            java.io.InputStream io = new java.io.FileInputStream(args[1].concat(separator).concat("disTest1.ace"));
            byte[] ac = new byte[io.available()];
            io.read(ac);
            repository.populate("cn=dis,ou=admin,o=permis,c=gb", CustomisePERMIS.getAttributeCertificateAttribute(), ac);
            io = new java.io.FileInputStream(args[1].concat(separator).concat("aa1Test1.ace"));
            ac = new byte[io.available()];
            io.read(ac);
            repository.populate("cn=aa1,ou=staff,o=permis,c=gb", CustomisePERMIS.getAttributeCertificateAttribute(), ac);
            io = new java.io.FileInputStream(args[1].concat(separator).concat("aa2Test1.ace"));
            ac = new byte[io.available()];
            io.read(ac);
            repository.populate("cn=aa2,ou=staff,o=permis,c=gb", CustomisePERMIS.getAttributeCertificateAttribute(), ac);
            Subject s = rbac.getCreds(new LDAPDNPrincipal("cn=aa1,ou=staff,o=permis,c=gb"));
            System.out.println("Attriute of " + s.getHolder().getName() + " is: " + s.exportCreds().toString());
            if (rbac.decision(s, new PermisAction("open"), new PermisTarget("cn=door,c=gb", null), null)) {
                System.out.println("Action is allowed");
            } else System.out.println("Action is NOT allowed");
            s = rbac.getCreds(new LDAPDNPrincipal("cn=aa2,ou=staff,o=permis,c=gb"));
            System.out.println("Attriute of " + s.getHolder().getName() + " is: " + s.exportCreds().toString());
            if (rbac.decision(s, new PermisAction("open"), new PermisTarget("cn=door,c=gb", null), null)) {
                System.out.println("Action is allowed");
            } else System.out.println("Action is NOT allowed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doTest2(String[] args) {
        try {
            java.io.InputStream io = new java.io.FileInputStream(args[1].concat(separator).concat("aa1Test2.ace"));
            byte[] ac = new byte[io.available()];
            io.read(ac);
            repository.populate("cn=aa1,ou=staff,o=permis,c=gb", CustomisePERMIS.getAttributeCertificateAttribute(), ac);
            io = new java.io.FileInputStream(args[1].concat(separator).concat("disTest2.ace"));
            ac = new byte[io.available()];
            io.read(ac);
            repository.populate("cn=dis,ou=admin,o=permis,c=gb", CustomisePERMIS.getAttributeCertificateAttribute(), ac);
            io = new java.io.FileInputStream(args[1].concat(separator).concat("aa2Test2.ace"));
            ac = new byte[io.available()];
            io.read(ac);
            repository.populate("cn=aa2,ou=staff,o=permis,c=gb", CustomisePERMIS.getAttributeCertificateAttribute(), ac);
            io = new java.io.FileInputStream(args[1].concat(separator).concat("aa3Test2.ace"));
            ac = new byte[io.available()];
            io.read(ac);
            repository.populate("cn=aa3,ou=staff,o=permis,c=gb", CustomisePERMIS.getAttributeCertificateAttribute(), ac);
            io = new java.io.FileInputStream(args[1].concat(separator).concat("aa4Test2.ace"));
            ac = new byte[io.available()];
            io.read(ac);
            repository.populate("cn=aa4,ou=staff,o=permis,c=gb", CustomisePERMIS.getAttributeCertificateAttribute(), ac);
            Subject s = rbac.getCreds(new LDAPDNPrincipal("cn=aa2,ou=staff,o=permis,c=gb"));
            System.out.println("Attriute of " + s.getHolder().getName() + " is: " + s.exportCreds().toString());
            if (rbac.decision(s, new PermisAction("open"), new PermisTarget("cn=door,c=gb", null), null)) {
                System.out.println("Action is allowed");
            } else System.out.println("Action is NOT allowed");
            s = rbac.getCreds(new LDAPDNPrincipal("cn=aa3,ou=staff,o=permis,c=gb"));
            System.out.println("Attriute of " + s.getHolder().getName() + " is: " + s.exportCreds().toString());
            if (rbac.decision(s, new PermisAction("open"), new PermisTarget("cn=door,c=gb", null), null)) {
                System.out.println("Action is allowed");
            } else System.out.println("Action is NOT allowed");
            s = rbac.getCreds(new LDAPDNPrincipal("cn=aa4,ou=staff,o=permis,c=gb"));
            System.out.println("Attriute of " + s.getHolder().getName() + " is: " + s.exportCreds().toString());
            if (rbac.decision(s, new PermisAction("open"), new PermisTarget("cn=door,c=gb", null), null)) {
                System.out.println("Action is allowed");
            } else System.out.println("Action is NOT allowed");
            System.out.println("Now remove aa4's attribute, issue a new attribute to him and test again ");
            repository.remove("cn=aa4,ou=staff,o=permis,c=gb");
            io = new java.io.FileInputStream(args[1].concat(separator).concat("aa4NewTest2.ace"));
            ac = new byte[io.available()];
            io.read(ac);
            repository.populate("cn=aa4,ou=staff,o=permis,c=gb", CustomisePERMIS.getAttributeCertificateAttribute(), ac);
            s = rbac.getCreds(new LDAPDNPrincipal("cn=aa4,ou=staff,o=permis,c=gb"));
            System.out.println("Attriute of " + s.getHolder().getName() + " is: " + s.exportCreds().toString());
            if (rbac.decision(s, new PermisAction("open"), new PermisTarget("cn=door,c=gb", null), null)) {
                System.out.println("Action is allowed");
            } else System.out.println("Action is NOT allowed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.setProperty("line.separator", "\r\n");
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        try {
            System.setOut(new PrintStream(new FileOutputStream(args[0])));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.setErr(System.out);
        TestIt test1 = new TestIt(args);
        TestIt test2 = new TestIt(args);
        test1.doTest1(args);
        test2.doTest2(args);
        System.out.close();
    }
}
