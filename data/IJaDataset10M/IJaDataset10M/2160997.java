package com.dyuproject.protostuff.benchmark.serializers.model;

import java.io.ByteArrayOutputStream;
import org.codehaus.jackson.JsonParser;
import com.dyuproject.protostuff.benchmark.V2LiteMedia;
import com.dyuproject.protostuff.benchmark.V2LiteMedia.MediaContent;
import com.dyuproject.protostuff.benchmark.serializers.AbstractLiteMediaSerializer;
import com.dyuproject.protostuff.json.ReflectionJSON;

/**
 * @author David Yu
 * @created Oct 2, 2009
 */
public class ReflectionLiteSerializer extends AbstractLiteMediaSerializer {

    final ReflectionJSON pbJSON = new ReflectionJSON(new Class[] { V2LiteMedia.class });

    public MediaContent deserialize(byte[] array) throws Exception {
        V2LiteMedia.MediaContent.Builder builder = V2LiteMedia.MediaContent.newBuilder();
        JsonParser parser = pbJSON.getJsonFactory().createJsonParser(array);
        pbJSON.mergeFrom(parser, builder);
        parser.close();
        return builder.build();
    }

    public String getName() {
        return "reflection-lite-json";
    }

    public byte[] serialize(MediaContent content) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(512);
        pbJSON.writeTo(out, content);
        return out.toByteArray();
    }
}
