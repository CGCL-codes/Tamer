package com.liferay.portal.upgrade.v5_1_0;

import com.liferay.portal.upgrade.SmartUpgradeSchema;

/**
 * <a href="UpgradeSchema.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class UpgradeSchema extends SmartUpgradeSchema {

    protected void upgradeOnce() throws Exception {
        runSQLTemplate("update-5.0.1-5.1.0.sql", false);
    }
}
