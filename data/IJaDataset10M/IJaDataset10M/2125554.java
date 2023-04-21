package server;

import util.ConexionEstablecida;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class ProxyServer {

    private ServerSocket servsocket = null;

    private int port;

    private boolean conectado = false;

    private ConexionEstablecida conexion = null;

    private boolean log;

    public ProxyServer(ServerSocket servsocket) {
        this.servsocket = servsocket;
    }

    public ProxyServer(int port, boolean log) {
        this.port = port;
        this.log = log;
    }

    public boolean estaConectado() {
        if (conexion != null) return conexion.estaConectado(); else return false;
    }

    public void conectar() throws IOException {
        while (servsocket == null) {
            servsocket = new ServerSocket(port);
            Socket clisocket = servsocket.accept();
            conexion = new ConexionEstablecida(clisocket, log);
            conectado = true;
        }
    }

    public String recibirMensaje() throws IOException {
        return conexion.recibirMensaje();
    }

    public void enviarMensaje(String mensaje) throws IOException {
        conexion.enviarMensaje(mensaje);
    }

    public void desconectar() throws IOException {
        if (this.conectado) {
            servsocket.close();
            if (conexion.estaConectado()) conexion.cerrarConexion();
            conectado = false;
        }
    }
}
