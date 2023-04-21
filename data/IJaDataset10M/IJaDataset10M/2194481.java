package org.ru.mse10.cvis.entity.system;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.ru.mse10.cvis.entity.misc.Identity;

@Entity
@Table(name = "system_defaults")
public class SystemDefaults extends Identity {

    private String defaultLanguage;

    private String defaultDatePattern;

    private String defaultNameDisplaySequence;

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getDefaultDatePattern() {
        return defaultDatePattern;
    }

    public void setDefaultDatePattern(String defaultDatePattern) {
        this.defaultDatePattern = defaultDatePattern;
    }

    public String getDefaultNameDisplaySequence() {
        return defaultNameDisplaySequence;
    }

    public void setDefaultNameDisplaySequence(String defaultNameDisplaySequence) {
        this.defaultNameDisplaySequence = defaultNameDisplaySequence;
    }
}
