package net.sf.joafip.java.util;

import java.util.Map;
import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.TestException;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@DoNotTransform
public class TestPLinkedHashMapListener extends AbstractTestMapListener {

    public TestPLinkedHashMapListener() throws TestException {
        super();
    }

    public TestPLinkedHashMapListener(final String name) throws TestException {
        super(name);
    }

    @Override
    protected Map<String, String> getMap() {
        return new PLinkedHashMap<String, String>();
    }
}
