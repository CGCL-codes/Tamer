package controlador;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import misc.Validador;
import vista.*;

public class ControladorBajaAlumno implements ActionListener {

    private static ControladorBajaAlumno controlador;

    private BajaAlumno vista;

    public ControladorBajaAlumno(BajaAlumno view) {
        vista = view;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == vista.getBuscarAlumno()) {
            boolean hayError = false;
            if (!Validador.isNumeric(vista.getLegajo())) {
                hayError = true;
                vista.setLegajo("");
            }
            if (vista.getNombre().equalsIgnoreCase("")) hayError = true;
            if (!hayError) {
                List<Alumno> alus = new ArrayList<Alumno>();
                alus = sistema.buscarAlumnos(vista.getLegajo(), vista.getNombre());
                if (alus != null) ;
            }
        }
    }
}
