package datos;

import com.aufa.elSecreto.UsuarioConfg;
import com.aufa.elSecreto.modelo.dominio.ProductoCantidad;
import com.aufa.elSecreto.datos.ListaPedidos;
import com.aufa.elSecreto.datos.ListaProductoCantidad;
import com.aufa.elSecreto.modelo.dao.impl.DetalleDaoImpl;
import com.aufa.elSecreto.modelo.dao.impl.EgresoDaoImpl;
import com.aufa.elSecreto.modelo.dao.impl.PedidoDaoImpl;
import com.aufa.elSecreto.modelo.dao.impl.ProductoDaoImpl;
import com.aufa.elSecreto.modelo.dao.impl.UsuarioDaoImpl;
import com.aufa.elSecreto.modelo.dominio.Detalle;
import com.aufa.elSecreto.modelo.dominio.Egreso;
import com.aufa.elSecreto.modelo.dominio.Pedido;
import com.aufa.elSecreto.modelo.dominio.Producto;
import com.aufa.elSecreto.modelo.dominio.Usuario;
import com.aufa.elSecreto.modelo.servicios.ServicioPedido;
import com.aufa.elSecreto.modelo.servicios.ServicioResumen;
import com.aufa.elSecreto.modelo.util.Validacion;
import gui.CuadroDialog;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Augusto
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Usuario u = new Usuario("", "Contador", "", "", "");
        System.out.println(u.getTipoCuenta2Integer());
        JOptionPane.showMessageDialog(null, UsuarioConfg.STR_CONTADOR);
        if (u.getTipoCuenta().equals(UsuarioConfg.STR_CONTADOR)) JOptionPane.showMessageDialog(null, "SIIIIIII"); else JOptionPane.showMessageDialog(null, "NOOOOOOOO");
        switch(u.getTipoCuenta2Integer()) {
            case UsuarioConfg.CONTADOR:
                JOptionPane.showMessageDialog(null, "Conta");
                break;
            case UsuarioConfg.CAJERO:
                JOptionPane.showMessageDialog(null, "cajer");
                break;
            case UsuarioConfg.MOZO:
                JOptionPane.showMessageDialog(null, "moz");
                break;
        }
    }
}
