package eyeswindle.tools;

import java.io.IOException;
import java.io.RandomAccessFile;

final class BifReader {

    private static final String SIG = "BIFF";

    private static final String VER = "V1  ";

    static Bif readBif(String filename) throws IOException {
        RandomAccessFile fPtr = new RandomAccessFile(filename, "r");
        String signature = Util.readString(fPtr, 4);
        String version = Util.readString(fPtr, 4);
        if (!signature.equals(SIG) || !version.equals(VER)) {
            throw new IOException("Invalid .bif file");
        }
        int fileCount = Util.readInt(fPtr);
        int tilesetCount = Util.readInt(fPtr);
        long fileTableOffset = Util.readUnsignedInt(fPtr);
        fPtr.seek(fileTableOffset);
        FileEntry[] fileEntries = new FileEntry[fileCount];
        for (int i = 0; i < fileCount; i++) {
            int resourceLocator = Util.readInt(fPtr);
            long offset = Util.readUnsignedInt(fPtr);
            int fileSize = Util.readInt(fPtr);
            short resourceType = Util.readShort(fPtr);
            short unknown = Util.readShort(fPtr);
            fileEntries[i] = new FileEntry(resourceLocator, offset, fileSize, resourceType, unknown);
        }
        TilesetEntry[] tilesetEntries = new TilesetEntry[tilesetCount];
        for (int i = 0; i < tilesetCount; i++) {
            int resourceLocator = Util.readInt(fPtr);
            long offset = Util.readUnsignedInt(fPtr);
            int numTiles = Util.readInt(fPtr);
            int resourceSizeInBytes = Util.readInt(fPtr);
            short resourceType = Util.readShort(fPtr);
            short unknown = Util.readShort(fPtr);
            tilesetEntries[i] = new TilesetEntry(resourceLocator, offset, numTiles, resourceSizeInBytes, resourceType, unknown);
        }
        fPtr.close();
        return new Bif(signature, version, fileEntries, tilesetEntries);
    }
}
