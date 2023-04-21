package dp.lib.dto.geda.assembler;

import org.junit.Ignore;
import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto
@Ignore
public class TestDto11ParentClass implements TestDto11ParentInterface {

    @DtoField
    private long entityId;

    @DtoField
    private String name;

    /**
	 * @return PK of this entity.
	 */
    public long getEntityId() {
        return entityId;
    }

    /**
	 * @param entityId PK of this entity.
	 */
    public void setEntityId(final long entityId) {
        this.entityId = entityId;
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
