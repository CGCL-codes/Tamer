package com.proyecto.tropero.core.service.model.Impl;

import java.sql.SQLException;
import java.util.List;
import com.proyecto.tropero.core.bd.AlimentoDTO;
import com.proyecto.tropero.core.bd.IDatosDTO;
import com.proyecto.tropero.core.bd.DAO.model.Interface.IAlimentoDAO;
import com.proyecto.tropero.core.domain.Alimento;
import com.proyecto.tropero.core.service.GenericServiceImpl;
import com.proyecto.tropero.core.service.model.ServiceLocator;
import com.proyecto.tropero.core.service.model.Interface.IAlimentoService;

public class AlimentoServiceImpl extends GenericServiceImpl implements IAlimentoService {

    private static String DTOSelectStatment = "select new com.proyecto.tropero.core.bd.AlimentoDTO(this.descripcion, this.tipo.descripcion, this.descripcionCaracteristicas, this.estadoCultivo, this.cantMinReposicion, this.stock, this.monto, this.unidadDeMedida.descUniMedida, this.id, this.almacen.descripcion)  ";

    public IAlimentoDAO getDao() {
        return (IAlimentoDAO) super.getDao();
    }

    @SuppressWarnings("unchecked")
    public List<Alimento> getAll() {
        return (List<Alimento>) this.getDao().getAll();
    }

    @Override
    public void grabar(IDatosDTO datos) throws SQLException {
        AlimentoDTO alimentoDTO = (AlimentoDTO) datos;
        Alimento alimento;
        if (alimentoDTO.getId() != null) {
            alimento = (Alimento) this.getDao().findById(alimentoDTO.getId());
        } else {
            alimento = new Alimento();
        }
        alimento.setDescripcion(alimentoDTO.getDescripcion());
        alimento.setTipo(ServiceLocator.getTipoAlimentoService().getByDescripcion(alimentoDTO.getTipo()));
        alimento.setDescripcionCaracteristicas(alimentoDTO.getDescripcionCaracteristicas());
        alimento.setEstadoCultivo(alimentoDTO.getEstadoCultivo());
        alimento.setCantMinReposicion(alimentoDTO.getCantMinReposicion());
        alimento.setStock(alimentoDTO.getStock());
        alimento.setMonto(alimentoDTO.getMonto());
        alimento.setUnidadDeMedida(ServiceLocator.getUnidadMedidaService().getUnidadMedidaByDescription(alimentoDTO.getUnidadDeMedida()));
        alimento.setAlmacen(ServiceLocator.getAlmacenService().getAlmacenByDescripcion(alimentoDTO.getAlmacen()));
        this.getDao().saveOrUpdate(alimento);
        ServiceLocator.getAlertaService().actualizarAlertas();
    }

    public String getDTOSelectStatment() {
        return DTOSelectStatment;
    }

    public void borrar(IDatosDTO datos) throws SQLException {
        AlimentoDTO datosAlimento = (AlimentoDTO) datos;
        Alimento alimento = (Alimento) this.getDao().findById(datosAlimento.getId());
        this.getDao().delete(alimento);
    }

    @Override
    public List<Alimento> getAllById(List<Integer> idAlimentos) {
        return this.getDao().getAllById(idAlimentos);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IDatosDTO> getAllDTO() {
        String query = "select new com.proyecto.tropero.core.bd.AlimentoDTO(this.descripcion, this.tipo.descripcion, this.descripcionCaracteristicas, this.estadoCultivo, this.cantMinReposicion, this.stock, this.monto, um.descUniMedida, this.id, al) from Alimento this, UnidadMedida um, Almacen al  " + " where this.almacen = al and this.unidadDeMedida = um";
        return this.getDao().find(query);
    }
}
