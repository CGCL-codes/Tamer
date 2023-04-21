package com.free2be.dimensions.device;

public interface Software extends DeviceComponent {

    String getOsName();

    String getOsVendor();

    Browser getBrowser();

    WapBrowser getWapBrowser();

    DeviceImage getImage();

    Audio getAudio();

    Java getJava();

    String getPreferredContentType();
}
