package br.com.petrobras.control;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import br.com.petrobras.facade.BDFacade;
import br.com.petrobras.model.TipoItemForm;
import br.com.petrobras.model.TipoItemVO;
import br.com.petrobras.util.Diretorio;
import br.com.petrobras.util.Manutencao;

public class AlterarTipoItemAction extends DispatchAction {

    private static Log logger;

    static {
        logger = LogFactory.getLog(AlterarTipoItemAction.class.getName());
    }

    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        return null;
    }

    @SuppressWarnings("unchecked")
    public ActionForward prepara(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TipoItemForm tipoItemForm = (TipoItemForm) form;
        List lista = (List) request.getSession().getAttribute("tiposItem");
        int pos = -1;
        try {
            pos = Integer.valueOf(request.getParameter("posTipoItem")).intValue();
            if (pos < 0) throw new Exception();
        } catch (Exception e) {
            return mapping.findForward("failure");
        }
        TipoItemVO tipoItem = (TipoItemVO) lista.get(pos);
        request.getSession().setAttribute("tipoItem", tipoItem);
        BeanUtils.copyProperties(tipoItemForm, tipoItem);
        tipoItemForm.setAlterar(true);
        return mapping.findForward("success");
    }

    public ActionForward altera(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            TipoItemForm tipoItemForm = (TipoItemForm) form;
            TipoItemVO tipoItem = new TipoItemVO();
            if (tipoItemForm != null) {
                BeanUtils.copyProperties(tipoItem, tipoItemForm);
                logger.info("Alterando Tipo Item");
                BDFacade facade = BDFacade.getInstance();
                Integer quant = facade.alterarTipoItem(tipoItem);
                if (quant.equals(new Integer(1))) {
                    logger.info("Sucesso!");
                    Manutencao.limpaDiretorio(Diretorio.RELATORIO, null);
                    tipoItemForm.reset();
                    ActionMessages messages = new ActionErrors();
                    ActionMessage msg = new ActionMessage("tipoItem.update.sucess");
                    messages.add(Globals.MESSAGE_KEY, msg);
                    saveMessages(request, messages);
                    return mapping.findForward("success");
                }
            }
            logger.info("O Tipo de Item n�o foi alterado!");
            ActionMessages errors = new ActionErrors();
            ActionMessage nError = new ActionMessage("tipoItem.update.nothing");
            errors.add("PessoaNotFound", nError);
            saveErrors(request, errors);
            return mapping.findForward("failure");
        } catch (Exception e) {
            logger.info("Excecao na alteracao do Tipo de Item");
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("excecao.literal", e.getMessage()));
            saveErrors(request, errors);
            return mapping.findForward("failure");
        }
    }
}
