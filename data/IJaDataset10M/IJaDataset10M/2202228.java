package br.com.wepa.webapps.orca.controle.actions.struts;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import br.com.wepa.webapps.logger.TraceLogger;
import br.com.wepa.webapps.orca.controle.ConstantesControle;
import br.com.wepa.webapps.orca.controle.ControleWeb;
import br.com.wepa.webapps.orca.controle.actions.login.UsuarioSessao;
import br.com.wepa.webapps.orca.controle.paging.PagingConfig;
import br.com.wepa.webapps.orca.logica.negocio.facade.basic.BasicTO;
import br.com.wepa.webapps.orca.util.bean.BeanHelperUtil;
import br.com.wepa.webapps.orca.visual.msg.MessagesConstants;
import br.com.wepa.webapps.search.PagingBean;

/**
 * 
 * @author Fabr�cio Silva Epaminondas
 * 
 */
public abstract class SuperAction extends DispatchAction implements ConstantesControle {

    /**
	 * Objeto de Log
	 */
    private static final TraceLogger logger = new TraceLogger(SuperAction.class);

    public static final String TRANSFER_OBJECT = "to";

    protected SuperAction() {
        super();
    }

    /**
	 * Recupera o usurio logado
	 * 
	 * @param request
	 * @return Usurio logado no sistema
	 * @throws Exception
	 */
    protected UsuarioSessao getUsuarioSessao() throws Exception {
        return ControleWeb.getUsuarioSessaoCorrente();
    }

    /**
	 * Dado nome do metodo que teve erro, recupera o nome do backForward que
	 * deve ser direcionado
	 * 
	 * @param nomeMetodo
	 *            Nome do metodo que provocou o erro
	 * @return Nome do backForward que deve se direcionado
	 */
    protected abstract String getForwardErroName(String nomeMetodo);

    /**
	 * 
	 */
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.log("unspecified Action Class -" + this.getClass());
        return principal(mapping, form, request, response);
    }

    /**
	 * 
	 */
    protected DynaActionForm getDynaForm(ActionForm form) throws Exception {
        return (DynaActionForm) form;
    }

    /**
	 * Seta mensagens globais no request
	 * 
	 * @param request
	 * @param mensagem
	 */
    protected void setMensagens(HttpServletRequest request, String mensagem) {
        addMensagens(request, mensagem);
    }

    /**
	 * Seta mensagens globais no request
	 * 
	 * @param request
	 * @param mensagem
	 */
    protected void setMensagens(HttpServletRequest request, String mensagem, String... parametro) {
        addMensagens(request, mensagem, parametro);
    }

    /**
	 * Seta mensagens globais no request
	 * 
	 * @param request
	 * @param mensagem
	 */
    private void addMensagens(HttpServletRequest request, String mensagem, String... parametro) {
        ActionMessages mens = this.getMessages(request);
        if (mens == null) {
            mens = new ActionMessages();
        }
        String tipo = ActionMessages.GLOBAL_MESSAGE;
        mens.add(tipo, createActionMessage(mensagem, parametro));
        saveMessages(request, mens);
        this.saveToken(request);
    }

    private ActionMessage createActionMessage(String mensagem, String... parametro) {
        if (parametro == null || parametro.length == 0) {
            return new ActionMessage(mensagem);
        }
        return new ActionMessage(mensagem, parametro);
    }

    /**
	 * Seta mensagens globais no request
	 * 
	 * @param request
	 * @param mensagem
	 */
    protected void addError(HttpServletRequest request, String mensagem) {
        addError(request, mensagem, null);
    }

    /**
	 * Seta mensagens globais no request
	 * 
	 * @param request
	 * @param mensagem
	 * @param param
	 */
    protected void addError(HttpServletRequest request, String mensagem, Object[] param) {
        ActionMessages mens = this.getErrors(request);
        if (mens == null) {
            mens = new ActionMessages();
        }
        String tipo = ActionMessages.GLOBAL_MESSAGE;
        if (param == null) {
            mens.add(tipo, new ActionMessage(mensagem));
        } else {
            mens.add(tipo, new ActionMessage(mensagem, param));
        }
        this.saveErrors(request, mens);
        this.saveToken(request);
    }

    /**
	 * Seta o TO no escopo definido
	 * 
	 * @param request
	 * @param to
	 * @param session,
	 *            se trua seta na sessao, caso contr�rio no request
	 */
    protected void clearAndSetTansferObject(HttpServletRequest request, BasicTO to, boolean session) {
        clearTansferObject(request, session);
        if (session) {
            request.getSession().setAttribute(TRANSFER_OBJECT, to);
        } else {
            request.setAttribute(TRANSFER_OBJECT, to);
        }
    }

    /**
	 * Seta o TO no escopo definido
	 * 
	 * @param request
	 * @param to
	 * @param session,
	 *            se trua seta na sessao, caso contr�rio no request
	 */
    protected void setTansferObject(HttpServletRequest request, BasicTO to, boolean session) {
        if (session) {
            request.getSession().setAttribute(TRANSFER_OBJECT, to);
        } else {
            request.setAttribute(TRANSFER_OBJECT, to);
        }
    }

    /**
	 * Recupera o TO da sessao
	 * 
	 * @param request
	 * @return
	 */
    protected BasicTO getTansferObjectInSession(HttpServletRequest request) {
        return (BasicTO) request.getSession().getAttribute(TRANSFER_OBJECT);
    }

    /**
	 * Recupera o TO
	 * 
	 * @param request
	 * @return
	 */
    protected BasicTO getTansferObjectInRequest(HttpServletRequest request) {
        return (BasicTO) request.getAttribute(TRANSFER_OBJECT);
    }

    protected void clearTansferObject(HttpServletRequest request) {
        request.removeAttribute(TRANSFER_OBJECT);
        request.getSession().removeAttribute(TRANSFER_OBJECT);
    }

    protected void clearTansferObject(HttpServletRequest request, boolean session) {
        if (session) {
            request.getSession().removeAttribute(TRANSFER_OBJECT);
        } else {
            request.removeAttribute(TRANSFER_OBJECT);
        }
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BeanHelperUtil.init(request.getLocale(), getResources(request));
        ActionForward forward = null;
        try {
            forward = super.execute(mapping, form, request, response);
        } catch (NoSuchMethodException ex) {
            forward = unspecified(mapping, form, request, response);
        }
        return forward;
    }

    /**
	 * M�todo abstrato default executado caso n�o seja passado um actionMethod
	 * no viewForm.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return Um ActionForward.
	 * @throws Exception
	 */
    protected abstract ActionForward principal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
	 * Pops the an action ActionEvent in Stack and returns
	 * 
	 * @param request
	 * @param forward
	 * @return forward, if not found previous returns the MAIN forward
	 * @throws Exception
	 */
    protected ActionForward back(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.entering();
        ActionTracer tracer = getActionTracer(request);
        ActionEvent event = tracer.pop();
        if (event == null) {
            ActionForward forward = mapping.findForward(GLOBAL_FWPAGE_PRINCIPAL);
            tracer.push(null, null, forward);
            logger.exiting();
            return forward;
        }
        if (event.hasForm()) {
            ControleWeb.setInScope(event.getName(), event.getForm(), request, event.getScope());
        }
        logger.exiting();
        return event.getForward();
    }

    /**
	 * Pops the an action ActionEvent in Stack and returns
	 * 
	 * @param request
	 * @param forward
	 * @return forward, if not found previous returns the MAIN forward
	 * @throws Exception
	 */
    protected ActionForward popBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.entering();
        ActionTracer tracer = getActionTracer(request);
        tracer.pop();
        ActionEvent event = tracer.pop();
        if (event == null) {
            ActionForward forward = mapping.findForward(GLOBAL_FWPAGE_PRINCIPAL);
            tracer.push(null, null, forward);
            logger.exiting();
            return forward;
        }
        if (event.hasForm()) {
            ControleWeb.setInScope(event.getName(), event.getForm(), request, event.getScope());
        }
        logger.entering();
        return event.getForward();
    }

    /**
	 * Pops the an action in Stack and returns
	 * 
	 * @param request
	 * @param forward
	 * @return forward, if not found previous returns the MAIN forward
	 * @throws Exception
	 */
    protected ActionForward recall(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.entering();
        ActionTracer tracer = getActionTracer(request);
        ActionEvent event = tracer.getLast();
        if (event == null) {
            ActionForward forward = mapping.findForward(GLOBAL_FWPAGE_PRINCIPAL);
            tracer.push(null, null, forward);
            logger.exiting();
            return forward;
        }
        if (event.hasForm()) {
            ControleWeb.setInScope(event.getName(), event.getForm(), request, event.getScope());
        }
        logger.entering();
        return event.getForward();
    }

    /**
	 * Recuperar um mensagem dado sua chave no arquivo de i18n
	 * 
	 * @param key
	 * @param request
	 * @return
	 */
    protected String getMessageResource(String key, HttpServletRequest request) {
        MessageResources messageResources = getResources(request);
        return messageResources.getMessage(key);
    }

    /**
	 * Indica que o response n�o aceita cache
	 * 
	 * @param response
	 */
    protected void noCache(HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-WebControlThreads", "no-cache");
    }

    /**
	 * Copia propriedades para o objeto
	 * 
	 * @param destino
	 * @param form
	 * @throws Exception
	 */
    protected void copyFormToObject(Object dest, ActionForm form) throws Exception {
        BeanHelperUtil.copyProperties(dest, form);
    }

    /**
	 * Copia propriedades para o objeto
	 * 
	 * @param destino
	 * @param form
	 * @throws Exception
	 */
    protected void setFormProperty(ActionForm form, String property, Object value) throws Exception {
        BeanHelperUtil.setProperty(form, property, value);
    }

    /**
	 * Get property do form
	 * 
	 * @param destino
	 * @param form
	 * @throws Exception
	 */
    protected Object getFormProperty(ActionForm form, String property) throws Exception {
        return BeanHelperUtil.getProperty(form, property);
    }

    /**
	 * Limpa todos campos de um form
	 * 
	 * @param form
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    protected void clearForm(ActionForm form) throws Exception {
        if (form instanceof DynaActionForm) {
            DynaActionForm dyna = (DynaActionForm) form;
            dyna.getMap().clear();
        } else {
            Map atts = BeanHelperUtil.describe(form);
            for (Object key : atts.keySet()) {
                atts.put(key, null);
            }
            populateForm(form, atts);
        }
    }

    /**
	 * Limpa todos campos de um form
	 * 
	 * @param form
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    protected Map getFormProperties(ActionForm form) throws Exception {
        if (form instanceof DynaActionForm) {
            DynaActionForm dyna = (DynaActionForm) form;
            return dyna.getMap();
        } else {
            return BeanHelperUtil.describe(form);
        }
    }

    /**
	 * Get property from dyna form
	 * 
	 * @param destino
	 * @param form
	 * @throws Exception
	 */
    protected String getStringFormProperty(ActionForm form, String property) throws Exception {
        if (form instanceof DynaActionForm) {
            DynaActionForm dyna = (DynaActionForm) form;
            return dyna.getString(property);
        } else {
            return (String) getFormProperty(form, property);
        }
    }

    /**
	 * Get property from dyna form
	 * 
	 * @param destino
	 * @param form
	 * @throws Exception
	 */
    protected Integer getIntegerFormPropertiy(ActionForm form, String property) throws Exception {
        return (Integer) BeanHelperUtil.convert(getStringFormProperty(form, property), Integer.class);
    }

    /**
	 * Copia propriedades para o form
	 * 
	 * @param form
	 * @param source
	 * @throws Exception
	 */
    protected void copyObjectToForm(ActionForm form, Object source) throws Exception {
        BeanHelperUtil.copyProperties(form, source);
    }

    /**
	 * Copia propriedades para o form
	 * 
	 * @param form
	 * @param source
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    protected void populateForm(ActionForm form, Map map) throws Exception {
        BeanHelperUtil.populate(form, map);
    }

    /**
	 * 
	 * Copia propriedades para da fonte para destino
	 * 
	 * @param form
	 * @param source
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    protected void copyProperties(Object dest, Object source) throws IllegalAccessException, InvocationTargetException {
        BeanHelperUtil.copyProperties(dest, source);
    }

    /**
	 * Empilha esta acao chamada pelo request
	 * 
	 * @param request
	 * @param form
	 * @param mapping
	 * @throws Exception
	 */
    protected void pushActionEvent(HttpServletRequest request, ActionForm form, ActionMapping mapping) throws Exception {
        getActionTracer(request).push(request, form, mapping);
    }

    /**
	 * Empilha um action ActionEvent com este path
	 * 
	 * @param request
	 * @param form
	 * @param mapping
	 * @param path
	 * @throws Exception
	 */
    protected void pushActionEvent(HttpServletRequest request, ActionForm form, ActionMapping mapping, String path) throws Exception {
        getActionTracer(request).push(form, mapping, path);
    }

    /**
	 * Empilha o evento com este forward
	 * 
	 * @param request
	 * @param form
	 * @param mapping
	 * @param forward
	 * @throws Exception
	 */
    protected void pushActionEvent(HttpServletRequest request, ActionForm form, ActionMapping mapping, ActionForward forward) throws Exception {
        getActionTracer(request).push(form, mapping, forward);
    }

    /**
	 * gets the action forward
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    protected ActionTracer getActionTracer(HttpServletRequest request) throws Exception {
        ActionTracer tracer = (ActionTracer) request.getSession().getAttribute(ACTIONTRACER);
        if (tracer == null) {
            tracer = new ActionTracer(ACTIONTRACER_DEFAULT_SIZE);
            request.getSession().setAttribute(ACTIONTRACER, tracer);
        }
        return tracer;
    }

    protected void checkToken(HttpServletRequest request) throws Exception {
        if (!super.isTokenValid(request, true)) {
            throw new Exception(MessagesConstants.ERROR_TOKEN_INVALIDO);
        }
    }

    /**
	 * Configura pagina��o 
	 * @param request
	 * @param form
	 * @param mapping
	 * @return
	 * @throws Exception
	 */
    protected PagingBean configPaging(HttpServletRequest request, ActionForm form, ActionMapping mapping) throws Exception {
        return PagingConfig.configPaging(request, form, mapping);
    }
}
