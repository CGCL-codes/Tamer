package com.turnengine.client.global.user.command;

import com.javabi.command.IExecutableCommand;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.serializer.AbstractCommandDataSerializer;
import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import com.turnengine.client.global.user.bean.IUser;
import com.turnengine.client.global.user.bean.UserSerializer;
import java.io.IOException;

/**
 * The Get User By Name Data Serializer.
 */
public class GetUserByNameDataSerializer extends AbstractCommandDataSerializer<IUser> implements IGetUserByNameDataSerializer {

    /**
	 * Creates a new Get User By Name Data Serializer.
	 */
    public GetUserByNameDataSerializer() {
        super(GetUserByName.COMMAND_ID);
    }

    @Override
    public IUser readReturnValue(IDataReader reader) throws IOException {
        IUser value = new UserSerializer().readObject(reader);
        return value;
    }

    @Override
    public void writeReturnValue(IDataWriter writer, IUser value) throws IOException {
        new UserSerializer().writeObject(writer, value);
    }

    @Override
    public IExecutableCommand<IUser> readRequest(IDataReader reader) throws IOException {
        long loginId = reader.readLong();
        String name = reader.readString(true);
        GetUserByName object = new GetUserByName();
        object.setLoginId(loginId);
        object.setName(name);
        return object;
    }

    @Override
    public void writeRequest(IDataWriter writer, IExecutableCommand<IUser> argument) throws IOException {
        GetUserByName object = (GetUserByName) argument;
        long loginId = object.getLoginId();
        String name = object.getName();
        writer.writeLong(loginId);
        writer.writeString(name, true);
    }

    @Override
    public IExecutableCommandResponse<IUser> newResponse() {
        return new GetUserByNameResponse();
    }
}
