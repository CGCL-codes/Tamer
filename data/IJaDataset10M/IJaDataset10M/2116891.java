package ds.moteur.voiture;

import ds.moteur.geometrie.Angle3D;
import ds.moteur.geometrie.Point;
import ds.moteur.route.cc.CourbeConduite;
import ds.moteur.voiture.moteur.MoteurThermique;

public class VoiturePilotee extends Voiture {

    private double angleRouesAvant;

    private Point positionProjetee;

    private MoteurThermique moteur;

    public VoiturePilotee(String nom, double longueur, double largeur) {
        super(nom, longueur, largeur);
        this.positionProjetee = new Point();
        this.moteur = MoteurThermique.getDefaultMoteur();
    }

    public void incrementer(long ms) {
        double ds = (mecanique.vitesse * ms) / 1000.0;
        position.setCourbeCourante(null);
        Point translation = Point.createPolar(ds, angle.theta);
        positionAvant.transformer(translation, 0);
        if (Math.abs(angleRouesAvant) > 0.01) {
            double rayon = 1.5 / Math.tan(angleRouesAvant);
            angle.theta -= ds / rayon;
        }
        super.replacerCarrosserie();
    }

    public void setRouesAvant(double angle) {
        angleRouesAvant = angle;
    }

    public void setPositionProjetee(CourbeConduite cc, double abscisse) {
        cc.recupererPosition(positionProjetee, new Angle3D(), abscisse);
        super.position.setCourbeCourante(cc);
        super.position.setAbscisse(abscisse);
    }

    public double getForceMoteur() {
        return moteur.getForce(mecanique.getVitesse());
    }

    public Point getPositionProjetee() {
        return positionProjetee;
    }

    public MoteurThermique getMoteur() {
        return moteur;
    }
}
