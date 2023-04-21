package de.powerstaff.business.entity;

import de.mogwai.common.business.entity.AuditableEntity;
import de.mogwai.common.utils.StringPresentationProvider;

public class ContactType extends AuditableEntity implements StringPresentationProvider {

    private static final long serialVersionUID = 8034787319984772308L;

    private String description;

    private boolean phone;

    private boolean fax;

    private boolean email;

    private boolean web;

    private boolean gulp;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isFax() {
        return fax;
    }

    public void setFax(boolean fax) {
        this.fax = fax;
    }

    public boolean isPhone() {
        return phone;
    }

    public void setPhone(boolean phone) {
        this.phone = phone;
    }

    public boolean isWeb() {
        return web;
    }

    public void setWeb(boolean web) {
        this.web = web;
    }

    public String getStringPresentation() {
        return description;
    }

    /**
	 * @return the gulp
	 */
    public boolean isGulp() {
        return gulp;
    }

    /**
	 * @param gulp
	 *            the gulp to set
	 */
    public void setGulp(boolean gulp) {
        this.gulp = gulp;
    }
}
