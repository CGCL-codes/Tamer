package org.jsemantic.support;

import org.jsemantic.core.session.SemanticSession;
import org.jservicerules.support.AbstractSemanticService;

public class MockAbstractSemanticService extends AbstractSemanticService {

    public MockAbstractSemanticService() {
        super();
    }

    public MockAbstractSemanticService(String rulesFile, boolean stateful) {
        super(rulesFile, stateful);
    }

    public MockAbstractSemanticService(String rulesFile) {
        super(rulesFile);
    }

    public SemanticSession getInstance() {
        return super.getInstance();
    }
}
