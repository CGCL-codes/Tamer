package org.kumenya.openapi.util;

import java.util.Map;

/**
 * Provides type-safe access to data.
 *
 * @author max
 * @author Konstantin Bulenkov
 */
@SuppressWarnings({ "AssignmentToStaticFieldFromInstanceMethod", "UnusedDeclaration", "EqualsWhichDoesntCheckParameterClass" })
public class Key<T> {

    private static int ourKeysCounter = 0;

    private final int myIndex = ourKeysCounter++;

    private final String myName;

    public Key(String name) {
        myName = name;
    }

    public int hashCode() {
        return myIndex;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    public String toString() {
        return myName;
    }

    public static <T> Key<T> create(String name) {
        return new Key<T>(name);
    }

    public T get(UserDataHolder holder) {
        return holder == null ? null : holder.getUserData(this);
    }

    public T get(Map<Key, Object> holder) {
        return holder == null ? null : (T) holder.get(this);
    }

    public T get(UserDataHolder holder, T defaultValue) {
        final T t = get(holder);
        return t == null ? defaultValue : t;
    }

    /**
   * Returns <code>true</code> if and only if the <code>holder</code> has
   * not null value by the key.
   *
   * @param holder user data holder object
   * @return <code>true</code> if holder.getUserData(this) != null
   * <code>false</code> otherwise.
   */
    public boolean isIn(UserDataHolder holder) {
        return get(holder) != null;
    }

    public void set(UserDataHolder holder, T value) {
        if (holder != null) {
            holder.putUserData(this, value);
        }
    }

    public void set(Map<Key, Object> holder, T value) {
        if (holder != null) {
            holder.put(this, value);
        }
    }
}
