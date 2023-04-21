package org.langkiss.data;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.langkiss.util.OwnPreferences;
import org.langkiss.util.TimeStamp;

/**
 * This object is a data holder for
 * <ul>
 * <li>unique id (important for synchronising and sharing)</li>
 * <li>language A</li>
 * <li>language B</li>
 * <li>eplanation</li>
 * <li>tags</li>
 * <li>time last modified of the fields above</li>
 * <li>deck (reflects the learning progress)</li>
 * <li>time last learned (more exactly: last moved into a deck)</li>
 * <li>time the card is due for learning</li>
 * </ul>
 * Every of these property can be empty except the unique id.
 *
 * @author tom
 */
public class Card {

    private String timeStampFormat;

    private String id;

    private String languageA;

    private String languageB;

    private String explanation;

    private String tags;

    private String lastModified;

    private String deck;

    private String lastLearned;

    private String due;

    private String learnCount;

    private String csvDelimiter;

    private int dayDueFailed;

    private int dayDuePassedHard;

    private int dayDuePassed;

    private int dayDuePassedEasy;

    private boolean isDue;

    private MergeResult mergeResult;

    private Pattern idPattern;

    private String idPatternString;

    public Card() {
        if (timeStampFormat == null) {
            OwnPreferences prefs = LangKissPreferences.getPreferences();
            this.timeStampFormat = prefs.get(LangKissPreferences.KEY_DATE_FORMAT, LangKissPreferences.DEFAULT_DATE_FORMAT);
            Logger.getLogger(Card.class.getName()).log(Level.CONFIG, "Date format is: ''{0}''.", timeStampFormat);
            TimeStamp.setDefaultDateFormat(timeStampFormat);
            this.csvDelimiter = prefs.get(LangKissPreferences.KEY_CSV_DELIMITER, LangKissPreferences.DEFAULT_CSV_DELIMITER);
            this.idPatternString = prefs.get(LangKissPreferences.KEY_REGULAR_EXPRESSION_CARD_ID, LangKissPreferences.DEFAULT_REGULAR_EXPRESSION_CARD_ID);
        }
        this.createUniqueId();
    }

    /**
     * Load the properties from a (text) line of comma separeted value. It uses
     * a separator character to split the line into the values. The default
     * seperator character (delimiter) is a semicolon ';' but can be set from
     * outside.<br/>
     * <br/>
     * Example line<br/>
     * 6123456789012;filia;Tochter;Filiale <br/> a-Deklination;L001 Wortschatz;2011-11-18 21:11:23.111;2;2011-11-19 15:03:00.001<br/>
     * <br/>
     * The default date format is 'yyyy-MM-dd HH:mm:ss.SSS' but can be set
     * to a different format from outside, for example loaded from the
     * properties file during start up of the programm.<br/>
     * Take care changing the date format. Why? It will throw away existing
     * dates/times (of modifying and learning) but won't touch the content to
     * learn (vocabulary).<br/>
     * <br/>
     * Every Card gets a unique id at the time of its creation. This id will be
     * overwritten if set in the csv line (parameter). This is important to know
     * for imports of Cards from sources different from this programm.
     * This way it is possible to import Cards from a different programm or
     * source that do not use unique ids for every Card.
     * 
     * @param csvLine 
     */
    public void load(String csvLine) throws CardCreationException {
        Logger.getLogger(Card.class.getName()).log(Level.FINER, "Loading card from line: {0}", csvLine);
        if (csvLine == null) {
            String msg = "Empty value for a line that should load a Card.";
            Logger.getLogger(Card.class.getName()).log(Level.SEVERE, msg);
            throw new CardCreationException(msg);
        }
        if (this.idPatternString != null && !"".equals(this.idPatternString)) {
            try {
                this.checkId(csvLine);
            } catch (java.util.regex.PatternSyntaxException ex) {
                String msg = "Failed to check card IDs. The regular search expression '" + this.idPatternString + "' is not correct.";
                Logger.getLogger(Card.class.getName()).log(Level.SEVERE, msg);
                throw new CardCreationException(msg);
            }
        } else {
            String msg = "Failed to load cards from a csv line. Why? Check of card IDs failed, because the search expression was 'null' or empty.";
            Logger.getLogger(Card.class.getName()).log(Level.SEVERE, msg);
            throw new CardCreationException(msg);
        }
        String[] splittees = csvLine.split(this.csvDelimiter);
        int length = splittees.length;
        CardValueFormater formater = new CardValueFormater();
        formater.setCsvDelimiter(this.csvDelimiter);
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                this.id = splittees[i];
            } else if (i == 1) {
                this.languageA = formater.formatCSVtoCARD(splittees[i]);
            } else if (i == 2) {
                this.languageB = formater.formatCSVtoCARD(splittees[i]);
            } else if (i == 3) {
                this.explanation = formater.formatCSVtoCARD(splittees[i]);
            } else if (i == 4) {
                this.tags = formater.replaceNewLinesWithSpaces(splittees[i]);
            } else if (i == 5) {
                this.setLastModified(splittees[i].trim());
            } else if (i == 6) {
                String value = splittees[i].trim();
                this.setDeck(value);
            } else if (i == 7) {
                String value = splittees[i].trim();
                this.setLearnCount(value);
            } else if (i == 8) {
                this.setLastLearned(splittees[i].trim());
            } else if (i == 9) {
                this.setDue(splittees[i].trim());
            }
        }
        if (this.id != null && !"".equals(this.id)) {
            Logger.getLogger(Card.class.getName()).log(Level.FINER, "Card is (toString): {0}", this.toString());
        } else {
            this.createUniqueId();
        }
    }

    /**
     * Cards are created from a line of text. This method checks wether the line
     * starts with a valid id followed by a ';'. The used regular expression is
     * '^\d+?;'
     * 
     * @return true if the line starts with a valid id, false otherwise
     */
    private boolean checkId(String csvLine) throws CardCreationException {
        if (this.idPattern == null) {
            this.compileIdPatternSearchString();
        }
        Matcher m = this.idPattern.matcher(csvLine);
        if (m.find()) {
            Logger.getLogger(Card.class.getName()).log(Level.FINER, "Card id is valid.");
            return true;
        } else {
            String msg = "Found no valid card id (using regular expression = '" + this.idPatternString + "') in csv line 'csvLine'.";
            Logger.getLogger(Card.class.getName()).log(Level.FINER, msg);
            return false;
        }
    }

    /**
     * The unique id is created by the nano seconds of the system time at the
     * moment of its creation.<b/>
     * Assume that no Card in these Cards are created on another computer
     * (by another lerner) at exactly the same time (nano seconds since 1970).<b/>
     * It is very unlikely.<b/>
     * What if it ever happens? This is no big deal. This card is not
     * updated or added.
     */
    private void createUniqueId() {
        long nanoTime = System.nanoTime();
        this.id = Long.toString(nanoTime);
        Logger.getLogger(Card.class.getName()).log(Level.FINER, "Created unique id from nano seconds: {0}", this.id);
    }

    /**
     * Should be used for exporting the card.
     * 
     * @return 
     */
    public String getAsCSVline() {
        String s = this.toString();
        Logger.getLogger(Card.class.getName()).log(Level.FINER, "Returning Card as CSV line: {0}", s);
        return s;
    }

    public boolean hasNoContent() {
        if (this.getLanguageA().equals("") && this.getLanguageB().equals("") && this.getExplanation().equals("") && this.getTags().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * Merge two Cards objects<br/>
     * <br/>
     * Each Card is identified and compared by its unique id.<br/>
     * <br/>
     * Two parts of a Card a compared and merged separatly
     * <ul>
     * <li>The content: languageA;languageB;explanation;tags;lastModified</li>
     * <li>The deck: deck;lastLearned;due date</li>
     * </ul>
     * Both parts have its own timestamp. The card that has the newer timestamp
     * wins and overwrites the content or deck of the older card.<br/>
     * <br/>
     * What if the date format is not correct? Keep the local Card and add
     * different Cards.<br/>
     * <br/>
     * Deletion of a Card<br/>
     * There is no real deletion. If the GUI removes (deletes) a Card the Card
     * is not really deletet, instead its just no shown to the learner. 
     * <ul>
     * <li>The content is removed: languageA;languageB;explanation;tags;lastModified</li>
     * <li>The deck keeps the same: deck;lastLearned;due date</li>
     * </ul>
     * Why not delete the Card completely? If a Card is deleted on one computer
     * the Card is not recognised as to be removed on another computer because
     * the Card is simply not there any longer and can not tell that it was
     * deleted.
     * 
     * @param cardRemote
     * @param contentOnly
     */
    public void merge(Card cardRemote, boolean contentOnly) {
        if (cardRemote == null) {
            return;
        }
        String idLocal = this.getId();
        String idRemote = cardRemote.getId();
        if (!idLocal.equals(idRemote)) {
            return;
        }
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Merging Cards. Local: ''{0}'', Remote: ''{1}''", new Object[] { this.toString(), cardRemote.toString() });
        long lastModifiedLocal = TimeStamp.parse(this.getLastModified());
        long lastModifiedRemote = TimeStamp.parse(cardRemote.getLastModified());
        if (lastModifiedLocal == 0) {
            if (this.mergeResult != null) {
                this.mergeResult.getDateFormatContentLocalMissingOrFailed().add(this);
            }
        }
        if (lastModifiedRemote == 0) {
            if (this.mergeResult != null) {
                this.mergeResult.getDateFormatContentRemoteMissingOrFailed().add(contentOnly);
            }
        }
        if (lastModifiedLocal < lastModifiedRemote) {
            Logger.getLogger(Card.class.getName()).log(Level.FINE, "Remote card is newer and overwrites the content of the local Card.");
            boolean contendWasChanged = false;
            if (!this.getLanguageA().trim().equals(cardRemote.getLanguageA().trim())) {
                contendWasChanged = true;
                this.languageA = cardRemote.getLanguageA().trim();
            }
            if (!this.getLanguageB().trim().equals(cardRemote.getLanguageB().trim())) {
                contendWasChanged = true;
                this.languageB = cardRemote.getLanguageB().trim();
            }
            if (!this.getExplanation().trim().equals(cardRemote.getExplanation().trim())) {
                contendWasChanged = true;
                this.explanation = cardRemote.getExplanation().trim();
            }
            if (!this.getTags().trim().equals(cardRemote.getTags().trim())) {
                contendWasChanged = true;
                this.tags = cardRemote.getTags().trim();
            }
            if (!this.getLastModified().trim().equals(cardRemote.getLastModified().trim())) {
                contendWasChanged = true;
                this.lastModified = cardRemote.getLastModified().trim();
            }
            if ("".equals(this.languageA) && "".equals(this.languageB) && "".equals(this.explanation) && "".equals(this.tags)) {
                if (contendWasChanged) {
                    if (this.mergeResult != null) {
                        this.mergeResult.getDeleted().add(this);
                    }
                    Logger.getLogger(Card.class.getName()).log(Level.FINE, "Content of local Card was deleted.");
                    return;
                }
            } else {
                if (contendWasChanged) {
                    if (this.mergeResult != null) {
                        this.mergeResult.getUpdatedContent().add(this);
                    }
                }
            }
        } else {
            Logger.getLogger(Card.class.getName()).log(Level.FINE, "Content of local Card was not updated because it is newer.");
        }
        if (contentOnly) {
            return;
        }
        long lastLearnedLocal = TimeStamp.parse(this.getLastLearned());
        long lastLearnedRemote = TimeStamp.parse(cardRemote.getLastLearned());
        long dueLocal = TimeStamp.parse(this.getDue());
        long dueRemote = TimeStamp.parse(cardRemote.getDue());
        if (lastLearnedLocal == 0) {
            if (this.mergeResult != null) {
                this.mergeResult.getDateFormatLearnedLocalMissingOrFailed().add(this);
            }
        }
        if (lastLearnedRemote == 0) {
            if (this.mergeResult != null) {
                this.mergeResult.getDateFormatLearnedRemoteMissingOrFailed().add(cardRemote);
            }
        }
        if (dueLocal == 0) {
            if (this.mergeResult != null) {
                this.mergeResult.getDateFormatDueLocalMissingOrFailed().add(this);
            }
        }
        if (dueRemote == 0) {
            if (this.mergeResult != null) {
                this.mergeResult.getDateFormatDueRemoteMissingOrFailed().add(cardRemote);
            }
        }
        if (lastLearnedLocal < lastLearnedRemote) {
            Logger.getLogger(Card.class.getName()).log(Level.FINE, "Remote card has newer progress data and overwrites the progress of the local Card.");
            boolean contendWasChanged = false;
            if (!this.getDeck().trim().equals(cardRemote.getDeck().trim())) {
                contendWasChanged = true;
                this.deck = cardRemote.getDeck().trim();
            }
            if (!this.getLearnCount().trim().equals(cardRemote.getLearnCount().trim())) {
                contendWasChanged = true;
                this.learnCount = cardRemote.getLearnCount().trim();
            }
            if (!this.getLastLearned().trim().equals(cardRemote.getLastLearned().trim())) {
                contendWasChanged = true;
                this.lastLearned = cardRemote.getLastLearned().trim();
            }
            if (!this.getDue().trim().equals(cardRemote.getDue().trim())) {
                contendWasChanged = true;
                this.due = cardRemote.getDue().trim();
            }
            if (contendWasChanged) {
                if (this.mergeResult != null) {
                    this.mergeResult.getUpdatedProgress().add(this);
                }
            }
        } else {
            Logger.getLogger(Card.class.getName()).log(Level.FINE, "Progress of local Card was not updated because it is newer.");
        }
    }

    public Card copy() {
        String csvLine = this.getAsCSVline();
        Card card = new Card();
        try {
            card.load(csvLine);
        } catch (CardCreationException ex) {
            Logger.getLogger(Card.class.getName()).log(Level.SEVERE, "Copy of card failed.", ex);
            return null;
        }
        return card;
    }

    public Card copyContentOnly() {
        String csvLine = this.getAsCSVline();
        Card card = new Card();
        try {
            card.load(csvLine);
        } catch (CardCreationException ex) {
            Logger.getLogger(Card.class.getName()).log(Level.SEVERE, "Copy (content only) of card failed.", ex);
            return null;
        }
        card.setDeck("");
        card.setLearnCount("");
        card.setLastLearned("");
        card.setDue("");
        return card;
    }

    /**
     * Move the Card from deck 1 to deck 7.
     */
    public void jumpOneDeckHigher() {
        try {
            if (this.getDeck() == null) {
                this.deck = "2";
                return;
            }
            if (this.getDeck().equals("")) {
                this.deck = "2";
                return;
            }
            int i = Integer.parseInt(this.getDeck());
            if (i < 7) {
                i++;
                Logger.getLogger(Card.class.getName()).log(Level.FINER, "Deck of Card is already '7'.");
            }
            this.deck = Integer.toString(i);
            Logger.getLogger(Card.class.getName()).log(Level.FINE, "Deck of Card is now: {0}", this.deck);
        } catch (NumberFormatException ex) {
            Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set deck to '0'. Deck is not a number. Deck: ''{0}" + "'', Card: ''" + "''", this.getDeck());
            this.deck = "1";
        }
    }

    /**
     * Move the Card from deck 1 to deck 7.
     */
    public void countLearned() {
        try {
            int i = Integer.parseInt(this.getLearnCount());
            i++;
            Logger.getLogger(Card.class.getName()).log(Level.FINER, "Counted learning. Counter is now{0}", i);
            this.learnCount = Integer.toString(i);
        } catch (NumberFormatException ex) {
            Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set learn counter to '0'. Counter is not a number. Counter: ''{0}'', Card: '" + this.toString() + "'", this.getLearnCount());
            this.learnCount = "0";
        }
    }

    /**
     * Jump back to deck 1.
     */
    public void jumpBackToFirstDeck() {
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Deck of Card was set back to: '1'.");
        this.deck = "1";
    }

    /**
     * It takes account of the seperator character (delimter 'aware')<br/>
     * 
     * @return Example: id;languageA;languageB;tags;lastModified;deck;lastLearned;due
     */
    @Override
    public String toString() {
        CardValueFormater formater = new CardValueFormater();
        formater.setCsvDelimiter(this.csvDelimiter);
        StringBuilder buf = new StringBuilder();
        String value = this.getId();
        buf.append(value);
        value = formater.formatCARDtoCSV(this.getLanguageA());
        buf.append(this.csvDelimiter).append(value);
        value = formater.formatCARDtoCSV(this.getLanguageB());
        buf.append(this.csvDelimiter).append(value);
        value = formater.formatCARDtoCSV(this.getExplanation());
        buf.append(this.csvDelimiter).append(value);
        value = formater.replaceNewLinesWithSpaces(this.getTags());
        buf.append(this.csvDelimiter).append(value);
        buf.append(this.csvDelimiter).append(this.getLastModified());
        buf.append(this.csvDelimiter).append(this.getDeck());
        buf.append(this.csvDelimiter).append(this.getLearnCount());
        buf.append(this.csvDelimiter).append(this.getLastLearned());
        buf.append(this.csvDelimiter).append(this.getDue());
        String s = buf.toString();
        Logger.getLogger(Card.class.getName()).log(Level.FINER, "toString() returns: {0}", s);
        return s;
    }

    /**
     * These are the columns: <br/>
     * <ul>
     * <li>0. id</li>
     * <li>1. language A</li>
     * <li>2. language B</li>
     * <li>3. explanation</li>
     * <li>4. tags</li>
     * <li>5. time last modified of the fields above</li>
     * <li>6. deck</li>
     * <li>7. learn count</li>
     * <li>8. time last learned</li>
     * <li>9. due date</li>
     * </ul>
     * @param columnCount
     * @return 
     */
    public String toStringForLeadingColumn(int columnCount) {
        if (columnCount < 0) {
            columnCount = 0;
        } else if (columnCount > 8) {
            columnCount = 9;
        }
        String s = null;
        if (columnCount == 0) {
            s = this.getId();
        } else if (columnCount == 1) {
            s = this.getLanguageA();
        } else if (columnCount == 2) {
            s = this.getLanguageB();
        } else if (columnCount == 3) {
            s = this.getExplanation();
        } else if (columnCount == 4) {
            s = this.getTags();
        } else if (columnCount == 5) {
            s = this.getLastModified();
        } else if (columnCount == 6) {
            s = this.getDeck();
        } else if (columnCount == 7) {
            s = this.getLearnCount();
        } else if (columnCount == 8) {
            s = this.getLastLearned();
        } else if (columnCount == 9) {
            s = this.getDue();
        }
        s = s + this.csvDelimiter + this.toString();
        Logger.getLogger(Card.class.getName()).log(Level.FINER, "toStringForLeadingColumn({0}) returns: {1}.", new Object[] { columnCount, s });
        return s;
    }

    public String toStringWithTrailingId() {
        String s = this.toString();
        s = s.substring(s.indexOf(this.csvDelimiter) + 1);
        s = s + this.csvDelimiter + this.getId();
        Logger.getLogger(Card.class.getName()).log(Level.FINER, "toStringWithTrailingId returns: {0}", s);
        return s;
    }

    /**
     * @return the languageA
     */
    public String getLanguageA() {
        if (this.languageA == null) {
            return "";
        }
        return languageA;
    }

    /**
     * @param languageA the languageA to set
     */
    public void setLanguageA(String languageA) {
        this.languageA = languageA;
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set languageA to:{0}", this.languageA);
    }

    /**
     * @return the languageB
     */
    public String getLanguageB() {
        if (this.languageB == null) {
            return "";
        }
        return languageB;
    }

    /**
     * @param languageB the languageB to set
     */
    public void setLanguageB(String languageB) {
        this.languageB = languageB;
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set languageB to:{0}", this.languageB);
    }

    /**
     * @return the explanation
     */
    public String getExplanation() {
        if (this.explanation == null) {
            return "";
        }
        return explanation;
    }

    /**
     * @param explanation the explanation to set
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set explanation to:{0}", this.explanation);
    }

    /**
     * trim double spaces (or more)
     * 
     * @return the tags as one String. Tags are separated by a space.
     */
    public String getTags() {
        if (this.tags == null) {
            return "";
        }
        if (this.tags.trim().equals("")) {
            return "";
        }
        TagFilter filter = new TagFilter();
        filter.load(tags);
        this.tags = filter.toString();
        return tags;
    }

    /**
     * trim double spaces (or more)
     * 
     * @param tags the tags to set
     */
    public void setTags(String tags) {
        if (tags == null) {
            return;
        }
        TagFilter filter = new TagFilter();
        filter.load(tags);
        this.tags = filter.toString();
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set tags to:{0}", this.tags);
    }

    /**
     * @return the lastModified
     */
    public String getLastModified() {
        if (this.lastModified == null) {
            return "";
        }
        return lastModified;
    }

    /**
     * @param lastModified the lastModified to set
     */
    public void setLastModified(String lastModified) {
        Long time = TimeStamp.parse(lastModified);
        if (time == 0) {
            this.lastModified = "";
            Logger.getLogger(Card.class.getName()).log(Level.FINE, "Found invalid date format for last modified: ''{0}'' but the format must be ''{1}''.", new Object[] { lastModified, timeStampFormat });
            return;
        }
        this.lastModified = lastModified;
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set last modified to:{0}", this.lastModified);
    }

    /**
     * Use this if you want to set the last modified date to the current time.
     * Creates a String in the default format "yyyy-MM-dd HH:mm:ss.SSS" or
     * as given in the properties file of languageKISS.
     */
    public void setLastModified() {
        this.lastModified = TimeStamp.getTimestamp(timeStampFormat);
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set last modified to:{0}", this.lastModified);
    }

    /**
     * @return the lastLearned
     */
    public String getLastLearned() {
        if (this.lastLearned == null) {
            return "";
        }
        return lastLearned;
    }

    /**
     * @param lastLearned the lastLearned to set
     */
    public void setLastLearned(String lastLearned) {
        Long time = TimeStamp.parse(lastLearned);
        if (time == 0) {
            this.lastLearned = "";
            Logger.getLogger(Card.class.getName()).log(Level.FINE, "Found invalid date format for last learned: ''{0}'' but the format must be ''{1}''.", new Object[] { lastLearned, timeStampFormat });
            return;
        }
        this.lastLearned = lastLearned;
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set last learned to:{0}", this.lastLearned);
    }

    /**
     * Use this if you want to set the last modified date to the current time.
     * Creates a String in the default format "yyyy-MM-dd HH:mm:ss.SSS" or
     * as given in the properties file of languageKISS.
     */
    public void setLastLearned() {
        this.lastLearned = TimeStamp.getTimestamp(timeStampFormat);
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set last learned to:{0}", this.lastLearned);
    }

    /**
     * @return the due date of card
     */
    public String getDue() {
        if (this.due == null) {
            return "";
        }
        return due;
    }

    /**
     * @param due date
     */
    public void setDue(String due) {
        Long time = TimeStamp.parse(due);
        if (time == 0) {
            this.due = "";
            Logger.getLogger(Card.class.getName()).log(Level.FINE, "Found invalid date format for due (date): ''{0}'' but the format must be ''{1}''.", new Object[] { due, timeStampFormat });
            return;
        }
        this.due = due;
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set due (date) to:{0}", this.due);
    }

    /**
     * Use this if you want to set the due date to the current date.
     * Creates a String in the default format "yyyy-MM-dd HH:mm:ss.SSS" or
     * as given in the properties file of languageKISS.
     * 
     * The hours, minutes,seconds are set to '0'. Example: param  days is
     * '2011-11-24 17:46:02.123' the due date would be '2011-11-24 00:00:00.000'.
     * The milliseconds might not be '000'.
     * 
     * @param day set due date today plus days (parameter value)
     */
    public void setDue(int days) {
        Calendar now = Calendar.getInstance(Locale.getDefault());
        long lastLastDueMilliSeconds = now.getTimeInMillis();
        long nextDueMilliSeconds = lastLastDueMilliSeconds + (1000 * 60 * 60 * 24 * (long) days);
        now.setTimeInMillis(nextDueMilliSeconds);
        this.due = TimeStamp.getFullTimstamp(now.getTime());
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set due (date) to:{0}", this.due);
    }

    /**
     * @return the csvDelimiter
     */
    public String getCsvDelimiter() {
        return csvDelimiter;
    }

    /**
     * @param csvDelimiter the csvDelimiter to set
     */
    public void setCsvDelimiter(String csvDelimiter) {
        this.csvDelimiter = csvDelimiter;
        this.compileIdPatternSearchString();
    }

    private void compileIdPatternSearchString() {
        String regExpr = this.idPatternString + this.csvDelimiter;
        this.idPattern = Pattern.compile(this.idPatternString);
    }

    /**
     * @return the deck
     */
    public String getDeck() {
        if (this.deck == null) {
            return "1";
        }
        if (this.deck.equals("")) {
            return "1";
        }
        return deck;
    }

    /**
     * @param deck the deck to set
     */
    public void setDeck(String deck) {
        if (deck == null) {
            Logger.getLogger(Card.class.getName()).log(Level.FINER, "Parameter deck was empty.");
            this.deck = "";
            return;
        }
        if (deck.equals("")) {
            Logger.getLogger(Card.class.getName()).log(Level.FINER, "Parameter deck was empty.");
            this.deck = "";
            return;
        }
        try {
            int i = Integer.parseInt(deck);
            if (i < 1) {
                Logger.getLogger(Card.class.getName()).log(Level.FINER, "Deck of Card was lower than '1'. Set deck to '1'.");
                this.deck = "1";
            } else if (i > 7) {
                Logger.getLogger(Card.class.getName()).log(Level.FINER, "Deck of Card was greater than '7'. Set deck to '7'.");
                this.deck = "7";
            } else {
                this.deck = deck;
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(Card.class.getName()).log(Level.FINE, "Set deck to ''0''. Value ''{0}'' is not a number.", deck);
            this.deck = "1";
        }
        Logger.getLogger(Card.class.getName()).log(Level.FINE, "Deck of Card is now: {0}", this.deck);
    }

    /**
     * Every Card has a unique id.<br/>
     * If this class creates the id it is the system time in nano seconds at
     * the moment of its creation.
     * 
     * @return The unique id. What if this Card has no id? In this case the 
     * unique id is created now. It is the system time in nano second (since
     * 1970).
     */
    public String getId() {
        if (id != null && !"".equals(id)) {
            return id;
        } else {
            this.createUniqueId();
            return this.id;
        }
    }

    /**
     * Every Card has a unique id.<br/>
     * If this class creates the id it is the system time in nano seconds at
     * the moment of its creation.
     * 
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param mergeResult the mergeResult to set
     */
    public void setMergeResult(MergeResult mergeResult) {
        this.mergeResult = mergeResult;
    }

    /**
     * @return the dayDueFailed
     */
    public int getDayDueFailed() {
        return dayDueFailed;
    }

    /**
     * @param dayDueFailed the dayDueFailed to set
     */
    public void setDayDueFailed(int dayDueFailed) {
        this.dayDueFailed = dayDueFailed;
    }

    /**
     * @return the dayDuePassedHard
     */
    public int getDayDuePassedHard() {
        return dayDuePassedHard;
    }

    /**
     * @param dayDuePassedHard the dayDuePassedHard to set
     */
    public void setDayDuePassedHard(int dayDuePassedHard) {
        this.dayDuePassedHard = dayDuePassedHard;
    }

    /**
     * @return the dayDuePassed
     */
    public int getDayDuePassed() {
        return dayDuePassed;
    }

    /**
     * @param dayDuePassed the dayDuePassed to set
     */
    public void setDayDuePassed(int dayDuePassed) {
        this.dayDuePassed = dayDuePassed;
    }

    /**
     * @return the dayDuePassedEasy
     */
    public int getDayDuePassedEasy() {
        return dayDuePassedEasy;
    }

    /**
     * @param dayDuePassedEasy the dayDuePassedEasy to set
     */
    public void setDayDuePassedEasy(int dayDuePassedEasy) {
        this.dayDuePassedEasy = dayDuePassedEasy;
    }

    /**
     * @return the isDue
     */
    public boolean isIsDue() {
        return isDue;
    }

    /**
     * @param isDue the isDue to set
     */
    public void setIsDue(boolean isDue) {
        this.isDue = isDue;
    }

    /**
     * @return the learnCount
     */
    public String getLearnCount() {
        if (this.learnCount == null) {
            return "0";
        }
        if (this.learnCount.equals("")) {
            return "0";
        }
        return this.learnCount.trim();
    }

    /**
     * @param learnCount the learnCount to set
     */
    public void setLearnCount(String learnCount) {
        this.learnCount = learnCount;
    }

    public boolean isCardDueForLearning(Card card) {
        Calendar now = Calendar.getInstance(Locale.getDefault());
        int yearNow = now.get(Calendar.YEAR);
        int monthNow = now.get(Calendar.MONTH);
        int dayNow = now.get(Calendar.DAY_OF_MONTH);
        now.set(yearNow, monthNow, dayNow, 0, 0, 2);
        long todayMilliSeconds = now.getTimeInMillis();
        Logger.getLogger(this.getClass().getName()).log(Level.FINER, "Check wether Card is due for learning. Card is ''{0}''...", new Object[] { card.getAsCSVline() });
        long dueMilliSeconds = TimeStamp.parse(this.getDue());
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(dueMilliSeconds);
        int yearLearned = cal.get(Calendar.YEAR);
        int monthLearned = cal.get(Calendar.MONTH);
        int dayLearned = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(yearLearned, monthLearned, dayLearned, 0, 0, 1);
        long dueDayLearnedMilliSeconds = cal.getTimeInMillis();
        String dueDate = TimeStamp.getFullTimstamp(new Date(dueDayLearnedMilliSeconds));
        String nowDate = TimeStamp.getFullTimstamp(new Date(todayMilliSeconds));
        Logger.getLogger(this.getClass().getName()).log(Level.FINER, "Due date: {0}, Today: {1}", new Object[] { dueDate, nowDate });
        if (dueDayLearnedMilliSeconds <= todayMilliSeconds) {
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Yes, Card is due for learning. Card is ''{0}''...", new Object[] { card.getAsCSVline() });
            return true;
        }
        return false;
    }
}
