package com.aelitis.azureus.core.cnetwork.impl;

import com.aelitis.azureus.core.cnetwork.ContentNetwork;

public class ContentNetworkVuze extends ContentNetworkVuzeGeneric {

    private static final String DEFAULT_ADDRESS = "client.vuze.com";

    private static final String DEFAULT_PORT = "80";

    private static final String DEFAULT_RELAY_ADDRESS = "www.vuze.com";

    private static final String DEFAULT_RELAY_PORT = "80";

    private static final String DEFAULT_EXT_ADDRESS = "www.vuze.com";

    private static final String URL_ADDRESS = System.getProperty("platform_address", DEFAULT_ADDRESS);

    private static final String URL_PORT = System.getProperty("platform_port", DEFAULT_PORT);

    private static final String URL_PREFIX = "http://" + URL_ADDRESS + ":" + URL_PORT + "/";

    private static final String URL_EXT_PREFIX = "http://" + System.getProperty("platform_address_ext", DEFAULT_EXT_ADDRESS) + ":" + System.getProperty("platform_port_ext", DEFAULT_PORT) + "/";

    private static final String DEFAULT_AUTHORIZED_RPC = "https://" + URL_ADDRESS + ":443/rpc";

    private static String URL_RELAY_RPC = System.getProperty("relay_url", "http://" + System.getProperty("relay_address", DEFAULT_RELAY_ADDRESS) + ":" + System.getProperty("relay_port", DEFAULT_RELAY_PORT) + "/msgrelay/rpc");

    private static final String URL_AUTHORIZED_RPC = System.getProperty("authorized_rpc", "1").equals("1") ? DEFAULT_AUTHORIZED_RPC : URL_PREFIX + "app";

    private static final String URL_FAQ = "http://faq.vuze.com/";

    private static final String URL_BLOG = "http://blog.vuze.com/";

    private static final String URL_FORUMS = "http://forum.vuze.com/";

    private static final String URL_WIKI = "http://wiki.vuze.com/";

    protected ContentNetworkVuze(ContentNetworkManagerImpl manager) {
        super(manager, ContentNetwork.CONTENT_NETWORK_VUZE, 1, "Vuze HD Network", null, null, URL_ADDRESS, URL_PREFIX, null, URL_RELAY_RPC, URL_AUTHORIZED_RPC, URL_FAQ, URL_BLOG, URL_FORUMS, URL_WIKI, URL_EXT_PREFIX);
    }
}
