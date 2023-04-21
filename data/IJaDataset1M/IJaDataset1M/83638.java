package galerias.daos.implementaciones;

import galeria.utilidades.ConstantesNegocio;
import galerias.daos.interfaces.ObraDaoLocalInterface;
import galerias.entidades.Obra;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ObraDaoImpl implements ObraDaoLocalInterface {

    @PersistenceContext
    private EntityManager em;

    public Boolean actualizarObra(Obra obra) {
        StringBuffer query = new StringBuffer();
        query.append(" UPDATE Obra o ");
        query.append(" SET o.nombre = :nombre ");
        query.append(" , o.descripcion = :descripcion ");
        query.append(" , o.estado = :estado ");
        query.append(" , o.porcentajeDescuento = :porcentajeDescuento ");
        query.append(" , o.obraUnica = :obraUnica ");
        query.append(" , o.precio = :precio ");
        query.append(" , o.galeria = :galeria ");
        query.append(" , o.tipoObra = :tipoObra ");
        if (obra.getObraUnica().equals(ConstantesNegocio.NO_ES_OBRA_UNICA)) {
            query.append(" , o.cantidad = o.cantidad + :cantidad  ");
        }
        query.append(" WHERE o.codigo = :codigo");
        Query q = em.createQuery(query.toString());
        q.setParameter("nombre", obra.getNombre());
        q.setParameter("descripcion", obra.getDescripcion());
        q.setParameter("estado", obra.getEstado());
        q.setParameter("porcentajeDescuento", obra.getPorcentajeDescuento());
        q.setParameter("obraUnica", obra.getObraUnica());
        q.setParameter("precio", obra.getPrecio());
        q.setParameter("galeria", obra.getGaleria());
        q.setParameter("tipoObra", obra.getTipoObra());
        q.setParameter("codigo", obra.getCodigo());
        if (obra.getObraUnica().equals(ConstantesNegocio.NO_ES_OBRA_UNICA)) {
            q.setParameter("cantidad", obra.getCantidad());
        }
        int resultado = q.executeUpdate();
        if (resultado > 0) {
            return new Boolean(true);
        } else {
            return new Boolean(false);
        }
    }

    @Override
    public Boolean disminuirCantidadObra(String codigoObra, int cantidadADisminuir) {
        System.out.println("disminuirCantidadObra");
        StringBuffer query = new StringBuffer();
        query.append(" UPDATE Obra obra");
        query.append(" SET obra.cantidad = ");
        query.append(" (obra.cantidad - ");
        query.append(cantidadADisminuir);
        query.append(") WHERE obra.codigo = ");
        query.append(codigoObra);
        int resultado = em.createQuery(query.toString()).executeUpdate();
        if (resultado > 0) {
            return new Boolean(true);
        } else {
            return new Boolean(false);
        }
    }

    @Override
    public String guardarObra(Obra obra) {
        try {
            this.em.persist(obra);
            this.em.flush();
            return obra.getCodigo();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Obra obtenerObra(String codigoObra) {
        Obra obra = this.em.find(Obra.class, codigoObra);
        return obra;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Obra> obtenerObrasArtista(String codigoArtista) {
        StringBuffer queryJPQL = new StringBuffer();
        queryJPQL.append(" SELECT o FROM Obra o ");
        queryJPQL.append(" WHERE o.galeria.usuario.codigo = :codigoArtista ");
        Query query = this.em.createQuery(queryJPQL.toString());
        query.setParameter("codigoArtista", codigoArtista);
        return (List<Obra>) query.getResultList();
    }

    @Override
    public Long obtenerCantidadObrasArtista(String codigoArtista, String codigoEstadoObra) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT COUNT(o) FROM Obra o ");
        query.append("WHERE 1 = 1 ");
        if (codigoArtista != null) {
            query.append("AND o.galeria.usuario.codigo = :codigoArtista ");
        }
        query.append("AND o.estado.codigo = :codigoEstadoObra");
        Query q = this.em.createQuery(query.toString());
        if (codigoArtista != null) {
            q.setParameter("codigoArtista", codigoArtista);
        }
        q.setParameter("codigoEstadoObra", codigoEstadoObra);
        Long resultado = (Long) q.getSingleResult();
        System.out.println("obtenerCantidadObrasArtista estado > " + codigoEstadoObra + ", cantidad > " + resultado);
        return resultado;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Obra> obtenerObrasSinEventoAsociado(String codigoArtista, String codigoEstado, String codigoEvento) {
        System.out.println("---> init obtenerObrasSinEventoAsociado");
        StringBuffer query = new StringBuffer();
        query.append(" SELECT obra FROM Obra obra");
        query.append(" WHERE obra.galeria.usuario.codigo = " + codigoArtista);
        query.append(" AND obra.estado.codigo = " + codigoEstado);
        if (codigoEvento != null) {
            query.append(" AND obra.codigo NOT IN " + "(SELECT eventoObra.obra.codigo " + "FROM EventoObra eventoObra WHERE eventoObra.evento.codigo =" + codigoEvento + ")");
        }
        List<Obra> listaObras = (List<Obra>) (this.em.createQuery(query.toString()).getResultList());
        System.out.println("---> Cantidad obras : " + listaObras.size());
        System.out.println("---> End obtenerObrasSinEventoAsociado");
        return listaObras;
    }
}
