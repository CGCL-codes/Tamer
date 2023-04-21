package ramon.mapper;

import java.util.Map;

/**
 * Clase abstracta de la que derivan las clases que contienen los algoritmos
 * para destruir un campo unitario, una colección de campos unitarios o bien un
 * mapa de campos unitarios.
 */
public abstract class DestructorHelper {

    public abstract void destruir(Map<String, String> destruccion, String nombre);
}
