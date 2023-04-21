package com.marist.cs2011;

import static com.googlecode.javacv.cpp.opencv_core.*;

public interface JCVKinectDelegate {

    /**
	 * Is called every time the kinect gives a new RGB camera frame. image is a 3 channel, 8U IplImage.
	 * @param image
	 * @param device
	 */
    void onRecievedRGBFrame(IplImage image, JCVKinect device);

    /**
	 * Is called every time the kinect gives a new RGB camera frame. image is a 1 channel, 8U IplImage.
	 * @param image
	 * @param device
	 */
    void onRecievedDepthFrame(IplImage image, JCVKinect device);
}
