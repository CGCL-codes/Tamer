package com.notuvy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple utility that adds convenience to lookups beyond what Map defines.
 * There are two problems with Map get() that this utility solves.
 *
 * First, with Map get() is that you need to check if it returned null,
 * whereas this will return a defined fallback value .
 *
 * Second, with Map get(), if you want different forms of the same key
 * to represent the same value (e.g. case-insensitive, whitespace-insensitive),
 * you need to explicitly enumerate ALL possible key values.  For trimmed
 * strings, this is not possible because the possibilities are infinite.
 * Instead, this class allows the definition of (possibly multiple)
 * <i>transformations</i> that are performed on the key before matching.
 *
 * It also defines add() as a cascading method, so a fully defined translation
 * table can be created in a single java statement.
 *
 * User: murali
 * Date: Nov 27, 2007
 * Time: 1:00:04 PM
 */
public class Translation<T> {

    public static final KeyTranformation CASE_INSENSITIVE = new KeyTranformation() {

        public String process(String pOriginal) {
            return pOriginal.toLowerCase();
        }
    };

    public static final KeyTranformation TRIMMED = new KeyTranformation() {

        public String process(String pOriginal) {
            return pOriginal.trim();
        }
    };

    public static final KeyTranformation ALPHANUMERIC = new KeyTranformation() {

        public String process(String pOriginal) {
            return pOriginal.replaceAll("[^\\p{Alnum}]+", "");
        }
    };

    private final HashMap<String, T> fUnderlying = new HashMap<String, T>();

    private T fFallback;

    private final List<KeyTranformation> transformations = new ArrayList<KeyTranformation>();

    public Map<String, T> getUnderlying() {
        return fUnderlying;
    }

    public T getFallback() {
        return fFallback;
    }

    public Translation<T> setFallback(T pFallback) {
        fFallback = pFallback;
        return this;
    }

    private String transform(String pOriginal) {
        String result = (pOriginal == null) ? "" : pOriginal;
        for (KeyTranformation tranformation : transformations) {
            result = tranformation.process(result);
        }
        return result;
    }

    /**
     * All transformations should be added before any definitions are added.
     * When this method is called, it is NOT retroactively applied to any existing definitions.
     *
     * @param pTransform  A new key transform to add.
     * @return  this instance.
     */
    public Translation<T> addTransform(KeyTranformation pTransform) {
        transformations.add(pTransform);
        return this;
    }

    public Translation<T> define(String pKey, T pVal) {
        getUnderlying().put(transform(pKey), pVal);
        return this;
    }

    public T lookup(String pKey) {
        T candidate = getUnderlying().get(transform(pKey));
        return (candidate == null) ? getFallback() : candidate;
    }

    public boolean defines(String pKey) {
        return getUnderlying().containsKey(transform(pKey));
    }

    public boolean isEmpty() {
        return getUnderlying().isEmpty();
    }

    public void clear() {
        getUnderlying().clear();
    }

    public interface KeyTranformation {

        String process(String pOriginal);
    }

    /**
     * This customizes the lookup behavior.  If no match is found and no fallback is defined, then
     * the lookup key is returned.  It requires that the value type is also String.
     */
    public static class Rewrite extends Translation<String> {

        public String lookup(String pKey) {
            String intermediate = super.lookup(pKey);
            return (intermediate == null) ? pKey : intermediate;
        }
    }
}
