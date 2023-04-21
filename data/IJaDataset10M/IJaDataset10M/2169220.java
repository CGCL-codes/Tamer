package visugraph.data;

/**
 * <p>Interface commune aux attributs. Un attribut est une Data spéciale
 * auquelle on ajoute un nom et un type et dont les changements peuvent être écoutés.
 * L'attribut, contrairement à la Data simple, est "Runtime-ready" puisqu'il peut être
 * manipulé à l'exécution. Dans une Data simple, le type est perdu à cause du mécanisme
 * de "type-erasure" de java.</p>
 */
public interface Attribute<K, V> extends Data<K, V> {

    /**
	 * Retourne le nom associé à cet attribut.
	 * @return le nom de l'attribut
	 */
    String getName();

    /**
	 * Retourne le type de l'attribut.
	 * @return la classe représentant le type de données.
	 */
    Class<? extends V> getType();

    /**
	 * Ajoute un nouveau listener sur cet attribut.
	 */
    void addAttributeListener(AttributeListener listener);

    /**
	 * Supprime un listener de cet attribut.
	 */
    void removeAttributeListener(AttributeListener listener);
}
