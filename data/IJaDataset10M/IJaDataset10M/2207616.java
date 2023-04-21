package net.ratshome.mailfilter;

import java.util.Properties;
import java.util.Enumeration;
import junit.framework.*;
import net.ratshome.mailfilter.MessageInfo;

public class JumfiTest extends TestCase {

    private Jumfi jumfitotest = null;

    private Properties alladaptors = null;

    private Properties allfilters = null;

    private Properties allmodifiers = null;

    /** Main method is run to start the tests
	 * @param args arguments passed on the commandline
   */
    public static void main(String args[]) {
        junit.textui.TestRunner.run(JumfiTest.class);
    }

    /** Setup method creates objects needed for all the tests 
	 *  This is run before every test.
   */
    protected void setUp() {
        jumfitotest = new Jumfi();
    }

    /** Test that checks if the creation of a jumfi object is successfull
   */
    public void testCreateJumfi() {
        Assert.assertTrue(!jumfitotest.equals(null));
    }

    /** Test that checks if the registration of filters and adaptors is functioning
	 * Pre: No filters or adaptors have been loaded
	 * Post: Zero or more filters and or adaptors have been loaded
   */
    public void test_registerFiltersAndAdaptors() {
        alladaptors = new Properties();
        allfilters = new Properties();
        allmodifiers = new Properties();
        jumfitotest.registerFiltersAndAdaptors(alladaptors, allfilters, allmodifiers);
    }

    /** Test that checks if the found filters getIdentification interface is functioning within constraints
	 * Filter must return a non null identification string
	 * Filter must return a non empty identification string
	 */
    public void test_foundFilters_identification() {
        alladaptors = new Properties();
        allfilters = new Properties();
        allmodifiers = new Properties();
        jumfitotest.registerFiltersAndAdaptors(alladaptors, allfilters, allmodifiers);
        for (Enumeration e = allfilters.keys(); e.hasMoreElements(); ) {
            MailFilter testfilter = (MailFilter) allfilters.get((String) e.nextElement());
            Assert.assertTrue(!testfilter.getIdentification().equals(null));
            Assert.assertTrue(!testfilter.getIdentification().equals(""));
        }
    }

    /** Test that checks if the found  adaptors getRequiredInformationTypes interface is functioning within constraints
	 * RequiredInformationTypes must be in the range of 0..7
	 */
    public void test_foundFilters_informationtypes() {
        alladaptors = new Properties();
        allfilters = new Properties();
        allmodifiers = new Properties();
        jumfitotest.registerFiltersAndAdaptors(alladaptors, allfilters, allmodifiers);
        for (Enumeration e = allfilters.keys(); e.hasMoreElements(); ) {
            MailFilter testfilter = (MailFilter) allfilters.get((String) e.nextElement());
            int requiredtypes = testfilter.getRequiredInformationTypes();
            Assert.assertTrue(((requiredtypes >= 0) && (requiredtypes <= 7)));
        }
    }

    /** Test that checks if the found  adaptors getRequiredPhases interface is functioning within constraints
	 * RequiredPhases must be in the range of 0..15 for all inputs of informationtype (1..7)
	 * RequiredPhases must be 0 for input of informationtype = 0
   * RequiredPhases must be 0 for input of informationtype = 8
	 * RequiredPhases must be 0 for input of informationtype = -1
	 */
    public void test_foundFilters_phases() {
        alladaptors = new Properties();
        allfilters = new Properties();
        allmodifiers = new Properties();
        jumfitotest.registerFiltersAndAdaptors(alladaptors, allfilters, allmodifiers);
        for (Enumeration e = allfilters.keys(); e.hasMoreElements(); ) {
            MailFilter testfilter = (MailFilter) allfilters.get((String) e.nextElement());
            for (int informationtype = 1; informationtype <= 7; informationtype++) {
                int phases = testfilter.getRequiredPhases(informationtype);
                Assert.assertTrue(((phases >= 0) && (phases <= 15)));
            }
            Assert.assertTrue(testfilter.getRequiredPhases(0) == 0);
            Assert.assertTrue(testfilter.getRequiredPhases(8) == 0);
            Assert.assertTrue(testfilter.getRequiredPhases(-1) == 0);
        }
    }

    /** Test that checks if the found  adaptors getRequiredtriggers interface is functioning within constraints
	 * Requiredtriggers must be in the range of 0..8191 all inputs of informationtype (1..7) and phase (1..15)
	 * Requiredtriggers must be 0 for inputs of informationtype = 0  and phase (1..15)
   * Requiredtriggers must be 0 for inputs of informationtype (1..7) and phase = 0
	 * Requiredtriggers must be 0 for inputs of informationtype = 8  and phase (1..15)
   * Requiredtriggers must be 0 for inputs of informationtype (1..7) and phase = 16
   * Requiredtriggers must be 0 for inputs of informationtype = 22 and phase = 333
   * Requiredtriggers must be 0 for inputs of informationtype = -1 and phase = -1
	 */
    public void test_foundFilters_triggers() {
        alladaptors = new Properties();
        allfilters = new Properties();
        allmodifiers = new Properties();
        jumfitotest.registerFiltersAndAdaptors(alladaptors, allfilters, allmodifiers);
        for (Enumeration e = allfilters.keys(); e.hasMoreElements(); ) {
            MailFilter testfilter = (MailFilter) allfilters.get((String) e.nextElement());
            for (int informationtype = 1; informationtype <= 7; informationtype++) {
                for (int phase = 1; phase <= 15; phase++) {
                    int triggers = testfilter.getRequiredtriggers(informationtype, phase);
                    Assert.assertTrue(((triggers >= 0) && (triggers <= 8191)));
                }
            }
            for (int phase = 1; phase < 15; phase++) {
                Assert.assertTrue(testfilter.getRequiredtriggers(0, phase) == 0);
            }
            for (int informationtype = 1; informationtype <= 7; informationtype++) {
                Assert.assertTrue(testfilter.getRequiredtriggers(informationtype, 0) == 0);
            }
            for (int phase = 1; phase <= 15; phase++) {
                Assert.assertTrue(testfilter.getRequiredtriggers(8, phase) == 0);
            }
            for (int informationtype = 1; informationtype <= 7; informationtype++) {
                Assert.assertTrue(testfilter.getRequiredtriggers(informationtype, 16) == 0);
            }
            Assert.assertTrue(testfilter.getRequiredtriggers(22, 333) == 0);
            Assert.assertTrue(testfilter.getRequiredtriggers(-1, -1) == 0);
        }
    }

    /** Test that checks if the found  adaptors getIdentification interface is functioning within constraints
	 * Adaptor must return a non null identification string
	 * Adaptor must return a non empty identification string
	 */
    public void test_foundAdaptors_identification() {
        alladaptors = new Properties();
        allfilters = new Properties();
        allmodifiers = new Properties();
        jumfitotest.registerFiltersAndAdaptors(alladaptors, allfilters, allmodifiers);
        for (Enumeration e = alladaptors.keys(); e.hasMoreElements(); ) {
            MailAdaptor testadaptor = (MailAdaptor) alladaptors.get((String) e.nextElement());
            Assert.assertTrue(!testadaptor.getIdentification().equals(null));
            Assert.assertTrue(!testadaptor.getIdentification().equals(""));
        }
    }

    /** Test that checks if the found  adaptors SupportedInformationTypes interface is functioning within constraints
	 * SupportedInformationTypes must be in the range of 0..7
	 */
    public void test_foundAdaptors_informationtypes() {
        alladaptors = new Properties();
        allfilters = new Properties();
        allmodifiers = new Properties();
        jumfitotest.registerFiltersAndAdaptors(alladaptors, allfilters, allmodifiers);
        for (Enumeration e = alladaptors.keys(); e.hasMoreElements(); ) {
            MailAdaptor testadaptor = (MailAdaptor) alladaptors.get((String) e.nextElement());
            int supportedtypes = testadaptor.getSupportedInformationTypes();
            Assert.assertTrue(((supportedtypes >= 0) && (supportedtypes <= 7)));
        }
    }

    /** Test that checks if the found  adaptors getSupportedPhases interface is functioning within constraints
	 * SupportedPhases must be in the range of 0..15 for all inputs of informationtype (1..7)
	 * SupportedPhases must be 0 for input of informationtype = 0
   * SupportedPhases must be 0 for input of informationtype = 8
	 * SupportedPhases must be 0 for input of informationtype = -1
	 */
    public void test_foundAdaptors_phases() {
        alladaptors = new Properties();
        allfilters = new Properties();
        allmodifiers = new Properties();
        jumfitotest.registerFiltersAndAdaptors(alladaptors, allfilters, allmodifiers);
        for (Enumeration e = alladaptors.keys(); e.hasMoreElements(); ) {
            MailAdaptor testadaptor = (MailAdaptor) alladaptors.get((String) e.nextElement());
            for (int informationtype = 1; informationtype <= 7; informationtype++) {
                int phases = testadaptor.getSupportedPhases(informationtype);
                Assert.assertTrue(((phases >= 0) && (phases <= 15)));
            }
            Assert.assertTrue(testadaptor.getSupportedPhases(0) == 0);
            Assert.assertTrue(testadaptor.getSupportedPhases(8) == 0);
            Assert.assertTrue(testadaptor.getSupportedPhases(-1) == 0);
        }
    }

    /** Test that checks if the found  adaptors getSupportedtriggers interface is functioning within constraints
	 * Supportedtriggers must be in the range of 0..8191 all inputs of informationtype (1..7) and phase (1..15)
	 * Supportedtriggers must be 0 for inputs of informationtype = 0  and phase (1..15)
   * Supportedtriggers must be 0 for inputs of informationtype (1..7) and phase = 0
	 * Supportedtriggers must be 0 for inputs of informationtype = 8  and phase (1..15)
   * Supportedtriggers must be 0 for inputs of informationtype (1..7) and phase = 16
   * Supportedtriggers must be 0 for inputs of informationtype = 22 and phase = 333
   * Supportedtriggers must be 0 for inputs of informationtype = -1 and phase = -1
	 */
    public void test_foundAdaptors_triggers() {
        alladaptors = new Properties();
        allfilters = new Properties();
        allmodifiers = new Properties();
        jumfitotest.registerFiltersAndAdaptors(alladaptors, allfilters, allmodifiers);
        for (Enumeration e = alladaptors.keys(); e.hasMoreElements(); ) {
            MailAdaptor testadaptor = (MailAdaptor) alladaptors.get((String) e.nextElement());
            for (int informationtype = 1; informationtype <= 7; informationtype++) {
                for (int phase = 1; phase <= 15; phase++) {
                    int triggers = testadaptor.getSupportedtriggers(informationtype, phase);
                    Assert.assertTrue(((triggers >= 0) && (triggers <= 8191)));
                }
            }
            for (int phase = 1; phase <= 15; phase++) {
                Assert.assertTrue(testadaptor.getSupportedtriggers(0, phase) == 0);
            }
            for (int informationtype = 1; informationtype <= 7; informationtype++) {
                Assert.assertTrue(testadaptor.getSupportedtriggers(informationtype, 0) == 0);
            }
            for (int phase = 1; phase <= 15; phase++) {
                Assert.assertTrue(testadaptor.getSupportedtriggers(8, phase) == 0);
            }
            for (int informationtype = 1; informationtype <= 7; informationtype++) {
                Assert.assertTrue(testadaptor.getSupportedtriggers(informationtype, 16) == 0);
            }
            Assert.assertTrue(testadaptor.getSupportedtriggers(22, 333) == 0);
            Assert.assertTrue(testadaptor.getSupportedtriggers(-1, -1) == 0);
        }
    }
}
