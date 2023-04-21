package es.cea;

import javax.ejb.Stateless;

@Stateless
public class ServicioCorreo implements ServicioCorreoRemote {

    public void enviar(String destinatario, String mensaje) {
        System.out.println("Se ha accedido al servicio de envio de email");
    }
}
