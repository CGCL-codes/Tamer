package org.pointrel.pointrel20100813.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ArchiveUsingRAM extends ArchiveAbstract {

    public static String archiveType = "ram";

    HashMap<String, byte[]> ramFiles;

    public ArchiveUsingRAM() {
        super();
        System.out.println("Making a RAM archive ");
        ramFiles = new HashMap<String, byte[]>();
    }

    @Override
    public ArrayList<String> basicGetResourceFileReferenceList(String suffix) {
        ArrayList<String> resultList = new ArrayList<String>();
        for (String key : ramFiles.keySet()) {
            if (suffix == null) {
                if (ResourceFileSupport.isValidResourceFileName(key)) {
                    resultList.add(key);
                }
            } else {
                if (ResourceFileSupport.isValidResourceFileNameWithSuffix(key, suffix)) {
                    resultList.add(key);
                }
            }
        }
        return resultList;
    }

    @Override
    String basicAddResourceFile(InputStream inputStream, String extension) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ResourceFileSupport.copyInputStreamToOutputStream(inputStream, outputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
        byte[] bytes = outputStream.toByteArray();
        String resourceFileReference = Standards.getResourceFileReferenceWithSHA256HashAsHexEncodedString(bytes, extension);
        ramFiles.put(resourceFileReference, bytes);
        return resourceFileReference;
    }

    @Override
    public boolean basicRetrieveResourceFile(String resourceFileReference, OutputStream outputStream) {
        byte[] bytes = ramFiles.get(resourceFileReference);
        if (bytes == null) return false;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            ResourceFileSupport.copyInputStreamToOutputStream(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
