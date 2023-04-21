package krowdix.control.creacion;

import java.util.Vector;
import krowdix.control.creacion.estrategias.EstrategiaAficiones;
import krowdix.control.creacion.estrategias.EstrategiaComentarios;
import krowdix.control.creacion.estrategias.EstrategiaContenidos;
import krowdix.control.creacion.estrategias.EstrategiaObjetivo;
import krowdix.control.creacion.estrategias.EstrategiaRelaciones;
import krowdix.interfaz.AreaTrabajo;
import krowdix.interfaz.Interfaz;
import krowdix.interfaz.VentanaHerramientas;
import krowdix.interfaz.VentanaVisualizacion;
import krowdix.modelo.RedSocial;
import krowdix.modelo.objetos.Contenido;
import krowdix.modelo.objetos.agentes.Nodo;

/**
 * Creador de grupos que hace uso de estrategias.
 * 
 * Al contrario de los nodos creados por el usuario durante el manejo de la
 * aplicación, los nodos que pertenezcan a un grupo tienen una serie de
 * características más o menos comunes a todos ellos. Estas características son
 * sus aficiones, los comentarios que han hecho, los contenidos que han creado,
 * sus objetivos y las relaciones que tienen con otros nodos. Todas estas
 * características se les asignan a los nodos por medio de estrategias.
 * 
 * @see EstrategiaAficiones
 * @see EstrategiaComentarios
 * @see EstrategiaContenidos
 * @see EstrategiaObjetivo
 * @see EstrategiaRelaciones
 * @author Daniel Alonso Fernández
 */
public class CreacionParametrizada implements CreadorGrupo {

    /**
	 * Estrategia que se usará para asignar aficiones a los nodos.
	 */
    private EstrategiaAficiones estrategiaAficiones;

    /**
	 * Estrategia que se usará para decidir qué comentarios creó cada nodo.
	 */
    private EstrategiaComentarios estrategiaComentarios;

    /**
	 * Estrategia que se usará para decidir qué contenidos creó cada nodo.
	 */
    private EstrategiaContenidos estrategiaContenidos;

    /**
	 * Estrategia que se usará para decidir el objetivo de cada nodo.
	 */
    private EstrategiaObjetivo estrategiaObjetivo;

    /**
	 * Estrategia que se usará para decidir qué relaciones tiene cada nodo.
	 */
    private EstrategiaRelaciones estrategiaRelaciones;

    /**
	 * Número total de nodos que se crearán.
	 */
    private int numeroNodos;

    /**
	 * Constructora por defecto. No especifica ninguna de las estrategias, por
	 * lo tanto, antes de crear la red será preciso llamar a los mutadores
	 * correspondientes.
	 * 
	 * @see #setEstrategiaAficiones(EstrategiaAficiones)
	 * @see #setEstrategiaComentarios(EstrategiaComentarios)
	 * @see #setEstrategiaContenidos(EstrategiaContenidos)
	 * @see #setEstrategiaObjetivo(EstrategiaObjetivo)
	 * @see #setEstrategiaRelaciones(EstrategiaRelaciones)
	 * @see #setNumeroNodos(int)
	 */
    public CreacionParametrizada() {
    }

    public void crearGrupo() {
        Vector<Nodo> nodos = new Vector<Nodo>(numeroNodos);
        Vector<Contenido> contenidos = new Vector<Contenido>(numeroNodos);
        for (int i = 0; i < numeroNodos; i++) {
            Nodo n = Nodo.newNodo();
            nodos.add(n);
            RedSocial.getRedSocial().crearNodo(n);
            estrategiaObjetivo.ponerObjetivo(n);
            estrategiaAficiones.ponerAficiones(n);
            estrategiaContenidos.crearContenidos(n);
            contenidos.addAll(n.dameContenidos());
        }
        int numeroContenidos = contenidos.size();
        for (int i = 0; i < numeroNodos - 1; i++) {
            Nodo nodo1 = nodos.get(i);
            for (int j = 1; j < numeroNodos; j++) {
                Nodo nodo2 = nodos.get(j);
                estrategiaRelaciones.crearRelacion(nodo1, nodo2);
            }
            if (numeroContenidos > 0) {
                estrategiaComentarios.crearComentarios(nodo1, contenidos);
            }
        }
        AreaTrabajo.getAreaTrabajo().reordenar();
        Interfaz.getInterfaz().actualizar();
        VentanaHerramientas.getVentanaHerramientas().reiniciar();
        VentanaVisualizacion.getVentanaVisualizacion().reiniciar();
    }

    /**
	 * Especifica la {@link EstrategiaAficiones} que se usará en la creación.
	 * 
	 * @param estrategia
	 *            Estrategia a usar
	 */
    public void setEstrategiaAficiones(EstrategiaAficiones estrategia) {
        estrategiaAficiones = estrategia;
    }

    /**
	 * Especifica la {@link EstrategiaComentarios} que se usará en la creación.
	 * 
	 * @param estrategia
	 *            Estrategia a usar
	 */
    public void setEstrategiaComentarios(EstrategiaComentarios estrategia) {
        estrategiaComentarios = estrategia;
    }

    /**
	 * Especifica la {@link EstrategiaContenidos} que se usará en la creación.
	 * 
	 * @param estrategia
	 *            Estrategia a usar
	 */
    public void setEstrategiaContenidos(EstrategiaContenidos estrategia) {
        estrategiaContenidos = estrategia;
    }

    /**
	 * Especifica la {@link EstrategiaObjetivo} que se usará en la creación.
	 * 
	 * @param estrategia
	 *            Estrategia a usar
	 */
    public void setEstrategiaObjetivo(EstrategiaObjetivo estrategia) {
        estrategiaObjetivo = estrategia;
    }

    /**
	 * Especifica la {@link EstrategiaRelaciones} que se usará en la creación.
	 * 
	 * @param estrategia
	 *            Estrategia a usar
	 */
    public void setEstrategiaRelaciones(EstrategiaRelaciones estrategia) {
        estrategiaRelaciones = estrategia;
    }

    /**
	 * Especifica el número de nodos que deberán crearse con la llamada a
	 * {@link #crearGrupo()}. Por lo tanto, debe invocarse antes.
	 * 
	 * @param numero
	 *            Número de nodos del grupo
	 */
    public void setNumeroNodos(int numero) {
        numeroNodos = numero;
    }
}
