package gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author lord_rex
 */
public class HTMLConfig {

    @Property(key = "html.root", defaultValue = "./data/static_data/HTML/")
    public static String HTML_ROOT;

    @Property(key = "html.cache.file", defaultValue = "./html.cache")
    public static String HTML_CACHE_FILE;

    @Property(key = "html.encoding", defaultValue = "UTF-8")
    public static String HTML_ENCODING;
}
