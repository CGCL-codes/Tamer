package net.sf.josser.jdbc.impl;

import java.sql.PreparedStatement;
import net.sf.josser.jdbc.Row;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class ExternalPage extends Row {

    private String link = null;

    private String Title = null;

    private String Description = null;

    private int catid = 0;

    private String ages = null;

    private String type = null;

    private int priority = 0;

    private String mediadate = null;

    private static PreparedStatement stmt = null;

    public ExternalPage() {
        this.setTablename("dmoz_externalpages");
        this.setLink(null);
        this.setTitle(null);
        this.setDescription(null);
        this.setCatid(0);
    }

    public ExternalPage(final String link, final String Title, final String Description, final int catid) {
        this.setTablename("dmoz_externalpages");
        this.setLink(link);
        this.setTitle(Title);
        this.setDescription(Description);
        this.setCatid(catid);
    }

    @Override
    public String getFields() {
        String temp = "(";
        temp = temp + " ages,";
        temp = temp + " type,";
        temp = temp + " link,";
        temp = temp + " Title,";
        temp = temp + " Description,";
        temp = temp + " mediadate,";
        temp = temp + " priority,";
        temp = temp + " catid ";
        temp = temp + ")";
        return temp;
    }

    @Override
    public void setValues() {
        try {
            if ((this.getAges() != null) && (this.getAges().length() > 0)) {
                this.getPreparedStatement().setString(1, this.getAges());
            } else {
                this.getPreparedStatement().setString(1, "");
            }
            if ((this.getType() != null) && (this.getType().length() > 0)) {
                this.getPreparedStatement().setString(2, this.getType());
            } else {
                this.getPreparedStatement().setString(2, "");
            }
            this.getPreparedStatement().setString(3, this.getLink());
            if ((this.getTitle() != null) && (this.getTitle().length() > 0)) {
                this.getPreparedStatement().setString(4, this.getTitle());
            } else {
                this.getPreparedStatement().setString(4, "");
            }
            if ((this.getDescription() != null) && (this.getDescription().length() > 0)) {
                this.getPreparedStatement().setString(5, this.getDescription());
            } else {
                this.getPreparedStatement().setString(5, "");
            }
            if ((this.getMediadate() != null) && (this.getMediadate().length() > 0)) {
                this.getPreparedStatement().setString(6, this.getMediadate());
            } else {
                this.getPreparedStatement().setString(6, "");
            }
            if (this.getPriority() != 0) {
                this.getPreparedStatement().setInt(7, this.getPriority());
            } else {
                this.getPreparedStatement().setInt(7, 0);
            }
            this.getPreparedStatement().setInt(8, this.getCatid());
        } catch (final Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public String getValues() {
        String temp = "";
        temp = temp + "?,";
        temp = temp + "?,";
        temp = temp + "?,";
        temp = temp + "?,";
        temp = temp + "?,";
        temp = temp + "?,";
        temp = temp + "?,";
        temp = temp + "?";
        return temp;
    }

    /**
	 * @param catid
	 *            The catid to set.
	 */
    public void setCatid(final int catid) {
        this.catid = catid;
    }

    /**
	 * @return Returns the catid.
	 */
    public int getCatid() {
        return this.catid;
    }

    /**
	 * @param description
	 *            The description to set.
	 */
    public void setDescription(final String description) {
        this.Description = description;
    }

    /**
	 * @return Returns the description.
	 */
    public String getDescription() {
        return this.Description;
    }

    /**
	 * @param link
	 *            The link to set.
	 */
    public void setLink(final String link) {
        this.link = link;
    }

    /**
	 * @return Returns the link.
	 */
    public String getLink() {
        return this.link;
    }

    /**
	 * @param mediadate
	 *            The mediadate to set.
	 */
    public void setMediadate(final String mediadate) {
        this.mediadate = mediadate;
    }

    /**
	 * @return Returns the mediadate.
	 */
    public String getMediadate() {
        return this.mediadate;
    }

    /**
	 * @param priority
	 *            The priority to set.
	 */
    public void setPriority(final int priority) {
        this.priority = priority;
    }

    /**
	 * @return Returns the priority.
	 */
    public int getPriority() {
        return this.priority;
    }

    /**
	 * @param title
	 *            The title to set.
	 */
    public void setTitle(final String title) {
        this.Title = title;
    }

    /**
	 * @return Returns the title.
	 */
    public String getTitle() {
        return this.Title;
    }

    /**
	 * @param type
	 *            The type to set.
	 */
    public void setType(final String type) {
        this.type = type;
    }

    /**
	 * @return Returns the type.
	 */
    public String getType() {
        return this.type;
    }

    /**
	 * @param stmt
	 *            The stmt to set.
	 */
    @Override
    protected void setStmt(final PreparedStatement stmt) {
        ExternalPage.stmt = stmt;
    }

    /**
	 * @return Returns the stmt.
	 */
    @Override
    protected PreparedStatement getStmt() {
        return ExternalPage.stmt;
    }

    /**
	 * @param ages
	 *            The ages to set.
	 */
    public void setAges(final String ages) {
        this.ages = ages;
    }

    /**
	 * @return Returns the ages.
	 */
    public String getAges() {
        return this.ages;
    }
}
