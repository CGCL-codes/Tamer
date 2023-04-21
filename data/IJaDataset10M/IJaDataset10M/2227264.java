package sjtu.llgx.util;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectory;
import com.drew.metadata.exif.ExifReader;

public class ExifUtil {

    public static Map<String, Object> getExif(HttpServletRequest request, String img) {
        LinkedHashMap<String, Object> imgInfo = new LinkedHashMap<String, Object>();
        File picFile = new File(JavaCenterHome.jchRoot + img);
        if (picFile.isFile()) {
            try {
                ExifReader er = new ExifReader(picFile);
                Metadata metadata = er.extract();
                Directory exif = metadata.getDirectory(ExifDirectory.class);
                if (exif.getTagCount() > 0) {
                    imgInfo.put(Common.getMessage(request, "FileName"), picFile.getName());
                    imgInfo.put(Common.getMessage(request, "FileType"), "JPG");
                    imgInfo.put(Common.getMessage(request, "MimeType"), "image/jpeg");
                    imgInfo.put(Common.getMessage(request, "FileSize"), picFile.length());
                    imgInfo.put(Common.getMessage(request, "FileDateTime"), Common.gmdate("yyyy-MM-dd HH:mm:ss", (int) (picFile.lastModified() / 1000), Common.getTimeOffset((Map<String, Object>) request.getAttribute("sGlobal"), (Map<String, Object>) request.getAttribute("sConfig"))));
                    Image image = ImageIO.read(picFile);
                    imgInfo.put(Common.getMessage(request, "ImageDescription"), exif.getDescription(ExifDirectory.TAG_IMAGE_DESCRIPTION));
                    imgInfo.put(Common.getMessage(request, "Artist"), exif.getString(ExifDirectory.TAG_ARTIST));
                    imgInfo.put(Common.getMessage(request, "Copyright"), exif.getDescription(ExifDirectory.TAG_COPYRIGHT));
                    imgInfo.put(Common.getMessage(request, "ImageHeight"), image.getHeight(null));
                    imgInfo.put(Common.getMessage(request, "ImageWidth"), image.getWidth(null));
                    imgInfo.put(Common.getMessage(request, "UserComment"), exif.getDescription(ExifDirectory.TAG_USER_COMMENT));
                    imgInfo.put(Common.getMessage(request, "Make"), exif.getString(ExifDirectory.TAG_MAKE));
                    imgInfo.put(Common.getMessage(request, "Model"), exif.getString(ExifDirectory.TAG_MODEL));
                    imgInfo.put(Common.getMessage(request, "Orientation"), exif.getDescription(ExifDirectory.TAG_ORIENTATION));
                    imgInfo.put(Common.getMessage(request, "XResolution"), exif.getString(ExifDirectory.TAG_X_RESOLUTION));
                    imgInfo.put(Common.getMessage(request, "YResolution"), exif.getString(ExifDirectory.TAG_Y_RESOLUTION));
                    String resolutionUnit = exif.getString(ExifDirectory.TAG_RESOLUTION_UNIT);
                    imgInfo.put(Common.getMessage(request, "ResolutionUnit"), Common.getMessage(request, "ResolutionUnit_" + (resolutionUnit == null ? 0 : resolutionUnit)));
                    imgInfo.put(Common.getMessage(request, "Software"), exif.getString(ExifDirectory.TAG_SOFTWARE));
                    imgInfo.put(Common.getMessage(request, "DateTime"), exif.getString(ExifDirectory.TAG_DATETIME));
                    imgInfo.put(Common.getMessage(request, "YCbCrPositioning"), exif.getDescription(ExifDirectory.TAG_YCBCR_POSITIONING));
                    imgInfo.put(Common.getMessage(request, "ExposureTime"), exif.getDescription(ExifDirectory.TAG_EXPOSURE_TIME));
                    String exposureProgram = exif.getString(ExifDirectory.TAG_EXPOSURE_PROGRAM);
                    imgInfo.put(Common.getMessage(request, "ExposureProgram"), Common.getMessage(request, "ExposureProgram_" + (exposureProgram == null ? 0 : exposureProgram)));
                    imgInfo.put(Common.getMessage(request, "FNumber"), exif.getDescription(ExifDirectory.TAG_FNUMBER));
                    imgInfo.put(Common.getMessage(request, "ISOSpeedRatings"), exif.getString(ExifDirectory.TAG_ISO_EQUIVALENT));
                    imgInfo.put(Common.getMessage(request, "ComponentsConfiguration"), "1 2 3 0".equals(exif.getString(ExifDirectory.TAG_COMPONENTS_CONFIGURATION)) ? "YCbCr" : "RGB");
                    imgInfo.put(Common.getMessage(request, "DateTimeOriginal"), exif.getDescription(ExifDirectory.TAG_DATETIME_ORIGINAL));
                    imgInfo.put(Common.getMessage(request, "DateTimeDigitized"), exif.getDescription(ExifDirectory.TAG_DATETIME_DIGITIZED));
                    imgInfo.put(Common.getMessage(request, "CompressedBitsPerPixel"), exif.getDescription(ExifDirectory.TAG_COMPRESSION_LEVEL));
                    imgInfo.put(Common.getMessage(request, "ShutterSpeedValue"), exif.getDescription(ExifDirectory.TAG_SHUTTER_SPEED));
                    imgInfo.put(Common.getMessage(request, "ApertureValue"), exif.getDescription(ExifDirectory.TAG_APERTURE));
                    imgInfo.put(Common.getMessage(request, "BrightnessValue"), exif.getDescription(ExifDirectory.TAG_BRIGHTNESS_VALUE));
                    imgInfo.put(Common.getMessage(request, "ExposureBiasValue"), exif.getDescription(ExifDirectory.TAG_EXPOSURE_BIAS));
                    imgInfo.put(Common.getMessage(request, "MaxApertureValue"), exif.getDescription(ExifDirectory.TAG_MAX_APERTURE));
                    imgInfo.put(Common.getMessage(request, "SubjectDistance"), exif.getDescription(ExifDirectory.TAG_SUBJECT_DISTANCE));
                    String meteringMode = exif.getString(ExifDirectory.TAG_METERING_MODE);
                    imgInfo.put(Common.getMessage(request, "MeteringMode"), Common.getMessage(request, "MeteringMode_" + (meteringMode == null ? 0 : meteringMode)));
                    String lightSource = exif.getString(ExifDirectory.TAG_LIGHT_SOURCE);
                    imgInfo.put(Common.getMessage(request, "LightSource"), Common.getMessage(request, "LightSource_" + (lightSource == null ? 0 : lightSource)));
                    imgInfo.put(Common.getMessage(request, "Flash"), exif.getDescription(ExifDirectory.TAG_FLASH));
                    String focalLength = exif.getString(ExifDirectory.TAG_FOCAL_LENGTH);
                    if (focalLength != null) {
                        imgInfo.put(Common.getMessage(request, "FocalLength"), exif.getString(ExifDirectory.TAG_FOCAL_LENGTH) + " mm");
                    }
                    String FocalLengthIn35mmFilm = exif.getString(ExifDirectory.TAG_35MM_FILM_EQUIV_FOCAL_LENGTH);
                    if (FocalLengthIn35mmFilm != null) {
                        imgInfo.put(Common.getMessage(request, "FocalLengthIn35mmFilm"), FocalLengthIn35mmFilm + " mm");
                    }
                    imgInfo.put(Common.getMessage(request, "ColorSpace"), exif.getDescription(ExifDirectory.TAG_COLOR_SPACE));
                    imgInfo.put(Common.getMessage(request, "ExifImageHeight"), exif.getString(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT));
                    imgInfo.put(Common.getMessage(request, "ExifImageWidth"), exif.getString(ExifDirectory.TAG_EXIF_IMAGE_WIDTH));
                    imgInfo.put(Common.getMessage(request, "ExposureIndex"), exif.getDescription(ExifDirectory.TAG_EXPOSURE_INDEX));
                    imgInfo.put(Common.getMessage(request, "SensingMethod"), exif.getDescription(ExifDirectory.TAG_SENSING_METHOD));
                    imgInfo.put(Common.getMessage(request, "SceneType"), exif.getDescription(ExifDirectory.TAG_SCENE_TYPE));
                    imgInfo.put(Common.getMessage(request, "ExposureMode"), Common.getMessage(request, "1".equals(exif.getString(ExifDirectory.TAG_EXPOSURE_MODE)) ? "manual" : "auto"));
                    imgInfo.put(Common.getMessage(request, "WhiteBalance"), Common.getMessage(request, "1".equals(exif.getString(ExifDirectory.TAG_WHITE_BALANCE)) ? "manual" : "auto"));
                    imgInfo.put(Common.getMessage(request, "DigitalZoomRatio"), exif.getDescription(ExifDirectory.TAG_DIGITAL_ZOOM_RATIO));
                    imgInfo.put(Common.getMessage(request, "SceneCaptureType"), exif.getDescription(ExifDirectory.TAG_SCENE_CAPTURE_TYPE));
                    imgInfo.put(Common.getMessage(request, "GainControl"), exif.getDescription(ExifDirectory.TAG_GAIN_CONTROL));
                    imgInfo.put(Common.getMessage(request, "Contrast"), exif.getDescription(ExifDirectory.TAG_CONTRAST));
                    imgInfo.put(Common.getMessage(request, "Saturation"), exif.getDescription(ExifDirectory.TAG_SATURATION));
                    imgInfo.put(Common.getMessage(request, "Sharpness"), exif.getDescription(ExifDirectory.TAG_SHARPNESS));
                    imgInfo.put(Common.getMessage(request, "SubjectDistanceRange"), exif.getDescription(ExifDirectory.TAG_SUBJECT_DISTANCE_RANGE));
                    imgInfo.put(Common.getMessage(request, "ExifVersion"), exif.getDescription(ExifDirectory.TAG_EXIF_VERSION));
                    String flashPixVersion = exif.getDescription(ExifDirectory.TAG_FLASHPIX_VERSION);
                    imgInfo.put(Common.getMessage(request, "FlashPixVersion"), "Ver. " + (flashPixVersion == null ? "0.00" : flashPixVersion));
                    imgInfo.put(Common.getMessage(request, "FileSource"), exif.getDescription(ExifDirectory.TAG_FILE_SOURCE));
                }
            } catch (Exception e) {
            }
        }
        if (imgInfo.isEmpty()) {
            imgInfo.put(Common.getMessage(request, "img_info_key"), Common.getMessage(request, "img_info_value"));
        }
        return imgInfo;
    }

    public static void main(String args[]) throws FileNotFoundException {
        File f = new File("d:\\1_1259808211kXVh.jpg");
        try {
            ExifReader er = new ExifReader(f);
            Metadata exif = er.extract();
            Directory directory = exif.getDirectory(ExifDirectory.class);
            if (directory.getTagCount() > 0) {
                Iterator tags = directory.getTagIterator();
                while (tags.hasNext()) {
                    Tag tag = (Tag) tags.next();
                    System.out.println(tag);
                }
                if (directory.hasErrors()) {
                    Iterator errors = directory.getErrors();
                    while (errors.hasNext()) {
                        System.out.println("ERROR:  " + errors.next());
                    }
                }
            }
        } catch (JpegProcessingException e) {
            System.err.println("not  jpeg  file");
        }
    }
}
