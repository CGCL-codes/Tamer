package org.designerator.exif;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectory;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.iptc.IptcDirectory;

public class Exif {

    public static ExifDirectory readExifOnly(File jpegFile) {
        final Metadata metadata = new Metadata();
        JpegSegmentReader segmentReader;
        try {
            segmentReader = new JpegSegmentReader(jpegFile);
            byte[] exifSegment = segmentReader.readSegment(JpegSegmentReader.SEGMENT_APP1);
            new ExifFastReader(exifSegment).extract(metadata);
            return (ExifDirectory) metadata.getDirectory(ExifDirectory.class);
        } catch (JpegProcessingException e) {
            System.err.println(e);
        }
        return null;
    }

    public static Image getThumbnail(Metadata metadata) throws Exception {
        ExifDirectory exifDir = null;
        Image img = null;
        if (metadata != null) {
            exifDir = (ExifDirectory) metadata.getDirectory(ExifDirectory.class);
        }
        if (exifDir != null && exifDir.containsThumbnail()) {
            int compression = 0;
            try {
                compression = exifDir.getInt(ExifDirectory.TAG_COMPRESSION);
            } catch (Throwable e) {
            }
            byte[] thumbnailBytes = null;
            try {
                thumbnailBytes = exifDir.getThumbnailData();
            } catch (MetadataException e) {
            }
            if (compression == ExifDirectory.COMPRESSION_JPEG) {
                ByteArrayInputStream bais = new ByteArrayInputStream(thumbnailBytes);
                img = new Image(Display.getCurrent(), bais);
                return img;
            }
        }
        return img;
    }

    public static String getDateTag(File jpegFile) {
        ExifDirectory exif = readExifOnly(jpegFile);
        if (exif != null) {
            return exif.getString(ExifDirectory.TAG_DATETIME);
        }
        return null;
    }

    public static Metadata getMetaData(File jpegFile) {
        Metadata metadata = null;
        try {
            if (jpegFile.exists()) return JpegMetadataReader.readMetadata(jpegFile);
        } catch (JpegProcessingException e) {
            return metadata;
        }
        return metadata;
    }

    public static ExifDirectory readEXIF(File jpegFile) {
        Metadata metadata = getMetaData(jpegFile);
        if (metadata == null) return null;
        ExifDirectory exifDir = (ExifDirectory) metadata.getDirectory(ExifDirectory.class);
        return exifDir;
    }

    public static ExifDirectory readEXIF(String jpegFile) {
        return readEXIF(new File(jpegFile));
    }

    public static Map<String, String> createExifMap(ExifDirectory exifDir) {
        Iterator i = exifDir.getTagIterator();
        Map<String, String> map = new HashMap<String, String>(exifDir.getTagCount());
        while (i.hasNext()) {
            Tag t = (Tag) i.next();
            try {
                map.put(t.getTagName(), exifDir.getDescription(t.getTagType()));
            } catch (MetadataException e) {
            }
        }
        return map;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        ExifDirectory readExifOnly = readExifOnly(new File("I:\\Eigene Bilder\\test2\\44.jpg"));
        String val = readExifOnly.getString(ExifDirectory.TAG_DATETIME);
        System.out.println(val);
    }

    public static void test1() {
        String s = "I:\\Eigene BilderTest\\A460hSLI100.JPG";
        ExifDirectory exifDir = readEXIF(s);
        Map<String, String> map = createExifMap(exifDir);
        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            System.out.println(key + " : " + value);
        }
    }
}
