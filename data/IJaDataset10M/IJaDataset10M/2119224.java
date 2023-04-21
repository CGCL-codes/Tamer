package org.minions.stigma.game.map;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.minions.stigma.globals.Position;
import org.minions.utils.logger.Log;

/**
 * Class which represent gate to another map. It hold
 * information about its id, tiles which it possess and and
 * the destination's map id and destination entrance. It is
 * connected with MapType class, please update MapType class
 * version after changing Exit.
 */
@XmlRootElement(name = "exit")
public class Exit {

    private List<Position> positionsList;

    private short destMap;

    private byte destEntrance;

    /**
     * Constructor.
     * @param destMap
     *            id of destination map
     * @param destGateId
     *            id of destination's map destination gate
     */
    public Exit(short destMap, byte destGateId) {
        this.destEntrance = destGateId;
        this.destMap = destMap;
        this.positionsList = new LinkedList<Position>();
        if (Log.isTraceEnabled()) {
            Log.logger.trace("CREATED: Exit: destination map id:" + this.destMap + "and destination map gate number: " + this.destEntrance);
        }
    }

    /**
     * Default constructor needed by JAXB.
     */
    public Exit() {
        this.destEntrance = -1;
        this.destMap = -1;
        this.positionsList = new LinkedList<Position>();
    }

    /**
     * Adds position to list with positions where gate is
     * placed.
     * @param position
     *            position of tile to add to gate
     */
    public void addPosition(Position position) {
        positionsList.add(position);
        if (Log.isTraceEnabled()) {
            Log.logger.trace("ADDED: To Exit to map: " + this.destMap + "Position:" + position.getX() + "," + position.getY());
        }
    }

    /**
     * Returns list of positions which are part of exit.
     * @return list of positions which are part of exit
     */
    public List<Position> getPositionsList() {
        return positionsList;
    }

    /**
     * Sets list of positions which are part of exit.
     * @param positionsList
     *            position's list to set
     */
    @XmlElement(name = "pos", required = true)
    public void setPositionsList(List<Position> positionsList) {
        this.positionsList = positionsList;
    }

    /**
     * Returns destination map type id.
     * @return destination map type id
     */
    public short getDestMap() {
        return destMap;
    }

    /**
     * Sets destination map type id.
     * @param destMap
     *            map id to set
     */
    @XmlAttribute(name = "destMap", required = true)
    public void setDestMap(short destMap) {
        this.destMap = destMap;
    }

    /**
     * Returns destination map entrance id.
     * @return destination map entrance id
     */
    public byte getDestEntrance() {
        return destEntrance;
    }

    /**
     * Sets destination map entrance id.
     * @param destEntrance
     *            new entrance id
     */
    @XmlAttribute(name = "destEntrance", required = true)
    public void setDestEntrance(byte destEntrance) {
        this.destEntrance = destEntrance;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("destMap: ").append(destMap).append('\n');
        out.append("destGateId: ").append(destEntrance).append('\n');
        out.append("positionsList:\n");
        for (Position pos : positionsList) {
            out.append(pos.toString() + " ");
        }
        out.append("\n");
        return out.toString();
    }
}
