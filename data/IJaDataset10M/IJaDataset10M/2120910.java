package vista;

import org.omg.uml.foundation.core.ModelElement;
import org.uml.diagrammanagement.Diagram;
import vista.graficador.GrDiagrama;

/**
 * The Interface DiagramContainer.
 * 
 * @author Juan Timoteo Ponce Ortiz
 */
public interface DiagramContainer {

    /**
     * Gets the graficador.
     * 
     * @param index the index
     * 
     * @return the graficador
     */
    GrDiagrama getGraficador(int index);

    /**
     * Diagram contains.
     * 
     * @param diagrama the diagrama
     * 
     * @return true, if successful
     */
    boolean diagramContains(Diagram diagrama);

    /**
     * Adds the graficador.
     * 
     * @param diagrama the diagrama
     * 
     * @return true, if successful
     */
    boolean addGraficador(Diagram diagrama);

    /**
     * Removes the graficador.
     * 
     * @param diagrama the diagrama
     * 
     * @return true, if successful
     */
    boolean removeGraficador(Diagram diagrama);

    /**
     * Adds the elemento to diagram.
     * 
     * @param elemento the elemento
     */
    void addElementoToDiagram(ModelElement elemento);

    /**
     * Checks if is showed.
     * 
     * @param diagrama the diagrama
     * 
     * @return true, if is showed
     */
    boolean isShowed(Diagram diagrama);

    /**
     * Sets the show.
     * 
     * @param diagrama the new show
     */
    void setShow(Diagram diagrama);

    /**
	 * Find relaciones.
	 * 
	 * @param elemento the elemento
	 * 
	 * @throws Exception the exception
	 */
    void findRelaciones(ModelElement elemento) throws Exception;
}
