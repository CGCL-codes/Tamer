package org.tigris.subversion.svnant.commands;

import java.io.File;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNKeywords;

/**
 * set keywords substitution list
 * @author C�dric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class Keywordsset extends Keywords {

    public void execute() throws SvnAntException {
        super.execute();
        if (file != null) {
            try {
                svnClient.setKeywords(file, keywords, false);
            } catch (SVNClientException e) {
                throw new SvnAntException("Can't set keywords on file " + file.toString(), e);
            }
        } else if (dir != null) {
            try {
                svnClient.setKeywords(dir, keywords, recurse);
            } catch (SVNClientException e) {
                throw new SvnAntException("Can't set keywords on directory " + dir.toString(), e);
            }
        } else if (filesets.size() > 0) {
            for (int i = 0; i < filesets.size(); i++) {
                FileSet fs = (FileSet) filesets.elementAt(i);
                keywordsSet(fs, keywords);
            }
        }
    }

    /**
     * set keywords on a fileset (both dirs and files)
     * @param svnClient
     * @param fs
     * @throws SvnAntException
     */
    private void keywordsSet(FileSet fs, SVNKeywords theKeywords) throws SvnAntException {
        DirectoryScanner ds = fs.getDirectoryScanner(getProject());
        File baseDir = fs.getDir(getProject());
        String[] files = ds.getIncludedFiles();
        for (int i = 0; i < files.length; i++) {
            File aFile = new File(baseDir, files[i]);
            try {
                svnClient.setKeywords(aFile, theKeywords, false);
            } catch (SVNClientException e) {
                throw new SvnAntException("Can't set keywords on file " + aFile.toString(), e);
            }
        }
    }
}
