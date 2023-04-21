package com.customwars.client.io.converter;

import com.customwars.client.model.gameobject.Terrain;
import com.customwars.client.model.gameobject.TerrainConnection;
import com.customwars.client.tools.UCaseMap;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.util.Collection;
import java.util.Map;

/**
 * Read all Terrains from an xml file
 * Each terrain inherits all the statistics from the base terrain.
 * The base terrain can be retrieved from baseTerrains by the 'type' parameter.
 *
 * A 'horizontal road' with type 'road' inherits all the statistics from the 'road' base terrain
 */
public class TerrainXmlConverter implements Converter {

    Map<String, Terrain> baseTerrains = new UCaseMap<Terrain>();

    private int terrainID;

    private String terrainName;

    private String terrainType;

    private String spansOverType;

    private TerrainConnection connections;

    public TerrainXmlConverter(Collection<Terrain> baseTerrains) {
        for (Terrain terrain : baseTerrains) {
            this.baseTerrains.put(terrain.getName(), terrain);
        }
    }

    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        throw new UnsupportedOperationException("Terrains only have read from xml support");
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
        readTerrain(reader);
        Terrain terrainCopy = createTerrainCopy(terrainType);
        writeFields(terrainCopy);
        return terrainCopy;
    }

    private void readTerrain(HierarchicalStreamReader reader) {
        terrainID = Integer.parseInt(reader.getAttribute("id"));
        terrainName = reader.getAttribute("name");
        terrainType = reader.getAttribute("type").toUpperCase();
        spansOverType = reader.getAttribute("spansOver") != null ? reader.getAttribute("spansOver").toUpperCase() : "";
        connections = ConvertUtil.readConnectionNode(reader, getBaseTerrain(terrainType));
    }

    private Terrain createTerrainCopy(String terrainType) {
        Terrain baseTerrain = getBaseTerrain(terrainType);
        baseTerrain.init();
        return new Terrain(baseTerrain);
    }

    private Terrain getBaseTerrain(String type) {
        if (!baseTerrains.containsKey(type)) {
            throw new IllegalArgumentException(type + " is not a base terrain");
        }
        return baseTerrains.get(type);
    }

    private void writeFields(Terrain terrain) {
        Terrain baseTerrain = getBaseTerrain(terrainType);
        ConvertUtil.writeField("id", terrain, Terrain.class, terrainID);
        ConvertUtil.writeField("name", terrain, Terrain.class, terrainName == null ? baseTerrain.getName() : terrainName);
        ConvertUtil.writeField("type", terrain, Terrain.class, terrainType);
        ConvertUtil.writeField("spansOverType", terrain, Terrain.class, spansOverType);
        ConvertUtil.writeField("connection", terrain, Terrain.class, connections);
    }

    public boolean canConvert(Class aClass) {
        return aClass == Terrain.class;
    }
}
