package org.translationcomponent.service.document.xml.attributes;

import java.util.concurrent.atomic.AtomicLong;
import javax.xml.stream.events.StartElement;
import org.translationcomponent.api.TranslationRequest;
import org.translationcomponent.service.document.xml.StaxParserConfiguration;

public class MockAttributeModifier extends AttributeModifierAbstract {

    private AtomicLong hits = new AtomicLong();

    public long getHits() {
        return hits.get();
    }

    public void init(StaxParserConfiguration config) {
    }

    public StartElement service(StartElement event, TranslationRequest pageRequest, String encoding, boolean translate) {
        hits.incrementAndGet();
        return event;
    }
}
