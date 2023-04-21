package com.siasal.view.backing.adminUsuariosConsulta;

import com.common.exception.BusinessException;
import com.common.log.ILogger;
import com.common.log.LogFactory;
import com.common.to.ServiceRequestTO;
import com.common.util.jsf.JSFUtils;
import com.commons.deploy.delegate.ServiceDelegate;
import com.siasal.documentos.business.Ejemplar;
import com.siasal.usuarios.business.Donacion;
import com.siasal.usuarios.business.UsuarioConsulta;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import oracle.adf.view.faces.event.ReturnEvent;

public class GestionarDonacion {

    private Ejemplar ejemplar = new Ejemplar();

    private String tipoEjemplar;

    private Map tiposEjemplares;

    private String tipoDonacion;

    private Map tiposDonaciones;

    private boolean visiblePanel = false;

    private boolean visiblePanelDon = false;

    private boolean modify;

    private Donacion donacion = new Donacion();

    public GestionarDonacion() {
        setTiposDonaciones(new HashMap());
        getTiposDonaciones().put("EJEMPLAR", "EJEMPLAR");
        getTiposDonaciones().put("DINERO", "DINERO");
        setTiposEjemplares(new HashMap());
        getTiposEjemplares().put("Libro", "Libro");
        getTiposEjemplares().put("Tesis", "Tesis");
        getTiposEjemplares().put("Revista", "Revista");
        getTiposEjemplares().put("Video", "Video");
    }

    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    private static String getClassName() {
        return "GestionarDonacion";
    }

    public void handleReturnEscogerCodigo(ReturnEvent event) {
        String methodName = "[" + getClassName() + "][handleReturnEscogerCodigo]";
        ILogger logger = LogFactory.getLogger(this);
        if (event.getReturnValue() == null) {
            return;
        }
        String codigoTmp = ((String) event.getReturnValue()).trim();
        try {
            ServiceRequestTO serviceRequestTO = new ServiceRequestTO("adminUsuarioConsulta.buscarUsuarioConsulta");
            serviceRequestTO.addParam(codigoTmp);
            ServiceDelegate serviceDelegate = new ServiceDelegate();
            UsuarioConsulta usuarioConsultaTmp = (UsuarioConsulta) serviceDelegate.executeService(serviceRequestTO);
            getDonacion().setUsuario(usuarioConsultaTmp);
            if (logger.isDebugEnabled()) {
                logger.logDebug(methodName + " se encontro usuario:" + getDonacion().getUsuario().getNombre());
            }
        } catch (BusinessException e) {
            if (logger.isDebugEnabled()) {
                logger.logDebug(methodName, e);
            }
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_WARN, "No se encuentra Usuario de Consulta", e.getMessage());
        } catch (Throwable e) {
            logger.logError(methodName, e);
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_FATAL, "Usuario de Consulta", "Error de la Aplicacion");
        }
    }

    public void registrarDonacionEjemplar() {
        String methodName = "[" + getClassName() + "][registrarDonacionEjemplar]";
        ILogger logger = LogFactory.getLogger(this);
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String userTmp = facesContext.getExternalContext().getRemoteUser();
            ServiceRequestTO serviceRequestTO = new ServiceRequestTO("adminUsuarioConsulta.registrarDonacionEjemplar");
            serviceRequestTO.addParam(getEjemplar());
            serviceRequestTO.addParam(getDonacion());
            serviceRequestTO.addParam(userTmp);
            ServiceDelegate serviceDelegate = new ServiceDelegate();
            serviceDelegate.executeService(serviceRequestTO);
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_INFO, "Exito al registrar Donaci�n de Ejemplar", "Se ha registrado exitosamente la donaci�n del Ejemplar");
            setEjemplar(new Ejemplar());
            setDonacion(new Donacion());
        } catch (BusinessException e) {
            if (logger.isDebugEnabled()) {
                logger.logDebug(methodName, e);
            }
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_WARN, "Registrar Donaci�n Ejemplar", e.getMessage());
        } catch (Throwable e) {
            logger.logError(methodName, e);
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_FATAL, "Registrar Donaci�n Ejemplar", "Error de la Aplicacion");
        }
    }

    public void registrarDonacionDinero() {
        String methodName = "[" + getClassName() + "][registrarDonacionDinero]";
        ILogger logger = LogFactory.getLogger(this);
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String userTmp = facesContext.getExternalContext().getRemoteUser();
            ServiceRequestTO serviceRequestTO = new ServiceRequestTO("adminUsuarioConsulta.registrarDonacionDinero");
            serviceRequestTO.addParam(getDonacion());
            serviceRequestTO.addParam(userTmp);
            ServiceDelegate serviceDelegate = new ServiceDelegate();
            serviceDelegate.executeService(serviceRequestTO);
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_INFO, "Exito al registrar la donaci�n de dinero", "Se ha registrado exitosamente las donaci�n de dinero");
            donacion = new Donacion();
            setVisiblePanelDon(false);
            setModify(false);
        } catch (BusinessException e) {
            if (logger.isDebugEnabled()) {
                logger.logDebug(methodName, e);
            }
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_WARN, "Error al registrar donaci�n dinero", e.getMessage());
        } catch (Throwable e) {
            logger.logError(methodName, e);
            JSFUtils.addFacesErrorMessage(FacesMessage.SEVERITY_FATAL, "Registrar Donaci�n de Dinero", "Error de la Aplicacion");
        }
    }

    public void setTipoDonacion(String tipoDonacion) {
        this.tipoDonacion = tipoDonacion;
    }

    public String getTipoDonacion() {
        return tipoDonacion;
    }

    public void setTiposDonaciones(Map tiposDonaciones) {
        this.tiposDonaciones = tiposDonaciones;
    }

    public Map getTiposDonaciones() {
        return tiposDonaciones;
    }

    public void enablePanel(ValueChangeEvent valueChangeEvent) {
        String ret = (String) valueChangeEvent.getNewValue();
        if (ret == "EJEMPLAR") {
            setVisiblePanel(true);
            setVisiblePanelDon(false);
            getDonacion().setTipo(donacion.EJEMPLAR);
        } else {
            setVisiblePanel(false);
            setVisiblePanelDon(true);
            getDonacion().setTipo(donacion.DINERO);
        }
    }

    public void setVisiblePanel(boolean visiblePanel) {
        this.visiblePanel = visiblePanel;
    }

    public boolean isVisiblePanel() {
        return visiblePanel;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public boolean isModify() {
        return modify;
    }

    public void setTiposEjemplares(Map tiposEjemplares) {
        this.tiposEjemplares = tiposEjemplares;
    }

    public Map getTiposEjemplares() {
        return tiposEjemplares;
    }

    public void setTipoEjemplar(String tipoEjemplar) {
        this.tipoEjemplar = tipoEjemplar;
    }

    public String getTipoEjemplar() {
        return tipoEjemplar;
    }

    public void setVisiblePanelDon(boolean visiblePanelDon) {
        this.visiblePanelDon = visiblePanelDon;
    }

    public boolean isVisiblePanelDon() {
        return visiblePanelDon;
    }

    public void setDonacion(Donacion donacion) {
        this.donacion = donacion;
    }

    public Donacion getDonacion() {
        return donacion;
    }
}
