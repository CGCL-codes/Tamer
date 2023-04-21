package com.lars_albrecht.foldergen.core.generator.worker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import com.lars_albrecht.foldergen.plugin.classes.FolderGenPlugin;
import com.lars_albrecht.foldergen.plugin.interfaces.IFolderGenPlugin;

/**
 * Worker to unzip files to filesystem.
 * 
 * Title: ZipWorker<br>
 * Filemarker: ><br>
 * Infomarker: zip<br>
 * 
 * @author lalbrecht
 * 
 */
public class ZipWorker extends FolderGenPlugin {

    public ZipWorker() {
        this.infoMap.put(IFolderGenPlugin.INFO_TITLE, "ZipWorker");
        this.infoMap.put(IFolderGenPlugin.INFO_FILEMARKER, ">");
        this.infoMap.put(IFolderGenPlugin.INFO_INFOMARKER, "zip");
        this.infoMap.put(IFolderGenPlugin.INFO_ADDITIONALKEYS, "src");
    }

    @Override
    public HashMap<String, Object> doWork(final HashMap<String, Object> workerMap) {
        @SuppressWarnings("unchecked") HashMap<String, String> tempAdditionalData = (HashMap<String, String>) workerMap.get("additionalData");
        File rootFolder = (File) workerMap.get("rootFolder");
        String name = (String) workerMap.get("name");
        this.workUnzip(rootFolder, tempAdditionalData, name);
        return null;
    }

    /**
	 * If needed, creates a copy of a file or a file from a HTTP-url.
	 * 
	 * @param rootFolder
	 *            File
	 * @param tempAdditionalData
	 *            HashMap<String,String>
	 * @param name
	 *            String
	 */
    private void workUnzip(final File rootFolder, final HashMap<String, String> tempAdditionalData, final String name) {
        if ((tempAdditionalData.get("src") != null)) {
            if (tempAdditionalData.get("src").endsWith(".zip") && new File(tempAdditionalData.get("src")).exists() && new File(tempAdditionalData.get("src")).isFile()) {
                try {
                    this.extractArchive(new File(tempAdditionalData.get("src")), rootFolder);
                } catch (IOException e) {
                }
            }
        }
    }

    /**
	 * Extract archive to filesystem.
	 * 
	 * @see "http://www.tutorials.de/java/215919-zip-entpacken.html"
	 * @param archive
	 *            File
	 * @param destDir
	 *            File
	 * @throws ZipException
	 * @throws IOException
	 */
    private void extractArchive(final File archive, final File destDir) throws ZipException, IOException {
        ZipFile zipFile = new ZipFile(archive);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        byte[] buffer = new byte[16384];
        int len;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            this.buildDirectoryHierarchyFor(entry.getName(), destDir);
            if (!entry.isDirectory()) {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())));
                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                while ((len = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                bos.close();
                bis.close();
            }
        }
        zipFile.close();
    }

    /**
	 * Create new directory if not exists.
	 * 
	 * @see "http://www.tutorials.de/java/215919-zip-entpacken.html"
	 * @param entryName
	 * @param destDir
	 */
    private void buildDirectoryHierarchyFor(final String entryName, final File destDir) {
        File dir = new File(destDir, entryName.substring(0, entryName.lastIndexOf(File.separator) + 1));
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public String getItemTitle(final HashMap<Integer, String> basicInfo) {
        return null;
    }

    @Override
    public HashMap<String, String> getAdditionlInfo(final HashMap<Integer, String> basicInfo) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("src", basicInfo.get(IFolderGenPlugin.BASICINFO_FILETITLE).trim());
        return tempMap;
    }

    @Override
    public Integer getPluginType() {
        return IFolderGenPlugin.PLUGINTYPE_CONFEXTENSION_FILE;
    }

    @Override
    public String replaceContent(final String content) {
        return null;
    }
}
