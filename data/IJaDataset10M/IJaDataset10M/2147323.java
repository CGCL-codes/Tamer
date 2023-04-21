package com.siasal.view.backing.documentos;

import com.common.exception.BusinessException;
import com.common.exception.InternalApplicationException;
import com.common.log.ILogger;
import com.common.log.LogFactory;
import com.common.to.ServiceRequestTO;
import com.common.util.jasper.JasperUtils;
import com.common.util.jsf.JSFUtils;
import com.commons.deploy.delegate.ServiceDelegate;
import com.siasal.documentos.business.DocumentoBasico;
import com.siasal.documentos.business.Ejemplar;
import com.siasal.documentos.business.Revista;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public class ImprimirCodigoEjemplar {

    private static String getClassName() {
        return "ImprimirCodigoEjemplar";
    }

    private String codigoEjemplar;

    public String getCodigoEjemplar() {
        return codigoEjemplar;
    }

    public void setCodigoEjemplar(String codigoEjemplar) {
        this.codigoEjemplar = codigoEjemplar;
    }

    public String imprimir() {
        String[] datosEjemplar = buscarDatosEjemplar();
        if (datosEjemplar == null) {
            return null;
        }
        Map parameters = new HashMap();
        parameters.put("REPORT_NAME", "C�digo de Ejemplar");
        JasperPrint reportout = JasperUtils.makereport("/documentos/ImprimirCodigoEjemplar.jasper", initializeMapCollection(datosEjemplar), parameters);
        byte[] pdf = null;
        try {
            pdf = JasperExportManager.exportReportToPdf(reportout);
        } catch (JRException e) {
            throw new InternalApplicationException("Error al exportar a pdf", e);
        }
        FacesContext faces = javax.faces.context.FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.setContentLength(pdf.length);
        response.setHeader("Content-disposition", "attachment; filename=\"" + "C�digo de Ejemplar" + ".pdf\"");
        ServletOutputStream out;
        try {
            out = response.getOutputStream();
            out.write(pdf);
        } catch (IOException e) {
            throw new InternalApplicationException("Error al escribir el reporte", e);
        }
        faces.responseComplete();
        return null;
    }

    private String[] buscarDatosEjemplar() {
        String methodName = "[" + getClassName() + "][cmdCrear_action]";
        ILogger logger = LogFactory.getLogger(this);
        try {
            ServiceRequestTO serviceRequestTO = new ServiceRequestTO("documentos.buscarEjemplarParaImprimir");
            serviceRequestTO.addParam(getCodigoEjemplar());
            ServiceDelegate serviceDelegate = new ServiceDelegate();
            String[] datosEjemplar = (String[]) serviceDelegate.executeService(serviceRequestTO);
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_INFO, "Exito al imprimir el c�digo del Ejemplar", "Se ha impreso el c�digo del Ejemplar");
            return datosEjemplar;
        } catch (BusinessException e) {
            if (logger.isDebugEnabled()) {
                logger.logDebug(methodName, e);
            }
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_WARN, "Impresi�n de Ejemplar", e.getMessage());
        } catch (Throwable e) {
            logger.logError(methodName, e);
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_FATAL, "Impresi�n del Ejemplar", "Error de la Aplicacion");
        }
        return null;
    }

    private Collection initializeMapCollection(String datosEjemplar[]) {
        ArrayList reportRows = new ArrayList();
        HashMap rowTmp = new HashMap();
        rowTmp.put("codigoEjemplar", "00" + datosEjemplar[0]);
        rowTmp.put("nombreDocumento", datosEjemplar[1]);
        reportRows.add(rowTmp);
        return reportRows;
    }
}
