package net.asfun.jvalog.resource;

import java.util.Collection;
import net.asfun.jvalog.common.Jdoer;
import net.asfun.jvalog.common.JdoerFactory;
import net.asfun.jvalog.entity.Map;
import net.asfun.jvalog.vo.Config;

public class MapDao {

    public static Collection<Map> listAll() {
        return JdoerFactory.jdoMap.all();
    }

    public static Map getOrSaveByName(String type, String name, String value) {
        Jdoer<Map> jdm = JdoerFactory.jdoMap;
        Map map = jdm.unique("item == '" + name + "' && type == '" + Config.TYPE_BLOG + "'");
        if (map == null) {
            map = new Map();
            map.setItem(name);
            map.setValue(value);
            map.setType(Config.TYPE_BLOG);
            jdm.save(map);
        }
        return map;
    }

    public static int getPageSize() {
        return getBlogProperty("pageSize", 10);
    }

    public static int getRecentPosts() {
        return getBlogProperty("recentPosts", 5);
    }

    public static int getRecentComms() {
        return getBlogProperty("recentComments", 5);
    }

    public static int getRssSize() {
        return getBlogProperty("rssSize", 20);
    }

    public static int getAtomSize() {
        return getBlogProperty("atomSize", 20);
    }

    public static int getMapSize() {
        return getBlogProperty("mapSize", 50);
    }

    public static int getTimezone() {
        return getBlogProperty("timezone", 8);
    }

    private static int getBlogProperty(String name, int value) {
        try {
            return Integer.parseInt(getBlogProperty(name, "" + value));
        } catch (Exception e) {
            return value;
        }
    }

    public static String getBlogProperty(String name, String value) {
        Map map = getOrSaveByName(Config.TYPE_BLOG, name, value);
        return map.getValue();
    }

    public static int getCheckComm() {
        return getBlogProperty("toCheckComment", 1);
    }
}
