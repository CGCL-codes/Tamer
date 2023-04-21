package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

/**
 * Test Entity for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity15Class {

    private final String name;

    private final String desc;

    /**
	 * @param name name
	 * @param desc desc
	 */
    public TestEntity15Class(final String name, final String desc) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return description
     */
    public String getDesc() {
        return desc;
    }
}
