package com.siasal.view.backing.documentos;

import com.common.exception.BusinessException;
import com.common.exception.InfraestructureException;
import com.common.exception.InternalApplicationException;
import com.common.internationalization.jsf.MessagesJsf;
import com.common.log.ILogger;
import com.common.log.LogFactory;
import com.common.to.ServiceRequestTO;
import com.common.to.ServiceResponseTO;
import com.common.to.ServiceTO;
import com.common.util.jsf.JSFUtils;
import com.commons.deploy.delegate.ServiceDelegate;
import com.siasal.documentos.commons.IdiomaTO;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlPanelGrid;
import oracle.adf.view.faces.component.core.input.CoreInputText;
import oracle.adf.view.faces.component.core.layout.CorePanelBox;
import oracle.adf.view.faces.component.core.layout.CorePanelForm;
import oracle.adf.view.faces.component.core.layout.CorePanelHorizontal;
import oracle.adf.view.faces.component.core.layout.CorePanelPage;
import oracle.adf.view.faces.component.core.nav.CoreCommandButton;
import oracle.adf.view.faces.component.core.nav.CoreCommandMenuItem;
import oracle.adf.view.faces.component.core.nav.CoreMenuTabs;
import oracle.adf.view.faces.component.core.output.CoreMessages;
import oracle.adf.view.faces.component.core.output.CoreObjectSpacer;
import oracle.adf.view.faces.component.html.HtmlBody;
import oracle.adf.view.faces.component.html.HtmlHead;
import oracle.adf.view.faces.component.html.HtmlHtml;

public class CrearIdioma {

    private static String getClassName() {
        return "CrearIdioma";
    }

    private IdiomaTO idioma = new IdiomaTO();

    public String cmdCrear_action() {
        String methodName = "[" + getClassName() + "][cmdCrear_action]";
        ILogger logger = LogFactory.getLogger(this);
        try {
            ServiceRequestTO serviceRequestTO = new ServiceRequestTO("documentos.crearIdioma");
            serviceRequestTO.addParam(idioma);
            ServiceDelegate serviceDelegate = new ServiceDelegate();
            Integer idIdioma = (Integer) serviceDelegate.executeService(serviceRequestTO);
            logger.logDebug("se termino de crear idioma");
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_INFO, "Exito crear Idioma", "Se ha creado exitosamente el Idioma");
            idioma = new IdiomaTO();
        } catch (BusinessException e) {
            if (logger.isDebugEnabled()) {
                logger.logDebug(methodName, e);
            }
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_WARN, "Creacion Idioma", e.getMessage());
        } catch (Throwable e) {
            logger.logError(methodName, e);
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_FATAL, "Creacion Idioma", "Error de la Aplicacion");
        }
        return null;
    }

    public IdiomaTO getIdioma() {
        return idioma;
    }

    public void setIdioma(IdiomaTO idioma) {
        this.idioma = idioma;
    }
}
