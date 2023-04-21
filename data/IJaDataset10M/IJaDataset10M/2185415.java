package org.apache.felix.upnp.basedriver.importer.core.upnp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.cybergarage.upnp.Icon;
import org.osgi.service.upnp.UPnPIcon;
import org.apache.felix.upnp.basedriver.importer.util.HTTPRequestForIcon;
import org.apache.felix.upnp.basedriver.importer.util.ParseLocation;

public class UPnPIconImpl implements UPnPIcon {

    private Icon icon;

    private org.cybergarage.upnp.Device cyberdev;

    public UPnPIconImpl(Icon cybericon, org.cybergarage.upnp.Device cyberdev) {
        this.icon = cybericon;
        this.cyberdev = cyberdev;
    }

    public String getMimeType() {
        return icon.getMimeType();
    }

    public int getWidth() {
        String width = icon.getWidth();
        if (width.length() == 0) {
            return -1;
        }
        return Integer.parseInt(width);
    }

    public int getHeight() {
        String higth = icon.getHeight();
        if (higth.length() == 0) {
            return -1;
        }
        return Integer.parseInt(higth);
    }

    public int getSize() {
        return -1;
    }

    public int getDepth() {
        String depth = icon.getDepth();
        if (depth.length() == 0) {
            return -1;
        }
        return Integer.parseInt(depth);
    }

    public InputStream getInputStream() throws IOException {
        String urlString = ParseLocation.getUrlBase(cyberdev.getLocation()) + icon.getURL();
        URL url = new URL(urlString);
        HTTPRequestForIcon requestor = new HTTPRequestForIcon(url);
        return requestor.getInputStream();
    }
}
