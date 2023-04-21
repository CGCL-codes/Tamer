package org.apache.sanselan.sampleUsage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.RationalNumber;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

public class MetadataExample {

    public static void metadataExample(File file) throws ImageReadException, IOException {
        IImageMetadata metadata = Sanselan.getMetadata(file);
        if (metadata instanceof JpegImageMetadata) {
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            System.out.println("file: " + file.getPath());
            printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_XRESOLUTION);
            printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_DATE_TIME);
            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_CREATE_DATE);
            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_ISO);
            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_SHUTTER_SPEED_VALUE);
            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_APERTURE_VALUE);
            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_BRIGHTNESS_VALUE);
            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LATITUDE_REF);
            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LATITUDE);
            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LONGITUDE_REF);
            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LONGITUDE);
            System.out.println();
            TiffImageMetadata exifMetadata = jpegMetadata.getExif();
            if (null != exifMetadata) {
                TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
                if (null != gpsInfo) {
                    String gpsDescription = gpsInfo.toString();
                    double longitude = gpsInfo.getLongitudeAsDegreesEast();
                    double latitude = gpsInfo.getLatitudeAsDegreesNorth();
                    System.out.println("    " + "GPS Description: " + gpsDescription);
                    System.out.println("    " + "GPS Longitude (Degrees East): " + longitude);
                    System.out.println("    " + "GPS Latitude (Degrees North): " + latitude);
                }
            }
            TiffField gpsLatitudeRefField = jpegMetadata.findEXIFValueWithExactMatch(TiffConstants.GPS_TAG_GPS_LATITUDE_REF);
            TiffField gpsLatitudeField = jpegMetadata.findEXIFValueWithExactMatch(TiffConstants.GPS_TAG_GPS_LATITUDE);
            TiffField gpsLongitudeRefField = jpegMetadata.findEXIFValueWithExactMatch(TiffConstants.GPS_TAG_GPS_LONGITUDE_REF);
            TiffField gpsLongitudeField = jpegMetadata.findEXIFValueWithExactMatch(TiffConstants.GPS_TAG_GPS_LONGITUDE);
            if (gpsLatitudeRefField != null && gpsLatitudeField != null && gpsLongitudeRefField != null && gpsLongitudeField != null) {
                String gpsLatitudeRef = (String) gpsLatitudeRefField.getValue();
                RationalNumber gpsLatitude[] = (RationalNumber[]) (gpsLatitudeField.getValue());
                String gpsLongitudeRef = (String) gpsLongitudeRefField.getValue();
                RationalNumber gpsLongitude[] = (RationalNumber[]) gpsLongitudeField.getValue();
                RationalNumber gpsLatitudeDegrees = gpsLatitude[0];
                RationalNumber gpsLatitudeMinutes = gpsLatitude[1];
                RationalNumber gpsLatitudeSeconds = gpsLatitude[2];
                RationalNumber gpsLongitudeDegrees = gpsLongitude[0];
                RationalNumber gpsLongitudeMinutes = gpsLongitude[1];
                RationalNumber gpsLongitudeSeconds = gpsLongitude[2];
                System.out.println("    " + "GPS Latitude: " + gpsLatitudeDegrees.toDisplayString() + " degrees, " + gpsLatitudeMinutes.toDisplayString() + " minutes, " + gpsLatitudeSeconds.toDisplayString() + " seconds " + gpsLatitudeRef);
                System.out.println("    " + "GPS Longitude: " + gpsLongitudeDegrees.toDisplayString() + " degrees, " + gpsLongitudeMinutes.toDisplayString() + " minutes, " + gpsLongitudeSeconds.toDisplayString() + " seconds " + gpsLongitudeRef);
            }
            System.out.println();
            ArrayList items = jpegMetadata.getItems();
            for (int i = 0; i < items.size(); i++) {
                Object item = items.get(i);
                System.out.println("    " + "item: " + item);
            }
            System.out.println();
        }
    }

    private static void printTagValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) {
        TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
        if (field == null) System.out.println(tagInfo.name + ": " + "Not Found."); else System.out.println(tagInfo.name + ": " + field.getValueDescription());
    }
}
