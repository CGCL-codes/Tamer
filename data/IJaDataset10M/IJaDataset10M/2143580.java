package com.luxmedien.jbox.filemanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import com.luxmedien.jbox.filemanagement.JboxConfigFile.Reason;
import com.luxmedien.jbox.filemanagement.JboxConfigFile.SyncStatus;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class FileManager {

    private XStream xstream = new XStream(new DomDriver("utf-8"));

    public FileManager() {
    }

    private File jBoxHome;

    private List<File> directoryList = new ArrayList<File>();

    Logger log = Logger.getLogger("FileManager");

    public FileManager(String pDefaultDirectory) {
        jBoxHome = new File(pDefaultDirectory);
        if (!checkDirectory(jBoxHome)) {
            log.error("Check your jbox.conf! jbox home isn't set correctly... Exiting...");
            System.exit(1);
        }
    }

    public int getLastFileCount(File pDirectory) {
        File lCount = new File(pDirectory, ".jboxcount");
        String lFileContents = "0";
        if (lCount.exists()) {
            try {
                BufferedReader lFileReader = new BufferedReader(new FileReader(lCount));
                while (lFileReader.ready()) {
                    lFileContents = lFileReader.readLine();
                    System.out.println(lFileContents);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Integer.parseInt(lFileContents);
    }

    public void createJboxHome(File pJBoxHomeDir) {
        File lnewDir = new File(pJBoxHomeDir, "pictures");
        lnewDir.mkdir();
        log.info("You didn't specify a jbox.home directory in jbox.conf, so I created one for you.");
        log.info("Create directories in " + lnewDir.getAbsolutePath() + "and simply upload images into them.");
    }

    /**
	 * This creates initial resources
	 * on the filesystem, so jBox can
	 * reference the js libraries in
	 * the albums
	 * @param pRootDirectory the home directory in which the resources can be found
	 * @param pJBoxHomeDir the jBox home, where the albums are and the resources are being copied to.
	 */
    public void createInitialResources(File pRootDirectory, File pJBoxHomeDir) throws IOException {
        log.info("Importing resources from " + pRootDirectory.getAbsolutePath() + " to " + pJBoxHomeDir.getAbsolutePath());
        File lResourcesDirectory = new File(pRootDirectory, "resources");
        log.info("ResourcesDir: " + lResourcesDirectory.isDirectory());
        boolean lFoundCSS = false;
        boolean lFoundImages = false;
        boolean lFoundJS = false;
        if (lResourcesDirectory.isDirectory()) {
            for (final File lDir : lResourcesDirectory.listFiles()) {
                if (lDir.isDirectory()) {
                    if (lDir.getName().equalsIgnoreCase("css")) {
                        if (!(new File(pJBoxHomeDir, lDir.getName()).exists())) {
                            FileUtils.copyDirectory(lDir, new File(pJBoxHomeDir, lDir.getName()));
                            log.info("Copied CSS directory to destination...");
                        } else {
                            log.info(new File(pJBoxHomeDir, lDir.getName()).getAbsolutePath() + " exists!");
                        }
                        lFoundCSS = true;
                    }
                    if (lDir.getName().equalsIgnoreCase("images")) {
                        if (!(new File(pJBoxHomeDir, lDir.getName()).exists())) {
                            FileUtils.copyDirectory(lDir, new File(pJBoxHomeDir, lDir.getName()));
                            log.info("Copied Images directory to destination...");
                        } else {
                            log.info(new File(pJBoxHomeDir, lDir.getName()).getAbsolutePath() + " exists!");
                        }
                        lFoundImages = true;
                    }
                    if (lDir.getName().equalsIgnoreCase("js")) {
                        if (!(new File(pJBoxHomeDir, lDir.getName()).exists())) {
                            FileUtils.copyDirectory(lDir, new File(pJBoxHomeDir, lDir.getName()));
                            log.info("Copied JS directory to destination...");
                        } else {
                            log.info(new File(pJBoxHomeDir, lDir.getName()).getAbsolutePath() + " exists!");
                        }
                        lFoundJS = true;
                    }
                }
            }
        }
        if (lFoundCSS && lFoundImages && lFoundJS) {
            log.info("Found directory structure as needed... Installed files to gallery!");
        } else {
            log.error("Missing directories! Please unzip lightbox-zipfile to resources/ directory.");
            System.exit(1);
        }
    }

    public Properties readAlbumInformation(File pDirectory) {
        Properties albumProperties = null;
        if (pDirectory.isDirectory()) try {
            log.trace("reading album information from " + pDirectory.getAbsolutePath());
            albumProperties = new Properties();
            albumProperties.load(new FileInputStream(new File(pDirectory, ".album")));
        } catch (FileNotFoundException e) {
            log.warn("Did not find album information. Creating default file.");
            writeAlbumInformation(pDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        } else {
            log.error(pDirectory.getAbsolutePath() + " is not a directory!");
        }
        return albumProperties;
    }

    public void saveConfigFile(JboxConfigFile pConfigFile) {
        log.debug("Saving configuration file: " + pConfigFile.getFullPathToFile() + " with SyncStatus " + pConfigFile.getStatus());
        try {
            File jboxconfig = new File(pConfigFile.getFullPathToFile());
            xstream.toXML(pConfigFile, new FileOutputStream(jboxconfig));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAlbumInformation(File pDirectory) {
        Properties albumProperties = null;
        File lPropertiesFile = new File(pDirectory, ".album");
        if (lPropertiesFile.exists()) {
            return;
        }
        if (pDirectory.isDirectory()) try {
            log.info("writing album information to " + pDirectory.getAbsolutePath());
            albumProperties = new Properties();
            albumProperties.put("photographer", "John Doe");
            albumProperties.put("location", "New York, NY");
            albumProperties.put("date", "11.9.2001");
            albumProperties.put("name", "Airplane photography");
            albumProperties.store(new FileOutputStream(new File(pDirectory, ".album")), "jBox Album Information.");
        } catch (IOException e) {
            e.printStackTrace();
        } else {
            log.error(pDirectory.getAbsolutePath() + " is not a directory!");
        }
    }

    public List<File> getRootDirectory() {
        return null;
    }

    public void writeFile(File pFile, String pContents) {
        try {
            log.info("writing new file to: " + pFile.getAbsolutePath());
            log.debug("File already existed: " + pFile.createNewFile());
            FileWriter fw = new FileWriter(pFile);
            fw.write(pContents);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<File> getAlbumDirectories() {
        directoryList.clear();
        for (final File lDir : jBoxHome.listFiles(new DirectoryFilter())) {
            directoryList.add(lDir);
        }
        return directoryList;
    }

    private JboxConfigFile createBasicConfiguration(File pDirectory) {
        JboxConfigFile lConfig = new JboxConfigFile();
        lConfig.setDirectoryName(pDirectory.getAbsolutePath());
        lConfig.setFullPathToFile(pDirectory.getAbsolutePath() + "/jboxInfo.xml");
        lConfig.setStatus(SyncStatus.NEW_DIRECTORY);
        Set<ImageFileInformation> lFileList = new HashSet<ImageFileInformation>();
        lConfig.setFileList(lFileList);
        return lConfig;
    }

    private ImageFileInformation createImageFileInformation(File pImageFile) {
        ImageFileInformation lImageInfo = new ImageFileInformation();
        lImageInfo.setFilename(pImageFile.getName());
        lImageInfo.setFilesize(pImageFile.length());
        lImageInfo.setLastupdated(pImageFile.lastModified());
        return lImageInfo;
    }

    public ImageDirectory getPreparedImageDir(String pDirectory) {
        ImageDirectory lResult = new ImageDirectory();
        JboxConfigFile lJboxConfiguration = null;
        File lDirectory = new File(pDirectory);
        for (final File lConfigFile : lDirectory.listFiles(new ConfigFileFilter())) {
            try {
                lJboxConfiguration = (JboxConfigFile) xstream.fromXML(new FileInputStream(lConfigFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (lJboxConfiguration == null) {
            log.debug("File jboxInfo.xml not found. Initializing new directory.");
            lJboxConfiguration = createBasicConfiguration(lDirectory);
            try {
                File jboxconfig = new File(lDirectory, "jboxInfo.xml");
                jboxconfig.createNewFile();
                xstream.toXML(lJboxConfiguration, new FileOutputStream(jboxconfig));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        lResult.setConfig(lJboxConfiguration);
        if (lDirectory.listFiles(new ImageFilter()).length < lResult.getConfig().getLastFileCount()) {
            log.debug("Seems like files have been deleted. Setting Reason to DELETED_FILES.");
            lResult.getConfig().setReason(Reason.DELETED_FILES);
            lResult.getConfig().setStatus(SyncStatus.OUT_OF_SYNC);
            saveConfigFile(lResult.getConfig());
        }
        for (final File lFile : lDirectory.listFiles(new ImageFilter())) {
            ImageFileInformation lCompareMeToListObject = new ImageFileInformation();
            lCompareMeToListObject.setFilename(lFile.getName());
            lCompareMeToListObject.setFilesize(lFile.length());
            lCompareMeToListObject.setLastupdated(lFile.lastModified());
            for (ImageFileInformation info : lJboxConfiguration.getFileList()) {
                if (info.getFilename().equalsIgnoreCase(lCompareMeToListObject.getFilename())) {
                    log.debug("File 1: " + info.toString());
                    log.debug("File 2: " + lCompareMeToListObject.toString());
                }
            }
            if (!lResult.getConfig().getFileList().contains(lCompareMeToListObject)) {
                log.debug("Found new/updated file: " + lCompareMeToListObject.getFilename() + " size: " + lCompareMeToListObject.getFilesize() + " timestamp: " + lCompareMeToListObject.getLastupdated());
                lResult.addResizeImage(lFile);
                lResult.addImage(lFile);
                lResult.getConfig().setReason(Reason.NEW_FILES);
                lResult.getConfig().setStatus(SyncStatus.OUT_OF_SYNC);
            } else {
                lResult.addImage(lFile);
            }
        }
        if (lResult.getConfig().getReason() != null && lResult.getConfig().getReason().equals(Reason.DELETED_FILES) && lResult.getConfig().getStatus().equals(SyncStatus.OUT_OF_SYNC)) {
            log.debug("Removing deleted files from config. Listsize: " + lResult.getConfig().getLastFileCount());
            lResult.getConfig().getFileList().clear();
            for (File imageFile : lResult.getImages()) {
                ImageFileInformation lAddMeToNewList = new ImageFileInformation();
                lAddMeToNewList.setFilename(imageFile.getName());
                lAddMeToNewList.setFilesize(imageFile.length());
                lAddMeToNewList.setLastupdated(imageFile.lastModified());
                lResult.getConfig().getFileList().add(lAddMeToNewList);
            }
            log.debug("Saving new filelist of length: " + lResult.getConfig().getLastFileCount());
            saveConfigFile(lResult.getConfig());
        }
        if (lResult.getResizeList().size() > 0) {
            saveConfigFile(lResult.getConfig());
        }
        log.debug("Returning " + lResult.getConfig().getDirectoryName() + " with status " + lResult.getConfig().getStatus() + " because of " + lResult.getConfig().getReason());
        return lResult;
    }

    private ImageDirectory updateConfigFile(File pConfigFile, ImageDirectory pImageDir) {
        return pImageDir;
    }

    private boolean checkDirectory(File pDirectory) {
        boolean lSuccess = true;
        if (!pDirectory.exists()) {
            log.error("Couldn't find " + pDirectory);
            lSuccess = false;
        }
        if (!pDirectory.canRead()) {
            log.error("Check permissions. No read access to " + pDirectory);
            lSuccess = false;
        }
        if (!pDirectory.canWrite()) {
            log.error("Check permissions. No write access to " + pDirectory);
            lSuccess = false;
        }
        if (!pDirectory.isDirectory()) {
            log.error(pDirectory + " is not a directory!");
            lSuccess = false;
        }
        return lSuccess;
    }
}
