package at.co.ait.domain.services;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import at.co.ait.utils.ConfigUtils;
import at.co.ait.web.common.UserPreferences;

public class DirectoryListingService implements Serializable {

    private static final long serialVersionUID = 1L;

    private File basedir = null;

    private Map<Integer, String> visitedFilesAndFolders = new HashMap<Integer, String>();

    private UserPreferences pref;

    public void init() throws MalformedURLException, IOException {
        pref = (UserPreferences) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Resource res = new UrlResource(pref.getBasedirectory());
        setBasedir(res.getFile());
        visitedFilesAndFolders.put(basedir.hashCode(), basedir.getAbsolutePath());
        addFolderContent(basedir);
    }

    public File getBasedir() {
        return basedir;
    }

    public void setBasedir(File basedir) {
        this.basedir = basedir;
    }

    public File getFileByKey(Integer key) {
        return new File(visitedFilesAndFolders.get(key));
    }

    /**
	 * Read all files and folders from a directory.
	 * @param dir AbsolutePath of directory.
	 * @return Collection of Files.
	 */
    public Collection<File> readFilesAndFoldersFromDir(String dir) {
        File[] filesAndFolders = (new File(dir)).listFiles();
        Collection<File> coll = new ArrayList<File>();
        for (File f : filesAndFolders) {
            coll.add(f);
        }
        return coll;
    }

    public void addFolderContent(File folder) {
        File[] filesAndFolders = folder.listFiles();
        if (filesAndFolders == null) {
            throw new RuntimeException("Not a folder or does not exist: " + folder.getAbsolutePath());
        }
        for (File f : filesAndFolders) {
            if (f.isDirectory()) {
                visitedFilesAndFolders.put(f.hashCode(), f.getAbsolutePath());
            }
        }
    }

    /**
	 * A directory tree contains a list of files and folders where each
	 * list's entry contains a map with specific entries used by Dynatree.
	 * @param key 32-bit Integer hashcode of a fileobject which returns the selected directory
	 * @return list of maps which can be serialized into JSON
	 */
    public List<Map<String, Object>> buildJSONMsgFromDir(String key) {
        String dir;
        if (key == null) {
            dir = visitedFilesAndFolders.get(basedir.hashCode());
        } else {
            dir = visitedFilesAndFolders.get(Integer.valueOf(key));
        }
        addFolderContent(new File(dir));
        List<Map<String, Object>> elements = new ArrayList<Map<String, Object>>();
        Collection<File> files = readFilesAndFoldersFromDir(dir);
        ArrayList<File> filelist = new ArrayList<File>(files);
        Collections.sort(filelist);
        for (File file : filelist) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", StringUtils.remove(file.toString(), dir + File.separator));
            if (file.isDirectory()) {
                map.put("isFolder", file.isDirectory());
                map.put("isLazy", true);
                if (file.getName().equals(ConfigUtils.TMP_PROCESSING)) {
                    map.put("unselectable", true);
                    map.put("hideCheckbox", true);
                }
            } else {
                map.put("unselectable", true);
                map.put("hideCheckbox", true);
            }
            map.put("key", String.valueOf(file.hashCode()));
            elements.add(map);
        }
        return elements;
    }
}
