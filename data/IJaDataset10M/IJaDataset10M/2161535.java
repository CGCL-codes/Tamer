package modele.simulateur;

import java.util.LinkedList;

public class PlanPartiel {

    protected LinkedList<ParametreServiceWeb> parametresServicesWeb;

    private int compteurExeSimultane, compteurRetourExe;

    private Object lock;

    public PlanPartiel() {
        parametresServicesWeb = new LinkedList<ParametreServiceWeb>();
    }

    public void initialiser(Simulateur simulateur) {
        lock = new Object();
        for (ParametreServiceWeb ps : parametresServicesWeb) {
            ps.initialiser(simulateur, this);
        }
    }

    public void executer(Simulateur simulateur) {
        compteurExeSimultane = 0;
        compteurRetourExe = 0;
        for (ParametreServiceWeb ps : parametresServicesWeb) {
            compteurExeSimultane++;
            ps.executer(simulateur);
        }
        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifierFinServiceExe() {
        synchronized (lock) {
            compteurRetourExe++;
            if (compteurRetourExe == compteurExeSimultane) {
                synchronized (this) {
                    notifyAll();
                }
            }
        }
    }

    public LinkedList<ParametreServiceWeb> getParametresServicesWeb() {
        return parametresServicesWeb;
    }

    public void stoperSimulation() {
        for (ParametreServiceWeb ps : parametresServicesWeb) {
            ps.stoperSimulation();
        }
    }
}
