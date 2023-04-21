package org.artags.server.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 *
 * @author Pierre Levy
 */
public class TagDAO extends GenericDAO<Tag> {

    @Override
    public void remove(long id) {
        Tag tag = findById(id);
        TagImageDAO daoImage = new TagImageDAO();
        daoImage.remove(tag.getKeyImage().getId());
        TagThumbnailDAO daoThumbnail = new TagThumbnailDAO();
        daoThumbnail.remove(tag.getKeyThumbnail().getId());
        super.remove(id);
    }
}
