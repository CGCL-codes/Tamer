package galaxiia.jeu.souffle;

import galaxiia.jeu.unite.InformateurUnite;

public class InformationSouffle extends ClasseurSouffle implements InformateurSouffle {

    private static final long serialVersionUID = 1;

    private Souffle souffle;

    public InformationSouffle(Souffle souffle) {
        this.souffle = souffle;
    }

    /**
	 * {@inheritDoc}
	 */
    public double[] accelerationSouffle(InformateurUnite unite) {
        return souffle.accelerationSouffle(unite);
    }

    /**
	 * {@inheritDoc}
	 */
    public double dommageSouffle(InformateurUnite unite) {
        return accelerationSouffle(unite)[2];
    }

    /**
	 * {@inheritDoc}
	 */
    public int type() {
        return souffle.type();
    }

    /**
	 * {@inheritDoc}
	 */
    public long id() {
        return souffle.id();
    }
}
