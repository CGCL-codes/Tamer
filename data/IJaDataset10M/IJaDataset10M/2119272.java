package org.pubcurator.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author Kai Schlamp (schlamp@gmx.de)
 *
 */
public class TagsUtilTest {

    /**
	 * Test method for {@link org.pubcurator.core.utils.TagsUtil#convert(java.lang.String)}.
	 */
    @Test
    public void testExtractTags() {
        String[] tags = TagsUtil.convert("Tag1 Tag2 Tag3", false);
        assertEquals("Tag1", tags[0]);
        assertEquals("Tag2", tags[1]);
        assertEquals("Tag3", tags[2]);
        boolean invalidTags = false;
        try {
            TagsUtil.convert("Tag1, Tag2", true);
        } catch (IllegalArgumentException e) {
            invalidTags = true;
        }
        assertTrue(invalidTags);
        tags = TagsUtil.convert("", true);
        assertTrue(tags.length == 0);
    }
}
