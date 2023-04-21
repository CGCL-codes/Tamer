package fr.ign.cogit.appli.geopensim.agent.macro;

import java.util.List;
import org.apache.log4j.Logger;
import fr.ign.cogit.appli.geopensim.agent.AgentGeographique;

/**
 * @author Florence Curie
 *
 */
public class EtatGlobal {

    static Logger logger = Logger.getLogger(EtatGlobal.class.getName());

    static int staticId = 1;

    /**
	 * Constructeur d'un état global.
	 */
    public EtatGlobal() {
        super();
        this.setId(staticId++);
    }

    protected int id;

    /**
	 * @return id identifiant de l'état global
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param Id identifiant de l'état global
	 */
    public void setId(int Id) {
        id = Id;
    }

    protected String nom;

    /**
	 * @return nom le nom de l'état global
	 */
    public String getNom() {
        return this.nom;
    }

    /**
	 * @param nom e nom de l'état global
	 */
    public void setNom(String nom) {
        this.nom = nom;
    }

    protected boolean simule;

    /**
	 * @return simule (true : si l'état est simulé et false : si l'état existe)  
	 */
    public boolean isSimule() {
        return this.simule;
    }

    /**
	 * @param simule (true : si l'état est simulé et false : si l'état existe)
	 */
    public void setSimule(boolean simule) {
        this.simule = simule;
    }

    protected int date;

    /**
	 * @return date la date de cet état global
	 */
    public int getDate() {
        return this.date;
    }

    /**
	 * @param date la date de cet état global
	 */
    public void setDate(int date) {
        this.date = date;
    }

    protected EtatGlobal etatPrecedent;

    /**
	 * @return etatPrecedent l'état global précédent cet état global
	 */
    public EtatGlobal getEtatPrecedent() {
        return this.etatPrecedent;
    }

    /**
	 * @param etatPrecedent l'état global précédent cet état global
	 */
    public void setEtatPrecedent(EtatGlobal etatPrecedent) {
        this.etatPrecedent = etatPrecedent;
    }

    protected List<AgentGeographique> collection;

    /**
	 * @return populations les populations liées à cet état global
	 */
    public List<AgentGeographique> getCollection() {
        return this.collection;
    }

    /**
	 * @param populations les populations liées à cet état global
	 */
    public void setCollection(List<AgentGeographique> collection) {
        this.collection = collection;
    }
}
