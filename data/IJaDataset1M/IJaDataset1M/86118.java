package com.doculibre.intelligid.wicket.pages.pilotage.domainevaleurs.element.masquesaisie;

import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.link.PageLink;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.PropertyModel;
import com.doculibre.intelligid.entites.ddv.MasqueSaisieLocalisation;
import com.doculibre.intelligid.wicket.pages.pilotage.domainevaleurs.element.EditElementFormPage;

@SuppressWarnings("serial")
public class EditMasqueSaisieLocalisationFormPage extends EditElementFormPage {

    public EditMasqueSaisieLocalisationFormPage(IModel elementModel, boolean dernierePageEstConsultation) {
        super(elementModel, dernierePageEstConsultation);
        initComponents();
    }

    private void initComponents() {
        Form form = getForm();
        TextField masqueSaisieField = new TextField("masqueSaisie", new PropertyModel((MasqueSaisieLocalisation) getElementModel().getObject(null), "regexp"));
        masqueSaisieField.setLabel(new Model("Expression régulière"));
        form.add(masqueSaisieField);
        form.add(new PageLink("aideLink", AideMasqueSaisie.class) {

            @Override
            public void onClick() {
                setResponsePage(new AideMasqueSaisie(this.getPage()));
            }
        });
    }
}
