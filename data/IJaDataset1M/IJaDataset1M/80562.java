package com.drewChanged.metadata.jpeg;

import com.drewChanged.imaging.jpeg.JpegProcessingException;
import com.drewChanged.imaging.jpeg.JpegSegmentReader;
import com.drewChanged.metadata.Metadata;
import com.drewChanged.metadata.MetadataException;
import com.drewChanged.metadata.MetadataReader;
import java.io.InputStream;

/**
 *
 * @author Darrell Silver http://www.darrellsilver.com and Drew Noakes
 */
public class JpegReader implements MetadataReader {

    /**
     * The SOF0 data segment.
     */
    private final byte[] _data;

    /** Creates a JpegReader for a JPEG stream.
     *
     * @param is JPEG stream. Stream will be closed.
     */
    public JpegReader(InputStream is) throws JpegProcessingException {
        this(new JpegSegmentReader(is).readSegment(JpegSegmentReader.SEGMENT_APPD));
    }

    public JpegReader(byte[] data) {
        _data = data;
    }

    /**
     * Performs the Jpeg data extraction, returning a new instance of <code>Metadata</code>.
     */
    public Metadata extract() {
        return extract(new Metadata());
    }

    /**
     * Performs the Jpeg data extraction, adding found values to the specified
     * instance of <code>Metadata</code>.
     */
    public Metadata extract(Metadata metadata) {
        if (_data == null) {
            return metadata;
        }
        JpegDirectory directory = (JpegDirectory) metadata.getDirectory(JpegDirectory.class);
        System.out.println("directory is obtained! " + directory.getName());
        try {
            int dataPrecision = get16Bits(JpegDirectory.TAG_JPEG_DATA_PRECISION);
            directory.setInt(JpegDirectory.TAG_JPEG_DATA_PRECISION, dataPrecision);
            int height = get32Bits(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT);
            directory.setInt(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT, height);
            int width = get32Bits(JpegDirectory.TAG_JPEG_IMAGE_WIDTH);
            directory.setInt(JpegDirectory.TAG_JPEG_IMAGE_WIDTH, width);
            int numberOfComponents = get16Bits(JpegDirectory.TAG_JPEG_NUMBER_OF_COMPONENTS);
            directory.setInt(JpegDirectory.TAG_JPEG_NUMBER_OF_COMPONENTS, numberOfComponents);
            int offset = 6;
            for (int i = 0; i < numberOfComponents; i++) {
                int componentId = get16Bits(offset++);
                int samplingFactorByte = get16Bits(offset++);
                int quantizationTableNumber = get16Bits(offset++);
                JpegComponent component = new JpegComponent(componentId, samplingFactorByte, quantizationTableNumber);
                directory.setObject(JpegDirectory.TAG_JPEG_COMPONENT_DATA_1 + i, component);
                System.out.println("Just set object JpegComponent: " + componentId + "," + samplingFactorByte + "," + quantizationTableNumber);
            }
        } catch (MetadataException me) {
            System.out.println("MetadataException: " + me);
        }
        return metadata;
    }

    /**
     * Returns an int calculated from two bytes of data at the specified offset (MSB, LSB).
     * @param offset position within the data buffer to read first byte
     * @return the 32 bit int value, between 0x0000 and 0xFFFF
     */
    private int get32Bits(int offset) throws MetadataException {
        if (offset + 1 >= _data.length) {
            throw new MetadataException("Attempt to read bytes from outside Jpeg segment data buffer");
        }
        return ((_data[offset] & 255) << 8) | (_data[offset + 1] & 255);
    }

    /**
     * Returns an int calculated from one byte of data at the specified offset.
     * @param offset position within the data buffer to read byte
     * @return the 16 bit int value, between 0x00 and 0xFF
     */
    private int get16Bits(int offset) throws MetadataException {
        if (offset >= _data.length) {
            throw new MetadataException("Attempt to read bytes from outside Jpeg segment data buffer");
        }
        return (_data[offset] & 255);
    }
}
