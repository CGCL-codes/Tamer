package pojos;

import java.io.Serializable;

/**
 *
 * @author Kinich
 */
public class Typ implements Serializable {

    private Long typnr;

    private String typ;

    public Typ() {
    }

    public boolean equals(Object o) {
        if (o instanceof Typ) {
            Typ typ = (Typ) o;
            if (typ.getTypnr() == typnr) {
                return true;
            }
        }
        return false;
    }

    public Long getTypnr() {
        return typnr;
    }

    public void setTypnr(Long typnr) {
        this.typnr = typnr;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
