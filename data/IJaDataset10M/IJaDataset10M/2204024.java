package es.cim.ESBClient.v1.envios;

import java.util.List;

public class MensajeEnvioSMS {

    protected List<String> destinatarios;

    protected String texto;

    public List<String> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<String> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
