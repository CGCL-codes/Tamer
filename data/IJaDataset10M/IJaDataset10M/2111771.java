package util.editor;

/**
 * Una clase interesada en recibir eventos asociado
 * a un editor, debe implementar esta interface.
 *
 * @author Carlos Rueda
 */
public interface EditorListener {

    /**
	 * Avisa que el contenido del archivo actual en
	 * edici�n acaba de ser modificado por el usuario.
	 */
    public void estaModificado();
}
