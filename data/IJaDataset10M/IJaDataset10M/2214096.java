package com.turnengine.client.global.translation.command;

import com.javabi.command.IExecutableCommand;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.serializer.AbstractCommandDataSerializer;
import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import com.javabi.common.locale.LanguageCountry;
import com.turnengine.client.global.translation.bean.ITranslation;
import com.turnengine.client.global.translation.bean.TranslationSerializer;
import java.io.IOException;

/**
 * The Get Translation By Id Data Serializer.
 */
public class GetTranslationByIdDataSerializer extends AbstractCommandDataSerializer<ITranslation> implements IGetTranslationByIdDataSerializer {

    /**
	 * Creates a new Get Translation By Id Data Serializer.
	 */
    public GetTranslationByIdDataSerializer() {
        super(GetTranslationById.COMMAND_ID);
    }

    @Override
    public ITranslation readReturnValue(IDataReader reader) throws IOException {
        ITranslation value = new TranslationSerializer().readObject(reader);
        return value;
    }

    @Override
    public void writeReturnValue(IDataWriter writer, ITranslation value) throws IOException {
        new TranslationSerializer().writeObject(writer, value);
    }

    @Override
    public IExecutableCommand<ITranslation> readRequest(IDataReader reader) throws IOException {
        int id = reader.readInt();
        LanguageCountry language = reader.readEnum(LanguageCountry.class, true);
        GetTranslationById object = new GetTranslationById();
        object.setId(id);
        object.setLanguage(language);
        return object;
    }

    @Override
    public void writeRequest(IDataWriter writer, IExecutableCommand<ITranslation> argument) throws IOException {
        GetTranslationById object = (GetTranslationById) argument;
        int id = object.getId();
        LanguageCountry language = object.getLanguage();
        writer.writeInt(id);
        writer.writeEnum(language, true);
    }

    @Override
    public IExecutableCommandResponse<ITranslation> newResponse() {
        return new GetTranslationByIdResponse();
    }
}
