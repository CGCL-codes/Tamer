package com.antares.sirius.dao.impl;

import java.util.Collection;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.antares.commons.dao.impl.BusinessEntityDAOImpl;
import com.antares.commons.filter.Filter;
import com.antares.commons.util.Utils;
import com.antares.sirius.dao.ActividadDAO;
import com.antares.sirius.filter.ActividadFilter;
import com.antares.sirius.model.Actividad;
import com.antares.sirius.model.EstadoActividad;
import com.antares.sirius.model.EstadoProyecto;
import com.antares.sirius.model.Meta;
import com.antares.sirius.model.ObjetivoEspecifico;
import com.antares.sirius.model.ObjetivoGeneral;
import com.antares.sirius.model.Proyecto;

/**
 * Implementacion de la interfaz ActividadDAO.
 *
 * @version 1.0.0 Created 23/01/2011 by Julian Martinez
 * @author <a href:mailto:otakon@gmail.com> Julian Martinez </a>
 *
 */
public class ActividadDAOImpl extends BusinessEntityDAOImpl<Actividad> implements ActividadDAO {

    public Actividad findByNombre(String nombre) {
        Criteria crit = buildCriteria();
        crit.add(ilike("nombre", nombre, MatchMode.EXACT));
        return (Actividad) crit.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public Collection<Actividad> findAllExceptEstados(EstadoProyecto... estadoProyecto) {
        Criteria crit = buildCriteria();
        crit.createAlias("meta", "meta");
        crit.createAlias("meta.objetivoEspecifico", "objetivoEspecifico");
        crit.createAlias("objetivoEspecifico.objetivoGeneral", "objetivoGeneral");
        crit.createAlias("objetivoGeneral.proyecto", "proyecto");
        crit.add(Restrictions.not(Restrictions.in("proyecto.estadoProyecto", estadoProyecto)));
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Actividad> findAllByProyecto(Proyecto proyecto) {
        Criteria crit = buildCriteria();
        crit.createAlias("meta", "meta");
        crit.createAlias("meta.objetivoEspecifico", "objetivoEspecifico");
        crit.createAlias("objetivoEspecifico.objetivoGeneral", "objetivoGeneral");
        crit.add(Restrictions.eq("objetivoGeneral.proyecto", proyecto));
        return crit.list();
    }

    public Integer countByProyectoExceptEstados(Proyecto proyecto, EstadoActividad... estadoActividad) {
        return findAllByProyectoExceptEstados(proyecto, estadoActividad).size();
    }

    @SuppressWarnings("unchecked")
    public Collection<Actividad> findAllByProyectoEstados(Proyecto proyecto, EstadoActividad... estadoActividad) {
        Criteria crit = buildCriteria();
        crit.createAlias("meta", "meta");
        crit.createAlias("meta.objetivoEspecifico", "objetivoEspecifico");
        crit.createAlias("objetivoEspecifico.objetivoGeneral", "objetivoGeneral");
        crit.add(Restrictions.eq("objetivoGeneral.proyecto", proyecto));
        crit.add(Restrictions.in("estadoActividad", estadoActividad));
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Actividad> findAllByProyectoExceptEstados(Proyecto proyecto, EstadoActividad... estadoActividad) {
        Criteria crit = buildCriteria();
        crit.createAlias("meta", "meta");
        crit.createAlias("meta.objetivoEspecifico", "objetivoEspecifico");
        crit.createAlias("objetivoEspecifico.objetivoGeneral", "objetivoGeneral");
        crit.add(Restrictions.eq("objetivoGeneral.proyecto", proyecto));
        crit.add(Restrictions.not(Restrictions.in("estadoActividad", estadoActividad)));
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Actividad> findAllByObjetivoGeneral(ObjetivoGeneral objetivoGeneral) {
        Criteria crit = buildCriteria();
        crit.createAlias("meta", "meta");
        crit.createAlias("meta.objetivoEspecifico", "objetivoEspecifico");
        crit.add(Restrictions.eq("objetivoEspecifico.objetivoGeneral", objetivoGeneral));
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Actividad> findAllByObjetivoEspecifico(ObjetivoEspecifico objetivoEspecifico) {
        Criteria crit = buildCriteria();
        crit.createAlias("meta", "meta");
        crit.add(Restrictions.eq("meta.objetivoEspecifico", objetivoEspecifico));
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Actividad> findAllByMeta(Meta meta) {
        Criteria crit = buildCriteria();
        crit.add(Restrictions.eq("meta", meta));
        return crit.list();
    }

    @Override
    protected void addFilter(Criteria crit, Filter<Actividad> filter) {
        ActividadFilter entityFilter = (ActividadFilter) filter;
        if (Utils.isNotNullNorEmpty(entityFilter.getNombre())) {
            crit.add(ilike("nombre", entityFilter.getNombre(), MatchMode.ANYWHERE));
        }
        if (entityFilter.getMeta() != null) {
            crit.add(Restrictions.eq("meta", entityFilter.getMeta()));
        }
        if (entityFilter.getEstadoActividad() != null) {
            crit.add(Restrictions.eq("estadoActividad", entityFilter.getEstadoActividad()));
        }
    }

    @Override
    protected void addOrder(Criteria crit) {
        crit.addOrder(Order.asc("nombre"));
    }
}
