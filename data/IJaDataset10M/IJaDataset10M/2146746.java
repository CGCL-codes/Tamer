package com.doculibre.intelligid.wicket.panels.lookup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import wicket.model.IModel;
import wicket.model.LoadableDetachableModel;
import wicket.util.convert.SimpleConverterAdapter;
import com.doculibre.intelligid.delegate.FGDDelegate;
import com.doculibre.intelligid.entites.PlanClassification;
import com.doculibre.intelligid.entites.ProcessusActivite;
import com.doculibre.intelligid.entites.UtilisateurIFGD;
import com.doculibre.intelligid.index.helpers.impl.PlanClassificationIndexHelper;
import com.doculibre.intelligid.utils.FGDSpringUtils;
import com.doculibre.intelligid.utils.comparators.ProcessusActiviteComparator;
import com.doculibre.intelligid.utils.filtres.IFiltreProcessusActivite;
import com.doculibre.intelligid.wicket.components.form.lookup.LookupTreeElement;
import com.doculibre.intelligid.wicket.components.form.lookup.modal.AbstractLookupAutoCompleteSearcher;
import com.doculibre.intelligid.wicket.components.form.lookup.modal.ILookupAutoCompleteSearcher;
import com.doculibre.intelligid.wicket.components.form.lookup.modal.ILookupAutoCompleteTextProvider;
import com.doculibre.intelligid.wicket.components.form.lookup.modal.LookupFieldPanel;
import com.doculibre.intelligid.wicket.components.protocol.http.FGDWebSession;
import com.doculibre.intelligid.wicket.models.RechargeableModel;

@SuppressWarnings("serial")
public class LookupProcessusActiviteField extends LookupFieldPanel {

    public static final int WIDTH = 750;

    public LookupProcessusActiviteField(String id, IModel model, SimpleConverterAdapter converter, boolean reglesApprouveesSeulement, int premierNiveauSelectionnable, boolean inclureInactifs) {
        super(id, model, loadTreeElements(reglesApprouveesSeulement, premierNiveauSelectionnable), newAutoCompleteSearcher(reglesApprouveesSeulement, premierNiveauSelectionnable, inclureInactifs), newAutoCompleteTextProvider(converter), converter);
    }

    public LookupProcessusActiviteField(String id, IModel model, boolean reglesApprouveesSeulement, int premierNiveauSelectionnable, boolean inclureInactifs) {
        this(id, model, DEFAULT_CONVERTER, reglesApprouveesSeulement, premierNiveauSelectionnable, inclureInactifs);
    }

    private static IModel loadTreeElements(final boolean reglesApprouveesSeulement, final int premierNiveauSelectionnable) {
        return new LoadableDetachableModel() {

            @Override
            protected Object load() {
                List<LookupTreeElement> treeElements = new ArrayList<LookupTreeElement>();
                FGDDelegate delegate = new FGDDelegate();
                PlanClassification planClassification = delegate.getPlanClassification();
                List<ProcessusActivite> processusActivites = new ArrayList<ProcessusActivite>(planClassification.getProcessusActivitesActifs());
                int niveauCourant = 0;
                int i = 0;
                Collections.sort(processusActivites, new ProcessusActiviteComparator());
                IFiltreProcessusActivite filtre = FGDSpringUtils.getFiltreProcessusActivite();
                UtilisateurIFGD utilisateurCourant = FGDWebSession.get().getUtilisateurCourant();
                for (Iterator<ProcessusActivite> it = processusActivites.iterator(); it.hasNext(); ) {
                    ProcessusActivite processusActivite = it.next();
                    if (Boolean.TRUE.equals(processusActivite.getInactif()) || (filtre != null && !filtre.isVisible(processusActivite, utilisateurCourant))) {
                        it.remove();
                    } else {
                        LookupTreeElement treeElement = toLookupTreeElement(processusActivite, niveauCourant, i++, reglesApprouveesSeulement, premierNiveauSelectionnable);
                        if (treeElement != null) {
                            treeElements.add(treeElement);
                        }
                    }
                }
                return treeElements;
            }
        };
    }

    private static ILookupAutoCompleteSearcher<ProcessusActivite> newAutoCompleteSearcher(final boolean reglesApprouveesSeulement, final int premierNiveauSelectionnable, final boolean inclureInactifs) {
        return new AbstractLookupAutoCompleteSearcher<ProcessusActivite>() {

            @Override
            public List<ProcessusActivite> suggest(String input) {
                PlanClassificationIndexHelper indexHelper = FGDSpringUtils.getPlanClassificationIndexHelper();
                List<ProcessusActivite> resultats = new ArrayList<ProcessusActivite>();
                IFiltreProcessusActivite filtre = FGDSpringUtils.getFiltreProcessusActivite();
                UtilisateurIFGD utilisateurCourant = FGDWebSession.get().getUtilisateurCourant();
                for (ProcessusActivite processusActivite : indexHelper.searchCodeTitre(input, inclureInactifs)) {
                    boolean selectionnable = processusActivite != null && processusActivite.getNiveau() >= premierNiveauSelectionnable && (!reglesApprouveesSeulement || processusActivite.hasRegleConservationApprouvee(false)) && (filtre == null || filtre.isVisible(processusActivite, utilisateurCourant));
                    if (selectionnable) {
                        resultats.add(processusActivite);
                    }
                }
                return resultats;
            }

            @Override
            public List<ProcessusActivite> search(String input) {
                PlanClassificationIndexHelper indexHelper = FGDSpringUtils.getPlanClassificationIndexHelper();
                List<ProcessusActivite> resultats = new ArrayList<ProcessusActivite>();
                IFiltreProcessusActivite filtre = FGDSpringUtils.getFiltreProcessusActivite();
                UtilisateurIFGD utilisateurCourant = FGDWebSession.get().getUtilisateurCourant();
                for (ProcessusActivite processusActivite : indexHelper.searchCodeTitreDescription(input, inclureInactifs)) {
                    boolean selectionnable = processusActivite != null && processusActivite.getNiveau() >= premierNiveauSelectionnable && (!reglesApprouveesSeulement || processusActivite.hasRegleConservationApprouvee(false)) && (filtre == null || filtre.isVisible(processusActivite, utilisateurCourant));
                    if (selectionnable) {
                        resultats.add(processusActivite);
                    }
                }
                return resultats;
            }
        };
    }

    private static ILookupAutoCompleteTextProvider newAutoCompleteTextProvider(final SimpleConverterAdapter converter) {
        return new ILookupAutoCompleteTextProvider() {

            @Override
            public String getTextValue(Object object) {
                String textValue = "";
                if (object != null) {
                    ProcessusActivite processusActivite = (ProcessusActivite) object;
                    if (converter == DEFAULT_CONVERTER || converter.equals(DEFAULT_CONVERTER)) {
                        textValue = processusActivite.getCode() + " - " + processusActivite.getTitre();
                    } else {
                        textValue = converter.toString(object);
                    }
                }
                return textValue;
            }

            @Override
            public String getDescription(Object object) {
                String textValue = "";
                if (object != null) {
                    ProcessusActivite processusActivite = (ProcessusActivite) object;
                    if (converter == DEFAULT_CONVERTER || converter.equals(DEFAULT_CONVERTER)) {
                        textValue = processusActivite.getDescription();
                    } else {
                        textValue = converter.toString(object);
                    }
                }
                return textValue;
            }
        };
    }

    @Override
    protected boolean isSortTreeElements() {
        return false;
    }

    private static LookupTreeElement toLookupTreeElement(ProcessusActivite processusActivite, int niveauCourant, int index, boolean reglesApprouveesSeulement, int premierNiveauSelectionnable) {
        LookupTreeElement treeElement;
        String code = processusActivite.getCode();
        String descriptionCourte = processusActivite.getCode() + " - " + processusActivite.getTitre();
        String descriptionLongue = processusActivite.getDescriptionAbregee() != null && processusActivite.getDescriptionAbregee().length() > 0 ? processusActivite.getDescriptionAbregee() : processusActivite.getDescription();
        descriptionLongue = StringUtils.replace(descriptionLongue, "\r", " ");
        boolean selectionnable = niveauCourant >= premierNiveauSelectionnable && (!reglesApprouveesSeulement || processusActivite.hasRegleConservationApprouvee(false));
        if (!reglesApprouveesSeulement || processusActivite.hasRegleConservationApprouvee(true)) {
            treeElement = new LookupTreeElement(code, descriptionCourte, descriptionLongue, selectionnable, null, false, new RechargeableModel(processusActivite));
            treeElement.setIndex(index);
            List<ProcessusActivite> sousProcessusActivites = new ArrayList<ProcessusActivite>(processusActivite.getSousElements());
            int i = 0;
            Collections.sort(sousProcessusActivites, new ProcessusActiviteComparator());
            for (Iterator<ProcessusActivite> it = sousProcessusActivites.iterator(); it.hasNext(); ) {
                ProcessusActivite sousProcessusActivite = it.next();
                if (Boolean.TRUE.equals(processusActivite.getInactif())) {
                    it.remove();
                } else {
                    LookupTreeElement sousTreeElement = toLookupTreeElement(sousProcessusActivite, niveauCourant + 1, i++, reglesApprouveesSeulement, premierNiveauSelectionnable);
                    if (sousTreeElement != null && !Boolean.TRUE.equals(sousProcessusActivite.getInactif())) {
                        treeElement.getSousElements().add(sousTreeElement);
                    }
                }
            }
        } else {
            treeElement = null;
        }
        return treeElement;
    }

    @Override
    protected String getLookupWindowTitle() {
        return "Assistant de recherche d'une rubrique du plan de classification";
    }
}
