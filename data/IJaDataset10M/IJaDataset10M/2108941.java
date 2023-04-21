package moteur;

import api_moteur.GarantieI;
import org.apache.log4j.*;
import java.util.*;
import java.net.*;
import java.io.*;

/** Une case qui n'en pas fixe */
public class CaseMobile extends Case implements api_moteur.CaseMobileI, Serializable {

    static Logger logger = Logger.getLogger(CaseMobile.class);

    /** Garantie de la case, si elle en possede une... */
    private GarantieI garantie;

    /** Constructeur general.
	 * @param geom Geometrie de la case.
	 * @param garantie Garantie de la case.
	 */
    public CaseMobile(CaseGeometry geom, GarantieI garantie) {
        super(geom);
        this.garantie = garantie;
        logger.debug("Object Created:(geom =" + geom + ", garantie=" + garantie + ")");
    }

    /** Methode de test d'egalite
	 */
    public boolean equals(Object bar) {
        if (bar instanceof CaseMobile) {
            CaseMobile foo = (CaseMobile) bar;
            boolean result = super.equals(foo);
            if (this.garantie != null && foo.garantie != null) result &= this.garantie.equals(foo.garantie); else result &= this.garantie == foo.garantie;
            return result;
        } else return false;
    }

    /** Renvoie la garantie associee a la case
	 * (peut etre null s'il n'y a pas de garantie sur la case.)
	 * @return garantie de la case
	 */
    public GarantieI getGarantie() {
        return this.garantie;
    }

    /** Definit la garantie associe a la case
	 * (peut etre null s'il n'y a pas de garantie sur la case.)
	 * @param garantie La garantie
	 */
    public void setGarantie(GarantieI garantie) {
        this.garantie = garantie;
    }
}
