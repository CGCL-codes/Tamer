package au.edu.jcu.v4l4j;

import au.edu.jcu.v4l4j.DeviceInfo;
import au.edu.jcu.v4l4j.ImageFormat;
import au.edu.jcu.v4l4j.RawFrameGrabber;
import au.edu.jcu.v4l4j.Tuner;
import au.edu.jcu.v4l4j.exceptions.ImageFormatException;

public class RawFrameGrabberExt extends RawFrameGrabber {

    RawFrameGrabberExt(DeviceInfo di, long o, int w, int h, int ch, int std, Tuner t, ImageFormat imf) throws ImageFormatException {
        super(di, o, w, h, ch, std, t, imf);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
