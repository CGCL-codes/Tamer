package xuniversewizard.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import xutools.text.TextManager;
import xutools.universe.core.Language;

/**
 * Manages installation and uninstallation of universes to X2.
 * 
 * @author Tobias Weigel
 * @date 25.10.2009
 * 
 */
public class UniverseInstaller {

    private UserSettings userSettings;

    private TextManager textManager;

    public UniverseInstaller(UserSettings userSettings, TextManager textManager) {
        super();
        this.userSettings = userSettings;
        this.textManager = textManager;
    }

    /**
     * 
     * @return the marker file located in the X2 install directory
     */
    public File getInstallInformationBaseFile() {
        return new File(userSettings.getX2InstallPath().getFile(), "xuw-installinfo.xml");
    }

    /**
     * Installs a universe using the given context. Fails if another universe is
     * currently installed.
     * 
     * @param name
     *            the name of the universe to install. The method expects the
     *            universe's files to exist in a "xuw-..." subdirectory of the
     *            X2 install path.
     * @throws Exception
     */
    public void installUniverse(String name) throws Exception {
        if (isUniverseInstalled()) throw new Exception("Cannot install universe as long as another universe is still installed!");
        File x2inst = userSettings.getX2InstallPath().getFile();
        File x2mod = new File(x2inst, "mods");
        File universeDir = new File(x2inst, "xuw-" + name);
        int baseCatalogNumber = determineNextBaseCatalogNumber();
        InstallInformation ii = new InstallInformation(name, baseCatalogNumber);
        String baseCat = "" + baseCatalogNumber;
        if (baseCat.length() == 1) baseCat = "0" + baseCat;
        ii.writeToFile(getInstallInformationBaseFile());
        copyFile(new File(universeDir, "expansionCatalog.cat"), new File(x2inst, baseCat + ".cat"));
        copyFile(new File(universeDir, "expansionCatalog.dat"), new File(x2inst, baseCat + ".dat"));
        copyFile(new File(universeDir, name + ".cat"), new File(x2mod, name + ".cat"));
        copyFile(new File(universeDir, name + ".dat"), new File(x2mod, name + ".dat"));
    }

    /**
     * 
     * @return an int describing which next X2 base catalog (expansion catalog)
     *         is available.
     */
    private int determineNextBaseCatalogNumber() {
        File x2inst = userSettings.getX2InstallPath().getFile();
        for (int i = 1; i < 100; i++) {
            String s = "" + i;
            if (s.length() == 1) s = "0" + s;
            File f = new File(x2inst, s + ".cat");
            if (!f.exists()) return i;
        }
        return 99;
    }

    /**
     * Uninstalls the currently installed universe.
     * 
     * @return true if the universe was uninstalled successfully, false
     *         otherwise.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public boolean uninstallUniverse() throws IOException, ParserConfigurationException, SAXException {
        if (!isUniverseInstalled()) return false;
        File iiFile = getInstallInformationBaseFile();
        InstallInformation ii = InstallInformation.readFromFile(iiFile);
        File x2inst = userSettings.getX2InstallPath().getFile();
        File x2mod = new File(x2inst, "mods");
        File modFileCat = new File(x2mod, ii.getMapName() + ".cat");
        File modFileDat = new File(x2mod, ii.getMapName() + ".dat");
        File baseFileCat = new File(x2inst, ii.getBaseCatalogNumberString() + ".cat");
        File baseFileDat = new File(x2inst, ii.getBaseCatalogNumberString() + ".dat");
        File universeDir = new File(x2inst, "xuw-" + ii.getMapName());
        if (!universeDir.exists() || (universeDir.listFiles().length == 0)) {
            int res = JOptionPane.showConfirmDialog(null, textManager.getString("installUniverseConfirmUninstallDelete"));
            if (res == JOptionPane.CANCEL_OPTION) return false; else if (res == JOptionPane.YES_OPTION) {
                if (!universeDir.exists()) universeDir.mkdir();
                try {
                    copyFile(baseFileCat, new File(universeDir, "expansionCatalog.cat"));
                    copyFile(baseFileDat, new File(universeDir, "expansionCatalog.dat"));
                } finally {
                    baseFileCat.delete();
                    baseFileDat.delete();
                }
                copyFile(modFileCat, new File(universeDir, ii.getMapName() + ".cat"));
                copyFile(modFileDat, new File(universeDir, ii.getMapName() + ".dat"));
            }
        }
        if (!iiFile.delete()) return false;
        boolean res = true;
        res = res && modFileCat.delete();
        res = res && modFileDat.delete();
        if (baseFileCat.exists()) res = res && baseFileCat.delete();
        if (baseFileDat.exists()) res = res && baseFileDat.delete();
        return res;
    }

    /**
     * Checks whether currently a universe is installed.
     * 
     * @return true or false
     */
    public boolean isUniverseInstalled() {
        File f = getInstallInformationBaseFile();
        return f.exists();
    }

    /**
     * Packs the universe of given context. Uses the raw files created by the
     * Generator class to pack a universe using x2build.exe. Resulting files
     * are: a mod file, a 0x.dat/cat couple containing the universe map, and a
     * X2 init script.
     * 
     * @param context
     * @throws IOException
     */
    public void packUniverse(GeneratorContext context) throws IOException {
        String x2build = "\"" + userSettings.getX2BuildExe().getFile().getCanonicalPath() + "\"";
        String x2tool = "\"" + userSettings.getX2ToolExe().getFile().getCanonicalPath() + "\"";
        Runtime runtime = Runtime.getRuntime();
        LinkedList<File> filesCreated = new LinkedList<File>();
        String gzip = "\"" + new File("").getAbsolutePath() + "\\gzip\\gzip.exe\"";
        String baseCatalog = "expansionCatalog";
        File outputDir = new File(context.getOutputDir());
        File batchFile = new File(outputDir, "pack.bat");
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        PrintStream ps = null;
        try {
            fos = new FileOutputStream(batchFile);
            bos = new BufferedOutputStream(fos);
            ps = new PrintStream(bos);
            String m = context.getMapName();
            ps.println("@echo off");
            ps.println("cd " + outputDir.getAbsolutePath().charAt(0) + ":\\");
            ps.println("cd " + outputDir);
            ps.println("md work");
            ps.println("md work\\t");
            ps.println("md work\\maps");
            ps.println("md work\\scripts");
            ps.println("md work2");
            ps.println("md work2\\cut");
            ps.println("copy \"" + context.getGalaxyFile().getFile().getAbsolutePath() + "\" work\\maps\\" + m + ".xml");
            ps.println("copy \"" + context.getPlayerInitScriptFile().getFile().getAbsolutePath() + "\" work\\scripts\\galaxy." + m + ".initplayership.xml");
            for (Iterator<Language> iter = context.languageIterator(); iter.hasNext(); ) {
                Language l = iter.next();
                ps.println("copy \"" + context.getLanguageFile(l) + "\" work\\t\\" + l.getCodeNumber() + "3303.xml");
            }
            ps.println(x2build + " " + m + " work");
            ps.println(gzip + " <\"" + context.getGalaxyMapFile().getFile().getAbsolutePath() + "\" >work2\\cut\\00749.txt.gz");
            ps.println(x2tool + " -crypt work2\\cut\\00749.txt.gz work2\\cut\\00749.pbd");
            ps.println("del work2\\cut\\00749.txt.gz");
            ps.println(x2build + " " + baseCatalog + " work2");
            ps.println("rmdir /S /Q work2");
            ps.println("rmdir /S /Q work");
            ps.flush();
        } finally {
            if (ps != null) ps.close();
            if (bos != null) bos.close();
            if (fos != null) fos.close();
        }
        filesCreated.add(batchFile);
        try {
            String[] command = { "cmd.exe", "/C", "pack.bat" };
            Process process = runtime.exec(command, null, outputDir);
            process.getOutputStream().close();
            process.getInputStream().close();
            process.getErrorStream().close();
            int res = process.waitFor();
            if (res != 0) throw new IOException("Could not exec 'pack.bat' batch file!");
        } catch (InterruptedException e) {
        }
    }

    public File createDirectory(File parentDir, String child) throws IOException {
        File d = new File(parentDir, child);
        if (!d.mkdir()) throw new IOException(textManager.getString("couldNotCreateDirectory") + d.getPath());
        return d;
    }

    /**
     * Copy a file from source to destination using channels. Based on code
     * provided on http://snippets.dzone.com/posts/show/4946.
     * 
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void copyFile(File source, File destination) throws IOException {
        FileChannel srcChannel = new FileInputStream(source).getChannel();
        FileChannel destChannel = new FileOutputStream(destination).getChannel();
        try {
            int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            long size = srcChannel.size();
            long position = 0;
            while (position < size) {
                position += srcChannel.transferTo(position, maxCount, destChannel);
            }
        } finally {
            if (srcChannel != null) srcChannel.close();
            if (destChannel != null) destChannel.close();
        }
    }

    /**
     * 
     * @return InstallInformation instance for currently installed universe or
     *         null if no universe is currently installed
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public InstallInformation getCurrentInstallInformation() throws IOException, ParserConfigurationException, SAXException {
        if (!isUniverseInstalled()) return null;
        return InstallInformation.readFromFile(getInstallInformationBaseFile());
    }

    public String[] listAvailableUniverses() {
        File dir = userSettings.getX2InstallPath().getFile();
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && pathname.getName().length() > 4 && pathname.getName().startsWith("xuw-") && (pathname.listFiles().length > 0);
            }
        };
        File[] fileList = dir.listFiles(filter);
        String[] res = new String[fileList.length];
        for (int i = 0; i < fileList.length; i++) {
            res[i] = fileList[i].getName().substring(4);
        }
        return res;
    }

    /**
     * Deletes the universe of given name by recursively deleting the
     * corresponsing "xuw-..." directory and all its files.
     * 
     * @param name
     *            name of the universe to delete
     * @throws Exception
     */
    public void deleteUniverse(String name) throws Exception {
        File universeDir = new File(userSettings.getX2InstallPath().getFile(), "xuw-" + name);
        if (!universeDir.exists() || !universeDir.isDirectory()) throw new Exception("'xuw-" + name + "' does not exist or is not a directory!");
        Queue<File> queue = new LinkedList<File>();
        queue.add(universeDir);
        FileFilter dirFilter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && (!pathname.getName().equals(".")) && (!pathname.getName().equals(".."));
            }
        };
        delTree(universeDir, dirFilter);
        universeDir.delete();
    }

    private static void delTree(File dir, FileFilter dirFilter) {
        for (File subdir : dir.listFiles(dirFilter)) {
            delTree(subdir, dirFilter);
        }
        for (File file : dir.listFiles()) {
            file.delete();
        }
    }
}
