package com.google.inject.spi;

import com.google.inject.Binding;

/**
 * A binding to a constant.
 *
 * <p>Example: {@code bindConstant().annotatedWith(PoolSize.class).to(5);}
 *
 * @author crazybob@google.com (Bob Lee)
 */
public interface ConstantBinding<T> extends Binding<T> {

    /**
   * Gets the constant value associated with this binding.
   */
    T getValue();
}
