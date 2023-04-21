package uy.gub.imm.sae.business.ejb.facade;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import uy.gub.imm.sae.common.VentanaDeTiempo;
import uy.gub.imm.sae.entity.DatoASolicitar;
import uy.gub.imm.sae.entity.DatoReserva;
import uy.gub.imm.sae.entity.Disponibilidad;
import uy.gub.imm.sae.entity.Recurso;
import uy.gub.imm.sae.entity.Reserva;
import uy.gub.imm.sae.entity.ValidacionPorRecurso;
import uy.gub.imm.sae.exception.ApplicationException;
import uy.gub.imm.sae.exception.BusinessException;
import uy.gub.imm.sae.exception.ErrorValidacionException;
import uy.gub.imm.sae.exception.UserException;
import uy.gub.imm.sae.exception.ValidacionException;
import uy.gub.imm.sae.exception.ValidacionPorCampoException;
import uy.gub.imm.sae.exception.WarningValidacionException;

@Local
public interface AgendarReservasHelperLocal {

    public VentanaDeTiempo obtenerVentanaCalendarioEstatica(Recurso recurso);

    public VentanaDeTiempo obtenerVentanaCalendarioAjustada(Recurso r, VentanaDeTiempo ventana);

    public VentanaDeTiempo obtenerVentanaCalendarioExtendida(Recurso r, VentanaDeTiempo ventana);

    public List<Object[]> obtenerCuposAsignados(Recurso r, VentanaDeTiempo ventana);

    public List<Object[]> obtenerCuposConsumidos(Recurso r, VentanaDeTiempo ventana);

    public List<Integer> obtenerCuposXDia(VentanaDeTiempo ventana, List<Object[]> cuposAsignados, List<Object[]> cuposConsumidos);

    public Reserva crearReservaPendiente(Disponibilidad d);

    public boolean chequeoCupoNegativo(Disponibilidad d);

    public List<DatoASolicitar> obtenerDatosASolicitar(Recurso r);

    public List<ValidacionPorRecurso> obtenerValidacionesPorRecurso(Recurso r);

    public void validarDatosReservaBasico(List<DatoASolicitar> campos, Map<String, DatoReserva> valores) throws BusinessException, ValidacionException;

    public void validarDatosReservaExtendido(List<ValidacionPorRecurso> validaciones, List<DatoASolicitar> campos, Map<String, DatoReserva> valores, Boolean noLanzarWarning) throws ApplicationException, BusinessException, ErrorValidacionException, WarningValidacionException;

    public List<Reserva> validarDatosReservaPorClave(Recurso recurso, Reserva reserva, List<DatoASolicitar> campos, Map<String, DatoReserva> valores) throws BusinessException;

    public void validarDatosRequeridosReserva(List<DatoASolicitar> campos, Map<String, DatoReserva> valores) throws BusinessException, UserException;

    public void validarTipoDatosReserva(List<DatoASolicitar> campos, Map<String, DatoReserva> valores) throws BusinessException, ValidacionPorCampoException;
}
