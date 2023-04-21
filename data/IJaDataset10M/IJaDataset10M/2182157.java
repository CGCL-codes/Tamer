package ar.com.larreta.vista.client;

import ar.com.larreta.controlador.client.RetornoDeLlamada;
import ar.com.larreta.vista.client.logs.Logger;

public abstract class RetornoDeLlamadaConVentana extends RetornoDeLlamada {

    protected static Logger logger = new Logger(RetornoDeLlamadaConVentana.class);

    protected Ventana ventana;

    public RetornoDeLlamadaConVentana() {
    }

    public RetornoDeLlamadaConVentana(Ventana ventana) {
        this.ventana = ventana;
    }

    public void fallo(Throwable error) {
        logger.error(error.getMessage());
        DialogError.mostrar("Error", Mensajes.getInstancia().get(error.getMessage()));
    }
}
