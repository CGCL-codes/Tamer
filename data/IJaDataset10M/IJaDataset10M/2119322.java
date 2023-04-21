package org.tigris.subversion.svnant.commands;

import java.io.File;
import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnant.SvnAntValidationException;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 * svn Diff.   
 * display the differences between two paths. (Unified format)
 * 
 * @author C�dric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class Diff extends SvnCommand {

    private SVNUrl oldUrl = null;

    private SVNUrl newUrl = null;

    private File oldPath = null;

    private File newPath = null;

    private SVNRevision oldTargetRevision = null;

    private SVNRevision newTargetRevision = null;

    private File outFile = new File("patch");

    private boolean recurse = true;

    public void execute() throws SvnAntException {
        try {
            if (oldUrl != null) svnClient.diff(oldUrl, oldTargetRevision, newUrl, newTargetRevision, outFile, recurse); else svnClient.diff(oldPath, oldTargetRevision, newPath, newTargetRevision, outFile, recurse);
        } catch (SVNClientException e) {
            throw new SvnAntException("Can't get the differences", e);
        }
    }

    /**
     * Ensure we have a consistent and legal set of attributes
     */
    protected void validateAttributes() throws SvnAntValidationException {
        if (oldUrl != null) {
            if ((oldPath != null) || (newPath != null)) throw new SvnAntValidationException("paths cannot be with urls when diffing");
        } else {
            if ((oldUrl != null) || (newUrl != null)) throw new SvnAntValidationException("paths cannot be with urls when diffing");
        }
    }

    /**
	 * @param file
	 */
    public void setNewPath(File file) {
        newPath = file;
    }

    /**
	 * @param revision
	 */
    public void setNewTargetRevision(String revision) {
        this.newTargetRevision = getRevisionFrom(revision);
    }

    /**
	 * @param url
	 */
    public void setNewUrl(SVNUrl url) {
        newUrl = url;
    }

    /**
	 * @param file
	 */
    public void setOldPath(File file) {
        oldPath = file;
    }

    /**
	 * @param revision
	 */
    public void setOldTargetRevision(String revision) {
        this.oldTargetRevision = getRevisionFrom(revision);
    }

    /**
	 * @param url
	 */
    public void setOldUrl(SVNUrl url) {
        oldUrl = url;
    }

    /**
	 * @param file
	 */
    public void setOutFile(File file) {
        outFile = file;
    }

    /**
	 * @param b
	 */
    public void setRecurse(boolean b) {
        recurse = b;
    }
}
