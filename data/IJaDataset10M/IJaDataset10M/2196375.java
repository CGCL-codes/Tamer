package controlador;

import java.util.Vector;
import accesoDatos.*;
import logica.*;

/**
 *
 * @author Gamboa Family
 */
public class ControladorPalabraClave {

    public void insertarPalabra(String codigo, String nombre, String descripcion) {
        PalabraClave p = new PalabraClave();
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        p.setCodigo(codigo);
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setEstado("0");
        daoPalabra.guardarPalabra(p);
        System.out.println("Se  insertó  una  nueva palabra clave en la BD");
        p = null;
        daoPalabra = null;
    }

    public Vector consultarPalabra(String codigo) {
        Vector v = new Vector();
        PalabraClave p = new PalabraClave();
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        System.out.println("Se va a consultar una palabra clave");
        p = daoPalabra.retornarPalabraPorCodigo(codigo);
        v.add(p.getCodigo());
        v.add(p.getNombre());
        v.add(p.getDescripcion());
        p = null;
        daoPalabra = null;
        return v;
    }

    public PalabraClave retornarPalabra(String codigo) {
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        return daoPalabra.retornarPalabraPorCodigo(codigo);
    }

    public boolean existeCodigo(String codigo) {
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        return daoPalabra.existeCodigo(codigo);
    }

    public void modificarPalabra(int caso, String codigo, String valor) {
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        daoPalabra.modificarPalabraClave(caso, codigo, valor);
    }

    public void descativarPalabra(String codigo) {
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        daoPalabra.desactivarPalabra(codigo);
    }

    public void activarPalabra(String codigo) {
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        daoPalabra.activarPalabra(codigo);
    }

    public Vector listarPalabras() {
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        return daoPalabra.listarPalabras();
    }

    public boolean esActivado(String codigo) {
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        return daoPalabra.esActivada(codigo);
    }

    public Vector consultarPalabraPorCodigo(String codigo) {
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        return daoPalabra.consultarPalabraPorCodigo(codigo);
    }

    public Vector consultarPalabraPorNombre(String nombre) {
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        return daoPalabra.consultarPalabraPorNombre(nombre);
    }

    public Vector consultarTodasLasPalabras() {
        DaoPalabraClave daoPalabra = new DaoPalabraClave();
        return daoPalabra.consultarTodasLasPalabras();
    }
}
