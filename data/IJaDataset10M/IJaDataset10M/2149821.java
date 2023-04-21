package org.dctmvfs.vfs.provider.dctm;

import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.GenericFileName;

public class DctmFileName extends GenericFileName {

    public DctmFileName(GenericFileName genFileName, String hostName) {
        super(genFileName.getScheme(), hostName, genFileName.getPort(), genFileName.getDefaultPort(), genFileName.getUserName(), genFileName.getPassword(), genFileName.getPath(), genFileName.getType());
    }

    public DctmFileName(DctmFileName genFileName, String absPath, FileType type, String hostName) {
        super(genFileName.getScheme(), hostName, genFileName.getPort(), genFileName.getDefaultPort(), genFileName.getUserName(), genFileName.getPassword(), absPath, type);
    }

    /**
	 * Returns a representation of the name, without the password.
	 */
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        appendRootUri(buffer, false);
        buffer.append(getPath());
        String pwdMask = ":*****";
        int pwdMaskStart = buffer.indexOf(":*****");
        if (pwdMaskStart > -1) {
            buffer.replace(pwdMaskStart, pwdMaskStart + pwdMask.length(), "");
        }
        return buffer.toString();
    }
}
