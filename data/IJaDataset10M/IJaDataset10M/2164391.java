package spacewars.versionreleaser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class VersionRelease {

    static String version;

    static File baseDir;

    static File destDir;

    static File FileDestDir;

    static File installDestDir;

    static File JarDestDir;

    static File keyStore;

    static String keyPassword;

    static String keyAlias;

    public static void main(String[] args) throws Exception {
        Properties versionProp = new Properties();
        versionProp.load(new FileInputStream("config/mainMessages.txt"));
        version = versionProp.getProperty("version", "SpaceWars-X.X");
        baseDir = new File(".");
        destDir = new File("../Releases/" + version);
        FileDestDir = new File(destDir, "files");
        JarDestDir = new File(destDir, "jars");
        installDestDir = new File(destDir, "installer");
        keyStore = new File("../spacewarskey");
        keyPassword = "123456";
        keyAlias = "spacewarsjws";
        deleteDir(destDir);
        destDir.mkdirs();
        System.out.println("Selecting Files...");
        processDir(baseDir, new Properties());
        geraPacotes();
    }

    public static void geraIzPack() throws IOException {
        System.out.println("Generating Installer...");
        File destInstallFile = new File(installDestDir, version + "_installer.jar");
        destInstallFile.getParentFile().mkdirs();
        String[] command = { new File(baseDir, "install.xml").getCanonicalPath(), "-b", FileDestDir.getCanonicalPath(), "-o", destInstallFile.getCanonicalPath(), "-k", "standard" };
    }

    public static void geraPacotes() throws IOException, InterruptedException {
        Properties packs = new Properties();
        packs.load(new FileInputStream("releasePacks"));
        for (Object packName : packs.keySet()) {
            System.out.println("Packing " + packName + "...");
            File zipFile = new File(destDir, packName.toString());
            zipFile.getParentFile().mkdirs();
            ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(zipFile));
            String regex = makeRegex(packs.getProperty(packName.toString()));
            for (File f : FileDestDir.listFiles()) {
                String filename = f.getName();
                if (filename.matches(regex)) addToZip(f, zip);
            }
            zip.close();
            String[] commandLine = { "C:/Arquivos de programas/Java/jdk1.6.0_03/bin/jarsigner", "-storepass", keyPassword, "-keystore", keyStore.getCanonicalPath(), zipFile.getCanonicalPath(), keyAlias };
            System.out.println("Assinando Arquivo " + zipFile);
            Process signProcess = Runtime.getRuntime().exec(commandLine);
            signProcess.waitFor();
            BufferedReader pIn = new BufferedReader(new InputStreamReader(signProcess.getInputStream()));
            BufferedReader pErr = new BufferedReader(new InputStreamReader(signProcess.getErrorStream()));
            while (pIn.ready()) {
                System.out.println(pIn.readLine());
            }
            while (pErr.ready()) {
                System.out.println(pErr.readLine());
            }
        }
    }

    public static void addToZip(File f, ZipOutputStream zip) throws IOException {
        if (f.isDirectory()) {
            for (File s : f.listFiles()) {
                addToZip(s, zip);
            }
        } else {
            String baseName = FileDestDir.getCanonicalPath();
            String fullPath = f.getCanonicalPath();
            String nameSufix = fullPath.substring(baseName.length() + 1);
            FileInputStream in = new FileInputStream(f);
            byte[] data = new byte[in.available()];
            in.read(data);
            ZipEntry entry = new ZipEntry(nameSufix.replace('\\', '/'));
            entry.setSize(data.length);
            entry.setTime(f.lastModified());
            zip.putNextEntry(entry);
            zip.write(data);
            zip.closeEntry();
        }
    }

    private static void deleteDir(File folder) {
        try {
            for (File f : folder.listFiles()) {
                if (f.isFile()) f.delete(); else deleteDir(f);
            }
            folder.delete();
        } catch (Exception e) {
        }
    }

    private static void processDir(File dir, Properties tmpfolderProperties) {
        Properties folderProperties = new Properties();
        folderProperties.putAll(tmpfolderProperties);
        folderProperties.setProperty("Folders", "*");
        if (folderProperties.containsKey("SubFolderFiles")) {
            folderProperties.setProperty("Files", folderProperties.getProperty("SubFolderFiles"));
            folderProperties.remove("SubFolderFiles");
        }
        try {
            folderProperties.load(new FileInputStream(new File(dir, "releaseFiles")));
        } catch (Exception e) {
        }
        String folderRegex = makeRegex(folderProperties.getProperty("Folders", "*"));
        String fileRegex = makeRegex(folderProperties.getProperty("Files", "*"));
        for (File f : dir.listFiles()) {
            if (f.getName().startsWith(".")) continue;
            if (f.isFile()) {
                if (f.getName().matches(fileRegex)) copyFile(f);
            } else {
                if (f.getName().equals("releaseFiles")) continue;
                if (f.getName().matches(folderRegex)) processDir(f, folderProperties);
            }
        }
    }

    private static void copyFile(File f) {
        try {
            String baseName = baseDir.getCanonicalPath();
            String fullPath = f.getCanonicalPath();
            String nameSufix = fullPath.substring(baseName.length() + 1);
            File destFile = new File(FileDestDir, nameSufix);
            destFile.getParentFile().mkdirs();
            destFile.createNewFile();
            FileChannel fromChannel = new FileInputStream(f).getChannel();
            FileChannel toChannel = new FileOutputStream(destFile).getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
            fromChannel.close();
            toChannel.close();
            destFile.setLastModified(f.lastModified());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static String makeRegex(String regex) {
        String r = "\\Q" + regex.replace("*", "\\E.*\\Q").replace(",", "\\E|\\Q").replace(" ", "") + "\\E";
        return r;
    }
}
