package event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import controlador.Sistema;
import misc.Validador;
import vista.FormAlumno;
import vo.AlumnoVo;
import exceptions.AgregarObjetoException;
import exceptions.EliminarObjetoException;

public class EventAlumnoHandler implements ActionListener {

    private FormAlumno vista;

    public EventAlumnoHandler(FormAlumno view) {
        vista = view;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == vista.getAltaAlumno()) {
            boolean hayError = false;
            if (!Validador.isNumeric(vista.getLegajo().getText())) {
                hayError = true;
                JOptionPane.showMessageDialog(null, "El campo Legajo es invalido.", "Error", JOptionPane.WARNING_MESSAGE);
                vista.getLegajo().setText("");
                vista.getLegajo().grabFocus();
            }
            if (!hayError) {
                try {
                    AlumnoVo alumnoVo = new AlumnoVo(Integer.parseInt(vista.getLegajo().getText()), vista.getNombre().getText(), vista.getOption());
                    Sistema.getInstancia().agregarAlumno(alumnoVo);
                    JOptionPane.showMessageDialog(null, "La operacion fue realizada exitosamente.", "Mensaje", JOptionPane.WARNING_MESSAGE);
                    vista.getAlumno().addItem(vista.getLegajo().getText() + " - " + vista.getNombre().getText());
                    vista.getLegajo().setText("");
                    vista.getNombre().setText("");
                    vista.getjRadioButtonInactivo().setSelected(true);
                    vista.getAlumno().setSelectedIndex(0);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Dato incorrecto.", "Error", JOptionPane.WARNING_MESSAGE);
                } catch (AgregarObjetoException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                    vista.getLegajo().setText("");
                    vista.getLegajo().grabFocus();
                }
            }
        } else if (source == vista.getBajaAlumno()) {
            if (" ".equalsIgnoreCase((String) vista.getAlumno().getSelectedItem())) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un Alumno", "Error en Baja de Alumno", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                AlumnoVo alumnoVo = new AlumnoVo(Integer.parseInt(vista.getLegajo().getText()), vista.getNombre().getText(), vista.getOption());
                Sistema.getInstancia().eliminarAlumno(alumnoVo);
                JOptionPane.showMessageDialog(null, "La operacion fue realizada exitosamente.", "Mensaje", JOptionPane.WARNING_MESSAGE);
                String item = vista.getLegajo().getText() + " - " + vista.getNombre().getText();
                for (int i = 0; i < vista.getAlumno().getItemCount(); i++) {
                    if (item.equalsIgnoreCase((String) vista.getAlumno().getItemAt(i))) {
                        vista.getAlumno().removeItemAt(i);
                        break;
                    }
                }
                vista.getLegajo().setText("");
                vista.getNombre().setText("");
                vista.getjRadioButtonInactivo().setSelected(true);
                vista.getAlumno().setSelectedIndex(0);
            } catch (EliminarObjetoException ex) {
                JOptionPane.showMessageDialog(null, "No es posible eliminar el Alumno", "Error en Baja de Alumno", JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == vista.getAlumno()) {
            AlumnoVo alumnoVo = null;
            if (vista.getAlumno().getSelectedIndex() == 0) {
                vista.getLegajo().setText("");
                vista.getNombre().setText("");
            } else {
                vista.setAlumnos(Sistema.getInstancia().getAlumnosVo());
                int idxAlumno = vista.getAlumno().getSelectedIndex() - 1;
                alumnoVo = vista.getAlumnos().get(idxAlumno);
                vista.getLegajo().setText(String.valueOf(alumnoVo.getLegajo()));
                vista.getNombre().setText(alumnoVo.getNombre());
                if ("activo".equalsIgnoreCase(alumnoVo.getEstado())) {
                    vista.getjRadioButtonActivo().setSelected(true);
                } else {
                    vista.getjRadioButtonInactivo().setSelected(true);
                }
            }
        } else if (source == vista.getjRadioButtonInactivo()) {
            vista.setOption("inactivo");
        } else if (source == vista.getjRadioButtonActivo()) {
            vista.setOption("activo");
        }
    }
}
