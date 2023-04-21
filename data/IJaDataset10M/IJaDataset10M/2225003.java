package com.endigi.frame.base.util.springext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import com.endigi.frame.base.util.gsonext.CIncludeAnnotationSerializationExclusionStrategy;
import com.endigi.frame.base.util.gsonext.TransientAnnotationSerializationExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class MappingGsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private GsonBuilder gsonBuilder = new GsonBuilder();

    public MappingGsonHttpMessageConverter() {
        super(new MediaType("application", "json", DEFAULT_CHARSET));
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(inputMessage.getBody(), "UTF-8"));
        Object o = gson.fromJson(reader, clazz);
        reader.close();
        return o;
    }

    @Override
    protected boolean supports(Class<?> arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getHeaders().setContentType((new MediaType("application", "json", DEFAULT_CHARSET)));
        Gson gson = gsonBuilder.serializeNulls().setExclusionStrategies(new TransientAnnotationSerializationExclusionStrategy(), new CIncludeAnnotationSerializationExclusionStrategy()).create();
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputMessage.getBody(), "UTF-8"));
        gson.toJson(o, o.getClass(), writer);
        writer.close();
    }
}
