package com.idna.trace.web.transformer;

public interface Transformer<InObject, OutObject> {

    OutObject unmarshal(String string);

    String marshal(InObject object);
}
