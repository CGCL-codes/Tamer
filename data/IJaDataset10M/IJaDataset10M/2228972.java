package visugraph.lambda;

import java.util.List;

/**
 * Décrit une fonction accessible à une lambda.
 * Une fonction est composée d'un ensemble de paramètres et d'une valeur de retour.
 * Une fonction est statiquement typée et doit être pure de préférence (sans effet de bord +
 * transparence référentielle). Le respect de ce critère est fortement recommandée mais non
 * obligatoire.
 */
public interface Function {

    /**
	 * Donne le type de retour de cette fonction.
	 * Un appel à exec() devra obligatoirement renvoyer un objet de ce type.
	 */
    Class<?> getReturnType();

    /**
	 * Retourne la liste des paramètres acceptés par la fonction.
	 * Dans le cas d'une fonction à nombre variable d'arguments, seuls
	 * les paramètres obligatoires et le paramètre "varargs" sont retournés.
	 */
    List<Class<?>> getParameters();

    /**
	 * Indique si cette fonction supporte les appels à nombre d'arguments variable.
	 * Dans ce cas, le dernier paramètre retourné par getParameters() est considéré
	 * comme étant "variable".
	 */
    boolean isVarArgs();

    /**
	 * Retourne le nom de la fonction. Ce nom est utilisé par le parseur pour caractériser la fonction.
	 */
    String getName();

    /**
	 * Exécute la fonction et renvoie le résultat associé.
	 * Les valeurs des paramètres doivent correspondre aux types retournés par getParameters().
	 */
    Object exec(List<Object> params);
}
