package netxrv.jnlp.jardiff;

import java.io.*;
import java.util.*;
import java.util.jar.JarOutputStream;
import java.util.zip.*;
import netxrv.jnlp.tardiff.TarDiff;
import netxrv.jnlp.util.JarFile2;

/**
 * JarDiff is able to create a jar file containing the delta between two
 * jar files (old and new). The delta jar file can then be applied to the
 * old jar file to reconstruct the new jar file.
 * <p>
 * Refer to the JNLP spec for details on how this is done.
 *
 * @version 1.13, 06/26/03
 */
public class JarDiff implements JarDiffConstants {

    private static final int DEFAULT_READ_SIZE = 2048;

    private static byte[] newBytes = new byte[DEFAULT_READ_SIZE];

    private static ResourceBundle _resources = null;

    private static boolean _debug;

    public static ResourceBundle getResources() {
        if (_resources == null) {
            _resources = ResourceBundle.getBundle("netxrv/jnlp/jardiff/resources/strings");
        }
        return _resources;
    }

    /**
	 * Creates a patch from the two passed in files, writing the result
	 * to <code>os</code>.
	 */
    public static void createPatch(String oldPath, String newPath, OutputStream os, boolean minimal) throws IOException {
        createPatch(oldPath, newPath, os, minimal, false, false);
    }

    /**
	 * Creates a patch from the two passed in files, writing the result
	 * to <code>os</code>.
	 */
    public static void createPatch(String oldPath, String newPath, OutputStream os, boolean minimal, boolean recursive, boolean doTarDiffs) throws IOException {
        JarFile2 oldJar = new JarFile2(oldPath);
        JarFile2 newJar = new JarFile2(newPath);
        HashMap moved = new HashMap();
        HashSet visited = new HashSet();
        HashSet implicit = new HashSet();
        HashSet moveSrc = new HashSet();
        HashSet newEntries = new HashSet();
        HashSet jarDiffs = new HashSet();
        Iterator entries = newJar.getJarEntries();
        if (entries != null) {
            while (entries.hasNext()) {
                ZipEntry newEntry = (ZipEntry) entries.next();
                String newname = newEntry.getName();
                String oldname = oldJar.getBestMatch(newJar, newEntry);
                if (oldname == null) {
                    if (_debug) {
                        System.out.println("NEW: " + newname);
                    }
                    newEntries.add(newname);
                } else {
                    if (oldname.equals(newname) && !moveSrc.contains(oldname)) {
                        if (_debug) {
                            System.out.println(newname + " added to implicit set!");
                        }
                        implicit.add(newname);
                    } else {
                        if (!minimal && (implicit.contains(oldname) || moveSrc.contains(oldname))) {
                            if (_debug) {
                                System.out.println("NEW: " + newname);
                            }
                            newEntries.add(newname);
                        } else {
                            if (_debug) {
                                System.err.println("moved.put " + newname + " " + oldname);
                            }
                            moved.put(newname, oldname);
                            moveSrc.add(oldname);
                        }
                        if (implicit.contains(oldname) && minimal) {
                            if (_debug) {
                                System.err.println("implicit.remove " + oldname);
                                System.err.println("moved.put " + oldname + " " + oldname);
                            }
                            implicit.remove(oldname);
                            moved.put(oldname, oldname);
                            moveSrc.add(oldname);
                        }
                    }
                }
            }
        }
        ArrayList deleted = new ArrayList();
        entries = oldJar.getJarEntries();
        if (entries != null) {
            while (entries.hasNext()) {
                ZipEntry oldEntry = (ZipEntry) entries.next();
                String oldName = oldEntry.getName();
                if (!implicit.contains(oldName) && !moveSrc.contains(oldName) && !newEntries.contains(oldName)) {
                    if (_debug) {
                        System.err.println("deleted.add " + oldName);
                    }
                    deleted.add(oldName);
                }
            }
        }
        if (_debug) {
            entries = moved.keySet().iterator();
            if (entries != null) {
                System.out.println("MOVED MAP!!!");
                while (entries.hasNext()) {
                    String newName = (String) entries.next();
                    String oldName = (String) moved.get(newName);
                    System.out.println("key is " + newName + " value is " + oldName);
                }
            }
            entries = implicit.iterator();
            if (entries != null) {
                System.out.println("IMOVE MAP!!!");
                while (entries.hasNext()) {
                    String newName = (String) entries.next();
                    System.out.println("key is " + newName);
                }
            }
        }
        JarOutputStream jos = new JarOutputStream(os);
        createIndex(jos, deleted, moved);
        entries = newEntries.iterator();
        if (entries != null) {
            while (entries.hasNext()) {
                String newName = (String) entries.next();
                if (_debug) {
                    System.out.println("New File: " + newName);
                }
                if (recursive && newName.endsWith(".jar") && oldJar.getEntryByName(newName) != null) {
                    String jardiffOldPath = oldJar.extractTempFile(newName, ".jar");
                    String jardiffNewPath = newJar.extractTempFile(newName, ".jar");
                    ByteArrayOutputStream jarDiffOs = new ByteArrayOutputStream();
                    createPatch(jardiffOldPath, jardiffNewPath, jarDiffOs, minimal, recursive, doTarDiffs);
                    InputStream jarDiffIs = new ByteArrayInputStream(jarDiffOs.toByteArray());
                    String jarDiffEntryName = newName.substring(0, newName.lastIndexOf('.')) + ".jardiff";
                    writeEntry(jos, new ZipEntry(jarDiffEntryName), jarDiffIs);
                } else if (doTarDiffs && (newName.endsWith(".tar.gz") || newName.endsWith(".tar")) && oldJar.getEntryByName(newName) != null) {
                    String tarExtension = null;
                    if (newName.endsWith(".tar.gz")) tarExtension = "tar.gz"; else tarExtension = "tar";
                    String tardiffOldPath = oldJar.extractTempFile(newName, "." + tarExtension);
                    String tardiffNewPath = newJar.extractTempFile(newName, "." + tarExtension);
                    ByteArrayOutputStream tarDiffOs = new ByteArrayOutputStream();
                    TarDiff.createPatch(tardiffOldPath, tardiffNewPath, tarDiffOs, minimal, recursive);
                    tarDiffOs.close();
                    InputStream tarDiffIs = new ByteArrayInputStream(tarDiffOs.toByteArray());
                    String tarDiffEntryName = null;
                    if (tarExtension.equals("tar.gz")) tarDiffEntryName = newName.replace("." + tarExtension, ".tardiff.gz"); else tarDiffEntryName = newName.substring(0, newName.lastIndexOf('.')) + ".tardiff";
                    writeEntry(jos, new ZipEntry(tarDiffEntryName), tarDiffIs);
                } else {
                    writeEntry(jos, newJar.getEntryByName(newName), newJar);
                }
            }
        }
        jos.finish();
    }

    /**
	 * Writes the index file out to <code>jos</code>.
	 * <code>oldEntries</code> gives the names of the files that were removed,
	 * <code>movedMap</code> maps from the new name to the old name.
	 */
    private static void createIndex(JarOutputStream jos, List oldEntries, Map movedMap) throws IOException {
        StringWriter writer = new StringWriter();
        writer.write(VERSION_HEADER);
        writer.write("\r\n");
        for (int counter = 0; counter < oldEntries.size(); counter++) {
            String name = (String) oldEntries.get(counter);
            writer.write(REMOVE_COMMAND);
            writer.write(" ");
            writeEscapedString(writer, name);
            writer.write("\r\n");
        }
        Iterator names = movedMap.keySet().iterator();
        if (names != null) {
            while (names.hasNext()) {
                String newName = (String) names.next();
                String oldName = (String) movedMap.get(newName);
                writer.write(MOVE_COMMAND);
                writer.write(" ");
                writeEscapedString(writer, oldName);
                writer.write(" ");
                writeEscapedString(writer, newName);
                writer.write("\r\n");
            }
        }
        ZipEntry je = new ZipEntry(INDEX_NAME);
        byte[] bytes = writer.toString().getBytes("UTF-8");
        writer.close();
        jos.putNextEntry(je);
        jos.write(bytes, 0, bytes.length);
    }

    private static void writeEscapedString(Writer writer, String string) throws IOException {
        int index = 0;
        int last = 0;
        char[] chars = null;
        while ((index = string.indexOf(' ', index)) != -1) {
            if (last != index) {
                if (chars == null) {
                    chars = string.toCharArray();
                }
                writer.write(chars, last, index - last);
            }
            last = index;
            index++;
            writer.write('\\');
        }
        if (last != 0) {
            writer.write(chars, last, chars.length - last);
        } else {
            writer.write(string);
        }
    }

    private static void writeEntry(JarOutputStream jos, ZipEntry entry, JarFile2 file) throws IOException {
        writeEntry(jos, entry, file.getInputStream(entry));
    }

    private static void writeEntry(JarOutputStream jos, ZipEntry entry, InputStream data) throws IOException {
        jos.putNextEntry(entry);
        int size = data.read(newBytes);
        while (size != -1) {
            jos.write(newBytes, 0, size);
            size = data.read(newBytes);
        }
        data.close();
    }

    private static void showHelp() {
        System.out.println("JarDiff: [-nonminimal (for backward compatibility with 1.0.1/1.0] [-creatediff | -applydiff] [-output file] old.jar new.jar");
    }

    public static void main(String[] args) throws IOException {
        boolean diff = true;
        boolean minimal = true;
        String outputFile = "out.jardiff";
        for (int counter = 0; counter < args.length; counter++) {
            if (args[counter].equals("-nonminimal") || args[counter].equals("-n")) {
                minimal = false;
            } else if (args[counter].equals("-creatediff") || args[counter].equals("-c")) {
                diff = true;
            } else if (args[counter].equals("-applydiff") || args[counter].equals("-a")) {
                diff = false;
            } else if (args[counter].equals("-debug") || args[counter].equals("-d")) {
                _debug = true;
            } else if (args[counter].equals("-output") || args[counter].equals("-o")) {
                if (++counter < args.length) {
                    outputFile = args[counter];
                }
            } else {
                if ((counter + 2) != args.length) {
                    showHelp();
                    System.exit(0);
                }
                if (diff) {
                    try {
                        OutputStream os = new FileOutputStream(outputFile);
                        JarDiff.createPatch(args[counter], args[counter + 1], os, minimal, true, true);
                        os.close();
                    } catch (IOException ioe) {
                        try {
                            System.out.println(getResources().getString("jardiff.error.create") + " " + ioe);
                        } catch (MissingResourceException mre) {
                        }
                    }
                } else {
                    try {
                        OutputStream os = new FileOutputStream(outputFile);
                        new JarDiffPatcher().applyPatch(null, args[counter], args[counter + 1], os);
                        os.close();
                    } catch (Exception e) {
                        try {
                            System.out.println(getResources().getString("jardiff.error.apply") + " " + e);
                        } catch (MissingResourceException mre) {
                        }
                    }
                }
                System.exit(0);
            }
        }
        showHelp();
    }
}
