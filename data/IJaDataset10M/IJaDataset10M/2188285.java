package com.turnengine.client.local.player.command;

import com.javabi.command.IExecutableCommand;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.serializer.AbstractCommandDataSerializer;
import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import com.turnengine.client.local.player.bean.IPlayer;
import com.turnengine.client.local.player.bean.PlayerSerializer;
import java.io.IOException;

/**
 * The Get Player By Id Data Serializer.
 */
public class GetPlayerByIdDataSerializer extends AbstractCommandDataSerializer<IPlayer> implements IGetPlayerByIdDataSerializer {

    /**
	 * Creates a new Get Player By Id Data Serializer.
	 */
    public GetPlayerByIdDataSerializer() {
        super(GetPlayerById.COMMAND_ID);
    }

    @Override
    public IPlayer readReturnValue(IDataReader reader) throws IOException {
        IPlayer value = new PlayerSerializer().readObject(reader);
        return value;
    }

    @Override
    public void writeReturnValue(IDataWriter writer, IPlayer value) throws IOException {
        new PlayerSerializer().writeObject(writer, value);
    }

    @Override
    public IExecutableCommand<IPlayer> readRequest(IDataReader reader) throws IOException {
        long loginId = reader.readLong();
        int instanceId = reader.readInt();
        int id = reader.readInt();
        GetPlayerById object = new GetPlayerById();
        object.setLoginId(loginId);
        object.setInstanceId(instanceId);
        object.setId(id);
        return object;
    }

    @Override
    public void writeRequest(IDataWriter writer, IExecutableCommand<IPlayer> argument) throws IOException {
        GetPlayerById object = (GetPlayerById) argument;
        long loginId = object.getLoginId();
        int instanceId = object.getInstanceId();
        int id = object.getId();
        writer.writeLong(loginId);
        writer.writeInt(instanceId);
        writer.writeInt(id);
    }

    @Override
    public IExecutableCommandResponse<IPlayer> newResponse() {
        return new GetPlayerByIdResponse();
    }
}
