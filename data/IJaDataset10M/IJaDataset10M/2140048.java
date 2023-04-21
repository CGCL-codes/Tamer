package com.loribel.commons.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.loribel.commons.abstraction.ENCODING;
import com.loribel.commons.abstraction.GB_StringAction;
import com.loribel.commons.io.GB_DirTools;
import com.loribel.commons.io.GB_FileFilterFactory;
import com.loribel.commons.net.GB_UrlTools;
import com.loribel.commons.util.comparator.GB_ComparatorTools;

/**
 * Class tools for <tt>File</tt>.
 * <p>
 * Somme methods use NIO.
 * 
 * See Test : com.loribel.commons.file.GB_FileToolsTest
 * 
 * @author Gregory Borelli
 */
public abstract class GB_FileTools {

    private static GB_FileToolsI tools;

    public static void appendLine(File a_file, String a_line, String a_encoding) throws IOException {
        appendLine(a_file, a_line, a_encoding, false, true, true);
    }

    public static void appendLine(File a_file, String a_line, String a_encoding, boolean a_sort, boolean a_ascending, boolean a_flagIgnoreCase) throws IOException {
        List l_lines = readLinesList(a_file, a_encoding);
        CTools.addSingle(l_lines, a_line);
        if (a_sort) {
            GB_ComparatorTools.sortStrings(l_lines, a_ascending, a_flagIgnoreCase);
        }
        writeLines(a_file, l_lines, a_encoding);
    }

    public static String appendSeparatorEnd(String a_filename, char a_separator) {
        if (!a_filename.endsWith("" + a_separator)) {
            return a_filename + a_separator;
        }
        return a_filename;
    }

    /**
     * Change le r�pertoire d'un fichier.
     * 
     * @param a_file Fichier source
     * @param a_dir R�pertoire de d�part 
     * @param a_destDir R�pertoire de destination
     * 
     * @return Nouveau fichier dans le r�pertoire de destr
     */
    public static File changeDir(File a_file, File a_dir, File a_destDir) {
        if (a_destDir == null) {
            return a_file;
        }
        if (a_dir.equals(a_destDir)) {
            return a_file;
        }
        String l_pathRelative = pathRelative(a_dir, a_file);
        return new File(a_destDir, l_pathRelative);
    }

    /**
     * Si le File n'a pas l'extension a_entension, retourne un nouveau File
     * avec cette extension.<p>
     * Dans le cas contraire, retourne a_file.<br>
     *
     * @param a_file File -
     * @param a_extension String - extension sans le '.'
     *
     * @return File
     */
    public static File checkAndAddExtension(File a_file, String a_extension) {
        if (a_file == null) {
            return null;
        }
        File retour = a_file;
        String l_filename = a_file.getAbsolutePath();
        if (!l_filename.toLowerCase().endsWith("." + a_extension.toLowerCase())) {
            l_filename += "." + a_extension;
            retour = new File(l_filename);
        }
        return retour;
    }

    /**
     * V�rifie et modifie si besoin le filename pour utiliser le bon s�parateur
     *  en fonction de l'OS.
     * <p>
     *
     * @param a_filename String -
     *
     * @return String
     */
    public static String checkSeparator(String a_filename) {
        String separator = File.separator;
        if (separator.equals("/")) {
            a_filename = STools.replace(a_filename, "\\", separator);
        } else {
            a_filename = STools.replace(a_filename, "/", separator);
        }
        return a_filename;
    }

    public static void closeSafe(InputStream a_is) {
        if (a_is == null) {
            return;
        }
        try {
            a_is.close();
        } catch (IOException ex) {
        }
    }

    public static void closeSafe(OutputStream a_os) {
        if (a_os == null) {
            return;
        }
        try {
            a_os.close();
        } catch (IOException ex) {
        }
    }

    public static void closeSafe(Reader a_reader) {
        if (a_reader == null) {
            return;
        }
        try {
            a_reader.close();
        } catch (IOException ex) {
        }
    }

    public static void closeSafe(Writer a_writer) {
        if (a_writer == null) {
            return;
        }
        try {
            a_writer.close();
        } catch (IOException ex) {
        }
    }

    /**
     * Concatene tout le contenu des fichiers d'un r�pertoire 
     * dans un fichier texte du m�me nom que le r�pertoire.
     * Ajoute un saut de ligne entre chaque contenu de fichier.
     */
    public static File concatFiles(File a_dir, boolean a_deep, String a_encoding) throws IOException {
        File retour = new File(a_dir.getParentFile(), a_dir.getName() + ".txt");
        FileFilter l_filter = GB_FileFilterFactory.newFilterFile();
        File[] l_files = GB_DirTools.listFiles(a_dir, l_filter, a_deep);
        concatFiles(l_files, retour, a_encoding, null, AA.SL);
        return retour;
    }

    public static void concatFiles(File[] a_filesSrc, File a_fileDest, String a_encoding, GB_StringAction a_sa, String a_separator) throws IOException {
        Writer l_writer = newFileWriter(a_fileDest, a_encoding);
        int len = CTools.getSize(a_filesSrc);
        int l_idInfo = Info.newId(len, "Regroupement de fichiers...");
        for (int i = 0; i < len; i++) {
            File l_file = a_filesSrc[i];
            Info.setInfoList(l_idInfo, i, l_file.getName());
            String l_content = readFile(l_file, a_encoding);
            if (a_sa != null) {
                l_content = a_sa.doActionStr(l_content);
            }
            l_writer.write(l_content);
            if (i != len - 1) {
                l_writer.write(a_separator);
            }
        }
        Info.end(l_idInfo);
        l_writer.close();
    }

    /**
     * Copy a file.
     *
     * @param a_fileSrc File - the source file
     * @param a_fileDest File - the destination file
     */
    public static void copy(File a_fileSrc, File a_fileDest) throws IOException {
        copy(a_fileSrc, a_fileDest, false);
    }

    /**
     * Copy a file to an other file.
     *
     * @param a_fileSrc File - the source file
     * @param a_fileDest File - the destination file
     * @param a_append boolean - true to append content
     */
    public static void copy(File a_fileSrc, File a_fileDest, boolean a_append) throws IOException {
        getTools().copyFile(a_fileSrc, a_fileDest, a_append);
    }

    /**
     * Copy a directory tree.
     * <ul>
     *   <li>If a_fileSrc is a file copy to a_fileDest</li>
     *   <li>If a_fileSrc is a directory, copy the content of this directory to
     *  a_fileDest (recursive)</li>
     * </ul>
     *
     * @param a_fileSrc File - a_fileSrc the file or directory to copy
     * @param a_fileDest File - a_fileDest the destination for the copy
     */
    public static void copyFiles(File a_fileSrc, File a_fileDest) throws IOException {
        if (a_fileSrc.isDirectory()) {
            a_fileDest.mkdirs();
            String[] l_list = a_fileSrc.list();
            int len = l_list.length;
            String l_filename = null;
            int l_id = Info.newId(len);
            for (int i = 0; i < len; i++) {
                l_filename = l_list[i];
                copyFiles(new File(a_fileSrc, l_filename), new File(a_fileDest, l_filename));
                Info.setInfoList(l_id, i, l_filename);
            }
            Info.end(l_id);
        } else {
            copy(a_fileSrc, a_fileDest, false);
        }
    }

    public static void copyInputStreamToFile(InputStream a_inputStream, File a_file) throws IOException {
        getTools().copyInputStreamToFile(a_inputStream, a_file);
    }

    public static boolean delete(File a_file) {
        if (a_file == null) {
            return false;
        }
        return a_file.delete();
    }

    /**
     * Supprime tous les fichiers et sous r�pertoires si deep.
     * <p>
     * Le r�pertoire lui m�me est aussi effac� si il est vide.
     * <p>
     *
     * @param a_dir File -
     */
    public static void deleteDir(File a_dir, boolean a_deep) {
        if (a_dir == null) {
            return;
        }
        String[] l_list = a_dir.list();
        if (l_list == null) {
            a_dir.delete();
            return;
        }
        File l_file;
        int len = l_list.length;
        int l_idInfo = Info.newId(len, "Suppression du dossier " + a_dir.getName() + "...");
        for (int i = 0; i < len; i++) {
            l_file = new File(a_dir, l_list[i]);
            Info.setInfoList(l_idInfo, i, l_file);
            if (l_file.isDirectory()) {
                if (a_deep) {
                    deleteDir(l_file, true);
                }
            } else {
                l_file.delete();
            }
        }
        Info.end(l_idInfo);
        if (CTools.isEmpty(a_dir.list())) {
            a_dir.delete();
        }
    }

    /**
     * Supprime tous les fichiers et sous r�pertoires.
     * <p>
     * Le r�pertoire lui m�me est aussi effac�.
     * <p>
     */
    public static void deleteDirDeeep(File a_dir) {
        deleteDir(a_dir, true);
    }

    /**
     * Supprime les fichiers d'un r�pertoire par rapport � un filtre.
     * <p>
     * Les fichiers supprim�s seront ceux qui valid� par le filtre.
     * <p>
     *
     * @param a_dir File -
     * @param a_filter FilenameFilter -
     */
    public static void deleteFiles(File a_dir, FilenameFilter a_filter) {
        if (a_dir == null) {
            return;
        }
        String[] l_list = a_dir.list(a_filter);
        if (l_list != null) {
            File l_file;
            int len = l_list.length;
            for (int i = 0; i < len; i++) {
                l_file = new File(a_dir, l_list[i]);
                l_file.delete();
            }
        }
    }

    public static void deleteFiles(File[] a_files) {
        int len = CTools.getSize(a_files);
        for (int i = 0; i < len; i++) {
            File l_file = a_files[i];
            l_file.delete();
        }
    }

    /**
     * Return true if file is equals.
     *
     * @param a_file1 File -
     * @param a_file2 File -
     *
     * @return boolean
     */
    public static boolean equalsPath(File a_file1, File a_file2) {
        if (a_file1 == null) {
            return (a_file2 == null);
        }
        if (a_file2 == null) {
            return false;
        }
        String l_file1 = a_file1.getAbsolutePath();
        String l_file2 = a_file2.getAbsolutePath();
        return l_file1.equals(l_file2);
    }

    /**
     * Return true if file is equals.
     *
     * @param a_file1 String -
     * @param a_file2 String -
     *
     * @return boolean
     */
    public static boolean equalsPath(String a_file1, String a_file2) {
        if (a_file1 == null) {
            return (a_file2 == null);
        }
        if (a_file2 == null) {
            return false;
        }
        String l_file1 = nameWithConvention(a_file1);
        String l_file2 = nameWithConvention(a_file2);
        return l_file1.equals(l_file2);
    }

    public static void extractResourceToFile(Class a_loader, String a_pathName, File a_file) throws IOException {
        getTools().extractResourceToFile(a_loader, a_pathName, a_file);
    }

    /**
     * Returns the extension of the file.
     * <p>
     * See {@link #getExtension(String)} for details.
     *
     * @param a_file File -
     *
     * @return String
     */
    public static String getExtension(File a_file) {
        return getExtension(a_file.getName());
    }

    /**
     * Returns the extension of the file.
     * <p>
     * L'extension est la partie apr�s le dernier '.'.
     * <p>
     *
     * @param a_filename String -
     *
     * @return String
     */
    public static String getExtension(String a_filename) {
        String retour = "";
        int l_index = a_filename.lastIndexOf('.');
        if (l_index > 0) {
            retour = a_filename.substring(l_index + 1);
        }
        return retour;
    }

    /**
     * Returns the file Attributes String.
     * <p>
     * <ul>
     *   <li>Default => return ""
     *   <li>Read Only => return "R"
     *   <li>Hidden => return "H"
     *   <li>Read Only / Hidden => return "RH"
     * </ul>
     *
     * @param a_file File -
     *
     * @return String
     */
    public static String getFileAttributes(File a_file) {
        String retour = "";
        if (!a_file.canWrite()) {
            retour += "R";
        }
        if (a_file.isHidden()) {
            retour += "H";
        }
        return retour;
    }

    /**
     * Returns a file into user home directory.
     */
    public static File getFileFromUserHome(String a_name) {
        File l_dir = getUserHome();
        return new File(l_dir, a_name);
    }

    public static String getName(File a_file, boolean a_fullPath) {
        if (a_file == null) {
            return null;
        }
        if (a_fullPath) {
            return a_file.getAbsolutePath();
        } else {
            return a_file.getName();
        }
    }

    /**
     * Returns the name of a filename.
     * <pre>
     *   null               -> null
     *   toto               -> toto
     *   toto.xml           -> toto.xml
     *   path/toto.xml      -> toto.xml
     *   C:/temp/toto.xml   -> toto.xml
     *   C:\temp\toto.xml   -> toto.xml
     * </pre>
     */
    public static String getName(String a_filename) {
        if (a_filename == null) {
            return null;
        }
        File l_file = new File(a_filename);
        return l_file.getName();
    }

    /**
     * Returns an array with two String.
     * <ul>
     *   <li>retour[0]: name without extension</li>
     *   <li>retour[1]: the extension</li>
     * </ul>
     * If dot ('.') not found in the filename, return null as extension.
     * <p>
     */
    public static String[] getNameExtension(File a_file) {
        String[] retour = new String[2];
        String l_filename = a_file.getName();
        int l_index = l_filename.lastIndexOf('.');
        if (l_index > 0) {
            retour[0] = l_filename.substring(0, l_index);
            retour[1] = l_filename.substring(l_index + 1);
        } else {
            retour[0] = l_filename;
            retour[1] = null;
        }
        return retour;
    }

    public static String[] getNames(File[] a_files, boolean a_fullPath) {
        int len = CTools.getSize(a_files);
        String[] retour = new String[len];
        for (int i = 0; i < len; i++) {
            File l_file = a_files[i];
            String l_name = getName(l_file, a_fullPath);
            retour[i] = l_name;
        }
        return retour;
    }

    /**
     * Returns the directory parent if exist.
     * Use separator '/' and '\'
     * Returns "" if no parent.
     */
    public static String getParent(String a_path) {
        if (a_path == null) {
            return null;
        }
        int l_index1 = a_path.lastIndexOf('/');
        int l_index2 = a_path.lastIndexOf('\\');
        int l_index = Math.max(l_index1, l_index2);
        if (l_index == -1) {
            return "";
        }
        return a_path.substring(0, l_index);
    }

    /**
     * Retournes le r�pertoire parent ayant comme nom a_dirName.
     * Si dirName n'est pas trouv�, retourne null.
     */
    public static File getParentWithName(File a_file, String a_dirName) {
        if (a_file == null) {
            return null;
        }
        File l_parent = a_file.getParentFile();
        if (l_parent == null) {
            return null;
        }
        if (a_dirName.equals(l_parent.getName())) {
            return l_parent;
        }
        return getParentWithName(l_parent, a_dirName);
    }

    /**
     * Retournes le r�pertoire parent ayant comme nom un nom qui respecte
     * l'expression r�guli�re a_regexDirName.
     * Si dirName n'est pas trouv�, retourne null.
     */
    public static File getParentWithRegex(File a_file, String a_regexDirName) {
        if (a_file == null) {
            return null;
        }
        File l_parent = a_file.getParentFile();
        if (l_parent == null) {
            return null;
        }
        if (GB_RegexTools.matches(l_parent.getName(), a_regexDirName)) {
            return l_parent;
        }
        return getParentWithRegex(l_parent, a_regexDirName);
    }

    /**
     * Returns the property "java.io.tmpdir".
     */
    public static File getTempDir() {
        String l_tempDir = System.getProperty("java.io.tmpdir");
        return new File(l_tempDir);
    }

    /**
     * Retourne un fichier temporaire qui est automatiquement effac� en sortie
     * de programme.
     * <p>
     *
     * @param a_filename String -
     *
     * @return File
     */
    public static File getTempFile(String a_filename) {
        return getTempFile(a_filename, true);
    }

    public static File getTempFile(String a_filename, boolean a_deleteOnExit) {
        File l_path = getTempDir();
        File retour = new File(l_path, a_filename);
        if (a_deleteOnExit) {
            retour.deleteOnExit();
        }
        return retour;
    }

    /**
     * Use trampoline pattern to use a default implementation of GB_FileToolsI.
     */
    private static GB_FileToolsI getTools() {
        if (tools == null) {
            tools = (GB_FileToolsI) GB_Tools.getTools(AA.CLASS_FILE_TOOLS, GB_FileToolsI.class);
        }
        return tools;
    }

    /**
     * Returns the user current directory, property "user.dir".
     *
     * @return File
     */
    public static File getUserDir() {
        String l_dir = System.getProperty("user.dir");
        return new File(l_dir);
    }

    /**
     * Returns the user home directory, property "user.home".
     *
     * @return File
     */
    public static File getUserHome() {
        String l_dir = System.getProperty("user.home");
        return new File(l_dir);
    }

    /**
     * Return true if the file has image extension.
     * Extensions :
     * <ul>
     *   <li>.gif</li>
     *   <li>.jpg</li>
     *   <li>.jpeg</li>
     *   <li>.png</li>
     * </ul>
     *
     * @param a_file File -
     *
     * @return boolean
     */
    public static boolean isImgFile(File a_file) {
        String l_name = a_file.getName().toLowerCase();
        if (l_name.endsWith(".gif")) {
            return true;
        }
        if (l_name.endsWith(".jpg")) {
            return true;
        }
        if (l_name.endsWith(".jpeg")) {
            return true;
        }
        if (l_name.endsWith(".png")) {
            return true;
        }
        return false;
    }

    /**
     * This method move files from directory a_fileSrc to a_fileDest.
     * Remove source directory if empty.
     *
     * @param a_fileSrc File - a_fileSrc
     * @param a_fileDest File - a_fileDest
     */
    public static void moveFiles(File a_fileSrc, File a_fileDest) throws IOException {
        moveFiles(a_fileSrc, a_fileDest, a_fileDest);
    }

    /**
     * This method move files from directory a_fileSrc to a_fileDest.
     * a_fileExcept is used to manage the case rename c:\xxx to c:\xxx\abc
     * and to not copy c:\xxx\abc to c:\xxx\abc\abc ...
     *
     * @param a_fileSrc File - the source file
     * @param a_fileDest File - the destination file
     * @param a_fileExcept File -
     */
    private static void moveFiles(File a_fileSrc, File a_fileDest, File a_fileExcept) throws IOException {
        if (a_fileSrc.equals(a_fileExcept)) {
            return;
        }
        if (a_fileSrc.isDirectory()) {
            a_fileDest.mkdirs();
            String[] l_list = a_fileSrc.list();
            int len = l_list.length;
            String l_file = null;
            for (int i = 0; i < len; i++) {
                l_file = l_list[i];
                moveFiles(new File(a_fileSrc, l_file), new File(a_fileDest, l_file), a_fileExcept);
            }
            l_list = a_fileSrc.list();
            if ((l_list == null) || (l_list.length == 0)) {
                a_fileSrc.delete();
            }
        } else {
            boolean b = a_fileSrc.renameTo(a_fileDest);
            if (!b) {
                throw new IOException("The file " + a_fileSrc + " cannot rename to " + a_fileDest);
            }
        }
    }

    /**
     * Return a name with File.separator.
     * Replace / or \\ if needded by File.separator.
     * Remove the last separator character if present.
     *
     * @param a_file String -
     *
     * @return String
     */
    public static String nameWithConvention(String a_file) {
        if (a_file == null) {
            return null;
        }
        String retour = a_file;
        String sep = File.separator;
        if (sep.equals("/")) {
            retour = STools.replace(retour, "\\", "/");
        } else if (sep.equals("\\")) {
            retour = STools.replace(retour, "/", "\\");
        }
        if (retour.endsWith(sep)) {
            retour = retour.substring(0, retour.length() - 1);
        }
        return retour;
    }

    /**
     * Cr�ation d'un r�pertoire avec la date du jour (YYYY.MM.dd-HHHH.mm.ss)
     */
    public static File newDirDate(File a_dir) {
        return newDirDate(a_dir, null);
    }

    public static File newDirDate(File a_dir, String a_prefix) {
        String l_name = GB_DateTools.getTodayWithHoursForFile();
        if (STools.isNotNull(a_prefix)) {
            l_name = a_prefix + l_name;
        }
        return new File(a_dir, l_name);
    }

    public static File newFile(File a_dir, String a_dirName) {
        if (STools.isNotNull(a_dirName)) {
            return new File(a_dir, a_dirName);
        }
        return a_dir;
    }

    public static Reader newFileReader(File a_file, String a_encoding) throws FileNotFoundException, UnsupportedEncodingException {
        if (a_encoding == null) {
            a_encoding = ENCODING.DEFAULT;
        }
        FileInputStream fileinputstream = new FileInputStream(a_file);
        Reader retour = new InputStreamReader(fileinputstream, a_encoding);
        return retour;
    }

    public static Writer newFileWriter(File a_file, String a_encoding) throws FileNotFoundException, UnsupportedEncodingException {
        return newFileWriter(a_file, a_encoding, false);
    }

    /**
     * Permet de cr�er un Writer avec un File et un encoding sp�cifique.
     */
    public static Writer newFileWriter(File a_file, String a_encoding, boolean a_append) throws FileNotFoundException, UnsupportedEncodingException {
        if (a_encoding == null) {
            a_encoding = ENCODING.DEFAULT;
        }
        a_file.getParentFile().mkdirs();
        FileOutputStream fileoutstream = new FileOutputStream(a_file, a_append);
        Writer retour = new OutputStreamWriter(fileoutstream, a_encoding);
        return retour;
    }

    public static String pathAbsolute(String a_pathRoot, String a_pathSrc, String a_path) {
        a_pathRoot = checkSeparator(a_pathRoot);
        a_pathSrc = checkSeparator(a_pathSrc);
        a_path = checkSeparator(a_path);
        a_pathRoot = STools.removeEnd(a_pathRoot, File.separator);
        a_pathSrc = STools.removeEnd(a_pathSrc, File.separator);
        String retour;
        if (a_path.startsWith(File.separator)) {
            retour = a_pathRoot + a_path;
        } else {
            retour = a_pathSrc + File.separator + a_path;
        }
        retour = STools.replace(retour, File.separator, "/");
        retour = GB_UrlTools.normalizePath(retour);
        return checkSeparator(retour);
    }

    /**
     * Retourne le path relatif de a_path par rapport � a_pathRoot.
     * Retourne a_path si non relatif � a_pathRoot.
     * Retourne . si identique.
     *
     * @param a_pathRoot File -
     * @param a_path File -
     *
     * @return String
     */
    public static String pathRelative(File a_pathRoot, File a_path) {
        if (a_path == null) {
            return null;
        }
        if (a_pathRoot == null) {
            return a_path.getAbsolutePath();
        }
        String l_pathRoot = a_pathRoot.getAbsolutePath();
        String l_path = a_path.getAbsolutePath();
        String retour = l_path;
        if (retour.equals(l_pathRoot)) {
            return ".";
        }
        if (retour.startsWith(l_pathRoot)) {
            retour = retour.substring(l_pathRoot.length() + 1);
        }
        return retour;
    }

    /**
     * Retourne le path relatif de a_path par rapport � a_pathRoot.
     * Retourne a_path si non relatif � a_pathRoot.
     * <p>
     *
     * @param a_pathRoot String -
     * @param a_path String -
     *
     * @return String
     */
    public static String pathRelative(String a_pathRoot, String a_path) {
        if (a_pathRoot == null) {
            return a_path;
        }
        if (a_path == null) {
            return a_path;
        }
        a_pathRoot = checkSeparator(a_pathRoot);
        a_path = checkSeparator(a_path);
        if (a_pathRoot.endsWith(File.separator)) {
            a_pathRoot = a_pathRoot.substring(0, a_pathRoot.length() - 1);
        }
        String retour = a_path;
        if (a_path.startsWith(a_pathRoot)) {
            retour = a_path.substring(a_pathRoot.length());
            if (retour.startsWith(File.separator)) {
                retour = retour.substring(1);
            }
            if (retour.length() == 0) {
                return ".";
            }
        }
        return retour;
    }

    public static void println(Writer a_writer, String a_line) throws IOException {
        a_writer.write(a_line + AA.SL);
    }

    /**
     * Reads a file.
     */
    public static String readFile(File a_file) throws IOException {
        return getTools().readFile(a_file);
    }

    /**
     * Reads a file.
     */
    public static String readFile(File a_file, String a_encoding) throws IOException {
        return getTools().readFile(a_file, a_encoding);
    }

    /**
     * Read a File and log a Warning if java.io.IOException occured.
     *
     * @param a_file File -
     *
     * @return String
     */
    public static String readFileWithLog(File a_file) {
        String retour = null;
        try {
            retour = readFile(a_file);
        } catch (IOException ex) {
            String l_msg = "Read " + a_file.getPath() + " error.";
            Log.logWarning(GB_FileTools.class, l_msg, ex);
        }
        return retour;
    }

    public static String[] readLines(Class a_loader, String a_pathName, String a_encoding) throws IOException {
        String l_content = readResource(a_loader, a_pathName, a_encoding);
        return GB_StringTools.toLinesArray(l_content);
    }

    public static String[] readLines(File a_file, String a_encoding) throws IOException {
        String l_content = readFile(a_file, a_encoding);
        return GB_StringTools.toLinesArray(l_content);
    }

    public static List readLinesList(File a_file, String a_encoding) throws IOException {
        if (!a_file.exists()) {
            return new ArrayList();
        }
        String l_content = readFile(a_file, a_encoding);
        return GB_StringTools.toLines(l_content);
    }

    /**
     * Si le fichier n'existe pas retourne un tableau vide
     */
    public static String[] readLinesSafe(File a_file, String a_encoding) throws IOException {
        if (!a_file.exists()) {
            return new String[0];
        }
        return readLines(a_file, a_encoding);
    }

    /**
     * Reads a file.
     */
    public static String readReader(Reader a_reader) throws IOException {
        return getTools().readReader(a_reader);
    }

    /**
     * Lecture du contenu d'un fichier de ressource.<p>
     * Attention, si vous utiliser un .jar, les fileName sont caseSensitive,
     * et vous ne pouvez r�f�rencer un fichier avec ../maRessource.txt
     * Si a_loader est null, alors on utilise readFile(a_pathName).
     *
     * @param a_loader Class -
     * @param a_pathName String -
     *
     * @return String
     */
    public static String readResource(Class a_loader, String a_pathName) throws IOException {
        return getTools().readResource(a_loader, a_pathName);
    }

    /**
     * Lecture du contenu d'un fichier de ressource.<p>
     * Attention, si vous utiliser un .jar, les fileName sont caseSensitive,
     * et vous ne pouvez r�f�rencer un fichier avec ../maRessource.txt
     * Si a_loader est null, alors on utilise readFile(a_pathName).
     *
     * @param a_loader Class -
     * @param a_pathName String -
     *
     * @return String
     */
    public static String readResource(Class a_loader, String a_pathName, String a_encoding) throws IOException {
        return getTools().readResource(a_loader, a_pathName, a_encoding);
    }

    /**
     * Remove the extension of the file.
     * Exemple :
     *  - C:/temp/toto.gif => C:/temp/toto
     *  - toto.gif => toto
     *
     * @param a_file File - a_file
     *
     * @return String
     */
    public static String removeExtension(File a_file) {
        if (a_file == null) {
            return null;
        }
        return removeExtension(a_file.getAbsolutePath());
    }

    /**
     * Remove the extension of the file.
     * Exemple :
     *  - C:/temp/toto.gif => C:/temp/toto
     *  - toto.gif => toto
     *
     * @param a_filename String -
     *
     * @return String
     */
    public static String removeExtension(String a_filename) {
        if (a_filename == null) {
            return null;
        }
        int l_index = a_filename.lastIndexOf(".");
        if (l_index > 0) {
            return a_filename.substring(0, l_index);
        }
        return a_filename;
    }

    /**
     * Renames a file.
     */
    public static boolean rename(File a_file, File a_newFile) throws IOException {
        return getTools().renameFile(a_file, a_newFile);
    }

    /**
     * Renomme un fichier selon le nouveau nom avec cr�ation des r�pertoire si
     * besoin.
     *
     * @param a_file File -
     * @param a_newFilename String -
     * @throws IOException
     */
    public static void rename(File a_file, String a_newFilename) throws IOException {
        File l_newFile = new File(a_newFilename);
        rename(a_file, l_newFile);
    }

    /**
     * Renomme un fichier selon le nouveau nom du � l'ex�cution de l'action.
     * On effectue l'action sur le nom seulement (sans path). <br>
     * Voir {@link #renameWithAction}pour que l'action agisse sur le nom complet.
     *
     * @param a_file File -
     * @param a_action GB_StringAction -
     */
    public static void renameNameOnlyWithAction(File a_file, GB_StringAction a_action) {
        String l_name = a_file.getName();
        String l_newName = a_action.doActionStr(l_name);
        if (!l_newName.equals(l_name)) {
            String l_path = a_file.getParent();
            a_file.renameTo(new File(l_path, l_newName));
        }
    }

    /**
     * Renomme un fichier selon le nouveau nom du � l'ex�cution de l'action. On effectue l'action
     * sur le filename complet (avec path) et non pas sur le nom du fichier seulement. <br>
     * Cr�ation automatique des r�pertoires de destination si besoin.
     *
     * @param a_file File -
     * @param a_action GB_StringAction -
     * @throws IOException
     */
    public static void renameWithAction(File a_file, GB_StringAction a_action) throws IOException {
        String l_filename = a_file.getAbsolutePath();
        String l_newFilename = a_action.doActionStr(l_filename);
        if (!l_newFilename.equals(l_filename)) {
            rename(a_file, l_newFilename);
        }
    }

    /**
     * Replace the extension of a file by an other.
     *
     * @param a_file File - the file
     *    (null accepted => return null)
     * @param a_newExtension String - the new extension
     *    (null accepted => remove extension)
     *
     * @return File
     */
    public static File replaceExtension(File a_file, String a_newExtension) {
        if (a_file == null) {
            return null;
        }
        String l_filename = removeExtension(a_file);
        if (a_newExtension != null) {
            l_filename += "." + a_newExtension;
        }
        File retour = new File(l_filename);
        return retour;
    }

    public static String replaceExtension(String a_file, String a_newExtension) {
        if (a_file == null) {
            return null;
        }
        String retour = removeExtension(a_file);
        if (a_newExtension != null) {
            retour += "." + a_newExtension;
        }
        return retour;
    }

    public static File replaceIntoPath(File a_file, String a_find, String a_replace) {
        if (a_file == null) {
            return null;
        }
        String l_name = a_file.getAbsolutePath();
        l_name = STools.replace(l_name, a_find, a_replace);
        File retour = new File(l_name);
        return retour;
    }

    /**
     * Sets the user current directory.
     *
     * @param a_file File -
     */
    public static void setUserDir(File a_file) {
        String l_fileStr = a_file.getAbsolutePath();
        System.setProperty("user.dir", l_fileStr);
    }

    /**
     * Return the name with the extension to lower case.
     *
     * @param a_filename String -
     *
     * @return String
     */
    public static String toLowerCaseExtension(String a_filename) {
        int l_index = a_filename.lastIndexOf('.');
        if (l_index == -1) {
            return a_filename;
        }
        String l_ext = a_filename.substring(l_index + 1);
        l_ext = l_ext.toLowerCase();
        return a_filename.substring(0, l_index + 1) + l_ext;
    }

    /**
     * Ecriture du contenu (a_content) dans le fichier a_file.
     * Si a_append == true, alors le contenu est ajout� � la fin du fichier,
     * sinon le contenu remplace l'ancien contenu.
     * Remarque : on se charge de la cr�ation du directory si besoin.
     *
     * @param a_file File -
     * @param a_content String -
     * @param a_append boolean - true to append content
     */
    public static void writeFile(File a_file, String a_content, boolean a_append) throws IOException {
        getTools().writeFile(a_file, a_content, a_append);
    }

    /**
     * Ecriture du contenu (a_content) dans le fichier a_file.
     * Si a_append == true, alors le contenu est ajout� � la fin du fichier,
     * sinon le contenu remplace l'ancien contenu.
     * Remarque : on se charge de la cr�ation du directory si besoin.
     *
     * @param a_file File -
     * @param a_content String -
     * @param a_append boolean - true to append content
     */
    public static void writeFile(File a_file, String a_content, boolean a_append, String a_encoding) throws IOException {
        getTools().writeFile(a_file, a_content, a_append, a_encoding);
    }

    public static void writeFileHeader(File a_file, String a_line) throws IOException {
        if (a_file.exists()) {
            return;
        }
        writeFile(a_file, a_line, false);
    }

    /**
     * Ecriture du contenu (a_content) dans le fichier a_file.
     * Si a_append == true, alors le contenu est ajout� � la fin du fichier,
     * sinon le contenu remplace l'ancien contenu.
     * Remarque : on se charge de la cr�ation du directory si besoin.
     * <p>
     *
     * @param a_file File -
     * @param a_content String -
     * @param a_append boolean - true to append content
     */
    public static void writeFileWithInfo(File a_file, String a_content, boolean a_append) throws IOException {
        writeFile(a_file, a_content, a_append);
        Log.debug(GB_FileTools.class, "Ecriture de " + a_file.getPath());
    }

    /**
     * Write a file and log a Warning if java.io.IOException occured.
     *
     * @param a_file File -
     * @param a_content String -
     * @param a_append boolean - true to append content
     */
    public static void writeFileWithLog(File a_file, String a_content, boolean a_append) {
        try {
            writeFileWithInfo(a_file, a_content, a_append);
        } catch (IOException ex) {
            String l_msg = "Write " + a_file.getPath() + " ERROR";
            Log.logWarning(GB_FileTools.class, l_msg, ex);
        }
    }

    public static void writeLines(File a_file, Collection a_lines, String a_encoding) throws IOException {
        writeLines(a_file, a_lines, a_encoding, false);
    }

    public static void writeLines(File a_file, Collection a_lines, String a_encoding, boolean a_append) throws IOException {
        Writer l_writer = newFileWriter(a_file, a_encoding, a_append);
        for (Iterator it = a_lines.iterator(); it.hasNext(); ) {
            String l_line = (String) it.next();
            l_writer.write(l_line + AA.SL);
        }
        l_writer.close();
    }

    public static void writeLines(File a_file, String[] a_lines, String a_encoding) throws IOException {
        writeLines(a_file, a_lines, a_encoding, false);
    }

    public static void writeLines(File a_file, String[] a_lines, String a_encoding, boolean a_append) throws IOException {
        Writer l_writer = newFileWriter(a_file, a_encoding, a_append);
        int len = CTools.getSize(a_lines);
        for (int i = 0; i < len; i++) {
            String l_line = a_lines[i];
            l_writer.write(l_line + AA.SL);
        }
        l_writer.close();
    }

    protected GB_FileTools() {
    }
}
