package org.apache.struts.flow.sugar;

import org.mozilla.javascript.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * Wraps Java objects by adding support for function extensions, which are
 * functions that extend existing Java objects at the Rhino level.
 */
public class SugarWrapFactory extends WrapFactory {

    private List<ExtensionEntry> functionRegistry = new ArrayList<ExtensionEntry>();

    private Map<Class<?>, Map<String, Method>> functionMappings = new HashMap<Class<?>, Map<String, Method>>();

    public SugarWrapFactory() {
        super();
        addExtensionFunctions(CollectionExtensions.class);
        addExtensionFunctions(ListExtensions.class);
        addExtensionFunctions(FileExtensions.class);
        addExtensionFunctions(InputStreamExtensions.class);
    }

    public void addExtensionFunction(Class<?> cls, String name, Method func) {
        int modifier = func.getModifiers();
        if (Modifier.isStatic(modifier) && Modifier.isPublic(modifier)) {
            ExtensionEntry entry = new ExtensionEntry(cls, name, func);
            functionRegistry.add(entry);
        } else {
            throw new IllegalArgumentException("Method " + func + " must be static and public");
        }
    }

    public void addExtensionFunctions(Class<?> holder) {
        Method[] methods = holder.getDeclaredMethods();
        for (int x = 0; x < methods.length; x++) {
            int modifier = methods[x].getModifiers();
            if (Modifier.isStatic(modifier) && Modifier.isPublic(modifier)) {
                String name = methods[x].getName();
                Class<?> target = methods[x].getParameterTypes()[0];
                ExtensionEntry entry = new ExtensionEntry(target, name, methods[x]);
                functionRegistry.add(entry);
            }
        }
    }

    /**
     * Wrap Java object as Scriptable instance to allow full access to its
     * methods and fields from JavaScript.
     * <p>
     * {@link #wrap(Context, Scriptable, Object, Class)} and
     * {@link #wrapNewObject(Context, Scriptable, Object)} call this method
     * when they can not convert <tt>javaObject</tt> to JavaScript primitive
     * value or JavaScript array.
     * @param cx the current Context for this thread
     * @param scope the scope of the executing script
     * @param javaObject the object to be wrapped
     * @param staticType type hint. If security restrictions prevent to wrap
                object based on its class, staticType will be used instead.
     * @return the wrapped value which shall not be null
     */
    @SuppressWarnings("unchecked")
    public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class staticType) {
        Map<String, Method> map = getExtensionFunctions(javaObject.getClass());
        Scriptable wrap = null;
        if (javaObject instanceof Map) {
            wrap = new ScriptableMap(scope, javaObject, staticType, map);
        } else if (javaObject instanceof List) {
            wrap = new ScriptableList(scope, javaObject, staticType, map);
        } else {
            wrap = new JavaObjectWrapper(scope, javaObject, staticType, map);
        }
        return wrap;
    }

    private Map<String, Method> getExtensionFunctions(Class<?> cls) {
        Map<String, Method> map = functionMappings.get(cls);
        ExtensionEntry entry;
        if (map == null) {
            map = new HashMap<String, Method>();
            for (Iterator<ExtensionEntry> i = functionRegistry.iterator(); i.hasNext(); ) {
                entry = i.next();
                if (entry.clazz.isAssignableFrom(cls)) {
                    map.put(entry.name, entry.function);
                }
            }
        }
        return map;
    }

    /**
     *  Test driver.  Also used for generating javascript javadocs.
     */
    public static final void main(String[] args) throws Exception {
        Context cx = ContextFactory.getGlobal().enterContext();
        try {
            cx.setWrapFactory(new SugarWrapFactory());
            Scriptable scope = new ImporterTopLevel(cx);
            FileReader reader = new FileReader(args[0]);
            Object[] array = args;
            if (args.length > 0) {
                int length = args.length - 1;
                array = new Object[length];
                System.arraycopy(args, 1, array, 0, length);
            }
            Scriptable argsObj = cx.newArray(scope, array);
            scope.put("arguments", scope, argsObj);
            Object result = cx.evaluateReader(scope, reader, args[0], 1, null);
            System.err.println(Context.toString(result));
        } finally {
            Context.exit();
        }
    }

    class ExtensionEntry {

        public Class<?> clazz;

        public String name;

        public Method function;

        public ExtensionEntry(Class<?> cls, String name, Method func) {
            this.clazz = cls;
            this.name = name;
            this.function = func;
        }
    }
}
