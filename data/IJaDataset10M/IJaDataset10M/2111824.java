package org.gtugs.repository;

import org.gtugs.domain.Chapter;
import java.util.List;
import java.util.Set;

/**
 * @author jasonacooper@google.com (Jason Cooper)
 */
public interface ChapterDao {

    public Integer getChapterCount();

    public Chapter getChapter(Long id);

    public List<Chapter> getChapters();

    public List<Chapter> getActiveChapters();

    public List<Chapter> getChapters(Set<Long> ids);

    public List<Chapter> getChaptersOrderedByCountry();

    public void storeChapter(Chapter c);
}
