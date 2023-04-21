package galerias.formularios;

import java.io.Serializable;
import galerias.colas.EnviarContactoQueue;
import galerias.validadores.CorreoElectronicoValidador;
import galerias.validadores.MotivoValidador;
import galerias.validadores.NombreContactoValidador;
import javax.annotation.Resource;
import javax.faces.event.ActionEvent;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.primefaces.context.RequestContext;

public class ContactoFormulario implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -979229424665368564L;

    private String correoElectronicoCliente = null;

    private String nombreCliente = null;

    private String motivo = null;

    private boolean mostrarPanel = false;

    @Resource(mappedName = "enviarContactoQueue")
    private Queue enviarContactoQueue;

    @Resource(mappedName = "jms/QueueConnectionFactory")
    private QueueConnectionFactory queueConnectionFactory;

    public void accionEnviarContacto(ActionEvent actionEvent) {
        System.out.println("accionEnviarContacto");
        Boolean validacionMotivo = (Boolean) RequestContext.getCurrentInstance().getCallbackParams().get(MotivoValidador.LLAVE_BANDERA_VALIDACION);
        Boolean validacionCorreoElectronico = (Boolean) RequestContext.getCurrentInstance().getCallbackParams().get(CorreoElectronicoValidador.LLAVE_BANDERA_VALIDACION);
        Boolean validacionContactoNombre = (Boolean) RequestContext.getCurrentInstance().getCallbackParams().get(NombreContactoValidador.LLAVE_BANDERA_VALIDACION);
        boolean mensajeEnviado = false;
        boolean mensajeEnviadoExitoso = false;
        if (!validacionMotivo.booleanValue() && !validacionCorreoElectronico.booleanValue() && !validacionContactoNombre.booleanValue()) {
            mensajeEnviado = true;
            QueueConnection queueConnection = null;
            try {
                queueConnection = this.queueConnectionFactory.createQueueConnection();
                queueConnection.start();
                QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                QueueSender sender = queueSession.createSender(this.enviarContactoQueue);
                TextMessage msg = queueSession.createTextMessage();
                msg.setStringProperty(EnviarContactoQueue.BANDERA_CORREO_ELECTRONICO_CLIENTE, this.correoElectronicoCliente);
                msg.setStringProperty(EnviarContactoQueue.BANDERA_MOTIVO, this.motivo);
                msg.setStringProperty(EnviarContactoQueue.BANDERA_NOMBRE_CLIENTE, this.nombreCliente);
                sender.send(msg);
                mensajeEnviadoExitoso = true;
                this.correoElectronicoCliente = null;
                this.nombreCliente = null;
                this.motivo = null;
            } catch (JMSException e) {
                e.printStackTrace();
                mensajeEnviadoExitoso = false;
            } finally {
                try {
                    if (queueConnection != null) {
                        queueConnection.close();
                    }
                } catch (JMSException e) {
                }
            }
        }
        RequestContext.getCurrentInstance().addCallbackParam("mensajeEnviado", new Boolean(mensajeEnviado));
        RequestContext.getCurrentInstance().addCallbackParam("mensajeEnviadoExitoso", new Boolean(mensajeEnviadoExitoso));
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getCorreoElectronicoCliente() {
        return correoElectronicoCliente;
    }

    public void setCorreoElectronicoCliente(String correoElectronicoCliente) {
        this.correoElectronicoCliente = correoElectronicoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public boolean isMostrarPanel() {
        return mostrarPanel;
    }

    public void setMostrarPanel(boolean mostrarPanel) {
        this.mostrarPanel = mostrarPanel;
    }
}
