package rb.media.webcam;

import javax.media.CaptureDeviceInfo;

public class WebCam {

    private CaptureDeviceInfo cdi;

    public WebCam() {
        cdi = new CaptureDeviceInfo();
    }

    public WebCam(CaptureDeviceInfo cdiValue) {
        cdi = cdiValue;
    }

    public CaptureDeviceInfo getCdi() {
        return cdi;
    }

    public void setCdi(CaptureDeviceInfo cdi) {
        this.cdi = cdi;
    }

    public String getName() {
        String name = cdi.getName();
        return name;
    }

    public void initialize() {
    }
}
