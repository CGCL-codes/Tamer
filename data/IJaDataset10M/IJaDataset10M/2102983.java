package javaframework.capadeaplicación.mensajes.rastreo.rastreador;

import javaframework.ClaseAbstractaBase;
import javaframework.capadeaplicación.mensajes.rastreo.MensajeEstadoDeEjecución;
import javaframework.capadeaplicación.mensajes.rastreo.mensajero.Mensajero;

/**
 * Representa una entidad de seguimiento de la ejecución de la aplicación. Permite recoger
 * información acerca de la ejecución de los métodos y enviarla a un destino.
 *
 * <br/><br/>
 *
 * <b><u>Notas de diseño</u></b><br/>
 * <b>· Fecha de creación:</b> 01/01/2007<br/>
 * <b>· Revisiones:</b> 23/12/2009, 03/05/2010, 20/02/2011, 21/03/2011<br/><br/>
 * <b><u>Estado</u></b><br/>
 * <b>· Depurado:</b> Sí<br/>
 * <b>· Pruebas estructurales:</b> -<br/>
 * <b>· Pruebas funcionales:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw@yahoo.es) (c) 2011
 * @version JavaFramework.0.0.1.desktop-web.es
 * @version InterfazRastreador.0.0.1
 * @since JavaFramework.0.0.1.desktop-web.es
 * @see <a href=””></a>
 *
 */
public final class Rastreador extends ClaseAbstractaBase implements InterfazRastreador {

    private InterfazRastreador.NivelesDeRastreo nivelDeRastreo;

    private int tamañoBufferEstados;

    private java.util.ArrayList<MensajeEstadoDeEjecución> estadosDeEjecución;

    private DestinoLog[] destinosLog;

    private void setNivelDeRastreo(final NivelesDeRastreo nivelDeRastreo) {
        this.nivelDeRastreo = nivelDeRastreo;
    }

    private NivelesDeRastreo getNivelDeRastreo() {
        return this.nivelDeRastreo;
    }

    private void setTamañoBufferEstados(final int tamañoBufferEstados) {
        this.tamañoBufferEstados = tamañoBufferEstados;
    }

    private int getTamañoBufferEstados() {
        return this.tamañoBufferEstados;
    }

    private void setEstadosDeEjecución(final java.util.ArrayList<MensajeEstadoDeEjecución> estadoDeEjecución) {
        this.estadosDeEjecución = estadoDeEjecución;
    }

    private java.util.ArrayList<MensajeEstadoDeEjecución> getEstadosDeEjecución() {
        return this.estadosDeEjecución;
    }

    private void setDestinosLog(final DestinoLog[] destinosLog) {
        this.destinosLog = destinosLog;
    }

    private DestinoLog[] getDestinosLog() {
        return this.destinosLog;
    }

    /**
	 * Instancia un objeto <code>Rastreador</code>.
	 *
	 * <br/><br/>
	 *
	 * @param nivelDeRastreo		Objeto del tipo <code>NivelesDeRastreo</code> que indica bajo qué condiciones 
	 *								se efectuará el rastreo de los métodos.
	 *
	 * @param tamañoBufferEstados	Nº máximo de mensajes que almacenará el buffer de estados del objeto Rastreador. 
	 *								Al sobrepasar este valor, todos los mensajes contenidos en el buffer son entregados
	 *								a los ditintos destinos.
	 *
	 * @param destinosLog			Array de objetos <code>DestinoLog</code> que indican las ubicaciones de entrega de
	 *								los mensajes de rastreo.
	 */
    public Rastreador(final NivelesDeRastreo nivelDeRastreo, final int tamañoBufferEstados, final DestinoLog[] destinosLog) {
        try {
            this.configurarRastreador(nivelDeRastreo, tamañoBufferEstados, destinosLog);
        } catch (Exception e) {
        }
    }

    private void configurarRastreador(final NivelesDeRastreo nivelDeRastreo, final int tamañoBufferEstados, final DestinoLog[] destinosLog) {
        try {
            this.setNivelDeRastreo(nivelDeRastreo);
            this.setTamañoBufferEstados(tamañoBufferEstados);
            this.setEstadosDeEjecución(new java.util.ArrayList<MensajeEstadoDeEjecución>());
            this.setDestinosLog(destinosLog);
        } catch (Exception e) {
        }
    }

    private void volcarEstadoEnDestinosYLiberarBuffer() {
        try {
            final Mensajero m = new Mensajero(null);
            for (MensajeEstadoDeEjecución mee : this.getEstadosDeEjecución()) {
                m.entregarMensaje(mee, this.getDestinosLog());
            }
            this.getEstadosDeEjecución().clear();
        } catch (Exception e) {
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final void guardarEstadoEnBuffer(final MensajeEstadoDeEjecución mensaje) {
        try {
            boolean registrarEstado = false;
            switch(this.getNivelDeRastreo()) {
                case NO_RASTREAR:
                    registrarEstado = false;
                    break;
                case TRAZAS:
                    if (mensaje.getTraza() != null) registrarEstado = true; else registrarEstado = false;
                case SÓLO_EXCEPCIONES:
                    if (mensaje.getExcepción() != null) registrarEstado = true; else registrarEstado = false;
                    break;
                case TODO:
                    registrarEstado = true;
                    break;
            }
            if (registrarEstado) {
                this.getEstadosDeEjecución().add(mensaje);
                if (this.getEstadosDeEjecución().size() >= this.getTamañoBufferEstados()) this.volcarEstadoEnDestinosYLiberarBuffer();
            }
        } catch (Exception e) {
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void liberarRecursos() {
        try {
            if (this.getEstadosDeEjecución() != null) {
                this.getEstadosDeEjecución().clear();
                this.setEstadosDeEjecución(null);
            }
            this.setNivelDeRastreo(null);
        } catch (Exception e) {
        }
    }
}
