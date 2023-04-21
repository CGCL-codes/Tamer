package javacommon.util.single;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton 类 (单态类,只有一个实例存在)
 * 存放内存数据
 * @author bzq (kingter)
 *
 */
public class Singleton {

    /**通过HASHMAP*/
    public static Map hashmap = new HashMap();

    private Singleton() {
    }

    private static Singleton instance = new Singleton();

    public static Singleton getInstance() {
        return instance;
    }
}
