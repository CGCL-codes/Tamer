package dp.lib.dto.geda.assembler;

import org.junit.Ignore;
import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;
import dp.lib.dto.geda.annotations.DtoParent;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto
@Ignore
public class TestDto11ChildClass implements TestDto11ChildInterface {

    @DtoParent
    @DtoField(value = "parent", dtoBeanKey = "dp.lib.dto.geda.assembler.TestDto11ParentClass", entityBeanKeys = { "dp.lib.dto.geda.assembler.TestEntity11ParentClass" })
    private TestDto11ParentInterface parent;

    @DtoField
    private String name;

    /**
	 * @return parent entity.
	 */
    public TestDto11ParentInterface getParent() {
        return parent;
    }

    /**
	 * @param parent parent entity.
	 */
    public void setParent(final TestDto11ParentInterface parent) {
        this.parent = parent;
    }

    /**
	 * @return name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name name
	 */
    public void setName(final String name) {
        this.name = name;
    }
}
