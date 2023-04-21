package dismapi;

import dismgui.OutputDialog;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author pucgenie
 */
public class dism {

    private static final String DISMGUIBUNDLE = "dismgui/Bundle";

    private static final String SYSTEMROOT = "SystemRoot";

    private static final String SYSTEM32 = "System32";

    private static final String CMDEXE = System.getenv(SYSTEMROOT) + File.separator + SYSTEM32 + File.separator + "cmd.exe";

    private static final String DISMEXE = System.getenv(SYSTEMROOT) + File.separator + SYSTEM32 + File.separator + "dism.exe";

    private static final String OPTENGLISH = "/English";

    private static final String OPTMOUNTDIR = "/MountDir:";

    private static final Runtime r = Runtime.getRuntime();

    public static boolean simulation = false;

    public static boolean forceOutput = false;

    static {
        System.out.println(java.util.ResourceBundle.getBundle(DISMGUIBUNDLE).getString("CLASS LOADED: DISMAPI.DISM"));
        System.out.println(" " + DISMEXE);
        boolean exist = new File(DISMEXE).exists();
        System.out.println(java.util.ResourceBundle.getBundle(DISMGUIBUNDLE).getString(" DISM.EXE EXISTS: ") + exist);
        if (!exist) {
            JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle(DISMGUIBUNDLE).getString("DISM.EXE NOT FOUND!"));
        }
    }

    /**
     * Mounts the specified WIM with the specified index to the specified working directory.
     * @param wim
     * @param dir
     * @return
     */
    public static void mount(File wim, File dir, int idx, boolean rw) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, "/Mount-Wim", "/WimFile:" + wim.getAbsolutePath(), "/Index:" + idx, OPTMOUNTDIR + dir.getAbsolutePath(), rw ? "" : "/ReadOnly" };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, true);
    }

    /**
     * Unmounts the WIM mounted to the specified working directory.
     * @param dir
     * @param commit Commit changes or not
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void unmount(File dir, boolean commit) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, "/Unmount-Wim", OPTMOUNTDIR + dir.getAbsolutePath(), commit ? "/commit" : "/discard" };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, true);
    }

    /**
     * Remounts the specified image.
     * @param dir
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void remount(File dir) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, "/Remount-Wim", OPTMOUNTDIR + dir.getAbsolutePath() };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, forceOutput);
    }

    /**
     * Saves the changes which were made in the image.
     * @param dir
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void commitChanges(File dir) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, "/Commit-Wim", OPTMOUNTDIR + dir.getAbsolutePath() };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, true);
    }

    /**
     * Returns the infos of the current mounted images.
     * @param dir
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static Vector<MountedInfo> getMountedWIMsInfo() throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, "/Get-MountedWimInfo" };
        Process exec = specialexec(toexec);
        String[] lines = showOutput(exec, toexec, forceOutput);
        Vector<Integer> idxs = getBlockStartIndexes(lines, java.util.ResourceBundle.getBundle(DISMGUIBUNDLE).getString("MOUNT DIR : "), 4);
        Vector<MountedInfo> m = new Vector<MountedInfo>();
        for (Integer ix : idxs) {
            m.add(MountedInfo.parse(lines, ix));
        }
        return m;
    }

    /**
     * Returns the index-infos of the specified WIM.
     * @param wim
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static Vector<WIMInfo> getWIMInfo(File wim) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, "/Get-WimInfo", "/WimFile:" + wim.getAbsolutePath() };
        Process exec = specialexec(toexec);
        String[] lines = showOutput(exec, toexec, forceOutput);
        Vector<Integer> idxs = getBlockStartIndexes(lines, java.util.ResourceBundle.getBundle(DISMGUIBUNDLE).getString("INDEX : "), 3);
        Vector<WIMInfo> m = new Vector<WIMInfo>();
        for (Integer ix : idxs) {
            m.add(WIMInfo.parse(lines, ix));
        }
        return m;
    }

    /**
     * Changes the default product key of the specified image.
     * Only for offline (WIM) images!
     * @param dir
     * @param key
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void setProductKey(File dir, String key) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, "/Image:" + dir.getAbsolutePath(), "/Set-ProductKey:" + key };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, forceOutput);
    }

    /**
     * Executes the process and shows the forceOutput-dialog just-in-time.
     * @param exec
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static String[] showOutput(Process exec, String[] cmdline, boolean dlgv) throws IOException, InterruptedException {
        StringBuilder strb = new StringBuilder();
        BufferedInputStream bis = new BufferedInputStream(exec.getInputStream());
        final OutputStream os = exec.getOutputStream();
        os.write('N');
        os.write('\r');
        os.write('\n');
        OutputDialog dlg = null;
        if (dlgv) {
            dlg = new OutputDialog();
            dlg.setVisible(true);
            for (String s : cmdline) {
                dlg.taOut.append(s + " ");
            }
            dlg.taOut.append(java.util.ResourceBundle.getBundle(DISMGUIBUNDLE).getString(":: OUTPUT"));
        }
        int c;
        while ((c = bis.read()) != -1) {
            if (dlgv) {
                dlg.taOut.append(String.valueOf((char) c));
            }
            strb.append((char) c);
        }
        bis.close();
        if (dlgv) {
            dlg.closeWithDelay();
        }
        return strb.toString().split(System.getProperty("line.separator"));
    }

    /**
     * Returns a collection of indexes of every line which starts with startOfBlock.
     * If startOfBlock was found, the next blockLenght lines are skipped.
     * @param lines
     * @param startOfBlock
     * @param blockLength
     * @return
     */
    private static Vector<Integer> getBlockStartIndexes(String[] lines, String startOfBlock, int blockLength) {
        Vector<Integer> idxs = new Vector<Integer>();
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith(startOfBlock)) {
                idxs.add(i);
                i += blockLength;
            }
        }
        return idxs;
    }

    /**
     * Returns a collection of all packages that the specified image contains.
     * @param wim
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static Vector<PackageShort> getPackages(File dir) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, dir != null ? "/Image:" + dir.getAbsolutePath() : "/Online", "/Get-Packages" };
        Process exec = specialexec(toexec);
        String[] lines = showOutput(exec, toexec, forceOutput);
        Vector<Integer> idxs = getBlockStartIndexes(lines, java.util.ResourceBundle.getBundle(DISMGUIBUNDLE).getString("PACKAGE IDENTITY : "), 4);
        Vector<PackageShort> m = new Vector<PackageShort>();
        for (Integer ix : idxs) {
            m.add(PackageShort.parse(lines, ix));
        }
        return m;
    }

    /**
     * Returns a collection of all features that the specified image contains.
     * @param wim
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static Vector<FeatureShort> getFeatures(File dir) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, dir != null ? "/Image:" + dir.getAbsolutePath() : "/Online", "/Get-Features" };
        Process exec = specialexec(toexec);
        String[] lines = showOutput(exec, toexec, forceOutput);
        Vector<Integer> idxs = getBlockStartIndexes(lines, java.util.ResourceBundle.getBundle(DISMGUIBUNDLE).getString("FEATURE NAME : "), 2);
        Vector<FeatureShort> m = new Vector<FeatureShort>();
        for (Integer ix : idxs) {
            m.add(FeatureShort.parse(lines, ix));
        }
        return m;
    }

    /**
     * Disables a feature in the image.
     * @param dir
     * @param fid
     * @throws IOException
     * @throws InterruptedException
     */
    public static void disableFeature(File dir, String fid) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, dir != null ? "/Image:" + dir.getAbsolutePath() : "/Online", "/Disable-Feature", "/FeatureName:" + fid };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, true);
    }

    /**
     * Enables a feature in the image.
     * @param dir
     * @param fid
     * @throws IOException
     * @throws InterruptedException
     */
    public static void enableFeature(File dir, String fid) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, dir != null ? "/Image:" + dir.getAbsolutePath() : "/Online", "/Enable-Feature", "/FeatureName:" + fid };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, true);
    }

    /**
     * Removes a package from the image.
     * @param dir
     * @param pid
     * @throws IOException
     * @throws InterruptedException
     */
    public static void removePackage(File dir, String pid) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, dir != null ? "/Image:" + dir.getAbsolutePath() : "/Online", "/Remove-Package", "/PackageName:" + pid };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, true);
    }

    /**
     * Fügt ein/mehrere Package/s hinzu.
     * @param dir
     * @param toadd
     * @param recurse
     * @throws IOException
     * @throws InterruptedException
     */
    public static void addPackage(File dir, File toadd, boolean check) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, dir != null ? "/Image:" + dir.getAbsolutePath() : "/Online", "/Add-Package", "/PackagePath:" + toadd.getCanonicalPath(), check ? "" : "/IgnoreCheck" };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, true);
    }

    /**
     * Fügt ein/mehrere Treiber hinzu.
     * @param dir
     * @param toadd
     * @param recurse
     * @throws IOException
     * @throws InterruptedException
     */
    public static void addDriver(File dir, File toadd, boolean recurse, boolean forceUnsigned) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, dir != null ? "/Image:" + dir.getAbsolutePath() : "/Online", "/Add-Driver", "/Driver:" + toadd.getCanonicalPath(), recurse ? "/recurse" : "", forceUnsigned ? "/forceUnsigned" : "" };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, true);
    }

    /**
     * Returns a collection of all drivers that the specified image contains.
     * @param wim
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static Vector<DriverShort> getDrivers(File dir) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, dir != null ? "/Image:" + dir.getAbsolutePath() : "/Online", "/Get-Drivers" };
        Process exec = specialexec(toexec);
        String[] lines = showOutput(exec, toexec, forceOutput);
        Vector<Integer> idxs = getBlockStartIndexes(lines, java.util.ResourceBundle.getBundle(DISMGUIBUNDLE).getString("PUBLISHED NAME : "), 7);
        Vector<DriverShort> m = new Vector<DriverShort>();
        for (Integer ix : idxs) {
            m.add(DriverShort.parse(lines, ix));
        }
        return m;
    }

    /**
     * Liefert detaillierte Treiber-Informationen.
     * @param dir
     * @param driverName
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static Vector<DriverFull> getDriverInfo(File dir, String driverName) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, dir != null ? "/Image:" + dir.getAbsolutePath() : "/Online", "/Get-DriverInfo", "/Driver:" + driverName };
        Process exec = specialexec(toexec);
        String[] lines = showOutput(exec, toexec, true);
        Vector<Integer> idxs = getBlockStartIndexes(lines, java.util.ResourceBundle.getBundle(DISMGUIBUNDLE).getString("PUBLISHED NAME : "), 7);
        Vector<DriverFull> m = new Vector<DriverFull>();
        for (Integer ix : idxs) {
            m.add(DriverFull.parse(lines, ix));
        }
        return m;
    }

    /**
     * Removes a driver from the image.
     * @param dir
     * @param dinf
     * @throws IOException
     * @throws InterruptedException
     */
    public static void removeDriver(File dir, String dinf) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, dir != null ? "/Image:" + dir.getAbsolutePath() : "/Online", "/Remove-Driver", "/Driver:" + dinf };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, true);
    }

    /**
     * TODO: Bessere Beschreibung.
     * @param dir
     * @throws IOException
     * @throws InterruptedException
     */
    public static void cleanupImage(File dir) throws IOException, InterruptedException {
        String[] toexec = new String[] { DISMEXE, OPTENGLISH, dir != null ? "/Image:" + dir.getAbsolutePath() : "/Online", "/Cleanup-Image", "/RevertPendingActions" };
        Process exec = specialexec(toexec);
        showOutput(exec, toexec, true);
    }

    /**
     * Führt den Befehl in der CMD aus.
     * @param toexec
     * @return
     * @throws IOException
     */
    private static Process specialexec(String[] toexec) throws IOException {
        return r.exec(simulation ? new String[] { CMDEXE, "/c", java.util.ResourceBundle.getBundle(DISMGUIBUNDLE).getString("ECHO.SIMULATION MODE. NO CHANGES.") } : toexec);
    }

    /**
     * TODO: Ausimplementierung.
     */
    public static void unhidePackages() {
    }
}
