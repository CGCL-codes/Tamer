package org.impalaframework.osgi.test;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.Assert;

/**
 * A {@link FileFilter} implementation designed to make it easy to select bundles for inclusion and exclusion from
 * tests.
 * @author Phil Zoio
 */
public class ConfigurableFileFilter extends BundleFileFilter {

    private Map<String, List<String>> includes;

    private Map<String, List<String>> excludes;

    public ConfigurableFileFilter(Map<String, List<String>> includes, Map<String, List<String>> excludes) {
        super();
        this.includes = includes;
        this.excludes = excludes;
    }

    /**
     * Takes includes and excludes in the format 'folder1:file1,file2; folder2:file3,file4'.
     * An include is matched if the parent folder is in the folder part of the String (to the left of the colon)
     * and one of the comma separated values is present to the right of the semi-colon.
     * An exclude is matched using the same logic.
     * A {@link File} is matched if it matches against includes but not against excludeds.
     * A special case is the following: 'folder:*' matches all items in that folder.
     */
    public ConfigurableFileFilter(String includes, String excludes) {
        super();
        this.includes = buildMap(includes);
        this.excludes = buildMap(excludes);
    }

    private static Map<String, List<String>> buildMap(String includes) {
        Map<String, List<String>> includesMap = new HashMap<String, List<String>>();
        if (includes != null) {
            String[] semiColonSplit = includes.split(";");
            for (String item : semiColonSplit) {
                item = item.trim();
                int colonIndex = item.indexOf(':');
                Assert.isTrue(colonIndex >= 0, "Invalid format for argument " + includes + ". Format should be something like 'folder1:file1,file2; folder2:file3,file4'");
                String folder = item.substring(0, colonIndex).trim();
                String afterColon = item.substring(colonIndex + 1);
                String[] fileSplit = afterColon.split(",");
                List<String> fileList = new ArrayList<String>();
                includesMap.put(folder, fileList);
                for (String file : fileSplit) {
                    fileList.add(file.trim());
                }
            }
        }
        return includesMap;
    }

    @Override
    public boolean accept(File file) {
        boolean accept = super.accept(file);
        if (!accept) return false;
        String parentFolder = file.getParentFile().getName();
        String name = file.getName();
        List<String> list = includes.get(parentFolder);
        if (list != null) {
            if (list.size() == 1 && "*".equalsIgnoreCase(list.get(0))) {
                return true;
            }
        }
        boolean included = isIncluded(parentFolder, name);
        if (!included) {
            return false;
        }
        boolean excluded = isExcluded(parentFolder, name);
        if (excluded) {
            return false;
        }
        return accept;
    }

    private boolean isIncluded(String parentFolder, String name) {
        return isPresentInMap(includes, parentFolder, name);
    }

    private boolean isExcluded(String parentFolder, String name) {
        return isPresentInMap(excludes, parentFolder, name);
    }

    private boolean isPresentInMap(Map<String, List<String>> map, String parentFolder, String name) {
        List<String> list = map.get(parentFolder);
        if (list == null) return false;
        boolean present = false;
        for (String item : list) {
            if (name.contains(item)) {
                present = true;
                break;
            }
        }
        return present;
    }
}

class BundleFileFilter implements FileFilter {

    public boolean accept(File pathname) {
        if (pathname.getName().contains("sources")) return false;
        if (!pathname.getName().endsWith(".jar")) return false;
        return true;
    }
}
