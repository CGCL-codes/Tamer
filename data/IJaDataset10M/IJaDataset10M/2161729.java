package ComunicacionCliente;

import ControlCliente.*;
import mensaje.MsjCrear;
import mensaje.MsjUnion;

public class InterfaceClienteServidor {

    /************atributos*******************************************/
    private ManejadorMensajesEntrada mme;

    private Cliente cliente;

    /************métodos**********************************************/
    public InterfaceClienteServidor(ManejadorMensajesSalida mms, ManejadorMensajesEntrada mme) {
        this.mme = mme;
        mms.setInterfaceClienteServidor(this);
    }

    public void enviar(MsjCrear msjCrear) {
        System.out.println("Enviando mensaje CrearPartida..");
        cliente.enviar(msjCrear);
    }

    public void enviar(MsjUnion msjUnion) {
        System.out.println("enviando mensaje UnirPartida..");
        cliente.enviar(msjUnion);
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
