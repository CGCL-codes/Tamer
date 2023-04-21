package org.hibernate.validator.engine;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.HashSet;

/**
 * Exposes Hibernate Validator Implementation Classes so they can be serialized.
 * <p>
 * Create a dummy method like the following in your RemoteService
 *
 * <pre>
 * org.hibernate.validator.engine.ValidationSupport dummy();
 * </pre>
 *
 * The following classes are included.
 * <ul>
 * <li>{@link ConstraintViolationImpl}</li>
 * <li>{@link PathImpl}</li>
 * <li>{@link HashSet}</li>
 * </ul>
 *
 */
public class ValidationSupport implements IsSerializable {

    @SuppressWarnings("unused")
    private ConstraintViolationImpl<?> constraintViolationImpl;

    @SuppressWarnings("unused")
    private PathImpl pathIpml;

    @SuppressWarnings("unused")
    private HashSet<?> hashSet;
}
