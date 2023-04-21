package parseur.expression.variable;

import parseur.Environnement;
import parseur.Expression;
import parseur.expression.Variable;

/**
 * Implementation basique pour une variable dont
 * la valeur recue est un mot.
 * @author Jeremy Morosi
 * @version 1.0
 */
public class VariableString extends Variable {

    /**
	 * Constructeur d'initialisation.
	 * @param nom nom de la variable.
	 * @param regexp expression reguliere de la variable.
	 * @since 1.0
	 */
    public VariableString(String nom, String regexp) {
        super(nom, regexp);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object convertir(String valeur, int pot, Environnement env, Expression prec, boolean aSuivant, int code) {
        return valeur;
    }
}
