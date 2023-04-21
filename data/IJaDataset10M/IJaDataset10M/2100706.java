package controlador;

import java.util.ArrayList;
import vista.VentanaTablaPosiciones;
import vista.VentanaTablaPosicionesModeloTabla;
import modelo.Liga;
import modelo.Equipo;

public class ControladorVentanaTablaPosiciones {

    private VentanaTablaPosiciones ventanaTablaPosiciones;

    private Liga liga;

    public ControladorVentanaTablaPosiciones(Liga liga) {
        super();
        ventanaTablaPosiciones = new VentanaTablaPosiciones();
        ventanaTablaPosiciones.setLocationRelativeTo(null);
        ventanaTablaPosiciones.setVisible(true);
        this.liga = liga;
        this.cargarListado();
    }

    private void cargarListado() {
        if (liga.cantidadPartidos() > 0) {
            ArrayList<Equipo> equipos = liga.GenerarTablaResultados();
            ArrayList<String> dif = new ArrayList<String>();
            for (int i = 0; i < equipos.size(); i++) if (i != 0) dif.add(String.valueOf(((equipos.get(0).getJuegos_g() - equipos.get(i).getJuegos_g()) + (equipos.get(i).getJuegos_p() - equipos.get(0).getJuegos_p()) / 2))); else dif.add("--");
            ventanaTablaPosiciones.setTabla(new VentanaTablaPosicionesModeloTabla(equipos, dif));
        } else ventanaTablaPosiciones.mostrarMensaje("Primero debe generar partidos.");
    }
}
