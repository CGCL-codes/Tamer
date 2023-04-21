package modelo.persistencia;

import java.io.Serializable;
import java.util.List;
import modelo.entidades.Entidad;

/**
 *
* @author Inmaculada Casanova Ruiz
 */
public interface GenericDAO<T extends Entidad, K extends Serializable> {

    void create(T entidad);

    T read(K pk);

    void update(T entidad);

    void delete(T entidad);

    List<T> list();
}
