package org.metaphile.segment;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import org.metaphile.directory.BasicDirectory;
import org.metaphile.directory.IParseableDirectory;
import org.metaphile.directory.IfdDirectory;
import org.metaphile.directory.exif.*;
import org.metaphile.tag.exif.tiff.CompressionValues;
import org.metaphile.util.HexDump;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

/**
 * Represents the EXIF Segment.
 *  
 * @author stuart
 * @since 0.1.1
 */
public class ExifSegment extends IFDSegment implements ISegment {

    private static final int POINTER_EXIF = 0x8769;

    private static final int POINTER_INTEROPERABILITY = 0xA005;

    private static final int POINTER_GPS = 0x8825;

    private static final int POINTER_MAKERNOTE = 0x927C;

    private TiffDirectory tiffDirectory = new TiffDirectory();

    private BasicDirectory makerNotesDirectory = null;

    private GpsDirectory gpsDirectory = null;

    private ExifDirectory exifDirectory = null;

    private InteroperabilityDirectory interoperabilityDirectory = null;

    public int getMarker() {
        return 0xE1;
    }

    public String getName() {
        return "EXIF";
    }

    public IfdDirectory getIfd0Directory() {
        return tiffDirectory;
    }

    /**
     * Returns the tiff directory
     * @return the tiff directory
     */
    public TiffDirectory getTiffDirectory() {
        return tiffDirectory;
    }

    /**
     * Returns the exif directory
     * @return the exif directory
     */
    public ExifDirectory getExifDirectory() {
        return exifDirectory;
    }

    /**
     * Returns the gps directory
     * @return the gps directory
     */
    public GpsDirectory getGpsDirectory() {
        return gpsDirectory;
    }

    /**
     * Returns the maker notes directory
     * @return the maker notes directory
     */
    public BasicDirectory getMakerNotesDirectory() {
        return makerNotesDirectory;
    }

    /**
     * Returns the interoperability directory
     * @return the interoperability directory
     */
    public InteroperabilityDirectory getInteroperabilityDirectory() {
        return interoperabilityDirectory;
    }

    /**
     * Returns the EXIF Thumbnail, or null if there isn't one
     * @return the EXIF Thumbnail, or null if there isn't one
     */
    public BufferedImage getThumbnail() {
        if (CompressionValues.JPEG.getValue().equals(tiffDirectory.getCompression())) {
            try {
                Integer format = tiffDirectory.getJpegInterchangeFormat();
                Integer length = tiffDirectory.getJpegInterchangeFormatLength();
                int offset = startOfSegment + format;
                int size = offset + length;
                ByteArrayInputStream bis = new ByteArrayInputStream(segmentData, offset, size);
                JPEGImageDecoder dec = JPEGCodec.createJPEGDecoder(bis);
                return dec.decodeAsBufferedImage();
            } catch (IOException e) {
                log.log(Level.SEVERE, "Could not decode JPEG thumbnail");
            }
        } else if (CompressionValues.UNCOMPRESSED.getValue().equals(tiffDirectory.getCompression())) {
            Integer offset = tiffDirectory.getStripOffsets();
            Integer width = tiffDirectory.getImageWidth();
            Integer height = tiffDirectory.getImageLength();
            BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int a = 0;
                    int r = 0xff & segmentData[offset++];
                    int g = 0xff & segmentData[offset++];
                    int b = 0xff & segmentData[offset++];
                    thumbnail.setRGB(col, row, (a << 24 | r << 16 | g << 8 | b));
                }
            }
            return thumbnail;
        }
        log.log(Level.WARNING, "Unknown thumbnail compression value: " + tiffDirectory.getCompression());
        return null;
    }

    public void processPointer(Integer tagIdentifier, Integer tagValueOffset, Integer pointer, Integer dataLength) {
        switch(tagIdentifier) {
            case POINTER_MAKERNOTE:
                log.log(Level.INFO, "Found Pointer to the MakerNote Directory");
                String maker = this.getTiffDirectory().getMake();
                if (maker == null) {
                    log.log(Level.WARNING, "Cannot load maker notes because camera maker is not specified");
                    break;
                } else {
                    if (maker.startsWith("SANYO")) {
                        makerNotesDirectory = new SanyoMakerNotesDirectory();
                        directoryList.add((IfdDirectory) makerNotesDirectory);
                        this.processDirectory(tagValueOffset + 8, tagIdentifier);
                    } else if (maker.startsWith("RICOH")) {
                        makerNotesDirectory = new RicohMakerNotesDirectory();
                        ((IParseableDirectory) makerNotesDirectory).parseDirectory(segmentData, tagValueOffset, dataLength);
                    } else if (maker.startsWith("OLYMPUS")) {
                        makerNotesDirectory = new OlympusMakerNotesDirectory();
                        directoryList.add((IfdDirectory) makerNotesDirectory);
                        this.processDirectory(tagValueOffset + 8, tagIdentifier);
                    } else if (maker.startsWith("NIKON")) {
                        makerNotesDirectory = new NikonMakerNotesDirectory();
                        directoryList.add((IfdDirectory) makerNotesDirectory);
                        this.processDirectory(tagValueOffset + 8, tagIdentifier);
                    } else if (maker.toLowerCase().startsWith("eastman kodak")) {
                        makerNotesDirectory = new KodakMakerNotesDirectory();
                        ((IParseableDirectory) makerNotesDirectory).parseDirectory(segmentData, tagValueOffset, dataLength);
                    } else if (maker.toLowerCase().startsWith("fujifilm")) {
                        makerNotesDirectory = new FujifilmMakerNotesDirectory();
                        directoryList.add((IfdDirectory) makerNotesDirectory);
                        this.processDirectory(tagValueOffset + 8, tagIdentifier);
                    } else {
                        HexDump.dumpHexData(segmentData, tagValueOffset, dataLength);
                        break;
                    }
                }
                break;
            case POINTER_EXIF:
                log.log(Level.INFO, "Found Pointer to the EXIF Directory");
                exifDirectory = new ExifDirectory();
                directoryList.add(exifDirectory);
                this.processDirectory(pointer + startOfSegment, tagIdentifier);
                break;
            case POINTER_INTEROPERABILITY:
                log.log(Level.INFO, "Found Pointer to the Interoperability Directory");
                interoperabilityDirectory = new InteroperabilityDirectory();
                directoryList.add(interoperabilityDirectory);
                this.processDirectory(pointer + startOfSegment, tagIdentifier);
                break;
            case POINTER_GPS:
                log.log(Level.INFO, "Found Pointer to the GPS Directory");
                gpsDirectory = new GpsDirectory();
                directoryList.add(gpsDirectory);
                this.processDirectory(pointer + startOfSegment, tagIdentifier);
                break;
            default:
                log.log(Level.WARNING, "Unknown tag with identifier: " + tagIdentifier);
                break;
        }
    }
}
