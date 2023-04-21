package eu.keep.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Common shared disk image utilities, such as read parameters, determine geometry, etc.
 * @author Bram Lohman
 */
public class DiskUtilities {

    private static final Logger logger = Logger.getLogger(DiskUtilities.class.getName());

    static byte[] sectorsPerTrack = new byte[2];

    static byte[] numHeads = new byte[2];

    static byte[] firstSectLBA = new byte[4];

    /**
     * Reads the heads and sectors/track information from a FAT image and calculates the
     * cylinders.
     * The information is read from the boot sector, or if a partition table exists, from the
     * first (primary) partition
     * @param diskFile A disk image, containing a valid FAT-formatted boot sector/partitions 
     * @return A three item list consisting of cylinder, heads and sectors/track, or an empty list if no information was found 
     * @throws IOException When there are issues reading/seeking the file 
     */
    public static List<Integer> determineCHS(File diskFile) throws IOException {
        List<Integer> chs = new ArrayList<Integer>();
        RandomAccessFile img = new RandomAccessFile(diskFile, "r");
        img.seek(0x18);
        img.read(sectorsPerTrack);
        img.seek(0x1A);
        img.read(numHeads);
        logger.debug("Boot sector values: s/t = " + hexArrayToDec(sectorsPerTrack) + " heads = " + hexArrayToDec(numHeads));
        img.seek(0x01BE + 0x08);
        img.read(firstSectLBA);
        int part1start = hexArrayToDec(firstSectLBA) * 512;
        if (part1start != 0) {
            logger.debug("Using primary partition 1 information (located at " + part1start + ");");
            img.seek(part1start + 0x18);
            img.read(sectorsPerTrack);
            img.seek(part1start + 0x1A);
            img.read(numHeads);
            logger.debug("Partition values: s/t = " + hexArrayToDec(sectorsPerTrack) + " heads = " + hexArrayToDec(numHeads));
        }
        int sect = hexArrayToDec(sectorsPerTrack);
        int heads = hexArrayToDec(numHeads);
        if (sect == 0 && heads == 0) {
            logger.debug("Partition information reports s/t=" + sect + " and heads=" + heads + "; will use defaults of 63/255 instead");
            sect = 63;
            sectorsPerTrack = new byte[] { 0x3F, 0x00 };
            heads = 255;
            numHeads = new byte[] { (byte) 0xFF, 0x00 };
        }
        int cyl = (int) Math.floor(diskFile.length() / (sect * heads * 512));
        chs.add(cyl);
        chs.add(heads);
        chs.add(sect);
        logger.debug("CHS values determined as: " + chs.toString());
        return chs;
    }

    /**
	 * Determines if a disk image is in the ISO9660 format
	 * @param diskFile A disk image to be tested for a ISO signature at position 0x8000
	 * @return True if the image is in the ISO9660 format, false otherwise
	 * @throws IOException When there are issues reading/seeking the file
	 */
    public static boolean isISO9660(File diskFile) throws IOException {
        byte[] isoSignature = new byte[] { 0x01, 0x43, 0x44, 0x30, 0x30, 0x31, 0x01 };
        byte[] imgData = new byte[isoSignature.length];
        RandomAccessFile img = new RandomAccessFile(diskFile, "r");
        if (diskFile.length() < (0x8000 + isoSignature.length)) return false;
        img.seek(0x8000);
        img.read(imgData);
        if (Arrays.equals(imgData, isoSignature)) {
            logger.debug("Image " + diskFile.getAbsolutePath() + " identified as ISO9660 format");
            return true;
        }
        logger.debug("Image " + diskFile.getAbsolutePath() + " is not in ISO9660 format");
        return false;
    }

    /**
	 * Convert a little-endian hexadecimal array into a single decimal value
	 * @param in A hex array of arbitrary (but less than 8) length
	 * @return A decimal value equal to the hexadecimal array
	 */
    private static int hexArrayToDec(byte[] in) {
        if (in.length > 7) return -1;
        int result = 0;
        for (int i = 0; i < in.length; i++) {
            result += (in[i] & 0xFF) << (8 * i);
        }
        return result;
    }
}
