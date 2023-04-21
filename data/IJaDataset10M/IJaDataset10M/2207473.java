package org.translationcomponent.api.impl.test.multithreaded.request;

import org.translationcomponent.api.impl.test.RequestResponse;
import org.translationcomponent.api.impl.test.multithreaded.MultiThreaded;

public interface TestCallableFactory {

    public TestCallable getInstance(final RequestResponse reqresp, final MultiThreaded test);
}
