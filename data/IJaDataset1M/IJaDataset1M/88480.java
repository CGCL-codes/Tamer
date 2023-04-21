package com.c2b2.ipoint.presentation.forms;

import com.c2b2.ipoint.model.Content;
import com.c2b2.ipoint.model.Group;
import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.model.Portlet;
import com.c2b2.ipoint.presentation.forms.fieldtypes.StringField;
import java.util.List;
import com.c2b2.ipoint.presentation.PresentationException;
import com.c2b2.ipoint.presentation.forms.fieldtypes.DateField;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateContent extends SimpleForm {

    private static final String VALID_FROM_FIELD = "ValidFrom";

    private static final String VALID_TO_FIELD = "ValidTo";

    public CreateContent() {
    }

    protected String getFormButtonLabel() {
        return "Create";
    }

    protected List getFormFields() {
        if (myFields == null) {
            myFields = new FieldMap();
            myFields.add(new StringField("Name", "Name", "", true, 5));
            myFields.add(new StringField("Title", "Title", "", true, 5));
            myFields.add(new DateField(VALID_FROM_FIELD, "Publish From", new Date()));
            GregorianCalendar expiry = new GregorianCalendar(3000, 1, 1);
            myFields.add(new DateField(VALID_TO_FIELD, "Publish Until", expiry.getTime()));
        }
        return myFields.getFields();
    }

    protected void handlePost() throws PresentationException {
        try {
            String name = myFields.getValue("Name");
            String title = myFields.getValue("Title");
            Date validFrom = (Date) myFields.getField(VALID_FROM_FIELD).getNativeValue();
            Date validTo = (Date) myFields.getField(VALID_TO_FIELD).getNativeValue();
            String contentType = "Content";
            if (myParent != null) {
                contentType = "ChildContent";
            }
            Content content = Content.createContent(name, title, "Please Edit...", myPR.getCurrentUser(), null, contentType, true, myParent);
            content.getPublishedVersion().setValidFrom(validFrom);
            content.getPublishedVersion().setValidTo(validTo);
            setAttribute("NewContent", content);
            setAttribute("NewContentID", Long.toString(content.getID()));
            if (myParent != null) {
                content.clonePermissions(myParent);
            } else {
                content.setEditable(Group.getAdminGroup());
                content.setViewable(Group.getAdminGroup());
            }
        } catch (PersistentModelException e) {
            throw new PresentationException("Unable to create content", e);
        }
    }

    public boolean canRender() throws PresentationException {
        boolean result = false;
        try {
            String parentContent = (String) getAttribute("ParentContent");
            if (parentContent != null) {
                myParent = Content.getContent(parentContent);
                if (myParent.isEditableBy(myPR.getCurrentUser())) {
                    result = true;
                }
            } else if (myPR.getCurrentUser().isInGroup(Group.getRegisteredGroup())) {
                result = true;
            }
        } catch (PersistentModelException e) {
            throw new PresentationException("Unable to determine whether the form should be displayed", e);
        }
        return result;
    }

    private FieldMap myFields;

    private Content myParent;
}
