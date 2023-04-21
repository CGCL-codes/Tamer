package mediathek.daten;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Datum extends Date {

    public Datum(long l) {
        super(l);
    }

    @Override
    public String toString() {
        if (this.getTime() == 0) {
            return "";
        } else {
            return new SimpleDateFormat("dd.MM.yyyy").format(this);
        }
    }

    public String toStringR() {
        if (this.getTime() == 0) {
            return new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        } else {
            return new SimpleDateFormat("yyyy.MM.dd").format(this);
        }
    }
}
