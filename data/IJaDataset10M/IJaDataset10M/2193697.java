package dp.lib.dto.geda.assembler.examples.collections;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;
import org.junit.Ignore;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 11:39:45 AM
 */
@Dto
@Ignore
public class TestDto7iCollectionSubClass implements TestDto7CollectionSubInterface {

    @DtoField(value = "name")
    private String name;

    /** {@inheritDoc} */
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    public void setName(final String name) {
        this.name = name;
    }
}
