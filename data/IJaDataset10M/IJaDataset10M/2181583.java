package voorraadbeheer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class InkoopBestelling {

    @Id
    private long id;

    @Temporal(TemporalType.DATE)
    private Date datum;

    private Collection<InkoopItem> items = new ArrayList<InkoopItem>();

    public long getId() {
        return id;
    }

    public Date getDatum() {
        return datum;
    }

    public Collection<InkoopItem> getItems() {
        return items;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public void setItems(Collection<InkoopItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
