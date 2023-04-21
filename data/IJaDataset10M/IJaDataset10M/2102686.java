package jcckit.util;

import java.applet.Applet;

/**
 *  Implementation of {@link FlatConfigData} based on
 *  <tt>java.applet.Applet</tt>.
 *
 *  @author Franz-Josef Elmer
 */
public class AppletBasedConfigData extends FlatConfigData {

    private final Applet _applet;

    /**
   * Creates an instance based on the specified applet.
   * The path is undefined.
   */
    public AppletBasedConfigData(Applet applet) {
        this(applet, null);
    }

    /** Creates an instance based on the specified properties and path. */
    private AppletBasedConfigData(Applet applet, String path) {
        super(path);
        _applet = applet;
    }

    protected String getValue(String fullKey) {
        return _applet.getParameter(fullKey);
    }

    protected ConfigData createConfigData(String path) {
        return new AppletBasedConfigData(_applet, path);
    }
}
