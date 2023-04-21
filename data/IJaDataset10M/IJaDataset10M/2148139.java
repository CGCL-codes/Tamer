package net.community.chest.svnkit;

import java.io.File;
import net.community.chest.io.filter.AbstractRootFolderFilesFilter;

/**
 * <P>Copyright 2009 as per GPLv2</P>
 *
 * <P>Filters out all the ".svn" sub-folders and any files under them</P>
 * 
 * @author Lyor G.
 * @since Aug 4, 2009 11:27:35 AM
 */
public class SVNFoldersFilter extends AbstractRootFolderFilesFilter {

    /**
	 * Default sub-folder name used by SVN on local file system
	 */
    public static final String SVN_SUBFOLDER_NAME = ".svn";

    public SVNFoldersFilter() {
        super(false, SVN_SUBFOLDER_NAME);
        setDescription(SVN_SUBFOLDER_NAME);
    }

    public static final boolean isSVNParentFolder(final File f) {
        final String n = (null == f) ? null : f.getName();
        if ((f != null) && f.isDirectory() && SVN_SUBFOLDER_NAME.equalsIgnoreCase(n)) return true;
        return false;
    }

    public static final SVNFoldersFilter DEFAULT = new SVNFoldersFilter();
}
