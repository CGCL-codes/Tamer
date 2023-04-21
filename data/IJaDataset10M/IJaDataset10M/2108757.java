package co.edu.javeriana.javerpg.cliente.creadores;

import co.edu.javeriana.javerpg.cliente.administradorcliente.AdministradorComando;
import co.edu.javeriana.javerpg.cliente.administradordatoscliente.ConfiguracionCliente;
import co.edu.javeriana.javerpg.cliente.administradordatoscliente.estado.EstadoJuego;
import co.edu.javeriana.javerpg.cliente.administradordatoscliente.estilo.EstiloCliente;
import co.edu.javeriana.javerpg.cliente.administradordatoscliente.estilo.EstiloVentana;
import co.edu.javeriana.javerpg.cliente.interfazvisual.mapa.DibujableMapa;
import co.edu.javeriana.javerpg.cliente.interfazvisual.mapa.DibujableMapaImpl;
import co.edu.javeriana.javerpg.cliente.interfazvisual.ventanas.VentanaJuego;
import co.edu.javeriana.javerpg.modelo.Estadistica;
import co.edu.javeriana.javerpg.modelo.Mapa;
import java.awt.Dimension;

/**
 *
 * @author Administrador
 */
public class CreadorVentanaJuego {

    /** Creates a new instance of CreadorVentanaJuego */
    public CreadorVentanaJuego() {
    }

    public VentanaJuego crear(AdministradorComando administradorComando, EstadoJuego estadoJuego, EstiloCliente estiloCliente, Dimension size, EstiloVentana estiloVentana) {
        VentanaJuego juego = new VentanaJuego(estiloVentana);
        juego.setAdministradorComando(administradorComando);
        juego.setEstadoJuego(estadoJuego);
        juego.setEstiloCliente(estiloCliente);
        DibujableMapa dibujableMapa = new DibujableMapaImpl();
        juego.getPanelMapa().setDibujableMapa(dibujableMapa);
        dibujableMapa.setPanelMapa(juego.getPanelMapa());
        juego.setVisible(true);
        juego.setSize(size);
        try {
            dibujableMapa.start();
        } catch (Exception e) {
            ConfiguracionCliente.getConfiguracionCliente().getEstadoMensaje().enviarMensaje("FATAL", "No iniciar el hijo de dibujo " + e.getMessage());
            e.printStackTrace();
        }
        administradorComando.setEstadoJuego(estadoJuego);
        try {
            administradorComando.start();
        } catch (Exception e) {
            ConfiguracionCliente.getConfiguracionCliente().getEstadoMensaje().enviarMensaje("FATAL", "No iniciar el hijo de dibujo " + e.getMessage());
            e.printStackTrace();
        }
        return juego;
    }
}
