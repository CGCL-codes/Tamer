package com.inspiresoftware.lib.dto.geda.assembler.examples.parent;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public interface TestEntity11ChildInterface {

    /**
	 * @return parent entity.
	 */
    TestEntity11ParentInterface getParent();

    /**
	 * @param parent parent entity.
	 */
    void setParent(final TestEntity11ParentInterface parent);

    /**
	 * @return name
	 */
    String getName();

    /**
	 * @param name name
	 */
    void setName(final String name);
}
