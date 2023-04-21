package edu.washington.assist.map;

import java.io.IOException;
import java.util.Properties;

public interface MapInfoReader {

    public MapInfo readMapInfo(Properties props) throws IOException;
}
