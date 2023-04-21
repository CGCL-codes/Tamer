package org.myrobotlab.image;

import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_ADAPTIVE_THRESH_MEAN_C;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvAdaptiveThreshold;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import java.awt.image.BufferedImage;
import org.apache.log4j.Logger;
import org.myrobotlab.service.OpenCV;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class OpenCVFilterAdaptiveThreshold extends OpenCVFilter {

    private static final long serialVersionUID = 1L;

    public static final Logger LOG = Logger.getLogger(OpenCVFilterAdaptiveThreshold.class.getCanonicalName());

    IplImage gray = null;

    public OpenCVFilterAdaptiveThreshold(OpenCV service, String name) {
        super(service, name);
    }

    @Override
    public BufferedImage display(IplImage image, Object[] data) {
        return image.getBufferedImage();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void loadDefaultConfiguration() {
        cfg.set("lowThreshold", 130.0f);
        cfg.set("highThreshold", 255.0f);
    }

    @Override
    public IplImage process(IplImage image) {
        if (gray == null) {
            gray = cvCreateImage(cvGetSize(image), 8, CV_THRESH_BINARY);
        }
        cvCvtColor(image, gray, CV_BGR2GRAY);
        cvAdaptiveThreshold(gray, gray, 255, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY, 7, 30);
        return image;
    }
}
