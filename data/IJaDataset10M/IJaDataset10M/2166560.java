package org.nakedobjects.nos.client.dnd.util;

import java.util.StringTokenizer;
import org.nakedobjects.nof.core.conf.Configuration;
import org.nakedobjects.nof.core.conf.ConfigurationException;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nos.client.dnd.drawing.Location;
import org.nakedobjects.nos.client.dnd.drawing.Size;

public class Properties {

    public static final String PROPERTY_BASE = Configuration.ROOT + "viewer.dnd.";

    public static Size getSize(String name, Size defaultSize) {
        String initialSize = NakedObjectsContext.getConfiguration().getString(name);
        if (initialSize != null) {
            StringTokenizer st = new StringTokenizer(initialSize, "x");
            if (st.countTokens() == 2) {
                int width = 0;
                int height = 0;
                width = Integer.valueOf(st.nextToken().trim()).intValue();
                height = Integer.valueOf(st.nextToken().trim()).intValue();
                return new Size(width, height);
            } else {
                throw new ConfigurationException("Size not specified correctly in " + name + ": " + initialSize);
            }
        }
        return defaultSize;
    }

    public static Location getLocation(String name, Location defaultLocation) {
        String initialLocation = NakedObjectsContext.getConfiguration().getString(name);
        if (initialLocation != null) {
            StringTokenizer st = new StringTokenizer(initialLocation, ",");
            if (st.countTokens() == 2) {
                int x = 0;
                int y = 0;
                x = Integer.valueOf(st.nextToken().trim()).intValue();
                y = Integer.valueOf(st.nextToken().trim()).intValue();
                return new Location(x, y);
            } else {
                throw new ConfigurationException("Location not specified correctly in " + name + ": " + initialLocation);
            }
        }
        return defaultLocation;
    }
}
