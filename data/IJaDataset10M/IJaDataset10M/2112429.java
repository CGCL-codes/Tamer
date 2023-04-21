package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.ComentarioDTO;
import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.entity.EstadoPublicacion.NombreEstadoPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ignaciogiagante
 */
@Local
public interface PublicacionBeanLocal {

    public Integer registrarPublicacion(String titulo, String descripcion, Date fechaDesde, Date fechaHasta, boolean destacada, int cantidad, int usuarioId, int categoria, Double precioHora, Double precioDia, Double precioSemana, Double precioMes, List<byte[]> imagenes, int periodoMinimo, int periodoMinimoFK, Integer periodoMaximo, Integer periodoMaximoFk, double latitud, double longitud) throws AlquilaCosasException;

    public PublicacionDTO getPublicacion(int id);

    public List<ComentarioDTO> getPreguntas(int publicationId);

    public void setPregunta(int publicacionId, ComentarioDTO nuevaPregunta) throws AlquilaCosasException;

    public List<ComentarioDTO> getPreguntasSinResponder(int usuarioId);

    public void setRespuesta(ComentarioDTO preguntaConRespuesta) throws AlquilaCosasException;

    public PublicacionDTO getDatosPublicacion(int publicacionId) throws AlquilaCosasException;

    public void actualizarPublicacion(int publicacionId, String titulo, String descripcion, Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad, int usuarioId, int categoria, Double precioHora, Double precioDia, Double precioSemana, Double precioMes, List<byte[]> imagenesAgregar, List<Integer> imagenesABorrar, int periodoMinimo, int periodoMinimoFk, Integer periodoMaximo, Integer periodoMaximoFk, NombreEstadoPublicacion estadoPublicacion) throws AlquilaCosasException;

    public List<Periodo> getPeriodos();

    public List<Date> getFechasSinStock(int publicationId, int cantidad) throws AlquilaCosasException;

    public void crearPedidoAlquiler(int publicationId, int usuarioId, Date beginDate, Date endDate, double monto, int cantidad) throws AlquilaCosasException;

    public double getUserRate(UsuarioDTO propietario);
}
