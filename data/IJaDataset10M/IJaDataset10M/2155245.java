package galerias.daos.implementaciones;

import galerias.daos.interfaces.DetalleVentaDaoLocalInterface;
import galerias.entidades.DetalleVenta;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class DetalleVentaDaoImpl implements DetalleVentaDaoLocalInterface {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long obtenerCantidadObrasDetalleVentaArtista(String codigoArtista, String codigoEstadoDetalleVenta) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT SUM(dv.cantidad) FROM DetalleVenta dv ");
        query.append("WHERE 1 = 1 ");
        if (codigoArtista != null) {
            query.append("AND dv.obra.galeria.usuario.codigo = :codigoArtista ");
        }
        if (codigoEstadoDetalleVenta != null) {
            query.append("AND dv.estado.codigo = :codigoEstadoDetalleVenta");
        }
        Query q = this.em.createQuery(query.toString());
        if (codigoArtista != null) {
            q.setParameter("codigoArtista", codigoArtista);
        }
        if (codigoEstadoDetalleVenta != null) {
            q.setParameter("codigoEstadoDetalleVenta", codigoEstadoDetalleVenta);
        }
        Long resultado = (Long) q.getSingleResult();
        if (resultado == null) {
            resultado = new Long(0);
        }
        System.out.println("obtenerCantidadDetalleVentaArtista estado > " + codigoEstadoDetalleVenta + ", cantidad > " + resultado);
        return resultado;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DetalleVenta> obtenerDetallesVenta(String codigoArtista, Date fechaCreacionDesde, Date fechaCreacionHasta, String codigoEstado) {
        StringBuffer queryJPQL = new StringBuffer();
        queryJPQL.append(" SELECT dv FROM DetalleVenta dv ");
        queryJPQL.append(" WHERE 1 = 1 ");
        if (codigoArtista != null) {
            queryJPQL.append(" AND dv.obra.galeria.usuario.codigo = :codigoArtista ");
        }
        boolean fechaDesde = false;
        boolean fechaHasta = false;
        if (fechaCreacionDesde != null && fechaCreacionHasta != null) {
            queryJPQL.append(" AND dv.venta.fechaCreacion >= :fechaCreacionDesde ");
            queryJPQL.append(" AND dv.venta.fechaCreacion <= :fechaCreacionHasta ");
            fechaDesde = true;
            fechaHasta = true;
        } else {
            if (fechaCreacionDesde != null && fechaCreacionHasta == null) {
                queryJPQL.append(" AND dv.venta.fechaCreacion >= :fechaCreacionDesde ");
                fechaDesde = true;
            } else {
                if (fechaCreacionDesde == null && fechaCreacionHasta != null) {
                    queryJPQL.append(" AND dv.venta.fechaCreacion <= :fechaCreacionHasta ");
                    fechaHasta = true;
                }
            }
        }
        if (codigoEstado != null) {
            queryJPQL.append(" AND dv.estado.codigo = :codigoEstado ");
        }
        Query query = this.em.createQuery(queryJPQL.toString());
        if (codigoArtista != null) {
            query.setParameter("codigoArtista", codigoArtista);
        }
        if (fechaDesde) {
            query.setParameter("fechaCreacionDesde", fechaCreacionDesde);
        }
        if (fechaHasta) {
            query.setParameter("fechaCreacionHasta", fechaCreacionHasta);
        }
        if (codigoEstado != null) {
            query.setParameter("codigoEstado", codigoEstado);
        }
        return (List<DetalleVenta>) query.getResultList();
    }

    @Override
    public Boolean actualizarEstadoDetalleVenta(DetalleVenta detalleVenta) {
        StringBuffer query = new StringBuffer();
        query.append(" UPDATE DetalleVenta dv ");
        query.append(" SET dv.estado = :estado ");
        query.append(" WHERE dv.codigo = :codigo ");
        Query q = em.createQuery(query.toString());
        q.setParameter("estado", detalleVenta.getEstado());
        q.setParameter("codigo", detalleVenta.getCodigo());
        int resultado = q.executeUpdate();
        if (resultado > 0) {
            return new Boolean(true);
        } else {
            return new Boolean(false);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DetalleVenta> obtenerDetallesVenta(String codigoVenta) {
        StringBuffer queryJPQL = new StringBuffer();
        queryJPQL.append(" SELECT dv FROM DetalleVenta dv ");
        queryJPQL.append(" WHERE dv.venta.codigo = :codigoVenta ");
        Query query = this.em.createQuery(queryJPQL.toString());
        query.setParameter("codigoVenta", codigoVenta);
        return (List<DetalleVenta>) query.getResultList();
    }
}
