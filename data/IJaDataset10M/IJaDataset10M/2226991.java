package com.siasal.view.backing.usuariosConsulta;

import com.common.exception.BusinessException;
import com.common.log.ILogger;
import com.common.log.LogFactory;
import com.common.to.ServiceRequestTO;
import com.common.util.jsf.JSFUtils;
import com.commons.deploy.delegate.ServiceDelegate;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlForm;
import oracle.adf.view.faces.component.core.layout.CorePanelBox;
import oracle.adf.view.faces.component.core.layout.CorePanelForm;
import oracle.adf.view.faces.component.core.layout.CorePanelPage;
import oracle.adf.view.faces.component.html.HtmlBody;
import oracle.adf.view.faces.component.html.HtmlHead;
import oracle.adf.view.faces.component.html.HtmlHtml;

public class HorarioAtencion {

    private static String getClassName() {
        return "HorarioAtencion";
    }

    private List horarioSalaLectura;

    public HorarioAtencion() {
        String methodName = "[" + getClassName() + "][HorarioAtencion]";
        ILogger logger = LogFactory.getLogger(this);
        try {
            ServiceRequestTO serviceRequestTO = new ServiceRequestTO("bibliotecarios.revisarHorariosBibliotecarios");
            ServiceDelegate serviceDelegate = new ServiceDelegate();
            setHorarioSalaLectura((List) serviceDelegate.executeService(serviceRequestTO));
        } catch (BusinessException e) {
            if (logger.isDebugEnabled()) {
                logger.logDebug(methodName, e);
            }
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_WARN, "Revisar Horario Atenci�n", e.getMessage());
        } catch (Throwable e) {
            logger.logError(methodName, e);
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_FATAL, "Revisar Horario Atenci�n", "Error de la Aplicacion");
        }
    }

    public List getHorarioSalaLectura() {
        return horarioSalaLectura;
    }

    public void setHorarioSalaLectura(List horarioSalaLectura) {
        this.horarioSalaLectura = horarioSalaLectura;
    }
}
