package org.translationcomponent.service.document.xml.exception;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.translationcomponent.api.TranslationRequest;
import org.translationcomponent.service.document.xml.eventmodifier.EventModifier;
import org.translationcomponent.service.document.xml.exception.ExceptionRulesScanner;
import org.translationcomponent.service.document.xml.exception.chunks.SingleUntranslatedChunk;
import org.translationcomponent.service.document.xml.exception.chunks.TranslateChunks;

public class MockExceptionRulesScanner implements ExceptionRulesScanner {

    private AtomicLong hits = new AtomicLong();

    public TranslateChunks service(TranslationRequest requestContext, String mainText, final List<EventModifier> modifiers) {
        hits.incrementAndGet();
        return new SingleUntranslatedChunk(mainText);
    }

    public void setConfig(Object config) {
    }

    public long getHits() {
        return hits.get();
    }
}
