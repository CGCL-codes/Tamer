package org.translationcomponent.api.impl.translator.cache.resource;

import org.translationcomponent.api.TranslationRequest;

public interface ResourceChecker {

    ResourceProperties getResourceProperties(TranslationRequest request);

    String getRealPath(TranslationRequest request);

    boolean isEnabled();
}
