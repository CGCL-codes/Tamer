package com.turnengine.client.local.location.bean;

import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import com.turnengine.client.local.location.enums.LocationSignup;
import com.turnengine.client.local.unit.bean.IUnitCount;
import com.turnengine.client.local.unit.bean.UnitCountSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Location Serializer.
 */
public class LocationSerializer implements ILocationSerializer {

    @Override
    public ILocation readObject(IDataReader reader) throws IOException {
        int id = reader.readInt();
        String name = reader.readString(true);
        int groupId = reader.readInt();
        int playerId = reader.readInt();
        LocationSignup signup = reader.readEnum(LocationSignup.class, true);
        int signupFactionId = reader.readInt();
        int[] coordinates = reader.readIntArray(false);
        List<IUnitCount> locationUnitList = reader.readObjectCollection(new ArrayList<IUnitCount>(), true, false, new UnitCountSerializer());
        List<IUnitCount> mobileUnitList = reader.readObjectCollection(new ArrayList<IUnitCount>(), true, false, new UnitCountSerializer());
        ILocation object = new Location();
        object.setId(id);
        object.setName(name);
        object.setGroupId(groupId);
        object.setPlayerId(playerId);
        object.setSignup(signup);
        object.setSignupFactionId(signupFactionId);
        object.setCoordinates(coordinates);
        object.setLocationUnitList(locationUnitList);
        object.setMobileUnitList(mobileUnitList);
        return object;
    }

    public void writeObject(IDataWriter writer, ILocation object) throws IOException {
        int id = object.getId();
        String name = object.getName();
        int groupId = object.getGroupId();
        int playerId = object.getPlayerId();
        LocationSignup signup = object.getSignup();
        int signupFactionId = object.getSignupFactionId();
        int[] coordinates = object.getCoordinates();
        List<IUnitCount> locationUnitList = object.getLocationUnitList();
        List<IUnitCount> mobileUnitList = object.getMobileUnitList();
        writer.writeInt(id);
        writer.writeString(name, true);
        writer.writeInt(groupId);
        writer.writeInt(playerId);
        writer.writeEnum(signup, true);
        writer.writeInt(signupFactionId);
        writer.writeIntArray(coordinates, false);
        writer.writeObjectCollection(locationUnitList, true, false, new UnitCountSerializer());
        writer.writeObjectCollection(mobileUnitList, true, false, new UnitCountSerializer());
    }
}
