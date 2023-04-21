package org.impalaframework.module.modification.graph;

public class StickyGraphModificationExtractor extends GraphModificationExtractor {

    @Override
    protected GraphAwareModificationExtractor newDelegate() {
        return new StickyGraphModificationExtractorDelegate();
    }
}
