package net.sf.kfgodel.dgarcia.java.lang;

import java.util.Map;
import net.sf.kfgodel.dgarcia.lang_identificators.EqualsIdentificator;

/**
 * Esta clase implementa la interfaz de Map.Entry brindando la posibilidad de tener un Par de
 * objetos como un objeto en si. Esta clase representa una asociacion de un par de elementos en la
 * cual se establece un orden. Key es el primer elemento y Value el segundo
 * 
 * @param <K>
 *            Tipo del primer objeto
 * @param <V>
 *            Tipo del segundo
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class ParOrdenado<K, V> implements Map.Entry<K, V> {

    /**
	 * Primero objeto del par
	 */
    private K primero;

    /**
	 * Segundo objeto del par
	 */
    private V segundo;

    /**
	 * Constructor basado en los dos elementos que conforman el par
	 * 
	 * @param primerElemento
	 *            primer objeto
	 * @param segundo
	 *            segundo del par
	 */
    public ParOrdenado(K primerElemento, V segundo) {
        this.primero = primerElemento;
        this.segundo = segundo;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        Map.Entry<K, V> that = (Map.Entry<K, V>) obj;
        return EqualsIdentificator.instance.areEquals(this.getKey(), that.getKey()) && EqualsIdentificator.instance.areEquals(this.getValue(), that.getValue());
    }

    /**
	 * @return Devuelve un array creado con ambos elementos TODO Cambiar por una variable de tipo
	 *         cuando se pueda inferir un supertipo
	 */
    public Object[] getElements() {
        return new Object[] { this.primero, this.segundo };
    }

    /**
	 * @return Returns the key.
	 */
    public K getKey() {
        return this.primero;
    }

    /**
	 * @return Devuelve el primer elemento del par
	 */
    public K getPrimero() {
        return this.getKey();
    }

    /**
	 * @return Devuelve el segundo elemento del par
	 */
    public V getSegundo() {
        return this.getValue();
    }

    /**
	 * @return Returns the value.
	 */
    public V getValue() {
        return this.segundo;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        int keyHash = (this.getKey() == null) ? 0 : this.getKey().hashCode();
        int valueHash = (this.getValue() == null) ? 0 : this.getValue().hashCode();
        return keyHash ^ valueHash;
    }

    /**
	 * @param key
	 *            The key to set.
	 */
    public void setKey(K key) {
        this.primero = key;
    }

    /**
	 * @param primero
	 *            The key to set.
	 */
    public void setPrimero(K primero) {
        this.setKey(primero);
    }

    /**
	 * Establece el segundo objeto del par
	 * 
	 * @param segundo
	 *            EL nuevo objeto a establecer como segundo elemento
	 * @return EL objeto anterior de est par
	 */
    public V setSegundo(V segundo) {
        return this.setValue(segundo);
    }

    /**
	 * Establece el segundo objeto del par
	 * 
	 * @param value
	 *            Objeto a contener en este par
	 * @return El objeto anterior usado como segundo
	 * @see java.util.Map.Entry#setValue(Object)
	 */
    public V setValue(V value) {
        V oldValue = this.segundo;
        this.segundo = value;
        return oldValue;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return this.primero + "->" + this.segundo;
    }

    /**
	 * Crea un nuevo par ordenado con los valores indicados
	 * 
	 * @param <K>
	 *            Tipo del primero objeto
	 * @param <V>
	 *            Tipo del segundo
	 * @param primero
	 *            Primer objeto del par
	 * @param segundo
	 *            Segundo objeto del par
	 * @return El par creado
	 */
    public static <K, V> ParOrdenado<K, V> create(K primero, V segundo) {
        ParOrdenado<K, V> par = new ParOrdenado<K, V>(primero, segundo);
        return par;
    }
}
