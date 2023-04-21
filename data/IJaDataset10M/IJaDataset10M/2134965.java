package org.svenk.svnhook.hook.inspector;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.svenk.svnhook.Svnhook;
import org.tmatesoft.svn.core.SVNNodeKind;

public abstract class AbstractInspector implements IInspector {

    protected Logger logger = Logger.getLogger(Svnhook.class);

    protected String errorMessage;

    protected List<String> fileExtensions = new ArrayList<String>();

    public boolean isUsable(String path, SVNNodeKind nodeKind, char status) {
        int pos = path.lastIndexOf(".");
        if (pos > -1) {
            String extension = path.substring(pos + 1).trim().toUpperCase();
            return fileExtensions.contains(extension);
        }
        return false;
    }

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
	 * @param csv Komma getrennte Liste mit zu verarbeitenden Dateiendungen
	 */
    public void setFileExtensions(String csv) {
        StringTokenizer tok = new StringTokenizer(csv, ",");
        String extension;
        while (tok.hasMoreTokens()) {
            extension = tok.nextToken().trim();
            if (!extension.equalsIgnoreCase("")) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Dateiendung zum Pruefen : " + extension);
                }
                fileExtensions.add(extension.toUpperCase());
            }
        }
    }
}
