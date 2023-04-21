package com.cubusmail.common.model;

import java.io.Serializable;

/**
 * Preferences POJO.
 * 
 * @author Juergen Schlierf
 */
@SuppressWarnings("serial")
public class Preferences implements Serializable {

    public static final int READING_PANE_BOTTOM = 0;

    public static final int READING_PANE_RIGHT = 1;

    public static final int READING_PANE_HIDE = 2;

    public String language;

    public String inboxFolderName;

    public String draftFolderName;

    public String sentFolderName;

    public String trashFolderName;

    public int messagesReloadPeriod;

    public String timezone;

    /**
	 * Offset in minutes.
	 */
    public int timezoneOffset;

    public int pageCount;

    public boolean shortTimeFormat;

    public boolean showHtml;

    public boolean createHtmlMsgs;

    public String confirmation;

    public boolean markAsDeletedWithoutTrash;

    public boolean emptyTrashAfterLogout;

    public String theme;

    public int readingPane;

    /**
	 * @return Returns the language.
	 */
    public String getLanguage() {
        return this.language;
    }

    /**
	 * @param language
	 *            The language to set.
	 */
    public void setLanguage(String language) {
        this.language = language;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    /**
	 * @return Returns the draftFolderName.
	 */
    public String getDraftFolderName() {
        return this.draftFolderName;
    }

    /**
	 * @param draftFolderName
	 *            The draftFolderName to set.
	 */
    public void setDraftFolderName(String draftFolderName) {
        this.draftFolderName = draftFolderName;
    }

    /**
	 * @return Returns the sentFolderName.
	 */
    public String getSentFolderName() {
        return this.sentFolderName;
    }

    /**
	 * @param sentFolderName
	 *            The sentFolderName to set.
	 */
    public void setSentFolderName(String sentFolderName) {
        this.sentFolderName = sentFolderName;
    }

    /**
	 * @return Returns the trashFolderName.
	 */
    public String getTrashFolderName() {
        return this.trashFolderName;
    }

    /**
	 * @param trashFolderName
	 *            The trashFolderName to set.
	 */
    public void setTrashFolderName(String trashFolderName) {
        this.trashFolderName = trashFolderName;
    }

    /**
	 * @return Returns the messagesReloadPeriod.
	 */
    public int getMessagesReloadPeriod() {
        return this.messagesReloadPeriod;
    }

    /**
	 * @param messagesReloadPeriod
	 *            The messagesReloadPeriod to set.
	 */
    public void setMessagesReloadPeriod(int messagesReloadPeriod) {
        this.messagesReloadPeriod = messagesReloadPeriod;
    }

    /**
	 * @return Returns the timezone.
	 */
    public String getTimezone() {
        return this.timezone;
    }

    /**
	 * @param timezone
	 *            The timezone to set.
	 */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
	 * @return Returns the pageCount.
	 */
    public int getPageCount() {
        return this.pageCount;
    }

    /**
	 * @param pageCount
	 *            The pageCount to set.
	 */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
	 * @return Returns the shortTimeFormat.
	 */
    public boolean isShortTimeFormat() {
        return this.shortTimeFormat;
    }

    /**
	 * @param shortTimeFormat
	 *            The shortTimeFormat to set.
	 */
    public void setShortTimeFormat(boolean shortTimeFormat) {
        this.shortTimeFormat = shortTimeFormat;
    }

    /**
	 * @return Returns the showHtml.
	 */
    public boolean isShowHtml() {
        return this.showHtml;
    }

    /**
	 * @param showHtml
	 *            The showHtml to set.
	 */
    public void setShowHtml(boolean showHtml) {
        this.showHtml = showHtml;
    }

    /**
	 * @return Returns the createHtmlMsgs.
	 */
    public boolean isCreateHtmlMsgs() {
        return this.createHtmlMsgs;
    }

    /**
	 * @param createHtmlMsgs
	 *            The createHtmlMsgs to set.
	 */
    public void setCreateHtmlMsgs(boolean createHtmlMsgs) {
        this.createHtmlMsgs = createHtmlMsgs;
    }

    /**
	 * @return Returns the confirmation.
	 */
    public String getConfirmation() {
        return this.confirmation;
    }

    /**
	 * @param confirmation
	 *            The confirmation to set.
	 */
    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    /**
	 * @return Returns the previewWindow.
	 */
    public boolean isShowReadingPane() {
        return this.readingPane != READING_PANE_HIDE;
    }

    /**
	 * @return Returns the markAsDeletedWithoutTrash.
	 */
    public boolean isMarkAsDeletedWithoutTrash() {
        return this.markAsDeletedWithoutTrash;
    }

    /**
	 * @param markAsDeletedWithoutTrash
	 *            The markAsDeletedWithoutTrash to set.
	 */
    public void setMarkAsDeletedWithoutTrash(boolean markAsDeleted) {
        this.markAsDeletedWithoutTrash = markAsDeleted;
    }

    /**
	 * @return Returns the theme.
	 */
    public String getTheme() {
        return this.theme;
    }

    /**
	 * @param theme
	 *            The theme to set.
	 */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Preferences clone() {
        Preferences clone = new Preferences();
        clone.language = this.language;
        clone.inboxFolderName = this.inboxFolderName;
        clone.draftFolderName = this.draftFolderName;
        clone.sentFolderName = this.sentFolderName;
        clone.trashFolderName = this.trashFolderName;
        clone.messagesReloadPeriod = this.messagesReloadPeriod;
        clone.timezone = this.timezone;
        clone.pageCount = this.pageCount;
        clone.shortTimeFormat = this.shortTimeFormat;
        clone.showHtml = this.showHtml;
        clone.createHtmlMsgs = this.createHtmlMsgs;
        clone.confirmation = this.confirmation;
        clone.markAsDeletedWithoutTrash = this.markAsDeletedWithoutTrash;
        clone.emptyTrashAfterLogout = this.emptyTrashAfterLogout;
        clone.theme = this.theme;
        clone.readingPane = this.readingPane;
        clone.timezoneOffset = this.timezoneOffset;
        return clone;
    }

    /**
	 * @return Returns the emptyTrashAfterLogout.
	 */
    public boolean isEmptyTrashAfterLogout() {
        return this.emptyTrashAfterLogout;
    }

    /**
	 * @param emptyTrashAfterLogout
	 *            The emptyTrashAfterLogout to set.
	 */
    public void setEmptyTrashAfterLogout(boolean emptyTrashAfterLogout) {
        this.emptyTrashAfterLogout = emptyTrashAfterLogout;
    }

    /**
	 * @return Returns the readingPane.
	 */
    public int getReadingPane() {
        return this.readingPane;
    }

    /**
	 * @param readingPane
	 *            The readingPane to set.
	 */
    public void setReadingPane(int readingPane) {
        this.readingPane = readingPane;
    }

    public void setInboxFolderName(String inboxFolderName) {
        this.inboxFolderName = inboxFolderName;
    }

    public String getInboxFolderName() {
        return this.inboxFolderName;
    }
}
