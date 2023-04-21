package com.doculibre.intelligid.wicket.pages.pilotage.domainevaleurs.element.typesupport;

import wicket.Page;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.Radio;
import wicket.markup.html.form.RadioGroup;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.PropertyModel;
import com.doculibre.intelligid.entites.ddv.TypeSupport;
import com.doculibre.intelligid.wicket.pages.pilotage.domainevaleurs.element.AddElementFormPage;

@SuppressWarnings("serial")
public class AddTypeSupportFormPage extends AddElementFormPage {

    private IModel domaineValeursModel;

    public AddTypeSupportFormPage(IModel domaineValeursModel) {
        super(domaineValeursModel, null);
        this.domaineValeursModel = domaineValeursModel;
        initComponents();
    }

    private void initComponents() {
        Form form = (Form) get(FORM_ID);
        TypeSupport typeSupport = (TypeSupport) form.getModelObject();
        RadioGroup supportInformatiqueRadioGroup = new RadioGroup("supportInformatique", new PropertyModel(typeSupport, "supportInformatique"));
        form.add(supportInformatiqueRadioGroup);
        supportInformatiqueRadioGroup.add(new Radio("supportInformatiqueOui", new Model(Boolean.TRUE)));
        supportInformatiqueRadioGroup.add(new Radio("supportInformatiqueNon", new Model(Boolean.FALSE)));
    }

    @Override
    protected Page getResponseAction() {
        return new TypesSupportsListPage(domaineValeursModel);
    }
}
