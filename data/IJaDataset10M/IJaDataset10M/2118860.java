package br.ufg.integrate.lookup.schema;

/**
 * @author Rogerio
 * @version 0.1
 *
 * Mant�m uma refer�ncia para a entidade e o atributo que faz
 * refer�ncia a outra entidade. 
 */
public interface Ref {

    public Entity getEntity();

    public Attribute getAttribute();
}
