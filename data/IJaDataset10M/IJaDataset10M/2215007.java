package org.game.thyvin.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sf.thyvin.core.res.IResource;
import net.sf.thyvin.core.res.IResourceLocator;
import net.sf.thyvin.core.res.ResourceUtilities;
import org.game.thyvin.logic.room.Room;
import org.game.thyvin.logic.room.ThyvinSampleNode;
import org.game.thyvin.logic.room.ThyvinSampleRoomMap;
import org.game.thyvin.logic.room.Unit;
import org.game.thyvin.logic.room.UnitClass;
import org.resource.FileConstants;
import org.resource.ResourceLoader;
import org.utils.Integers;

public class RoomUnitLoader implements ResourceLoader {

    protected final IResourceLocator resourceLocator;

    protected final RoomMapLoader roomMapLoader;

    protected final UnitClassLoader unitClassLoader;

    protected final UnitClassManager unitClassManager = new UnitClassManager();

    public RoomUnitLoader(IResourceLocator resourceLocator) {
        this(resourceLocator, null, null);
    }

    public RoomUnitLoader(IResourceLocator resourceLocator, RoomMapLoader roomMapLoader, UnitClassLoader unitClassLoader) {
        this.resourceLocator = resourceLocator;
        if (roomMapLoader == null) {
            this.roomMapLoader = new RoomMapLoader();
        } else {
            this.roomMapLoader = roomMapLoader;
        }
        if (unitClassLoader == null) {
            this.unitClassLoader = new UnitClassLoader();
        } else {
            this.unitClassLoader = unitClassLoader;
        }
    }

    public Room loadResource(IResource resource) throws IOException, Exception {
        unitClassManager.clear();
        if (resource == null) {
            throw new IOException("Loading of unit room failed: no resource found.");
        }
        byte[] fileContents = ResourceUtilities.getResourceAsBuffer(resource);
        int firstNumber = Integers.readBigEndian(fileContents, 0);
        if (firstNumber == 2) {
            return loadResource2(resource, fileContents);
        } else if (firstNumber != FileConstants.THYVIN_FORMAT) {
            throw new IOException("Unrecognized format: expected 2 or " + Integer.toHexString(FileConstants.THYVIN_FORMAT) + ", but received: " + Integer.toHexString(firstNumber));
        }
        int roomUnitNumber = Integers.readBigEndian(fileContents, 4);
        if (roomUnitNumber != FileConstants.ROOM_UNIT_FORMAT) {
            throw new IOException("Unrecognized format: expected " + Integer.toHexString(FileConstants.ROOM_UNIT_FORMAT) + ", but received: " + Integer.toHexString(roomUnitNumber));
        }
        int versionNumber = Integers.readBigEndian(fileContents, 8);
        switch(versionNumber) {
            case 2:
                return loadResource2(resource, fileContents);
            case 3:
                return loadResource3(resource, fileContents);
            default:
                throw new IOException("Unsupported version: " + versionNumber);
        }
    }

    private Room loadResource2(IResource resource, byte[] fileContents) throws IOException, Exception {
        int playerOffset = Integers.readBigEndian(fileContents, 4);
        int playerNumber = Integers.readBigEndian(fileContents, 8);
        int roomOffset = Integers.readBigEndian(fileContents, 12);
        int unitOffset = Integers.readBigEndian(fileContents, 16);
        int unitNumber = Integers.readBigEndian(fileContents, 20);
        int offset = playerOffset;
        PlayerLoadingInfo[] playerData = new PlayerLoadingInfo[playerNumber];
        for (int i = 0; i < playerNumber; i++) {
            offset++;
            int team = Integers.readBigEndian(fileContents, offset);
            offset += 4;
            int nameSize = Integers.readBigEndian(fileContents, offset);
            offset += 4;
            String name = new String(fileContents, offset, nameSize);
            offset += nameSize;
            playerData[i] = new PlayerLoadingInfo(name, team);
        }
        offset = roomOffset;
        Room room;
        {
            int nameSize = Integers.readBigEndian(fileContents, offset);
            String name;
            IResource roomRes;
            offset += 4;
            name = new String(fileContents, offset, nameSize);
            try {
                roomRes = resourceLocator.requireResource(name);
            } catch (IOException e) {
                throw new FileNotFoundException("Counld find " + name + " which is required for " + resource.getName());
            }
            room = new Room(roomMapLoader.loadResource(roomRes));
        }
        {
            Map<Integer, String> offsetToUnitClass = new HashMap<Integer, String>();
            ThyvinSampleRoomMap map = room.getMap();
            final int unitSize = 16;
            for (int i = 0; i < unitNumber; i++) {
                offset = unitOffset + unitSize * i;
                int unitClassOffset = Integers.readBigEndian(fileContents, offset + 3 * 4);
                String unitClassPath = offsetToUnitClass.get(unitClassOffset);
                if (unitClassPath == null) {
                    int nameSize = Integers.readBigEndian(fileContents, unitClassOffset);
                    unitClassPath = new String(fileContents, unitClassOffset + 4, nameSize);
                    offsetToUnitClass.put(unitClassOffset, unitClassPath);
                    IResource unitRes;
                    try {
                        unitRes = resourceLocator.requireResource(unitClassPath);
                    } catch (IOException e) {
                        throw new FileNotFoundException("Counld find " + unitClassPath + " which is required for " + resource.getName());
                    }
                    UnitClass unitClass = unitClassLoader.loadResource(unitRes);
                    unitClassManager.setUnitClass(unitClassPath, unitClass);
                }
                Unit unit = new Unit(unitClassManager.getUnitClass(unitClassPath));
                unit.setCurrentHealth(Integers.readBigEndian(fileContents, offset + 1 * 4));
                int playerIndex = Integers.readBigEndian(fileContents, offset + 2 * 4);
                unit.setPlayerId(playerIndex);
                unit.setTeam(playerData[playerIndex].getTeam());
                map.setNodeUnit(Integers.readBigEndian(fileContents, offset), unit);
            }
        }
        room.setPlayerInfo(playerData);
        return room;
    }

    private Room loadResource3(IResource resource, byte[] fileContents) throws IOException, Exception {
        int playerOffset = Integers.readBigEndian(fileContents, 4 * 3);
        int playerNumber = Integers.readBigEndian(fileContents, 4 * 4);
        int roomOffset = Integers.readBigEndian(fileContents, 4 * 5);
        int unitOffset = Integers.readBigEndian(fileContents, 4 * 6);
        int unitNumber = Integers.readBigEndian(fileContents, 4 * 7);
        int nodeOffset = Integers.readBigEndian(fileContents, 4 * 8);
        int nodeNumber = Integers.readBigEndian(fileContents, 4 * 9);
        int offset = playerOffset;
        PlayerLoadingInfo[] playerData = new PlayerLoadingInfo[playerNumber];
        for (int i = 0; i < playerNumber; i++) {
            offset++;
            int team = Integers.readBigEndian(fileContents, offset);
            offset += 4;
            int nameSize = Integers.readBigEndian(fileContents, offset);
            offset += 4;
            String name = new String(fileContents, offset, nameSize);
            offset += nameSize;
            playerData[i] = new PlayerLoadingInfo(name, team);
        }
        offset = roomOffset;
        Room room;
        {
            int nameSize = Integers.readBigEndian(fileContents, offset);
            String name;
            IResource roomRes;
            offset += 4;
            name = new String(fileContents, offset, nameSize);
            try {
                roomRes = resourceLocator.requireResource(name);
            } catch (IOException e) {
                throw new FileNotFoundException("Counld find " + name + " which is required for " + resource.getName());
            }
            room = new Room(roomMapLoader.loadResource(roomRes));
        }
        {
            Map<Integer, String> offsetToUnitClass = new HashMap<Integer, String>();
            ThyvinSampleRoomMap map = room.getMap();
            final int unitSize = 16;
            for (int i = 0; i < unitNumber; i++) {
                offset = unitOffset + unitSize * i;
                int unitClassOffset = Integers.readBigEndian(fileContents, offset + 3 * 4);
                String unitClassPath = offsetToUnitClass.get(unitClassOffset);
                if (unitClassPath == null) {
                    int nameSize = Integers.readBigEndian(fileContents, unitClassOffset);
                    unitClassPath = new String(fileContents, unitClassOffset + 4, nameSize);
                    offsetToUnitClass.put(unitClassOffset, unitClassPath);
                    IResource unitRes;
                    try {
                        unitRes = resourceLocator.requireResource(unitClassPath);
                    } catch (IOException e) {
                        throw new FileNotFoundException("Counld find " + unitClassPath + " which is required for " + resource.getName());
                    }
                    UnitClass unitClass = unitClassLoader.loadResource(unitRes);
                    unitClassManager.setUnitClass(unitClassPath, unitClass);
                }
                Unit unit = new Unit(unitClassManager.getUnitClass(unitClassPath));
                unit.setCurrentHealth(Integers.readBigEndian(fileContents, offset + 1 * 4));
                int playerIndex = Integers.readBigEndian(fileContents, offset + 2 * 4);
                unit.setPlayerId(playerIndex);
                unit.setTeam(playerData[playerIndex].getTeam());
                map.setNodeUnit(Integers.readBigEndian(fileContents, offset), unit);
            }
        }
        room.setPlayerInfo(playerData);
        for (int i = 0; i < nodeNumber; i++) {
            int index = i * 8 + nodeOffset;
            final int nodeId = Integers.readBigEndian(fileContents, index);
            final int team = Integers.readBigEndian(fileContents, index + 4);
            ThyvinSampleNode node = room.getMap().getNode(nodeId);
            if (node.getResourceElement() != null) {
                node.getResourceElement().setTeam(team);
            } else if (node.getFortressElement() != null) {
                node.getFortressElement().setTeam(team);
            }
        }
        return room;
    }

    public Unit loadUnit(String unitClassPath) throws IOException, Exception {
        return new Unit(unitClassManager.getUnitClass(unitClassPath));
    }

    public UnitClass loadUnitClass(String unitClassPath) throws IOException, Exception {
        IResource unitRes = resourceLocator.requireResource(unitClassPath);
        UnitClass unitClass = unitClassLoader.loadResource(unitRes);
        unitClassManager.setUnitClass(unitClassPath, unitClass);
        return unitClass;
    }

    class UnitClassManager {

        private Map<String, UnitClass> unitPathToUnitClass = new HashMap<String, UnitClass>();

        public UnitClass getUnitClass(String unitPath) throws IOException, Exception {
            UnitClass u = unitPathToUnitClass.get(unitPath);
            if (u == null) {
                loadUnitClass(unitPath);
                u = unitPathToUnitClass.get(unitPath);
            }
            return u;
        }

        public void clear() {
            unitPathToUnitClass.clear();
        }

        public void setUnitClass(String unitPath, UnitClass unitClass) {
            unitPathToUnitClass.put(unitPath, unitClass);
        }
    }
}
