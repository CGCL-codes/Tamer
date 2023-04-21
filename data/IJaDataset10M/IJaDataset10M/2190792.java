package jk.spider.mod.plugin;

import jk.spider.api.event.EventSink;

public interface Plugin extends EventSink {

    public static final String PLUGIN_OUT = "[PLUGIN] - ";

    public String getName();

    public String getVersion();

    public String getDescription();
}
