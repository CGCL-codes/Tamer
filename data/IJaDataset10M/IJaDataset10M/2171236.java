package ch.unibe.id.se.a3ublogin.persistence.localaccounts.quickp;

public interface IStoreObject {

    public long getSyncTime();

    public void setSyncTime(long time);

    public void setId(String id);

    public String getId();

    public void setValue(Object value);

    public Object getValue();
}
