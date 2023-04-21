package es.antonio.duarte.dao;

import java.util.Collection;

/**
 * Interfaz general para los DAOs.
 * @author Antonio Duarte
 */
public interface DAOGeneral {

    /**
    * Consulta de todos los registros. Podra recibir un objeto o ser invocado
    * sin parametros.
    * @return Lista de objetos (vacia si no hay registros)
    * @param objeto Objeto con el que se realizara la consulta
    */
    @SuppressWarnings("unchecked")
    Collection consultar(Object... objeto);

    /**
    * Inserta la entidad proporcionada.
    * @param entidad Entidad que se desea insertar a la BBDD
    * @return ID del registro insertado
    */
    long insertar(Object entidad);

    /**
    * Actualiza la entidad proporcionada.
    * @param entidad Entidad que se desea actualizar en la BBDD
    * @return Numero de registros modificados.
    */
    int actualizar(Object entidad);

    /**
    * Elimina la entidad proporcionada.
    * @param entidad Entidad que se desea eliminar de la BBDD
    * @return Numero de registros modificados
    */
    int eliminar(Object entidad);

    /**
    * Consulta del registro que corresponda con el ID proporcionado.
    * @param id ID del registro a consultar
    * @param clase Tipo del registro a consultar
    * @return Objeto que corresponde con el ID proporcionado
    */
    Object consultarPorId(Long id, Class... clase);
}
