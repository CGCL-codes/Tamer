package br.ufrj.cad.view;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import br.ufrj.cad.fwk.model.ObjetoPersistente;
import br.ufrj.cad.fwk.util.ResourceUtil;
import br.ufrj.cad.fwk.view.action.BaseAction;
import br.ufrj.cad.model.bo.Usuario;
import br.ufrj.cad.view.util.SessionUtil;

public abstract class CADBaseAction extends BaseAction {

    public static final String ATRIBUTO_SESSAO_USUARIO = "usuario";

    private static final String REQUEST_ATTRIBUTE_SUCESS_MESSAGE = "sucessMessage";

    public static final String RESULTADO_BUSCA = "resultadoBusca";

    private static final Logger logger = Logger.getLogger(CADBaseAction.class);

    protected void executeLogic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("form", form);
        Usuario usuarioLogado = SessionUtil.obtemUsuarioLogado(request.getSession());
        try {
            executeLogic(mapping, form.getClass().cast(form), request, response, usuarioLogado);
        } catch (Exception e) {
            logger.error("Erro de execução: [" + usuarioLogado + "] " + e.getMessage(), e);
            throw e;
        }
    }

    protected abstract void executeLogic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws Exception;

    protected void addSucessMessage(HttpServletRequest request, String key) {
        String mensagem = ResourceUtil.getResourceMessage(ResourceUtil.NOTIFICATION, key);
        if (ObjectUtils.equals(mensagem, null)) {
            mensagem = key;
        }
        request.setAttribute(REQUEST_ATTRIBUTE_SUCESS_MESSAGE, mensagem);
    }

    protected void salvaResultado(HttpServletRequest request, List<? extends ObjetoPersistente> resultadoBusca) {
        request.setAttribute(CADBaseAction.RESULTADO_BUSCA, resultadoBusca);
    }
}
