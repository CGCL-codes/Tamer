package com.tencent.tendon.convert.json.tokens;

import com.tencent.tendon.convert.Poolable;
import com.tencent.tendon.convert.json.*;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author nbzhang
 */
public final class JsonCharArrayToken extends JsonToken<char[]> {

    public static final JsonCharArrayToken instance = new JsonCharArrayToken();

    private static final JsonXListener listener = JsonXListener.getInstance();

    @Override
    public char[] convertFrom(final JsonReader in) {
        final JsonCharArrayList result = JsonCharArrayList.poll();
        in.checkArray();
        while (in.hasNext()) {
            result.add(listener.convertCharFrom(in));
        }
        char[] rs = result.toArray();
        JsonCharArrayList.offer(result);
        return rs;
    }

    @Override
    public Class<char[]> getType() {
        return char[].class;
    }

    public Class getComponentType() {
        return char.class;
    }

    @Override
    public boolean isSimpled() {
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public static final class JsonCharArrayList implements Poolable {

        private static final Queue<JsonCharArrayList> queue = new ArrayBlockingQueue<JsonCharArrayList>(Math.max(Runtime.getRuntime().availableProcessors(), 2) * 2);

        public static JsonCharArrayList poll() {
            JsonCharArrayList result = queue.poll();
            if (result == null) {
                result = new JsonCharArrayList();
            } else {
                result.prepare();
            }
            return result;
        }

        public static void offer(final JsonCharArrayList object) {
            if (object != null) {
                object.release();
                queue.offer(object);
            }
        }

        private char[] data;

        private int size;

        public JsonCharArrayList() {
            this(8);
        }

        public JsonCharArrayList(int initialCapacity) {
            this.data = new char[initialCapacity];
        }

        private void grow() {
            char[] newdata = new char[data.length + 8];
            System.arraycopy(data, 0, newdata, 0, size);
            this.data = newdata;
        }

        public void add(char e) {
            if (size == data.length) grow();
            data[size++] = e;
        }

        public char[] toArray() {
            if (size == data.length) return data;
            char[] newdata = new char[size];
            System.arraycopy(data, 0, newdata, 0, size);
            return newdata;
        }

        @Override
        public void prepare() {
        }

        @Override
        public void release() {
            this.size = 0;
            this.data = new char[8];
        }

        public void clear() {
            this.release();
        }
    }
}
