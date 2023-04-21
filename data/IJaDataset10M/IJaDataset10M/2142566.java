package qurtext.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Section {

    @SuppressWarnings("unused")
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Integer sectionNo = 0;

    @Persistent
    private Chapter chapter;

    @Persistent
    private Integer chapterNo = 0;

    @Persistent
    private Integer startVerse = 0;

    @Persistent
    private Integer endVerse = 0;

    @Persistent
    private String summary;

    @Persistent(mappedBy = "section")
    @Order(extensions = @Extension(vendorName = "datanucleus", key = "list-ordering", value = "verseNo asc"))
    private List<Verse> verses;

    public Section() {
    }

    public Section(int sectionNo, int chapterNo, int startVerse, int endVerse) {
        this.setSectionNo(sectionNo);
        this.chapterNo = chapterNo;
        this.startVerse = startVerse;
        this.endVerse = endVerse;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setStartVerse(int startVerse) {
        this.startVerse = startVerse;
    }

    public int getStartVerse() {
        return startVerse;
    }

    public void setEndVerse(int endVerse) {
        this.endVerse = endVerse;
    }

    public int getEndVerse() {
        return endVerse;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    private List<Verse> getModifiableVerses() {
        if (null == verses) verses = new ArrayList<Verse>();
        return verses;
    }

    public List<Verse> getVerses() {
        return Collections.unmodifiableList(getModifiableVerses());
    }

    public void addVerse(Verse verse) {
        verse.setSection(this);
        getModifiableVerses().add(verse);
    }

    public void removeVerse(Verse verse) {
        verse.setSection(null);
        getModifiableVerses().remove(verse);
    }

    public void setChapterNo(int chapterNo) {
        this.chapterNo = chapterNo;
    }

    public int getChapterNo() {
        return chapterNo;
    }

    public void setSectionNo(int sectionNo) {
        this.sectionNo = sectionNo;
    }

    public int getSectionNo() {
        return sectionNo;
    }

    public Verse getVerse(int chapterNo, int verseNo) {
        for (Verse verse : verses) {
            if (verse.getChapterNo() == chapterNo && verse.getVerseNo() == verseNo) return verse;
        }
        return null;
    }
}
