package gov.lanl.arc.registry;

import java.util.*;

/**
 * TapeMap.java
 * 
 * A container which contains all prop of all tapes from database
 */
public class TapeMap {

    Map tapemap;

    public TapeMap() {
        Map tapemap = new HashMap();
    }

    public void addtape(String TapeName, Properties p) {
        tapemap.put((Object) TapeName, (Object) p);
    }

    public Map getmap() {
        return tapemap;
    }

    public Properties gettape(String TapeName) {
        return (Properties) tapemap.get(TapeName);
    }
}
