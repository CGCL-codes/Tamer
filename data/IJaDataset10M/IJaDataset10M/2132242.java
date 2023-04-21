package biketreeDatastructures;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Godwin
 */
public class SaleDay extends BikeTreeDatastructure {

    private int openCount, closeCount;

    private BikeShopMember openUser, closeUser;

    private ArrayList<SaleDayEvent> payouts;

    private ArrayList<SaleDayEvent> deposits;

    private Date openTime, closeTime;

    private String comments;

    public SaleDay(int saleDayID, Date openedAt, BikeShopMember openedBy, int openingCount, Date closedAt, BikeShopMember closedBy, int closingCount, String dayComments) {
        super(saleDayID);
        setComments(dayComments);
        setOpeningTime(openedAt);
        setClosingTime(closedAt);
        setOpener(openedBy);
        setCloser(closedBy);
        setOpeningCount(openingCount);
        setClosingCount(closingCount);
        payouts = new ArrayList<SaleDayEvent>();
        deposits = new ArrayList<SaleDayEvent>();
    }

    public SaleDay(Date openedAt, BikeShopMember openedBy, int openingCount, String openComments) {
        this(-1, openedAt, openedBy, openingCount, (Date) null, (BikeShopMember) null, -1, openComments);
    }

    public int getOpeningCount() {
        return openCount;
    }

    public int getClosingCount() {
        return closeCount;
    }

    public BikeShopMember getOpener() {
        return openUser;
    }

    public BikeShopMember getCloser() {
        return closeUser;
    }

    public Date getOpeningTime() {
        return openTime;
    }

    public Date getClosingTime() {
        return closeTime;
    }

    public ArrayList<SaleDayEvent> getPayouts() {
        return payouts;
    }

    public ArrayList<SaleDayEvent> getDeposits() {
        return deposits;
    }

    public void addPayout(SaleDayEvent payout) {
        payouts.add(payout);
    }

    public void addDeposits(SaleDayEvent deposit) {
        deposits.add(deposit);
    }

    public void close(Date closedAt, BikeShopMember closedBy, int closingCount, String closeComments) {
        addComment(closeComments);
        setClosingTime(closedAt);
        setCloser(closedBy);
        setClosingCount(closingCount);
    }

    public void setOpeningCount(int newCount) {
        openCount = newCount;
    }

    public void setClosingCount(int newCount) {
        closeCount = newCount;
    }

    public void setOpener(BikeShopMember newUser) {
        openUser = newUser;
    }

    public void setCloser(BikeShopMember newUser) {
        closeUser = newUser;
    }

    public void setOpeningTime(Date newTime) {
        openTime = newTime;
    }

    public void setClosingTime(Date newTime) {
        closeTime = newTime;
    }

    public void setComments(String newComments) {
        comments = newComments;
    }

    public void addComment(String newComment) {
        if (comments == null || comments.isEmpty()) {
            setComments(newComment);
        } else {
            comments += "\n" + newComment;
        }
    }
}
