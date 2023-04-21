package org.bitdrive.file.core.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHashTools {

    private static final int CHUNK_SIZE = 1024 * 1024;

    private static void readAndUpdate(long count, FileInputStream stream, MessageDigest digest) throws IOException {
        int read = 0;
        byte[] buffer = new byte[CHUNK_SIZE];
        while ((read = stream.read(buffer, 0, Math.min((int) count, buffer.length))) != -1) {
            digest.update(buffer, 0, read);
            count -= read;
            if (count == 0) break;
        }
        if (count != 0) {
            throw new IOException("Failed to read all data");
        }
    }

    public static byte[] hashFile(File file) {
        long size = file.length();
        long jump = (long) (size / (float) CHUNK_SIZE);
        MessageDigest digest;
        FileInputStream stream;
        try {
            stream = new FileInputStream(file);
            digest = MessageDigest.getInstance("SHA-256");
            if (size < CHUNK_SIZE * 4) {
                readAndUpdate(size, stream, digest);
            } else {
                if (stream.skip(jump) != jump) return null;
                readAndUpdate(CHUNK_SIZE, stream, digest);
                if (stream.skip(jump - CHUNK_SIZE) != jump - CHUNK_SIZE) return null;
                readAndUpdate(CHUNK_SIZE, stream, digest);
                if (stream.skip(jump - CHUNK_SIZE) != jump - CHUNK_SIZE) return null;
                readAndUpdate(CHUNK_SIZE, stream, digest);
                digest.update(Long.toString(size).getBytes());
            }
            return digest.digest();
        } catch (FileNotFoundException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
