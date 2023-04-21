package org.framework.bean.configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Bean OffreDolceVita servant � la s�rialisation et d�s�rialisation de la
 * configuration via XStream. Ce bean contient une valeur d'une Offre Dolce
 * Vita. Ce bean est contenu dans le bean Solution
 * 
 * @author Eric Reboisson
 * @since 14 sept. 06
 *        <h4>Copyright ELITOST 2006</h4>
 */
@XStreamAlias("offre-dolce-vita")
public class OffreDolceVita {

    private Long id;

    private String libelle = "";

    @XStreamAlias("fichier")
    private String fichier = "";

    @XStreamAlias("liste-offres-solutions")
    private List<OffreSolution> listeOffreSolution;

    public OffreDolceVita() {
        super();
        listeOffreSolution = new Vector<OffreSolution>();
    }

    /**
         * Champs renseign� par le module (GenProjDV) et permettant au front de
         * savoir si il doit ou non afficher cette offre dolcevita
         */
    @XStreamAlias("affiche-ihm")
    private Boolean afficheIHM;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    /**
         * Champs renseign� par le module (GenProjDV) et permettant au front de
         * savoir si il doit ou non afficher cette offre dolcevita
         * 
         * @return
         */
    public Boolean isAfficheIHM() {
        if (afficheIHM == null) return new Boolean(false); else return afficheIHM;
    }

    /**
         * Champs renseign� par le module (GenProjDV) et permettant au front de
         * savoir si il doit ou non afficher cette offre dolcevita
         * 
         * @param afficheIHM
         */
    public void setAfficheIHM(Boolean afficheIHM) {
        this.afficheIHM = afficheIHM;
    }

    public void addOffreSolution(OffreSolution offreSolution) {
        listeOffreSolution.add(offreSolution);
    }

    public List<OffreSolution> getListeOffreSolution() {
        return listeOffreSolution;
    }

    /**
         * M�thode renvoyant la liste des id Solution d'une offre dolce vita.
         * Dans un hashset ce qui permet de v�rifier la pr�sence d'une solution
         * dans l'offre dolce vita facilement
         * 
         * @return
         */
    public Set<Long> getHashIdSolutionOffreDolceVita() {
        Set<Long> hashIdSolution = new HashSet<Long>();
        for (OffreSolution os : listeOffreSolution) {
            hashIdSolution.add(os.getIdSolution());
        }
        return hashIdSolution;
    }

    /**
         * M�thode renvoyant la liste des id Solution d'une offre dolce vita
         * selectionn�e par le module DV. Dans un hashset ce qui permet de
         * v�rifier la pr�sence d'une solution selectionnee dans l'offre dolce
         * vita facilement
         * 
         * @return
         */
    public Set<Long> getHashIdSolutionSelectionneFiltreDV() {
        Set<Long> hashIdSolution = new HashSet<Long>();
        for (OffreSolution os : listeOffreSolution) {
            if (os.isSelectionneeDV()) {
                hashIdSolution.add(os.getIdSolution());
            }
        }
        return hashIdSolution;
    }
}
