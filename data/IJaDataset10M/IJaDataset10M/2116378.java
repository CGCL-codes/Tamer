package com.twolattes.json.types;

import static com.twolattes.json.Json.string;
import java.net.URI;
import java.net.URISyntaxException;
import com.twolattes.json.Json;

public class URIType extends NullSafeType<URI, Json.String> {

    @Override
    protected Json.String nullSafeMarshall(URI entity) {
        return string(entity.toString());
    }

    @Override
    protected URI nullSafeUnmarshall(Json.String object) {
        try {
            return new URI(object.getString());
        } catch (URISyntaxException e) {
            throw new IllegalStateException();
        }
    }

    public Class<URI> getReturnedClass() {
        return URI.class;
    }
}
