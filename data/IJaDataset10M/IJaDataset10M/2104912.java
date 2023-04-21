package org.pcorp.space.persistance.dao;

import java.util.List;
import java.util.Map;
import org.pcorp.space.metier.domaine.Capacite;
import org.pcorp.space.metier.domaine.Coordonne;
import org.pcorp.space.metier.domaine.Equipement;
import org.pcorp.space.metier.domaine.Vue;
import org.pcorp.space.metier.domaine.infovue.EquipementVue;
import org.pcorp.space.persistance.exception.PersistanceException;

public interface EquipDAO {

    /**
	 * Retourne les capactit�s disponible pour le vaisseau
	 * @param type_equipement
	 * @param id_vaiss
	 * @return
	 */
    public Map<String, Capacite> getCapaEquipement(List<Integer> type_equipement, int id_vaiss) throws PersistanceException;

    /**
	 * Retourne la liste d'�quipement du vaisseau
	 * @param vaiss_id l'identifiant du vaisseau
	 * @return la liste des �quipements install�s
	 * @throws PersistanceException
	 */
    public List<Equipement> getEquipementVx(int vaiss_id) throws PersistanceException;

    /**
	 * Retourne la liste des �quipements en soute pouvant �tre mont� sur le vaisseau
	 * @param vaiss_id l'identifiant du vaisseau
	 * @return Retourne la liste des �quipements en soute pouvant �tre mont� sur le vaisseau
	 * @throws PersistanceException
	 */
    public List<Equipement> getEquipementVxEquipable(int vaiss_id) throws PersistanceException;

    /**
	 * Permet la cr�ation de l'�l�ment �quipement pass� en parametre (avec un identifiant null/0/-1)
	 * Si l'�quipement n'existe pas dans la base il est cr�� 
	 * @param equ l'�quipement � cr�er
	 * @return l'identifiant de l'�l�ment �quipement cr��
	 * @throws PersistanceException
	 */
    public int createEquipement(Equipement equ) throws PersistanceException;

    /**
	 * Permet d'obtenir l'�l�ment equipement dont l'identifiant est pass� en parametre
	 * @param id identifiant de l'�quipement (element)
	 * @return l'equipement
	 * @throws PersistanceException
	 */
    public Equipement getEquiVaiss(int id) throws PersistanceException;

    /**
	 * Permet d'obtenir les capacit� de l'�l�ment dont l'identifiant est pass� en parametre (avec capacit� renseign�)
	 * @param id identifiant de l'�quipement (element)
	 * @return la liste des capacit�s
	 * @throws PersistanceException
	 */
    public List<Capacite> getEquiCapacite(int id_equip) throws PersistanceException;

    /**
	 * 
	 * @param id_capa
	 * @return
	 * @throws PersistanceException
	 */
    public Capacite getCapacite(int id_capa) throws PersistanceException;

    /**
	 * Permet d'obtenir l'�l�ment equipement dont l'identifiant est pass� en parametre (avec capacit� renseign�)
	 * @param id identifiant de l'�quipement (element)
	 * @return l'equipement
	 * @throws PersistanceException
	 */
    public Equipement getEquiComplet(int id_equip) throws PersistanceException;

    /**
	 * Permet d'obtenir la liste des �quipements situ� dans la vue correspondant aux informations pass� en parametre
	 * @param coord les coordonn�es du point central de la vue
	 * @param vue les distances min/max visualisable
	 * @return la liste des �quipements en vue
	 * @throws PersistanceException
	 */
    public List<EquipementVue> getEquipementInView(Coordonne coord, Vue vue) throws PersistanceException;

    /**
	 * Charge l'�quipement s�lectionn� dans la soute. Si l'�quipement n'est pas charg� en entier un nouvel �l�ment sera cr�� avec une quantite �gale � la diff�rence.
	 * @param soute l'identifiant de la soute
	 * @param ele_id l'identifiant de l'�l�ment � charger
	 * @param quantite la quantite de l'�l�ment � charger
	 * @return l'equipement charg�
	 * @throws PersistanceException
	 */
    public Equipement chargeEquipement(int soute, int ele_id, int quantite) throws PersistanceException;

    /**
	 * Equipe l'equipement dans le vaisseau
	 * @param equip identifiant de l'equipement
	 * @param id_vaiss identifiant du vaisseau
	 * @throws PersistanceException
	 */
    public void equipe(int equip, int id_vaiss) throws PersistanceException;

    /**
	 * retire l'�quipement du vaisseau et le place dans la soute
	 * @param equip identifiant de l'�quipement
	 * @param id_vaiss identifiant du vaisseau
	 * @param id_soute identifiant de la soute
	 * @throws PersistanceException
	 */
    public void desequipe(int equip, int id_vaiss, int id_soute) throws PersistanceException;

    /**
	 * largue l'�quipement au coordonn� transmise. Si la quantite largu� est inf�rieure � la quantit� totale un nouvel �l�ment sera cr�� en soute
	 * @param equip identifiant de l'�quipement � larguer
	 * @param coord coordonn� du largage
	 * @param quantite quantite � larguer
	 * @throws PersistanceException
	 */
    public int largue(int equip, Coordonne coord, int quantite) throws PersistanceException;

    /**
	 * fusionne les �quipements entre-eux. Ne fonctionne que pour les marchandises
	 * @param liste_equipement liste des identifiants des �l�ments � fusionner
	 * @throws PersistanceException
	 */
    public void fusionne(List<Integer> liste_equipement) throws PersistanceException;

    /**
	 * met � jour les �quipements dans la base (quantite, etat, position, etc...)
	 * @param liste liste des �quipements � mettre � jour
	 * @throws PersistanceException
	 */
    public void majEquipement(List<Equipement> liste) throws PersistanceException;

    /**
	 * supprime l'�quipement s�lectionn�
	 * @param equ l'�quipement � supprimer
	 * @throws PersistanceException
	 */
    public void deleteEquipement(Equipement equ) throws PersistanceException;

    /**
	 * supprime la liste des �quipements s�lectionn�s
	 * @param liste_equ la liste des �quipements � supprimer
	 * @throws PersistanceException
	 */
    public void deleteEquipement(List<Equipement> liste_equ) throws PersistanceException;

    /**
	 * 
	 */
    public void transfertEquipement(int id_source, int id_destination, int equipement, int quantite) throws PersistanceException;
}
