package net.sourceforge.grass.model.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import net.sourceforge.grass.Grass;
import net.sourceforge.grass.model.target.InvalidTargetException;

/**
 * ModuleFileSet is a class which abstracts the configuration file set of
 * targets/target groups for an application module. It contains a collection of
 * FileSet for different target/target group.
 * 
 * @author Saravana Aravind R
 * @version ModuleFileSet.java,v 1.2 2006/01/30 14:28:46 rsaravind Exp
 */
public class ModuleFileSet implements Cloneable {

    private static Logger logger;

    private String name;

    private Map fileSetMap;

    static {
        logger = Logger.getLogger(ModuleFileSet.class.getName());
    }

    /**
     * Creates a new instance of ModuleFileSet.
     */
    public ModuleFileSet() {
        fileSetMap = new LinkedHashMap();
    }

    /**
     * Adds the file definition to the module fileset.
     * @param fileElement the file definition
     * @throws DuplicateFileException if duplicate file definition
     * @throws InvalidFileException if invalid file definition
     * @throws InvalidTargetException
     */
    public void addFileElement(FileElement fileElement) throws DuplicateFileException, InvalidFileException, InvalidTargetException {
        String src = fileElement.getSrc().trim();
        String dest = fileElement.getDest().trim();
        String id = fileElement.getId().trim();
        if (id == null || id.equals("")) {
            throw new InvalidTargetException("Invalid target/group " + id);
        }
        if (src == null || src.equals("")) {
            throw new InvalidFileException("Invalid config file source " + src + ", defined for " + id);
        }
        if (dest == null || dest.equals("")) {
            throw new InvalidFileException("Invalid config file " + dest + ", defined for " + id);
        }
        FileSet aFileSet = (FileSet) fileSetMap.get(id);
        if (aFileSet == null) {
            aFileSet = new FileSet(id);
            fileSetMap.put(id, aFileSet);
        }
        aFileSet.addFileElement(fileElement);
    }

    /**
     * Returns the array of defined config file source names.
     * @return String[] array of defined config file source names
     */
    public String[] getDefinedFiles() {
        FileSet globalFileSet = (FileSet) getFileSetMap().get(Grass.GLOBAL);
        if (globalFileSet == null) {
            return null;
        }
        Set files = globalFileSet.getFileTargetMap().keySet();
        return (String[]) files.toArray(new String[files.size()]);
    }

    /**
     * Returns the list of file definitions in this module fileset.
     * @return List the list of file definitions
     */
    public List getFileElements() {
        List list = new ArrayList();
        FileSet globalFileSet = (FileSet) getFileSetMap().get("global");
        if (globalFileSet == null) {
            return null;
        }
        Set files = globalFileSet.getFileTargetMap().keySet();
        Collection filesets = getFileSetMap().values();
        Iterator filesIterator = files.iterator();
        while (filesIterator.hasNext()) {
            String file = (String) filesIterator.next();
            Iterator fileSetIterator = filesets.iterator();
            while (fileSetIterator.hasNext()) {
                FileSet aFileSet = (FileSet) fileSetIterator.next();
                FileElement fileElement = (FileElement) aFileSet.getFileElement(file);
                if (fileElement != null) {
                    list.add(fileElement);
                }
            }
        }
        logger.fine("Files in module " + name + " are " + list);
        return list;
    }

    /**
     * Returns the file definitions for the given config source file.
     * @param srcFile the config source file
     * @return List the list of file definitions
     */
    public List getFileElements(String srcFile) {
        List list = new ArrayList();
        Collection filesets = getFileSetMap().values();
        Iterator fileSetIterator = filesets.iterator();
        while (fileSetIterator.hasNext()) {
            FileSet aFileSet = (FileSet) fileSetIterator.next();
            FileElement fileElement = (FileElement) aFileSet.getFileElement(srcFile);
            if (fileElement != null) {
                list.add(fileElement);
            }
        }
        return list;
    }

    /**
     * Returns the fileset map in this module fileset.
     * @return Map the fileset map
     */
    public Map getFileSetMap() {
        return fileSetMap;
    }

    /**
     * Returns the name of the module fileset.
     * @return String the module fileset name.
     */
    public String getName() {
        return name;
    }

    /**
     * Tests if this module fileset is empty.
     * @return true if empty else false
     */
    public boolean isEmpty() {
        return (fileSetMap == null || (fileSetMap.size() == 0));
    }

    /**
     * Removes file definitions for the given source file in this module
     * fileset.
     * @param src the config file source.
     * @throws InvalidFileException
     */
    public void removeFileElement(String src) throws InvalidFileException {
        if (src == null || src.equals("")) {
            throw new InvalidFileException("No file selected to remove.");
        }
        Collection filesets = getFileSetMap().values();
        Iterator fileSetIterator = filesets.iterator();
        while (fileSetIterator.hasNext()) {
            FileSet aFileSet = (FileSet) fileSetIterator.next();
            aFileSet.removeFileElement(src);
        }
    }

    /**
     * Removes the file definition for the given id and file source.
     * @param id the target/group id
     * @param src the config file source
     * @throws InvalidFileException
     * @throws InvalidTargetException
     */
    public void removeFileElement(String id, String src) throws InvalidFileException, InvalidTargetException {
        if (id == null || id.equals("")) {
            throw new InvalidTargetException("Null or empty target/group specified.");
        } else if (id.equals("global")) {
            throw new InvalidFileException("File definition for global targets cannot be removed.");
        } else if (src == null || src.equals("")) {
            throw new InvalidFileException("No file selected to remove.");
        }
        FileSet aFileSet = (FileSet) fileSetMap.get(id);
        aFileSet.removeFileElement(src);
    }

    /**
     * Sets the name of this module fileset.
     * @param name the module fileset name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Updates the file definition in this module fileset.
     * 
     * @param fileElement the file definition
     * @throws DuplicateFileException if duplicate file definition
     * @throws InvalidFileException if invalid file definition
     * @throws InvalidTargetException
     */
    public void updateFileElement(FileElement fileElement) throws DuplicateFileException, InvalidFileException, InvalidTargetException {
        String src = fileElement.getSrc().trim();
        String dest = fileElement.getDest().trim();
        String id = fileElement.getId().trim();
        if (id == null || id.equals("")) {
            throw new InvalidTargetException("Invalid target/group " + id);
        }
        if (src == null || src.equals("")) {
            throw new InvalidFileException("Invalid config file source " + src + ", defined for " + id);
        }
        if (dest == null || dest.equals("")) {
            throw new InvalidFileException("Invalid config file " + dest + ", defined for " + id);
        }
        FileSet aFileSet = (FileSet) fileSetMap.get(fileElement.getId());
        aFileSet.updateFileElement(fileElement);
    }

    public boolean equals(Object anotherObject) {
        boolean equal = false;
        if (anotherObject != null && anotherObject instanceof ModuleFileSet) {
            ModuleFileSet anotherFileSets = (ModuleFileSet) anotherObject;
            equal = fileSetMap.equals(anotherFileSets.getFileSetMap());
        }
        return equal;
    }

    public String toString() {
        return "Name " + name + "\n" + fileSetMap;
    }
}
