package org.fb4j.client.parser.json;

import java.util.Collections;
import java.util.List;
import net.sf.json.JSONArray;
import org.fb4j.client.InvalidResponseException;
import org.fb4j.client.MethodCall;

public class MultipleResultJsonParser<T> extends AbstractJsonParser<T> {

    public MultipleResultJsonParser(Class<?> entityImplClass, String expectedMethodName) {
        super(entityImplClass, expectedMethodName);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T parseInternal(MethodCall<T> call, String data) throws InvalidResponseException {
        return (T) parseList(data);
    }
}
