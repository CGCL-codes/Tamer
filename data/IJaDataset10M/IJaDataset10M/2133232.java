package SimulatorStandard;

import EcoSpeed.Utils;
import Simulator.Simulator;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe de gestion d'un ascenseur standard
 */
public class StandardDriverLift extends Observable implements Observer {

    /**
	 * Identifiant de l'ascenseur
	 */
    private int id;

    /**
     * Status de la porte (ouverte/fermer)
     */
    private boolean doorOpen;

    /**
     * Etage actuelle de l'ascenseur
     */
    private int currentFloor = EcoSpeed.Utils.NB_UNDERGROUND;

    /**
     * Savoir si l'ascenseur vient juste de s'arreté ou pas
     */
    private boolean stop;

    /**
     * Charge courent de l'ascenseur
     */
    private int weight = 0;

    /**
     * Charge maximu des ascenseurs
     */
    public static int maxWeight = 1000;

    /**
     * TreeMap courant stockant les étages à traverser.
     */
    private ArrayList<Integer> stoppingSerie;

    /**
     * Constructeur
     * @param id identifiant de l'ascenseur
     */
    public StandardDriverLift(int id) {
        this.id = id;
        stoppingSerie = new ArrayList<Integer>();
    }

    /**
     * Ascesseur de l'étage actuelle
     * @return L'étage ou est l'ascenseur
     */
    public int getFloor() {
        return currentFloor;
    }

    /**
     * Modifie la charge
     * @param weight nouvelle charge
     * @return true si le chagement s'est effectué, false si non
     */
    public boolean setWeight(int weight) {
        if (weight <= StandardDriverLift.maxWeight) {
            this.weight = weight;
            return true;
        }
        return false;
    }

    /**
     * Ascesseur de la charge courrante
     * @return Charge courrante
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Ascesseur de l'id de l'ascenseur
     * @return l'Id de l'ascenseur
     */
    public int getId() {
        return this.id;
    }

    /**
     * Savoir si l'ascenseur a ses portes ouvertent ou non
     * @return true si les portes sont ouvertent, false si non
     */
    public boolean doorOpened() {
        return this.doorOpen;
    }

    /**
     * Methode apeller par @EcoSpeed.TimeLine pour le depalcement des ascenseurs
     */
    public void update() {
        if (this.doorOpen) {
            this.closeDoor();
            this.setChanged();
            this.notifyObservers(0);
        } else if (this.stoppingSerie.size() > 0) {
            if (this.currentFloor < this.stoppingSerie.get(0)) {
                this.stop = false;
                this.currentFloor++;
                this.setChanged();
                this.notifyObservers(1);
            } else if (this.currentFloor > this.stoppingSerie.get(0)) {
                this.stop = false;
                this.currentFloor--;
                this.setChanged();
                this.notifyObservers(-1);
            }
            if (!this.stop && this.stoppingSerie.contains(this.currentFloor)) {
                this.stop = true;
                this.openDoor();
                this.setChanged();
                this.notifyObservers(0);
                this.stoppingSerie.remove(new Integer(this.currentFloor));
            }
        }
    }

    /**
     * Ferme les portes de l'ascenseur
     */
    public void closeDoor() {
        this.doorOpen = false;
    }

    /**
     * Ouvre les portes de l'ascenseur
     */
    public void openDoor() {
        this.doorOpen = true;
    }

    /**
     * Ajout un arret a l'ascenseur
     * @param startFloor l'étage de l'arret
     */
    public void addStop(int startFloor) {
        this.stoppingSerie.add(startFloor);
        this.stop = false;
    }

    /**
     * Methode de mise a jour du pattern observer
     * @param o classe qui a reveille l'ascenseur
     * @param o1 parametre du message
     */
    public void update(Observable o, Object o1) {
        if (o instanceof Simulator) {
            Simulator k = (Simulator) o;
        }
    }

    /**
	 * Methode pour réinitialliser l'ascenseur
	 */
    public void reset() {
        this.currentFloor = Utils.NB_UNDERGROUND;
        this.weight = 0;
        this.doorOpen = false;
        this.stoppingSerie = new ArrayList<Integer>();
        this.setChanged();
        this.notifyObservers(0);
    }
}
