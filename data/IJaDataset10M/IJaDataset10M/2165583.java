package edu.vt.middleware.ldap;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import edu.vt.middleware.ldap.bean.LdapEntry;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link LdapCli} class.
 *
 * @author  Middleware Services
 * @version  $Revision: 1330 $
 */
public class LdapCliTest {

    /** Entry created for ldap tests. */
    private static LdapEntry testLdapEntry;

    /**
   * @param  ldifFile  to create.
   *
   * @throws  Exception  On test failure.
   */
    @Parameters({ "createEntry5" })
    @BeforeClass(groups = { "ldapclitest" })
    public void createLdapEntry(final String ldifFile) throws Exception {
        final String ldif = TestUtil.readFileIntoString(ldifFile);
        testLdapEntry = TestUtil.convertLdifToEntry(ldif);
        Ldap ldap = TestUtil.createSetupLdap();
        ldap.create(testLdapEntry.getDn(), testLdapEntry.getLdapAttributes().toAttributes());
        ldap.close();
        ldap = TestUtil.createLdap();
        while (!ldap.compare(testLdapEntry.getDn(), new SearchFilter(testLdapEntry.getDn().split(",")[0]))) {
            Thread.sleep(100);
        }
        ldap.close();
    }

    /** @throws  Exception  On test failure. */
    @AfterClass(groups = { "ldapclitest" })
    public void deleteLdapEntry() throws Exception {
        final Ldap ldap = TestUtil.createSetupLdap();
        ldap.delete(testLdapEntry.getDn());
        ldap.close();
    }

    /**
   * @param  args  List of delimited arguments to pass to the CLI.
   * @param  ldifFile  to compare with
   *
   * @throws  Exception  On test failure.
   */
    @Parameters({ "cliSearchArgs", "cliSearchResults" })
    @Test(groups = { "ldapclitest" })
    public void search(final String args, final String ldifFile) throws Exception {
        final String ldif = TestUtil.readFileIntoString(ldifFile);
        final PrintStream oldStdOut = System.out;
        try {
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outStream));
            LdapCli.main(args.split("\\|"));
            AssertJUnit.assertEquals(TestUtil.convertLdifToEntry(ldif), TestUtil.convertLdifToEntry(outStream.toString()));
        } finally {
            System.setOut(oldStdOut);
        }
    }
}
