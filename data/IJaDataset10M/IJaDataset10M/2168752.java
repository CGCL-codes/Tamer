package lg.dao.api;

import lg.domain.bean.Alojamiento;
import org.springframework.transaction.annotation.Transactional;

public interface AlojamientoDao {

    @Transactional
    public abstract void guardarNuevo(Alojamiento transientInstance);

    @Transactional
    public abstract void guardar(Alojamiento instance);

    @Transactional
    public abstract void eliminar(Alojamiento persistentInstance);

    @Transactional
    public abstract Alojamiento merge(Alojamiento detachedInstance);

    @Transactional(readOnly = true)
    public abstract Alojamiento buscarPorId(java.lang.Integer id);
}
