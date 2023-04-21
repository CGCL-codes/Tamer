package org.geonetwork.web.ebrim.struts.form;

import org.apache.struts.validator.ValidatorForm;

public class SearchForm extends ValidatorForm {

    private String title;

    private String abstractz;

    private String classificationId;

    public String getAbstract() {
        return abstractz;
    }

    public void setAbstract(String abstractz) {
        this.abstractz = abstractz;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }
}
