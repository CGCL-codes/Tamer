package com.doculibre.intelligid.wicket.panels.mesdossiers.commun.champs.saisie;

import wicket.Component;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.IModel;
import com.doculibre.intelligid.delegate.FGDDelegate;
import com.doculibre.intelligid.entites.FicheMetadonnees;
import com.doculibre.intelligid.entites.UtilisateurIFGD;
import com.doculibre.intelligid.entites.profil.MetadonneeProfilMO;
import com.doculibre.intelligid.entites.profil.ProfilSaisieMO;
import com.doculibre.intelligid.wicket.panels.lookup.LookupUtilisateurField;

@SuppressWarnings("serial")
public class ChampLookupUtilisateurPanel extends ChampLookup2Panel {

    public ChampLookupUtilisateurPanel(String id, MetadonneeProfilMO metadonnee, ProfilSaisieMO profilMO, FicheMetadonnees fiche) {
        super(id, metadonnee, profilMO, fiche);
    }

    @Override
    protected WebMarkupContainer getField(String id, final MetadonneeProfilMO metadonnee, FicheMetadonnees fiche) {
        LookupUtilisateurField champ = new LookupUtilisateurField(id, null, false) {

            @Override
            protected String getLookupWindowTitle() {
                return getPopupLinkTitle(metadonnee);
            }
        };
        champ.setNullValid(metadonnee.isFacultative());
        return champ;
    }

    protected String getPopupLinkTitle(MetadonneeProfilMO metadonnee) {
        return "Assistant de recherche d'utilisateur";
    }

    @Override
    protected IModel getFieldModel(MetadonneeProfilMO metadonnee, FicheMetadonnees fiche) {
        final IModel baseModel = super.getFieldModel(metadonnee, fiche);
        return new IModel() {

            @Override
            public void detach() {
                baseModel.detach();
            }

            @Override
            public IModel getNestedModel() {
                return baseModel;
            }

            @Override
            public Object getObject(Component arg0) {
                String nomUtilisateur = (String) baseModel.getObject(arg0);
                return nomUtilisateur == null ? null : new FGDDelegate().getUtilisateur(nomUtilisateur);
            }

            @Override
            public void setObject(Component comp, Object val) {
                UtilisateurIFGD utilisateur = (UtilisateurIFGD) val;
                if (utilisateur != null || (comp != null && comp.getId().equals("deleteContentLink"))) {
                    String nomUtilisateur = utilisateur == null ? null : utilisateur.getNomUtilisateur();
                    baseModel.setObject(comp, nomUtilisateur);
                }
            }
        };
    }
}
