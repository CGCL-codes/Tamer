package jsync.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import jFileLib.text.FileReader;
import jFileLib.text.FileWriter;

public class HashHelper {

    private static final String hashFileName = getHashFileName();

    private static synchronized String getHashFileName() {
        return String.format("." + ConfigHelper.checkSumType());
    }

    public static synchronized String getCheckSum(String path) {
        if (jFileLib.common.Path.isValidePath(path) == false || jFileLib.common.File.exists(path) == false || jFileLib.common.Path.isFile(path) == false || jFileLib.common.File.canRead(path) == false) return null;
        return HashHelper.getCheckSum(new File(path));
    }

    public static synchronized String getCheckSum(File file) {
        if (file.getName().equals(hashFileName)) return null;
        return getHashString(file, ConfigHelper.checkSumType());
    }

    private static synchronized String getHashString(File file, String hashType) {
        try {
            if (file.exists() == false || file.canRead() == false) {
                return null;
            }
            return getMessageDiggetString(file, hashType);
        } catch (Exception e) {
            LogHelper.writeException(e);
        }
        return null;
    }

    private static synchronized String getMessageDiggetString(File file, String algorithm) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        if (file.exists() == false || file.canRead() == false) {
            return null;
        }
        MessageDigest md = MessageDigest.getInstance(algorithm);
        String hash = "";
        int bufferSize = 1024;
        if (file.length() < Integer.MAX_VALUE) bufferSize = (int) file.length(); else bufferSize = Integer.MAX_VALUE;
        byte[] data = new byte[bufferSize];
        FileInputStream fis = new FileInputStream(file);
        for (int n = 0; (n = fis.read(data)) > -1; ) md.update(data, 0, n);
        fis.close();
        byte[] digest = md.digest();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(digest[i]);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            hex = hex.substring(hex.length() - 2);
            hash += hex;
        }
        return hash;
    }

    public static synchronized boolean writeHashToFile(String filePath, String name, String hash) {
        if (name.equals(hashFileName) || ConfigHelper.useHashFile() == false) return true;
        String fullPath = jFileLib.common.Path.getCorrectPath(filePath) + hashFileName;
        List<String> lines = FileReader.readFile(filePath);
        List<String> newLines = new ArrayList<String>();
        boolean foundEntry = false;
        for (String line : lines) {
            String[] parts = line.split(";");
            String fileName = parts[0];
            String fileHash = parts[1];
            String newLine = line;
            if (fileName.equals(name) == true) {
                foundEntry = true;
                if (fileHash.equals(hash) == false) {
                    newLine = String.format("%s;%s", name, hash);
                }
            }
            newLines.add(newLine);
        }
        if (foundEntry == false) {
            newLines.add(String.format("%s;%s", name, hash));
        }
        return FileWriter.writeLinesToFile(fullPath, newLines);
    }

    public static synchronized String getHashFromFile(String filePath, String name) {
        if (name.equals(hashFileName)) return null;
        String fullPath = jFileLib.common.Path.getCorrectPath(filePath) + hashFileName;
        List<String> lines = FileReader.readFile(fullPath);
        String hash = null;
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts == null) continue;
            String fileName = parts[0];
            String fileHash = parts[1];
            if (fileName.equals(name) == true) {
                hash = fileHash;
            }
        }
        return hash;
    }

    public static synchronized boolean isHashFile(String fileName) {
        return fileName.equals(hashFileName);
    }

    public static synchronized void cleanHashFile(String path) {
        String hashFilePath = jFileLib.common.Path.getCorrectPath(path) + hashFileName;
        File hashFile = new File(hashFilePath);
        if (hashFile.exists() == false) return;
        if (hashFile.canRead() == false || hashFile.canWrite() == false) return;
        File parent = new File(path);
        File[] files = parent.listFiles();
        List<String> lines = new ArrayList<String>();
        for (File file : files) {
            if (file.isFile() == true) {
                String name = file.getName();
                if (name.equals(hashFileName) == false) {
                    String hash = HashHelper.getHashFromFile(path, name);
                    String line = String.format("%s;%s", name, hash);
                    lines.add(line);
                }
            }
        }
        FileWriter.writeLinesToFile(hashFilePath, lines);
    }
}
