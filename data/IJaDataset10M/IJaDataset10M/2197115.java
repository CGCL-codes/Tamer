package mapshared;

import java.io.Serializable;

/**
 *
 * @author Cody Stoutenburg
 */
public class IOID implements Serializable {

    private Integer _id;

    public IOID() {
        this._id = -1;
    }

    public IOID(Integer id) {
        this._id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IOID other = (IOID) obj;
        if (this._id != other._id && (this._id == null || !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this._id != null ? this._id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return _id.toString();
    }

    public static IOID valueOf(String str) {
        return new IOID(Integer.valueOf(str));
    }

    public static IOID valueOf(Integer id) {
        return new IOID(id);
    }
}
