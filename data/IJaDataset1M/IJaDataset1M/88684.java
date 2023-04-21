package org.squabble.domain.property;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.squabble.domain.Account;
import org.squabble.domain.AccountProperty;

@Entity(name = "accountBooleanProperty")
@DiscriminatorValue("B")
public class AccountBooleanProperty extends AccountProperty {

    private static final long serialVersionUID = 1L;

    public AccountBooleanProperty() {
        super();
    }

    public AccountBooleanProperty(Account account, String name, Boolean value) {
        super.setName(name);
        super.setAccount(account);
        this.setObject(value);
    }

    @Column(name = "BOOLEAN_VALUE")
    protected Boolean value = null;

    public boolean isStorable(Object value) {
        if (value == null) return true;
        return (Boolean.class.isAssignableFrom(value.getClass()));
    }

    public Object getObject() {
        return value;
    }

    public void setObject(Object value) {
        this.value = (Boolean) value;
    }
}
