package com.google.web.bindery.requestfactory.shared;

import com.google.gwt.user.client.rpc.UnicodeEscapingTest;

/**
 * Provides access to the static test methods in {@link UnicodeEscapingTes}.
 */
@Service(UnicodeEscapingTest.class)
public interface UnicodeTestRequest extends RequestContext {

    Request<String> getStringContainingCharacterRange(int start, int end);

    Request<Void> verifyStringContainingCharacterRange(int start, int end, String str);
}
