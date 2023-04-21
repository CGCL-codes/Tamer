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
 * The Get User By Login Id Data Serializer.
 */
public class GetUserByLoginIdDataSerializer extends AbstractCommandDataSerializer<IUser> implements IGetUserByLoginIdDataSerializer {

    /**
	 * Creates a new Get User By Login Id Data Serializer.
	 */
    public GetUserByLoginIdDataSerializer() {
        super(GetUserByLoginId.COMMAND_ID);
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
        long id = reader.readLong();
        GetUserByLoginId object = new GetUserByLoginId();
        object.setLoginId(loginId);
        object.setId(id);
        return object;
    }

    @Override
    public void writeRequest(IDataWriter writer, IExecutableCommand<IUser> argument) throws IOException {
        GetUserByLoginId object = (GetUserByLoginId) argument;
        long loginId = object.getLoginId();
        long id = object.getId();
        writer.writeLong(loginId);
        writer.writeLong(id);
    }

    @Override
    public IExecutableCommandResponse<IUser> newResponse() {
        return new GetUserByLoginIdResponse();
    }
}
