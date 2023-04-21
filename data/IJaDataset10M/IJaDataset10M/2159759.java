package atg.metier.dao;

import java.lang.reflect.Constructor;
import java.util.Enumeration;
import atg.metier.dao.hibernate.ATGDaoHibernate;
import atg.metier.dao.jdo.ATGDaoJdo;
import atg.metier.entite.ATGIEntite;
import atg.service.constante.AtgConstantes;
import atg.service.constante.AtgConstantesWF;
import atg.util.service.ATGBasicStaticClass;

/**
 * <p>
 * Titre : Classe mère de tous les DAO Factorys.
 * </p>
 * <p>
 * Description : Cette classe sera la base de tous les futurs DAO Factory du
 * Framework
 * </p>
 * <p>
 * <b>ATGDaoFactory peut être utlisée de deux manières :</b><br />
 * <br/> - <u>Manière spécialisée</u> : On hérite de ATGDaoFactory et on
 * implemente nous m�me les getXxxDao()<br/> Exemple :<br />
 * <code><pre>
 * public class FicheEntiteDaoFactory extends ATGDaoFactory(){
 *   public static ATGDao &lt;PaysEntite&gt; getFicheDao(FicheEntite.class){
 *     return new ATGDaoHibernate&lt;FicheEntite&gt;(FicheEntite.class);
 *   }
 * }
 * </pre></code><br/> - <u>Mani�re param�tr�e</u> : On renseigne le fichier de
 * constantes atg de l'application et on appelle getDao()<br/> <u>Exemple de
 * constantes</u> :<br/> <code>dao.default = hibernate</code> <i>indique le
 * dao par d�faut de l'application. s'il n'est pas renseign� le DAO par d�faut
 * d'atg est utilis� (AtgConstantes.ATG_DAO_DEFAULT)</i><br/> ou<br/>
 * <code>dao.default = atg.metier.dao.hibernate.ATGDaoHibernate</code> <i>idem</i><br/>
 * ou<br/> <code>dao.default = jdo</code> <i>pour jdo</i><br/> ou<br/>
 * <code>dao.default = atg.metier.dao.jdo.ATGDaoJdo</code> <i>idem</i><br/>
 * On peut aussi indiquer un dao pr�cis pour ceraintes entit�s : syntaxe :
 * <code>dao.monpackage.monpackages2.MonEntite = [hibernate|jdo|&ltun autre DAO&gt]</code><br/>
 * Que ce soit pour le defaut ou pour une entit� particuli�re, on peut en valeur
 * indiquer la Classe de Dao personnel � utliser.<br/> <b>/!\ ATTENTION : Un
 * DAO perso DOIT contenir un constructeur vide ou un constructeur avec 1
 * parametre de type Class</b><br/>
 * <code>dao.fr.societe.projet.metier.entite.FicheEntite=fr.societe.projet.metier.entite.FicheDao</code><br />
 * <u>Utilisation de la mani�re param�tr�e</u> : <br/>
 * <code>...<br />ATGDao&lt;EntiteBidon&gt; dao = ATGDaoFactory.getDao(EntiteBidon.class);<br/>...</code><br />
 * ou en héritant et spécifiant des méthode par DAO pour éviter le cast au
 * niveau du metier<br />
 * <code>
 * <pre>
 * ...
 * public class FicheEntiteDaoFactory extends ATGDaoFactory(){
 *   public static IFicheDao &lt;PaysEntite&gt; getFicheDao(FicheEntite.class){
 *     return IFicheDao super.getHibernateDao(FicheEntite.class);
 *   }
 * }
 * ...
 * </pre></code><br />
 * </p>
 * <p>
 * Copyright : FERRARI Olivier
 * </p>
 * 
 * @author FERRARI Olivier
 * @version 1.0 Ce logiciel est r�gi par la licence CeCILL soumise au droit
 *          fran�ais et respectant les principes de diffusion des logiciels
 *          libres. Vous pouvez utiliser, modifier et/ou redistribuer ce
 *          programme sous les conditions de la licence CeCILL telle que
 *          diffus�e par le CEA, le CNRS et l'INRIA sur le site
 *          http://www.cecill.info.
 * 
 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez pris
 * connaissance de la licence CeCILL, et que vous en avez accept� les termes.
 */
public class ATGDaoFactory extends ATGBasicStaticClass {

    public static Object getDao(Class laClasse) {
        String sDao = AtgConstantesWF.getValue(AtgConstantes.ATG_DAO_CONST_PREFIX + laClasse.getName());
        if (sDao == null) {
            String pattern = null;
            Enumeration<String> patterns = AtgConstantesWF.listKeyValue.keys();
            while ((sDao == null) && (patterns.hasMoreElements())) {
                pattern = patterns.nextElement();
                if ((AtgConstantes.ATG_DAO_CONST_PREFIX + laClasse.getName()).matches(pattern.replace(".", "\\.").replace("*", ".*"))) {
                    logFine(pattern);
                    sDao = AtgConstantesWF.getValue(pattern);
                    AtgConstantesWF.setValue("dao." + laClasse.getName(), sDao);
                }
            }
        }
        if (sDao == null) {
            sDao = AtgConstantesWF.getValue(AtgConstantes.ATG_DAO_CONST_DEFAULT);
        }
        if (sDao == null) {
            sDao = AtgConstantes.ATG_DAO_DEFAULT;
        }
        if (sDao.equals("hibernate") || sDao.equals("atg.metier.dao.hibernate.ATGDaoHibernate")) {
            logFinest("Dao retourné : " + sDao);
            return new ATGDaoHibernate(laClasse);
        } else if (sDao.equals("jdo") || sDao.equals("atg.metier.dao.jdo.ATGDaoJdo")) {
            logFinest("Dao retourné : " + sDao);
            return new ATGDaoJdo(laClasse);
        } else {
            try {
                Class classeDao = Class.forName(sDao, true, (new ATGDaoFactory()).getClass().getClassLoader());
                try {
                    Class[] paramContructeur = { Class.class };
                    Constructor leContructeur = classeDao.getConstructor(paramContructeur);
                    Object[] initargs = { laClasse };
                    logFinest("Dao retourné : " + sDao);
                    return leContructeur.newInstance(initargs);
                } catch (Exception e) {
                    logFinest("Dao retourné : " + sDao);
                    return classeDao.newInstance();
                }
            } catch (Exception e) {
                logSevere("Impossible d'instancier le dao '" + sDao + "' : " + e.getMessage());
                return null;
            }
        }
    }

    /**
     * Retourne un ATGIDaoHibernate. Attention, la classe du dao indiquée dans
     * le fichier de constante doit implémenter ATGIDao
     * 
     * @param laClasse
     * @return un ATGIDaoHibernate
     */
    public static <K extends ATGIEntite> ATGIDaoHibernate<K> getHibernateDao(Class<K> laClasse) {
        try {
            return (ATGIDaoHibernate<K>) getDao(laClasse);
        } catch (Exception e) {
            logSevere("Impossible d'instancier un dao implementant ATGIDaoHibernate : " + e.getMessage());
            return null;
        }
    }
}
