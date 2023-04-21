package org.wicketrad.samples.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.wicketrad.annotation.FieldOrder;
import org.wicketrad.jpa.propertyeditor.annotation.validation.UniqueField;
import org.wicketrad.propertyeditor.Scope;
import org.wicketrad.propertyeditor.annotation.DatePicker;
import org.wicketrad.propertyeditor.annotation.EditScope;
import org.wicketrad.propertyeditor.annotation.TextArea;
import org.wicketrad.propertyeditor.annotation.TextField;
import org.wicketrad.propertyeditor.annotation.validation.Required;
import org.wicketrad.service.Identifiable;

@Entity
@Table(name = "simpleFormBean")
public class SimpleFormBean implements Identifiable<String> {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "datePicker")
    private Date date;

    @Column(name = "textArea")
    private String textArea;

    @FieldOrder(1)
    @TextField
    @Required
    @UniqueField(beanType = SimpleFormBean.class)
    @EditScope(Scope.CREATE)
    public String getId() {
        return id;
    }

    @FieldOrder(2)
    @DatePicker
    public Date getDate() {
        return date;
    }

    @FieldOrder(3)
    @TextArea
    public String getTextArea() {
        return textArea;
    }

    public void setTextArea(String textArea) {
        this.textArea = textArea;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }
}
