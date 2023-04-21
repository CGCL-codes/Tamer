package gui.grafica;

import gestorDeConfiguracion.ControlConfiguracionClienteException;
import gui.grafica.principal.ControladorVentanaPrincipal;
import gui.grafica.principal.GUIVentanaPrincipal;

/**
 * Clase que se encarga de gestionar la gui de la aplicación en modo gráfico.
 * 
 * @author Javier Salcedo
 */
public class GUIGrafica {

    /**
     * Constructor de la clase GUIGrafica.
     * 
     * @param controlador Controlador de la ventana principal en modo grafico.
     */
    public GUIGrafica(ControladorVentanaPrincipal controlador) throws ControlConfiguracionClienteException {
        new GUIVentanaPrincipal(controlador);
    }
}
