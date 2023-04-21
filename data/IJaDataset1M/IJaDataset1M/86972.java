package net.sourceforge.cridmanager;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import net.sourceforge.cridmanager.box.IBox;
import org.apache.log4j.Logger;

/**
 * Ein Verzeichnis in einem Dateisystem. Die Verzeichnisse sind hierarchisch.
 */
public class FsLocation implements ILocation {

    private static Logger logger = Logger.getLogger("net.sourceforge.cridmanager.FsLocation");

    private File dir;

    /**
	 * Erstellt ein <code>FsLocation</code> -Objekt aus einem <code>File</code> -Objekt. Das
	 * File-Objekt muss ein Verzeichnis im Dateisystem darstellen.
	 * 
	 * @param file Das File-Objekt des Verzeichnisses.
	 * @throws IllegalArgumentException wenn das File-Objekt existiert, aber kein Verzeichnis
	 *           darstellt.
	 */
    public FsLocation(File file) {
        if (file == null || (file.exists() && !file.isDirectory())) {
            String filename = (file == null ? "unbekannt" : file.getAbsolutePath());
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException(Messages.getString("FsLocation.DirOnly"));
            logger.error("\nFsLocation(): Fehler beim Einlesen des file= " + filename + ". Das File ist kein oder kein gueltiges Verzeichnis.", illegalArgumentException);
            throw new IllegalArgumentException(Messages.getString("FsLocation.DirOnly") + "\nFile= " + filename);
        }
        dir = file;
    }

    public IFile[] readCridFiles() {
        if (logger.isDebugEnabled()) {
            logger.debug("readCridFiles() - start");
        }
        File[] files = listDirFiles(new CridFilter(false));
        IFile[] result = new IFile[files.length];
        for (int i = 0; i < files.length; i++) if (!files[i].isDirectory()) try {
            result[i] = new CridFsFile(files[i]);
        } catch (IllegalArgumentException e) {
            logger.fatal(Messages.getString("Error.Fatal"), e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("readCridFiles() - end");
        }
        return result;
    }

    public IFile[] readFiles(String regex) {
        if (logger.isDebugEnabled()) {
            logger.debug("readFiles(String) - start");
        }
        if (regex == null) regex = ".*";
        File[] files = listDirFiles(new RegexFileFilter(regex));
        IFile[] result = new IFile[files.length];
        for (int i = 0; i < files.length; i++) result[i] = new FsFile(files[i]);
        if (logger.isDebugEnabled()) {
            logger.debug("readFiles(String) - end");
        }
        return result;
    }

    public boolean isHierarchical() {
        return true;
    }

    public ILocation getParent() {
        if (logger.isDebugEnabled()) {
            logger.debug("getParent() - start");
        }
        File parentFile = dir.getParentFile();
        if (parentFile == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("getParent() - end");
            }
            return null;
        } else {
            ILocation returnILocation = new FsLocation(parentFile);
            if (logger.isDebugEnabled()) {
                logger.debug("getParent() - end");
            }
            return returnILocation;
        }
    }

    public ILocation[] getSubLocations() {
        if (logger.isDebugEnabled()) {
            logger.debug("getSubLocations() - start");
        }
        getPathElements();
        File[] subdirs = listDirFiles(new DirFilter());
        ILocation[] result = new ILocation[subdirs.length];
        for (int i = 0; i < subdirs.length; i++) result[i] = new FsLocation(subdirs[i]);
        if (logger.isDebugEnabled()) {
            logger.debug("getSubLocations() - end");
        }
        return result;
    }

    public IFile create(String name) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("create(String) - start");
        }
        File newfile = new File(dir, name);
        newfile.createNewFile();
        IFile returnIFile = new FsFile(newfile);
        if (logger.isDebugEnabled()) {
            logger.debug("create(String) - end");
        }
        return returnIFile;
    }

    public IFile createCridFile(String name) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("createCridFile(String) - start");
        }
        File newfile = new File(dir, name);
        newfile.createNewFile();
        IFile returnIFile = new CridFsFile(newfile);
        if (logger.isDebugEnabled()) {
            logger.debug("createCridFile(String) - end");
        }
        return returnIFile;
    }

    public String getName() {
        if (logger.isDebugEnabled()) {
            logger.debug("getName() - start");
        }
        if (dir.getName().equals("")) {
            String returnString = getAbsolutePath();
            if (logger.isDebugEnabled()) {
                logger.debug("getName() - end");
            }
            return returnString;
        } else {
            String returnString = dir.getName();
            if (logger.isDebugEnabled()) {
                logger.debug("getName() - end");
            }
            return returnString;
        }
    }

    public String getAbsolutePath() {
        if (logger.isDebugEnabled()) {
            logger.debug("getAbsolutePath() - start");
        }
        String returnString = dir.getAbsolutePath();
        if (logger.isDebugEnabled()) {
            logger.debug("getAbsolutePath() - end");
        }
        return returnString;
    }

    /**
	 * String-Darstellung des Objektes. Liefert <code>getName</code>.
	 * 
	 * @return Name des Verzeichnisses.
	 */
    public String toString() {
        if (logger.isDebugEnabled()) {
            logger.debug("toString() - start");
        }
        String returnString = getName();
        if (logger.isDebugEnabled()) {
            logger.debug("toString() - end");
        }
        return returnString;
    }

    public int hashCode() {
        if (logger.isDebugEnabled()) {
            logger.debug("hashCode() - start");
        }
        int returnint = dir.hashCode();
        if (logger.isDebugEnabled()) {
            logger.debug("hashCode() - end");
        }
        return returnint;
    }

    public boolean equals(Object obj) {
        if (logger.isDebugEnabled()) {
            logger.debug("equals(Object) - start");
        }
        if (obj instanceof FsLocation) {
            boolean returnboolean = ((FsLocation) obj).dir.equals(this.dir);
            if (logger.isDebugEnabled()) {
                logger.debug("equals(Object) - end");
            }
            return returnboolean;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("equals(Object) - end");
            }
            return false;
        }
    }

    public IFile getFile(String name) {
        if (logger.isDebugEnabled()) {
            logger.debug("getFile(String) - start");
        }
        IFile returnIFile = new FsFile(new File(dir, name));
        if (logger.isDebugEnabled()) {
            logger.debug("getFile(String) - end");
        }
        return returnIFile;
    }

    public IFile getCridFile(String name) {
        if (logger.isDebugEnabled()) {
            logger.debug("getCridFile(String) - start");
        }
        IFile returnIFile = new CridFsFile(new File(dir, name));
        if (logger.isDebugEnabled()) {
            logger.debug("getCridFile(String) - end");
        }
        return returnIFile;
    }

    public boolean exists() {
        if (logger.isDebugEnabled()) {
            logger.debug("exists() - start");
        }
        boolean returnboolean = dir.exists();
        if (logger.isDebugEnabled()) {
            logger.debug("exists() - end");
        }
        return returnboolean;
    }

    public URI getURI() {
        if (logger.isDebugEnabled()) {
            logger.debug("getURI() - start");
        }
        URI returnURI = dir.toURI();
        if (logger.isDebugEnabled()) {
            logger.debug("getURI() - end");
        }
        return returnURI;
    }

    /**
	 * Gibt eine Liste aller Files der Directory im Kontext zurueck. Dabei werden nicht lesbare Files
	 * dargestellt (z.B. "System Volume Information").
	 * 
	 * @param filter FileFilter zm darstellen nur bestimter Files
	 * @return Array aller Files, die dem Filter entsprechen
	 */
    private File[] listDirFiles(FileFilter filter) {
        if (logger.isDebugEnabled()) {
            logger.debug("listDirFiles(FileFilter) - start");
        }
        File[] subdirs = dir.listFiles(filter);
        if (subdirs == null) {
            subdirs = new File[0];
        }
        if (logger.isDebugEnabled()) {
            logger.debug("listDirFiles(FileFilter) - end");
        }
        return subdirs;
    }

    public boolean copy(IFile source, IFile dest) {
        if (logger.isDebugEnabled()) {
            logger.debug("copy(IFile, IFile) - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("copy(IFile, IFile) - end");
        }
        return false;
    }

    public boolean move(IFile source, IFile dest) {
        if (logger.isDebugEnabled()) {
            logger.debug("move(IFile, IFile) - start");
        }
        boolean result = false;
        if (source instanceof FsFile && dest instanceof FsFile) {
            FsFile fsSource = (FsFile) source;
            FsFile fsDest = (FsFile) dest;
            result = fsSource.getFile().renameTo(fsDest.getFile());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("move(IFile, IFile) - end");
        }
        return result;
    }

    public File getInternalRepresentation() {
        return dir;
    }

    public String[] getPathElements() {
        if (logger.isDebugEnabled()) {
            logger.debug("getPathElements() - start");
        }
        String[] result;
        ILocation parent = getParent();
        if (parent != null) {
            String[] parents = parent.getPathElements();
            result = new String[parents.length + 1];
            for (int i = 0; i < parents.length; i++) result[i] = parents[i];
        } else result = new String[1];
        result[result.length - 1] = getName();
        if (logger.isDebugEnabled()) {
            logger.debug("getPathElements() - end");
        }
        return result;
    }

    public String getOptionString() {
        return "";
    }

    public void setOptionString(String options) {
    }

    public boolean isFile() {
        return false;
    }

    public boolean isLocation() {
        return true;
    }

    public boolean isOnBox() {
        return false;
    }

    public IBox getBox() {
        return null;
    }
}
