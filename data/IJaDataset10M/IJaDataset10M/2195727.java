package com.google.code.ssm.transcoders;

import java.util.Map;
import com.google.code.ssm.providers.CacheTranscoder;

/**
 * 
 * Supports adding new transcoders.
 * 
 * @author Jakub Białek
 * @since 2.0.0
 * 
 */
public class JsonTranscodersConfigurer {

    private final JsonTranscoders jsonTranscoders = new JsonTranscoders();

    public void setTranscoders(final Map<Class<?>, CacheTranscoder<?>> transcoders) {
        jsonTranscoders.getTranscoders().putAll(transcoders);
    }
}
