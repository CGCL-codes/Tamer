package EcoSpeed;

import java.util.*;
import java.util.Vector;

/**
 * Class SmartSystem
 * Coeur de calcul.
 * Reçois les demandes d'ascenseur. (Observer) (newRequest)
 * Reçois les informations de l'alarme. (Observer) (alarm)
 * Reçois les informations quand un ascenseur a fini son trajet. (Observer)
 * (endRequest)
 * Reçois les tic du TimeLine. (updateZone)
 * Demande aux ascenseurs de se déplacer. (moveLift)
 * Donne aux afficheurs la valeur à afficher. (printValue)
 */
public class SmartSystem implements Observer {

    /**
	 * Es la 1er mise a jour ?
	 */
    private boolean firstUpdate = true;

    /**
     * Objet de manipulation des statistiques.
     */
    private Statistics stats;

    /**
     * Vecteur des écrans de saisie et de visualisation
     * des étages.
     */
    private Vector<Screen> screens;

    /**
     * Vecteur des ascenseurs.
     */
    private Vector<Lift> lifts;

    /**
     * Vecteur stockant les zones de positionnement
     * des ascenseurs.
     */
    private Vector<Zone> zones;

    /**
     * Constructeur
     * @param driversLiftList Liste des drivers
     * @param screens Liste des Ecrans
     */
    public SmartSystem(Vector driversLiftList, Vector screens) {
        if (driversLiftList.isEmpty()) {
            throw new NullPointerException("Impossible d'initialiser le SmartSystem avec une liste vide de driversLift.");
        }
        stats = new Statistics();
        this.screens = screens;
        lifts = new Vector<Lift>();
        zones = new Vector<Zone>();
        Iterator it = driversLiftList.iterator();
        EcoSpeed.DriverLift lift;
        while (it.hasNext()) {
            lift = (EcoSpeed.DriverLift) it.next();
            lifts.add(new Lift(lift));
        }
    }

    ;

    /**
     * Accesseur du tableau de zones
     * @return la valeur de zones
     */
    public Vector<Zone> getZones() {
        return this.zones;
    }

    /**
     * Toutes les heures il faut changer les zones en fonction des stats
     */
    public void updateZones() {
        int size = zones.size();
        if (!this.firstUpdate) {
            this.stats.newHour();
        }
        this.firstUpdate = false;
        int[] NCileResult = stats.nCile(lifts.size());
        int nbZones = NCileResult.length;
        if (nbZones > size) {
            for (int i = size; i < nbZones; i++) {
                zones.add(new Zone());
            }
        } else if (nbZones < size) {
            for (int i = nbZones; i < size; i++) {
                zones.remove(zones.lastElement());
            }
        }
        for (int i = 0; i < nbZones; i++) {
            int startFloor = i == 0 ? 0 : NCileResult[i - 1];
            int endFloor = NCileResult[i];
            int bestFloor = stats.floorForZone(startFloor, endFloor);
            zones.get(i).updateZone(startFloor, endFloor, bestFloor);
        }
        this.checkZonesCoverage();
    }

    /**
     * Methode apellée quand un clavier effectue une demande :
     * - Recherche du meilleur ascenseur : le plus près sur le chemin, sinon celui qui s'arrete
     * le moins loin
     * - On achemine le meilleur ascenseur à l'étage de la demande
     * - Affichage de l'ascenseur sur l'écran
     * @param screen Ecran d'affichage de la nouvelle requete
     * @param startFloor Etage de debut
     * @param endFloor Etage destination
     * @param urgent C'est urgent ?
     */
    public void newRequest(Screen screen, int startFloor, int endFloor, boolean urgent) {
        Lift bestLift = this.selectLift(startFloor, endFloor, urgent);
        stats.newRequest(startFloor);
        if (urgent) {
            bestLift.clearTarget();
        }
        bestLift.setUrgence(urgent);
        bestLift.addStop(startFloor, endFloor);
        screen.printf(lifts.indexOf(bestLift));
        this.checkZonesCoverage();
    }

    /**
     * Methode quand l'alarme change d'etat.
     * @param        value
     */
    public void alarm(Boolean value) {
        Iterator itr = this.lifts.iterator();
        if (value) {
            while (itr.hasNext()) {
                Lift l = (Lift) itr.next();
                int stopFloor = l.getFloor() + l.getState();
                l.clearTarget();
                l.addStop(stopFloor, stopFloor);
            }
        } else {
            while (itr.hasNext()) {
                int groundFloor = (Utils.NB_UNDERGROUND);
                ((Lift) itr.next()).addStop(groundFloor, groundFloor);
            }
        }
    }

    /**
     * Verifie la couverture des zones de l'immeuble
     */
    public void checkZonesCoverage() {
        Vector<Zone> tmpZones = new Vector<Zone>();
        Vector<Lift> tmpLifts = new Vector<Lift>();
        Iterator it, it2;
        Zone z;
        Lift l, lForZone;
        int pathLength1, pathLength2;
        it = this.zones.iterator();
        while (it.hasNext()) {
            tmpZones.add((Zone) it.next());
        }
        it = this.lifts.iterator();
        while (it.hasNext()) {
            tmpLifts.add((Lift) it.next());
        }
        for (int i = 0; i < tmpZones.size(); ) {
            z = tmpZones.get(i);
            lForZone = null;
            l = tmpLifts.get(0);
            if (l.getState() == Lift.NO_MOVE) {
                pathLength1 = l.getPathLength(z.getBestFloor(), z.getBestFloor(), 1);
            } else {
                pathLength1 = l.getPathLength(l.getFinalFloor(), z.getBestFloor(), 1);
            }
            it = tmpLifts.iterator();
            while (it.hasNext()) {
                l = (Lift) it.next();
                if (l.getState() == Lift.NO_MOVE) {
                    pathLength2 = l.getPathLength(z.getBestFloor(), z.getBestFloor(), 1);
                } else {
                    pathLength2 = l.getPathLength(l.getFinalFloor(), z.getBestFloor(), 1);
                }
                if (z.getStartFloor() <= l.getFinalFloor() && z.getEndFloor() >= l.getFinalFloor()) {
                    if ((lForZone == null || pathLength1 > pathLength2)) {
                        pathLength1 = pathLength2;
                        lForZone = l;
                    }
                }
            }
            if (z.getBestFloor() == 42) {
                z.getBestFloor();
            }
            if (lForZone != null && (lForZone.getState() != Lift.NO_MOVE || lForZone.getFinalFloor() != z.getBestFloor())) {
                lForZone.addStop(z.getBestFloor(), z.getBestFloor());
                tmpLifts.remove(lForZone);
                tmpZones.remove(z);
            } else {
                i++;
            }
        }
        while (tmpZones.size() > 0) {
            z = tmpZones.get(0);
            it = tmpLifts.iterator();
            lForZone = tmpLifts.get(0);
            if (lForZone.getState() != lForZone.getRequestState(lForZone.getFinalFloor(), z.getBestFloor())) {
                pathLength1 = lForZone.getPathLength(z.getBestFloor(), z.getBestFloor(), 1);
            } else {
                pathLength1 = lForZone.getPathLength(lForZone.getFinalFloor(), z.getBestFloor(), 1);
            }
            while (it.hasNext()) {
                l = (Lift) it.next();
                pathLength2 = l.getPathLength(z.getBestFloor(), z.getBestFloor(), 1);
                if (pathLength1 > pathLength2) {
                    lForZone = l;
                    pathLength1 = pathLength2;
                }
            }
            if (lForZone.getFinalFloor() != z.getBestFloor()) {
                lForZone.addStop(z.getBestFloor(), z.getBestFloor());
            }
            tmpLifts.remove(lForZone);
            tmpZones.remove(z);
        }
    }

    /**
     * Methodes qui selectionne le meilleur ascenseur pour aller de @startFloor à
     * @endFloor
     * @return       EcoSpeed.Lift
     * @param        startFloor Etage de la demande
     * @param        endFloor Etage destination
     * @param urgent C'est urgent ?
     */
    public EcoSpeed.Lift selectLift(int startFloor, int endFloor, boolean urgent) {
        int bestPathLength = Integer.MAX_VALUE;
        Iterator itr;
        itr = this.lifts.iterator();
        Lift bestLift = null;
        while (itr.hasNext() && bestLift == null) {
            Lift l = (Lift) itr.next();
            if (!l.getUrgence()) {
                bestLift = l;
            }
        }
        if (bestLift == null) {
            return null;
        }
        if (urgent) {
            int tmpBestFloor = Math.abs(startFloor - Utils.NB_FLOORS);
            itr = this.lifts.iterator();
            while (itr.hasNext()) {
                Lift l = (Lift) itr.next();
                int currentFloor = l.getFloor();
                if (!l.getUrgence() && Math.abs(startFloor - currentFloor) < Math.abs(startFloor - tmpBestFloor)) {
                    tmpBestFloor = currentFloor;
                    bestLift = l;
                }
            }
            bestLift.clearTarget();
        } else {
            bestPathLength = this.lifts.get(0).getPathLength(startFloor, endFloor, 2);
            itr = this.lifts.iterator();
            while (itr.hasNext()) {
                Lift l = (Lift) itr.next();
                int currentPathLength = l.getPathLength(startFloor, endFloor, 2);
                if (currentPathLength < bestPathLength) {
                    bestPathLength = currentPathLength;
                    bestLift = l;
                }
            }
        }
        return bestLift;
    }

    /**
     * Accesseur récupérant la liste des ascenseurs.
     * @return les ascenseurs
     */
    public Vector<Lift> getLifts() {
        return lifts;
    }

    /**
     * Mise a jour du pattern Observer
     * @param arg0 l'objet qui est observé
     * @param arg1 les arguments
     */
    public void update(Observable arg0, Object arg1) {
        if (arg0 instanceof DriverKeyboard) {
            DriverKeyboard keyboard = (DriverKeyboard) arg0;
            NotificationMessage mes = (NotificationMessage) arg1;
            Iterator it = this.screens.iterator();
            Screen tmp;
            boolean end = false;
            while (it.hasNext() && !end) {
                tmp = (Screen) it.next();
                if (tmp.getId() == keyboard.getId()) {
                    this.newRequest(tmp, mes.currentFloor, mes.targetFloor, mes.urgent);
                    end = true;
                }
            }
        } else if (arg0 instanceof Alarme) {
            Alarme alarm = (Alarme) arg0;
            this.alarm(alarm.getStatut());
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * Ascesseur des statistics
     * @return les statistics
     */
    public Statistics getStats() {
        return this.stats;
    }

    /**
     * Changement de la date courante
     * @param d la nouvelle date
     */
    public void setDate(Date d) {
        this.stats.setDate(d);
    }

    /**
     * Réinitialise le SmartSystem
     */
    public void reset() {
        Iterator it = this.lifts.iterator();
        Lift l;
        while (it.hasNext()) {
            l = (Lift) it.next();
            l.reset();
        }
    }
}
