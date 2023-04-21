package juego;

/**
 *
 * @author juan
 */
public interface ObservadorHit {

    /**
     * A implementar: este metodo debe decir si el hit esta permitido o no.
     * 
     * @param fuente es el bloque que ocasiono la colision
     * @param destino es el bloque contra el que se colisiono
     * @return un booleano que determina si el golpe es afecta o no al movimiento
     */
    public boolean hited(Bloque fuente, Bloque destino);
}
