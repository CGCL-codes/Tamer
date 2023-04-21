package marubinotto.piggydb.model.fragment;

import static marubinotto.piggydb.model.Assert.assertClassificationEquals;
import static marubinotto.util.CollectionUtils.set;
import static org.junit.Assert.*;
import marubinotto.piggydb.model.Fragment;
import marubinotto.piggydb.model.Tag;
import marubinotto.util.paging.Page;
import marubinotto.util.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * Get fragments order by update date desc.
 */
public class GetRecentChangesTest extends FragmentRepositoryTestBase {

    protected long fragment1Id;

    protected long fragment2Id;

    protected long fragment3Id;

    @Before
    public void given() throws Exception {
        super.given();
        DateTime.setCurrentTimeForTest(new DateTime(2008, 1, 1));
        this.fragment1Id = this.object.register(new Fragment("title1", null));
        DateTime.setCurrentTimeForTest(new DateTime(2008, 1, 2));
        this.fragment2Id = this.object.register(new Fragment("title2", null));
        DateTime.setCurrentTimeForTest(new DateTime(2008, 1, 3));
        this.fragment3Id = this.object.register(createFragmentWithOneTag("title3", "tag"));
        DateTime.setCurrentTimeForTest(null);
        this.object.createRelation(this.fragment1Id, this.fragment3Id);
        this.object.createRelation(this.fragment3Id, this.fragment2Id);
    }

    @Test
    public void fetching() throws Exception {
        Fragment fragment = this.object.getRecentChanges(3, 0).get(0);
        assertEquals(this.fragment3Id, fragment.id.longValue());
        assertEquals("title3", fragment.title);
        assertEquals(new DateTime(2008, 1, 3), fragment.creationDatetime);
        assertEquals(new DateTime(2008, 1, 3), fragment.updateDatetime);
        assertClassificationEquals(set("tag"), fragment.getClassification());
        assertEquals(1, fragment.getParents().size());
        Fragment parent = fragment.getParents().get(0).from;
        assertEquals(this.fragment1Id, parent.id.longValue());
        assertEquals("title1", parent.title);
        assertEquals(1, fragment.getChildren().size());
        Fragment child = fragment.getChildren().get(0).to;
        assertEquals(this.fragment2Id, child.id.longValue());
        assertEquals("title2", child.title);
    }

    @Test
    public void orderByUpdateDateDesc() throws Exception {
        Page<Fragment> page = this.object.getRecentChanges(3, 0);
        assertEquals(3, page.size());
        assertEquals(this.fragment3Id, page.get(0).id.longValue());
        assertEquals(this.fragment2Id, page.get(1).id.longValue());
        assertEquals(this.fragment1Id, page.get(2).id.longValue());
    }

    @Test
    public void shouldChangeOrderWhenUpdated() throws Exception {
        DateTime.setCurrentTimeForTest(new DateTime(2008, 1, 4));
        Fragment fragment = this.object.get(this.fragment1Id);
        fragment.title = "should be the latest";
        this.object.update(fragment);
        Page<Fragment> page = this.object.getRecentChanges(3, 0);
        assertEquals(3, page.size());
        assertEquals(this.fragment1Id, page.get(0).id.longValue());
        assertEquals(this.fragment3Id, page.get(1).id.longValue());
        assertEquals(this.fragment2Id, page.get(2).id.longValue());
    }

    @Test
    public void whenTagNameHasBeenChanged() throws Exception {
        Tag tag = this.tagRepository.getByName("tag");
        tag.name = "renamed-tag";
        this.tagRepository.update(tag);
        Page<Fragment> page = this.object.getRecentChanges(3, 0);
        Fragment fragment = page.get(0);
        assertEquals(1, fragment.getClassification().size());
        assertTrue(fragment.getClassification().containsTagName("renamed-tag"));
    }
}
