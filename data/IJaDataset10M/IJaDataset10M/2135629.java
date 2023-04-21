package net.martinimix.beans.bind;

import junit.framework.TestCase;
import com.bluemartini.dna.DNAList;

public class AttributeAwareBeanDataBinderTest extends TestCase {

    private DNAList source;

    private BeanBinder binder;

    public void setUp() {
        binder = new SimpleBeanBinder(new AttributeAwareFieldPropertyMappingHandler());
        source = new DNAList();
        source.setLong("ATR_a_long_number", 100L);
    }

    public void testSimpleBind() {
        SampleBean sampleBean;
        assertTrue(binder.supports(SampleBean.class));
        sampleBean = (SampleBean) binder.bind(source, new SampleBean());
        assertEquals(new Long(100L), sampleBean.getLongNumber());
    }

    public void testSimpleBindAgain() {
        SampleBean sampleBean;
        assertTrue(binder.supports(SampleBean.class));
        sampleBean = (SampleBean) binder.bind(source, new SampleBean());
        assertEquals(new Long(100L), sampleBean.getLongNumber());
    }
}
