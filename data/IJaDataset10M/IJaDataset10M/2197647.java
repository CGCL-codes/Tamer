package es.caib.sistra.plugins.pagos;

import java.util.Map;
import es.caib.sistra.plugins.PluginSistraIntf;

/**
 * 
 * Interfaz con la pasarela de pagos
 *
 */
public interface PluginPagosIntf extends PluginSistraIntf {

    /**
	 * Inicia sesi�n de pagos contra la pasarela de pagos
	 * 
	 * @param datosPago Datos del pago
	 * @param sesionSistra Datos para el retorno a SISTRA. Al retornar de la pasarela de pagos
	 * se invocar� a la funci�n comprobarEstadoSesionPago para actualizar el asistente de tramitaci�n.
	 * @return Datos para redirigir la sesi�n a la pasarela de pagos
	 * @throws Exception
	 */
    public SesionPago iniciarSesionPago(DatosPago datosPago, SesionSistra sesionSistra) throws Exception;

    /**
	 * Retoma sesi�n de pagos existente en la pasarela de pagos
	 * 
	 * @param localizador Localizador de la sesi�n de pagos
	 * @param sesionSistra Datos para el retorno a SISTRA. Al retornar de la pasarela de pagos
	 * se invocar� a la funci�n comprobarEstadoSesionPago para actualizar el asistente de tramitaci�n.
	 * @return Datos para redirigir la sesi�n a la pasarela de pagos. Si no existe sesi�n con dicho localizador debe devolver null.
	 * @throws Exception
	 */
    public SesionPago reanudarSesionPago(String localizador, SesionSistra sesionSistra) throws Exception;

    /**
	 * Comprueba estado de una sesi�n de pagos
	 * 
	 * @param localizador Localizador de la sesi�n de pagos
	 * @return Estado sesi�n de pago
	 * @throws Exception
	 */
    public EstadoSesionPago comprobarEstadoSesionPago(String localizador) throws Exception;

    /**
	 * Indica al plugin de pagos que puede eliminar la informaci�n referente a la sesi�n de pagos
	 * <br/>
	 * El plugin entonces podr� eliminar los datos de la sesi�n:
	 * <ul>
	 * <li>Pago en curso: cancelar� el proceso de pago</li>
	 * <li>Pago pendiente confirmaci�n: no se permitir� finalizar la sesi�n</li>
	 * <li>Pago confirmado: dar� por acabado el proceso de pago</li>
	 * 
	 * @param localizador Localizador de la sesi�n de pagos
	 * @throws Exception
	 */
    public void finalizarSesionPago(String localizador) throws Exception;

    /**
	 * Permite consultar el importe de una tasa
	 * @param idTasa identificador de la tasa
	 * @param paramTasa Map con par�metros para el c�clulo de la tasa
	 * @return Importe en cents de la tasa (Ej: 10,20� = 1020 cents)
	 * @throws Exception
	 */
    public long consultarImporteTasa(String idTasa, Map paramTasa) throws Exception;
}
