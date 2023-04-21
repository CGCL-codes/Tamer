package com.doculibre.intelligid.wicket.models;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import wicket.Session;
import wicket.model.IModel;
import wicket.model.LoadableDetachableModel;
import wicket.model.Model;

/**
 * Ce modèle permet de faciliter l'affichage de l'âge d'un élément. Il calcul donc la différence de
 * temps entre le moment fournit et le moment actuel pour formuler une expression du genre : 50
 * minutes, Hier, Plus de 2 mois
 * 
 * Attention : une technique approximative est utilisée pour calculer le nombre de mois. Il peut y
 * avoir une erreur de l'ordre d'une journée ou deux.
 * 
 * @author Francis Baril
 * 
 */
@SuppressWarnings("serial")
public class DateDifferenceTextModel extends LoadableDetachableModel {

    IModel modelDate;

    IModel modelMomentActuel;

    IModel modelLocale;

    private static final double AVERAGE_MILLIS_PER_MONTH = 365.24 * 24 * 60 * 60 * 1000 / 12;

    private static final int MINUTE = 1000 * 60;

    private static final int HEURE = MINUTE * 60;

    private static final int JOURNEE = HEURE * 24;

    private static final int SEMAINE = JOURNEE * 7;

    public DateDifferenceTextModel(IModel modelDate) {
        this.modelDate = modelDate;
        this.modelMomentActuel = new LoadableDetachableModel() {

            @Override
            protected Object load() {
                return Calendar.getInstance();
            }
        };
        this.modelLocale = new LoadableDetachableModel() {

            @Override
            protected Object load() {
                return Session.get().getLocale();
            }
        };
    }

    /**
	 * Seulement pour tester
	 * 
	 * @deprecated
	 * @param modelDate
	 * @param momentActuel
	 */
    public DateDifferenceTextModel(Calendar momentActuel, Locale locale, Calendar momentDate) {
        this.modelDate = new Model(momentDate);
        this.modelMomentActuel = new Model(momentActuel);
        this.modelLocale = new Model(locale);
    }

    @Override
    protected Object load() {
        Calendar momentActuel = (Calendar) modelMomentActuel.getObject(null);
        Object objMomentDate = modelDate.getObject(null);
        Calendar momentDate;
        if (objMomentDate instanceof Calendar) {
            momentDate = (Calendar) objMomentDate;
        } else if (objMomentDate instanceof Date) {
            momentDate = Calendar.getInstance();
            momentDate.setTime((Date) objMomentDate);
        } else {
            throw new RuntimeException("Le type de date fourni n'est pas géré : " + objMomentDate.getClass().getSimpleName());
        }
        Locale locale = (Locale) modelLocale.getObject(null);
        if (!locale.equals(Locale.CANADA_FRENCH)) {
            throw new RuntimeException("Fonctionne seulement avec une Locale CANADA_FRENCH");
        }
        Long distance = momentActuel.getTime().getTime() - momentDate.getTime().getTime();
        String texte = null;
        if (distance < MINUTE) {
            texte = "Moins d'une minute";
        } else if (distance < HEURE) {
            if (distance < 2 * MINUTE) {
                texte = "Une minute";
            } else {
                texte = ((int) (distance / MINUTE)) + " minutes";
            }
        } else if (distance < JOURNEE) {
            int nb = ((int) (distance / HEURE));
            texte = nb + " heure" + (nb > 1 ? "s" : "");
        } else if (distance < SEMAINE) {
            int nb = ((int) (distance / JOURNEE));
            if (nb == 1) {
                texte = "Hier";
            } else {
                texte = nb + " jours";
            }
        } else {
            Calendar momentDateClone = (Calendar) momentDate.clone();
            momentDateClone.add(Calendar.MONTH, 1);
            if (distance < AVERAGE_MILLIS_PER_MONTH) {
                int nb = ((int) (distance / SEMAINE));
                texte = nb + " semaine" + (nb > 1 ? "s" : "");
            } else {
                int nb = ((int) (distance / AVERAGE_MILLIS_PER_MONTH));
                texte = nb + " mois";
            }
        }
        return texte;
    }
}
