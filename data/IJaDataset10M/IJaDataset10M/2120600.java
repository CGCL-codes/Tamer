package org.sam.colisiones;

/**
 * Interface que proporciona los métodos para tratar las colisones entre objetos.
 */
public interface Colisionable {

    /**
	 * @return Los {@code Limites} del objeto solicitados.
	 */
    public Limites getLimites();

    /**
	 * Este método evalúa si hay una colisión, entre el objeto que lo invoca y otro objeto {@code Colisionable}.
	 * 
	 * @param otro El otro objeto a evaluar.
	 * 
	 * @return <ul><li>{@code true}, cuando hay una colisión. <li>{@code false}, en caso contrario.</ul>
	 */
    public boolean hayColision(Colisionable otro);

    /**
	 * Este método trata la colisión, entre el objeto que lo invoca y otro objeto  {@code Colisionable}.
	 * 
	 * @param otro El otro objeto con el que se produce la colisión.
	 */
    public void colisionar(Colisionable otro);
}
