package com.javabi.sizeof;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import com.javabi.sizeof.definition.BooleanArrayDefinition;
import com.javabi.sizeof.definition.ByteArrayDefinition;
import com.javabi.sizeof.definition.CharArrayDefinition;
import com.javabi.sizeof.definition.DoubleArrayDefinition;
import com.javabi.sizeof.definition.FloatArrayDefinition;
import com.javabi.sizeof.definition.HashMapDefinition;
import com.javabi.sizeof.definition.IgnoreDefinition;
import com.javabi.sizeof.definition.IntArrayDefinition;
import com.javabi.sizeof.definition.LinkedHashMapDefinition;
import com.javabi.sizeof.definition.LinkedListDefinition;
import com.javabi.sizeof.definition.LongArrayDefinition;
import com.javabi.sizeof.definition.ObjectArrayDefinition;
import com.javabi.sizeof.definition.ObjectDefinition;
import com.javabi.sizeof.definition.ShortArrayDefinition;

/**
 * A Class Definition Map.
 */
public class ClassDefinitionMap {

    /**
     * The class to definition map.
     */
    private final Map<Class<?>, ClassDefinition<?>> classToDefinition = new IdentityHashMap<Class<?>, ClassDefinition<?>>();

    /**
     * The ignore field set.
     */
    private final Set<Field> ignoreFieldSet = new HashSet<Field>();

    /**
     * Creates a new class cache.
     *
     * @throws ClassDefinitionInitialisationException
     *
     */
    public ClassDefinitionMap() {
        try {
            defineRequiredTypes();
            defineOptionalTypes();
        } catch (Exception e) {
            throw new ClassDefinitionInitialisationException(e);
        }
    }

    /**
     * Define the required base types necessary for any map.
     */
    protected void defineRequiredTypes() {
        defineType(Object[].class, new ObjectArrayDefinition());
        defineType(boolean[].class, new BooleanArrayDefinition());
        defineType(char[].class, new CharArrayDefinition());
        defineType(byte[].class, new ByteArrayDefinition());
        defineType(short[].class, new ShortArrayDefinition());
        defineType(int[].class, new IntArrayDefinition());
        defineType(long[].class, new LongArrayDefinition());
        defineType(float[].class, new FloatArrayDefinition());
        defineType(double[].class, new DoubleArrayDefinition());
        ignoreType(ClassLoader.class);
        ignoreType(Class.class);
    }

    /**
     * Define optional types.
     */
    protected void defineOptionalTypes() throws Exception {
        defineType(LinkedList.class, new LinkedListDefinition());
        defineType(HashMap.class, new HashMapDefinition());
        defineType(LinkedHashMap.class, new LinkedHashMapDefinition());
    }

    /**
     * Set the definition for the given class.
     *
     * @param <T>        the type.
     * @param clazz      the class.
     * @param definition the definition.
     */
    public final <T> void defineType(Class<T> clazz, ClassDefinition<T> definition) {
        if (clazz == null || definition == null) {
            throw new NullPointerException();
        }
        classToDefinition.put(clazz, definition);
    }

    /**
     * Ignore the given type (any instance will return zero).
     *
     * @param <T>  the type.
     * @param type the type.
     */
    public final <T> void ignoreType(Class<T> type) {
        defineType(type, new IgnoreDefinition<T>());
    }

    /**
     * Ignore the given field.
     *
     * @param field the field.
     */
    public final void ignoreField(Field field) {
        if (field == null) {
            throw new NullPointerException();
        }
        ignoreFieldSet.add(field);
    }

    /**
     * Returns true if the given field should be ignored.
     *
     * @param field the field.
     * @return true if the given field should be ignored.
     */
    public final boolean shouldIgnoreField(Field field) {
        return ignoreFieldSet.contains(field);
    }

    /**
     * Ignore the named field in the given class.
     *
     * @param type      the class.
     * @param fieldName the field name.
     */
    public final void ignoreField(Class<?> type, String fieldName) throws NoSuchFieldException {
        Field field = type.getDeclaredField(fieldName);
        ignoreField(field);
    }

    /**
     * Ignore the named field in the given class.
     *
     * @param className the class name.
     * @param fieldName the field name.
     */
    public final void ignoreField(String className, String fieldName) throws ClassNotFoundException, NoSuchFieldException {
        Class<?> clazz = Class.forName(className);
        ignoreField(clazz, fieldName);
    }

    /**
     * Returns the definition for the given class.
     *
     * @param <T>   the type.
     * @param clazz the class.
     * @return the definition.
     */
    public final <T> ClassDefinition<T> get(Class<T> clazz) {
        ClassDefinition<T> definition = (ClassDefinition<T>) classToDefinition.get(clazz);
        if (definition == null) {
            definition = newDefinition(clazz);
            classToDefinition.put(clazz, definition);
        }
        return definition;
    }

    /**
     * Returns true if this contains a definition for the given class.
     *
     * @param clazz the class.
     * @return true if a definition exists.
     */
    public final boolean contains(Class<?> clazz) {
        return classToDefinition.containsKey(clazz);
    }

    /**
     * Returns a new definition for the given class.
     *
     * @param <T>   the type.
     * @param clazz the class.
     * @return the definition.
     */
    protected <T> ClassDefinition<T> newDefinition(Class<T> clazz) {
        ClassDefinition<T> definition;
        if (Object[].class.isAssignableFrom(clazz)) {
            definition = (ClassDefinition<T>) new ObjectArrayDefinition();
        } else {
            definition = new ObjectDefinition<T>(clazz, this);
        }
        return definition;
    }
}
