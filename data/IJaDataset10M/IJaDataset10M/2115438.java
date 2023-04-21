package org.openthinclient.common.test.ldap;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.directory.server.sar.DirectoryService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openthinclient.common.model.OrganizationalUnit;
import org.openthinclient.ldap.DirectoryException;
import org.openthinclient.ldap.DirectoryFacade;
import org.openthinclient.ldap.DiropLogger;
import org.openthinclient.ldap.LDAPConnectionDescriptor;
import org.openthinclient.ldap.Mapping;
import org.openthinclient.ldap.Util;
import org.openthinclient.ldap.LDAPConnectionDescriptor.ProviderType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AbstractEmbeddedDirectoryTest {

    private static DirectoryService ds;

    private static short ldapPort;

    protected static String baseDN = "dc=test,dc=test";

    protected static String envDN = "ou=test," + baseDN;

    @BeforeClass
    public static void setUp() throws Exception {
        DiropLogger.LOG.enable(true, true);
        ds = new DirectoryService();
        ds.setEmbeddedAccessControlEnabled(false);
        ds.setEmbeddedAnonymousAccess(true);
        ds.setEmbeddedServerEnabled(true);
        ds.setContextFactory("org.apache.directory.server.jndi.ServerContextFactory");
        ds.setContextProviderURL("uid=admin,ou=system");
        ds.setContextSecurityAuthentication("simple");
        ds.setContextSecurityCredentials("secret");
        ds.setContextSecurityPrincipal("uid=admin,ou=system");
        ds.setEmbeddedCustomRootPartitionName("dc=test,dc=test");
        ds.setEmbeddedWkdir("unit-test-tmp");
        final DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        final DocumentBuilder b = f.newDocumentBuilder();
        final Document d = b.newDocument();
        final Element wrapper = d.createElement("xml-properties");
        d.appendChild(wrapper);
        final Element e = d.createElement("config-property");
        e.setAttribute("name", "NisSchema");
        e.appendChild(d.createTextNode("org.apache.directory.server.core.schema.bootstrap.NisSchema"));
        wrapper.appendChild(e);
        ds.setEmbeddedCustomBootstrapSchema(wrapper);
        ds.setEmbeddedEnableNtp(false);
        ds.setEmbeddedEnableKerberos(false);
        ds.setEmbeddedEnableChangePassword(false);
        ds.setEmbeddedLDAPNetworkingSupport(true);
        ldapPort = getRandomNumber();
        ds.setEmbeddedLDAPPort(ldapPort);
        ds.start();
    }

    @AfterClass
    public static void cleanUp() {
        if (null != ds) ds.stop();
        deleteRecursively(new File("unit-test-tmp"));
    }

    protected static LDAPConnectionDescriptor getConnectionDescriptor() {
        final LDAPConnectionDescriptor lcd = new LDAPConnectionDescriptor();
        lcd.setPortNumber(ldapPort);
        lcd.setProviderType(ProviderType.SUN);
        return lcd;
    }

    static void deleteRecursively(File file) {
        if (!file.exists()) return;
        if (file.isDirectory()) for (final File f : file.listFiles()) if (f.isDirectory()) deleteRecursively(f); else f.delete();
        file.delete();
    }

    private static short getRandomNumber() {
        final Random ran = new Random();
        return (short) (11000 + ran.nextInt(999));
    }

    protected Mapping mapping;

    protected LDAPConnectionDescriptor connectionDescriptor;

    @Before
    public void initEnv() throws Exception {
        Mapping.disableCache = true;
        connectionDescriptor = getConnectionDescriptor();
        connectionDescriptor.setBaseDN(baseDN);
        final DirectoryFacade facade = connectionDescriptor.createDirectoryFacade();
        final DirContext ctx = facade.createDirContext();
        try {
            Util.deleteRecursively(ctx, facade.makeRelativeName(envDN));
        } catch (final NameNotFoundException e) {
        } finally {
            ctx.close();
        }
        mapping = Mapping.load(getClass().getResourceAsStream("/org/openthinclient/common/directory/APACHE_DS.xml"));
        mapping.initialize();
        mapping.setConnectionDescriptor(connectionDescriptor);
        final OrganizationalUnit ou = new OrganizationalUnit();
        ou.setName("test");
        ou.setDescription("openthinclient.org Console");
        mapping.save(ou, "");
        connectionDescriptor.setBaseDN(envDN);
        mapping.setConnectionDescriptor(connectionDescriptor);
        final OrganizationalUnit clients = new OrganizationalUnit();
        clients.setName("clients");
        mapping.save(clients, "");
        final OrganizationalUnit users = new OrganizationalUnit();
        users.setName("users");
        mapping.save(users, "");
        final OrganizationalUnit usergroups = new OrganizationalUnit();
        usergroups.setName("usergroups");
        mapping.save(usergroups, "");
        final OrganizationalUnit apps = new OrganizationalUnit();
        apps.setName("apps");
        mapping.save(apps, "");
        final OrganizationalUnit appgroups = new OrganizationalUnit();
        appgroups.setName("appgroups");
        mapping.save(appgroups, "");
        final OrganizationalUnit devices = new OrganizationalUnit();
        devices.setName("devices");
        mapping.save(devices, "");
        final OrganizationalUnit locations = new OrganizationalUnit();
        locations.setName("locations");
        mapping.save(locations, "");
        final OrganizationalUnit hwtypes = new OrganizationalUnit();
        hwtypes.setName("hwtypes");
        mapping.save(hwtypes, "");
        final OrganizationalUnit printers = new OrganizationalUnit();
        printers.setName("printers");
        mapping.save(printers, "");
        final OrganizationalUnit unrecognizedClients = new OrganizationalUnit();
        unrecognizedClients.setName("unrecognized-clients");
        mapping.save(unrecognizedClients, "");
    }

    @After
    public void destroyEnv() throws IOException, DirectoryException, NamingException {
        final LDAPConnectionDescriptor lcd = getConnectionDescriptor();
        lcd.setBaseDN(baseDN);
        final DirectoryFacade facade = lcd.createDirectoryFacade();
        final DirContext ctx = facade.createDirContext();
        try {
            Util.deleteRecursively(ctx, facade.makeRelativeName(envDN));
        } catch (final NameNotFoundException e) {
        } finally {
            ctx.close();
            Runtime.getRuntime().gc();
        }
    }
}
