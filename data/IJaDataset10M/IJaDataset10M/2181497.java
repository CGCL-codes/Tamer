package org.openfast.codec;

import org.openfast.dictionary.DictionaryRegistry;
import org.openfast.fast.impl.FastImplementation;
import org.openfast.template.Field;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Scalar;

public interface CodecFactory {

    MessageCodec createMessageCodec(int id, MessageTemplate template, FastImplementation implementation, DictionaryRegistry dictionaryRegistry);

    FieldCodec createScalarCodec(MessageTemplate template, Scalar scalar, FastImplementation fastImplementation, DictionaryRegistry dictionaryRegistry);

    FieldCodec createCompositeCodec(MessageTemplate template, Field field, FastImplementation implementation, DictionaryRegistry dictionaryRegistry);

    FieldCodec createGroupCodec(MessageTemplate template, Field field, FastImplementation implementation, DictionaryRegistry dictionaryRegistry);
}
