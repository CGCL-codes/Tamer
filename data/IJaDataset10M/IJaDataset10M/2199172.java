package com.doculibre.intelligid.wicket.panels.recherche.resultats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import wicket.Component;
import wicket.Page;
import wicket.PageParameters;
import wicket.ResourceReference;
import wicket.ajax.AjaxRequestTarget;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.form.ChoiceRenderer;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.link.IPageLink;
import wicket.markup.html.link.Link;
import wicket.markup.html.link.PageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.model.LoadableDetachableModel;
import wicket.model.Model;
import com.doculibre.intelligid.delegate.FGDDelegate;
import com.doculibre.intelligid.entites.FicheDocument;
import com.doculibre.intelligid.entites.FicheDossier;
import com.doculibre.intelligid.entites.FicheMetadonnees;
import com.doculibre.intelligid.entites.FicheMetadonnees.TypeFiche;
import com.doculibre.intelligid.entites.profil.MetadonneeProfilMO;
import com.doculibre.intelligid.entites.profil.ProfilSaisieMO;
import com.doculibre.intelligid.recherche.criteres.CritereRechercheFicheMetadonnees;
import com.doculibre.intelligid.recherche.criteres.CriteresRechercheFicheMetadonnees;
import com.doculibre.intelligid.wicket.components.protocol.http.FGDWebSession;
import com.doculibre.intelligid.wicket.models.ResultatsRechercheVisiblesModel;
import com.doculibre.intelligid.wicket.pages.mesdossiers.document.ConsulterDocumentPage;
import com.doculibre.intelligid.wicket.pages.mesdossiers.dossier.ConsulterDossierPage;
import com.doculibre.intelligid.wicket.panels.mesdossiers.dossier.icone.IconeDossierLinkPanel;
import com.doculibre.intelligid.wicket.utils.IconeFicheUtils;
import com.doculibre.wicket.list.PageChangeAwarePageableListView;
import com.doculibre.wicket.panels.ImagePanel;
import com.doculibre.wicket.panels.crud.BaseCRUDTableColumn;
import com.doculibre.wicket.panels.crud.ICRUDTableColumn;
import com.doculibre.wicket.panels.crud.PageableCRUDPanel;
import com.doculibre.wicket.panels.crud.SelectionCRUDTableColumn;
import com.doculibre.wicket.util.ContextPathResourceReference;

/**
 * @author Vincent Cormier
 *
 * Classe permettant d'afficher le résultat d'une recherche simple.
 * 
 */
@SuppressWarnings("serial")
public abstract class ResultatsRecherchePanel extends PageableCRUDPanel {

    private CriteresRechercheFicheMetadonnees criteres;

    private IModel resultatsParPageModel;

    private IModel selectedResultsModel;

    public static final Integer[] RESULTATS_PAR_PAGE_POSSIBLES = { 10, 20, 50, 100 };

    public ResultatsRecherchePanel(String id, CriteresRechercheFicheMetadonnees criteresP, final IModel selectedResultsModel, final ResultatsRechercheVisiblesModel itemsModel, IModel resultatsParPageModel) {
        super(id, itemsModel, itemsModel.getResultatsParPage());
        this.criteres = criteresP;
        this.selectedResultsModel = selectedResultsModel;
        this.resultatsParPageModel = resultatsParPageModel;
        DropDownChoice resultatsRechercheParPageField = new DropDownChoice("resultatsParPage", resultatsParPageModel, Arrays.asList(RESULTATS_PAR_PAGE_POSSIBLES)) {

            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        };
        add(resultatsRechercheParPageField);
        final IModel triOptionsModel = new LoadableDetachableModel() {

            @Override
            protected Object load() {
                boolean rechercheDossier = false;
                boolean rechercheDocument = false;
                for (CritereRechercheFicheMetadonnees critere : criteres.getCriteres()) {
                    String typeCritere = critere.getTypeCritere();
                    if (CritereRechercheFicheMetadonnees.TYPE_CRITERE_DOSSIER.equals(typeCritere)) {
                        rechercheDossier = true;
                    } else if (CritereRechercheFicheMetadonnees.TYPE_CRITERE_DOCUMENT_TRANSACTION.equals(typeCritere) || CritereRechercheFicheMetadonnees.TYPE_CRITERE_DOCUMENT_COURRIEL.equals(typeCritere)) {
                        rechercheDocument = true;
                    }
                    if (rechercheDossier && rechercheDocument) {
                        break;
                    }
                }
                FGDDelegate delegate = new FGDDelegate();
                ProfilSaisieMO profilMO = delegate.getProfilSaisieMO();
                List<CritereRechercheFicheMetadonnees> options = new ArrayList<CritereRechercheFicheMetadonnees>();
                if (rechercheDossier) {
                    for (MetadonneeProfilMO metadonnee : profilMO.getMetadonnees(TypeFiche.DOSSIER)) {
                        CritereRechercheFicheMetadonnees critere = new CritereRechercheFicheMetadonnees();
                        critere.setTypeCritere(CritereRechercheFicheMetadonnees.TYPE_CRITERE_DOSSIER);
                        critere.setMetadonnee(metadonnee);
                        options.add(critere);
                    }
                }
                if (rechercheDocument) {
                    Set<String> nomsMetadonnees = new HashSet<String>();
                    for (MetadonneeProfilMO metadonnee : profilMO.getMetadonnees(TypeFiche.COURRIEL)) {
                        if (!nomsMetadonnees.contains(metadonnee.getNomPropriete())) {
                            CritereRechercheFicheMetadonnees critere = new CritereRechercheFicheMetadonnees();
                            critere.setTypeCritere(CritereRechercheFicheMetadonnees.TYPE_CRITERE_DOCUMENT_COURRIEL);
                            critere.setMetadonnee(metadonnee);
                            options.add(critere);
                            nomsMetadonnees.add(metadonnee.getNomPropriete());
                        }
                    }
                    for (MetadonneeProfilMO metadonnee : profilMO.getMetadonnees(TypeFiche.DOCUMENT)) {
                        if (!nomsMetadonnees.contains(metadonnee.getNomPropriete())) {
                            CritereRechercheFicheMetadonnees critere = new CritereRechercheFicheMetadonnees();
                            critere.setTypeCritere(CritereRechercheFicheMetadonnees.TYPE_CRITERE_DOCUMENT_TRANSACTION);
                            critere.setMetadonnee(metadonnee);
                            options.add(critere);
                            nomsMetadonnees.add(metadonnee.getNomPropriete());
                        }
                    }
                }
                if (!options.isEmpty()) {
                    CritereRechercheFicheMetadonnees critereAucunTri = new CritereRechercheFicheMetadonnees();
                    critereAucunTri.setTypeCritere(CritereRechercheFicheMetadonnees.TRI_AUCUN);
                    options.add(0, critereAucunTri);
                }
                return options;
            }
        };
        WebMarkupContainer triContainer = new WebMarkupContainer("triContainer") {

            @Override
            public boolean isVisible() {
                return false;
            }
        };
        IChoiceRenderer triRenderer = new ChoiceRenderer() {

            @Override
            public Object getDisplayValue(Object object) {
                String libelle;
                CritereRechercheFicheMetadonnees critere = (CritereRechercheFicheMetadonnees) object;
                if (critere.getTypeCritere().equals(CritereRechercheFicheMetadonnees.TRI_AUCUN)) {
                    libelle = "Aucun tri";
                } else {
                    if (critere.getTypeCritere().equals(CritereRechercheFicheMetadonnees.TYPE_CRITERE_DOSSIER)) {
                        libelle = "Dossier - ";
                    } else {
                        libelle = "Document - ";
                    }
                    libelle += critere.getMetadonnee().getLibelle();
                }
                return libelle;
            }
        };
        DropDownChoice triField = new DropDownChoice("triField", new Model(), triOptionsModel, triRenderer) {

            @Override
            protected void onSelectionChanged(Object newSelection) {
                CritereRechercheFicheMetadonnees critereTri = (CritereRechercheFicheMetadonnees) newSelection;
                if (CritereRechercheFicheMetadonnees.TRI_AUCUN.equals(critereTri.getTypeCritere())) {
                    criteres.setCritereTri(null);
                } else {
                    criteres.setCritereTri(critereTri);
                }
            }

            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        };
        triContainer.add(triField);
        add(new ResultatsRechercheReportsPanel("rapports", selectedResultsModel));
    }

    @Override
    protected ICRUDTableColumn[] getTableColumns() {
        ICRUDTableColumn selectionColumn = new SelectionCRUDTableColumn() {

            @Override
            protected IModel getSelectedListModel() {
                return selectedResultsModel;
            }

            @Override
            protected IModel getItemsListModel() {
                return ResultatsRecherchePanel.this.getModel();
            }
        };
        ICRUDTableColumn typeColumn = new BaseCRUDTableColumn("Type", null) {

            @Override
            public WebMarkupContainer getData(String id, ListItem rowItem) {
                FicheMetadonnees fiche = (FicheMetadonnees) rowItem.getModelObject();
                if (fiche == null) {
                    rowItem.setVisible(false);
                    return new WebMarkupContainer(id);
                } else {
                    Panel imagePanel;
                    String altKey;
                    if (TypeFiche.DOSSIER.equals(fiche.getTypeFiche())) {
                        altKey = "Dossier";
                        FicheDossier ficheDossier = (FicheDossier) fiche;
                        imagePanel = new IconeDossierLinkPanel(id, ficheDossier, altKey, false) {

                            @Override
                            protected void onLinkClicked(AjaxRequestTarget target) {
                                ListItem columnItem = (ListItem) findParent(ListItem.class);
                                ListItem rowItem = (ListItem) columnItem.findParent(ListItem.class);
                                SectionPrincipaleResultatRecherchePanel sectionPrincipaleResultatRecherchePanel = (SectionPrincipaleResultatRecherchePanel) rowItem.visitChildren(SectionPrincipaleResultatRecherchePanel.class, new IVisitor() {

                                    @Override
                                    public Object component(Component component) {
                                        return component;
                                    }
                                });
                                String visibility = isOuvert() ? "inline" : "none";
                                String markupId = sectionPrincipaleResultatRecherchePanel.getContenuDossierPanel().getMarkupId();
                                target.appendJavascript("document.getElementById('" + markupId + "').style.display='" + visibility + "'");
                            }
                        };
                    } else {
                        String imagePath;
                        if (TypeFiche.COURRIEL.equals(fiche.getTypeFiche())) {
                            altKey = "Courriel";
                            imagePath = IconeFicheUtils.getPathIconeCourriel();
                        } else {
                            altKey = "Document";
                            FicheDocument ficheDocument = (FicheDocument) fiche;
                            imagePath = IconeFicheUtils.getPathIcone(ficheDocument);
                        }
                        ResourceReference imageReference = new ContextPathResourceReference(imagePath);
                        imagePanel = new ImagePanel(id, imageReference, altKey);
                    }
                    return imagePanel;
                }
            }

            @Override
            public String getStyleClass() {
                return "typeColumn";
            }
        };
        ICRUDTableColumn descriptionColumn = new BaseCRUDTableColumn("Description", null) {

            @Override
            public WebMarkupContainer getData(String id, ListItem rowItem) {
                FicheMetadonnees fiche = (FicheMetadonnees) rowItem.getModelObject();
                if (fiche == null) {
                    rowItem.setVisible(false);
                    return new WebMarkupContainer(id);
                } else {
                    return new SectionPrincipaleResultatRecherchePanel(id, fiche);
                }
            }
        };
        ICRUDTableColumn detailsColumn = new BaseCRUDTableColumn("", null) {

            @Override
            public WebMarkupContainer getData(String id, ListItem rowItem) {
                FicheMetadonnees fiche = (FicheMetadonnees) rowItem.getModelObject();
                if (fiche == null) {
                    rowItem.setVisible(false);
                    return new WebMarkupContainer(id);
                } else {
                    return new SectionSecondaireResultatRecherchePanel(id, fiche);
                }
            }

            @Override
            public String getStyleClass() {
                return "detailsColumn";
            }
        };
        return new ICRUDTableColumn[] { selectionColumn, typeColumn, descriptionColumn, detailsColumn };
    }

    @Override
    protected boolean isDetailsPossible() {
        return false;
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
    protected Link getDetailsLink(String id, Object itemObject) {
        FicheMetadonnees fiche = (FicheMetadonnees) itemObject;
        final Long idFiche = fiche.getId();
        Link consulterFicheLink;
        if (fiche instanceof FicheDossier) {
            consulterFicheLink = new PageLink("link", new IPageLink() {

                public Page getPage() {
                    return new ConsulterDossierPage(new PageParameters("id=" + idFiche));
                }

                @SuppressWarnings("rawtypes")
                public Class getPageIdentity() {
                    return ConsulterDossierPage.class;
                }
            });
        } else {
            consulterFicheLink = new PageLink("link", new IPageLink() {

                public Page getPage() {
                    FGDWebSession.get().setPageRetourConsultationDocument(ResultatsRecherchePanel.this.getPage());
                    return new ConsulterDocumentPage(new PageParameters("id=" + idFiche));
                }

                @SuppressWarnings("rawtypes")
                public Class getPageIdentity() {
                    return ConsulterDocumentPage.class;
                }
            });
        }
        return consulterFicheLink;
    }

    @Override
    protected ListView newListView(String id, IModel itemsModel, final ListViewPopulator listViewPopulator) {
        final ResultatsRechercheVisiblesModel resultatsRechercheVisiblesModel = (ResultatsRechercheVisiblesModel) itemsModel;
        return new PageChangeAwarePageableListView(id, itemsModel, resultatsRechercheVisiblesModel.getResultatsParPage()) {

            @Override
            protected void populateItem(ListItem item) {
                listViewPopulator.populateItem(item);
            }

            @Override
            protected void onPageChange(int oldPage, int newPage) {
                resultatsRechercheVisiblesModel.chargerPage(newPage + 1);
            }
        }.setReuseItems(true);
    }

    @Override
    protected Link getModifyLink(String id, final Object itemObject) {
        return null;
    }

    @Override
    protected Link getDeleteLink(String id, final Object itemObject) {
        return null;
    }

    public CriteresRechercheFicheMetadonnees getCriteres() {
        return criteres;
    }

    @Override
    public void detachModels() {
        if (selectedResultsModel != null) {
            selectedResultsModel.detach();
        }
        if (resultatsParPageModel != null) {
            resultatsParPageModel.detach();
        }
        super.detachModels();
    }
}
