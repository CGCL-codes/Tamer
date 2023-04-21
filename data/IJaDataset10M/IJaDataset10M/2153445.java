package cz.expenses.android.storage;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import cz.expenses.android.commons.data.Currency;
import cz.expenses.android.commons.data.Id;

@Root(name = "currencies")
public class CurrencySave extends AbstractValueSave {

    @Element
    private String iso;

    public CurrencySave() {
        super();
    }

    public CurrencySave(final String iso) {
        super();
        this.iso = iso;
    }

    public CurrencySave(final String id, final String iso) {
        super(id);
        this.iso = iso;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(final String iso) {
        this.iso = iso;
    }

    public CurrencySave(final Currency c) {
        this(c.getId().getText(), c.getIso());
    }

    public Currency getValue() {
        return new Currency(new Id(getId()), getIso());
    }
}
