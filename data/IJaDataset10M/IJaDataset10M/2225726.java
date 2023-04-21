package parseur.expression;

import parseur.Environnement;
import parseur.Expression;

/**
 * Indique au parseur d'effectuer une action
 * lorsqu'il arrive sur cette expression.
 * @author Jeremy Morosi
 * @version 1.0
 */
public abstract class Action extends Expression {

    /**
	 * Constructeur d'initialisation.
	 * @param nom nom de l'expression.
	 * @param regexp expression reguliere representant
	 * cette expression.
	 * @since 1.0
	 */
    public Action(String nom, String regexp) {
        super(nom, regexp);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int activer(String arg, int pos, Environnement env, Expression prec, boolean aSuivant, int code) {
        return action(arg, pos, env, prec, aSuivant, code);
    }

    /**
	 * Methode appellee lorsque le parseur arrive sur
	 * cette expression.
	 * @param arg argument ayant mene a cette expression.
	 * @param pos position de l'argument dans la liste.
	 * @param env environnement d'execution du parseur.
	 * @param prec expression ayant menee a celle-ci.
	 * @param aSuivant indique s'il y a un autre argument
	 * dans la commande apres celui-ci.
	 * @param code code de sortie actuel.
	 * @return code de sortie suite a cette action.
	 * @since 1.0
	 */
    public abstract int action(String arg, int pos, Environnement env, Expression prec, boolean aSuivant, int code);
}
