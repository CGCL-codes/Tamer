package vydavky.client.objects;

import vydavky.client.ciselniky.TypObjektu;
import vydavky.client.utils.ClientUtils;

/**
 * Zakladny value objekt - clovek.
 */
public class ClovekValue extends BaseValue {

    /** Krstne meno cloveka. */
    private String meno = null;

    /** Priezvisko cloveka. */
    private String priezvisko = null;

    public String getMeno() {
        return meno;
    }

    public void setMeno(final String meno) {
        this.meno = meno;
    }

    public String getPriezvisko() {
        return priezvisko;
    }

    public void setPriezvisko(final String priezvisko) {
        this.priezvisko = priezvisko;
    }

    public String getCeleMeno() {
        return (this.meno == null ? "" : (this.meno + " ")) + (this.priezvisko == null ? "" : this.priezvisko);
    }

    @Override
    public boolean equals(final Object o) {
        return (o instanceof ClovekValue && this.getId().equals(((ClovekValue) o).getId()));
    }

    @Override
    public boolean deepEquals(final Object o) {
        if (!(o instanceof ClovekValue)) {
            return false;
        }
        final ClovekValue other = (ClovekValue) o;
        final boolean zhodneMeno = ClientUtils.zhodne(this.meno, other.getMeno());
        final boolean zhodnePriezvisko = ClientUtils.zhodne(this.priezvisko, other.getPriezvisko());
        return zhodneMeno && zhodnePriezvisko;
    }

    @Override
    public TypObjektu getTypObjektu() {
        return TypObjektu.CLOVEK;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.meno != null ? this.meno.hashCode() : 0);
        hash = 13 * hash + (this.priezvisko != null ? this.priezvisko.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Clovek: meno=" + this.getCeleMeno();
    }
}
