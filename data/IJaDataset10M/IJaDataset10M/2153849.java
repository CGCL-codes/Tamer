package com.servepdf.dto;

import java.util.ArrayList;
import java.util.List;
import com.servepdf.dto.table.Table;
import com.thoughtworks.xstream.XStream;

/**
 * Content specifies data that is applied to a template document and may result in one or more PDF pages.
 * 
 * @author ernestmicklei
 *
 */
public class Content {

    public String formURL;

    private List<TextField> fields;

    private List<Table> tables;

    public List<TextField> getFields() {
        if (fields == null) fields = new ArrayList<TextField>();
        return fields;
    }

    public List<Table> getTables() {
        if (tables == null) tables = new ArrayList<Table>();
        return tables;
    }

    public void validate() throws ValidationException {
        ValidationException.throwUnlessURL(formURL, "formUrl");
        if (fields == null) return;
        for (TextField field : this.getFields()) {
            field.validate();
        }
        if (tables == null) return;
        for (Table table : this.getTables()) {
            table.validate();
        }
    }

    /**
     * Convencience methods to add a new TextField.
     * @param name
     * @param text
     */
    public void addText(String name, String text) {
        if (name == null || name.length() == 0) return;
        this.getFields().add(new TextField(name, text));
    }

    public static void setup(XStream xstream) {
        xstream.alias("content", Content.class);
        xstream.aliasField("form-url", Content.class, "formURL");
    }
}
