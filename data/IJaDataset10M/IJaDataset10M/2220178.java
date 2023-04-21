package net.infonode.docking.internal;

import net.infonode.docking.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.4 $
 */
public class ReadContext {

    private RootWindow rootWindow;

    private int version;

    private boolean propertyValuesAvailable;

    private boolean readPropertiesEnabled;

    public RootWindow getRootWindow() {
        return rootWindow;
    }

    public ViewSerializer getViewSerializer() {
        return rootWindow.getViewSerializer();
    }

    public boolean isPropertyValuesAvailable() {
        return propertyValuesAvailable;
    }

    public boolean getReadPropertiesEnabled() {
        return readPropertiesEnabled;
    }

    /**
   * @return returns the serialized version
   */
    public int getVersion() {
        return version;
    }
}
