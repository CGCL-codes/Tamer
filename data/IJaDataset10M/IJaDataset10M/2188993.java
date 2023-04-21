package org.apache.commons.vfs.provider.sftp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jcraft.jsch.UserInfo;

/**
 * Helper class to trust a new host
 */
public class TrustEveryoneUserInfo implements UserInfo {

    private Log log = LogFactory.getLog(TrustEveryoneUserInfo.class);

    public String getPassphrase() {
        return null;
    }

    public String getPassword() {
        return null;
    }

    public boolean promptPassword(String s) {
        log.info(s + " - Answer: False");
        return false;
    }

    public boolean promptPassphrase(String s) {
        log.info(s + " - Answer: False");
        return false;
    }

    public boolean promptYesNo(String s) {
        log.debug(s + " - Answer: Yes");
        return true;
    }

    public void showMessage(String s) {
        log.debug(s);
    }
}
