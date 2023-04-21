package er.changenotification;

public interface ERCNSubscriberDelegate {

    public boolean processInsertions(ERCNSnapshot ercnSnapshot);

    public boolean processUpdates(ERCNSnapshot ercnSnapshot);

    public boolean processDeletions(ERCNSnapshot ercnSnapshot);
}
