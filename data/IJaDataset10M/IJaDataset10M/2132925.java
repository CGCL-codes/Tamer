package photorganizer.common.context;

import photorganizer.common.lifecycle.Closeable;

public class ContextHome {

    private static final ThreadLocal<Context> contexts = new ThreadLocal<Context>();

    public static Context getContext() {
        return contexts.get();
    }

    public static void setContext(Context context) {
        contexts.set(context);
    }

    /**
	 * @see Closeable#close()
	 */
    public static void close() {
        Context context = getContext();
        if (context != null) {
            context.close();
        }
    }

    /**
	 * @see Context#get(Object)
	 */
    public static Object get(Object key) {
        Context context = getContext();
        return context.get(key);
    }

    /**
	 * @see Context#put(Object, Object)
	 */
    public static void put(Object key, Object value) {
        Context context = getContext();
        context.put(key, value);
    }

    private ContextHome() {
        assert false;
    }
}
