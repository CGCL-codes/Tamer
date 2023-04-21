package modelo.gestionCarta;

import java.io.Serializable;
import java.util.*;

/**
 * @version 1.0
 * @created 21-mar-2007 21:39:41
 */
public class Seccion implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8328957860345395899L;

    private int id_seccion;

    private String nombreSeccion;

    private ArrayList<Producto> m_Producto;

    /**
	 *
	 * @param nombre
	 */
    public Seccion(String nombre) {
        this.nombreSeccion = nombre;
        m_Producto = new ArrayList<Producto>();
        id_seccion = -1;
    }

    public Seccion(String nombre, int id) {
        this.nombreSeccion = nombre;
        m_Producto = new ArrayList<Producto>();
        id_seccion = id;
    }

    public void finalize() throws Throwable {
    }

    /**
	 *
	 * @param p
	 */
    public void agregarProducto(Producto p) {
        m_Producto.add(p);
    }

    @SuppressWarnings("unchecked")
    public void agregarCollection(Collection c) {
        this.m_Producto.addAll(c);
    }

    /**
	 *
	 * @param nombre
	 */
    public Producto buscarProducto(String nombre) {
        Producto p = new Producto(0, 0, nombre, "");
        int index = m_Producto.indexOf(p);
        return m_Producto.get(index);
    }

    /**
	 *
	 * @param nuevo_nombre
	 */
    public void setNombreSeccion(String nuevo_nombre) {
        this.nombreSeccion = nuevo_nombre;
    }

    public int getIdSeccion() {
        return this.id_seccion;
    }

    public void setIdSeccion(int nueva_id) {
        this.id_seccion = nueva_id;
    }

    public String getNombreSeccion() {
        return this.nombreSeccion;
    }

    /**
	 *
	 * @param p
	 */
    public void eliminarProducto(Producto p) {
        m_Producto.remove(p);
    }

    public Producto[] getProductos() {
        return (Producto[]) this.m_Producto.toArray(new Producto[0]);
    }

    public boolean equals(Object o) {
        if (o instanceof Seccion) {
            Seccion s = (Seccion) o;
            if (this.nombreSeccion.equals(s.getNombreSeccion())) return true;
        }
        return false;
    }
}
