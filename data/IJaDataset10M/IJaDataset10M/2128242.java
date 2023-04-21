package org.helianto.web.action;

import org.helianto.core.security.PublicUserDetails;
import org.springframework.webflow.core.collection.MutableAttributeMap;

/**
 * A mock object to help in flow tests.
 * 
 * @author mauriciofernandesdecastro
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class MockAction<T> extends AbstractAction<T> {

    MutableAttributeMap receivedAttributes;

    PublicUserDetails receivedUserDetails;

    T createdTarget;

    T receivedInPrepare;

    T receivedInStore;

    String targetName = "TARGET";

    /**
	 * Mock constructor.
	 */
    public MockAction() {
        super();
    }

    /**
	 * Mock target name constructor.
	 */
    public MockAction(String targetName) {
        this();
        this.targetName = targetName;
    }

    @Override
    protected T doCreate(MutableAttributeMap attributes, PublicUserDetails userDetails) {
        receivedAttributes = attributes;
        receivedUserDetails = userDetails;
        return createdTarget;
    }

    @Override
    protected T doPrepare(T target, MutableAttributeMap attributes) {
        receivedInPrepare = target;
        return target;
    }

    @Override
    protected T doStore(T target) {
        receivedInStore = target;
        return target;
    }

    @Override
    protected String getTargetName() {
        return targetName;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected SimpleModel doCreateModel(MutableAttributeMap attributes, PublicUserDetails userDetails) {
        return new SimpleModel(new Object());
    }
}
