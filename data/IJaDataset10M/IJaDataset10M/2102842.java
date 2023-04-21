package javaframework.capadeaccesoadatos.io.streams.datostipados;

import java.io.DataInputStream;
import java.util.Date;
import javaframework.capadeaccesoadatos.io.streams.ClaseAbstractaStreamLectura;
import javaframework.capadeaplicación.mensajes.rastreo.rastreador.Rastreador;

/**
 * Miembros públicos para realizar operaciones de lectura de datos tipados
 * a través de un stream.
 *
 * <br/><br/>
 *
 * <b><u>Notas de diseño</u></b><br/>
 * <b>· Fecha de creación:</b> 01/01/2007<br/>
 * <b>· Revisiones:</b><br/><br/>
 * <b><u>Estado</u></b><br/>
 * <b>· Depurado:</b> -<br/>
 * <b>· Pruebas estructurales:</b> -<br/>
 * <b>· Pruebas funcionales:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw@yahoo.es) (c) 2011
 * @version JavaFramework.0.0.1.desktop-web.es
 * @version InterfazLecturaDatosTipados.0.0.1
 * @since JavaFramework.0.0.1.desktop-web.es
 * @see <a href=””></a>
 *
 */
public final class StreamLecturaDatosTipados extends ClaseAbstractaStreamLectura implements InterfazLecturaDatosTipados {

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int getBytesLeídosEnÚltimaLectura() {
        return super.getBytesLeídosEnÚltimaLectura();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int getTamañoArrayLecturaOptimizado() {
        return super.getTamañoArrayLecturaOptimizado();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Integer getDatosDisponibles() {
        return super.getDatosDisponibles();
    }

    /**
	 * Instancia un objeto <code>StreamLecturaDatosTipados</code>
	 *
	 * <br/><br/>
	 *
	 * @param stream		Objeto del tipo <code>ClaseAbstractaStreamLectura</code> que representa el
	 *						stream origen desde el que se leen los datos. El objeto instanciado interpretará
	 *						esos datos como datos tipados.
	 *
	 * @param rastreador	Objeto <code>Rastreador</code> que efectuará el seguimiento del objeto. Si
	 *						se especifica un valor <code>null</code>, no se efectúa el seguimiento.
	 *
	 */
    public StreamLecturaDatosTipados(ClaseAbstractaStreamLectura stream, Rastreador rastreador) {
        super(rastreador);
        try {
            DataInputStream DIS = new DataInputStream(stream.getStreamLectura());
            super.setStreamLectura(DIS);
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "StreamLecturaDatosTipados", null, null, stream, rastreador);
        } catch (Exception e) {
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "StreamLecturaDatosTipados", e, null, stream, rastreador);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final byte[] leerArrayBytes(final int bytesALeer) {
        try {
            final byte BYTES_LEÍDOS[] = new byte[bytesALeer];
            ((java.io.DataInputStream) (this.getStreamLectura())).read(BYTES_LEÍDOS);
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerArrayBytes", null, null, bytesALeer);
            return BYTES_LEÍDOS;
        } catch (Exception e) {
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerArrayBytes", e, null, bytesALeer);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final Boolean leerBoolean() {
        try {
            final boolean VALOR_DE_RETORNO = ((java.io.DataInputStream) (this.getStreamLectura())).readBoolean();
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerBoolean", null, null);
            return VALOR_DE_RETORNO;
        } catch (Exception e) {
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerBoolean", e, null);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final Character leerChar() {
        try {
            final char VALOR_DE_RETORNO = ((java.io.DataInputStream) (this.getStreamLectura())).readChar();
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerChar", null, null);
            return VALOR_DE_RETORNO;
        } catch (Exception e) {
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerChar", e, null);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final Double leerDouble() {
        try {
            double VALOR_DE_RETORNO = ((java.io.DataInputStream) (this.getStreamLectura())).readDouble();
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerDouble", null, null);
            return VALOR_DE_RETORNO;
        } catch (Exception e) {
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerDouble", e, null);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final Float leerFloat() {
        try {
            float VALOR_DE_RETORNO = ((java.io.DataInputStream) (this.getStreamLectura())).readFloat();
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerFloat", null, null);
            return VALOR_DE_RETORNO;
        } catch (Exception e) {
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerFloat", e, null);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final Integer leerInt() {
        try {
            int VALOR_DE_RETORNO = ((java.io.DataInputStream) (this.getStreamLectura())).readInt();
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerInt", null, null);
            return VALOR_DE_RETORNO;
        } catch (Exception e) {
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerInt", e, null);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final Short leerShort() {
        try {
            short VALOR_DE_RETORNO = ((java.io.DataInputStream) (this.getStreamLectura())).readShort();
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerShort", null, null);
            return VALOR_DE_RETORNO;
        } catch (Exception e) {
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerShort", e, null);
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final String leerString() {
        try {
            String VALOR_DE_RETORNO = ((java.io.DataInputStream) (this.getStreamLectura())).readUTF();
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerString", null, null);
            return VALOR_DE_RETORNO;
        } catch (Exception e) {
            registrarEstado(new Date(), this.getClass().getCanonicalName(), "leerString", e, null);
            return null;
        }
    }
}
