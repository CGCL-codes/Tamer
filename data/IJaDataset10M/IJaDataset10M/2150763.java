package com.google.gwt.dev.resource.impl;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.util.msg.Message1String;
import java.io.File;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * TODO(bruce): write me.
 */
public class DirectoryClassPathEntry extends ClassPathEntry {

    private static class Messages {

        static final Message1String NOT_DESCENDING_INTO_DIR = new Message1String(TreeLogger.SPAM, "Prefix set does not include dir: $0");

        static final Message1String DESCENDING_INTO_DIR = new Message1String(TreeLogger.SPAM, "Descending into dir: $0");

        static final Message1String EXCLUDING_FILE = new Message1String(TreeLogger.DEBUG, "Filter excludes file: $0");

        static final Message1String INCLUDING_FILE = new Message1String(TreeLogger.DEBUG, "Including file: $0");
    }

    private final File dir;

    public DirectoryClassPathEntry(File dir) {
        this.dir = dir;
    }

    @Override
    public Map<AbstractResource, PathPrefix> findApplicableResources(TreeLogger logger, PathPrefixSet pathPrefixSet) {
        Map<AbstractResource, PathPrefix> results = new IdentityHashMap<AbstractResource, PathPrefix>();
        descendToFindResources(logger, pathPrefixSet, results, dir, "");
        return results;
    }

    @Override
    public String getLocation() {
        return dir.getAbsoluteFile().toURI().toString();
    }

    /**
   * @param logger logs progress
   * @param resources the accumulating set of resources (each with the
   *          corresponding pathPrefix) found
   * @param dir the file or directory to consider
   * @param dirPath the abstract path name associated with 'parent', which
   *          explicitly does not include the classpath entry in its path
   */
    private void descendToFindResources(TreeLogger logger, PathPrefixSet pathPrefixSet, Map<AbstractResource, PathPrefix> resources, File dir, String dirPath) {
        assert (dir.isDirectory());
        File[] children = dir.listFiles();
        for (File child : children) {
            String childPath = dirPath + child.getName();
            if (child.isDirectory()) {
                String childDirPath = childPath + "/";
                if (pathPrefixSet.includesDirectory(childDirPath)) {
                    Messages.DESCENDING_INTO_DIR.log(logger, child.getAbsolutePath(), null);
                    descendToFindResources(logger, pathPrefixSet, resources, child, childDirPath);
                } else {
                    Messages.NOT_DESCENDING_INTO_DIR.log(logger, child.getAbsolutePath(), null);
                }
            } else {
                PathPrefix prefix = null;
                if ((prefix = pathPrefixSet.includesResource(childPath)) != null) {
                    Messages.INCLUDING_FILE.log(logger, childPath, null);
                    FileResource r = new FileResource(this, childPath, child);
                    resources.put(r, prefix);
                } else {
                    Messages.EXCLUDING_FILE.log(logger, childPath, null);
                }
            }
        }
    }
}
