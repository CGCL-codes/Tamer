package com.liferay.portal.upgrade.v4_3_4;

import com.liferay.portal.model.impl.ClassNameImpl;
import com.liferay.portal.upgrade.UpgradeException;
import com.liferay.portal.upgrade.UpgradeProcess;
import com.liferay.portal.upgrade.util.DefaultUpgradeTableImpl;
import com.liferay.portal.upgrade.util.UpgradeTable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="UpgradeClassName.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class UpgradeClassName extends UpgradeProcess {

    public void upgrade() throws UpgradeException {
        _log.info("Upgrading");
        try {
            doUpgrade();
        } catch (Exception e) {
            throw new UpgradeException(e);
        }
    }

    protected void doUpgrade() throws Exception {
        UpgradeTable upgradeTable = new DefaultUpgradeTableImpl(ClassNameImpl.TABLE_NAME, ClassNameImpl.TABLE_COLUMNS);
        upgradeTable.setCreateSQL(ClassNameImpl.TABLE_SQL_CREATE);
        upgradeTable.updateTable();
    }

    private static Log _log = LogFactory.getLog(UpgradeClassName.class);
}
