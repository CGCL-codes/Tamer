package uk.icat3.acctests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import uk.icat3.acctests.functional.ICAT_F_1;
import uk.icat3.acctests.functional.ICAT_F_2;
import uk.icat3.acctests.functional.ICAT_F_3;
import uk.icat3.acctests.functional.ICAT_F_4;
import uk.icat3.acctests.functional.ICAT_F_5;
import uk.icat3.acctests.functional.ICAT_F_6;
import uk.icat3.acctests.performance.ICAT_P_1;
import uk.icat3.acctests.performance.ICAT_P_3;
import uk.icat3.acctests.security.ICAT_S_1;
import uk.icat3.acctests.security.ICAT_S_2;
import uk.icat3.acctests.security.ICAT_S_3;
import uk.icat3.acctests.security.ICAT_S_4;

/**
 *
 * @author df01
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ICAT_F_1.class, ICAT_F_2.class, ICAT_F_3.class, ICAT_F_5.class, ICAT_F_6.class, ICAT_P_1.class, ICAT_P_3.class, ICAT_S_1.class, ICAT_S_2.class, ICAT_S_3.class, ICAT_S_4.class })
public class TestAll {

    public static Test suite() {
        return new JUnit4TestAdapter(TestAll.class);
    }
}
