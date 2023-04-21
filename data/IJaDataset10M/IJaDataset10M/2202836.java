package org.tolk;

import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.legacy.ClassImposteriser;

public class BaseTestCase extends MockObjectTestCase {

    {
        setImposteriser(ClassImposteriser.INSTANCE);
    }
}
