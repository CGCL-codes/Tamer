package componente.usuario.control;

/**
 * @author Henrick Daniel
 *
 */
public class MudancaComboTipoUsuario {

    /**
	 * 
	 */
    public MudancaComboTipoUsuario() {
        super();
        if (UsuarioControl.panelUsuario.tipo.getSelectedItem().toString().equals("M�DICO")) {
            try {
                UsuarioControl.panelUsuario.crm.setEnabled(true);
                UsuarioControl.panelUsuario.consultorio.setEnabled(true);
                UsuarioControl.panelUsuario.tempoCons.setEnabled(true);
                UsuarioControl.panelUsuario.end.setEnabled(true);
                UsuarioControl.panelUsuario.cidade.setEnabled(true);
                UsuarioControl.panelUsuario.estado.setEnabled(true);
            } catch (Exception e1) {
            }
            UsuarioControl.panelUsuario.validate();
            UsuarioControl.panelUsuario.repaint();
        }
        if (UsuarioControl.panelUsuario.tipo.getSelectedItem().toString().equals("SECRETARIA")) {
            try {
                UsuarioControl.panelUsuario.crm.setEnabled(false);
                UsuarioControl.panelUsuario.consultorio.setEnabled(false);
                UsuarioControl.panelUsuario.tempoCons.setEnabled(false);
                UsuarioControl.panelUsuario.end.setEnabled(false);
                UsuarioControl.panelUsuario.cidade.setEnabled(false);
                UsuarioControl.panelUsuario.estado.setEnabled(false);
            } catch (Exception e2) {
            }
            UsuarioControl.panelUsuario.validate();
            UsuarioControl.panelUsuario.repaint();
        }
    }
}
