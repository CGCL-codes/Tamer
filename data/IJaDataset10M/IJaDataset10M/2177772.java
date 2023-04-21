package org.opensih.documentSourceXDR.management;

public class Parameters implements ParametersManagement {

    private String endPointRecipientXDR = "http://172.17.99.246:8080/RecipientXDR-ear-1.0-RecipientXDR-ejb-1.0/DocumentRepository?wsdl";

    private int minutosTimer = 1;

    private String rutaImportarCDA = "/importCDA/";

    public void create() throws Exception {
        System.out.println("Parameters - Creating");
    }

    public void start() throws Exception {
        System.out.println("Parameters - Starting");
    }

    public void stop() {
        System.out.println("Parameters - Stopping");
    }

    public void destroy() {
        System.out.println("Parameters - Destroying");
    }

    public String getEndPointRecipientXDR() {
        return endPointRecipientXDR;
    }

    public void setEndPointRecipientXDR(String endPointRecipientXDR) {
        this.endPointRecipientXDR = endPointRecipientXDR;
    }

    public int getMinutosTimer() {
        return minutosTimer;
    }

    public void setMinutosTimer(int minutosTimer) {
        this.minutosTimer = minutosTimer;
    }

    public String getRutaImportarCDA() {
        return rutaImportarCDA;
    }

    public void setRutaImportarCDA(String rutaImportarCDA) {
        this.rutaImportarCDA = rutaImportarCDA;
    }
}
