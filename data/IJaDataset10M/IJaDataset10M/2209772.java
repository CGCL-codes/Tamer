package com.doculibre.intelligid.wicket.panels.mesdossiers.commun.champs.saisie.tabbedPanels;

import java.util.List;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.extensions.markup.html.tabs.TabbedPanel;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.model.IModel;
import wicket.model.LoadableDetachableModel;
import com.doculibre.intelligid.delegate.FGDDelegate;
import com.doculibre.intelligid.entites.FicheDossier;
import com.doculibre.intelligid.entites.FicheMetadonnees;
import com.doculibre.intelligid.entites.profil.MetadonneeProfilMO;
import com.doculibre.intelligid.entites.profil.ProfilSaisieMO;
import com.doculibre.intelligid.entrepot.conversation.ConversationManager;
import com.doculibre.intelligid.utils.FGDSpringUtils;
import com.doculibre.intelligid.wicket.pages.mesdossiers.dossier.ConsulterDossierPage;
import com.doculibre.intelligid.wicket.validators.FicheMetadonneesValidator;

@SuppressWarnings("serial")
public class ChampsMetadonneesDossierTabbedPanel extends ChampsMetadonneesTabbedPanel {

    @Override
    public Button getSaveButton(String id) {
        Button actionButton = new Button(id) {

            @Override
            protected void onSubmit() {
                FicheDossier ficheDossier = (FicheDossier) fiche;
                FGDDelegate delegate = new FGDDelegate();
                ProfilSaisieMO profilMO = delegate.getProfilSaisieMO();
                if (ficheDossier.getUniteAdministrativeProprietaire() == null && ficheDossier.getRelationEstPartieDe() != null) {
                    ficheDossier.setUniteAdministrativeProprietaire(ficheDossier.getRelationEstPartieDe().getUniteAdministrativeProprietaire());
                }
                FicheMetadonneesValidator validator = new FicheMetadonneesValidator();
                List<String> messagesErreurs = validator.validate(fiche, profilMO.getMetadonneesDossierEcriture(getUtilisateurCourant(), fiche));
                if (messagesErreurs.isEmpty()) {
                    ficheDossier.setFicheCompletee(true);
                    delegate.sauvegarder(ficheDossier, getUtilisateurCourant());
                    ConversationManager conversationManager = FGDSpringUtils.getConversationManager();
                    conversationManager.commitTransaction();
                    RequestCycle.get().setResponsePage(ConsulterDossierPage.class, new PageParameters("id=" + fiche.getId()));
                } else {
                    for (String messageErreur : messagesErreurs) {
                        error(messageErreur);
                    }
                    TabbedPanel tabbedPanel = (TabbedPanel) this.getForm().get("tabs");
                    tabbedPanel.setSelectedTab(0);
                }
            }

            @Override
            public String getMarkupId() {
                return "saveButton";
            }
        };
        actionButton.setDefaultFormProcessing(true);
        return actionButton;
    }

    public ChampsMetadonneesDossierTabbedPanel(String id, boolean isObligatoires, FicheMetadonnees fiche, Form form) {
        super(id, isObligatoires, fiche, form);
    }

    @Override
    protected List<MetadonneeProfilMO> getMetadonneesEcriture(FicheMetadonnees fiche) {
        return new FGDDelegate().getProfilSaisieMO().getMetadonneesDossierEcriture(getUtilisateurCourant(), (FicheDossier) fiche);
    }

    @Override
    protected IModel getTitleModel() {
        return new LoadableDetachableModel() {

            @Override
            protected Object load() {
                String titre;
                if (fiche.getId() != null) {
                    titre = "Mettre à jour une fiche dossier";
                } else {
                    titre = "Créer un dossier";
                }
                return titre;
            }
        };
    }
}
