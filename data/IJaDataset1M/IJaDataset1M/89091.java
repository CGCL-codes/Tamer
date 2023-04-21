package at.vartmp.jschnellen.backend.spieler.ki;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import at.vartmp.jschnellen.backend.utils.ConfigurationUnit;
import at.vartmp.jschnellen.core.basic.Constants;
import at.vartmp.jschnellen.core.basic.Karte;
import at.vartmp.jschnellen.core.config.ServerConfManagerSlave;
import at.vartmp.jschnellen.core.modules.ISchrift;
import at.vartmp.jschnellen.core.util.BeanManager;
import at.vartmp.jschnellen.core.util.SortVector;

/**
 * The Class KarteTauschen.
 */
public class KarteTauschen {

    /** The bean manager. */
    private BeanManager beanManager;

    /** The server conf manager. */
    private ServerConfManagerSlave serverConfManager;

    private ISchrift schrift;

    /**
	 * Instantiates a new karte tauschen.
	 * 
	 * @param gameName the game name
	 * @param schrift 
	 */
    public KarteTauschen(String gameName, ISchrift schrift) {
        beanManager = BeanManager.getInstance();
        serverConfManager = ConfigurationUnit.getInstance().getServerConfManagerFor(gameName);
        this.schrift = schrift;
    }

    /**
	 * Zu tauschende karten.
	 * 
	 * @param kd
	 *            the kd
	 * 
	 * @return the vector< integer>
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public synchronized Vector<Integer> zuTauschendeKarten(List<Karte> kd) throws IOException {
        int max = 5;
        if (serverConfManager.getPlayerCount(false) == 4) {
            max = 3;
        }
        SortVector tmp = new SortVector();
        SortVector rundenfarbenKarten = new SortVector();
        SortVector asKarten = new SortVector();
        Vector<Integer> zuTauschen = new Vector<Integer>();
        int farbenMitRundenfarbeCount = 0;
        for (int i = 0; i < kd.size(); i++) {
            tmp.add(kd.get(i));
        }
        for (int i = tmp.size() - 1; i >= 0; i--) {
            if (tmp.get(i).getKartenFarbe().equals(schrift.getRoundColor()) || tmp.get(i).getKartenFarbe().equals(beanManager.getFarbe("Steiner"))) {
                farbenMitRundenfarbeCount++;
                rundenfarbenKarten.add(tmp.remove(i));
            } else if (tmp.get(i).getKartenWert() == 15) {
                asKarten.add(tmp.remove(i));
            } else if (tmp.get(i).getKartenWert() >= 12 && schrift.getRoundColor().equals(beanManager.getFarbe("Steiner"))) {
                tmp.remove(i);
            }
        }
        if (max == 3 && tmp.size() > 3) {
            tmp.remove(0);
        } else if (max == 5 && tmp.size() == 4) {
            tmp.remove(0);
        }
        if (schrift.getRoundColor().equals(beanManager.getFarbe("Steiner"))) {
            if (asKarten.isEmpty() && max == 5) {
                tauscheAlle(zuTauschen);
            } else {
                tauscheKarten(zuTauschen, kd, tmp);
            }
        } else if (rundenfarbenKarten.isEmpty() && max == 5) {
            tauscheAlle(zuTauschen);
        } else if (rundenfarbenKarten.size() == 1) {
            if (rundenfarbenKarten.firstElement().getKartenWert() > 11 && !asKarten.isEmpty()) {
                tauscheKarten(zuTauschen, kd, tmp);
            } else {
                if (max == 5) {
                    tauscheAlle(zuTauschen);
                } else {
                    tauscheKarten(zuTauschen, kd, tmp);
                }
            }
        } else if (rundenfarbenKarten.size() >= 2) {
            tauscheKarten(zuTauschen, kd, tmp);
        }
        return zuTauschen;
    }

    /**
	 * Tausche alle.
	 * 
	 * @param zuTauschen the zu tauschen
	 */
    private synchronized void tauscheAlle(Vector<Integer> zuTauschen) {
        for (int i = 0; i < Constants.ANZAHL_KARTEN_PRO_SPIELER; i++) {
            zuTauschen.add(i);
        }
    }

    /**
	 * Tausche karten.
	 * 
	 * @param zuTauschen the zu tauschen
	 * @param kd the kd
	 * @param left the left
	 */
    private synchronized void tauscheKarten(Vector<Integer> zuTauschen, List<Karte> kd, SortVector left) {
        for (int i = 0; i < left.size(); i++) {
            zuTauschen.add(getIndexFromKarte(kd, left.get(i)));
        }
    }

    /**
	 * Tausche einzelne karte.
	 * 
	 * @param k
	 *            the k
	 * 
	 * @return true, if successful
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public boolean tauscheEinzelneKarte(Karte k) throws IOException {
        if (k.getKartenWert() >= 14) {
            return true;
        } else if (k.getKartenFarbe().equals(schrift.getRoundColor())) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Gets the index from karte.
	 * 
	 * @param kd the kd
	 * @param k the k
	 * 
	 * @return the index from karte
	 */
    private static int getIndexFromKarte(List<Karte> kd, Karte k) {
        int i = -1;
        for (Karte tmp : kd) {
            i++;
            if (tmp.equals(k)) {
                return i;
            }
        }
        return i;
    }
}
