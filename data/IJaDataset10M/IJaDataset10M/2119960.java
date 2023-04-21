package lg.servicios.impl;

import java.util.Date;
import java.util.List;
import lg.dao.api.MovimientoDao;
import lg.dao.api.TipoMovimientoDao;
import lg.domain.bean.Movimiento;
import lg.domain.bean.TipoMovimiento;
import lg.obne.contable.Caja;
import lg.servicios.api.MovimientosServicio;

/**
 * @author Nicolas
 *
 */
public class MovimientosServicioImpl implements MovimientosServicio {

    private MovimientoDao movimientoDao;

    private TipoMovimientoDao tipoMovimientoDao;

    public List<Movimiento> buscarTodosMovimientos() {
        return movimientoDao.buscarTodos();
    }

    public void guardarMovimientoNuevo(Movimiento movimiento) {
        movimientoDao.guardar(movimiento);
    }

    public Movimiento buscarMovimientoPorId(Integer id) {
        return movimientoDao.buscarPorId(id);
    }

    public List<Movimiento> buscarMovimientoPorTipo(Integer idTipo) {
        return movimientoDao.buscarPorTipo(idTipo);
    }

    public void eliminarMovimiento(Integer id) {
        Movimiento obj = this.buscarMovimientoPorId(id);
        movimientoDao.eliminar(obj);
    }

    public void eliminarMovimiento(Movimiento obj) {
        movimientoDao.eliminar(obj);
    }

    public List<TipoMovimiento> buscarTodosTiposMovimiento() {
        return tipoMovimientoDao.buscarTodos();
    }

    public void guardarTipoMovimientoNuevo(TipoMovimiento obj) {
        tipoMovimientoDao.guardar(obj);
    }

    public TipoMovimiento buscarTipoMovimientoPorId(Integer id) {
        return tipoMovimientoDao.buscarPorId(id);
    }

    public void eliminarTipoMovimiento(Integer id) {
        TipoMovimiento obj = this.buscarTipoMovimientoPorId(id);
        tipoMovimientoDao.eliminar(obj);
    }

    public void eliminarTipoMovimiento(TipoMovimiento obj) {
        tipoMovimientoDao.eliminar(obj);
    }

    public List<Movimiento> buscarMovPorFecha(Date dia) {
        java.sql.Date diaSql = new java.sql.Date(dia.getTime());
        return movimientoDao.buscarPorFecha(diaSql);
    }

    public Caja cajaDiaria(Date dia) {
        Caja caja = new Caja();
        caja.setFecha(dia);
        caja.setMovimientos(this.buscarMovPorFecha(dia));
        return caja;
    }

    public MovimientoDao getMovimientoDao() {
        return movimientoDao;
    }

    public void setMovimientoDao(MovimientoDao movimientoDao) {
        this.movimientoDao = movimientoDao;
    }

    public TipoMovimientoDao getTipoMovimientoDao() {
        return tipoMovimientoDao;
    }

    public void setTipoMovimientoDao(TipoMovimientoDao tipoMovimientoDao) {
        this.tipoMovimientoDao = tipoMovimientoDao;
    }
}
