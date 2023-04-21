package net.sf.ideoreport.tools.cache;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * G�n�rateur de cl� unique � base de base 64 et MD5, utilis� dans la gestion du cache des rapports compil�s
 * @author jbeausseron 
 */
public class IdeoReportKeyGenerator {

    /**
    * Logger for this class
    */
    private static final Log LOGGER = LogFactory.getLog(IdeoReportKeyGenerator.class);

    /**
    * Mod�le singleton : une seule instance de cette classe
    */
    private static IdeoReportKeyGenerator instance;

    /**
    * Renvoie une instance de g�n�rateur de cl�
    * @return
    */
    public static IdeoReportKeyGenerator getInstance() {
        if (instance == null) {
            instance = new IdeoReportKeyGenerator();
        }
        return instance;
    }

    /**
    * Pas d'instanciation "publique" de ce composant
    *
    */
    protected IdeoReportKeyGenerator() {
    }

    /**
    * Construit une cl� � partir d'un contenu pass� en param�tre sous la forme d'une cha�ne de caract�res.
    * 
    * @param pContent contenu � identifier
    * @return la cl� calcul�e
    */
    public String getKey(String pContent) {
        String vRetour = "";
        vRetour = DigestUtils.md5Hex(pContent.getBytes());
        vRetour = new String(Base64.encodeBase64(vRetour.getBytes()));
        vRetour = vRetour.replace('/', '_');
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("generating key for object [" + pContent.hashCode() + "] : key = [" + vRetour + "]");
        }
        return vRetour;
    }
}
