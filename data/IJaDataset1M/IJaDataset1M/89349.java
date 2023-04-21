package antirashka.map.impl;

import antirashka.map.IMap;
import antirashka.map.generation.GenerationConfig;
import antirashka.map.items.IPC;
import java.util.List;

public final class MapFactory {

    public static IMap createMap(GenerationConfig config, List<IPC> pcs) {
        return new MapImpl(config, pcs);
    }
}
