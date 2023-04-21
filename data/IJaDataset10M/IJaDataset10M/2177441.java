package com.turnengine.client.local.player.bean;

import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import com.turnengine.client.local.alliance.enums.AllianceMemberType;
import com.turnengine.client.local.player.enums.PlayerEmailAlerts;
import com.turnengine.client.local.unit.bean.IUnitCount;
import com.turnengine.client.local.unit.bean.UnitCountSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Player Serializer.
 */
public class PlayerSerializer implements IPlayerSerializer {

    @Override
    public IPlayer readObject(IDataReader reader) throws IOException {
        int id = reader.readInt();
        String name = reader.readString(true);
        int userId = reader.readInt();
        int factionId = reader.readInt();
        int requestId = reader.readInt();
        int allianceId = reader.readInt();
        int leaveTurns = reader.readInt();
        AllianceMemberType memberType = reader.readEnum(AllianceMemberType.class, true);
        PlayerEmailAlerts emailAlerts = reader.readEnum(PlayerEmailAlerts.class, true);
        List<IUnitCount> unitList = reader.readObjectCollection(new ArrayList<IUnitCount>(), true, false, new UnitCountSerializer());
        IPlayer object = new Player();
        object.setId(id);
        object.setName(name);
        object.setUserId(userId);
        object.setFactionId(factionId);
        object.setRequestId(requestId);
        object.setAllianceId(allianceId);
        object.setLeaveTurns(leaveTurns);
        object.setMemberType(memberType);
        object.setEmailAlerts(emailAlerts);
        object.setUnitList(unitList);
        return object;
    }

    public void writeObject(IDataWriter writer, IPlayer object) throws IOException {
        int id = object.getId();
        String name = object.getName();
        int userId = object.getUserId();
        int factionId = object.getFactionId();
        int requestId = object.getRequestId();
        int allianceId = object.getAllianceId();
        int leaveTurns = object.getLeaveTurns();
        AllianceMemberType memberType = object.getMemberType();
        PlayerEmailAlerts emailAlerts = object.getEmailAlerts();
        List<IUnitCount> unitList = object.getUnitList();
        writer.writeInt(id);
        writer.writeString(name, true);
        writer.writeInt(userId);
        writer.writeInt(factionId);
        writer.writeInt(requestId);
        writer.writeInt(allianceId);
        writer.writeInt(leaveTurns);
        writer.writeEnum(memberType, true);
        writer.writeEnum(emailAlerts, true);
        writer.writeObjectCollection(unitList, true, false, new UnitCountSerializer());
    }
}
