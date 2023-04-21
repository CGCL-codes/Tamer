package iuTaxSimula;

import java.util.ArrayList;
import java.util.Calendar;
import mnTaxSimula.*;

public class AsignarServicio {

    private CentralComunicaciones centralComunicaciones;

    private Simulacion simulacion;

    private Usuario usuario;

    private PantallaTaxi pantallaTaxi;

    private PantallaAsignacion pantallaAsignacion;

    private ArrayList<PlacasServicios> taxiscercanos;

    public AsignarServicio(CentralComunicaciones centralComunicaciones, Simulacion simulacion, Usuario usuario) {
        this.centralComunicaciones = centralComunicaciones;
        this.simulacion = simulacion;
        this.usuario = usuario;
    }
}
