package alessiolab.moneyreport;

public class SuperSaveAccount extends AbstAccount {

    public SuperSaveAccount() {
        super("SuperSave");
    }

    @Override
    public boolean doesTxBelongsToMe(Transaction tx) {
        String reason = tx.getReason().toLowerCase();
        return reason.contains("supersave");
    }

    @Override
    public int getReportFactor() {
        return -1;
    }
}
