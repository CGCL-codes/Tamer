package com.miranteinfo.seam.framework.action;

import java.io.Serializable;
import java.text.MessageFormat;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ajax4jsf.component.html.AjaxForm;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.ResourceBundle;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import com.miranteinfo.seam.framework.action.interceptor.annotation.BusinessMessageInterceptor;
import com.miranteinfo.seam.framework.action.interceptor.annotation.ConversationInterceptor;
import com.miranteinfo.seam.framework.action.interceptor.annotation.ExceptionsLogInterceptor;

/**
 * Classe suporte a todas as actions do projeto. Possui uma s�rie de m�todos que facilitam
 * o desenvolvimento como adic��o de mensagens no faces message e etc.
 * 
 * @author lucas lins
 *
 */
@BusinessMessageInterceptor
@ConversationInterceptor
@ExceptionsLogInterceptor
public abstract class BaseAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Logger
    public Log log;

    @In
    public FacesMessages facesMessages;

    @Create
    public void construir() {
        log.debug("Construindo componente.");
    }

    @Destroy
    public void destruir() {
        log.debug("Destruindo componente.");
    }

    /**
     * Retorna a referencia do FacesContext.
     * @return HttpServletResponse
     */
    protected FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * Retorna a referencia para o HttpServletResponse atual.
     * @return HttpServletResponse
     */
    protected HttpServletResponse getResponse() {
        return (HttpServletResponse) getContext().getExternalContext().getResponse();
    }

    /**
     * Retorna a referencia para o HttpServletRequest atual.
     * @return HttpServletRequest
     */
    protected HttpServletRequest getRequest() {
        return (HttpServletRequest) getContext().getExternalContext().getRequest();
    }

    /**
     * Retorna a instancia atual do contexto ServletContext.
     * @return ServletContext
     */
    protected ServletContext getServletContext() {
        return (ServletContext) getContext().getExternalContext().getContext();
    }

    /**
     * Retorna um parametro do Request HTTP (HttpServletRequest) sob a chave 'name'.
     * @param name - chave do parametro no Request HTTP
     * @return String - parametro do Request HTTP sob a chave 'name'
     */
    public String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * Retorna a referencia para o HttpSession atual.
     * @return HttpSession
     */
    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * Recupera um componente do contexto como nome passado.
     * Caso nao exista retorna null.
     * 
     * @param componentName
     * @return
     */
    protected Object retrieveComponent(String componentName) {
        return Component.getInstanceFromFactory(componentName);
    }

    /**
     * Remove de todos os contextos o componente cujo o nome e igual ao nome passado.
     * Se nao houver o compoenente, apenas nao faz nada.
     * 
     * @param compoenentName
     */
    protected void removeComponent(String componentName) {
        Contexts.removeFromAllContexts(componentName);
    }

    /**
     * Adiciona uma mensagem no facesMessages com severidade info.
     * @param msg
     * @param params
     */
    public void addMsg(String msg, Object... params) {
        facesMessages.add(FacesMessage.SEVERITY_INFO, msg, params);
    }

    /**
     * Adiciona uma mensagem no facesMessages com severidade INFO, para o componente 
     * especificado
     * @param compoenentId
     * @param msg
     * @param params
     */
    public void addMsgToComponent(String componenteId, String msg, Object... params) {
        facesMessages.addToControl(componenteId, FacesMessage.SEVERITY_INFO, msg, params);
    }

    /**
     * Adiciona uma mensagem do bundle no facesMessages com severidade info.
     * @param msg
     * @param params
     */
    public void addMsgBundle(String key, Object... params) {
        facesMessages.addFromResourceBundle(FacesMessage.SEVERITY_INFO, key, params);
    }

    /**
     * Adiciona uma mensagem do bundle no facesMessages com severidade INFO, para o componente 
     * especificado
     * @param compoenentId
     * @param key
     * @param params
     */
    public void addMsgToComponentFromBundle(String componenteId, String key, Object... params) {
        facesMessages.addToControlFromResourceBundle(componenteId, FacesMessage.SEVERITY_INFO, key, params);
    }

    /**
     * Adiciona uma mensagem no facesMessages com severidade warn.
     * @param msg
     * @param params
     */
    public void addMsgWarn(String msg, Object... params) {
        facesMessages.add(FacesMessage.SEVERITY_WARN, msg, params);
    }

    /**
     * Adiciona uma mensagem no facesMessages com severidade WARN, para o componente 
     * especificado.
     * @param compoenentId
     * @param msg
     * @param params
     */
    public void addMsgWarnToComponent(String componenteId, String msg, Object... params) {
        facesMessages.addToControl(componenteId, FacesMessage.SEVERITY_WARN, msg, params);
    }

    /**
     * Adiciona uma mensagem do bundle no facesMessages com severidade warn.
     * @param msg
     * @param params
     */
    public void addMsgWarnBundle(String key, Object... params) {
        facesMessages.addFromResourceBundle(FacesMessage.SEVERITY_WARN, key, params);
    }

    /**
     * Adiciona uma mensagem do bundle no facesMessages com severidade WARN, para o componente 
     * especificado
     * @param compoenentId
     * @param key
     * @param params
     */
    public void addMsgWarnToComponentFromBundle(String componenteId, String key, Object... params) {
        facesMessages.addToControlFromResourceBundle(componenteId, FacesMessage.SEVERITY_WARN, key, params);
    }

    /**
     * Adiciona uma mensagem no facesMessages com severidade error.
     * @param msg
     * @param params
     */
    public void addMsgErro(String msg, Object... params) {
        facesMessages.add(FacesMessage.SEVERITY_ERROR, msg, params);
    }

    /**
     * Adiciona uma mensagem do bundle no facesMessages com severidade error, para o componente 
     * especificado.
     * @param compoenentId
     * @param msg
     * @param params
     */
    public void addMsgErroToComponent(String componenteId, String msg, Object... params) {
        facesMessages.addToControl(componenteId, FacesMessage.SEVERITY_ERROR, msg, params);
    }

    /**
     * Adiciona uma mensagem do bundle no facesMessages com severidade error.
     * @param msg
     * @param params
     */
    public void addMsgErroBundle(String key, Object... params) {
        facesMessages.addFromResourceBundle(FacesMessage.SEVERITY_ERROR, key, params);
    }

    /**
     * Adiciona uma mensagem do bundle no facesMessages com severidade ERROR, para o componente 
     * especificado
     * @param compoenentId
     * @param key
     * @param params
     */
    public void addMsgErroToComponentFromBundle(String componenteId, String key, Object... params) {
        facesMessages.addToControlFromResourceBundle(componenteId, FacesMessage.SEVERITY_ERROR, key, params);
    }

    /**
	 * Lanca erro de validacao com a mensagem do bundle. 
	 * @param chave
	 * @throws ValidatorException
	 */
    public void lancarErroValidacao(String chave) throws ValidatorException {
        FacesMessage facesMessage = new FacesMessage(getMsg(chave));
        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        throw new ValidatorException(facesMessage);
    }

    /**
	 * Retorna a mensagem do arquivo properties default ("messages") formatada com os parametros passados.
	 * 
	 * @param key
	 * @param params
	 * @return mensagem formatada
	 */
    public String getMessageFromDefaultBundle(String key, String... params) {
        if (params == null || params.length == 0) {
            return ResourceBundle.instance().getString(key);
        }
        MessageFormat form = new MessageFormat(ResourceBundle.instance().getString(key));
        return form.format(params);
    }

    /**
	 * Retorna a mensagem do arquivo properties, que possui o nome passado como argumento, formatada com os parametros passados.
	 * 
	 * @param bundleName
	 * @param key
	 * @param params
	 * @return mensagem formatada
	 */
    public String getMessageFromBundle(String bundleName, String key, String... params) {
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle(bundleName);
        if (params == null || params.length == 0) {
            return bundle.getString(key);
        }
        MessageFormat form = new MessageFormat(bundle.getString(key));
        return form.format(params);
    }

    private String getMsg(String chave) {
        return ResourceBundle.instance().getString(chave);
    }

    /**
	 * Metodo para ser utilizado em acoes de cancelamento que exigem a limpeza de um form.
	 * Realiza, assim, a limpeza completa na arvore de componentes do JSF, do form que o botao se encontra.
	 * @param event
	 */
    public void resetForm(ActionEvent event) {
        UIComponent component = event.getComponent();
        while (!(component instanceof HtmlForm) && !(component instanceof AjaxForm) && component.getParent() != null) component = component.getParent();
        if (component instanceof HtmlForm || component instanceof AjaxForm) cleanComponent(component);
    }

    /**
	 * Metodo para limpar o componente, na arvore jsf, cujo id informado. 
	 * @param componentClientId
	 */
    public void resetComponent(String componentClientId) {
        cleanComponent(getContext().getViewRoot().findComponent(componentClientId));
    }

    private void cleanComponent(UIComponent component) {
        if (component == null) return;
        if (component instanceof UIInput) ((UIInput) component).resetValue();
        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                cleanComponent(child);
            }
        }
    }
}
