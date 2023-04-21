package br.ufmg.lcc.pcollecta.controller;

import java.util.ArrayList;
import java.util.List;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.controller.BasicButtonBarController;
import br.ufmg.lcc.arangi.controller.IApplicationContext;
import br.ufmg.lcc.arangi.controller.Message;
import br.ufmg.lcc.arangi.controller.SearchController;
import br.ufmg.lcc.arangi.controller.bean.ControllerBean;
import br.ufmg.lcc.arangi.model.IFacade;
import br.ufmg.lcc.arangi.model.ModelService;
import br.ufmg.lcc.pcollecta.dto.Consulta;

public class ConsultaController extends SearchController {

    @Override
    public ControllerBean search(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        List<Message> errorMessages = new ArrayList<Message>();
        Consulta consulta = (Consulta) controllerBean.getDto();
        if (consulta.getRepositorio().getId() == null) {
            Message message = new Message();
            message.setKey("validacaoCampoObrigatorioRepositorio");
            message.setType(Message.TYPE_ERROR);
            message.setArgs(new String[] { "" });
            controllerBean.getMessages().add(message);
            errorMessages.add(message);
        }
        if (consulta.getRegistroInicial() == null || consulta.getRegistroInicial() < 1) {
            Message message = new Message();
            message.setKey("validacaoCampoObrigatorioRegistroInicial");
            message.setType(Message.TYPE_ERROR);
            message.setArgs(new String[] { "" });
            controllerBean.getMessages().add(message);
            errorMessages.add(message);
        }
        if (consulta.getMaximoRegistros() == null || consulta.getMaximoRegistros() < 1) {
            Message message = new Message();
            message.setKey("validacaoCampoObrigatorioRegistroMaximo");
            message.setType(Message.TYPE_ERROR);
            message.setArgs(new String[] { "" });
            controllerBean.getMessages().add(message);
            errorMessages.add(message);
        }
        if (consulta.getSql().equals("")) {
            Message message = new Message();
            message.setKey("validacaoCampoObrigatorioSql");
            message.setType(Message.TYPE_ERROR);
            message.setArgs(new String[] { "" });
            controllerBean.getMessages().add(message);
            errorMessages.add(message);
        }
        if (errorMessages.size() != 0) {
            return controllerBean;
        }
        IFacade facade = ModelService.getFacade();
        List[] listas = (List[]) facade.executeGenericOperation("br.ufmg.lcc.pcollecta.model.RepositorioBO", "executaConsulta", new Object[] { (Consulta) controllerBean.getDto() });
        context.setRequestAttribute("listColunasConsulta", listas[0]);
        context.setRequestAttribute("listCampos", listas[1]);
        context.setRequestAttribute("sizeListCampos", listas[1].size());
        return controllerBean;
    }

    @Override
    protected ControllerBean afterNewObject(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        context.setSessionAttribute("sizeListCampos", 0);
        context.setRequestAttribute("listColunasConsulta", new ArrayList());
        context.setRequestAttribute("listCampos", new ArrayList());
        Consulta consulta = new Consulta();
        consulta.setRegistroInicial(new Long(1));
        consulta.setMaximoRegistros(new Long(100));
        controllerBean.setDto(consulta);
        return controllerBean;
    }

    @Override
    protected void afterRenderButtons(IApplicationContext context, ControllerBean controllerBean) {
        BasicButtonBarController buttonBarController = controllerBean.getButtonBarController();
        buttonBarController.setButtonNewVisible(false);
    }
}
