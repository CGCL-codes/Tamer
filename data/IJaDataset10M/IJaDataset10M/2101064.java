package org.minions.stigma.game.map.data;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.minions.stigma.databases.parsers.Parsable;
import org.minions.stigma.databases.xml.Converter;
import org.minions.stigma.game.map.TerrainSet;
import org.minions.stigma.game.map.TerrainType;

/**
 * Plain Old Java Object holding all data needed for
 * creation of {@link TerrainSet}.
 */
@XmlRootElement(name = "terrainset")
@XmlType(propOrder = {  })
public class TerrainSetData implements Parsable {

    private static final String CURRENT_VERSION = "2.0";

    private String version;

    private List<TerrainTypeData> terrainList;

    private short id;

    private String name;

    private String description;

    /**
     * Converter between {@link TerrainSetData} and
     * {@link TerrainSet}.
     */
    public static class DataConverter implements Converter<TerrainSet, TerrainSetData> {

        private Converter<TerrainType, TerrainTypeData> terrainConverter = new TerrainTypeData.DataConverter();

        /** {@inheritDoc} */
        @Override
        public TerrainSet buildObject(TerrainSetData data) {
            TerrainSet r = new TerrainSet(data.id);
            r.setName(data.name);
            r.setDescription(data.description);
            for (TerrainTypeData t : data.terrainList) r.addTerrainType(terrainConverter.buildObject(t));
            return r;
        }

        /** {@inheritDoc} */
        @Override
        public TerrainSetData buildData(TerrainSet object) {
            List<TerrainTypeData> list = new LinkedList<TerrainTypeData>();
            for (TerrainType tt : object.getTerrainTypes()) list.add(terrainConverter.buildData(tt));
            return new TerrainSetData(list, object.getId(), object.getName(), object.getDescription());
        }
    }

    /**
     * Default constructor (for JAXB).
     */
    public TerrainSetData() {
        this.id = -1;
        this.terrainList = new LinkedList<TerrainTypeData>();
    }

    /**
     * Constructor.
     * @param terrainList
     *            terrain for this terrain set
     * @param id
     *            id of this terrain set
     * @param name
     *            name of this terrain set
     * @param description
     *            description of this terrain set
     */
    public TerrainSetData(List<TerrainTypeData> terrainList, short id, String name, String description) {
        this.terrainList = terrainList;
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = CURRENT_VERSION;
    }

    /**
     * Returns id.
     * @return id
     */
    @XmlAttribute(required = true)
    public short getId() {
        return id;
    }

    /**
     * Sets new value of id.
     * @param id
     *            the id to set
     */
    public void setId(short id) {
        this.id = id;
    }

    /**
     * Returns terrainList.
     * @return terrainList
     */
    @XmlElementWrapper
    @XmlElements(value = @XmlElement(name = "terrain", type = TerrainTypeData.class))
    public List<TerrainTypeData> getTerrainList() {
        return terrainList;
    }

    /** {@inheritDoc} */
    @Override
    public String getNewestVersion() {
        return CURRENT_VERSION;
    }

    /** {@inheritDoc} */
    @XmlAttribute(required = true)
    @Override
    public String getVersion() {
        return version;
    }

    /**
     * Sets new version.
     * @param version
     *            new version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Returns name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets new value of name.
     * @param name
     *            the name to set
     */
    @XmlElement(name = "name", required = false)
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns description.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets new value of description.
     * @param description
     *            the description to set
     */
    @XmlElement(name = "desc", required = false)
    public void setDescription(String description) {
        this.description = description;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGood() {
        if (id == -1 || terrainList == null || terrainList.isEmpty()) return false;
        for (TerrainTypeData t : terrainList) if (!t.isGood()) return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        String newline = System.getProperty("line.separator");
        StringBuffer out = new StringBuffer();
        out.append("id: ").append(id).append(newline);
        out.append("name: ").append(name).append(newline);
        out.append("description: ").append(description).append(newline);
        out.append("tilesMap:").append(newline);
        for (TerrainTypeData tt : terrainList) out.append("[" + tt.getId() + ": " + tt.isPassable());
        return out.toString();
    }
}
