package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import utils.defines.Defines;

/**
 * Esta clase permite mostrar un mensaje en pantalla. Los mensajes
 * pueden ser errores ({@link #ERROR_MESSAGE}), advertencias
 * ({@link #WARNING_MESSAGE}) o informaci�n ({@link #INFO_MESSAGE}).
 * Permite centralizar el manejo de los mensajes de error.
  */
public class ErrorMessage {

    public static final int ERROR_MESSAGE = SWT.ICON_ERROR | SWT.OK;

    public static final int WARNING_MESSAGE = SWT.ICON_WARNING | SWT.OK;

    public static final int WARNING_QUESTION = SWT.ICON_WARNING | SWT.YES | SWT.NO;

    public static final int INFO_MESSAGE = SWT.ICON_INFORMATION | SWT.OK;

    /**
	 * Muestra en pantalla un di�logo con un mensaje personalizado.
	 * @param msg		mensaje.
	 * @param msgType	tipo de mensaje.
	 * @param parent	instancia <code>Composite</code> padre.
	 * @return			el ID del bot�n que fue seleccionado para cerrar
	 * 					el di�logo, o <code>Defines.ERROR</code> en caso
	 * 					de error. 
	 */
    public static int customMessage(String msg, int msgType, Composite parent) {
        if (parent == null) return Defines.ERROR;
        if (msg == null) msg = "";
        MessageBox messageBox = new MessageBox(parent.getShell(), msgType);
        if (msgType == ERROR_MESSAGE) messageBox.setText("Error"); else if (msgType == WARNING_MESSAGE || msgType == WARNING_QUESTION) messageBox.setText("Advetencia"); else messageBox.setText("Informaci�n");
        messageBox.setMessage(msg);
        return messageBox.open();
    }

    /**
	 * Muestra en pantalla un di�logo con un mensaje de error.
	 * @param errCode	c�digo del mensaje de error a mostrar.
	 * @param parent	instancia <code>Composite</code> padre.
	 * @return			el ID del bot�n que fue seleccionado para cerrar
	 * 					el di�logo, o <code>Defines.ERROR</code> en caso
	 * 					de error. 
	 */
    public static int errorMessage(int errCode, Composite parent) {
        if (parent == null) return Defines.ERROR;
        String msg = null;
        switch(errCode) {
            case Defines.ERROR_NO_ROBOT_FOUND_WITH_NAME:
                msg = "No se pudo encontrar un robot con ese nombre.";
                break;
            case Defines.ERROR_NOT_ALL_ROBOT_RECOGNIZED:
                msg = "No fueron reconocidos todos los robot de la simulaci�n.";
                break;
            case Defines.ERROR_NO_ROBOT_RECOGNIZED:
                msg = "No se pudo reconocer ning�n robot.";
                break;
            case Defines.ERROR_NO_ICON_RECOGNIZED:
                msg = "No se pudo reconocer ning�n �cono.";
                break;
            case Defines.ERROR_NO_WEBCAM_RECOGNIZED:
                msg = "No se detecto ninguna c�mara conectada.";
                break;
            case Defines.ERROR_WRITING_SIMULATION_XML_FILE:
                msg = "No se pudo guardar el archivo de configuraci�n de la simulaci�n.";
                break;
            case Defines.ERROR_IMAGE_TOO_BIG:
                msg = "La imagen seleccionada es m�s grande que " + Defines.DEFAULT_IMAGE_RESOLUTION[0] + "x" + Defines.DEFAULT_IMAGE_RESOLUTION[1] + ".";
                break;
            case Defines.ERROR_NOT_ALL_ROBOT_PLACED:
                msg = "No se han posicionado todos los robots.";
                break;
            case Defines.ERROR_NO_ROBOT_DEFINED:
                msg = "Debe agregar alg�n robot para ejecutar esta operaci�n.";
                break;
            case Defines.ERROR_IMAGE_IMPROPER_FORMAT:
                msg = "La imagen debe ser con formato JPG o PNG.";
                break;
            case Defines.ERROR_ICON_ID_INVALID:
                msg = "El nombre del �cono coloreado debe ser un n�mero entero.";
                break;
            case Defines.ERROR_NO_PLACES_RESOLVED:
                msg = "No se pudo resolver ning�n lugar en la im�gen (�hay m�s de un �cono coloreado?).";
                break;
            case Defines.ERROR_NO_PLACE_RESOLVED:
                msg = "Alguno de los lugares del laberinto no pudo resolverse (�insuficientes �conos coloreados?).";
                break;
            case Defines.ERROR_NEW_PLUGIN_INSTANCE:
                msg = "No se pudo crear la instancia a partir de la definici�n del plug-in.";
                break;
            case Defines.ERROR_NO_SIMULATION_SELECTED:
                msg = "Debe seleccionar una simulaci�n de la tabla.";
                break;
            case Defines.ERROR_NO_IMAGEFILE_SELECTED:
                msg = "No se ha seleccionado un archivo de imagen para la simulaci�n.";
                break;
            case Defines.ERROR_OPENNING_IMAGE_FILE:
                msg = "No se pudo abrir la imagen indicada.";
                break;
            case Defines.ERROR_BASEDIRECTORY_DONOT_EXISTS:
                msg = "No existe el directorio seleccionado para guardar la simulaci�n.";
                break;
            case Defines.ERROR_NO_COMPORT_SELECTED:
                msg = "Debe seleccionar un puerto.";
                break;
            case Defines.ERROR_SIMULATION_IN_USE:
                msg = "La simulaci�n est� en uso y no puede eliminarse.";
                break;
        }
        return customMessage(msg, ERROR_MESSAGE, parent);
    }

    /**
	 * Muestra en pantalla un di�logo con un mensaje de interrogaci�n (SI/NO).
	 * @param warCode	c�digo del mensaje de interrogaci�n a mostrar.
	 * @param parent	instancia <code>Composite</code> padre.
	 * @return			el ID del bot�n que fue seleccionado para cerrar
	 * 					el di�logo, o <code>Defines.ERROR</code> en caso
	 * 					de error. 
	 */
    public static int warningYesNoMessage(int warCode, Composite parent) {
        if (parent == null) return Defines.ERROR;
        String msg = null;
        switch(warCode) {
            case Defines.WARNING_CHANGE_SIMULATION_TYPE:
                msg = "�Desea cambiar el tipo de simulaci�n?.\n Si responde que SI perder� las configuraciones\n de los robots y GPS que haya hecho.";
                break;
            case Defines.WARNING_DIRECTORY_USED_BY_SIMULATION:
                msg = "El directorio seleccionado ya contiene una simulaci�n. �Desea sobreescribirla?";
                break;
            case Defines.WARNING_DELETE_SIMULATION:
                msg = "Se va a eliminar la simulaci�n. �Est� seguro?";
                break;
            case Defines.WARNING_DELETE_NEURONALNET:
                msg = "Se va a quitar la red neuronal del sistema. �Est� seguro?";
                break;
        }
        return customMessage(msg, WARNING_QUESTION, parent);
    }

    /**
	 * Muestra en pantalla un di�logo con un mensaje de advertencia.
	 * @param warCode	c�digo del mensaje de advertencia a mostrar.
	 * @param parent	instancia <code>Composite</code> padre.
	 * @return			el ID del bot�n que fue seleccionado para cerrar
	 * 					el di�logo, o <code>Defines.ERROR</code> en caso
	 * 					de error. 
	 */
    public static int warningMessage(int warCode, Composite parent) {
        if (parent == null) return Defines.ERROR;
        String msg = null;
        switch(warCode) {
            case Defines.WARNING_NO_ICON_RECOGNIZED:
                msg = "No se ha reconocido ning�n �cono coloreado.";
                break;
            case Defines.ERROR_NOT_ALL_ROBOT_RECOGNIZED:
                msg = "No fueron reconocidos todos los robots de la simulaci�n.";
                break;
        }
        return customMessage(msg, WARNING_MESSAGE, parent);
    }

    /**
	 * Muestra en pantalla un di�logo con un mensaje de informaci�n.
	 * @param infoCode	c�digo del mensaje de informaci�n a mostrar.
	 * @param parent	instancia <code>Composite</code> padre.
	 * @return			el ID del bot�n que fue seleccionado para cerrar
	 * 					el di�logo, o <code>Defines.ERROR</code> en caso
	 * 					de error. 
	 */
    public static int infoMessage(int infoCode, Composite parent) {
        if (parent == null) return Defines.ERROR;
        String msg = null;
        switch(infoCode) {
            case Defines.INFO_MAX_VIRTUAL_ROBOTS:
                msg = "Solo pueden crearse " + Defines.MAX_VIRTUAL_ROBOTS + " robot(s) virtual(es).";
                break;
        }
        return customMessage(msg, INFO_MESSAGE, parent);
    }
}
