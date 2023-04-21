package datascheme;

import java.util.Date;

public class Dienstgrad {

    private Integer id;

    private Date Datum;

    public Dienstgrad(Integer Id, Date Date) {
        id = Id;
        Datum = Date;
    }

    public Integer getId() {
        return id;
    }

    public void setName(Integer id) {
        this.id = id;
    }

    public Date getDatum() {
        return Datum;
    }

    public void setDatum(Date datum) {
        Datum = datum;
    }
}
