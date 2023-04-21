package com.genia.toolbox.web.gwt.form.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.genia.toolbox.web.gwt.basics.client.i18n.GwtI18nMessage;
import com.genia.toolbox.web.gwt.basics.client.oracle.SimpleSuggestion;
import com.genia.toolbox.web.gwt.form.client.form.Form;
import com.genia.toolbox.web.gwt.form.client.form.impl.TextBoxItemImpl;
import com.genia.toolbox.web.gwt.form.client.form.impl.VerticalFormImpl;
import com.genia.toolbox.web.gwt.form.client.value.DataIdentifier;
import com.genia.toolbox.web.gwt.form.client.value.FormValues;
import com.genia.toolbox.web.gwt.form.client.value.impl.FormSimpleValueImpl;
import com.genia.toolbox.web.gwt.form.client.value.impl.FormValuesImpl;
import com.genia.toolbox.web.gwt.form.server.provider.FormProvider;

public class Form2 implements FormProvider {

    private static Form form = null;

    public static final List<FormValues> formValues = new ArrayList<FormValues>();

    public String getDisplayString(final String fieldName, final String value) {
        return null;
    }

    public Form getForm(DataIdentifier dataIdentifier) {
        if (form == null) {
            final VerticalFormImpl vForm = new VerticalFormImpl();
            final TextBoxItemImpl tBox = new TextBoxItemImpl();
            tBox.setName("name");
            tBox.setLabelKey(new GwtI18nMessage("Name"));
            vForm.addForm(tBox);
            form = vForm;
        }
        return form;
    }

    public String getFormIdentifier() {
        return "form2";
    }

    public FormValues getInitialValue(final DataIdentifier dataIdentifier) {
        if (dataIdentifier.getIdentifier() == null) {
            FormValuesImpl defaultValues = new FormValuesImpl();
            defaultValues.setFormIdentifier(getFormIdentifier());
            FormSimpleValueImpl fv;
            fv = new FormSimpleValueImpl();
            fv.setValue(null);
            fv.setName("name");
            defaultValues.addFormSimpleValue(fv);
            defaultValues.setDataIdentifier(dataIdentifier);
            return defaultValues;
        }
        final Integer index = Integer.valueOf(dataIdentifier.getIdentifier());
        return formValues.get(index);
    }

    public List<SimpleSuggestion> getSuggestions(DataIdentifier dataIdentifier, final String fieldName, final String query, final int limit) {
        return null;
    }

    public FormValues saveForm(final FormValues newValues) {
        if (newValues.getDataIdentifier().getIdentifier() == null) {
            formValues.add(newValues);
            newValues.getDataIdentifier().setIdentifier(formValues.size() - 1);
        } else {
            formValues.set(Integer.parseInt(newValues.getDataIdentifier().getIdentifier()), newValues);
        }
        return newValues;
    }
}
