package org.identifylife.character.store.repository.media.impl;

import org.identifylife.character.store.model.media.MediaItem;
import org.identifylife.character.store.repository.impl.BaseRepositoryImpl;
import org.identifylife.character.store.repository.media.MediaItemRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dbarnier
 *
 */
@Repository("mediaItemRepository")
public class MediaItemRepositoryImpl extends BaseRepositoryImpl<MediaItem> implements MediaItemRepository {
}
