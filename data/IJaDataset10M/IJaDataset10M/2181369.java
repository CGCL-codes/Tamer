package com.doculibre.intelligid.wicket.panels.mesdossiers.commun.sections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import wicket.Component;
import wicket.markup.html.basic.Label;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.list.PropertyListView;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;
import com.doculibre.intelligid.delegate.FGDDelegate;
import com.doculibre.intelligid.entites.FicheDocument;
import com.doculibre.intelligid.entites.FicheDossier;
import com.doculibre.intelligid.entites.FicheMetadonnees;
import com.doculibre.intelligid.entites.ddv.RoleIFGD;
import com.doculibre.intelligid.entites.profil.MetadonneeProfilMO;
import com.doculibre.intelligid.entites.profil.ProfilSaisieMO;
import com.doculibre.intelligid.entites.profil.constantes.MetadonneesFiche;
import com.doculibre.intelligid.wicket.components.protocol.http.FGDWebSession;
import com.doculibre.intelligid.wicket.panels.BaseFGDPanel;
import com.doculibre.intelligid.wicket.panels.mesdossiers.commun.champs.consultation.ChampsMetadonneeConsultationFactory;

/**
 * @author Francis Baril
 * Classe abstraite représentant une section de la page FormulaireCourantPanel
 */
@SuppressWarnings("serial")
public abstract class AbstractConsultationPanel extends BaseFGDPanel {

    protected Panel panelConteneur;

    public AbstractConsultationPanel(String id, FicheMetadonnees fiche, Panel panelConteneur) {
        super(id);
        this.panelConteneur = panelConteneur;
        this.add(getListeComposantes(fiche));
    }

    /**
	 * Fournit la liste de métadonné selon le type de fiche
	 */
    private List<MetadonneeProfilMO> getMetadonneesAffichee(FicheMetadonnees fiche, ProfilSaisieMO profil) {
        List<MetadonneeProfilMO> metadonnees = null;
        ProfilSaisieMO profilMO = new FGDDelegate().getProfilSaisieMO();
        if (fiche instanceof FicheDocument && ((FicheDocument) fiche).isCourriel()) {
            metadonnees = profilMO.getMetadonneesDocumentCourrielLecture(getUtilisateurCourant(), fiche);
        } else if (fiche instanceof FicheDocument && ((FicheDocument) fiche).isTransaction()) {
            metadonnees = profilMO.getMetadonneesDocumentTransactionLecture(getUtilisateurCourant(), fiche);
        } else if (fiche instanceof FicheDossier) {
            metadonnees = profilMO.getMetadonneesDossierLecture(getUtilisateurCourant(), fiche);
        }
        List<MetadonneeProfilMO> metadonneesAffichees = new ArrayList<MetadonneeProfilMO>();
        for (MetadonneeProfilMO metadonnee : metadonnees) {
            RoleIFGD roleRequis = metadonnee.getRoleRequis();
            RoleIFGD roleUtilisateur = FGDWebSession.get().getUtilisateurCourant().getRole();
            if (roleRequis == null || roleUtilisateur.compareTo(roleRequis) >= 0 || fiche.getMetadonnee(metadonnee) != null) {
                metadonneesAffichees.add(metadonnee);
            }
        }
        return metadonneesAffichees;
    }

    /**
	 * Cette méthode retourne une composante ListeView responsable de |affichage des métadonnées.
	 */
    protected ListView getListeComposantes(final FicheMetadonnees fiche) {
        final Model composantesListModel = new Model() {

            @Override
            public Object getObject(Component component) {
                return getMetadonneesAffichee(fiche, new FGDDelegate().getProfilSaisieMO());
            }
        };
        ListView composantesList = new PropertyListView("composantesList", composantesListModel) {

            @Override
            protected void populateItem(final ListItem item) {
                MetadonneeProfilMO metadonnee = (MetadonneeProfilMO) item.getModelObject();
                item.add(new Label("label", metadonnee.getLibelle()));
                Component component = ChampsMetadonneeConsultationFactory.instancierComposante("component", metadonnee, fiche, panelConteneur, getGroupes());
                item.add(component == null ? new Label("component", "") : component);
                boolean valeurNonNulle = !fiche.isMetadonneeEmpty(metadonnee);
                boolean isTitre = metadonnee.getNomPropriete().equals(MetadonneesFiche.TITRE);
                item.setVisible(valeurNonNulle && component != null && component.isVisible() && !isTitre);
            }
        };
        return composantesList;
    }

    /**
	 * @return le groupe des métadonnées acceptées pour cette section
	 */
    protected abstract List<String> getGroupes();

    /**
	 * @return Le compteur indiquant l'alternance des couleurs (null = pas d'alternance)
	 */
    protected abstract BooleanModifiable getCompteurAlternanceCouleur();

    @SuppressWarnings("unchecked")
    @Override
    public boolean isVisible() {
        boolean visible;
        boolean visibleHerite = super.isVisible();
        if (visibleHerite) {
            ListView composantesList = (ListView) get("composantesList");
            boolean itemVisible = false;
            Iterator<ListItem> it = composantesList.iterator();
            if (it.hasNext()) {
                while (!itemVisible && it.hasNext()) {
                    ListItem item = it.next();
                    itemVisible = item.isVisible();
                }
                visible = itemVisible;
            } else {
                visible = true;
            }
        } else {
            visible = false;
        }
        return visible;
    }
}
