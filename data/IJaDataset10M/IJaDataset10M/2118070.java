package org.drftpd.tools.installer;

/**
 * @author djb61
 * @version $Id: LogWindowInterface.java 1926 2009-06-20 17:02:07Z djb61 $
 */
public interface LogWindowInterface {

    public String getUserDir();

    public void setProgress(int pluginsDone);

    public void setProgressMessage(String message);
}
