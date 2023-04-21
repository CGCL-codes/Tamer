package marubinotto.piggydb.model.tag;

import static org.junit.Assert.assertEquals;
import marubinotto.piggydb.model.Tag;
import org.junit.Test;

public class MiscTest extends TagRepositoryTestBase {

    @Test
    public void shouldNotBeAffectedByModifyingOriginalObject() throws Exception {
        Tag originalTag = new Tag("name");
        long tagId = this.object.register(originalTag);
        originalTag.name = "modified";
        assertEquals("name", this.object.get(tagId).name);
    }
}
