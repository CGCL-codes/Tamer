package org.archive.crawler.scope;

import org.archive.crawler.framework.CrawlerProcessorTestBase;

/**
 * Unit test for {@link ClassicScope}.
 *
 * @author pjack
 */
public class ClassicScopeTest extends CrawlerProcessorTestBase {

    @Override
    protected Class getModuleClass() {
        return ClassicScope.class;
    }

    @Override
    protected Object makeModule() {
        return new ClassicScope();
    }
}
