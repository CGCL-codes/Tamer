package org.eclipse.tptp.models.web.common.test.data.rep;

import org.eclipse.hyades.models.common.facades.behavioral.ITestSuite;
import org.eclipse.tptp.models.web.common.test.data.rep.impl.AssertionRepImpl;
import org.eclipse.tptp.models.web.common.test.data.rep.impl.FilledParameterRepImpl;
import org.eclipse.tptp.models.web.common.test.data.rep.impl.ValueSelectorRepImpl;
import org.w3c.dom.Document;

public class RepositoryFactoryImpl implements IRepositoryFactory {

    private IAssertionRep assertionRep = null;

    private IValueSelectorRep selectorRep = null;

    private IFilledParameterRep paramRep = null;

    private Document doc;

    private ITestSuite suite;

    public synchronized IAssertionRep createAssertionRep(ITestSuite suite) {
        if (assertionRep != null) {
            return assertionRep;
        }
        assertionRep = new AssertionRepImpl();
        assertionRep.setInput(suite);
        return assertionRep;
    }

    public synchronized IValueSelectorRep createValueSelectorRep(ITestSuite suite) {
        if (selectorRep != null) {
            return selectorRep;
        }
        selectorRep = new ValueSelectorRepImpl();
        selectorRep.setInput(suite);
        return selectorRep;
    }

    public synchronized IFilledParameterRep createFilledParameterRep(ITestSuite suite) {
        if (paramRep != null) {
            return paramRep;
        }
        paramRep = new FilledParameterRepImpl();
        paramRep.setInput(suite);
        return paramRep;
    }
}
