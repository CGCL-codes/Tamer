package net.sourceforge.squirrel_sql.plugins.oracle.tab;

import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.AbstractBasePreparedStatementTabTest;
import org.junit.Before;

public class OptionsTabTest extends AbstractBasePreparedStatementTabTest {

    @Before
    public void setUp() throws Exception {
        classUnderTest = new OptionsTab();
    }
}
