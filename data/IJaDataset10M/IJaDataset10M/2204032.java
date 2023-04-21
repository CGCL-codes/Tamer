package org.gestionabierta.dominio.servicios.presupuesto;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.gestionabierta.dominio.modelo.presupuesto.Asignacion;
import org.gestionabierta.dominio.modelo.presupuesto.CodigoPresupuestario;
import org.gestionabierta.dominio.modelo.presupuesto.UnidadEjecutora;
import org.gestionabierta.dominio.persistencia.presupuesto.IAsignacionDao;
import org.gestionabierta.dominio.persistencia.presupuesto.ICodigoPresupuestarioDao;
import org.gestionabierta.dominio.persistencia.presupuesto.IUnidadEjecutoraDao;
import org.gestionabierta.dominio.servicios.GenericSrv;
import org.gestionabierta.utilidades.StringUtil;
import org.gestionabierta.utilidades.excepciones.DataAccessLayerException;
import org.gestionabierta.utilidades.excepciones.ServiceLayerException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Franky villadiego
 */
@Service
public class AsignacionSrv extends GenericSrv implements IAsignacionSrv {

    private static final Logger log = Logger.getLogger(AsignacionSrv.class.getName());

    @Resource
    private IAsignacionDao asignacionDao;

    @Resource
    private ICodigoPresupuestarioDao codigoPresupuestarioDao;

    @Resource
    private IUnidadEjecutoraDao unidadEjecutoraDao;

    @Transactional(readOnly = true)
    public Asignacion traerAsignacion(UnidadEjecutora unidadEjecutora, CodigoPresupuestario codigoPresupuestario) throws ServiceLayerException, DataAccessLayerException {
        return asignacionDao.traerAsignacion(unidadEjecutora, codigoPresupuestario);
    }

    @Transactional(readOnly = true)
    public boolean existeAsignacion(UnidadEjecutora unidadEjecutora, CodigoPresupuestario codigoPresupuestario) throws ServiceLayerException, DataAccessLayerException {
        return asignacionDao.existeAsignacion(unidadEjecutora, codigoPresupuestario);
    }

    @Transactional
    public void crearAsignacion(Asignacion asignacion) throws ServiceLayerException, DataAccessLayerException {
        if (asignacion == null) {
            throw new ServiceLayerException("La asignacion es nula");
        }
        if (asignacion.getCodigoPresupuestario() == null || asignacion.getCodigoPresupuestario().getCodigo() == null) {
            throw new ServiceLayerException("El CodigoPresupuestario de la Asignación no es válido");
        } else {
            CodigoPresupuestario cod = codigoPresupuestarioDao.traerPorCodigo(asignacion.getCodigoPresupuestario().getCodigo());
            if (cod == null) {
                throw new ServiceLayerException("El CodigoPresupuestario de la Asignación no existe");
            } else {
                if (cod.getNivel() <= 3) {
                    throw new ServiceLayerException("El CodigoPresupuestario de la Asignacion debe ser de nivel 4");
                }
            }
        }
        if (asignacion.getUnidadEjecutora() == null || asignacion.getUnidadEjecutora().getCodigo() == null) {
            throw new ServiceLayerException("La UnidadEjecutora de la Asignación no es válida");
        } else {
            UnidadEjecutora uni = unidadEjecutoraDao.traerPorCodigo(asignacion.getUnidadEjecutora().getCodigo());
            if (uni == null) {
                throw new ServiceLayerException("La UnidadEjecutora de la Asignación no existe");
            } else {
                if (uni.getNivel() <= 4) {
                    throw new ServiceLayerException("La UnidadEjecutora de la Asignación debe ser nivel 5");
                }
            }
        }
        if (asignacion.getMonto() == null || !(asignacion.getMonto().compareTo(BigDecimal.ZERO) == 1)) {
            throw new ServiceLayerException("El monto de la Asignación es incorrecto");
        }
        boolean existe = existeAsignacion(asignacion.getUnidadEjecutora(), asignacion.getCodigoPresupuestario());
        if (existe) {
        } else {
            asignacionDao.guardar(asignacion);
        }
    }

    @Transactional
    public void crearAsignacion(UnidadEjecutora unidadEjecutora, CodigoPresupuestario codigoPresupuestario, BigDecimal monto) throws ServiceLayerException, DataAccessLayerException {
        Asignacion asignacion = new Asignacion(unidadEjecutora, codigoPresupuestario, monto);
        crearAsignacion(asignacion);
    }

    @Override
    @Transactional
    public void eliminarAsignacion(Asignacion asignacion) throws ServiceLayerException, DataAccessLayerException {
        asignacionDao.eliminar(asignacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asignacion> asignacionesPorUnidadEjecutora(UnidadEjecutora unidadEjecutora) throws ServiceLayerException, DataAccessLayerException {
        if (unidadEjecutora == null) {
            throw new ServiceLayerException("La unidad ejecutora es null");
        }
        List<Asignacion> lst = null;
        lst = asignacionDao.listarAsignacionesPorUnidadEjecutora(unidadEjecutora);
        return lst;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asignacion> asignacionesPorCodigoPresupuestario(CodigoPresupuestario codigoPresupuestario) throws ServiceLayerException, DataAccessLayerException {
        if (codigoPresupuestario == null) {
            throw new ServiceLayerException("El código presupuestario es null");
        }
        List<Asignacion> lst = null;
        lst = asignacionDao.listarAsignacionesPorCodigoPresupuestario(codigoPresupuestario);
        return lst;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal montoAsignadoPorUnidadEjecutora(UnidadEjecutora unidadEjecutora) throws ServiceLayerException, DataAccessLayerException {
        if (unidadEjecutora == null) {
            throw new ServiceLayerException("La unidad ejecutora es null");
        }
        return asignacionDao.montoAsignadoPorUnidadEjecutora(unidadEjecutora);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal montoAsignadoPorCodigoPresupuestario(CodigoPresupuestario codigoPresupuestario) throws ServiceLayerException, DataAccessLayerException {
        if (codigoPresupuestario == null) {
            throw new ServiceLayerException("El código presupuestario es null");
        }
        return asignacionDao.montoAsignadoPorCodigoPresupuestario(codigoPresupuestario);
    }

    @Override
    public BigDecimal montoTotalAsignado() throws ServiceLayerException, DataAccessLayerException {
        return asignacionDao.montoTotalAsignado();
    }

    @Override
    public Asignacion traerAsignacion(String unidadEjecutora, String codigoPresupuestario) throws ServiceLayerException, DataAccessLayerException {
        if (StringUtil.isNullOrEmpty(unidadEjecutora)) {
            throw new ServiceLayerException("La UnidadEjecutora es nula");
        }
        if (StringUtil.isNullOrEmpty(codigoPresupuestario)) {
            throw new ServiceLayerException("El CodigoPresupuestario es nulo");
        }
        CodigoPresupuestario codigoPre = codigoPresupuestarioDao.traerPorCodigo(codigoPresupuestario);
        UnidadEjecutora unidadEje = unidadEjecutoraDao.traerPorCodigo(unidadEjecutora);
        return asignacionDao.traerAsignacion(unidadEje, codigoPre);
    }
}
