package org.apache.sanselan.formats.jpeg.exif;

import java.io.File;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class TextFieldTest extends SpecificExifTagTest {

    protected void checkField(File imageFile, TiffField field) throws IOException, ImageReadException, ImageWriteException {
        if (field.tag == EXIF_TAG_USER_COMMENT.tag) ; else if (field.tag == GPS_TAG_GPS_PROCESSING_METHOD.tag && field.directoryType == EXIF_DIRECTORY_GPS.directoryType) ; else if (field.tag == GPS_TAG_GPS_AREA_INFORMATION.tag && field.directoryType == EXIF_DIRECTORY_GPS.directoryType) ; else return;
        try {
            Object textFieldValue = field.getValue();
        } catch (ImageReadException e) {
            Debug.debug("imageFile", imageFile.getAbsoluteFile());
            Debug.debug(e);
            throw e;
        }
    }
}
