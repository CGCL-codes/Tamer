package event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import vista.AsocAlumnoCurso;
import vo.AlumnoVo;
import controlador.Sistema;
import exceptions.AgregarObjetoException;

public class EventAsocAlumnoCursoHandler implements ActionListener {

    private AsocAlumnoCurso vista;

    public EventAsocAlumnoCursoHandler(AsocAlumnoCurso view) {
        vista = view;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == vista.getAsociar()) {
            if (" ".equalsIgnoreCase((String) vista.getAlumno().getSelectedItem())) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un alumno", "Error en asociar el alumno al curso", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (" ".equalsIgnoreCase((String) vista.getCurso().getSelectedItem())) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un curso", "Error en asociar el alumno al curso", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idxAlumno = vista.getAlumno().getSelectedIndex() - 1;
            vista.setAlumnoVo(vista.getAlumnos().get(idxAlumno));
            int idxCurso = vista.getCurso().getSelectedIndex() - 1;
            vista.setCursoVo(vista.getCursos().get(idxCurso));
            try {
                Sistema.getInstancia().asignarAlumnoCurso(vista.getCursoVo(), vista.getAlumnoVo());
                JOptionPane.showMessageDialog(null, "Se agreg� el alumno al curso.", "Mensaje", JOptionPane.WARNING_MESSAGE);
                vista.getAlumno().setSelectedIndex(0);
                vista.setInscriptos(Sistema.getInstancia().getInscriptos(vista.getCursoVo()));
                Vector<String> columnas = new Vector<String>();
                columnas.add("Legajo");
                columnas.add("Nombre");
                vista.setjTable1Model(new DefaultTableModel(columnas, 0));
                vista.getjTable().setModel(vista.getjTable1Model());
                for (int i = 0; i < vista.getInscriptos().size(); i++) {
                    Object[] num = {};
                    vista.getjTable1Model().addRow(num);
                    vista.getjTable().setValueAt(vista.getInscriptos().get(i).getLegajo(), i, 0);
                    vista.getjTable().setValueAt(vista.getInscriptos().get(i).getNombre(), i, 1);
                }
                TableColumn column = null;
                column = vista.getjTable().getColumnModel().getColumn(0);
                column.setPreferredWidth(120);
                column = vista.getjTable().getColumnModel().getColumn(1);
                column.setPreferredWidth(140);
            } catch (AgregarObjetoException ex) {
                JOptionPane.showMessageDialog(null, "No es posible asociar el alumno al curso", "Error en asociar el alumno al curso", JOptionPane.ERROR_MESSAGE);
                vista.getAlumno().setSelectedIndex(0);
                vista.getCurso().setSelectedIndex(0);
            }
        } else if (source == vista.getCurso()) {
            if (vista.getCurso().getSelectedIndex() == 0) {
                vista.setInscriptos(new ArrayList<AlumnoVo>());
                vista.getAlumno().setEnabled(false);
            } else {
                int idxCurso = vista.getCurso().getSelectedIndex() - 1;
                vista.setCursoVo(vista.getCursos().get(idxCurso));
                vista.setInscriptos(Sistema.getInstancia().getInscriptos(vista.getCursoVo()));
                vista.getAlumno().setEnabled(true);
            }
            Vector<String> columnas = new Vector<String>();
            columnas.add("Legajo");
            columnas.add("Nombre");
            vista.setjTable1Model(new DefaultTableModel(columnas, 0));
            vista.getjTable().setModel(vista.getjTable1Model());
            for (int i = 0; i < vista.getInscriptos().size(); i++) {
                Object[] num = {};
                vista.getjTable1Model().addRow(num);
                vista.getjTable().setValueAt(vista.getInscriptos().get(i).getLegajo(), i, 0);
                vista.getjTable().setValueAt(vista.getInscriptos().get(i).getNombre(), i, 1);
            }
            TableColumn column = null;
            column = vista.getjTable().getColumnModel().getColumn(0);
            column.setPreferredWidth(120);
            column = vista.getjTable().getColumnModel().getColumn(1);
            column.setPreferredWidth(140);
        }
    }
}
