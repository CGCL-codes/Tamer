package tp2.modelo.menues.menuCreditos;

import tp2.control.menues.menuCreditos.ControlKeyPressMenuCreditos;
import tp2.modelo.menues.Menu;
import tp2.modelo.menues.MenuI;
import tp2.vista.menues.menuCreditos.VistaMenuCreditos;
import tp2.vista.ventanas.VentanaPrincipal;

public class MenuCreditos extends Menu {

    public MenuCreditos(VentanaPrincipal ventanaPrincipal, MenuI menuPadre) {
        super(ventanaPrincipal, menuPadre);
        this.setVistaMenu(new VistaMenuCreditos());
        this.getVistaMenu().setPosicionable(this);
    }

    @Override
    public void resetear() {
    }

    @Override
    public void activarControl() {
        this.setControlKeyPress(new ControlKeyPressMenuCreditos(this));
        this.getVentanaPrincipal().setControlKeyPressActivo(this.getControlKeyPress());
    }

    @Override
    public void realizarAlOcultar() {
    }

    @Override
    public void realizarAlMostrar() {
    }
}
