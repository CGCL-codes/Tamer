package net.sf.joafip.java.util;

/**
 * to check {@link PHashSet} implementation
 * 
 * @author luc peuvrier
 * 
 */
public class TestPHashSet extends AbstractSetTest {

    @Override
    protected void setUp() throws Exception {
        set = new PHashSet<String>();
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
