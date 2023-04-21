package muebleria;

import java.util.Iterator;

/** Clase Registro: Aqui se encuentran los metodos para que trabaje en
 *       Forma interna la muebleria.
        * @param Ninguno
        * @return Sin valor de retorno
        * @exception exceptions Ningún error (Excepción) definida
        */
public class Registro {

    public static void mostrarTotalPedidos() {
        System.out.println("<<Total Pedidos Clientes>>");
        for (Iterator it = Manejo.listaClientes.iterator(); it.hasNext(); ) {
            Cliente unCliente = (Cliente) it.next();
            System.out.println("Cliente " + unCliente.nombre + " : ");
            unCliente.mostrarListaPedidos();
        }
    }
}
