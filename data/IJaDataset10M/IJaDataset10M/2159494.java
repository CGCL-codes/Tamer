package com.iver.cit.gvsig.fmap;

import org.gvsig.remoteClient.wcs.WCSStatus;
import com.iver.utiles.ExceptionDescription;

public class WCSDriverExceptionType extends ExceptionDescription {

    WCSStatus status;

    public WCSDriverExceptionType() {
        super(60, "Error al acceder a un servicio WCS");
    }

    public String getHtmlErrorMessage() {
        String message = "<p><b>Error en una petici�n a servidor WCS</b></p>";
        message += "Informaci�n adicional:<br>";
        message += "Mensaje del error: " + status.getMessage();
        message += "Direcci�n: " + status.getOnlineResource();
        message += "<br> Par�metros: " + status.getParameters();
        return message;
    }

    public WCSStatus getWcsStatus() {
        return status;
    }

    public void setWcsStatus(WCSStatus wcsStatus) {
        this.status = wcsStatus;
    }
}
