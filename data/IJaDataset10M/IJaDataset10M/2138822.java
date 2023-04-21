package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public interface TestEntity11ParentInterface {

    /**
	 * @return PK of this entity.
	 */
    long getEntityId();

    /**
	 * @param entityId PK of this entity.
	 */
    void setEntityId(final long entityId);

    /**
	 * @return name
	 */
    String getName();

    /**
	 * @param name name
	 */
    void setName(final String name);
}
