package org.one.stone.soup.wiki.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.one.stone.soup.file.FileHelper;
import org.one.stone.soup.wiki.file.manager.ResourceFolder;
import org.one.stone.soup.wiki.file.manager.ResourceStore;

public class WikiUnZipper {

    private ZipInputStream zipInputStream;

    private String[] excludeList = null;

    public WikiUnZipper(InputStream iStream) throws IOException {
        zipInputStream = new ZipInputStream(iStream);
    }

    public void unzipAll(ResourceStore resourceStore, ResourceFolder targetFolder) throws IOException {
        ZipEntry entry = zipInputStream.getNextEntry();
        while (entry != null) {
            if (isExcluded(entry.getName())) {
                entry = zipInputStream.getNextEntry();
                continue;
            }
            if (entry.isDirectory()) {
                resourceStore.getResourceFolder(targetFolder + "/" + entry.getName(), true);
            } else {
                resourceStore.buildResource(resourceStore.getResourceFolder(targetFolder.getPath(), true), entry.getName(), zipInputStream, entry.getSize());
            }
            entry = zipInputStream.getNextEntry();
        }
    }

    public void setExcludeList(String[] excludeList) {
        this.excludeList = excludeList;
    }

    private boolean isExcluded(String entryName) {
        if (excludeList == null) {
            return false;
        }
        for (int loop = 0; loop < excludeList.length; loop++) {
            if (entryName.indexOf(excludeList[loop]) == 0) {
                return true;
            }
        }
        return false;
    }
}
