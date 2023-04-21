package org.openi.project;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openi.util.FileItem;
import org.openi.util.Folder;
import org.openi.util.NameValue;
import org.openi.util.NameValueType;

/**
 * @author plucas
 *
 * TODO : refactor
 */
public class DirectoryLister {

    private static Logger logger = Logger.getLogger(DirectoryLister.class);

    /**
     * Makes list of all folders starting from 'directory'
     *
     * @param directory String
     * @return List
     */
    public static List findProjectDirectories(String directory) {
        return findProjectDirectories(directory, null);
    }

    /**
     * Makes list of all folders starting from 'directory'
     *
     * @param directory String
     * @return List
     */
    public static List findProjectDirectories(String directory, List modules) {
        List dirlist = new LinkedList();
        try {
            int depth = 0;
            if (modules != null) {
                Object[] dirs = modules.toArray();
                for (int i = 0; i < dirs.length; i++) {
                    String path = directory + "/" + (String) dirs[i];
                    recurse(depth, dirlist, new File(path));
                }
            } else {
                recurse(depth, dirlist, new File(directory));
            }
        } catch (IOException e) {
            logger.error(e);
        }
        return dirlist;
    }

    /**
     * Recurses folder. This method doesn't include files.
     *
     * @param depth int
     * @param dirs List
     * @param parent File
     * @throws IOException
     */
    private static void recurse(int depth, List dirs, File parent) throws IOException {
        ++depth;
        if (parent.isDirectory()) {
            dirs.add(new NameValue(prependName(depth, "-") + parent.getName(), parent.getCanonicalPath()));
            File[] children = parent.listFiles();
            java.util.Arrays.sort(children);
            for (int i = 0; i < children.length; i++) {
                if (children[i].isDirectory()) recurse(depth, dirs, children[i]);
            }
            for (int i = 0; i < children.length; i++) {
                if (!children[i].isDirectory()) recurse(depth, dirs, children[i]);
            }
        }
    }

    /**
     * Builds All available folders and files List avilable on module list
     * as Folder and FileItem object respectively
     *
     * @param directory String
     * @param modules List
     * @param includeEmptyFolder boolean
     * @return Folder
     */
    public static Folder buildFolderList(String directory, List modules, boolean includeEmptyFolder) {
        Folder root = new Folder();
        root.setDisplayName("root");
        File dir = new File(directory);
        List dirs = new ArrayList();
        try {
            String rootPath = dir.getCanonicalPath();
            Object[] moduleList = modules.toArray();
            for (int i = 0; i < moduleList.length; i++) {
                String path = rootPath + "/" + (String) moduleList[i];
                dir = new File(path);
                if (dir.exists()) {
                    recurse(rootPath, dirs, dir, includeEmptyFolder);
                }
            }
            root.setChildren(dirs);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return root;
    }

    private static void recurse(String rootPath, List dirs, File dir, boolean includeEmptyFolder) {
        File[] files = dir.listFiles();
        String path = null;
        try {
            path = dir.getCanonicalPath().substring(rootPath.length());
        } catch (IOException ex) {
            logger.error(ex);
        }
        if (dir.isFile()) {
            FileItem file = new FileItem();
            file.setPath(path);
            DateFormat format = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
            file.setDateModified(format.format(new Date(dir.lastModified())));
            file.setSize(dir.length());
            file.setDisplayName(dir.getName());
            dirs.add(file);
        } else if (dir.isDirectory() && (files.length == 0)) {
            if (includeEmptyFolder) {
                Folder folder = new Folder();
                folder.setPath(path);
                folder.setDisplayName(dir.getName());
                dirs.add(folder);
            }
        } else {
            Folder folder = new Folder();
            folder.setPath(path);
            folder.setDisplayName(dir.getName());
            dirs.add(folder);
            List childs = new ArrayList();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) recurse(rootPath, childs, files[i], includeEmptyFolder);
            }
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory()) recurse(rootPath, childs, files[i], includeEmptyFolder);
            }
            folder.setChildren(childs);
        }
    }

    /**
     * This method creates list of public and private modules defined in
     * project configuration. Also checks user's permission.
     * File/directory path is relative to project.
     *
     * @param directory String
     * @param includeFiles boolean
     * @param prepend String
     * @param includeEmptyFolder boolean
     * @return List
     */
    public static List findProjectDirectories(String directory, List modules, boolean includeFiles, String prepend, boolean includeEmptyFolder) {
        File dir = new File(directory);
        List dirs = new LinkedList();
        int depth = 1;
        String rootPath = null;
        try {
            rootPath = dir.getCanonicalPath();
            Object[] moduleList = modules.toArray();
            for (int i = 0; i < moduleList.length; i++) {
                String path = rootPath + "/" + (String) moduleList[i];
                dir = new File(path);
                if (dir.exists()) {
                    recurse(rootPath, depth, dirs, dir, prepend, includeEmptyFolder);
                }
            }
        } catch (IOException ex) {
            logger.error(ex);
        }
        return dirs;
    }

    private static void recurse(String rootPath, int depth, List dirs, File dir, String prepend, boolean includeEmptyFolder) {
        File[] files = dir.listFiles();
        String path = null;
        try {
            path = dir.getCanonicalPath().substring(rootPath.length());
        } catch (IOException ex) {
            logger.error(ex);
        }
        if (dir.isFile()) {
            dirs.add(new NameValueType(dir.getName(), path, "file", prependName(depth, prepend)));
        } else if (dir.isDirectory() && (files.length == 0)) {
            if (includeEmptyFolder) {
                dirs.add(new NameValueType(dir.getName(), path, "folder", prependName(depth, prepend)));
            }
        } else {
            dirs.add(new NameValueType(dir.getName(), path, "folder", prependName(depth, prepend)));
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) recurse(rootPath, depth + 1, dirs, files[i], prepend, includeEmptyFolder);
            }
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory()) recurse(rootPath, depth + 1, dirs, files[i], prepend, includeEmptyFolder);
            }
        }
    }

    /**
     * Returns prepend text.
     *
     *
     * @param depth int
     * @param prepend String
     * @return String
     */
    private static String prependName(int depth, String prepend) {
        String newName = "";
        for (int i = 1; i < depth; i++) {
            newName += prepend;
        }
        return newName;
    }

    /**
     * Builds list of project root's sub directory
     *
     * @param projectroot String
     * @return List
     */
    public static List buildProjectRootSubDirList(String projectroot) {
        List folders = new ArrayList();
        File root = new File(projectroot);
        if (root.exists() && root.isDirectory()) {
            File[] childs = root.listFiles();
            for (int i = 0; i < childs.length; i++) {
                if (childs[i].isDirectory()) {
                    folders.add("/" + childs[i].getName());
                }
            }
        }
        return folders;
    }

    /**
     * builds file path list filtered by extension
     * @param projectDir String
     * @param modules List
     * @param fileext String
     * @return List
     */
    public static List buildFileListByExtension(String projectDir, List modules, String fileext) {
        Folder folder;
        folder = buildFolderList(projectDir, modules, false);
        List allfiles = new ArrayList();
        Folder.buildChildList(folder, null, allfiles);
        Iterator iter = allfiles.iterator();
        List files = new ArrayList();
        while (iter.hasNext()) {
            String path = (String) iter.next();
            if (path != null) {
                path = path.replace('\\', '/');
            }
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (path.toLowerCase().endsWith(fileext.toLowerCase())) {
                files.add(path);
            }
        }
        return files;
    }
}
