package ru.newton.pokertrainer.businesslogic.databaseinterface.handhistory;

/**
 * @author newton
 *         Date: Jan 18, 2011
 */
public class DeskListRow {

    private int id = 0;

    private int pokerSiteId = 0;

    private String deskName = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPokerSiteId() {
        return pokerSiteId;
    }

    public void setPokerSiteId(int pokerSiteId) {
        this.pokerSiteId = pokerSiteId;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }
}
