package arqueologia.model.Comandos;

import arqueologia.controller.GestorEquipos;
import arqueologia.controller.GestorRegla;
import arqueologia.model.Cubo;
import arqueologia.model.EquipoCubo;
import arqueologia.view.juego.Juego;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ComandoExcavarTodos extends Comando {

    private static final String nombre = "Excavar todas las áreas";

    private static ImageIcon icon;

    public ComandoExcavarTodos(Cubo cubo) {
        super(cubo);
    }

    public static void loadIcon() throws Exception {
        if (icon == null) {
            File file = new File(GestorRegla.getRegla("RutaImagenComandoExcavarTodos"));
            Image icono = ImageIO.read(file);
            icon = new ImageIcon(icono);
        }
    }

    protected boolean ejecutar() {
        GestorEquipos equipos = Juego.getDirector().getGestorEquipos();
        boolean flag = true;
        for (int i = 0; i < equipos.size(); i++) {
            EquipoCubo equipoCubo = equipos.getEquipo(i).getEquipoCubo();
            if (equipoCubo != null) {
                equipoCubo.excavar();
            } else {
                flag = false;
            }
        }
        return flag;
    }

    public static Image getIcon() {
        return icon.getImage();
    }

    public static String getNombre() {
        return nombre;
    }
}
