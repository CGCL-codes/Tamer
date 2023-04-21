package net.sourceforge.squirrel_sql.plugins.mysql.tab;

import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.AbstractBaseObjectTabExternalTest;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.AliasNames;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.BaseObjectTab;

public class ShowSlaveStatusTabExternalTest extends AbstractBaseObjectTabExternalTest {

    protected String getSimpleName() {
        return "dbcopydest";
    }

    protected BaseObjectTab getTabToTest() {
        return new ShowSlaveStatusTab();
    }

    protected String getAlias() {
        return AliasNames.MYSQL5_DEST_ALIAS_NAME;
    }
}
