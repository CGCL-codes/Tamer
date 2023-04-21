package componente.usuario.control;

/**
 * @author Henrick Daniel
 *
 */
public class CancelarEdicaoCommand {

    /**
	 * 
	 */
    public CancelarEdicaoCommand() {
        super();
        try {
            UsuarioControl.panelUsuario.remove(UsuarioControl.panelUsuario.p2);
            UsuarioControl.panelUsuario.remove(UsuarioControl.panelUsuario.buttons2);
            UsuarioControl.panelUsuario.validate();
            UsuarioControl.panelUsuario.repaint();
            UsuarioControl.panelUsuario.add(UsuarioControl.panelUsuario.p1);
            UsuarioControl.panelUsuario.add(UsuarioControl.panelUsuario.buttons1);
        } catch (Exception e4) {
        }
        UsuarioControl.panelUsuario.validate();
        UsuarioControl.panelUsuario.repaint();
    }
}
