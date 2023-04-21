package net.hanjava.alole.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hpsf.ClassID;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/** POIFSFileSystem<->Physical File System */
public class FileSystemConverter {

    private FileSystemConverter() {
    }

    /**
     * @param outputPath
     *            ������ ���丮 Path
     * @throws IOException
     */
    public static void toPhysicalFileSystem(POIFSFileSystem poifs, String outputPath) throws IOException {
        File outputDir = new File(outputPath);
        boolean created = outputDir.mkdirs();
        writeToFileSystem(poifs.getRoot(), outputPath);
    }

    public static POIFSFileSystem toOLEFileSystem(String importDir) throws IOException {
        POIFSFileSystem newFS = new POIFSFileSystem();
        readFromFileSystem(newFS.getRoot(), importDir);
        return newFS;
    }

    private static void readFromFileSystem(DirectoryEntry dirEntry, String importDir) throws IOException {
        String metaInfoPath = importDir + "/" + StorageInfo.FILENAME;
        FileInputStream fis = new FileInputStream(metaInfoPath);
        StorageInfo storageInfo = StorageInfo.load(fis);
        String classID = storageInfo.getClassID();
        if (classID != null) {
            dirEntry.setStorageClsid(parseClassID(classID));
        }
        fis.close();
        ArrayList<String> entryTable = storageInfo.getTable();
        int entryCount = entryTable.size();
        for (int i = 0; i < entryCount; i++) {
            String childPath = importDir + "/" + i;
            File file = new File(childPath);
            String childEntryName = storageInfo.getEntry(i);
            if (file.isDirectory()) {
                DirectoryEntry childDirEntry = dirEntry.createDirectory(childEntryName);
                readFromFileSystem(childDirEntry, childPath);
            } else {
                FileInputStream childIS = new FileInputStream(childPath);
                dirEntry.createDocument(childEntryName, childIS);
                childIS.close();
            }
        }
    }

    private static ClassID parseClassID(String classID) {
        int offsetToRead = 1;
        int baseOffsetToWrite = 0;
        byte[] littleEndian = new byte[16];
        littleEndian[baseOffsetToWrite + 3] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        littleEndian[baseOffsetToWrite + 2] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        littleEndian[baseOffsetToWrite + 1] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        littleEndian[baseOffsetToWrite] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        baseOffsetToWrite += 4;
        offsetToRead++;
        littleEndian[baseOffsetToWrite + 1] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        littleEndian[baseOffsetToWrite] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        baseOffsetToWrite += 2;
        offsetToRead++;
        littleEndian[baseOffsetToWrite + 1] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        littleEndian[baseOffsetToWrite] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        baseOffsetToWrite += 2;
        offsetToRead++;
        littleEndian[baseOffsetToWrite] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        littleEndian[baseOffsetToWrite + 1] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        offsetToRead++;
        littleEndian[baseOffsetToWrite + 2] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        littleEndian[baseOffsetToWrite + 3] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        littleEndian[baseOffsetToWrite + 4] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        littleEndian[baseOffsetToWrite + 5] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        littleEndian[baseOffsetToWrite + 6] = parseByte(classID, offsetToRead);
        offsetToRead += 2;
        littleEndian[baseOffsetToWrite + 7] = parseByte(classID, offsetToRead);
        ClassID newClassID = new ClassID(littleEndian, 0);
        return newClassID;
    }

    private static byte parseByte(String rawString, int offset) {
        return (byte) Integer.parseInt(rawString.substring(offset, offset + 2), 16);
    }

    /**
     * recursive�ϰ� ���鼭 File System���� �Ű� ��´�
     * 
     * @throws IOException
     */
    private static void writeToFileSystem(DirectoryEntry dirEntry, String outputPath) throws IOException {
        StorageInfo storageInfo = new StorageInfo();
        String classID = dirEntry.getStorageClsid().toString();
        storageInfo.setClassID(classID);
        for (Iterator it = dirEntry.getEntries(); it.hasNext(); ) {
            Entry entry = (Entry) it.next();
            int id = storageInfo.addEntry(entry.getName());
            String childPath = outputPath + "/" + id;
            if (entry instanceof DirectoryEntry) {
                DirectoryEntry childDirEntry = (DirectoryEntry) entry;
                File physicalDir = new File(childPath);
                physicalDir.mkdirs();
                writeToFileSystem(childDirEntry, childPath);
            } else {
                DocumentEntry childDocEntry = (DocumentEntry) entry;
                DocumentInputStream dis = new DocumentInputStream(childDocEntry);
                FileOutputStream fos = new FileOutputStream(childPath);
                Utility.copyStreamContents(dis, fos);
                fos.close();
                dis.close();
            }
        }
        FileOutputStream metaInfoOS = new FileOutputStream(outputPath + "/" + StorageInfo.FILENAME);
        storageInfo.save(metaInfoOS);
        metaInfoOS.close();
    }
}
