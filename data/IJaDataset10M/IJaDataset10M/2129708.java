package org.jabusuite.cms.news.employee;

import org.jabusuite.cms.news.*;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.jabusuite.core.language.JbsLangObject;
import org.jabusuite.core.language.JbsLanguage;

/**
 * Stores a longText for a employeeNewsEntry
 * @author hilwers
 */
@Entity
public class EmployeeNewsText extends JbsLangObject implements Serializable {

    private String headline;

    private String longText;

    private EmployeeNewsEntry employeeNewsEntry;

    public EmployeeNewsText() {
        super(null);
    }

    public EmployeeNewsText(JbsLanguage language) {
        super(language);
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    @Column(columnDefinition = "text")
    public String getLongText() {
        return longText;
    }

    public void setLongText(String text) {
        this.longText = text;
    }

    @ManyToOne
    public EmployeeNewsEntry getEmployeeNewsEntry() {
        return employeeNewsEntry;
    }

    public void setEmployeeNewsEntry(EmployeeNewsEntry newsEntry) {
        this.employeeNewsEntry = newsEntry;
    }
}
