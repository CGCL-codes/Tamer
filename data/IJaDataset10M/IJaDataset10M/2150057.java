package JCPC.system.cpc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.*;
import JCPC.core.device.Device;
import JCPC.core.device.floppy.DiscImage;
import JCPC.core.device.floppy.UPD765A;

/**
 * CPC disc image.
 *
 * @author Roland.Barthel
 */
public class CPCDiscImage extends DiscImage {

    protected boolean DEBUG = false;

    private static String SAVED_DSK = "";

    private static final String WIN_APE_EYECATCHER = "Win APE 32 1.0";

    private static final String MV_CPC_EYECATCHER = "MV - CPC";

    private static final String EXTENDED_EYECATCHER = "EXTENDED";

    private static final String EXTENDED_DESCRIPTION = EXTENDED_EYECATCHER + " CPC DSK File\r\nDisk-Info\r\n";

    private static final String CREATOR_DATA = "JAVACPC EXTDSK";

    private static final String ENCODING = "UTF-8";

    private static final String TRACK_INFO = "Track-Info\r\n";

    private static final int SIDE_MASK = 1;

    private static final int MAX_TRACK = 79;

    private static final int BUFFER_SIZE = 8192;

    private static final int[] AMSDOS_SECTOR_IDS = { 0xC1, 0xC3, 0xC5, 0xC7, 0xC9, 0xC2, 0xC4, 0xC6, 0xC8 };

    /** new image or loaded? */
    private final boolean newImage;

    private final String discId;

    /** name of creator. */
    private final String creator;

    /** number of tracks. */
    private final int numberOfTracks;

    /** number of sides. */
    private final int numberOfSides;

    /** size of a track . */
    private final int sizeOfTrack;

    /** extended DISK format? (Revision 5) */
    private final boolean extended;

    /** the tracks. */
    private final CPCDiscImageTrack[][] tracks;

    /**
   * Creates a new instance of CPCDiscImage.
   *
   * @param name file name
   * @param is image input stream
   * @return the disc image
   * @throws IOException when image creation fails
   */
    public static CPCDiscImage create(final String name, final InputStream is) throws IOException {
        return new CPCDiscImage(name, load(is));
    }

    /**
   * Create an empty AMSDOS disc image.
   *
   * @param name file name
   * @param numberOfSides number of sides 1/2
   */
    public CPCDiscImage(String name, int numberOfSides) {
        super(name);
        this.newImage = true;
        this.discId = EXTENDED_DESCRIPTION;
        this.creator = CREATOR_DATA;
        this.numberOfTracks = 80;
        this.numberOfSides = 1;
        this.sizeOfTrack = Math.max(1, Math.min(2, numberOfSides));
        this.extended = true;
        this.tracks = new CPCDiscImageTrack[this.numberOfTracks][this.numberOfSides];
        final int sectorSize = UPD765A.getCommandSize(512);
        for (int track = 0; track < this.numberOfTracks; track++) {
            for (int side = 0; side < this.numberOfSides; side++) {
                this.tracks[track][side] = new CPCDiscImageTrack(track, side, 9 * 512, 9);
                for (int sector = 0; sector < 9; sector++) {
                    final byte[] data = new byte[512];
                    for (int i = 0; i < data.length; i++) {
                        data[i] = 0;
                    }
                    this.tracks[track][side].setSector(new CPCDiscImageSector(track, side, AMSDOS_SECTOR_IDS[sector], sectorSize, data), sector);
                }
            }
        }
    }

    /**
   * Creates a new instance of CPCDiscImage.
   *
   * @param name file name
   * @param data image data
   */
    public CPCDiscImage(String name, byte[] data) {
        super(name);
        this.newImage = false;
        this.discId = new String(data, 0, 0x22);
        this.creator = new String(data, 0x22, 0x0E);
        this.numberOfTracks = data[0x30] & 0xff;
        System.out.println("Numberof tracks is:" + numberOfTracks);
        this.numberOfSides = data[0x31] & 0xff;
        this.sizeOfTrack = Device.getWord(data, 0x32);
        this.extended = this.discId.toUpperCase().startsWith(EXTENDED_EYECATCHER);
        final boolean isCpcDisc = this.extended || this.discId.toUpperCase().startsWith(MV_CPC_EYECATCHER);
        final boolean winape = this.creator.equalsIgnoreCase(WIN_APE_EYECATCHER);
        this.tracks = new CPCDiscImageTrack[this.numberOfTracks][this.numberOfSides];
        if (isCpcDisc) {
            final byte[] trackSizes = new byte[256];
            if (this.extended) {
                System.arraycopy(data, 0x34, trackSizes, 0, this.numberOfTracks * this.numberOfSides);
            }
            int offs = 0x100;
            for (int track = 0; track < this.numberOfTracks; track++) {
                for (int side = 0; side < this.numberOfSides; side++) {
                    int trackLength = this.sizeOfTrack;
                    if (this.extended) {
                        trackLength = (trackSizes[track * this.numberOfSides + side] & 0xff) * 0x100;
                    }
                    if (trackLength != 0 && offs < data.length) {
                        final int sot = offs;
                        final int numberOfSectors = data[offs + 0x15] & 0xff;
                        int sectorInformationPos = offs + 0x18;
                        this.tracks[track][side] = new CPCDiscImageTrack(track, side, trackLength, numberOfSectors);
                        offs += 0x100;
                        for (int sect = 0; sect < numberOfSectors; sect++) {
                            final int sectTrack = data[sectorInformationPos++] & 0xff;
                            final int sectSide = data[sectorInformationPos++] & 0xff;
                            final int sectId = data[sectorInformationPos++] & 0xff;
                            int sectSize = data[sectorInformationPos++] & 0xff;
                            sectorInformationPos += 2;
                            int bytes = UPD765A.getSectorSize(sectSize);
                            if (this.extended && !winape) {
                                final int sz = Device.getWord(data, sectorInformationPos);
                                if (sz != 0) {
                                    bytes = sz;
                                    sectSize = UPD765A.getCommandSize(bytes);
                                }
                            }
                            sectorInformationPos += 2;
                            final byte[] sectData = new byte[bytes];
                            try {
                                System.arraycopy(data, offs, sectData, 0, bytes);
                            } catch (Exception e) {
                                System.err.println("Corrupt diskimage loaded..." + bytes);
                                for (int r = 0; r < bytes; r++) sectData[r] = (byte) 0xE5;
                            }
                            offs += bytes;
                            this.tracks[track][side].setSector(new CPCDiscImageSector(sectTrack, sectSide, sectId, sectSize, sectData), sect);
                        }
                        if (!winape) {
                            offs = sot + trackLength;
                        }
                    }
                }
            }
        }
    }

    /**
   * Create a double sided image from two single sided images.
   *
   * @param newFileName new image file name
   * @param firstImage first image for side 0
   * @param secondImage second image for side 1
   * @throws IOException when merge fails
   */
    public CPCDiscImage(String newFileName, CPCDiscImage firstImage, CPCDiscImage secondImage) throws IOException {
        super(newFileName);
        this.newImage = true;
        this.discId = firstImage.discId;
        this.creator = firstImage.creator;
        this.numberOfTracks = Math.max(firstImage.numberOfTracks, secondImage.numberOfTracks);
        this.numberOfSides = 2;
        this.sizeOfTrack = firstImage.sizeOfTrack;
        this.extended = firstImage.extended;
        if (!firstImage.extended || !secondImage.extended) {
            throw new IOException("only extended images can be merged!");
        }
        if (firstImage.numberOfSides != 1 || secondImage.numberOfSides != 1) {
            throw new IOException("only single sided extended images can be merged!");
        }
        this.tracks = new CPCDiscImageTrack[this.numberOfTracks][this.numberOfSides];
        for (int i = 0; i < firstImage.numberOfTracks; i++) {
            this.tracks[i][0] = firstImage.tracks[i][0];
        }
        for (int i = 0; i < secondImage.numberOfTracks; i++) {
            final CPCDiscImageTrack track = secondImage.tracks[i][0];
            track.setSide(1);
            this.tracks[i][1] = track;
        }
    }

    public byte[] readSector(final int track, final int side, final int c, final int h, final int r, final int n) {
        if (DEBUG) {
            System.out.println("TRACK: " + track + " SIDE:" + side + " C:" + c + " H:" + h + " R:" + r + " N:" + n);
        }
        if (track <= MAX_TRACK) {
            try {
                return this.tracks[track][side & SIDE_MASK].getSectorData(c, h, r, n);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public int[] getSectorID(final int track, final int side, final int index) {
        return this.tracks[track][side & SIDE_MASK].getSectorIDs(index);
    }

    public int getSectorCount(final int track, final int side) {
        int result = 0;
        try {
            result = track > MAX_TRACK ? 0 : this.tracks[track][side & SIDE_MASK].getSectorCount();
        } catch (Exception e) {
        }
        return result;
    }

    public void writeSector(final int track, final int side, final int c, final int h, final int r, final int n, final byte[] data) {
    }

    /**
   * @return disc info if
   */
    public String getDiscId() {
        return this.discId;
    }

    /**
   * @return disc creator
   */
    public String getCreator() {
        return this.creator;
    }

    /**
   * @return number of tracks
   */
    public int getNumberOfTracks() {
        return this.numberOfTracks;
    }

    /**
   * @return number of sides
   */
    public int getNumberOfSides() {
        return this.numberOfSides;
    }

    /**
   * @return the track size
   */
    public int getSizeOfTrack() {
        return this.sizeOfTrack;
    }

    /**
   * @return <code>true</code> for extended disc format
   */
    public boolean isExtended() {
        return this.extended;
    }

    /**
   * @return track data for both sides
   */
    public CPCDiscImageTrack[][] getTracks() {
        return this.tracks;
    }

    static byte[] load(final InputStream is) throws IOException {
        if (is == null) {
            return new byte[0];
        }
        final int bufferSize = BUFFER_SIZE;
        byte[] tmpData = new byte[bufferSize];
        int offs = 0;
        int addOn = BUFFER_SIZE * 2;
        do {
            final int readLen = is.read(tmpData, offs, tmpData.length - offs);
            if (readLen == -1) {
                break;
            }
            offs += readLen;
            if (offs == tmpData.length) {
                final byte[] newres = new byte[tmpData.length + addOn];
                if (addOn < 1048576) {
                    addOn = addOn * 2;
                }
                System.arraycopy(tmpData, 0, newres, 0, tmpData.length);
                tmpData = newres;
            }
        } while (true);
        is.close();
        final byte[] data = new byte[offs];
        System.arraycopy(tmpData, 0, data, 0, offs);
        return data;
    }
}
