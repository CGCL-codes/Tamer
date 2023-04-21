package mx.ipn.pruebas;

import java.util.ArrayList;
import mx.ipn.persistencia.FabricaDeDAOs;
import mx.ipn.persistencia.dao.*;
import mx.ipn.to.*;

public class PruebaDAOs5 {

    public static void main(String[] args) {
        FabricaDeDAOs fabricaMySQL = FabricaDeDAOs.getFabricaDeDAOs(mx.ipn.Constantes.FABRICA_DAOS_MYSQL);
        short resultado;
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<RFC>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            RfcDAO rfcDAO = fabricaMySQL.getRfcDAO();
            RfcTO rfcTO = new RfcTO(0, "hm1950125kg8", "hsbc mexico sa");
            if (rfcDAO.insertRfc(rfcTO)) {
                rfcDAO.insertRfc(rfcTO);
                System.out.println("Insertado correctamente");
            } else System.out.println("Falla en la insercion");
            resultado = rfcDAO.deleteRfc(2);
            if (resultado == 1) System.out.println("Borrado correctamente"); else if (resultado == 0) System.out.println("Falla en el borrado"); else if (resultado == -1) System.out.println("Ocurrio una excepcion al borrar");
            rfcTO.setIdRfc(3);
            rfcTO.setRegistro("hsbc mexico s a b");
            resultado = rfcDAO.updateRfc(rfcTO);
            if (resultado == 1) System.out.println("Actualizado correctamente"); else if (resultado == 0) System.out.println("Falla en la actualizacion"); else if (resultado == -1) System.out.println("Ocurrio una excepcion");
            System.out.println("BUSQUEDA POR ID");
            rfcTO = rfcDAO.findRfcById(1);
            if (rfcTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("RFC");
                System.out.println("id_rfc: " + rfcTO.getIdRfc());
                System.out.println("registro: " + rfcTO.getRegistro());
                System.out.println("Denemoniacion: " + rfcTO.getDenominacion());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("BUSQUEDA POR REGISTRO");
            rfcTO = rfcDAO.findRfcByRegistro("tme840315kt6");
            if (rfcTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("RFC");
                System.out.println("id_rfc: " + rfcTO.getIdRfc());
                System.out.println("registro: " + rfcTO.getRegistro());
                System.out.println("Denemoniacion: " + rfcTO.getDenominacion());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("BUSQUEDA POR DENOMINACION");
            rfcTO = rfcDAO.findRfcByDenominacion("telefonos de mexico s a b de c v");
            if (rfcTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("RFC");
                System.out.println("id_rfc: " + rfcTO.getIdRfc());
                System.out.println("registro: " + rfcTO.getRegistro());
                System.out.println("Denemoniacion: " + rfcTO.getDenominacion());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("**+SELECT RFC+**");
            ArrayList<RfcTO> listRfcTO = rfcDAO.selectRfc();
            for (int i = 0; i < listRfcTO.size(); i++) {
                rfcTO = listRfcTO.get(i);
                System.out.println("RFC");
                System.out.println("id_rfc: " + rfcTO.getIdRfc());
                System.out.println("registro: " + rfcTO.getRegistro());
                System.out.println("Denemoniacion: " + rfcTO.getDenominacion());
            }
        } catch (Exception e) {
            System.out.println("Ocurrio una excepcion");
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<ESTADO>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            EstadoDAO estadoDAO = fabricaMySQL.getEstadoDAO();
            EstadoTO estadoTO = new EstadoTO((short) 0, "PetroleroLA");
            if (estadoDAO.insertEstado(estadoTO)) {
                estadoDAO.insertEstado(estadoTO);
                System.out.println("Insertado correctamente");
            } else System.out.println("Falla en la insercion");
            resultado = estadoDAO.deleteEstado((short) 15);
            if (resultado == 1) System.out.println("Borrado correctamente"); else if (resultado == 0) System.out.println("Falla en el borrado"); else if (resultado == -1) System.out.println("Ocurrio una excepcion al borrar");
            estadoTO.setIdEstado((short) 2);
            estadoTO.setNombre("nuevo");
            resultado = estadoDAO.updateEstado(estadoTO);
            if (resultado == 1) System.out.println("Actualizado correctamente"); else if (resultado == 0) System.out.println("Falla en la actualizacion"); else if (resultado == -1) System.out.println("Ocurrio una excepcion");
            System.out.println("Busqueda POR ID");
            estadoTO = estadoDAO.findEstadoById((short) 2);
            if (estadoTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("ESTADO");
                System.out.println("id_estado: " + estadoTO.getIdEstado());
                System.out.println("nombre: " + estadoTO.getNombre());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("Busqueda POR NOMBRE");
            estadoTO = estadoDAO.findEstadoByNombre("michoacan");
            if (estadoTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("ESTADO");
                System.out.println("id_estado: " + estadoTO.getIdEstado());
                System.out.println("nombre: " + estadoTO.getNombre());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("**+SELECT ESTADO+**");
            ArrayList<EstadoTO> listEstadoTO = estadoDAO.selectEstado();
            for (int i = 0; i < listEstadoTO.size(); i++) {
                estadoTO = listEstadoTO.get(i);
                System.out.println("ESTADO");
                System.out.println("id_estado: " + estadoTO.getIdEstado());
                System.out.println("nombre: " + estadoTO.getNombre());
            }
        } catch (Exception e) {
            System.out.println("Ocurrio una excepcion");
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<GRUPOUSUARIO>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            GrupoUsuarioDAO grupoUsuarioDAO = fabricaMySQL.getGrupoUsuarioDAO();
            GrupoUsuarioTO grupoUsuarioTO = new GrupoUsuarioTO((short) 0, "maestros", "los mas valiosos");
            if (grupoUsuarioDAO.insertGrupoUsuario(grupoUsuarioTO)) {
                grupoUsuarioDAO.insertGrupoUsuario(grupoUsuarioTO);
                System.out.println("Insertado correctamente");
            } else System.out.println("Falla en la insercion");
            resultado = grupoUsuarioDAO.deleteGrupoUsuario((short) 5);
            if (resultado == 1) System.out.println("Borrado correctamente"); else if (resultado == 0) System.out.println("Falla en el borrado"); else if (resultado == -1) System.out.println("Ocurrio una excepcion al borrar");
            grupoUsuarioTO.setIdGrupoUsuario((short) 6);
            grupoUsuarioTO.setNombre("grandes maestros");
            resultado = grupoUsuarioDAO.updateGrupoUsuario(grupoUsuarioTO);
            if (resultado == 1) System.out.println("Actualizado correctamente"); else if (resultado == 0) System.out.println("Falla en la actualizacion"); else if (resultado == -1) System.out.println("Ocurrio una excepcion");
            System.out.println("BUSQUEDA POR ID");
            grupoUsuarioTO = grupoUsuarioDAO.findGrupoUsuarioById((short) 1);
            if (grupoUsuarioTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("GRUPOUSUARIO");
                System.out.println("id_grupoUsuario: " + grupoUsuarioTO.getIdGrupoUsuario());
                System.out.println("nombre: " + grupoUsuarioTO.getNombre());
                System.out.println("descripcion: " + grupoUsuarioTO.getDescripcion());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("BUSQUEDA POR NOMBRE");
            grupoUsuarioTO = grupoUsuarioDAO.findGrupoUsuarioByNombre("administrador");
            if (grupoUsuarioTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("GRUPOUSUARIO");
                System.out.println("id_grupoUsuario: " + grupoUsuarioTO.getIdGrupoUsuario());
                System.out.println("nombre: " + grupoUsuarioTO.getNombre());
                System.out.println("descripcion: " + grupoUsuarioTO.getDescripcion());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("**+SELECT GRUPOUSUARIO+**");
            java.util.Collection<GrupoUsuarioTO> listGrupoUsuarioTO = grupoUsuarioDAO.selectGrupoUsuario();
            for (int i = 0; i < listGrupoUsuarioTO.size(); i++) {
                grupoUsuarioTO = ((ArrayList<GrupoUsuarioTO>) listGrupoUsuarioTO).get(i);
                System.out.println("GRUPOUSUARIO");
                System.out.println("id_grupoUsuario: " + grupoUsuarioTO.getIdGrupoUsuario());
                System.out.println("nombre: " + grupoUsuarioTO.getNombre());
                System.out.println("descripcion: " + grupoUsuarioTO.getDescripcion());
            }
        } catch (Exception e) {
            System.out.println("Ocurrio una excepcion");
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<TIPOEMPLEADO>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            TipoEmpleadoDAO tipoEmpleadoDAO = fabricaMySQL.getTipoEmpleadoDAO();
            TipoEmpleadoTO tipoEmpleadoTO = new TipoEmpleadoTO((short) 0, "Temporal", "Contratadis por temporada especial");
            if (tipoEmpleadoDAO.insertTipoEmpleado(tipoEmpleadoTO)) {
                tipoEmpleadoDAO.insertTipoEmpleado(tipoEmpleadoTO);
                System.out.println("Insertado correctamente");
            } else System.out.println("Falla en la insercion");
            resultado = tipoEmpleadoDAO.deleteTipoEmpleado((short) 5);
            if (resultado == 1) System.out.println("Borrado correctamente"); else if (resultado == 0) System.out.println("Falla en el borrado"); else if (resultado == -1) System.out.println("Ocurrio una excepcion al borrar");
            tipoEmpleadoTO.setIdTipoEmpleado((short) 6);
            tipoEmpleadoTO.setNombre("por temporada");
            resultado = tipoEmpleadoDAO.updateTipoEmpleado(tipoEmpleadoTO);
            if (resultado == 1) System.out.println("Actualizado correctamente"); else if (resultado == 0) System.out.println("Falla en la actualizacion"); else if (resultado == -1) System.out.println("Ocurrio una excepcion");
            System.out.println("BUSQUEDA POR ID");
            tipoEmpleadoTO = tipoEmpleadoDAO.findTipoEmpleadoById((short) 1);
            if (tipoEmpleadoTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("TIPOEMPLEADO");
                System.out.println("id_tipoEmpleado: " + tipoEmpleadoTO.getIdTipoEmpleado());
                System.out.println("nombre: " + tipoEmpleadoTO.getNombre());
                System.out.println("descripcion: " + tipoEmpleadoTO.getDescripcion());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("BUSQUEDA POR NOMBRE");
            tipoEmpleadoTO = tipoEmpleadoDAO.findTipoEmpleadoByNombre("administrador");
            if (tipoEmpleadoTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("TIPOEMPLEADO");
                System.out.println("id_tipoEmpleado: " + tipoEmpleadoTO.getIdTipoEmpleado());
                System.out.println("nombre: " + tipoEmpleadoTO.getNombre());
                System.out.println("descripcion: " + tipoEmpleadoTO.getDescripcion());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("**+SELECT TIPOEMPLEADO+**");
            java.util.Collection<TipoEmpleadoTO> listTipoEmpleadoTO = tipoEmpleadoDAO.selectTipoEmpleado();
            for (int i = 0; i < listTipoEmpleadoTO.size(); i++) {
                tipoEmpleadoTO = ((ArrayList<TipoEmpleadoTO>) listTipoEmpleadoTO).get(i);
                System.out.println("TIPOEMPLEADO");
                System.out.println("id_tipoEmpleado: " + tipoEmpleadoTO.getIdTipoEmpleado());
                System.out.println("nombre: " + tipoEmpleadoTO.getNombre());
                System.out.println("descripcion: " + tipoEmpleadoTO.getDescripcion());
            }
        } catch (Exception e) {
            System.out.println("Ocurrio una excepcion");
        }
    }
}
