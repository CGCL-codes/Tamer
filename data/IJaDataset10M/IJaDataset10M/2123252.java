package cz.expenses.commons.data;

public class Currency extends AbstractValue implements Comparable<Currency> {

    private final String iso;

    public Currency(final String iso) {
        super();
        this.iso = iso;
    }

    public Currency(final Id id, final String iso) {
        super(id);
        this.iso = iso;
    }

    public String getIso() {
        return iso;
    }

    @Override
    public String toGuiString() {
        return iso;
    }

    @Override
    public int compareTo(final Currency o) {
        return getIso().compareTo(o.getIso());
    }

    @Override
    public String toString() {
        return iso + "(" + getId().toString() + ")";
    }
}
