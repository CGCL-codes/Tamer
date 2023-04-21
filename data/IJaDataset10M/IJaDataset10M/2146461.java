package org.scribble.conversation.comparator;

import org.scribble.extensions.RegistryFactory;
import org.scribble.model.*;
import org.scribble.conversation.model.*;
import org.scribble.model.admin.DefaultModelListener;
import org.scribble.comparator.*;
import junit.framework.TestCase;

public class ParallelComparatorRuleTest extends TestCase {

    private static final String TEST_NOTATION = "test";

    public void testCompareShallow() {
        ParallelComparatorRule rule = new ParallelComparatorRule();
        RegistryFactory.setRegistry(null);
        DefaultModelListener l = new DefaultModelListener();
        java.util.List<ComparatorRule> rules = new java.util.Vector<ComparatorRule>();
        DefaultComparatorContext context = new DefaultComparatorContext(new ModelReference(TEST_NOTATION), new ModelReference(TEST_NOTATION), rules);
        Parallel main = new Parallel();
        Parallel reference = new Parallel();
        boolean result = rule.compare(context, main, reference, l, false);
        if (l.getErrors().size() > 0) {
            fail("No errors expected");
        }
        if (result == false) {
            fail("Comparator should NOT have failed");
        }
    }
}
