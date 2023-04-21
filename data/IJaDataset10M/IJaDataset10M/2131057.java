package Pepys;

import java.util.*;

/** <tt>Post</tt> is a relatively simple container class representing a 
 *  LiveJournal event.  It contains fields to store all of the information 
 *  that could be sent in a LiveJournal post.  As more properties are added,
 *  <tt>Post</tt> can be easily updated to incorporate such properties.
 *  @version 1.1.0 2001, 07, 19
 *  @author John Price
 */
public class Post {

    public static final String SEC_PRIVATE = "private";

    public static final String SEC_PUBLIC = "public";

    public static final String SEC_USEMASK = "usemask";

    public static final String FRIENDSMASK = "00000000000000000000000000000000";

    public static final int NUM_PROPERTIES = 5;

    public static final int SUBJECT = 0;

    public static final int MUSIC = 1;

    public static final int MOOD = 2;

    public static final int PICTURE = 3;

    public static final int USE_JOURNAL = 4;

    private boolean[] properties = new boolean[NUM_PROPERTIES];

    private String subject;

    private String message;

    private String mood;

    private String music;

    private String securityLevel = SEC_PUBLIC;

    private String allowMask;

    private String pictureKeyword;

    private String useJournal;

    private LJDate date = new LJDate();

    private boolean isBackDated;

    private boolean dontAutoFormat;

    private boolean dontAllowComments;

    private int itemID;

    /** Create a post with no values filled in.
     */
    public Post() {
    }

    /** Create a post with values specified in a hashtable (as would be
     *  returned from the server via LJClient).  Typically this is used with
     *  the "getevents" protocol mode, which could return many events (posts)
     *  in the same hashtable.  The desired event can be specified by the
     *  index assigned to it by LJClient.
     *  @param client the LJClient
     *  @param h a Hashtable containing key value pairs as specified in the
     *		"getevents" LJ protocol mode
     *  @param i which of possibly many events to pick out of the hashtable
     */
    public Post(LJClient client, Hashtable h, int i) throws IllegalArgumentException {
        if (h == null) throw new IllegalArgumentException("Bad post data");
        int numEvents = Integer.parseInt((String) h.get("events_count"));
        if (numEvents < i) throw new IllegalArgumentException("Bad post data");
        this.message = (String) h.get(("events_" + i + "_event"));
        this.subject = (String) h.get(("events_" + i + "_subject"));
        this.allowMask = (String) h.get(("events_" + i + "_allowmask"));
        this.securityLevel = (String) h.get(("events_" + i + "_security"));
        String dateString = (String) h.get(("events_" + i + "_eventtime"));
        this.date = new LJDate(dateString);
        itemID = Integer.parseInt((String) h.get("events_" + i + "_itemid"));
        String pc = (String) h.get("prop_count");
        if (pc != null) {
            int propCount = Integer.parseInt(pc);
            for (int j = 1; j <= propCount; j++) {
                int itemn = Integer.parseInt((String) h.get("prop_" + j + "_itemid"));
                if (itemn == itemID) {
                    String propName = (String) h.get("prop_" + j + "_name");
                    if (propName.equals("prop_current_music")) {
                        music = (String) h.get("prop_" + j + "_value");
                    } else if (propName.equals("prop_current_moodid")) {
                        int moodid = Integer.parseInt((String) h.get("prop_" + j + "_value"));
                        mood = client.getMoodName(moodid);
                    } else if (propName.equals("prop_picture_keyword")) {
                        pictureKeyword = (String) h.get("prop_" + j + "_value");
                    } else if (propName.equals("prop_opt_backdated")) {
                        int bd = Integer.parseInt((String) h.get("prop_" + j + "_value"));
                        if (bd == 1) isBackDated = true; else isBackDated = false;
                    }
                }
            }
        }
    }

    /** Create a post with values specified in a hashtable (as would be
     *  returned from the server via LJClient).  Typically this is used with
     *  the "getevents" protocol mode, which could return many events (posts)
     *  in the same hashtable.  This constructer assumes you just want the
     *  first (and possibly only) one.
     *  @param client the LJClient
     *  @param h a Hashtable containing key value pairs as specified in the
     *		"getevents" LJ protocol mode
     */
    public Post(LJClient client, Hashtable h) throws IllegalArgumentException {
        this(client, h, 1);
    }

    /** Check to see if a particular propery has been defined for this post.
     *  Which property to check is specified using the constants defined in
     *  this class.
     *  @param whichProp the property to check for
     *  @return true if the specified property has been set for this post
     */
    public boolean hasProperty(int whichProp) {
        return properties[whichProp];
    }

    /** Set the subject of the post.
     *  @param subject what to set the subject to
     */
    public void setSubject(String subject) {
        this.subject = subject;
        toggleProperty(SUBJECT, subject);
    }

    /** Get the subject of this post.
     *  @return the subject of this post
     */
    public String getSubject() {
        return subject;
    }

    /** Set the message of the post.
     *  @param message what to send as the body of the post
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /** Get the message of this post.
     *  @return the message of this post
     */
    public String getMessage() {
        return message;
    }

    /** Set the mood of the post.
     *  @param mood what to set the mood to
     */
    public void setMood(String mood) {
        this.mood = mood;
        toggleProperty(MOOD, mood);
    }

    /** Get the mood of this post.
     *  @return the mood of this post
     */
    public String getMood() {
        return mood;
    }

    /** Set the "current music" of the post.
     *  @param music what to set the post's "current music" to
     */
    public void setMusic(String music) {
        this.music = music;
        toggleProperty(MUSIC, music);
    }

    /** Get the music of this post.
     *  @return the music of this post
     */
    public String getMusic() {
        return music;
    }

    /** Set the security level of the post. <b>clarify this</b>
     *  @param secLevel the security level of this post
     */
    public void setSecurity(String secLevel) {
        this.securityLevel = secLevel;
    }

    /** Get the security level of this post.
     *  @return the security level of this post
     */
    public String getSecurity() {
        return securityLevel;
    }

    /** Set the friends mask for this post.
     *  @param mask the friends mask for this post
     */
    public void setMask(String mask) {
        this.allowMask = mask;
    }

    /** Get the friends mask of this post.
     *  @return the friends mask of this post
     */
    public String getMask() {
        return allowMask;
    }

    /** Set the picture keyword for this post
     *  @param pkw the picture keyword for this post
     */
    public void setPictureKeyword(String pkw) {
        this.pictureKeyword = pkw;
        toggleProperty(PICTURE, pkw);
    }

    /** Get the picture keyword of this post.
     *  @return the picture keyword of this post
     */
    public String getPictureKeyword() {
        return pictureKeyword;
    }

    /** Set the shared community to post this event to
     *  @param journal the shared journal to post this to
     */
    public void setJournal(String journal) {
        this.useJournal = journal;
        toggleProperty(USE_JOURNAL, journal);
    }

    /** Get the shared journal this post is going to (if any)
     *  @return the shared journal (if any) to add this post to
     */
    public String getJournal() {
        return useJournal;
    }

    /** Set the date for this post
     *  @param date the date for this post
     */
    public void setLJDate(LJDate date) {
        this.date = date;
    }

    /** Get the date this post was made as an LJDate object.
     *  @return the date this post was made
     */
    public LJDate getLJDate() {
        return date;
    }

    /** Set the date for this post
     *  @param date the date for this post
     */
    public void setDate(GregorianCalendar date) {
        this.date = new LJDate(date);
    }

    /** Get the date this post was made as a GregorianCalendar object
     *  @return the date this post was made
     */
    public GregorianCalendar getDate() {
        return date.getGregCal();
    }

    /** Toggle backdating for this post
     *  @param isBackDated if true, this post will be backdated
     */
    public void setIsBackDated(boolean isBackDated) {
        this.isBackDated = isBackDated;
    }

    /** Check to see if this post is backdated
     *  @deprecated use <code>isBackDated()</code> instead
     *  @return true if this post is backdated
     */
    public boolean getIsBackDated() {
        return isBackDated;
    }

    /** Check to see if this post is backdated.
     *  @return true if this post is backdated
     */
    public boolean isBackDated() {
        return isBackDated;
    }

    private void setItemID(int itemID) {
        this.itemID = itemID;
    }

    /** Get the itemID of this post.
     *  @return the itemID of this post
     */
    public int getItemID() {
        return itemID;
    }

    /** Toggle disabling of comments for this post
     *  @deprecated use <code>disableComments( boolean )</code> instead
     *  @param disable if true, commenting will be disabled on this post
     */
    public void setDisableComments(boolean disable) {
        this.dontAllowComments = disable;
    }

    /** Toggle disabling of comments for this post
     *  @param disable if true, commenting will be disabled on this post
     */
    public void disableComments(boolean disable) {
        this.dontAllowComments = disable;
    }

    /** Check to see if commenting is disabled for this post
     *  @deprecated use <code>commentsDisabled()</code> instead
     *  @return true if comments are disabled for this post
     */
    public boolean getDisableComments() {
        return this.dontAllowComments;
    }

    /** Check to see if commenting is disabled for this post
     *  @return true if comments are disabled for this post
     */
    public boolean commentsDisabled() {
        return this.dontAllowComments;
    }

    /** Toggle auto-formatting for this post.
     *  @deprecated use <code>disableAutoFormat( boolean )</code> instead
     *  @param disable if true, auto-formatting will be disabled for this post
     */
    public void setDontAutoFormat(boolean disable) {
        this.dontAutoFormat = disable;
    }

    /** Toggle auto-formatting for this post (default is "on").
     *  @param disable if false, auto-formatting will be disabled for this post
     */
    public void disableAutoFormat(boolean disable) {
        this.dontAutoFormat = disable;
    }

    /** Check to see if autoformatting is turned off for this post.
     *  @deprecated use <code>autoFormatDisabled()</code> instead
     *  @return true if auto-formatting is off for this post
     */
    public boolean getDontAutoFormat() {
        return this.dontAutoFormat;
    }

    /** Check to see if autoformatting is turned off for this post.
     *  @return true if auto-formatting is off for this post
     */
    public boolean autoFormatDisabled() {
        return this.dontAutoFormat;
    }

    private void toggleProperty(int whichProperty, String propVal) {
        if (propVal != null && !propVal.equals("")) properties[whichProperty] = true; else properties[whichProperty] = false;
    }
}
