package com.liferay.portal.upgrade;

import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.upgrade.v5_1_2.UpgradeCalendar;
import com.liferay.portal.upgrade.v5_1_2.UpgradeSchema;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="UpgradeProcess_5_1_2.java.html"><b><i>View Source</i></b></a>
 *
 * @author Jorge Ferrer
 *
 * @deprecated
 *
 */
public class UpgradeProcess_5_1_2 extends UpgradeProcess {

    public int getThreshold() {
        return ReleaseInfo.RELEASE_5_1_2_BUILD_NUMBER;
    }

    public void upgrade() throws UpgradeException {
        _log.info("Upgrading");
        upgrade(UpgradeSchema.class);
        upgrade(UpgradeCalendar.class);
    }

    private static Log _log = LogFactory.getLog(UpgradeProcess_5_1_2.class);
}
