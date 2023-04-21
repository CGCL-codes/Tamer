package nat;

import nat.transcodeur.Ambiguity;
import nat.transcodeur.AmbiguityResolverUI;

/**
 * @author bruno
 *
 */
public class AmbiguityResolverTextGUI implements AmbiguityResolverUI {

    /**
	 * Pour l'instant, on prend la première solution proposée
	 * @see nat.transcodeur.AmbiguityResolverUI#resolve(nat.transcodeur.Ambiguity)
	 */
    @Override
    public void resolve(Ambiguity amb) {
        amb.setSolution(amb.getPropositions().get(0));
    }
}
