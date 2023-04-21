package modelo;

/**Clase que implementa la interfase Habilidad.-
 * @author Mart
 * @since 11/10/08
 * 
 */
public abstract class Herramienta implements Habilidad {

    /**Constructor de Herramienta.-
	 * 
	 */
    public Herramienta() {
    }

    public void utilizar(ObjetoVivo personaje) {
    }

    public void utilizar(Terreno terreno) {
    }

    public void utilizar(Terreno terreno, ObjetoVivo personaje) {
    }
}
