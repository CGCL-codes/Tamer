package mx.ipn.pruebas;

import java.util.ArrayList;
import mx.ipn.persistencia.FabricaDeDAOs;
import mx.ipn.persistencia.dao.*;
import mx.ipn.to.*;

public class PruebaDAOs17 {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        FabricaDeDAOs fabricaMySQL = FabricaDeDAOs.getFabricaDeDAOs(mx.ipn.Constantes.FABRICA_DAOS_MYSQL);
        short resultado;
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<UNIDAD>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            EconomicoTO economicoTO = new EconomicoTO();
            economicoTO.setIdEconomico(1);
            SubmarcaTO submarcaTO = new SubmarcaTO();
            submarcaTO.setIdSubmarca(1);
            ColorAutoTO colorAutoTO = new ColorAutoTO();
            colorAutoTO.setIdColorAuto((short) 1);
            UnidadDAO unidadDAO = fabricaMySQL.getUnidadDAO();
            UnidadTO unidadTO = new UnidadTO(0, "l3456s", 2008, economicoTO, submarcaTO, colorAutoTO, true);
            if (unidadDAO.insertUnidad(unidadTO)) {
                unidadDAO.insertUnidad(unidadTO);
                System.out.println("+++++++++++++++++++ Insertado correctamente");
            } else System.out.println("Falla en la insercion");
            resultado = unidadDAO.activaDesactivaUnidad(4, false);
            if (resultado == 1) System.out.println(">>>>>>>>>>>>>>>>>>> Desactivado correctamente"); else if (resultado == 0) System.out.println("Falla en el Desactivado"); else if (resultado == -1) System.out.println("Ocurrio una excepcion");
            unidadTO.setIdUnidad(5);
            unidadTO.setActivo(true);
            resultado = unidadDAO.updateUnidad(unidadTO);
            if (resultado == 1) System.out.println(">>>>>>>>>>>>>>>>>>> Actualizado correctamente"); else if (resultado == 0) System.out.println("Falla en la actualizacion"); else if (resultado == -1) System.out.println("Ocurrio una excepcion");
            System.out.println("------------------------- BUSQUEDA POR ID");
            unidadTO = unidadDAO.findUnidadById(1);
            if (unidadTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("UNIDAD");
                ImprimirTO.Imprime(unidadTO);
                System.out.println("ECONOMICO");
                ImprimirTO.Imprime(unidadTO.getEconomicoTO());
                System.out.println("SUBMARCA");
                ImprimirTO.Imprime(unidadTO.getSubmarcaTO());
                System.out.println("COLORAUTO");
                ImprimirTO.Imprime(unidadTO.getColorAutoTO());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("------------------------- BUSQUEDA POR PLACAS ACTIVAS");
            unidadTO = unidadDAO.findUnidadActivoByPlacas("l3456s");
            if (unidadTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("UNIDAD");
                ImprimirTO.Imprime(unidadTO);
                System.out.println("ECONOMICO");
                ImprimirTO.Imprime(unidadTO.getEconomicoTO());
                System.out.println("SUBMARCA");
                ImprimirTO.Imprime(unidadTO.getSubmarcaTO());
                System.out.println("COLORAUTO");
                ImprimirTO.Imprime(unidadTO.getColorAutoTO());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("------------------------- BUSQUEDA POR ECONOMICO ACTIVO");
            unidadTO = unidadDAO.findUnidadActivoByEconomico(1);
            if (unidadTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("UNIDAD");
                ImprimirTO.Imprime(unidadTO);
                System.out.println("ECONOMICO");
                ImprimirTO.Imprime(unidadTO.getEconomicoTO());
                System.out.println("SUBMARCA");
                ImprimirTO.Imprime(unidadTO.getSubmarcaTO());
                System.out.println("COLORAUTO");
                ImprimirTO.Imprime(unidadTO.getColorAutoTO());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("**++++++++++++++++++++++ SELECT UNIDAD +**");
            java.util.Collection<UnidadTO> listUnidadTO = unidadDAO.selectUnidad();
            if (listUnidadTO != null) {
                for (int i = 0; i < listUnidadTO.size(); i++) {
                    unidadTO = ((ArrayList<UnidadTO>) listUnidadTO).get(i);
                    System.out.println("UNIDAD");
                    ImprimirTO.Imprime(unidadTO);
                    System.out.println("ECONOMICO");
                    ImprimirTO.Imprime(unidadTO.getEconomicoTO());
                }
            } else {
                System.out.println("No hubo resultados en la seleccion");
            }
            System.out.println("**++++++++++++++++++++++ SELECT UNIDAD+**");
            listUnidadTO = unidadDAO.selectUnidad();
            if (listUnidadTO != null) {
                for (int i = 0; i < listUnidadTO.size(); i++) {
                    unidadTO = ((ArrayList<UnidadTO>) listUnidadTO).get(i);
                    System.out.println("UNIDAD");
                    ImprimirTO.Imprime(unidadTO);
                    System.out.println("ECONOMICO");
                    ImprimirTO.Imprime(unidadTO.getEconomicoTO());
                    System.out.println("SUBMARCA");
                    ImprimirTO.Imprime(unidadTO.getSubmarcaTO());
                    System.out.println("COLORAUTO");
                    ImprimirTO.Imprime(unidadTO.getColorAutoTO());
                }
            } else {
                System.out.println("No hubo resultados en la seleccion");
            }
            System.out.println("**++++++++++++++++++++++ SELECT UNIDAD ACTIVO+**");
            listUnidadTO = unidadDAO.selectUnidad();
            if (listUnidadTO != null) {
                for (int i = 0; i < listUnidadTO.size(); i++) {
                    unidadTO = ((ArrayList<UnidadTO>) listUnidadTO).get(i);
                    System.out.println("UNIDAD");
                    ImprimirTO.Imprime(unidadTO);
                    System.out.println("ECONOMICO");
                    ImprimirTO.Imprime(unidadTO.getEconomicoTO());
                    System.out.println("SUBMARCA");
                    ImprimirTO.Imprime(unidadTO.getSubmarcaTO());
                    System.out.println("COLORAUTO");
                    ImprimirTO.Imprime(unidadTO.getColorAutoTO());
                }
            } else {
                System.out.println("No hubo resultados en la seleccion");
            }
            System.out.println("**++++++++++++++++++++++ SELECT UNIDAD ACTIVO POR MODELO+**");
            listUnidadTO = unidadDAO.selectActivaByModelo(2008);
            if (listUnidadTO != null) {
                for (int i = 0; i < listUnidadTO.size(); i++) {
                    unidadTO = ((ArrayList<UnidadTO>) listUnidadTO).get(i);
                    System.out.println("UNIDAD");
                    ImprimirTO.Imprime(unidadTO);
                    System.out.println("ECONOMICO");
                    ImprimirTO.Imprime(unidadTO.getEconomicoTO());
                    System.out.println("SUBMARCA");
                    ImprimirTO.Imprime(unidadTO.getSubmarcaTO());
                    System.out.println("COLORAUTO");
                    ImprimirTO.Imprime(unidadTO.getColorAutoTO());
                }
            } else {
                System.out.println("No hubo resultados en la seleccion");
            }
            System.out.println("------------------- BUSQUEDA POR ECONOMICO");
            unidadTO = unidadDAO.selectUnidadByEconomico(1);
            if (unidadTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("UNIDAD");
                for (int x = 0; x < unidadTO.getListIdUnidad().length; x++) {
                    System.out.println("IDUNIDAD");
                    System.out.println(unidadTO.getListIdUnidad()[x]);
                }
                for (int x = 0; x < unidadTO.getListPlacas().length; x++) {
                    System.out.println("PLACAS");
                    System.out.println(unidadTO.getListPlacas()[x]);
                }
                for (int x = 0; x < unidadTO.getListModelo().length; x++) {
                    System.out.println("MODELO");
                    System.out.println(unidadTO.getListModelo()[x]);
                }
                for (int x = 0; x < unidadTO.getListActivo().length; x++) {
                    System.out.println("ACTIVO");
                    System.out.println(unidadTO.getListActivo()[x]);
                }
                System.out.println("Economico");
                ImprimirTO.Imprime(unidadTO.getEconomicoTO());
            } else {
                System.out.println("Falla en la seleccion");
            }
        } catch (Exception e) {
            System.out.println("Ocurrio una excepcion");
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<EMPLEADO>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            PuestoTO puestoTO = new PuestoTO();
            puestoTO.setIdPuesto((short) 1);
            PersonaTO personaTO = new PersonaTO();
            personaTO.setIdPersona(1);
            EmpleadoDAO empleadoDAO = fabricaMySQL.getEmpleadoDAO();
            EmpleadoTO empleadoTO = new EmpleadoTO(0, "1234567", FechaHoraTO.getFecha("2008/09/09"), (short) 0, puestoTO, personaTO, true);
            resultado = empleadoDAO.activaDesactivaEmpleado(6, false);
            if (resultado == 1) System.out.println(">>>>>>>>>>>>>>>>>>> Desactivado correctamente"); else if (resultado == 0) System.out.println("Falla en el Desactivado"); else if (resultado == -1) System.out.println("Ocurrio una excepcion");
            empleadoTO.setIdEmpleado(5);
            empleadoTO.setActivo(false);
            resultado = empleadoDAO.updateEmpleado(empleadoTO);
            if (resultado == 1) System.out.println(">>>>>>>>>>>>>>>>>>> Actualizado correctamente"); else if (resultado == 0) System.out.println("Falla en la actualizacion"); else if (resultado == -1) System.out.println("Ocurrio una excepcion");
            System.out.println("------------------------- BUSQUEDA POR ID");
            empleadoTO = empleadoDAO.findEmpleadoById(1);
            if (empleadoTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("EMPLEADO");
                ImprimirTO.Imprime(empleadoTO);
                System.out.println("PUESTO");
                ImprimirTO.Imprime(empleadoTO.getPuestoTO());
                System.out.println("PERSONA");
                ImprimirTO.Imprime(empleadoTO.getPersonaTO());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("------------------------- BUSQUEDA POR NSS");
            empleadoTO = empleadoDAO.findEmpleadoByNss("1234567");
            if (empleadoTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("EMPLEADO");
                ImprimirTO.Imprime(empleadoTO);
                System.out.println("PUESTO");
                ImprimirTO.Imprime(empleadoTO.getPuestoTO());
                System.out.println("PERSONA");
                ImprimirTO.Imprime(empleadoTO.getPersonaTO());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("**++++++++++++++++++++++ SELECT EMPLEADO +**");
            java.util.Collection<EmpleadoTO> listEmpleadoTO = empleadoDAO.selectEmpleado();
            if (listEmpleadoTO != null) {
                for (int i = 0; i < listEmpleadoTO.size(); i++) {
                    empleadoTO = ((ArrayList<EmpleadoTO>) listEmpleadoTO).get(i);
                    System.out.println("EMPLEADO");
                    ImprimirTO.Imprime(empleadoTO);
                    System.out.println("PUESTO");
                    ImprimirTO.Imprime(empleadoTO.getPuestoTO());
                    System.out.println("PERSONA");
                    ImprimirTO.Imprime(empleadoTO.getPersonaTO());
                }
            } else {
                System.out.println("No hubo resultados en la seleccion");
            }
            System.out.println("**++++++++++++++++++++++ SELECT EMPLEADO ACTIVO+**");
            listEmpleadoTO = empleadoDAO.selectEmpleadoActivo();
            if (listEmpleadoTO != null) {
                for (int i = 0; i < listEmpleadoTO.size(); i++) {
                    empleadoTO = ((ArrayList<EmpleadoTO>) listEmpleadoTO).get(i);
                    System.out.println("EMPLEADO");
                    ImprimirTO.Imprime(empleadoTO);
                    System.out.println("ECONOMICO");
                    ImprimirTO.Imprime(empleadoTO.getPuestoTO());
                    System.out.println("SUBMARCA");
                    ImprimirTO.Imprime(empleadoTO.getPersonaTO());
                }
            } else {
                System.out.println("No hubo resultados en la seleccion");
            }
            System.out.println("**++++++++++++++++++++++ SELECT EMPLEADO ACTIVO POR NOMBRE Y AP PATERNO+------------------------------**");
            listEmpleadoTO = empleadoDAO.selectEmpleadoByNombrePaternoActivo("pedro israel", "anaya");
            if (listEmpleadoTO != null) {
                for (int i = 0; i < listEmpleadoTO.size(); i++) {
                    empleadoTO = ((ArrayList<EmpleadoTO>) listEmpleadoTO).get(i);
                    System.out.println("EMPLEADO");
                    ImprimirTO.Imprime(empleadoTO);
                    System.out.println("PUESTO");
                    ImprimirTO.Imprime(empleadoTO.getPuestoTO());
                    System.out.println("PERSONA");
                    ImprimirTO.Imprime(empleadoTO.getPersonaTO());
                }
            } else {
                System.out.println("No hubo resultados en la seleccion");
            }
            System.out.println("------------------- BUSQUEDA POR PUESTO");
            empleadoTO = empleadoDAO.selectEmpleadoByPuesto((short) 1);
            if (empleadoTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("EMPLEADO");
                for (int x = 0; x < empleadoTO.getListIdEmpleado().length; x++) {
                    System.out.println("IDEMPLEADO");
                    System.out.println(empleadoTO.getListIdEmpleado()[x]);
                }
                for (int x = 0; x < empleadoTO.getListNss().length; x++) {
                    System.out.println("NSS");
                    System.out.println(empleadoTO.getListNss()[x]);
                }
                for (int x = 0; x < empleadoTO.getListFechaIngreso().length; x++) {
                    System.out.println("FECHAINGRESO");
                    System.out.println(empleadoTO.getListFechaIngreso()[x]);
                }
                for (int x = 0; x < empleadoTO.getListDiasVacaciones().length; x++) {
                    System.out.println("DIASVACACIONES");
                    System.out.println(empleadoTO.getListDiasVacaciones()[x]);
                }
                for (int x = 0; x < empleadoTO.getListActivo().length; x++) {
                    System.out.println("ACTIVO");
                    System.out.println(empleadoTO.getListActivo()[x]);
                }
                for (int x = 0; x < empleadoTO.getListPersonaTO().length; x++) {
                    System.out.println("PERSONA");
                    System.out.println(empleadoTO.getListPersonaTO()[x]);
                }
                System.out.println("Puesto");
                ImprimirTO.Imprime(empleadoTO.getPuestoTO());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("------------------- BUSQUEDA POR PUESTO ACTIVO");
            empleadoTO = empleadoDAO.selectEmpleadoActivoByPuesto((short) 1);
            if (empleadoTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("EMPLEADO");
                for (int x = 0; x < empleadoTO.getListIdEmpleado().length; x++) {
                    System.out.println("IDEMPLEADO");
                    System.out.println(empleadoTO.getListIdEmpleado()[x]);
                }
                for (int x = 0; x < empleadoTO.getListNss().length; x++) {
                    System.out.println("NSS");
                    System.out.println(empleadoTO.getListNss()[x]);
                }
                for (int x = 0; x < empleadoTO.getListFechaIngreso().length; x++) {
                    System.out.println("FECHAINGRESO");
                    System.out.println(empleadoTO.getListFechaIngreso()[x]);
                }
                for (int x = 0; x < empleadoTO.getListDiasVacaciones().length; x++) {
                    System.out.println("DIASVACACIONES");
                    System.out.println(empleadoTO.getListDiasVacaciones()[x]);
                }
                for (int x = 0; x < empleadoTO.getListActivo().length; x++) {
                    System.out.println("ACTIVO");
                    System.out.println(empleadoTO.getListActivo()[x]);
                }
                for (int x = 0; x < empleadoTO.getListPersonaTO().length; x++) {
                    System.out.println("PERSONA");
                    ImprimirTO.Imprime(empleadoTO.getListPersonaTO()[x]);
                }
                System.out.println("Puesto");
                ImprimirTO.Imprime(empleadoTO.getPuestoTO());
            } else {
                System.out.println("Falla en la seleccion");
            }
        } catch (Exception e) {
            System.out.println("Ocurrio una excepcion");
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<BITACORAEMPLEADO>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            PuestoTO puestoTO = new PuestoTO();
            puestoTO.setIdPuesto((short) 1);
            EmpleadoTO empleadoTO = new EmpleadoTO();
            empleadoTO.setIdEmpleado(1);
            BitacoraEmpleadoDAO bitacoraEmpleadoDAO = fabricaMySQL.getBitacoraEmpleadoDAO();
            BitacoraEmpleadoTO bitacoraEmpleadoTO = new BitacoraEmpleadoTO(0, empleadoTO, FechaHoraTO.getFecha("2008/09/09"), puestoTO);
            if (bitacoraEmpleadoDAO.insertBitacoraEmpleado(bitacoraEmpleadoTO)) {
                bitacoraEmpleadoDAO.insertBitacoraEmpleado(bitacoraEmpleadoTO);
                System.out.println("+++++++++++++++++++ Insertado correctamente");
            } else System.out.println("Falla en la insercion");
            bitacoraEmpleadoTO.setIdOperacion(1);
            bitacoraEmpleadoTO.setFecha(FechaHoraTO.getFecha("2005/05/05"));
            resultado = bitacoraEmpleadoDAO.updateBitacoraEmpleado(bitacoraEmpleadoTO);
            if (resultado == 1) System.out.println(">>>>>>>>>>>>>>>>>>> Actualizado correctamente"); else if (resultado == 0) System.out.println("Falla en la actualizacion"); else if (resultado == -1) System.out.println("Ocurrio una excepcion");
            System.out.println("------------------------- BUSQUEDA POR ID");
            bitacoraEmpleadoTO = bitacoraEmpleadoDAO.findBitacoraEmpleadoById(1);
            if (bitacoraEmpleadoTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("BITACORAEMPLEADO");
                ImprimirTO.Imprime(bitacoraEmpleadoTO);
                System.out.println("PUESTO");
                ImprimirTO.Imprime(bitacoraEmpleadoTO.getPuestoTO());
                System.out.println("EMPLEADO");
                ImprimirTO.Imprime(bitacoraEmpleadoTO.getEmpleadoTO());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("**++++++++++++++++++++++ SELECT BITACORAEMPLEADO +**");
            java.util.Collection<BitacoraEmpleadoTO> listBitacoraEmpleadoTO = bitacoraEmpleadoDAO.selectBitacoraEmpleado();
            if (listBitacoraEmpleadoTO != null) {
                for (int i = 0; i < listBitacoraEmpleadoTO.size(); i++) {
                    bitacoraEmpleadoTO = ((ArrayList<BitacoraEmpleadoTO>) listBitacoraEmpleadoTO).get(i);
                    System.out.println("BITACORAEMPLEADO");
                    ImprimirTO.Imprime(bitacoraEmpleadoTO);
                    System.out.println("PUESTO");
                    ImprimirTO.Imprime(bitacoraEmpleadoTO.getPuestoTO());
                    System.out.println("EMPLEADO");
                    ImprimirTO.Imprime(bitacoraEmpleadoTO.getEmpleadoTO());
                }
            } else {
                System.out.println("No hubo resultados en la seleccion");
            }
            System.out.println("**++++++++++++++++++++++ SELECT BITACORAEMPLEADO POR FECHA+**");
            listBitacoraEmpleadoTO = bitacoraEmpleadoDAO.selectBitacoraEmpleadoByRangoFecha(FechaHoraTO.getFecha("2000/05/05"), FechaHoraTO.getFecha("2008/05/05"));
            if (listBitacoraEmpleadoTO != null) {
                for (int i = 0; i < listBitacoraEmpleadoTO.size(); i++) {
                    bitacoraEmpleadoTO = ((ArrayList<BitacoraEmpleadoTO>) listBitacoraEmpleadoTO).get(i);
                    System.out.println("BITACORAEMPLEADO");
                    ImprimirTO.Imprime(bitacoraEmpleadoTO);
                    System.out.println("PUESTO");
                    ImprimirTO.Imprime(bitacoraEmpleadoTO.getPuestoTO());
                    System.out.println("EMPLEADO");
                    ImprimirTO.Imprime(bitacoraEmpleadoTO.getEmpleadoTO());
                }
            } else {
                System.out.println("No hubo resultados en la seleccion");
            }
            System.out.println("------------------- BUSQUEDA POR PUESTO");
            bitacoraEmpleadoTO = bitacoraEmpleadoDAO.selectBitacoraEmpleadoByPuesto((short) 1);
            if (bitacoraEmpleadoTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("BITACORAEMPLEADO");
                for (int x = 0; x < bitacoraEmpleadoTO.getListIdOperacion().length; x++) {
                    System.out.println("IDBITACORAEMPLEADO");
                    System.out.println(bitacoraEmpleadoTO.getListIdOperacion()[x]);
                }
                for (int x = 0; x < bitacoraEmpleadoTO.getListFecha().length; x++) {
                    System.out.println("FECHA");
                    System.out.println(bitacoraEmpleadoTO.getListFecha()[x]);
                }
                for (int x = 0; x < bitacoraEmpleadoTO.getListEmpleadoTO().length; x++) {
                    System.out.println("EMPLEADO");
                    System.out.println(bitacoraEmpleadoTO.getListEmpleadoTO()[x]);
                }
                System.out.println("Puesto");
                ImprimirTO.Imprime(bitacoraEmpleadoTO.getPuestoTO());
            } else {
                System.out.println("Falla en la seleccion");
            }
            System.out.println("------------------- BUSQUEDA POR EMPLEADO");
            bitacoraEmpleadoTO = bitacoraEmpleadoDAO.selectBitacoraEmpleadoByEmpleado(1);
            if (bitacoraEmpleadoTO != null) {
                System.out.println("Busqueda exitosa");
                System.out.println("BITACORAEMPLEADO");
                for (int x = 0; x < bitacoraEmpleadoTO.getListIdOperacion().length; x++) {
                    System.out.println("IDBITACORAEMPLEADO");
                    System.out.println(bitacoraEmpleadoTO.getListIdOperacion()[x]);
                }
                for (int x = 0; x < bitacoraEmpleadoTO.getListFecha().length; x++) {
                    System.out.println("FECHA");
                    System.out.println(bitacoraEmpleadoTO.getListFecha()[x]);
                }
                for (int x = 0; x < bitacoraEmpleadoTO.getListPuestoTO().length; x++) {
                    System.out.println("PUESTO");
                    ImprimirTO.Imprime(bitacoraEmpleadoTO.getListPuestoTO()[x]);
                }
                System.out.println("EMPLEADO");
                ImprimirTO.Imprime(bitacoraEmpleadoTO.getEmpleadoTO());
            } else {
                System.out.println("Falla en la seleccion");
            }
        } catch (Exception e) {
            System.out.println("Ocurrio una excepcion");
        }
    }
}
