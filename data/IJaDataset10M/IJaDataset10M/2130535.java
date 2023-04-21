package es.devel.opentrats.model;

import es.devel.opentrats.service.impl.SMSServiceImpl;
import java.io.Serializable;

/**
 *
 * @author Fran Serrano
 */
public class SMSMessage extends Object implements Serializable {

    private static final long serialVersionUID = -1659659381319540529L;

    private SMSRecipientList destinatarios;

    private SMSRecipient destinatario;

    private String texto;

    /** Crea una nueva instancia de Mensaje */
    public SMSMessage() {
        destinatarios = new SMSRecipientList();
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public SMSRecipient getDestinatario() {
        return destinatario;
    }

    public void addDestinatario(SMSRecipient destinatario) {
        this.destinatarios.add(destinatario);
    }

    public SMSRecipientList getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(SMSRecipientList destinatarios) {
        this.destinatarios = destinatarios;
    }
}
