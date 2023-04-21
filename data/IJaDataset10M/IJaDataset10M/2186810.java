package FWPckg2;

public class Account {

    public static final int ref2 = 12;

    int ref = 11;

    String acctid = new String("acctid");

    public Account() {
    }

    public int getRef() {
        return ref;
    }

    public String getAcctId() {
        return acctid;
    }

    public void printConstant() {
        System.out.println(ref2);
    }
}
