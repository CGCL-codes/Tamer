package org.ldaptive.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.ldaptive.AbstractTest;
import org.ldaptive.LdapEntry;
import org.ldaptive.TestUtils;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link AuthenticatorCli} class.
 *
 * @author  Middleware Services
 * @version  $Revision: 2329 $
 */
public class AuthenticatorCliTest extends AbstractTest {

    /** Entry created for ldap tests. */
    private static LdapEntry testLdapEntry;

    /**
   * @param  ldifFile  to create.
   *
   * @throws  Exception  On test failure.
   */
    @Parameters("createEntry9")
    @BeforeClass(groups = { "authcli" })
    public void createLdapEntry(final String ldifFile) throws Exception {
        final String ldif = TestUtils.readFileIntoString(ldifFile);
        testLdapEntry = TestUtils.convertLdifToResult(ldif).getEntry();
        super.createLdapEntry(testLdapEntry);
        System.setProperty("javax.net.ssl.trustStore", "target/test-classes/ldaptive.truststore");
        System.setProperty("javax.net.ssl.trustStoreType", "BKS");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
    }

    /** @throws  Exception  On test failure. */
    @AfterClass(groups = { "authcli" })
    public void deleteLdapEntry() throws Exception {
        System.clearProperty("javax.net.ssl.trustStore");
        System.clearProperty("javax.net.ssl.trustStoreType");
        System.clearProperty("javax.net.ssl.trustStorePassword");
        super.deleteLdapEntry(testLdapEntry.getDn());
    }

    /**
   * @param  args  List of delimited arguments to pass to the CLI.
   * @param  ldifFile  to compare with
   *
   * @throws  Exception  On test failure.
   */
    @Parameters({ "cliAuthTLSArgs", "cliAuthResults" })
    @Test(groups = { "authcli" })
    public void authenticateTLS(final String args, final String ldifFile) throws Exception {
        authenticate(args, ldifFile);
    }

    /**
   * @param  args  List of delimited arguments to pass to the CLI.
   * @param  ldifFile  to compare with
   *
   * @throws  Exception  On test failure.
   */
    @Parameters({ "cliAuthSSLArgs", "cliAuthResults" })
    @Test(groups = { "authcli" })
    public void authenticateSSL(final String args, final String ldifFile) throws Exception {
        authenticate(args, ldifFile);
    }

    /**
   * @param  args  List of delimited arguments to pass to the CLI.
   * @param  ldifFile  to compare with
   *
   * @throws  Exception  On test failure.
   */
    private void authenticate(final String args, final String ldifFile) throws Exception {
        final String ldif = TestUtils.readFileIntoString(ldifFile);
        final PrintStream oldStdOut = System.out;
        try {
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outStream));
            AuthenticatorCli.main(args.split("\\|"));
            AssertJUnit.assertEquals(TestUtils.convertLdifToResult(ldif), TestUtils.convertLdifToResult(outStream.toString()));
        } finally {
            System.setOut(oldStdOut);
        }
    }
}
