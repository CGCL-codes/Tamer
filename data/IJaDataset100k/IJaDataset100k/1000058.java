package apple.core;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class DiskImage {

    private static final int NUMBER_TRACKS_PER_DISK = 35;

    private static final int NUMBER_SECTORS_PER_TRACK = 16;

    private static final int NUMBER_BYTES_PER_SECTOR = 256;

    private static final int DISK_IMAGE_TRACK_LENGTH = NUMBER_SECTORS_PER_TRACK * NUMBER_BYTES_PER_SECTOR;

    private static final byte DEFAULT_VOLUME = (byte) 254;

    private static final byte SYNC_BYTE = (byte) 0xFF;

    private static final byte PROLOGUE1 = (byte) 0xD5;

    private static final byte PROLOGUE2 = (byte) 0xAA;

    private static final byte ADDRESS_PROLOGUE3 = (byte) 0x96;

    private static final byte DATA_PROLOGUE3 = (byte) 0xAD;

    private static final byte EPILOGUE1 = (byte) 0xDE;

    private static final byte EPILOGUE2 = (byte) 0xAA;

    private static final byte EPILOGUE3 = (byte) 0xEB;

    private static final int ADDRESS_BLOCK_LENGTH = 3 + 8 + 3;

    private static final int DATA_BLOCK_LENGTH = 3 + 342 + 1 + 3;

    private static final int NUMBER_GAP1_SYNC_BYTES = 528;

    private static final int NUMBER_GAP2_SYNC_BYTES = 6;

    private static final int NUMBER_GAP3_SYNC_BYTES = 14;

    private static final int NIBBLIZED_SECTOR_LENGTH = ADDRESS_BLOCK_LENGTH + NUMBER_GAP2_SYNC_BYTES + DATA_BLOCK_LENGTH + NUMBER_GAP3_SYNC_BYTES;

    private static final int NIBBLIZED_TRACK_LENGTH = NUMBER_GAP1_SYNC_BYTES + NIBBLIZED_SECTOR_LENGTH * NUMBER_SECTORS_PER_TRACK;

    private static final int NIBBLIZED_DISK_IMAGE_LENGTH = NIBBLIZED_TRACK_LENGTH * NUMBER_TRACKS_PER_DISK;

    private static final byte DOS33_SECTOR_SKEWING[] = { 0, 7, 14, 6, 13, 5, 12, 4, 11, 3, 10, 2, 9, 1, 8, 15 };

    private static final byte BYTE_TRANSLATION[] = { (byte) 0x96, (byte) 0x97, (byte) 0x9a, (byte) 0x9b, (byte) 0x9d, (byte) 0x9e, (byte) 0x9f, (byte) 0xa6, (byte) 0xa7, (byte) 0xab, (byte) 0xac, (byte) 0xad, (byte) 0xae, (byte) 0xaf, (byte) 0xb2, (byte) 0xb3, (byte) 0xb4, (byte) 0xb5, (byte) 0xb6, (byte) 0xb7, (byte) 0xb9, (byte) 0xba, (byte) 0xbb, (byte) 0xbc, (byte) 0xbd, (byte) 0xbe, (byte) 0xbf, (byte) 0xcb, (byte) 0xcd, (byte) 0xce, (byte) 0xcf, (byte) 0xd3, (byte) 0xd6, (byte) 0xd7, (byte) 0xd9, (byte) 0xda, (byte) 0xdb, (byte) 0xdc, (byte) 0xdd, (byte) 0xde, (byte) 0xdf, (byte) 0xe5, (byte) 0xe6, (byte) 0xe7, (byte) 0xe9, (byte) 0xea, (byte) 0xeb, (byte) 0xec, (byte) 0xed, (byte) 0xee, (byte) 0xef, (byte) 0xf2, (byte) 0xf3, (byte) 0xf4, (byte) 0xf5, (byte) 0xf6, (byte) 0xf7, (byte) 0xf9, (byte) 0xfa, (byte) 0xfb, (byte) 0xfc, (byte) 0xfd, (byte) 0xfe, (byte) 0xff };

    public static byte[][] loadImage(String filename) throws IOException {
        byte[][] diskImage = new byte[NUMBER_TRACKS_PER_DISK][DISK_IMAGE_TRACK_LENGTH];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            for (int track = 0; track < NUMBER_TRACKS_PER_DISK; track++) {
                int bytesRead = fis.read(diskImage[track], 0, DISK_IMAGE_TRACK_LENGTH);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return diskImage;
    }

    public static byte[][] nibblizeImage(byte[][] diskImage) {
        return nibblizeImage(diskImage, DEFAULT_VOLUME);
    }

    public static byte[][] nibblizeImage(byte[][] diskImage, byte volume) {
        byte[][] nibblizedTracks = new byte[NUMBER_TRACKS_PER_DISK][];
        for (byte track = 0; track < NUMBER_TRACKS_PER_DISK; track++) {
            ByteArrayOutputStream nibblizedTrack = new ByteArrayOutputStream(NIBBLIZED_TRACK_LENGTH);
            for (byte sector = 0; sector < NUMBER_SECTORS_PER_TRACK; sector++) {
                writeSyncBytes(nibblizedTrack, NUMBER_GAP3_SYNC_BYTES);
                nibblizedTrack.write(PROLOGUE1);
                nibblizedTrack.write(PROLOGUE2);
                nibblizedTrack.write(ADDRESS_PROLOGUE3);
                nibblizedTrack.write(xx(volume));
                nibblizedTrack.write(yy(volume));
                nibblizedTrack.write(xx(track));
                nibblizedTrack.write(yy(track));
                nibblizedTrack.write(xx(sector));
                nibblizedTrack.write(yy(sector));
                byte checksum = (byte) (volume ^ track ^ sector);
                nibblizedTrack.write(xx(checksum));
                nibblizedTrack.write(yy(checksum));
                nibblizedTrack.write(EPILOGUE1);
                nibblizedTrack.write(EPILOGUE2);
                nibblizedTrack.write(EPILOGUE3);
                writeSyncBytes(nibblizedTrack, NUMBER_GAP2_SYNC_BYTES);
                nibblizedTrack.write(PROLOGUE1);
                nibblizedTrack.write(PROLOGUE2);
                nibblizedTrack.write(DATA_PROLOGUE3);
                byte logicalSector = DOS33_SECTOR_SKEWING[sector];
                int sectorOffset = logicalSector * NUMBER_BYTES_PER_SECTOR;
                byte[] nibblized = prenibblizeSector(diskImage[track], sectorOffset);
                checksum = 0;
                for (int i = 86; i > 0; i--) {
                    byte nibblizedByte = nibblized[256 + i - 1];
                    byte eorByte = (byte) (nibblizedByte ^ checksum);
                    byte translatedByte = BYTE_TRANSLATION[eorByte];
                    nibblizedTrack.write(translatedByte);
                    checksum = nibblizedByte;
                }
                for (int i = 0; i < 256; i++) {
                    byte nibblizedByte = nibblized[i];
                    byte eorByte = (byte) (nibblizedByte ^ checksum);
                    byte translatedByte = BYTE_TRANSLATION[eorByte];
                    nibblizedTrack.write(translatedByte);
                    checksum = nibblizedByte;
                }
                byte translatedByte = BYTE_TRANSLATION[checksum];
                nibblizedTrack.write(translatedByte);
                nibblizedTrack.write(EPILOGUE1);
                nibblizedTrack.write(EPILOGUE2);
                nibblizedTrack.write(EPILOGUE3);
            }
            writeSyncBytes(nibblizedTrack, NUMBER_GAP1_SYNC_BYTES);
            nibblizedTracks[track] = nibblizedTrack.toByteArray();
        }
        return nibblizedTracks;
    }

    private static byte[] prenibblizeSector(byte[] diskImage, int sectorOffset) {
        int numNibblizedBytes = (int) Math.ceil((double) NUMBER_BYTES_PER_SECTOR * 4 / 3);
        byte[] nibblized = new byte[numNibblizedBytes];
        byte userByte;
        byte byte6;
        byte byte2;
        for (int i = 0; i < 84; i++) {
            userByte = diskImage[sectorOffset + i];
            byte6 = (byte) ((userByte & 0xFF) >> 2);
            byte2 = (byte) ((userByte & 0x01) << 1 | (userByte & 0x02) >> 1);
            nibblized[i] = byte6;
            userByte = diskImage[sectorOffset + i + 86];
            byte6 = (byte) ((userByte & 0xFF) >> 2);
            byte2 |= (byte) ((userByte & 0x01) << 3 | (userByte & 0x02) << 1);
            nibblized[i + 86] = byte6;
            userByte = diskImage[sectorOffset + i + 172];
            byte6 = (byte) ((userByte & 0xFF) >> 2);
            byte2 |= (byte) ((userByte & 0x01) << 5 | (userByte & 0x02) << 3);
            nibblized[i + 172] = byte6;
            nibblized[256 + 86 - 1 - i] = byte2;
        }
        userByte = diskImage[sectorOffset + 84];
        byte6 = (byte) ((userByte & 0xFF) >> 2);
        byte2 = (byte) ((userByte & 0x01) << 1 | (userByte & 0x02) >> 1);
        nibblized[84] = byte6;
        userByte = diskImage[sectorOffset + 170];
        byte6 = (byte) ((userByte & 0xFF) >> 2);
        byte2 |= (byte) ((userByte & 0x01) << 3 | (userByte & 0x02) << 1);
        nibblized[170] = byte6;
        nibblized[256 + 86 - 1 - 84] = byte2;
        userByte = diskImage[sectorOffset + 85];
        byte6 = (byte) ((userByte & 0xFF) >> 2);
        byte2 = (byte) ((userByte & 0x01) << 1 | (userByte & 0x02) >> 1);
        nibblized[85] = byte6;
        userByte = diskImage[sectorOffset + 171];
        byte6 = (byte) ((userByte & 0xFF) >> 2);
        byte2 |= (byte) ((userByte & 0x01) << 3 | (userByte & 0x02) << 1);
        nibblized[171] = byte6;
        nibblized[256 + 86 - 1 - 85] = byte2;
        return nibblized;
    }

    private static void writeSyncBytes(ByteArrayOutputStream nibblizedImage, int numberBytes) {
        for (int i = 0; i < numberBytes; i++) {
            nibblizedImage.write(SYNC_BYTE);
        }
    }

    private static byte xx(byte datum) {
        return (byte) (datum >>> 1 | 0xAA);
    }

    private static byte yy(byte datum) {
        return (byte) (datum | 0xAA);
    }
}
