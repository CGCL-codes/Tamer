package ws;

import java.util.ArrayList;
import java.util.Collection;
import org.orm.PersistentException;
import org.orm.PersistentTransaction;
import SOAPVO.EmpleadoSOAPVO;
import SOAPVO.SolicitudSOAPVO;
import validaciones.Validaciones;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.orm.PersistentException;
import org.orm.PersistentTransaction;

/**
 * @author chos3n
 *
 */
public class DepartamentoSOA {

    public String AgregarDepto(String id_depto, String nombre, String director, double T_aprobacion) throws PersistentException {
        String message = "";
        String resultado = "";
        ;
        Validaciones v = new Validaciones();
        if (v.equals(id_depto) == false || v.equals(id_depto) == false || (nombre == null || nombre.equals("")) || v.equals(director) == false || (director == null || director.equals("")) || v.soloNumerosD(T_aprobacion) == false) {
            message = "Datos incorrectos";
            System.out.println("id_depto: " + id_depto);
            System.out.println("nombre depto: " + nombre);
            System.out.println("director: " + director);
            System.out.println("t de aprobacion: " + T_aprobacion);
        } else {
            try {
                PersistentTransaction t = orm.SolicitudEmpleadoPersistentManager.instance().getSession().beginTransaction();
                orm.DAOFactory lDAOFactory = orm.DAOFactory.getDAOFactory();
                orm.dao.SA_DepartamentoDAO deptoDAO = lDAOFactory.getSA_DepartamentoDAO();
                orm.SA_Departamento objOrmdepto = deptoDAO.createSA_Departamento();
                orm.SA_Departamento ormDepto = deptoDAO.loadSA_DepartamentoByQuery("id_depto='" + id_depto + "'", null);
                objOrmdepto.setId_depto(id_depto);
                objOrmdepto.setNombre(nombre);
                objOrmdepto.setDirector(director);
                objOrmdepto.setT_aprobacion(T_aprobacion);
                deptoDAO.save(objOrmdepto);
                resultado = "consulta exitosa.";
                t.commit();
            } catch (PersistentException e) {
                e.printStackTrace();
                resultado = "<ERROR>";
            }
        }
        message = resultado + "\n se ha agregado una nuevo departamento ";
        return message;
    }
}
