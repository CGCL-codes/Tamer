package com.tencent.tendon.convert.json;

import com.tencent.tendon.convert.*;

/**
 *  解析时给对象字段赋值的操作接口， 一个字段一个JsonDeHandle
 *
 * @author nbzhang
 */
public abstract class JsonDeHandle<T, V> extends ConvertDeHandle<char[], T, V> {

    public static final JsonDeHandle<Object, Object> NULL = JsonDeHandles.createJsonDeHandle(null, null, null);

    public static final JsonDeHandle<Object, Object> REFER = JsonDeHandles.createJsonDeHandle(null, null, null);

    public final char[] fields;

    public JsonDeHandle(JsonDeHandle<T, V> self, JsonDeHandle<T, ?> next) {
        super(self, next);
        this.fields = self.fields;
    }

    public JsonDeHandle(final String field) {
        super(field);
        this.fields = field.toCharArray();
    }

    void setParent(JsonDeHandle<T, ?> parent) {
        this.parent = parent;
    }

    public abstract void set(JsonReader in, T obj);

    public final void set(ConvertInput<char[]> in, T obj) {
        this.set((JsonReader) in, obj);
    }
}
