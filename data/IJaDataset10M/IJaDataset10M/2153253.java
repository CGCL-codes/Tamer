package com.turnengine.client.local.alliance.command;

import com.javabi.command.IExecutableCommand;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.serializer.AbstractCommandDataSerializer;
import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import com.turnengine.client.local.alliance.bean.AllianceSerializer;
import com.turnengine.client.local.alliance.bean.IAlliance;
import java.io.IOException;

/**
 * The Form Alliance Data Serializer.
 */
public class FormAllianceDataSerializer extends AbstractCommandDataSerializer<IAlliance> implements IFormAllianceDataSerializer {

    /**
	 * Creates a new Form Alliance Data Serializer.
	 */
    public FormAllianceDataSerializer() {
        super(FormAlliance.COMMAND_ID);
    }

    @Override
    public IAlliance readReturnValue(IDataReader reader) throws IOException {
        IAlliance value = new AllianceSerializer().readObject(reader);
        return value;
    }

    @Override
    public void writeReturnValue(IDataWriter writer, IAlliance value) throws IOException {
        new AllianceSerializer().writeObject(writer, value);
    }

    @Override
    public IExecutableCommand<IAlliance> readRequest(IDataReader reader) throws IOException {
        long loginId = reader.readLong();
        int instanceId = reader.readInt();
        String name = reader.readString(true);
        FormAlliance object = new FormAlliance();
        object.setLoginId(loginId);
        object.setInstanceId(instanceId);
        object.setName(name);
        return object;
    }

    @Override
    public void writeRequest(IDataWriter writer, IExecutableCommand<IAlliance> argument) throws IOException {
        FormAlliance object = (FormAlliance) argument;
        long loginId = object.getLoginId();
        int instanceId = object.getInstanceId();
        String name = object.getName();
        writer.writeLong(loginId);
        writer.writeInt(instanceId);
        writer.writeString(name, true);
    }

    @Override
    public IExecutableCommandResponse<IAlliance> newResponse() {
        return new FormAllianceResponse();
    }
}
