package com.google.guiceberry.controllable;

import com.google.inject.Key;
import com.google.inject.Provider;

/**
 * <p>Intercepts object provision.
 *
 * @author Jesse Wilson
 * @author Jerome Mourits
 */
interface ProvisionInterceptor {

    <T> T intercept(Key<T> key, Provider<? extends T> delegate);
}
