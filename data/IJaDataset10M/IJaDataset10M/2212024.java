package fr.aston.gestionconges.services;

import fr.aston.gestionconges.metiers.DemandeConges;

/**
 * D�finition du contrat de service pour la Getion des Demandes de cong�
 * @author Wissem
 *
 */
public interface IServiceDemandeConges {

    public void enregistrerDemandeConges(DemandeConges demande);

    public DemandeConges consulterDemandeConges(int idDemande);
}
