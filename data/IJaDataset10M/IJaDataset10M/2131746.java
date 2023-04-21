package es.addlink.tutormates.equationEditor.Exceptions;

/**
 * Captura todas las excepciones producidas en las clases del paquete Manager (motor del editor).
 * 
 * @author Ignacio Celaya Sesma
 */
public class ManagerEditorException extends EditorException {

    static final long serialVersionUID = 0;

    public ManagerEditorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ManagerEditorException(Throwable arg0) {
        super(arg0);
    }
}
