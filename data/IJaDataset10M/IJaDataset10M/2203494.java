package vue.simulateur.zoneEvenements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import javax.swing.JPanel;
import controleur.ControleurPrincipal;

public class ZoneEvenements extends JPanel {

    private static final long serialVersionUID = -6612393505909766434L;

    protected EtatDeCroyanceGraphique etatDeCroyanceGraphique;

    protected SuperviseurGraphique superviseurGraphique;

    protected ObservateurGraphique observateurGraphique;

    public ZoneEvenements(ControleurPrincipal controleurPrincipal) {
        super(new GridLayout(0, 3));
        etatDeCroyanceGraphique = new EtatDeCroyanceGraphique(controleurPrincipal);
        superviseurGraphique = new SuperviseurGraphique(controleurPrincipal);
        observateurGraphique = new ObservateurGraphique(controleurPrincipal);
        this.add(etatDeCroyanceGraphique);
        this.add(observateurGraphique);
        this.add(superviseurGraphique);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.black);
    }

    public EtatDeCroyanceGraphique getEtatDeCroyanceGraphique() {
        return etatDeCroyanceGraphique;
    }

    public SuperviseurGraphique getSuperviseurGraphique() {
        return superviseurGraphique;
    }

    public ObservateurGraphique getObservateurGraphique() {
        return observateurGraphique;
    }

    public void clearLog() {
        etatDeCroyanceGraphique.supprimerTexte();
        superviseurGraphique.supprimerTexte();
        observateurGraphique.supprimerTexte();
    }
}
