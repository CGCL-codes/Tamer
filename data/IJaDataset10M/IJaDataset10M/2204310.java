package dp.lib.dto.geda.assembler.examples.collections;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public interface TestEntity12WrapCollectionInterface {

    /**
	 * @return wrapper for collection
	 */
    TestEntity12CollectionInterface getCollectionWrapper();

    /**
	 * @param collectionWrapper wrapper for collection
	 */
    void setCollectionWrapper(TestEntity12CollectionInterface collectionWrapper);
}
