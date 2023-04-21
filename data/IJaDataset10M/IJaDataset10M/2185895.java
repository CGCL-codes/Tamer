package gui.grafica.configuracion;

import gestorDeConfiguracion.ControlConfiguracionCliente;
import gestorDeConfiguracion.ControlConfiguracionClienteException;
import gestorDeConfiguracion.PropiedadCliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Clase controladora (siguiendo el patron MVC) que se encarga de recibir los eventos
 * de la Vista (GUIPanelConfiguracion) y tratarlos adecuadamente, realizando operaciones 
 * sobre el Modelo (objeto ControlConfiguracionCliente) si es necesario.
 * 
 * @author F. Javier Sanchez Pardo
 */
public class ControladorPanelConfiguracion implements ActionListener {

    private ControlConfiguracionCliente _objetoModelo;

    private GUIDialogoConfiguracion _objetoVista;

    /**
     * Constructor que recibe una instancia del Modelo (objeto ControlConfiguracionCliente)
     * y otra de la Vista (objeto GUIPanelConfiguracion)
     * @param oCtrlConfigCli referencia al Modelo (patron MVC)
     * @param oGUIPanelConfig referencia a la Vista (patron MVC) 
     */
    public ControladorPanelConfiguracion(ControlConfiguracionCliente oCtrlConfigCli, GUIDialogoConfiguracion oGUIDialogoConfig) {
        _objetoModelo = oCtrlConfigCli;
        _objetoVista = oGUIDialogoConfig;
    }

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == _objetoVista.obtenerBotonAceptar()) {
            try {
                if (validar()) {
                    modificarModelo();
                    _objetoVista.setVisible(false);
                }
            } catch (ControlConfiguracionClienteException ex) {
                Logger.getLogger(ControladorPanelConfiguracion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (source == _objetoVista.obtenerBotonCancelar()) {
            _objetoVista.setVisible(false);
        } else if (source == _objetoVista.obtenerBotonRestaurar()) {
            _objetoVista.inicializarCampos(_objetoModelo.obtenerConfiguracionPorDefecto());
        } else if (source == _objetoVista.obtenerBotonDirLlegada()) {
            String sDirectorio = _objetoVista.obtenerDirectorio();
            if (sDirectorio != null) _objetoVista.establecerDirLlegada(sDirectorio);
        } else if (source == _objetoVista.obtenerBotonDirCompartidos()) {
            String sDirectorio = _objetoVista.obtenerDirectorio();
            if (sDirectorio != null) _objetoVista.establecerDirCompartidos(sDirectorio);
        }
    }

    /**
     * Si el contenido de los controles de la pestaña (la Vista) es diferente de los valores 
     * de las propiedades en el objeto ControlConfiguracionCliente (Modelo) entonces
     * actualizo los valores de las propiedades en el Modelo.
     * 
     */
    private void modificarModelo() throws ControlConfiguracionClienteException {
        Properties propiedades = new Properties();
        String sCadenaAux = null;
        sCadenaAux = _objetoVista.obtenerNumDescargasSim();
        if (sCadenaAux.compareTo(_objetoModelo.obtenerPropiedad(PropiedadCliente.NUM_DESCARGAS_SIM.obtenerLiteral())) != 0) propiedades.setProperty(PropiedadCliente.NUM_DESCARGAS_SIM.obtenerLiteral(), sCadenaAux);
        sCadenaAux = _objetoVista.obtenerLimVelocidadSubida();
        if (sCadenaAux.compareTo(_objetoModelo.obtenerPropiedad(PropiedadCliente.LIM_VELOCIDAD_SUBIDA.obtenerLiteral())) != 0) propiedades.setProperty(PropiedadCliente.LIM_VELOCIDAD_SUBIDA.obtenerLiteral(), sCadenaAux);
        sCadenaAux = _objetoVista.obtenerLimVelocidadBajada();
        if (sCadenaAux.compareTo(_objetoModelo.obtenerPropiedad(PropiedadCliente.LIM_VELOCIDAD_BAJADA.obtenerLiteral())) != 0) propiedades.setProperty(PropiedadCliente.LIM_VELOCIDAD_BAJADA.obtenerLiteral(), sCadenaAux);
        sCadenaAux = _objetoVista.obtenerPuerto();
        if (sCadenaAux.compareTo(_objetoModelo.obtenerPropiedad(PropiedadCliente.PUERTO.obtenerLiteral())) != 0) propiedades.setProperty(PropiedadCliente.PUERTO.obtenerLiteral(), sCadenaAux);
        sCadenaAux = _objetoVista.obtenerDirLlegada();
        if (sCadenaAux.compareTo(_objetoModelo.obtenerPropiedad(PropiedadCliente.DIR_LLEGADA.obtenerLiteral())) != 0) propiedades.setProperty(PropiedadCliente.DIR_LLEGADA.obtenerLiteral(), sCadenaAux);
        sCadenaAux = _objetoVista.obtenerDirCompartidos();
        if (sCadenaAux.compareTo(_objetoModelo.obtenerPropiedad(PropiedadCliente.DIR_COMPARTIDOS.obtenerLiteral())) != 0) propiedades.setProperty(PropiedadCliente.DIR_COMPARTIDOS.obtenerLiteral(), sCadenaAux);
        sCadenaAux = _objetoVista.obtenerIPServidor();
        if (sCadenaAux.compareTo(_objetoModelo.obtenerPropiedad(PropiedadCliente.IP_SERVIDOR.obtenerLiteral())) != 0) propiedades.setProperty(PropiedadCliente.IP_SERVIDOR.obtenerLiteral(), sCadenaAux);
        sCadenaAux = _objetoVista.obtenerPuertoServidor();
        if (sCadenaAux.compareTo(_objetoModelo.obtenerPropiedad(PropiedadCliente.PUERTO_SERVIDOR.obtenerLiteral())) != 0) propiedades.setProperty(PropiedadCliente.PUERTO_SERVIDOR.obtenerLiteral(), sCadenaAux);
        sCadenaAux = _objetoVista.obtenerNombreServidor();
        if (sCadenaAux.compareTo(_objetoModelo.obtenerPropiedad(PropiedadCliente.NOMBRE_SERVIDOR.obtenerLiteral())) != 0) propiedades.setProperty(PropiedadCliente.NOMBRE_SERVIDOR.obtenerLiteral(), sCadenaAux);
        sCadenaAux = _objetoVista.obtenerDescripServidor();
        if (sCadenaAux.compareTo(_objetoModelo.obtenerPropiedad(PropiedadCliente.DESCRIP_SERVIDOR.obtenerLiteral())) != 0) propiedades.setProperty(PropiedadCliente.DESCRIP_SERVIDOR.obtenerLiteral(), sCadenaAux);
        sCadenaAux = _objetoVista.obtenerNombreUsuario();
        if (sCadenaAux.compareTo(_objetoModelo.obtenerPropiedad(PropiedadCliente.NOMBRE_USUARIO.obtenerLiteral())) != 0) propiedades.setProperty(PropiedadCliente.NOMBRE_USUARIO.obtenerLiteral(), sCadenaAux);
        if (propiedades.size() != 0) _objetoModelo.establecerConfiguracion(propiedades);
    }

    /**
     * Valida el contenido de los controles de entrada.
     */
    private boolean validar() {
        String sMensajeError = "";
        int numErrores = 0;
        if ((_objetoVista.obtenerNumDescargasSim().compareTo("") == 0) || (_objetoVista.obtenerLimVelocidadSubida().compareTo("") == 0) || (_objetoVista.obtenerLimVelocidadBajada().compareTo("") == 0) || (_objetoVista.obtenerPuerto().compareTo("") == 0) || (_objetoVista.obtenerDirLlegada().compareTo("") == 0) || (_objetoVista.obtenerDirCompartidos().compareTo("") == 0) || (_objetoVista.obtenerIPServidor().compareTo("") == 0) || (_objetoVista.obtenerPuertoServidor().compareTo("") == 0) || (_objetoVista.obtenerNombreServidor().compareTo("") == 0) || (_objetoVista.obtenerDescripServidor().compareTo("") == 0) || (_objetoVista.obtenerNombreUsuario().compareTo("") == 0)) {
            sMensajeError = "Todos los campos son obligatorios.\n";
            numErrores++;
        }
        try {
            Integer.parseInt(_objetoVista.obtenerNumDescargasSim());
        } catch (NumberFormatException e) {
            sMensajeError = sMensajeError + "El Numero de Descargas debe ser un numero.\n";
            numErrores++;
        }
        try {
            Integer.parseInt(_objetoVista.obtenerLimVelocidadSubida());
        } catch (NumberFormatException e) {
            sMensajeError = sMensajeError + "El Limite de Velocidad de subida debe ser un numero.\n";
            numErrores++;
        }
        try {
            Integer.parseInt(_objetoVista.obtenerLimVelocidadBajada());
        } catch (NumberFormatException e) {
            sMensajeError = sMensajeError + "El Limite de Velocidad de bajada debe ser un numero.\n";
            numErrores++;
        }
        try {
            Integer.parseInt(_objetoVista.obtenerPuerto());
        } catch (NumberFormatException e) {
            sMensajeError = sMensajeError + "El Puerto debe ser un numero.\n";
            numErrores++;
        }
        try {
            Integer.parseInt(_objetoVista.obtenerPuertoServidor());
        } catch (NumberFormatException e) {
            sMensajeError = sMensajeError + "El Puerto del Servidor debe ser un numero.\n";
            numErrores++;
        }
        if (numErrores > 0) {
            JOptionPane.showMessageDialog(_objetoVista, sMensajeError, "Error configuracion", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
