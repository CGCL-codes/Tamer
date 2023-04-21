package org.cyberaide.gridshell.commands.object;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.cyberaide.gridshell.commands.GSException;
import org.cyberaide.gridshell.commands.GSSystem;
import org.cyberaide.gridshell.commands.util.ObjectLocator;

public class GSType {

    private static final String MUST = "[MUST HAVE ATTRIBUTES]";

    private static final String MAY = "[MAY HAVE ATTRIBUTES]";

    private static final String LINE0 = "[METADATA]";

    private static final String LINE1 = "name:";

    private static final String LINE2 = "";

    private static final String LINE3 = MUST;

    private String localPath = null;

    private String name = null;

    private Set<String> mustAttribs = null;

    private Set<String> mayAttribs = null;

    private LinkedHashMap<String, String> attribMap;

    /**
	 * Constructor for an existing type.
	 * 
	 * @param name
	 */
    public GSType(String name) {
        this.name = name;
        mustAttribs = new LinkedHashSet<String>();
        mayAttribs = new LinkedHashSet<String>();
    }

    /**
	 * Constructor for a new type.
	 * 
	 * @param name
	 * @param mustAttribs
	 * @param mayAttribs
	 */
    public GSType(String name, Set<String> mustAttribs, Set<String> mayAttribs) {
        this.name = name;
        this.mustAttribs = mustAttribs;
        this.mayAttribs = mayAttribs;
        createAttribMap();
        if (ObjectLocator.isTypeNameUnique(name, true)) {
            store(false);
        }
    }

    /**
	 * Load the type from a file.
	 * 
	 * @param file
	 * @return true if the object was successfully loaded, false ow
	 */
    public boolean load(File file) {
        boolean valid = true;
        String line = null;
        String[] tokens = null;
        int lineCount = 0;
        BufferedReader reader = null;
        String constraint = null;
        String attrib = null;
        localPath = file.getAbsolutePath();
        try {
            reader = new BufferedReader(new FileReader(file));
            valid = reader.ready();
            while (((line = reader.readLine()) != null) && valid) {
                switch(lineCount) {
                    case 0:
                        valid = (line.equals(LINE0));
                        break;
                    case 1:
                        valid = (line.startsWith(LINE1));
                        if (valid) {
                            tokens = line.split(LINE1);
                            valid = name.equals(tokens[1].trim());
                        }
                        break;
                    case 2:
                        valid = (line.equals(LINE2));
                        break;
                    case 3:
                        valid = (line.equals(LINE3));
                        constraint = MUST;
                        break;
                    default:
                        if (line.trim().length() > 0) {
                            if (!line.startsWith(GSSystem.COMMENT_MARKER)) {
                                if (line.startsWith(MAY)) {
                                    valid = constraint.equals(MUST);
                                    constraint = MAY;
                                } else {
                                    attrib = line;
                                    if (constraint.equals(MUST)) {
                                        mustAttribs.add(attrib);
                                    } else {
                                        mayAttribs.add(attrib);
                                    }
                                }
                            }
                        }
                        break;
                }
                lineCount++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            valid = false;
        } catch (IOException e) {
            e.printStackTrace();
            valid = false;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!valid) {
            System.err.println("Error: GSTYPE Type load failed on line " + (lineCount - 1) + " (indexed from 0).");
        } else {
            createAttribMap();
        }
        return valid;
    }

    /**
	 * Store the object to a file.
	 * 
	 * @param overwrite - true if it's okay to overwrite an existing file
	 */
    public void store(boolean overwrite) {
        File file = null;
        BufferedWriter writer = null;
        Iterator<String> attribIter = null;
        String attrib = null;
        try {
            if (localPath != null) {
                file = new File(localPath);
            } else {
                String localDir = GSSystem.TYPE_DIR + File.separator;
                localPath = localDir + name + GSSystem.EXTENSION;
                file = new File(localPath);
                if (file.exists()) {
                    if (!overwrite) {
                        throw new GSException("Error: " + localPath + " already exists.\n" + "Please delete the existing type.");
                    }
                } else {
                    File path = new File(localDir);
                    if (!path.exists()) {
                        path.mkdirs();
                    }
                    file.createNewFile();
                }
            }
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(LINE0 + "\n");
            writer.write(LINE1 + name + "\n");
            writer.write(LINE2 + "\n");
            writer.write(LINE3 + "\n");
            attribIter = mustAttribs.iterator();
            while (attribIter.hasNext()) {
                attrib = attribIter.next();
                writer.write(attrib + "\n");
            }
            writer.write("\n" + MAY + "\n");
            attribIter = mayAttribs.iterator();
            while (attribIter.hasNext()) {
                attrib = attribIter.next();
                writer.write(attrib + "\n");
            }
            writer.flush();
            System.out.println("WRITTEN: " + localPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GSException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Delete this entity by removing its file.
	 */
    public void delete() {
        System.gc();
        if ((new File(localPath)).delete()) {
            System.out.println("DELETED: " + localPath);
        } else {
            System.out.println("Error: Could not delete" + localPath);
        }
    }

    private void createAttribMap() {
        attribMap = new LinkedHashMap<String, String>();
        attribMap.put("name", name);
        attribMap.put(MUST, mustAttribs.toString());
        attribMap.put(MAY, mayAttribs.toString());
    }

    public Map<String, String> getAttribMap() {
        return attribMap;
    }

    /**
	 * Get attribute listing
	 * 
	 * @return attribute listing
	 */
    public String getAttribListing() {
        String listing = "";
        Set<String> mustAttrib = new LinkedHashSet<String>();
        Set<String> mayAttrib = new LinkedHashSet<String>();
        Iterator<String> attribIter = attribMap.keySet().iterator();
        String attrib, constraint = null;
        while (attribIter.hasNext()) {
            attrib = attribIter.next();
            constraint = attribMap.get(attrib);
            if (constraint.equals(MUST)) {
                mustAttrib.add(attrib);
            } else {
                mayAttrib.add(attrib);
            }
        }
        listing += "name " + GSSystem.DELIMITER + " " + name + "\n";
        attribIter = mustAttrib.iterator();
        listing += MUST + "\n";
        while (attribIter.hasNext()) {
            attrib = attribIter.next();
            listing += attrib + "\n";
        }
        attribIter = mayAttrib.iterator();
        listing += MAY + "\n";
        while (attribIter.hasNext()) {
            attrib = attribIter.next();
            listing += attrib + "\n";
        }
        return listing.trim();
    }

    /**
	 * Check an object's attributes.
	 * 
	 * @param object
	 * @return true if the object is valid, false ow
	 */
    public boolean check(GSObject object) {
        boolean valid = true;
        String checkAttrib = null;
        Map<String, String> objectAttribMap = object.getAttribMap();
        Iterator<String> attribIter = mustAttribs.iterator();
        while (attribIter.hasNext() && valid) {
            checkAttrib = attribIter.next();
            valid = objectAttribMap.containsKey(checkAttrib);
            if (valid) valid = !objectAttribMap.get(checkAttrib).equals("");
            if (!valid) {
                valid = GSSystem.profile.containsKey(checkAttrib);
                if (valid) valid = !GSSystem.profile.getProperty(checkAttrib).trim().equals("");
            }
        }
        if (!valid) {
            System.err.println("Error: " + object.getName() + " is missing must have attribute " + checkAttrib);
        }
        return valid;
    }

    protected String getStorageDir() {
        return GSSystem.TYPE_DIR + File.separator;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
