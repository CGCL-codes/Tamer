package es.eucm.eadventure.common.data.adventure;

import java.util.ArrayList;
import java.util.List;
import es.eucm.eadventure.common.data.chapter.Chapter;

/**
 * Represents the whole data of an adventure game. This includes: Descriptor
 * Information (adventure title, description, path of each chapter, adaptation &
 * assessment configuration, gui configuration) List of Chapters List of
 * AssessmentProfiles List of AdaptationProfiles
 * 
 * @author Javier
 * 
 */
public class AdventureData extends DescriptorData {

    /**
     * List of chapters. Contains the main data of the adventures.
     */
    protected List<Chapter> chapters;

    /**
     * Default constructor
     */
    public AdventureData() {
        super();
        this.chapters = new ArrayList<Chapter>();
        contents = null;
    }

    /**
     * Adds a new chapter.
     * 
     * @param chapter
     */
    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
    }

    /**
     * @return the chapters
     */
    public List<Chapter> getChapters() {
        return chapters;
    }

    /**
     * @param chapters
     *            the chapters to set
     */
    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    @Override
    public void addChapterSummary(ChapterSummary chapter) {
        if (chapter instanceof Chapter) {
            chapters.add((Chapter) chapter);
        }
    }

    /**
     * Returns if the chapter.xml has adaptation and/or assessment data
     * 
     * @return
     */
    public boolean hasAdapOrAssesData() {
        for (int i = 0; i < chapters.size(); i++) {
            String[] apn = chapters.get(i).getAdaptationProfilesNames();
            if (apn.length > 0) return true;
            String[] aspn = chapters.get(i).getAssessmentProfilesNames();
            if (aspn.length > 0) return true;
        }
        return false;
    }

    /**
     * Returns the list of chapters of the game
     * 
     * @return List of chapters of the game
     */
    @Override
    public List<ChapterSummary> getChapterSummaries() {
        List<ChapterSummary> summary = new ArrayList<ChapterSummary>();
        for (Chapter chapter : chapters) {
            summary.add(chapter);
        }
        return summary;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AdventureData ad = (AdventureData) super.clone();
        ad.buttons = new ArrayList<CustomButton>();
        for (CustomButton cb : buttons) ad.buttons.add((CustomButton) cb.clone());
        for (CustomArrow ca : arrows) ad.arrows.add((CustomArrow) ca.clone());
        ad.commentaries = commentaries;
        ad.contents = new ArrayList<ChapterSummary>();
        for (ChapterSummary cs : contents) ad.contents.add((ChapterSummary) cs.clone());
        ad.cursors = new ArrayList<CustomCursor>();
        for (CustomCursor cc : cursors) ad.cursors.add((CustomCursor) cc.clone());
        ad.description = new String(description);
        ad.guiCustomized = guiCustomized;
        ad.guiType = guiType;
        ad.playerMode = playerMode;
        ad.playerName = (playerName != null ? new String(playerName) : null);
        ad.title = (title != null ? new String(title) : null);
        return ad;
    }
}
