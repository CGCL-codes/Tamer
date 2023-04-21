package Datos;

import Logica.Aniodetrabajodeobra;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class AniodetrabajodeobraDAO extends DAO {

    public AniodetrabajodeobraDAO() {
        super();
    }

    public boolean Insertar(Aniodetrabajodeobra anTrab) {
        String call = "{CALL sp_aniotrabajodeobra_Insertar(?,?,?,?)}";
        try {
            ejecutaSP(call);
            cl.setInt(1, 0);
            cl.setInt(2, anTrab.getAnio());
            cl.setString(3, String.valueOf(anTrab.getTipodeplanilla().getIdtipodeplanilla()));
            cl.setInt(4, anTrab.getIdobra());
            cl.executeUpdate();
            cl.getInt(1);
            anTrab.setIdaniotrabajodeobra(cl.getInt(1));
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }
}
