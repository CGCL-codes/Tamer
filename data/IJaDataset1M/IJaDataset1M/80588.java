package au.org.tpac.portal.manager;

import java.util.LinkedList;
import java.util.List;
import au.org.tpac.portal.domain.Tag;
import au.org.tpac.portal.repository.TagDao;

/**
 * The Class TagManagerImpl.
 */
public class TagManagerImpl implements TagManager {

    /** The tag dao. */
    private TagDao tagDao;

    /**
     * Sets the tag dao.
     * 
     * @param tDao the new tag dao
     */
    public final void setTagDao(TagDao tDao) {
        this.tagDao = tDao;
    }

    public List<String> findNames() {
        List<String> result = new LinkedList<String>();
        List<Tag> tags = tagDao.findTags();
        for (Tag tag : tags) {
            result.add(tag.getName());
        }
        return result;
    }

    @Override
    public final int findTagId(final String tagName) {
        return tagDao.findTagId(tagName);
    }

    @Override
    public final void insertTag(final Tag tag) {
        tagDao.insertTag(tag);
    }

    @Override
    public final List<Tag> findTags(final String classification) {
        return tagDao.findTags(classification);
    }

    @Override
    public final void deleteTag(final int id) {
        tagDao.deleteTag(id);
    }

    @Override
    public void update(Tag tag) {
        tagDao.updateTag(tag);
    }
}
