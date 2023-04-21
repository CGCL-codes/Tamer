package biketreeDatastructures;

import java.util.Date;

/**
 * Holds information about a deposit or payout
 * @author Godwin
 */
public class SaleDayEvent extends BikeTreeDatastructure {

    public enum SaleDayEvents {

        DEPOSIT("deposit"), PAYOUT("payout");

        String event;

        private SaleDayEvents(String e) {
            event = e;
        }

        @Override
        public String toString() {
            return event;
        }

        public static SaleDayEvents fromString(String e) {
            if (e.compareToIgnoreCase("deposit") == 0) {
                return DEPOSIT;
            }
            if (e.compareToIgnoreCase("payout") == 0) {
                return PAYOUT;
            }
            return null;
        }
    }

    public enum SalesAccounts {

        CASH("cash"), BANK("bank");

        String event;

        private SalesAccounts(String e) {
            event = e;
        }

        @Override
        public String toString() {
            return event;
        }

        public static SalesAccounts fromString(String e) {
            if (e.compareToIgnoreCase("cash") == 0) {
                return CASH;
            }
            if (e.compareToIgnoreCase("bank") == 0) {
                return BANK;
            }
            return null;
        }
    }

    private int ammount;

    private SaleDay saleDay;

    private SaleDayEvents event;

    private SalesAccounts account;

    private BikeShopMember user;

    private BikeShopMember member;

    private Date date;

    private String comments;

    public SaleDayEvent(int eventID, SaleDay day, SaleDayEvents e, SalesAccounts a, int theAmmount, Date time, BikeShopMember theUser, BikeShopMember theMember, String theComments) {
        super(eventID);
        saleDay = day;
        account = a;
        ammount = theAmmount;
        date = time;
        user = theUser;
        member = theMember;
        comments = theComments;
    }

    public SaleDay getSaleDay() {
        return saleDay;
    }

    public SaleDayEvents getEvent() {
        return event;
    }

    public SalesAccounts getAccount() {
        return account;
    }

    public int getAmmount() {
        return ammount;
    }

    public Date getDate() {
        return date;
    }

    public BikeShopMember getUser() {
        return user;
    }

    public BikeShopMember getMember() {
        return member;
    }

    public String getComments() {
        return comments;
    }

    public void setAccount(SalesAccounts newAccount) {
        account = newAccount;
    }

    public void setAmmount(int newAmmount) {
        ammount = newAmmount;
    }

    public void setDate(Date newDate) {
        date = newDate;
    }

    public void setUser(BikeShopMember newUser) {
        user = newUser;
    }

    public void setMember(BikeShopMember newMember) {
        member = newMember;
    }

    public void setComments(String newComments) {
        comments = newComments;
    }
}
