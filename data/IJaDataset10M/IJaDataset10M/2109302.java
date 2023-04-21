package org.game.thyvin.resource;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.game.thyvin.logic.room.Room;
import org.game.thyvin.logic.room.ThyvinSampleNode;
import org.game.thyvin.logic.room.ThyvinSampleRoomMap;
import org.game.thyvin.logic.room.Unit;
import org.game.thyvin.logic.room.UnitClass;
import org.resource.FileConstants;
import org.utils.Integers;

/**
 * A writer that takes a room with
 * units and rooms and writes a file
 * from that.
 * 
 * @author Melvin Winstroem-Moeller
 *
 */
public class RoomUnitWriter {

    /**
	 * Intern means that the game defines the player,
	 * extern that the user defines the player.
	 * 
	 * The arrays must all be of equal length.
	 * Only for internal players must the corresponding
	 * fields in names and descriptions be non-null.
	 * 
	 */
    public void writeRoomUnit(Room room, String path, String roomPath, int format, boolean[] internPlayer, int[] team, String[] names) throws IOException {
        roomPath = roomPath.replaceAll(File.separatorChar == '\\' ? "\\\\" : File.separator, "\\/");
        switch(format) {
            case 3:
                writeRoomUnit3(room, path, roomPath, format, internPlayer, team, names);
                break;
            default:
                throw new IOException("Unrecognized format: " + format);
        }
    }

    private void writeRoomUnit3(Room room, String path, String roomPath, int format, boolean[] internPlayer, int[] team, String[] names) throws IOException {
        DataOutputStream stream = null;
        try {
            stream = new DataOutputStream(new FileOutputStream(path));
            int size = 0;
            final int headerSize = 100;
            int offsetPlayers = headerSize;
            int offsetRoom;
            int offsetUnit;
            int offsetUnitFile;
            size += headerSize;
            int playerNumber = team.length;
            byte[][] byteNames = new byte[playerNumber][];
            for (int i = 0; i < playerNumber; i++) {
                byteNames[i] = names[i].getBytes();
                size += 4 * 2 + 1 + byteNames[i].length;
            }
            offsetRoom = size;
            byte[] roomName = roomPath.getBytes();
            size += 4 + roomName.length;
            offsetUnit = size;
            int unitNumber = room.getMap().getUnitNodes().size();
            size += unitNumber * 4 * 4;
            offsetUnitFile = size;
            Set<UnitClass> unitClasses = new HashSet<UnitClass>();
            for (ThyvinSampleNode node : room.getMap().getUnitNodes()) {
                Unit unit = node.getUnit();
                unitClasses.add(unit.getUnitClass());
            }
            int unitFileNumber = unitClasses.size();
            byte[][] unitFileByteNames = new byte[unitFileNumber][];
            Map<UnitClass, Integer> unitFileRelativeOffsets = new HashMap<UnitClass, Integer>();
            {
                int tempSize = 0;
                int i = 0;
                for (UnitClass unitClass : unitClasses) {
                    unitFileByteNames[i] = unitClass.getPath().getBytes();
                    size += unitFileByteNames[i].length + 4;
                    unitFileRelativeOffsets.put(unitClass, tempSize);
                    tempSize += unitFileByteNames[i].length + 4;
                    i++;
                }
            }
            int offsetNode = size;
            ThyvinSampleRoomMap map = room.getMap();
            int nodeNumber = map.getMonumentResourceNodes().size() + map.getFortressNodes().size();
            size += nodeNumber * 8;
            byte[] fileContents = new byte[size];
            int offset;
            int offsetHeader = 8;
            Integers.putBigEndianInt(FileConstants.THYVIN_FORMAT, fileContents, 0);
            Integers.putBigEndianInt(FileConstants.ROOM_UNIT_FORMAT, fileContents, 4);
            Integers.putBigEndianInt(3, fileContents, offsetHeader + 0);
            Integers.putBigEndianInt(offsetPlayers, fileContents, offsetHeader + 4);
            Integers.putBigEndianInt(playerNumber, fileContents, offsetHeader + 8);
            Integers.putBigEndianInt(offsetRoom, fileContents, offsetHeader + 12);
            Integers.putBigEndianInt(offsetUnit, fileContents, offsetHeader + 16);
            Integers.putBigEndianInt(unitNumber, fileContents, offsetHeader + 20);
            Integers.putBigEndianInt(offsetNode, fileContents, offsetHeader + 24);
            Integers.putBigEndianInt(nodeNumber, fileContents, offsetHeader + 28);
            offset = 100;
            for (int i = 0; i < playerNumber; i++) {
                fileContents[offset] = (byte) (internPlayer[i] ? 1 : 0);
                offset++;
                Integers.putBigEndianInt(team[i], fileContents, offset);
                offset += 4;
                Integers.putBigEndianInt(byteNames[i].length, fileContents, offset);
                offset += 4;
                System.arraycopy(byteNames[i], 0, fileContents, offset, byteNames[i].length);
                offset += byteNames[i].length;
            }
            if (offset != offsetRoom) {
                throw new AssertionError("The internal offsets doesn't match up:\n" + "room offset: " + offsetRoom + ",  current offset: " + offset);
            }
            byte[] roomByteName = roomPath.getBytes();
            Integers.putBigEndianInt(roomByteName.length, fileContents, offset);
            offset += 4;
            System.arraycopy(roomByteName, 0, fileContents, offset, roomByteName.length);
            offset += roomByteName.length;
            if (offset != offsetUnit) {
                throw new AssertionError("The internal offsets doesn't match up.");
            }
            for (ThyvinSampleNode node : room.getMap().getUnitNodes()) {
                Unit unit = node.getUnit();
                Integers.putBigEndianInt(node.getId(), fileContents, offset);
                offset += 4;
                Integers.putBigEndianInt(unit.getCurrentHealth(), fileContents, offset);
                offset += 4;
                Integers.putBigEndianInt(unit.getPlayerId(), fileContents, offset);
                offset += 4;
                Integers.putBigEndianInt(offsetUnitFile + unitFileRelativeOffsets.get(unit.getUnitClass()), fileContents, offset);
                offset += 4;
            }
            if (offset != offsetUnitFile) {
                throw new AssertionError("The internal offsets doesn't match up\n" + "unit file offset: " + offsetUnitFile + ",  current offset: " + offset);
            }
            for (int i = 0; i < unitFileByteNames.length; i++) {
                byte[] byteName = unitFileByteNames[i];
                Integers.putBigEndianInt(byteName.length, fileContents, offset);
                offset += 4;
                System.arraycopy(byteName, 0, fileContents, offset, byteName.length);
                offset += byteName.length;
            }
            if (offset != offsetNode) {
                throw new AssertionError("The internal offsets doesn't match up\n" + "node teams offset: " + offsetNode + ",  current offset: " + offset);
            }
            for (ThyvinSampleNode node : room.getMap().getMonumentResourceNodes()) {
                Integers.putBigEndianInt(node.getId(), fileContents, offset);
                offset += 4;
                Integers.putBigEndianInt(node.getResourceElement().getTeam(), fileContents, offset);
                offset += 4;
            }
            for (ThyvinSampleNode node : room.getMap().getFortressNodes()) {
                Integers.putBigEndianInt(node.getId(), fileContents, offset);
                offset += 4;
                Integers.putBigEndianInt(node.getFortressElement().getTeam(), fileContents, offset);
                offset += 4;
            }
            if (offset != size) {
                throw new AssertionError("The internal offsets doesn't match up.");
            }
            stream.write(fileContents);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
