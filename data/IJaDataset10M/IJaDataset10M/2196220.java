package org.firebirdsql.squirrel.tab;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.AbstractBasePreparedStatementTabExternalTest;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.AliasNames;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.BasePreparedStatementTab;

public class GeneratorDetailsTabExternalTest extends AbstractBasePreparedStatementTabExternalTest {

    protected String getSimpleName() {
        return "testGenerator";
    }

    protected BasePreparedStatementTab getTabToTest() {
        return new GeneratorDetailsTab();
    }

    protected String getAlias() {
        return AliasNames.FIREBIRD_DEST_ALIAS_NAME;
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.AbstractBasePreparedStatementTabExternalTest#getSetupStatements()
	 */
    @Override
    protected List<String> getSetupStatements() {
        ArrayList<String> result = new ArrayList<String>();
        result.add("create generator " + getSimpleName());
        return result;
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.AbstractBasePreparedStatementTabExternalTest#getTeardownStatements()
	 */
    @Override
    protected List<String> getTeardownStatements() {
        ArrayList<String> result = new ArrayList<String>();
        result.add("drop generator " + getSimpleName());
        return result;
    }
}
