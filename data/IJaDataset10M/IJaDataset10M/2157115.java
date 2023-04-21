package net.sourceforge.piqle.util;

import com.google.inject.internal.Function;

public interface InvertibleFunction<F, T> extends Function<F, T> {

    public F inverse(T t);
}
