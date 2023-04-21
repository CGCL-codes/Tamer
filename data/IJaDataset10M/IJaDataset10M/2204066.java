package presentacion.menus;

import datos.coneccion.BaseDatos;
import datos.coneccion.mysql.BaseDatosMySQL;
import presentacion.Consola;
import presentacion.util.In;

/**
 *
 * @author luciano
 */
public class Menu_M0 extends Menu {

    public static final String M0_OP1 = " 1 - Agregar nuevos registros a la base de datos";

    public static final String M0_OP2 = " 2 - Editar registros de la base de datos";

    public static final String M0_OP3 = " 3 – Consultar información en la base de datos.";

    public static final String M0_OP4 = " 4 - Formularios especiales.";

    public static final String M0_OP5 = " 5 – Créditos";

    public static final String M0_OP6 = " 6 – Salir";

    public Menu_M0(BaseDatosMySQL baseDatosMySQL) {
        this.baseDatosMySQL = baseDatosMySQL;
    }

    public void iniciarMenu() {
        int opcion = 0;
        while (opcion != 6) {
            try {
                imprimirMenu();
                opcion = leerOpcion();
                switch(opcion) {
                    case 1:
                        iniciarM0_Op1();
                        break;
                    case 2:
                        iniciarM0_Op2();
                        break;
                    case 3:
                        iniciarM0_Op3();
                        break;
                    case 4:
                        iniciarM0_Op4();
                        break;
                    case 5:
                        iniciarM0_Op5();
                        break;
                    case 6:
                        System.out.println("Finalizando programa");
                        break;
                    default:
                        System.out.println(Consola.ERROR_NO_EXISTE_OPCION);
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println(Consola.ERROR_LA_OPCION_DEBE_SER_UN_NUMERO_ENTERO);
            } catch (Exception e) {
                System.out.println(Consola.ERROR_DESCONOCIDO);
            }
        }
    }

    protected void imprimirMenu() {
        System.out.println("Menu inicial. Ingrese una opcion.");
        System.out.println(M0_OP1);
        System.out.println(M0_OP2);
        System.out.println(M0_OP3);
        System.out.println(M0_OP4);
        System.out.println(M0_OP5);
        System.out.println(M0_OP6);
    }

    private void iniciarM0_Op1() {
        System.out.println(M0_OP1);
        Menu_M1 menu_M1 = new Menu_M1(baseDatosMySQL);
        menu_M1.iniciarMenu();
    }

    private void iniciarM0_Op2() {
        System.out.println(M0_OP2);
        Menu_M2 menu_M2 = new Menu_M2(baseDatosMySQL);
        menu_M2.iniciarMenu();
    }

    private void iniciarM0_Op3() {
        System.out.println(M0_OP3);
        Menu_M3 menu_M3 = new Menu_M3(baseDatosMySQL);
        menu_M3.iniciarMenu();
    }

    private void iniciarM0_Op4() {
        System.out.println(M0_OP4);
        Menu_M4 menu_M4 = new Menu_M4(baseDatosMySQL);
        menu_M4.iniciarMenu();
    }

    private void iniciarM0_Op5() {
        System.out.println(M0_OP5);
        System.out.println("falta terminar");
    }
}
