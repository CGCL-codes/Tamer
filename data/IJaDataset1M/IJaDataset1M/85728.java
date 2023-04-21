package es.upm.fi.datsi.teledeteccion.gyrdir.dao.impl;

import java.util.List;
import org.apache.log4j.Logger;
import es.upm.fi.datsi.teledeteccion.gyrdir.dao.interfaces.UsuarioDao;
import es.upm.fi.datsi.teledeteccion.gyrdir.entities.UsuarioEntity;

public class HbSpringUsuarioDaoImpl extends AbstractHbSpringGenericDaoImpl implements UsuarioDao {

    @SuppressWarnings("unchecked")
    public List<UsuarioEntity> findAll() {
        Logger.getRootLogger().info("usando el metodo 'findAll()' de la clase '" + this.getClass().getSimpleName() + " '");
        return getHibernateTemplate().find("from UsuarioEntity");
    }

    public UsuarioEntity findById(String id) {
        Logger.getRootLogger().info("usando el metodo 'findById()' de la clase '" + this.getClass().getSimpleName() + " '");
        return (UsuarioEntity) getHibernateTemplate().get(UsuarioEntity.class, id);
    }
}
