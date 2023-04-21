package com.bluemarsh.jswat.core.path;

import com.bluemarsh.jswat.core.PlatformProvider;
import com.bluemarsh.jswat.core.PlatformService;
import com.bluemarsh.jswat.core.connect.JvmConnection;
import com.bluemarsh.jswat.core.session.Session;
import com.bluemarsh.jswat.core.util.Names;
import com.bluemarsh.jswat.core.util.Strings;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.PathSearchingVirtualMachine;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * The default implementation of the PathManager interface. Stores the
 * path values in the session properties.
 *
 * @author Nathan Fiedler
 */
public class DefaultPathManager extends AbstractPathManager {

    /** The classpath setting, if specified by the user (read-only). */
    private List<String> classPath;

    /** The sourcepath setting, if specified by the user (read-only). */
    private List<String> sourcePath;

    /**
     * Creates a new instance of DefaultPathManager.
     */
    public DefaultPathManager() {
    }

    @Override
    public boolean canSetSourcePath() {
        return true;
    }

    @Override
    public boolean canSetClassPath() {
        return true;
    }

    @Override
    public PathEntry findByteCode(ReferenceType clazz) {
        String filename = clazz.name();
        filename = filename.replace('.', File.separatorChar);
        filename += ".class";
        return findFile(filename, false);
    }

    @Override
    public PathEntry findFile(String filename) {
        return findFile(filename, false);
    }

    /**
     * Find the named file in the sourcepath or classpath. If at first the
     * file cannot be found, any leading path will be trimmed and the search
     * will commense once more, if and only if the fuzzy parameter is true.
     *
     * @param  filename  name of file to locate.
     * @param  fuzzy     apply a fuzzy search.
     * @return  file object, or null if not found.
     */
    protected PathEntry findFile(String filename, boolean fuzzy) {
        PathEntry pe = null;
        if (sourcePath != null) {
            for (String path : sourcePath) {
                pe = findResource(path, filename);
                if (pe != null) {
                    break;
                }
            }
        }
        if (pe == null && classPath != null) {
            for (String path : classPath) {
                pe = findResource(path, filename);
                if (pe != null) {
                    break;
                }
            }
        }
        if (pe == null && fuzzy) {
            int last = filename.lastIndexOf(File.separatorChar);
            if (last > 0) {
                pe = findFile(filename.substring(last + 1), false);
            }
        }
        return pe;
    }

    /**
     * Given an entry in a collection of paths (e.g. class path or
     * source path) and a file path and name, check if the specified
     * file exists within that path.
     *
     * @param  path  element of class or source path.
     * @param  file  the path and file name to find.
     * @return  new path entry if found, null otherwise.
     */
    private PathEntry findResource(String path, String file) {
        int idx = path.indexOf('!');
        String prefix = null;
        String zippath = null;
        if (idx > 0) {
            prefix = path.substring(idx + 1);
            zippath = path.substring(0, idx);
        } else {
            zippath = path;
        }
        File f = new File(zippath);
        if (f.isFile()) {
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(zippath);
            } catch (IOException ioe) {
                return null;
            }
            String queryName = prefix != null ? prefix + File.separator + file : file;
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String entryName = zipEntry.getName();
                entryName = new File(entryName).getPath();
                if (entryName.equals(queryName)) {
                    return new ZipPathEntry(zipFile, zipEntry);
                }
            }
        } else {
            f = new File(path, file);
            if (f.exists()) {
                return new FilePathEntry(f);
            }
        }
        return null;
    }

    @Override
    public PathEntry findSource(String name) {
        String filename = Names.classnameToFilename(name);
        PathEntry pe = findFile(filename, true);
        if (pe == null) {
            try {
                filename = name.replace('.', File.separatorChar) + ".class";
                pe = findFile(filename, false);
                if (pe != null) {
                    PlatformService platform = PlatformProvider.getPlatformService();
                    InputStream is = pe.getInputStream();
                    String srcname = platform.getSourceName(is, name);
                    if (srcname != null) {
                        filename = Names.classnameToFilename(name, srcname);
                        pe = findFile(filename, true);
                    }
                }
            } catch (IOException ioe) {
            }
        }
        return pe;
    }

    @Override
    public PathEntry findSource(Location location) {
        try {
            String source = location.sourceName();
            String classname = location.declaringType().name();
            String filename = Names.classnameToFilename(classname, source);
            PathEntry pe = findFile(filename, true);
            if (pe == null) {
                VirtualMachine vm = location.virtualMachine();
                if (vm.canGetSourceDebugExtension()) {
                    String path = location.sourcePath(null);
                    pe = findFile(path, true);
                }
            }
            if (pe != null) {
                return pe;
            }
        } catch (AbsentInformationException aie) {
        }
        return findSource(location.declaringType());
    }

    @Override
    public PathEntry findSource(ReferenceType clazz) {
        String classname = clazz.name();
        String filename;
        try {
            String srcname = clazz.sourceName();
            filename = Names.classnameToFilename(classname, srcname);
        } catch (AbsentInformationException aie) {
            filename = Names.classnameToFilename(classname);
        }
        PathEntry pe = findFile(filename, true);
        if (pe == null) {
            VirtualMachine vm = clazz.virtualMachine();
            if (vm.canGetSourceDebugExtension()) {
                try {
                    List<String> paths = clazz.sourcePaths(null);
                    Iterator<String> iter = paths.iterator();
                    while (iter.hasNext()) {
                        String path = iter.next();
                        pe = findFile(path, true);
                        if (pe != null) {
                            break;
                        }
                    }
                } catch (AbsentInformationException aie) {
                }
            }
        }
        return pe;
    }

    @Override
    public List<String> getClassPath() {
        Session session = PathProvider.getSession(this);
        String ignore = session.getProperty(PROP_IGNORE_DEBUGGEE);
        if (ignore == null || ignore.length() == 0) {
            JvmConnection vmConnection = session.getConnection();
            if (vmConnection != null) {
                VirtualMachine vm = vmConnection.getVM();
                if (vm instanceof PathSearchingVirtualMachine) {
                    return ((PathSearchingVirtualMachine) vm).classPath();
                }
            }
        }
        return classPath == null ? classPath : new ArrayList<String>(classPath);
    }

    @Override
    public List<String> getSourcePath() {
        return sourcePath == null ? sourcePath : new ArrayList<String>(sourcePath);
    }

    @Override
    protected List<String> getUserClassPath() {
        return classPath == null ? classPath : new ArrayList<String>(classPath);
    }

    @Override
    protected void loadPaths(Session session) {
        String cp = session.getProperty(PROP_CLASSPATH);
        if (cp != null) {
            setClassPath(Strings.stringToList(cp, File.pathSeparator));
        }
        String sp = session.getProperty(PROP_SOURCEPATH);
        if (sp != null) {
            setSourcePath(Strings.stringToList(sp, File.pathSeparator));
        }
    }

    @Override
    protected void savePaths(Session session) {
        if (classPath == null || classPath.isEmpty()) {
            session.setProperty(PROP_CLASSPATH, null);
        } else {
            String cp = Strings.listToString(classPath, File.pathSeparator);
            session.setProperty(PROP_CLASSPATH, cp);
        }
        if (sourcePath == null || sourcePath.isEmpty()) {
            session.setProperty(PROP_SOURCEPATH, null);
        } else {
            String sp = Strings.listToString(sourcePath, File.pathSeparator);
            session.setProperty(PROP_SOURCEPATH, sp);
        }
    }

    @Override
    public void setClassPath(List<String> roots) {
        List<String> oldPath = classPath;
        if (roots == null || roots.isEmpty()) {
            classPath = null;
        } else {
            classPath = Collections.unmodifiableList(roots);
        }
        firePropertyChange(PROP_CLASSPATH, oldPath, classPath);
    }

    @Override
    public void setSourcePath(List<String> roots) {
        List<String> oldPath = sourcePath;
        if (roots == null || roots.isEmpty()) {
            sourcePath = null;
        } else {
            List<String> temp = new ArrayList<String>(roots);
            for (int ii = 0; ii < temp.size(); ii++) {
                String root = temp.get(ii);
                if (new File(root).isFile()) {
                    ZipFile zipFile = null;
                    try {
                        zipFile = new ZipFile(root);
                    } catch (IOException ioe) {
                        continue;
                    }
                    Set<String> ruuts = new HashSet<String>();
                    Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry zipEntry = entries.nextElement();
                        String entryName = zipEntry.getName();
                        int si = entryName.indexOf('/');
                        if (si > 0) {
                            ruuts.add(entryName.substring(0, si));
                        }
                    }
                    if (ruuts.size() == 1) {
                        String ruut = ruuts.iterator().next();
                        if (ruut.equals("src")) {
                            temp.set(ii, root + '!' + ruut);
                        }
                    }
                }
            }
            sourcePath = Collections.unmodifiableList(temp);
        }
        firePropertyChange(PROP_SOURCEPATH, oldPath, sourcePath);
    }

    /**
     * A FilePathEntry is a concrete implementation of PathEntry in
     * which the source is backed by a <code>java.io.File</code> instance.
     *
     * @author  Nathan Fiedler
     */
    private class FilePathEntry implements PathEntry {

        /** The file that contains the source object. */
        private final File fileSource;

        /**
         * Constructs a FilePathEntry instance for the given File.
         *
         * @param  src  file path entry.
         */
        FilePathEntry(File src) {
            if (src == null) {
                throw new NullPointerException("src must be non-null");
            }
            fileSource = src;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof FilePathEntry) {
                FilePathEntry ofs = (FilePathEntry) o;
                return ofs.getSource().equals(fileSource);
            }
            return false;
        }

        @Override
        public String getDisplayName() {
            return fileSource.getName();
        }

        public File getSource() {
            return fileSource;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream(fileSource);
        }

        @Override
        public String getName() {
            return fileSource.getName();
        }

        @Override
        public String getPath() {
            try {
                return fileSource.getCanonicalPath();
            } catch (IOException ioe) {
                return null;
            }
        }

        @Override
        public URL getURL() {
            try {
                return fileSource.toURI().toURL();
            } catch (MalformedURLException ex) {
                return null;
            }
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + (this.fileSource != null ? this.fileSource.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean isSame(Object o) {
            if (o instanceof File) {
                File f = (File) o;
                return f.equals(fileSource);
            }
            return false;
        }
    }

    /**
     * A PathEntry based on an entry in a zip file.
     *
     * @author  Nathan Fiedler
     */
    private class ZipPathEntry implements PathEntry {

        /** Zip file. */
        private ZipFile zipFile;

        /** Entry in zip file. */
        private ZipEntry zipEntry;

        /** The last part of the zip entry name. */
        private final String entryName;

        /** Used for getting just the name of the zip entry. */
        private final File entryAsFile;

        /**
         * Constructs a ZipPathEntry from the given file and entry.
         *
         * @param  file   zip file.
         * @param  entry  zip file entry.
         */
        ZipPathEntry(ZipFile file, ZipEntry entry) {
            if (file == null || entry == null) {
                throw new IllegalArgumentException("arguments must be non-null");
            }
            zipFile = file;
            zipEntry = entry;
            entryName = new File(zipEntry.getName()).getName();
            entryAsFile = new File(file.getName(), entry.getName());
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ZipPathEntry) {
                ZipPathEntry zpe = (ZipPathEntry) o;
                return zpe.getEntryFile().equals(entryAsFile);
            }
            return false;
        }

        @Override
        public String getDisplayName() {
            return entryName;
        }

        public File getEntryFile() {
            return entryAsFile;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            try {
                return zipFile.getInputStream(zipEntry);
            } catch (IllegalStateException ise) {
                zipFile = new ZipFile(zipFile.getName());
                zipEntry = zipFile.getEntry(zipEntry.getName());
                return zipFile.getInputStream(zipEntry);
            }
        }

        @Override
        public String getName() {
            return entryName;
        }

        @Override
        public String getPath() {
            return null;
        }

        @Override
        public URL getURL() {
            try {
                return entryAsFile.toURI().toURL();
            } catch (MalformedURLException ex) {
                return null;
            }
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 41 * hash + (this.entryAsFile != null ? this.entryAsFile.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean isSame(Object o) {
            if (o instanceof ZipEntry) {
                ZipEntry ze = (ZipEntry) o;
                return ze.equals(zipEntry);
            }
            return false;
        }
    }
}
