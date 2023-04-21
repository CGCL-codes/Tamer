package com.pjsofts.eurobudget.report;

/**
 * Report
 * Default ones (TO DO):
 * # DEPENSES
 *1 Repartition (/ctg)
 *1 B�n�ficiaire (/entity)
 *1 Flux d'argent mensuel (revenues+beneficiaires)
 *1 Par Cat�gories (2eme niveau)
 *1 D�penses/Revenues
 *3 Budget(objectif, plafond, gestion,...)
 * # AVOIRS
 *3 Situation Patrimonial ( � terme)
 *2 Soldes de comptes
 *1 Historiques des soldes
 *# DETTES
 *A venir
 *Debit, credit du mois
 *Debit, credit du mois sur CB
 *Pret
 *# PLACEMENTS
 * Interets,
 * Cours
 * # IMPOTS
 * Transactions inclut ds ref fiscal de l'ann�e.
 * # CUSTOM SAVED
 * # FAVORITES
 * @author  Standard
 */
public interface Report {

    /** @return ReportData */
    public ReportData getData();
}
