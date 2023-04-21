package sti.installer.setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import sti.installer.tools.MD5Summer;
import sti.installer.tools.SetupFileViewer;

public class SetupMain {

    public static File extractEntry(File path, ZipEntry ze, ZipFile zf) throws IOException, FileNotFoundException {
        String filep;
        if (ze.getName().substring(0, 5).equals("data/")) {
            filep = ze.getName().substring(5, ze.getName().length());
        } else if (ze.getName().substring(0, 6).equals("media/")) {
            filep = ze.getName().substring(6, ze.getName().length());
        } else {
            filep = ze.getName();
        }
        System.out.println(filep);
        FileOutputStream out = null;
        InputStream in = null;
        in = zf.getInputStream(ze);
        File outFile = new File(path, filep);
        out = new FileOutputStream(outFile);
        byte[] buf = new byte[1024];
        while (true) {
            int nRead = in.read(buf, 0, buf.length);
            if (nRead <= 0) break;
            out.write(buf, 0, nRead);
        }
        out.close();
        in.close();
        return outFile;
    }

    public static void deleteTemp() {
        File tmp = new File("tmp_jinst\\");
        File[] tmpdir = tmp.listFiles();
        for (int i = 0; i < tmpdir.length; i++) {
            tmpdir[i].delete();
        }
        tmp.delete();
    }

    public static void main(String args[]) {
        try {
            File jarFile = new File(FileCopyGui.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            ZipFile zf = new ZipFile(jarFile.getName());
            File tmp = new File("tmp_jinst\\");
            if (!tmp.exists()) {
                tmp.mkdir();
            }
            ZipEntry zsetup = zf.getEntry("setupfile.ijs");
            File config = SetupMain.extractEntry(tmp, zsetup, zf);
            ZipEntry headerimg = zf.getEntry("media/stijinstaller.png");
            File img = SetupMain.extractEntry(tmp, headerimg, zf);
            SetupFile setupFile = new SetupFile();
            setupFile = SetupFile.readSetupFile(config);
            String destination = setupFile.getDefaultDestination();
            File destf;
            String title = setupFile.getSetupName() + " : StiJInstaller";
            if (args.length == 1) {
                if (args[0].equals("-info")) {
                    new SetupFileViewer(setupFile);
                    SetupMain.deleteTemp();
                }
            } else {
                SetupFirstGui firstGui = new SetupFirstGui();
                SetupDestGui destGui = new SetupDestGui();
                FileCopyGui copyGui = new FileCopyGui();
                SetupEndGui endGui = new SetupEndGui();
                firstGui.setGuiTitle(title);
                firstGui.setAppName(setupFile.getSetupName() + " " + setupFile.getAppVersion());
                firstGui.setIcon(img);
                firstGui.setGuiVisible(true);
                while (firstGui.isGuiVisible()) {
                }
                destGui.setGuiTitle(title);
                destGui.setIcon(img);
                destGui.setDefaultDestination(destination);
                destGui.setSizeRequired(setupFile.getSize());
                destGui.setGuiVisible(true);
                while (destGui.isGuiVisible()) {
                }
                destination = destGui.getDestination();
                destf = new File(destination);
                if (!destf.exists()) {
                    destf.mkdir();
                }
                copyGui.setGuiTitle(title);
                copyGui.setIcon(img);
                copyGui.setProgressBar(0, (int) (setupFile.getSize() / 1024));
                copyGui.setNbFile(setupFile.getNbFile());
                copyGui.setGuiVisible(true);
                setupFile.mkdirs(destf);
                for (int i = 0; i < setupFile.getNbFile(); i++) {
                    copyGui.setCurrentNFile(i);
                    copyGui.setCurrentFileSize(setupFile.getFileData(i).getSize());
                    String name = "data/" + setupFile.getFileData(i).getRelativePath();
                    name = name.replace('\\', '/');
                    File tmpfile;
                    copyGui.setCurrentFile(setupFile.getFileData(i).getRelativePath());
                    ZipEntry cfile = zf.getEntry(name);
                    if (cfile == null) {
                        System.out.println("null : " + name);
                    } else {
                        tmpfile = SetupMain.extractEntry(destf, cfile, zf);
                    }
                    copyGui.setProgressBarValue(copyGui.getProgressBarValue() + (int) (setupFile.getFileData(i).getSize() / 1024));
                }
                if (setupFile.isDoMD5CheckAfter()) {
                    copyGui.setProgressBar(0, setupFile.getNbFile() - 1);
                    copyGui.setCurrentFile("Checking file integrity...");
                    for (int i = 0; i < setupFile.getNbFile(); i++) {
                        File f = new File(destf, setupFile.getFileData(i).getRelativePath());
                        copyGui.setCurrentNFile(i);
                        copyGui.setCurrentFileSize(setupFile.getFileData(i).getSize());
                        copyGui.setProgressBarValue(i);
                        if (!MD5Summer.MD5Sum(f.getPath()).equals(setupFile.getFileData(i).getMD5())) {
                            copyGui.setCurrentFile("Package broken... Error in the file " + setupFile.getFileData(i).getFilename());
                            break;
                        }
                    }
                }
                copyGui.setGuiVisible(false);
                endGui.setGuiTitle(title);
                endGui.setAppName(setupFile.getSetupName());
                endGui.setIcon(img);
                endGui.setGuiVisible(true);
                while (endGui.isGuiVisible()) {
                }
                SetupMain.deleteTemp();
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
