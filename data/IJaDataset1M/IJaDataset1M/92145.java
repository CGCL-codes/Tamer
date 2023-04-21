package com.turnengine.client.local.player.command;

import com.javabi.command.IExecutableCommand;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.serializer.AbstractCommandDataSerializer;
import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import java.io.IOException;

/**
 * The Get Player Action Queue Limit Data Serializer.
 */
public class GetPlayerActionQueueLimitDataSerializer extends AbstractCommandDataSerializer<Integer> implements IGetPlayerActionQueueLimitDataSerializer {

    /**
	 * Creates a new Get Player Action Queue Limit Data Serializer.
	 */
    public GetPlayerActionQueueLimitDataSerializer() {
        super(GetPlayerActionQueueLimit.COMMAND_ID);
    }

    @Override
    public Integer readReturnValue(IDataReader reader) throws IOException {
        Integer value = reader.readIntegerObject(true);
        return value;
    }

    @Override
    public void writeReturnValue(IDataWriter writer, Integer value) throws IOException {
        writer.writeIntegerObject(value, true);
    }

    @Override
    public IExecutableCommand<Integer> readRequest(IDataReader reader) throws IOException {
        long loginId = reader.readLong();
        int instanceId = reader.readInt();
        GetPlayerActionQueueLimit object = new GetPlayerActionQueueLimit();
        object.setLoginId(loginId);
        object.setInstanceId(instanceId);
        return object;
    }

    @Override
    public void writeRequest(IDataWriter writer, IExecutableCommand<Integer> argument) throws IOException {
        GetPlayerActionQueueLimit object = (GetPlayerActionQueueLimit) argument;
        long loginId = object.getLoginId();
        int instanceId = object.getInstanceId();
        writer.writeLong(loginId);
        writer.writeInt(instanceId);
    }

    @Override
    public IExecutableCommandResponse<Integer> newResponse() {
        return new GetPlayerActionQueueLimitResponse();
    }
}
