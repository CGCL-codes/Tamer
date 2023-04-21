package galaxiia.jeu.souffle.explosif;

import galaxiia.jeu.Outils;
import galaxiia.jeu.souffle.SouffleVirtuel;
import galaxiia.jeu.unite.ClasseurUnite;
import galaxiia.jeu.unite.InformateurUnite;

public class SouffleExplosifVirtuel extends SouffleVirtuel implements InformateurSouffleExplosif {

    private static final long serialVersionUID = 0;

    private final double[] position;

    private final double rayon;

    private final double rayonMinimum;

    private final double rayonMaximum;

    private final double puissanceAcceleration;

    private final double dommage;

    private final long duree;

    private final long tempsExistance;

    public SouffleExplosifVirtuel(int type, double[] position, double rayon, double rayonMinimum, double rayonMaximum, long duree, long tempsExistance, double puissanceAcceleration, double dommage, long id) {
        super(type, id);
        this.dommage = dommage;
        this.puissanceAcceleration = puissanceAcceleration;
        this.position = position;
        this.rayon = rayon;
        this.rayonMinimum = rayonMinimum;
        this.rayonMaximum = rayonMaximum;
        this.duree = duree;
        this.tempsExistance = tempsExistance;
    }

    public final double[] position() {
        return new double[] { position[0], position[1] };
    }

    public final double rayon() {
        return rayon;
    }

    public final double rayonMinimum() {
        return rayonMinimum;
    }

    public final double rayonMaximum() {
        return rayonMaximum;
    }

    public final long duree() {
        return duree;
    }

    public final long tempsExistance() {
        return tempsExistance;
    }

    public double dommage(InformateurUnite unite) {
        switch(type()) {
            case SOUFFLE_EXPLOSION:
                if (ClasseurUnite.estProjectile(unite.type())) {
                    return dommage / 2.0;
                } else {
                    return dommage;
                }
            case SOUFFLE_ELECTROMAGNETIQUE:
                if (ClasseurUnite.estProjectile(unite.type())) {
                    return dommage * 7.5;
                } else {
                    return dommage;
                }
            case SOUFFLE_ENERGIE_NOIRE:
                return (50 + (unite.energie() * 10.0));
        }
        return 0;
    }

    public double puissanceAcceleration(InformateurUnite unite) {
        return puissanceAcceleration;
    }

    public final double[] accelerationSouffle(InformateurUnite unite) {
        if (Outils.distance(position, unite.position()) < rayon + unite.rayon()) {
            double[] puissance = Outils.normalisation(Outils.vecteur(this.position, unite.position()), puissanceAcceleration(unite));
            return new double[] { puissance[0], puissance[1], dommage(unite) };
        } else {
            return new double[] { 0, 0, 0 };
        }
    }
}
