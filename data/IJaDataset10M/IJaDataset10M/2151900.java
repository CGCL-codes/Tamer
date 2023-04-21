package mx.ipn.pruebas;

import java.util.ArrayList;
import mx.ipn.persistencia.FabricaDeDAOs;
import mx.ipn.persistencia.dao.*;
import mx.ipn.to.*;

public class PruebaDAOs27 {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        FabricaDeDAOs fabricaMySQL = FabricaDeDAOs.getFabricaDeDAOs(mx.ipn.Constantes.FABRICA_DAOS_MYSQL);
        short resultado;
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<TARJETACLIENTE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            TarjetaClienteDAO tarjetaClienteDAO = fabricaMySQL.getTarjetaClienteDAO();
            TarjetaClienteTO tarjetaClienteTO = new TarjetaClienteTO();
            tarjetaClienteTO.setIdCliente(1);
            tarjetaClienteTO.setIdTarjeta(3);
            tarjetaClienteTO.setActivo(true);
            if (tarjetaClienteDAO.insertTarjetaCliente(tarjetaClienteTO)) {
                tarjetaClienteDAO.insertTarjetaCliente(tarjetaClienteTO);
                System.out.println("+++++++++++++++++++ Insertado correctamente");
            } else System.out.println("Falla en la insercion");
            resultado = tarjetaClienteDAO.ActivaDesactivaTarjetaCliente(1, false);
            if (resultado == 1) System.out.println(">>>>>>>>>>>>>>>>>>>>>desactivado correctamente"); else if (resultado == 0) System.out.println("Falla en el desactivado"); else if (resultado == -1) System.out.println("Ocurrio una excepcion al desactivar");
            tarjetaClienteTO.setIdTarjetaCliente(4);
            tarjetaClienteTO.setIdCliente(1);
            resultado = tarjetaClienteDAO.updateTarjetaCliente(tarjetaClienteTO);
            if (resultado == 1) System.out.println(">>>>>>>>>>>>>>>>>>> Actualizado correctamente"); else if (resultado == 0) System.out.println("Falla en la actualizacion"); else if (resultado == -1) System.out.println("Ocurrio una excepcion");
            System.out.println("------------------------- BUSQUEDA POR ID");
            tarjetaClienteTO = tarjetaClienteDAO.findTarjetaClienteById(1);
            if (tarjetaClienteTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("TARJETACLIENTE");
                ImprimirTO.Imprime(tarjetaClienteTO);
                for (int x = 0; x < tarjetaClienteTO.getListClienteTO().length; x++) {
                    System.out.println("CLIENTE");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListClienteTO()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListTarjetaTO().length; x++) {
                    System.out.println("TARJETA");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListTarjetaTO()[x]);
                }
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("--------------------  BUSQUEDA POR IDCLIENTE IDTARJETA");
            tarjetaClienteTO = tarjetaClienteDAO.findTarjetaClienteByIdClienteIdTarjeta(1, 1);
            if (tarjetaClienteTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("TARJETACLIENTE");
                ImprimirTO.Imprime(tarjetaClienteTO);
                for (int x = 0; x < tarjetaClienteTO.getListClienteTO().length; x++) {
                    System.out.println("CLIENTE");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListClienteTO()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListTarjetaTO().length; x++) {
                    System.out.println("TARJETA");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListTarjetaTO()[x]);
                }
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("**++++++++++++++++++++++ SELECT TARJETACLIENTE +**");
            java.util.Collection<TarjetaClienteTO> listTarjetaClienteTO = tarjetaClienteDAO.selectTarjetaCliente();
            for (int i = 0; i < listTarjetaClienteTO.size(); i++) {
                tarjetaClienteTO = ((ArrayList<TarjetaClienteTO>) listTarjetaClienteTO).get(i);
                System.out.println("TARJETACLIENTE");
                ImprimirTO.Imprime(tarjetaClienteTO);
                for (int x = 0; x < tarjetaClienteTO.getListClienteTO().length; x++) {
                    System.out.println("CLIENTE");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListClienteTO()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListTarjetaTO().length; x++) {
                    System.out.println("TARJETA");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListTarjetaTO()[x]);
                }
            }
            System.out.println("**++++++++++++++++++++++ SELECT TARJETACLIENTE ACTIVO+**");
            listTarjetaClienteTO = tarjetaClienteDAO.selectTarjetaClienteActivo();
            for (int i = 0; i < listTarjetaClienteTO.size(); i++) {
                tarjetaClienteTO = ((ArrayList<TarjetaClienteTO>) listTarjetaClienteTO).get(i);
                System.out.println("TARJETACLIENTE");
                ImprimirTO.Imprime(tarjetaClienteTO);
                for (int x = 0; x < tarjetaClienteTO.getListClienteTO().length; x++) {
                    System.out.println("CLIENTE");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListClienteTO()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListTarjetaTO().length; x++) {
                    System.out.println("TARJETA");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListTarjetaTO()[x]);
                }
            }
            System.out.println("------------------- BUSQUEDA POR ID CLIENTE");
            tarjetaClienteTO = tarjetaClienteDAO.selectTarjetaClienteByCliente(2);
            if (tarjetaClienteTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("TARJETACLIENTE");
                for (int x = 0; x < tarjetaClienteTO.getListIdTarjetaCliente().length; x++) {
                    System.out.println("IDTARJETACLIENTE");
                    System.out.println(tarjetaClienteTO.getListIdTarjetaCliente()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListActivos().length; x++) {
                    System.out.println("ACTIVOS");
                    System.out.println(tarjetaClienteTO.getListActivos()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListTarjetaTO().length; x++) {
                    System.out.println("TARJETA");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListTarjetaTO()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListClienteTO().length; x++) {
                    System.out.println("CLIENTE");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListClienteTO()[x]);
                }
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("------------------- BUSQUEDA POR ID TARJETA");
            tarjetaClienteTO = tarjetaClienteDAO.selectTarjetaClienteByTarjeta(3);
            if (tarjetaClienteTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("TARJETACLIENTE");
                for (int x = 0; x < tarjetaClienteTO.getListIdTarjetaCliente().length; x++) {
                    System.out.println("IDTARJETACLIENTE");
                    System.out.println(tarjetaClienteTO.getListIdTarjetaCliente()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListActivos().length; x++) {
                    System.out.println("ACTIVOS");
                    System.out.println(tarjetaClienteTO.getListActivos()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListClienteTO().length; x++) {
                    System.out.println("CLIENTE");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListClienteTO()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListTarjetaTO().length; x++) {
                    System.out.println("TARJETA");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListTarjetaTO()[x]);
                }
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("------------------- BUSQUEDA POR ID CLIENTE ACTIVO");
            tarjetaClienteTO = tarjetaClienteDAO.selectTarjetaClienteActivoByCliente(2);
            if (tarjetaClienteTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("TARJETACLIENTE");
                for (int x = 0; x < tarjetaClienteTO.getListIdTarjetaCliente().length; x++) {
                    System.out.println("IDTARJETACLIENTE");
                    System.out.println(tarjetaClienteTO.getListIdTarjetaCliente()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListActivos().length; x++) {
                    System.out.println("ACTIVOS");
                    System.out.println(tarjetaClienteTO.getListActivos()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListTarjetaTO().length; x++) {
                    System.out.println("TARJETA");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListTarjetaTO()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListClienteTO().length; x++) {
                    System.out.println("CLIENTE");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListClienteTO()[x]);
                }
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("------------------- BUSQUEDA POR ID TARJETA ACTIVO");
            tarjetaClienteTO = tarjetaClienteDAO.selectTarjetaClienteActivoByTarjeta(1);
            if (tarjetaClienteTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("TARJETACLIENTE");
                for (int x = 0; x < tarjetaClienteTO.getListIdTarjetaCliente().length; x++) {
                    System.out.println("IDTARJETACLIENTE");
                    System.out.println(tarjetaClienteTO.getListIdTarjetaCliente()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListActivos().length; x++) {
                    System.out.println("ACTIVOS");
                    System.out.println(tarjetaClienteTO.getListActivos()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListClienteTO().length; x++) {
                    System.out.println("CLIENTE");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListClienteTO()[x]);
                }
                for (int x = 0; x < tarjetaClienteTO.getListTarjetaTO().length; x++) {
                    System.out.println("TARJETA");
                    ImprimirTO.Imprime(tarjetaClienteTO.getListTarjetaTO()[x]);
                }
            } else {
                System.out.println("Falla en la seleccion");
            }
        } catch (Exception e) {
            System.out.println("Ocurrio una excepcion");
        }
    }
}
