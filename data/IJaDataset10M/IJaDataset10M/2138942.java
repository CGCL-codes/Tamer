package net.sourceforge.taggerplugin.model;

import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TagSetTest {

    private TagTestFixture tagFixture;

    private TagAssociationTestFixture assocFixture;

    private TagSetTestFixture setFixture;

    public TagSetTest() {
        tagFixture = new TagTestFixture();
        assocFixture = new TagAssociationTestFixture();
        setFixture = new TagSetTestFixture();
    }

    @Before
    public void init() {
        tagFixture.init();
        assocFixture.init(tagFixture);
        setFixture.init(tagFixture, assocFixture);
    }

    @Test
    public void properties() {
        Assert.assertEquals(TagSetTestFixture.TAGSETA_ID, setFixture.tagSetA.getId());
    }

    @Test
    public void equals() {
        Assert.assertSame(setFixture.tagSetA, setFixture.tagSetA);
        Assert.assertEquals(setFixture.tagSetA, setFixture.tagSetA);
        Assert.assertFalse(setFixture.tagSetA.equals(setFixture.tagSetB));
    }

    @Test
    public void collections() {
        Set<TagSet> set = new HashSet<TagSet>();
        set.add(setFixture.tagSetA);
        set.add(setFixture.tagSetA);
        set.add(setFixture.tagSetB);
        set.add(setFixture.tagSetB);
        Assert.assertEquals(2, set.size());
        Assert.assertTrue(set.contains(setFixture.tagSetA));
        Assert.assertTrue(set.contains(setFixture.tagSetB));
    }

    @After
    public void destroy() {
        tagFixture.destroy();
        assocFixture.destroy();
        setFixture.destroy();
    }
}
