package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.dto.CalificacionDTO;
import com.alquilacosas.dto.PedidoCambioDTO;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.Puntuacion;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface AlquileresOfrecidosBeanLocal {

    public java.util.List<AlquilerDTO> getAlquileresVigentes(int usuarioId);

    public java.util.List<Puntuacion> getPuntuaciones();

    public void registrarCalificacion(int usuarioId, int alquilerId, int puntuacionId, java.lang.String comentario);

    public java.util.List<AlquilerDTO> getAlquileresSinCalificar(int usuarioId);

    public java.util.List<AlquilerDTO> getAlquileresCalificados(int usuarioId);

    public AlquilerDTO modificarAlquiler(AlquilerDTO alquiler, NombrePeriodo periodo, int duracion) throws AlquilaCosasException;

    public boolean cancelarAlquiler(int alquilerId) throws AlquilaCosasException;

    public PedidoCambioDTO getPedidoCambio(int pedidoCambioId);

    public AlquilerDTO responderPedidoCambio(int pedidoCambioId, AlquilerDTO dto, boolean aceptado) throws AlquilaCosasException;

    public java.util.List<java.util.Date> getFechasSinStock(int alquilerId);

    public void registrarReplica(int calificacionId, String comentarioReplica, int usuarioId) throws AlquilaCosasException;

    public CalificacionDTO getCalificacionToma(Integer alquilerId) throws AlquilaCosasException;

    public CalificacionDTO getCalificacionOfrece(Integer alquilerId) throws AlquilaCosasException;
}
