package net.sf.hdkp.resources;

import java.net.URL;
import net.sf.hdkp.Launcher;

public class Resources {

    private final String style;

    public Resources(Launcher.Mode mode) {
        this.style = mode.toString();
    }

    public URL[] getFrameIconResources() {
        return getResources(this.style + "-016.png", this.style + "-032.png", this.style + "-048.png", this.style + "-128.png");
    }

    public URL getSpinnerImageResource(String size) {
        return getResource("spinner-" + size + ".gif");
    }

    public URL getHeaderImageResource(String type) {
        return getResource(this.style + type + "-header.png");
    }

    public URL getCheckmarkImageResource() {
        return getResource("checkmark.png");
    }

    public URL getChooserImageResource(String type) {
        return getResource("chooser-" + type + ".png");
    }

    private URL[] getResources(String... names) {
        final URL[] urls = new URL[names.length];
        for (int i = 0; i < names.length; i++) {
            urls[i] = getResource(names[i]);
        }
        return urls;
    }

    private URL getResource(String name) {
        return getClass().getResource(name);
    }
}
