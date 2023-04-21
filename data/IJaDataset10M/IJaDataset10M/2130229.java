package donnees;

import java.util.ArrayList;

/**
 * Classe Graphe<br/>
 * <b>Graphe est une classe faite pour r�aliser la recherche du plus court et de
 * plus rapide chemin (dans l'algo de djikstra @see Djisktra).</b>
 * <p>
 * Graphe prend des points et des vecteurs pour construire un graphe . Elle va
 * donc pouvoir �tre le miroir de la carte du GPS.
 * @see PointGPS
 * @see Vecteur
 * @see DonneesGPS
 * 
 * @author groupe2
 * @version 1.0
 * </p>
 */
public class Graphe {

    /**
	 * m-LVecteur est une liste de VecteurGraphe. Cet attribut m�morisera tous les routes de
	 * la carte.
	 */
    ArrayList<VecteurGraphe> m_LVecteur = new ArrayList<VecteurGraphe>();

    /**
	 * m_lPoints est une liste de point.Cet attribut m�morisera tous les points rencontr�s.
	 */
    ArrayList<PointGPS> m_lPoints = new ArrayList<PointGPS>();

    /**
	 * Constructeur de Graphe qui prend aucun parametre.
	 */
    public Graphe() {
    }

    /**
	 * Constructeur de la classe Graphe qui prend trois param�tres:
	 * @param entrant: le point d'entr�e, point de d�part pour calculer l'itin�raire.
	 * @param sortant: le point de sortie, point d'arriv�e pour calculer l'itin�raire.
	 * @param mode: le mode de recherche de l'itin�raire, soit l'option choisi.
	 */
    public Graphe(PointGPS entrant, PointGPS sortant, int mode) {
        m_lPoints.add(entrant);
        for (PointGPS point : DonneesGPS.getL_Points()) {
            if ((entrant.getX() != point.getX() || entrant.getY() != point.getY()) && ((sortant.getX() != point.getX() || sortant.getY() != point.getY()))) {
                m_lPoints.add(point);
            }
        }
        m_lPoints.add(sortant);
        for (Vecteur vecteur : DonneesGPS.getL_Vecteurs()) {
            if (mode == 2) {
                m_LVecteur.add(new VecteurGraphe(vecteur.getDepart(), vecteur.getArrive(), vecteur.getVitesse()));
                if (vecteur.isSens()) {
                    m_LVecteur.add(new VecteurGraphe(vecteur.getArrive(), vecteur.getDepart(), vecteur.getVitesse()));
                }
            } else if (mode == 1) {
                if (vecteur.getVitesse() != 0) {
                    m_LVecteur.add(new VecteurGraphe(vecteur.getDepart(), vecteur.getArrive(), vecteur.getVitesse()));
                    if (vecteur.isSens()) {
                        m_LVecteur.add(new VecteurGraphe(vecteur.getArrive(), vecteur.getDepart(), vecteur.getVitesse()));
                    }
                }
            }
        }
    }

    /**
	 * Une Methode pour savoir si une route existe.
	 * @param depart: le point de d�part.
	 * @param arrive: le point d'arriv�e.
	 * @return un boolean pour dire si le vecteur existe ou pas.
	 */
    public boolean vecExist(PointGPS depart, PointGPS arrive) {
        for (VecteurGraphe item : this.m_LVecteur) {
            if (item.getPointDepart().equals(depart) && item.getPointArrive().equals(arrive)) return true;
        }
        return false;
    }

    /**
	 * Accesseur de la liste de vecteur graphe.
	 * @return m_LVecteur: la liste de vecteur graphe.
	 */
    public ArrayList<VecteurGraphe> getM_LVecteur() {
        return m_LVecteur;
    }

    /**
	 * Accesseur de la liste de Point GPS.
	 * @return m_lPoints: la liste de Point GPS.
	 */
    public ArrayList<PointGPS> getM_lPoints() {
        return m_lPoints;
    }
}
