package ds;

import java.io.File;
import ds.ihm.Ihm;
import ds.ihm.InterfaceHommeMachine;
import ds.ihm.joystick.ValeursJoystick;
import ds.moteur.Moteur;

public class Controleur {

    private InterfaceHommeMachine ihm;

    private Moteur moteur;

    private static long DUREE = 50;

    public Controleur() {
    }

    public void init() {
        this.ihm = new Ihm(this);
        this.ihm.afficherEcranDemarrage();
    }

    public void initSimu(File fichierTerrain) {
        this.ihm.detruireEcranDemarrage();
        this.ihm.initIhm();
        this.moteur = new Moteur();
        this.moteur.init(fichierTerrain);
        this.ihm.getInterfaceGraphique().chargerTerrain(moteur.getScene().getTerrain());
        Thread gb = new GrandeBoucle(this, DUREE);
        gb.start();
    }

    public void boucler() {
        boolean touches[] = this.ihm.getClavier().getTouchePressee();
        ValeursJoystick valeursJoystick = this.ihm.getValeursJoystick();
        this.moteur.incrementer(DUREE, touches, valeursJoystick);
        this.ihm.getInterfaceGraphique().afficherScene(moteur.getScene());
    }
}
