package dp.lib.dto.geda.assembler.examples.nested;

import org.junit.Ignore;

/**
 * Test entity for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity4Class {

    private TestEntity4SubClass wrapper;

    /**
	 * @return property wrapper.
	 */
    public TestEntity4SubClass getWrapper() {
        return wrapper;
    }

    /**
	 * @param wrapper property wrapper.
	 */
    public void setWrapper(final TestEntity4SubClass wrapper) {
        this.wrapper = wrapper;
    }
}
