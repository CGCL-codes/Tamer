package ru.bluesteel.j2me.lj;

import java.util.Calendar;
import java.util.Vector;
import java.util.Enumeration;
import ru.bluesteel.utils.StringTokenizer;

/**
 * User: Administrator
 * Date: 15.08.2004
 * Time: 15:59:36
 */
public class LJPost {

    private String body;

    private String title;

    private Calendar calendar = Calendar.getInstance();

    private String currentMood;

    private String currentMusic;

    private String security;

    private String pictureKeyword;

    private String journal;

    private Vector tags;

    private int allowMask;

    private int id;

    private boolean noComments, backDated;

    public static final String PRIVATE = "private";

    public static final String PUBLIC = "public";

    public static final String FRIENDS = "usemask&allowmask=1";

    public LJPost() {
        body = "";
        title = "";
        currentMood = "";
        currentMusic = "";
        pictureKeyword = "";
        journal = "";
        security = PUBLIC;
        allowMask = 0;
        id = 0;
        noComments = false;
        backDated = false;
        tags = null;
    }

    public LJPost(String body, String title, String currentMood, String currentMusic, String security, int allowMask) {
        this.body = body;
        this.title = title;
        this.calendar = Calendar.getInstance();
        this.currentMood = currentMood;
        this.currentMusic = currentMusic;
        this.security = security;
        this.allowMask = allowMask;
        id = 0;
        pictureKeyword = "";
        journal = "";
        noComments = false;
        backDated = false;
        tags = null;
    }

    public LJPost(String body, String title, String currentMood, String currentMusic) {
        this.body = body;
        this.title = title;
        this.currentMood = currentMood;
        this.currentMusic = currentMusic;
        pictureKeyword = "";
        journal = "";
        noComments = false;
        backDated = false;
        tags = null;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String getCurrentMood() {
        return currentMood;
    }

    public void setCurrentMood(String currentMood) {
        this.currentMood = currentMood;
    }

    public String getCurrentMusic() {
        return currentMusic;
    }

    public void setCurrentMusic(String currentMusic) {
        this.currentMusic = currentMusic;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public int getAllowMask() {
        return allowMask;
    }

    public void setAllowMask(int allowMask) {
        this.allowMask = allowMask;
    }

    public boolean getNoComments() {
        return noComments;
    }

    public void setNoComments(boolean noComments) {
        this.noComments = noComments;
    }

    public boolean getBackDated() {
        return backDated;
    }

    public void setBackDated(boolean backDated) {
        this.backDated = backDated;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getPictureKeyword() {
        return pictureKeyword;
    }

    public void setPictureKeyword(String pictureKeyword) {
        this.pictureKeyword = pictureKeyword;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void addTag(String newTag) {
        if (tags == null) {
            tags = new Vector();
        }
        tags.addElement(newTag);
    }

    public Enumeration getTags() {
        if (tags == null) return null;
        return tags.elements();
    }

    public String getTagsAsString() {
        if (tags == null) return "";
        StringBuffer buf = new StringBuffer();
        Enumeration enumeration = tags.elements();
        if (enumeration.hasMoreElements()) buf.append((String) enumeration.nextElement());
        while (enumeration.hasMoreElements()) {
            buf.append(", ");
            buf.append((String) enumeration.nextElement());
        }
        return buf.toString();
    }

    public void addTagsAsString(String str) {
        Enumeration enumeration = StringTokenizer.tokenize(str, ',').elements();
        if (tags == null) tags = new Vector();
        while (enumeration.hasMoreElements()) {
            String newStr = (String) enumeration.nextElement();
            addTag(newStr.trim());
        }
        ;
    }

    public void setTagsAsString(String str) {
        Enumeration enumeration = StringTokenizer.tokenize(str, ',').elements();
        if (tags != null) tags.removeAllElements();
        while (enumeration.hasMoreElements()) {
            String newStr = (String) enumeration.nextElement();
            addTag(newStr.trim());
        }
        ;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
