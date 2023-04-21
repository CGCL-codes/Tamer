package com.doculibre.intelligid.wicket.panels.pilotage.planclassification;

import wicket.Page;
import wicket.RequestCycle;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.link.Link;
import wicket.markup.html.link.PageLink;
import wicket.markup.html.list.ListItem;
import wicket.model.IModel;
import com.doculibre.intelligid.delegate.FGDDelegate;
import com.doculibre.intelligid.entites.Delai;
import com.doculibre.intelligid.entites.PlanClassification;
import com.doculibre.intelligid.entites.ProcessusActivite;
import com.doculibre.intelligid.entites.RegleConservation;
import com.doculibre.intelligid.entrepot.conversation.ConversationManager;
import com.doculibre.intelligid.utils.FGDSpringUtils;
import com.doculibre.intelligid.wicket.pages.pilotage.calendrierconservation.DelaiCalendrierConservationFormPage;
import com.doculibre.intelligid.wicket.pages.pilotage.planclassification.AddRegleConservationFormPage;
import com.doculibre.wicket.panels.crud.BaseCRUDTableColumn;
import com.doculibre.wicket.panels.crud.CRUDPanel;
import com.doculibre.wicket.panels.crud.ICRUDTableColumn;
import com.doculibre.wicket.panels.filariane.cheminement.CheminementModel;
import com.doculibre.wicket.panels.filariane.cheminement.Passage;

/**
 * @author Francis Baril
 * 
 * Panel permettant d'afficher les délais de conservations dans la page AddRegleConservationFormPage
 */
@SuppressWarnings("serial")
public class SelectionDelaisPanel extends CRUDPanel {

    CheminementModel cheminement;

    Passage passageActuel;

    public SelectionDelaisPanel(String id, IModel itemsModel, CheminementModel cheminement, String criterion) {
        super(id, itemsModel);
        this.cheminement = cheminement;
        this.passageActuel = cheminement.getDernierPassage();
        getCriterionModel().setObject(null, criterion);
    }

    @Override
    protected ICRUDTableColumn[] getTableColumns() {
        ICRUDTableColumn numeroColumn = new BaseCRUDTableColumn("Numéro", "numero");
        ICRUDTableColumn titreSerieColumn = new BaseCRUDTableColumn("Titre de la série", "titreSerie");
        ICRUDTableColumn descriptionColumn = new BaseCRUDTableColumn("Description", "description");
        ICRUDTableColumn chooseColumn = new BaseCRUDTableColumn("", null) {

            @Override
            public WebMarkupContainer getData(String id, final ListItem rowItem) {
                Link linkSelection = new PageLink(SelectionPanel.COMPONENT_ID, DelaiCalendrierConservationFormPage.class) {

                    @Override
                    public void onClick() {
                        PlanClassification plan = new FGDDelegate().getPlanClassification();
                        ProcessusActivite processus = plan.getProcessusActivite(((ProcessusActivite) cheminement.getDernierPassage().getElement()).getCode());
                        Delai delai = (Delai) rowItem.getModelObject();
                        RegleConservation regle = new RegleConservation();
                        regle.setDelai(delai);
                        regle.setProcessusActivite(processus);
                        processus.getReglesConservation().add(regle);
                        FGDDelegate delegate = new FGDDelegate();
                        delegate.sauvegarder(processus);
                        ConversationManager conversationManager = FGDSpringUtils.getConversationManager();
                        conversationManager.commitTransaction();
                        RequestCycle.get().setResponsePage(cheminement.getDernierPassage().getPageRetour().getPage());
                    }
                };
                return new SelectionPanel(id, "ui/images/commun/list-add.gif", linkSelection);
            }
        };
        return new ICRUDTableColumn[] { numeroColumn, titreSerieColumn, descriptionColumn, chooseColumn };
    }

    @Override
    protected boolean inclureChampRecherche() {
        return true;
    }

    @Override
    protected Page actualiserPage() {
        return new AddRegleConservationFormPage(cheminement, (String) criterionModel.getObject(null));
    }

    @Override
    protected boolean isDeletePossible() {
        return false;
    }

    @Override
    protected Link getAddLink(String id) {
        return null;
    }

    @Override
    protected Link getDeleteLink(String id, final Object itemObject) {
        return null;
    }

    @Override
    protected Link getDetailsLink(String id, Object itemObject) {
        return null;
    }

    @Override
    protected Link getModifyLink(String id, Object itemObject) {
        return null;
    }
}
