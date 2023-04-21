package org.wicketrad.samples.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.wicketrad.annotation.FieldOrder;
import org.wicketrad.jpa.propertyeditor.annotation.validation.UniqueField;
import org.wicketrad.propertyeditor.Scope;
import org.wicketrad.propertyeditor.annotation.EditScope;
import org.wicketrad.propertyeditor.annotation.TextField;
import org.wicketrad.propertyeditor.annotation.validation.Required;
import org.wicketrad.propertytable.annotation.BeanLinkProperty;
import org.wicketrad.propertytable.annotation.Searchable;
import org.wicketrad.propertytable.annotation.Sortable;
import org.wicketrad.samples.pages.advanced.UpdatePageToRuleThemAll;
import org.wicketrad.service.Identifiable;

@Entity
@Table(name = "countries")
public class Country implements Identifiable<String> {

    @Id
    @Column(name = "id")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "currency_name")
    private String currency;

    @Column(name = "short_currency")
    private String shortCurrency;

    @FieldOrder(1)
    @EditScope(Scope.CREATE)
    @UniqueField(beanType = Country.class)
    @Required
    @TextField
    @Sortable
    @Searchable
    @BeanLinkProperty(UpdatePageToRuleThemAll.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @FieldOrder(2)
    @TextField
    @Sortable
    @BeanLinkProperty(UpdatePageToRuleThemAll.class)
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @FieldOrder(3)
    @TextField
    @Sortable
    @BeanLinkProperty(UpdatePageToRuleThemAll.class)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @FieldOrder(4)
    @TextField
    @Sortable
    @BeanLinkProperty(UpdatePageToRuleThemAll.class)
    public String getShortCurrency() {
        return shortCurrency;
    }

    public void setShortCurrency(String shortCurrency) {
        this.shortCurrency = shortCurrency;
    }

    public String getId() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((shortCurrency == null) ? 0 : shortCurrency.hashCode());
        result = prime * result + ((shortName == null) ? 0 : shortName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Country other = (Country) obj;
        if (currency == null) {
            if (other.currency != null) return false;
        } else if (!currency.equals(other.currency)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (shortCurrency == null) {
            if (other.shortCurrency != null) return false;
        } else if (!shortCurrency.equals(other.shortCurrency)) return false;
        if (shortName == null) {
            if (other.shortName != null) return false;
        } else if (!shortName.equals(other.shortName)) return false;
        return true;
    }

    public String toString() {
        return name;
    }
}
