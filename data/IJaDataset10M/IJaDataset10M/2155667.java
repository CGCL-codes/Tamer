package aoetec.javalang._411collections;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import aoetec.util.lession.Lesson;

@Lesson(title = "Map<K,V> 接口", lastModifed = "2008/02/25", keyword = { "在处理元素前用于保存元素的 collection", "插入、移除、检查" }, content = { "1 Map<K,V> 接口 的定义", "   public interface Map<K,V> {                         ", "       // Basic operations                             ", "       V put(K key, V value);                          ", "       V get(Object key);                              ", "       V remove(Object key);                           ", "       boolean containsKey(Object key);                ", "       boolean containsValue(Object value);            ", "       int size();                                     ", "       boolean isEmpty();                              ", "                                                       ", "       // Bulk operations                              ", "       void putAll(Map<? extends K, ? extends V> m);   ", "       void clear();                                   ", "                                                       ", "       // Collection Views                             ", "       public Set<K> keySet();                         ", "       public Collection<V> values();                  ", "       public Set<Map.Entry<K,V>> entrySet();          ", "                                                       ", "       // Interface for entrySet elements              ", "       public interface Entry {                        ", "           K getKey();                                 ", "           V getValue();                               ", "           V setValue(V value);                        ", "       }                                               ", "   }                                                   ", "                                                                                         ", "2 JDK 里提供的 Map<K,V> 接口 的实现", "                            java.util.Map                                      ", "          _________________________*_________________________                  ", "          |                        |                        |                  ", "   java.util.HashMap       java.util.TreeMap     java.util.LinkedHashMap       ", "  （基本实现 效率最高）   （按键值的自然顺序排序）   （按元素的插入顺序排序）       ", "    ", "" })
public class A05_Map {

    public static void main(String[] args) {
        bulkOperations();
    }

    static void basicOperations() {
        String[] strs = { "i", "came", "i", "saw", "i", "left" };
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String str : strs) {
            Integer freq = map.get(str);
            map.put(str, freq == null ? 1 : ++freq);
        }
        System.out.println(map.size() + " distinct words");
        System.out.println(map);
    }

    static void bulkOperations() {
        Map<Integer, String> originMap = new HashMap<Integer, String>();
        originMap.put(1, "one");
        originMap.put(2, "two");
        originMap.put(3, "three");
        System.out.println("originMap=" + originMap);
        Map<Number, String> unionMap = new HashMap<Number, String>();
        unionMap.put(3.0, "three");
        unionMap.put(4, "four");
        unionMap.put(5, "five");
        System.out.println("unionMap =" + unionMap);
        unionMap.putAll(originMap);
        System.out.println("unionMap =" + unionMap);
        unionMap.clear();
        System.out.println("unionMap =" + unionMap);
    }

    static void differenceAmongImpls() {
        Map<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("one", 100);
        hashMap.put("two", 200);
        hashMap.put("three", 300);
        System.out.println("hashMap=" + hashMap);
        Map<String, Integer> treeMap = new TreeMap<String, Integer>();
        treeMap.put("one", 100);
        treeMap.put("two", 200);
        treeMap.put("three", 300);
        System.out.println("treeMap=" + treeMap);
        Map<String, Integer> linkMap = new LinkedHashMap<String, Integer>();
        linkMap.put("one", 100);
        linkMap.put("two", 200);
        linkMap.put("three", 300);
        System.out.println("linkMap=" + linkMap);
    }
}
