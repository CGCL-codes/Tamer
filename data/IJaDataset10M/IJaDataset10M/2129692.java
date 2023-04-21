package net.derquinse.common.meta;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import net.derquinse.common.meta.MetaFlag;
import org.testng.annotations.Test;
import com.google.common.base.Predicates;

/**
 * Tests for MetaFlag
 * @author Andres Rodriguez
 */
public class MetaFlagTest {

    /**
	 * Sample property.
	 */
    @Test
    public void name() {
        final TestObject t = new TestObject(true);
        final TestObject f = new TestObject(false);
        assertTrue(t.isSample());
        assertTrue(TestObject.SAMPLE.apply(t));
        assertFalse(f.isSample());
        assertFalse(TestObject.SAMPLE.apply(f));
        assertTrue(TestObject.SAMPLE.or(Predicates.alwaysFalse()).apply(t));
        assertFalse(TestObject.SAMPLE.and(Predicates.alwaysFalse()).apply(t));
    }

    private static final class TestObject {

        /** Descriptor for sample flag. */
        public static final MetaFlag<TestObject> SAMPLE = new MetaFlag<TestObject>("sample") {

            public boolean apply(TestObject input) {
                return input.isSample();
            }
        };

        /** Sample property. */
        private final boolean sample;

        public TestObject(boolean sample) {
            this.sample = sample;
        }

        public boolean isSample() {
            return sample;
        }

        @Override
        public String toString() {
            return Metas.toStringHelper(this).add(SAMPLE).toString();
        }
    }
}
