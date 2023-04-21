package org.synthful.gwt.mvp.client.fields;

import java.util.ArrayList;
import org.synthful.gwt.mvp.client.PresentationEntity;
import com.google.gwt.user.client.ui.Composite;

public class FieldSetDescriptor<FldEnt extends PresentationEntity, A> extends Composite {

    public void addFields(A[] ff) {
        for (A f : ff) {
            this.fields.add(f);
        }
    }

    public ArrayList<A> getFields() {
        return fields;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected final ArrayList<A> fields = new ArrayList<A>();

    protected String caption;

    protected String name;

    protected String description;

    protected FldEnt fieldEntity;
}
