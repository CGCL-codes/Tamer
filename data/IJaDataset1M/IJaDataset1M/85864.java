package modelo;

import java.sql.SQLException;
import java.util.*;
import BD.CitasBD;
import Interfaz.Principal;

public class GestorCitas {

    private static Principal principal;

    private static CitasBD bd;

    public static void init(Principal p, CitasBD baseDatos) {
        principal = p;
        bd = baseDatos;
    }

    public static ArrayList<Cita> getCitas(String rol, String dni) {
        ArrayList<Cita> citas = new ArrayList<Cita>();
        citas = bd.consultar_citas(rol, dni);
        return citas;
    }

    public static boolean comprobarCita(String dni_paciente) {
        return bd.comprobarCita(dni_paciente);
    }

    /**
	 * Anula una cita de un paciente por el dni.
	 * @param Paciente.
	 * @return true si se ha anulado con �xito
	 * @return false en caso contrario
	 * @author Antonio Miguel Fern�ndez G�mez
	 */
    public static boolean AnularCita(String dni, String fecha) {
        return bd.anular_cita(dni, fecha);
    }

    public static void creaCita(Cita c, String tipo) {
        bd.add_cita(c, tipo);
    }

    public static boolean consultarCita(String dni, String fecha, String hora, String tipo) {
        return bd.comprobarCita(dni, fecha, hora, tipo);
    }

    public static ArrayList<Cita> consultar_citas(String tipo, String dni) {
        return bd.consultar_citas(tipo, dni);
    }

    public static ArrayList<String> consultarCitasLibres(String tipo, String fecha) {
        return bd.consultar_citas_libres(tipo, fecha);
    }

    public static ArrayList<ArrayList<String>> consultarTurno(String dia, String dniMedico) {
        return bd.consultar_turno(dia, dniMedico);
    }

    public static ArrayList<ArrayList<String>> consultarHorarios(String tipo, String fecha) {
        return bd.consultarHorarios(tipo, fecha);
    }
}
