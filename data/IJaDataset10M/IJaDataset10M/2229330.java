package muebleria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/** Clase Cliente: Contiene los datos del cliente y sus operaciones
        * @param Ninguno
        * @return Sin valor de retorno
        * @exception exceptions Ningún error (Excepción) definida
        */
public class Cliente implements Serializable {

    ArrayList listaMueblesCliente = new ArrayList();

    String nombre;

    String apellido;

    String run;

    String telefono;

    String direccion;

    int id;

    public Cliente() {
    }

    public Cliente(String nom, String ap, String run, String tel, String dir) {
        this.nombre = nom;
        this.apellido = ap;
        this.run = run;
        this.telefono = tel;
        this.direccion = dir;
        this.id = Manejo.listaClientes.size() + 1;
    }

    void obtenerDatos() {
        System.out.print("Nombre: ");
        this.nombre = Idatos.dato();
        System.out.print(" Apellido: ");
        this.apellido = Idatos.dato();
        System.out.print("Run: ");
        this.run = Idatos.dato();
        System.out.print("Direccion: ");
        this.direccion = Idatos.dato();
        System.out.print("Telefono: ");
        this.telefono = Idatos.dato();
        this.id = Manejo.listaClientes.size() + 1;
    }

    public Object presupuestoMueble() {
        int tipoMueble = 0;
        System.out.print("Escoge el tipo de mueble [1=MesaRectangular, 2=MesaRedonda, 3=Estante]: ");
        tipoMueble = Idatos.datoIntRef(1, 3);
        switch(tipoMueble) {
            case 1:
                Mueble unaMesa = new MesaRectangular();
                unaMesa.obtenerDatos();
                return unaMesa;
            case 2:
                Mueble unaCircular = new MesaCircular();
                unaCircular.obtenerDatos();
                return unaCircular;
            case 3:
                Mueble unEstante = new Estante();
                unEstante.obtenerDatos();
                return unEstante;
        }
        return null;
    }

    void borrarListaPedidos() {
        listaMueblesCliente.clear();
    }

    void mostrarListaPedidos() {
        if (listaMueblesCliente.isEmpty()) System.out.println("\nCliente " + nombre + " " + apellido + " No tiene Pedidos de Mueble");
        System.out.println("\nMuebles del cliente " + nombre + " " + apellido);
        for (Iterator it = listaMueblesCliente.iterator(); it.hasNext(); ) {
            Mueble x2 = (Mueble) it.next();
            System.out.print("\nMueble: ");
            x2.mostrarDatos();
        }
    }

    public void mostrarDatos() {
        System.out.println("\nInformacion Cliente");
        System.out.println("Nombre: " + nombre);
        System.out.println("Apellido: " + apellido);
        System.out.println("Run: " + run);
        System.out.println("Telefono: " + telefono);
        System.out.println("Direccion: " + direccion);
        System.out.println("Id: " + id);
    }

    public void ordenaListaPedidos(int parametro) {
        switch(parametro) {
            case 1:
                Collections.sort(listaMueblesCliente);
                break;
            case 2:
                Collections.sort(listaMueblesCliente, new ordenaCosto());
                break;
        }
    }
}
