package org.gestionabierta.utilidades;

/**
 *
 * @author Franky Villadiego
 */
public class ClassUtil {

    private ClassUtil() {
    }

    /**
     * Retorna solo el nombre de la clase sin el paquete al
     * cual pertenezca.
     *
     * @param cls
     * @return
     */
    public static String getNombreSimple(Class cls) {
        return cls.getSimpleName();
    }

    public static String getNombreSimple(String cls) {
        return cls;
    }

    /**
     * Retorna el nombre de la clase full o completo, esto incluye
     * paquetes y todo.
     *
     * @param cls
     * @return
     */
    public static String getNombreCompleto(Class cls) {
        return cls.getName();
    }

    /**
     * Retorna solo el nombre del paquete al cual pertenece la clase,
     * si no pertenece a ningun paquete retorna cadena vacia.
     *
     * @param cls
     * @return
     */
    public static String getNombrePaquete(Class cls) {
        String nombre = cls.getPackage().getName();
        return nombre != null ? nombre : "";
    }
}
