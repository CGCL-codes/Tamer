package com.langerra.server.channel;

import com.langerra.shared.channel.ChannelService;

/**
 * 
 * TODO(kaz) currently the channel service allow duplicates message during a fan
 * out.
 * 
 * so we need to implements a new ChannelServiceEntry<String,
 * 
 * */
public class ChannelServiceFactory {

    static ChannelService impl;

    public static String getNamespace(String channelName) {
        return "CHAN-" + channelName + "_";
    }

    public static ChannelService getChannelService() {
        if (impl == null) {
            impl = getChannelService("com.langerra.server.channel.impl.AppEngineChannelServiceImpl");
        }
        return impl;
    }

    /**
	 * register the channel service implementation
	 * 
	 * @param className
	 *          the name of a default constructable {@code ChannelService}
	 *          implementation
	 * @return the instance of the class
	 * */
    @SuppressWarnings("unchecked")
    public static ChannelService getChannelService(String className) {
        try {
            Class<? extends ChannelService> clazz = (Class<? extends ChannelService>) Class.forName(className);
            return impl = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
