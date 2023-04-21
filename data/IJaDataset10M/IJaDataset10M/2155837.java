package dp.lib.dto.geda.assembler;

import java.util.Collection;
import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public interface TestDto12CollectionIterface {

    /**
	 * @return items of collection
	 */
    Collection<TestDto12CollectionItemIterface> getItems();

    /**
	 * @param items items of collection
	 */
    void setItems(Collection<TestDto12CollectionItemIterface> items);
}
