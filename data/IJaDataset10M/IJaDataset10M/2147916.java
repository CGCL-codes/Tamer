package org.matsim.utils.vis.otfivs.interfaces;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.matsim.utils.vis.otfivs.caching.SceneGraph;
import org.matsim.utils.vis.otfivs.data.OTFData;

public abstract class OTFDataReader {

    public static Map<String, Class> previousVersions = new HashMap<String, Class>();

    public static Class getPreviousVersion(String identifier) {
        return previousVersions.get(identifier);
    }

    public static boolean setPreviousVersion(String identifier, Class clazz) {
        previousVersions.put(identifier, clazz);
        return true;
    }

    public static String getVersionString(int major, int minor) {
        return "V" + major + "." + minor;
    }

    public abstract void readConstData(ByteBuffer in) throws IOException;

    public abstract void readDynData(ByteBuffer in, SceneGraph graph) throws IOException;

    public abstract void connect(OTFData.Receiver receiver);

    public abstract void invalidate(SceneGraph graph);
}
