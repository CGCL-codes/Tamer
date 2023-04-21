package jifx.connection.translator.iso;

import java.util.Map;
import jifx.commons.mapper.ejb.IMapper;
import jifx.commons.messages.IMessage;
import jifx.connection.translator.TranslateException;
import jifx.connection.translator.generics.AbstractTranslator;
import jifx.message.iso8583.ISOMessageFactory;

public class CreateRetiroRequest extends AbstractTranslator {

    public IMessage translate(Map<String, Object> context, IMapper mapper, IMessage orig, IMessage proc) throws TranslateException {
        return ISOMessageFactory.createATMRequest("0");
    }
}
