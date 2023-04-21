package com.doculibre.intelligid.wicket.panels.mesdossiers.commun.menus;

import wicket.AttributeModifier;
import wicket.Page;
import wicket.PageParameters;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.model.IModel;
import wicket.model.LoadableDetachableModel;
import wicket.model.Model;
import com.doculibre.intelligid.entites.FicheDossier;
import com.doculibre.intelligid.entites.FicheMetadonnees;
import com.doculibre.intelligid.utils.FGDSpringUtils;
import com.doculibre.intelligid.wicket.pages.mesdossiers.document.ConsulterDocumentPage;
import com.doculibre.intelligid.wicket.pages.mesdossiers.dossier.ConsulterDossierPage;

@SuppressWarnings("serial")
public class LienPermanentLink extends AjaxLink {

    public LienPermanentLink(String id, FicheMetadonnees fiche) {
        super(id);
        final Class<? extends Page> pageClass;
        if (fiche instanceof FicheDossier) {
            pageClass = ConsulterDossierPage.class;
        } else {
            pageClass = ConsulterDocumentPage.class;
        }
        final Long idFiche = fiche.getId();
        final IModel lienPermanentModel = new LoadableDetachableModel() {

            @Override
            protected Object load() {
                String baseUrl = "http://" + FGDSpringUtils.getServerHost() + ":" + FGDSpringUtils.getServerPort();
                StringBuffer url = new StringBuffer(baseUrl);
                url.append(urlFor(pageClass, new PageParameters("id=" + idFiche)));
                return url;
            }
        };
        final IModel jsCopyToClipboardModel = new LoadableDetachableModel() {

            @Override
            protected Object load() {
                CharSequence lienPermanent = (CharSequence) lienPermanentModel.getObject(null);
                return "window.clipboardData.setData('Text','" + lienPermanent + "');";
            }
        };
        add(new AttributeModifier("onclick", new Model()) {

            @Override
            protected String newValue(String currentValue, String replacementValue) {
                String ajaxCall = currentValue;
                String jsCopyToClipboard = (String) jsCopyToClipboardModel.getObject(null);
                return jsCopyToClipboard + "; alert('Le lien vers la fiche a été copié dans le presse-papier.');" + ajaxCall;
            }
        });
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
    }
}
