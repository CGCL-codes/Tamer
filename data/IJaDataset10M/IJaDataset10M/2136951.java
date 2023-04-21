package org.myrobotlab.image;

import static com.googlecode.javacv.cpp.opencv_core.CV_RGB;
import static com.googlecode.javacv.cpp.opencv_core.cvClearMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvDrawLine;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import org.myrobotlab.service.OpenCV;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

public class OpenCVFilterFaceDetect extends OpenCVFilter {

    private static final long serialVersionUID = 1L;

    public static final Logger LOG = Logger.getLogger(OpenCVFilterFaceDetect.class.getCanonicalName());

    IplImage buffer = null;

    BufferedImage frameBuffer = null;

    int convert = CV_BGR2HSV;

    JFrame myFrame = null;

    JTextField pixelsPerDegree = new JTextField("8.5");

    public OpenCVFilterFaceDetect(OpenCV service, String name) {
        super(service, name);
    }

    @Override
    public BufferedImage display(IplImage image, Object[] data) {
        return buffer.getBufferedImage();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void loadDefaultConfiguration() {
    }

    CvMemStorage storage = null;

    CvHaarClassifierCascade cascade = null;

    int scale = 1;

    CvPoint pt1 = new CvPoint(0, 0);

    CvPoint pt2 = new CvPoint(0, 0);

    CvPoint ch1 = new CvPoint(0, 0);

    CvPoint ch2 = new CvPoint(0, 0);

    CvPoint centeroid = new CvPoint(0, 0);

    int i;

    @Override
    public IplImage process(IplImage img) {
        if (cascade == null) {
            Loader.load(opencv_objdetect.class);
            cvLoad("haarcascades/haarcascade_frontalface_alt.xml");
            cascade = new CvHaarClassifierCascade(cvLoad("haarcascades/haarcascade_frontalface_alt.xml"));
            if (cascade == null) {
                LOG.error("Could not load classifier cascade");
                return img;
            }
        }
        if (storage == null) {
            storage = cvCreateMemStorage(0);
        }
        cvClearMemStorage(storage);
        if (cascade != null) {
            CvSeq faces = cvHaarDetectObjects(img, cascade, storage, 1.1, 2, CV_HAAR_DO_CANNY_PRUNING);
            for (i = 0; i < (faces != null ? faces.total() : 0); i++) {
                CvRect r = new CvRect(cvGetSeqElem(faces, i));
                pt1.x(r.x() * scale);
                pt2.x((r.x() + r.width()) * scale);
                pt1.y(r.y() * scale);
                pt2.y((r.y() + r.height()) * scale);
                cvRectangle(img, pt1, pt2, CV_RGB(255, 0, 0), 3, 8, 0);
                centeroid.x(r.x() + r.width() * scale / 2);
                centeroid.y(r.y() + r.height() * scale / 2);
                cvDrawLine(img, centeroid, centeroid, CV_RGB(255, 0, 0), 3, 8, 0);
                myService.invoke("publish", centeroid);
            }
        }
        buffer = img;
        return buffer;
    }
}
