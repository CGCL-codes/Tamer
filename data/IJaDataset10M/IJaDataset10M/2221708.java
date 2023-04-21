package de.schlund.pfixcore.webservice.jsonws;

import java.io.IOException;
import java.io.Writer;

public class JSONSerializer {

    boolean classHinting = false;

    SerializerRegistry registry;

    public JSONSerializer(SerializerRegistry registry) {
        this.registry = registry;
    }

    public JSONSerializer(SerializerRegistry registry, boolean classHinting) {
        this.registry = registry;
        this.classHinting = classHinting;
    }

    public void serialize(Object obj, Writer writer) throws SerializationException, IOException {
        SerializationContext ctx = new SerializationContext(registry, classHinting);
        ctx.serialize(obj, writer);
    }
}
