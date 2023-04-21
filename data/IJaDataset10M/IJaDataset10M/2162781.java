package comunicacion;

import control.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensaje.*;

public class InterfazEnvia {

    /************atributos*******************************************/
    private ManejadorMensajesEntrada mme;

    private InterfazClienteServidor interfazClienteServidor;

    /************métodos**********************************************/
    public InterfazEnvia(ManejadorMensajesSalida mms, ManejadorMensajesEntrada mme, InterfazClienteServidor interfazClienteServidor) {
        this.mme = mme;
        this.interfazClienteServidor = interfazClienteServidor;
        mms.setInterfaceEnvia(this);
    }

    public InterfazClienteServidor getInterfazClienteServidor() {
        return interfazClienteServidor;
    }

    public void enviar(Mensaje mensaje) {
        try {
            interfazClienteServidor.objout.writeObject(mensaje);
        } catch (IOException ex) {
            System.exit(0);
            Logger.getLogger(InterfazEnvia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
