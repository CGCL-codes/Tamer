package com.jawise.serviceadapter.convert.soap.type;

import com.jawise.serviceadapter.convert.soap.binding.MessagePartReader;
import com.jawise.serviceadapter.convert.soap.binding.MessagePartWriter;

public class SourceType extends SoapType {

    @Override
    public Object read(MessagePartReader reader) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(Object object, MessagePartWriter writer) throws Exception {
        throw new UnsupportedOperationException();
    }
}
