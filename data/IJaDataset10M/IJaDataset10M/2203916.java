package org.framework.bean.module;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Bean XStream contenant la liste des param�tres pour l'appel d'un module
 * donn�e. et une m�thode permettant de tout r�cup�rer dans une hashtable. Un
 * �l�ment de la hashtable r�sultat est constitu� de :
 * <ul>
 * <li>en cl� : le nom du param�tre</li>
 * <li>en valeur : la liste de valeurs correspondant � ce param�tres sous forme
 * d'une liste de String</li>
 * 
 * @author Eric Reboisson
 * @since 28 sept. 06
 *        <h4>Copyright ELITOST 2006</h4>
 */
@XStreamAlias("parametres-modules")
public class ParametresModules {

    @XStreamAlias("liste-parametres")
    private List<Parametre> listeParametres;

    public ParametresModules() {
        super();
        listeParametres = new Vector<Parametre>();
    }

    public List<Parametre> getListeParametres() {
        return listeParametres;
    }

    public void addParametre(Parametre parametre) {
        listeParametres.add(parametre);
    }

    /**
         * M�thode permettant de r�cup�rer dans une hashtable le nom des
         * param�tres et leurs valeurs Un �l�ment de la hashtable r�sultat est
         * constitu� de :
         * <ul>
         * <li>en cl� : le nom du param�tre</li>
         * <li>en valeur : la liste de valeurs correspondant � ce param�tres
         * sous forme d'une liste de String</li>
         * 
         * @return
         */
    public Hashtable<String, List<String>> getHashParametres() {
        Hashtable<String, List<String>> hashParametres = new Hashtable<String, List<String>>();
        Parametre parametre = null;
        for (Iterator iter = listeParametres.iterator(); iter.hasNext(); ) {
            parametre = (Parametre) iter.next();
            hashParametres.put(parametre.getNomParametre(), parametre.getReponses().getListeValeurs());
        }
        return hashParametres;
    }
}
