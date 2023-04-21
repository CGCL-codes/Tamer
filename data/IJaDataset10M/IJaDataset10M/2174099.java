package com.turnengine.client.global.user.command;

import com.javabi.command.IExecutableCommand;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.serializer.AbstractCommandDataSerializer;
import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import com.turnengine.client.global.game.enums.GameLevel;
import com.turnengine.client.global.user.enums.UserPermissionType;
import java.io.IOException;

/**
 * The Set User Game Permission Data Serializer.
 */
public class SetUserGamePermissionDataSerializer extends AbstractCommandDataSerializer<Boolean> implements ISetUserGamePermissionDataSerializer {

    /**
	 * Creates a new Set User Game Permission Data Serializer.
	 */
    public SetUserGamePermissionDataSerializer() {
        super(SetUserGamePermission.COMMAND_ID);
    }

    @Override
    public Boolean readReturnValue(IDataReader reader) throws IOException {
        Boolean value = reader.readBooleanObject(true);
        return value;
    }

    @Override
    public void writeReturnValue(IDataWriter writer, Boolean value) throws IOException {
        writer.writeBooleanObject(value, true);
    }

    @Override
    public IExecutableCommand<Boolean> readRequest(IDataReader reader) throws IOException {
        long loginId = reader.readLong();
        int userId = reader.readInt();
        int id = reader.readInt();
        UserPermissionType type = reader.readEnum(UserPermissionType.class, true);
        GameLevel level = reader.readEnum(GameLevel.class, true);
        SetUserGamePermission object = new SetUserGamePermission();
        object.setLoginId(loginId);
        object.setUserId(userId);
        object.setId(id);
        object.setType(type);
        object.setLevel(level);
        return object;
    }

    @Override
    public void writeRequest(IDataWriter writer, IExecutableCommand<Boolean> argument) throws IOException {
        SetUserGamePermission object = (SetUserGamePermission) argument;
        long loginId = object.getLoginId();
        int userId = object.getUserId();
        int id = object.getId();
        UserPermissionType type = object.getType();
        GameLevel level = object.getLevel();
        writer.writeLong(loginId);
        writer.writeInt(userId);
        writer.writeInt(id);
        writer.writeEnum(type, true);
        writer.writeEnum(level, true);
    }

    @Override
    public IExecutableCommandResponse<Boolean> newResponse() {
        return new SetUserGamePermissionResponse();
    }
}
