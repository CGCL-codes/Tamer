package com.shine.framework.DB4o.util;

import java.io.File;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * 基于Map实现的数据库Map表
 * 与其他Map实现类的区别在于此类将数据存储于物理文件而非内存
 * 
 * @author qun1988@gmail.com
 * @date 2012.04.25
 */
public class DB4oMap<K, V> extends AbstractMap<K, V> implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DB4OFILENAME = "db4o.dat";

    /** 数据库对象 */
    private ObjectContainer db;

    /** 数据库物理文件存放路径 */
    private String filePath;

    /** 是否让键值唯一，true时键/值对必须唯一，false时键/值对则可重复插入数据 */
    private boolean keyUnique = false;

    private transient Set<K> keySet;

    private transient Collection<V> values;

    private transient Set<Map.Entry<K, V>> entrySet;

    public DB4oMap() {
        this(Thread.currentThread().getContextClassLoader().getResource("").getPath() + UUID.randomUUID().toString() + DB4OFILENAME, false);
    }

    public DB4oMap(boolean keyUnique) {
        this(Thread.currentThread().getContextClassLoader().getResource("").getPath() + UUID.randomUUID().toString() + DB4OFILENAME, keyUnique);
    }

    public DB4oMap(String filePath) {
        this(filePath, false);
    }

    public DB4oMap(String filePath, boolean keyUnique) {
        this.keyUnique = keyUnique;
        this.filePath = filePath;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), filePath);
        addShutdowHook();
    }

    /**
	 * 初始化
	 */
    public void init() {
    }

    /**
	 * 将数据保存到Map中
	 * 注意!!!
	 * <code>keyUinque</code>为true时，如果Map先前已存在对应数据时会被新的<code>value</code>替换掉
	 * <code>keyUinque</code>为false时，Map则会对数据进行重复叠加
	 */
    @Override
    public V put(K key, V value) {
        check(key);
        V foundValue = null;
        if (this.keyUnique) {
            ObjectSet<Entry<K, V>> objects = db.queryByExample(new Entry<K, V>(key, null));
            while (objects.hasNext()) {
                Entry<K, V> entry = objects.next();
                if (entry.getKey().equals(key)) {
                    foundValue = entry.getValue();
                    db.delete(entry);
                }
            }
            ;
        }
        Entry<K, V> entry = new Entry<K, V>(key, value);
        db.store(entry);
        db.commit();
        return this.keyUnique ? foundValue : value;
    }

    /**
	 * 根据<code>key</code>获取Map中单条数据，不存在数据时返回 null
	 * 注意!!!如果<code>key</code>下有多条数据时只返回前面第一条数据
	 * 
	 * @param key
	 */
    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        check(key);
        ObjectSet<Entry<K, V>> objects = db.queryByExample(new Entry<K, V>((K) key, null));
        if (objects.hasNext()) {
            Entry<K, V> entry = objects.next();
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
	 * 根据<code>key</code>移除Map中单条数据，不存在数据时不做删除并返回null
	 * 注意!!!如果<code>key</code>下有多条数据时只删除前面第一条数据
	 * 
	 * @param  key
	 * @return 返回被删除的数据
	 */
    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        check(key);
        ObjectSet<Entry<K, V>> objects = db.queryByExample(new Entry<K, V>((K) key, null));
        Entry<K, V> entry = null;
        if (objects.hasNext() && (entry = objects.next()).getKey().equals(key)) {
            db.delete(entry);
            db.commit();
            return entry.getValue();
        }
        return null;
    }

    /**
	 * 移除指定<code>key</code><code>value</code>的数据
	 * 
	 * @param key
	 * @param value
	 */
    public Entry<K, V> remove(K key, V value) {
        check(key);
        ObjectSet<Entry<K, V>> objects = db.queryByExample(new Entry<K, V>(key, value));
        Entry<K, V> entry = null;
        if (objects.hasNext() && (entry = objects.next()).getKey().equals(key) && entry.getValue().equals(value)) {
            db.delete(entry);
            db.commit();
            return entry;
        }
        return null;
    }

    /**
	 * 清空Map中所有数据，物理文件所有数据将被清空
	 */
    @Override
    public void clear() {
        ObjectSet<?> objects = db.query().execute();
        while (objects.hasNext()) {
            db.delete(objects.next());
        }
        db.commit();
    }

    @Override
    public int size() {
        Entry<K, V> entry = new Entry<K, V>(null, null);
        return db.queryByExample(entry).size();
    }

    /**
	 * 判断Map中是否存在<code>key</code>
	 * 提示：判断的依据是<code>key</code>必须和Map中对应的键相互equals()
	 */
    @SuppressWarnings("unchecked")
    @Override
    public boolean containsKey(Object key) {
        check(key);
        ObjectSet<Entry<K, V>> objects = db.queryByExample(new Entry<K, V>((K) key, null));
        return objects.hasNext() ? objects.next().getKey().equals(key) : false;
    }

    /**
	 * 判断Map中是否存在<code>value</code>
	 * 提示：判断的依据是<code>value</code>必须和Map中对应的值相互equals()
	 */
    @SuppressWarnings("unchecked")
    @Override
    public boolean containsValue(Object value) {
        check(value);
        ObjectSet<Entry<K, V>> objects = db.queryByExample(new Entry<K, V>(null, (V) value));
        return objects.hasNext() ? objects.next().getValue().equals(value) : false;
    }

    @Override
    public Set<K> keySet() {
        Set<K> ks = keySet;
        return (ks != null ? ks : (keySet = new KeySet()));
    }

    @Override
    public Collection<V> values() {
        Collection<V> vs = values;
        return (vs != null ? vs : (values = new Values()));
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet());
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        check(map);
        for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i = map.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<? extends K, ? extends V> e = i.next();
            put(e.getKey(), e.getValue());
        }
    }

    /**
	 * 根据指定的<code>key</code>枚举所有符合条件的数据集
	 * 
	 * @param  key
	 * @return 数据集
	 */
    public List<V> getAll(K key) {
        check(key);
        ObjectSet<Entry<K, V>> objects = db.queryByExample(new Entry<K, V>(key, null));
        List<V> dataList = new ArrayList<V>();
        while (objects.hasNext()) {
            Entry<K, V> entry = objects.next();
            if (entry.getKey().equals(key)) {
                dataList.add(entry.getValue());
            }
        }
        return dataList;
    }

    /**
	 * 枚举所有数据集
	 * 
	 * @param  key
	 * @return 数据集
	 */
    public List<V> getAll() {
        ObjectSet<Entry<K, V>> objects = db.queryByExample(new Entry<K, V>(null, null));
        List<V> dataList = new ArrayList<V>();
        while (objects.hasNext()) {
            dataList.add(objects.next().getValue());
        }
        return dataList;
    }

    /**
	 * 删除所有数据
	 */
    public void removeAll() {
        clear();
    }

    /**
	 * 根据指定的<code>key</code>删除所有符合条件的数据
	 * 
	 * @param key
	 */
    public void removeAll(K key) {
        check(key);
        ObjectSet<Entry<K, V>> objects = db.queryByExample(new Entry<K, V>(key, null));
        while (objects.hasNext()) {
            Entry<K, V> entry = objects.next();
            if (entry.getKey().equals(key)) {
                db.delete(entry);
            }
        }
        db.commit();
    }

    /**
	 * 移除指定<code>key</code><code>value</code>的所有数据
	 * 
	 * @param key
	 * @param value
	 */
    public void removeAll(K key, V value) {
        check(key);
        ObjectSet<Entry<K, V>> objects = db.queryByExample(new Entry<K, V>(key, value));
        while (objects.hasNext()) {
            Entry<K, V> entry = objects.next();
            if (entry.getKey().equals(key) && entry.getValue().equals(value)) {
                db.delete(entry);
            }
        }
        db.commit();
    }

    /**
	 * 销毁，用于最后资源的释放操作
	 */
    public void destroy() {
        if (db != null) {
            db.close();
            db = null;
        }
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
	 * 检查参数
	 * 
	 * @param param
	 */
    private void check(Object param) {
        if (param == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
	 * 在JVM关闭的时候，释放资源
	 */
    private void addShutdowHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                DB4oMap.this.destroy();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private final Entry<K, V> removeMapping(Object obj) {
        Map.Entry<K, V> entry = (Map.Entry<K, V>) obj;
        ObjectSet<Entry<K, V>> objects = db.queryByExample(entry);
        Entry<K, V> removeEntry = null;
        if (objects.hasNext() && (removeEntry = objects.next()).getKey().equals(entry.getKey()) && removeEntry.getValue().equals(entry.getValue())) {
            db.delete(removeEntry);
            db.commit();
            return removeEntry;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private final Entry<K, V> getEntry(Object key) {
        check(key);
        ObjectSet<Entry<K, V>> objects = db.queryByExample(new Entry<K, V>((K) key, null));
        Entry<K, V> entry = objects.hasNext() ? objects.next() : null;
        if (entry != null && entry.getKey().equals(key)) {
            return entry;
        }
        return null;
    }

    public static class Entry<K, V> implements Map.Entry<K, V> {

        private final K key;

        private V value;

        public Entry(K k, V v) {
            this.key = k;
            this.value = v;
        }

        @Override
        public final K getKey() {
            return key;
        }

        @Override
        public final V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @SuppressWarnings("unchecked")
        @Override
        public final boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry e = (Map.Entry) o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2))) return true;
            }
            return false;
        }

        @Override
        public final int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }

        @Override
        public final String toString() {
            return getKey() + "=" + getValue();
        }
    }

    private final class KeySet extends AbstractSet<K> {

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return DB4oMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return DB4oMap.this.containsKey(o);
        }

        @Override
        public boolean remove(Object o) {
            return DB4oMap.this.remove(o) != null;
        }

        @Override
        public void clear() {
            DB4oMap.this.clear();
        }
    }

    private final class Values extends AbstractCollection<V> {

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return DB4oMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return DB4oMap.this.containsValue(o);
        }

        @Override
        public void clear() {
            DB4oMap.this.clear();
        }
    }

    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry<K, V> e = (Map.Entry<K, V>) o;
            Entry<K, V> candidate = getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }

        @Override
        public boolean remove(Object obj) {
            return DB4oMap.this.removeMapping(obj) != null;
        }

        @Override
        public int size() {
            return DB4oMap.this.size();
        }

        @Override
        public void clear() {
            DB4oMap.this.clear();
        }
    }

    private final class KeyIterator extends Db4oIterator<K> {

        @Override
        public K next() {
            return nextEntry().getKey();
        }
    }

    private final class ValueIterator extends Db4oIterator<V> {

        @Override
        public V next() {
            return nextEntry().getValue();
        }
    }

    private final class EntryIterator extends Db4oIterator<Map.Entry<K, V>> {

        @Override
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    private abstract class Db4oIterator<E> implements Iterator<E> {

        private ObjectSet<Entry<K, V>> objects;

        private Entry<K, V> current;

        public Db4oIterator() {
            Entry<K, V> entry = new Entry<K, V>(null, null);
            objects = db.queryByExample(entry);
        }

        @Override
        public final boolean hasNext() {
            return objects.hasNext();
        }

        protected final Entry<K, V> nextEntry() {
            if (!objects.hasNext()) throw new NoSuchElementException();
            Entry<K, V> entry = objects.next();
            current = entry;
            return entry;
        }

        @Override
        public void remove() {
            if (current == null) throw new IllegalStateException();
            db.delete(current);
        }
    }
}
