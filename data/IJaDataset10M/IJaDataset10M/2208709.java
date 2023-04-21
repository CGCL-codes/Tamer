package br.ufmg.lcc.pcollecta.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.controller.BasicButtonBarController;
import br.ufmg.lcc.arangi.controller.IApplicationContext;
import br.ufmg.lcc.arangi.controller.MasterDetailController;
import br.ufmg.lcc.arangi.controller.Message;
import br.ufmg.lcc.arangi.controller.bean.ControllerBean;
import br.ufmg.lcc.arangi.controller.bean.SavingLogicBean;
import br.ufmg.lcc.arangi.controller.bean.SelectList;
import br.ufmg.lcc.arangi.dto.BasicDTO;
import br.ufmg.lcc.arangi.model.IFacade;
import br.ufmg.lcc.arangi.model.ModelService;
import br.ufmg.lcc.pcollecta.dto.Agendamento;
import br.ufmg.lcc.pcollecta.dto.ItemProcesso;
import br.ufmg.lcc.pcollecta.dto.Processo;

public class AgendamentoController extends MasterDetailController {

    public ControllerBean cancelarAgendamento(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        Agendamento agendamento = (Agendamento) controllerBean.getDto();
        AgenteAgendamentoController agente = AgenteAgendamentoController.getInstancia();
        if (agente.cancelaProcesso(agendamento, true)) {
            agendamento.setSituacao(Agendamento.CANCELADO);
            agendamento.setExibeMensagemCancelamento(Boolean.TRUE);
        } else {
            agendamento.setSituacao(Agendamento.CANCELADO);
            IFacade facade = ModelService.getFacade();
            this.basicValidateDTO(context, controllerBean, agendamento);
            facade.update(agendamento, null);
            agendamento.setExibeMensagemCancelamento(Boolean.FALSE);
            controllerBean.addMessage("msgNotificacaoProcessamentoCancelado", new String[] {}, Message.TYPE_INFO);
        }
        SavingLogicBean savingLogic = (SavingLogicBean) controllerBean.getCurrentLogicBean();
        savingLogic.setDataReadOnly(true);
        return controllerBean;
    }

    public ControllerBean finalizarAgendamento(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        Agendamento agendamento = (Agendamento) controllerBean.getDto();
        agendamento.setSituacao(Agendamento.FINALIZADO);
        agendamento.setDataAnteriorProximaExecucao(null);
        SavingLogicBean savingLogic = (SavingLogicBean) controllerBean.getCurrentLogicBean();
        if (savingLogic.isDataReadOnly()) savingLogic.setDataReadOnly(false);
        controllerBean = super.save(context, controllerBean);
        return controllerBean;
    }

    @Override
    protected void afterRenderButtons(IApplicationContext context, ControllerBean controllerBean) {
        BasicButtonBarController btc = controllerBean.getButtonBarController();
        SavingLogicBean savingLogic = (SavingLogicBean) controllerBean.getCurrentLogicBean();
        Agendamento agendamento = (Agendamento) controllerBean.getDto();
        if (agendamento.getSituacao().equals(Agendamento.EM_EXECUCAO) && !savingLogic.isDataReadOnly()) {
            btc.setButtonCancelVisible(false);
            btc.setButtonNewVisible(true);
            btc.setButtonPrintVisible(true);
            btc.setButtonSaveVisible(false);
            btc.setButtonOpenVisible(true);
        }
    }

    @Override
    protected ControllerBean beforeSave(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        Agendamento agendamento = (Agendamento) controllerBean.getDto();
        if (agendamento.getDataAnteriorProximaExecucao() != null) {
            if (agendamento.getDataAnteriorProximaExecucao().getTime() != agendamento.getDataProximaExecucao().getTime() && !agendamento.getSituacao().equals(Agendamento.AGUARDANDO)) {
                agendamento.setSituacao(Agendamento.AGUARDANDO);
                agendamento.setProcessamento(agendamento.getProcessamento() + 1);
            }
            if (agendamento.getSituacao().equals(agendamento.FINALIZADO) && agendamento.getDataAnteriorProximaExecucao().getTime() == agendamento.getDataProximaExecucao().getTime()) {
                agendamento.setSituacao(Agendamento.AGUARDANDO);
                agendamento.setProcessamento(agendamento.getProcessamento() + 1);
            }
        }
        if (agendamento.getId() == null) {
            agendamento.setSituacao(Agendamento.AGUARDANDO);
            agendamento.setProcessamento(1L);
        }
        if (agendamento.getProcessamento() == null) {
            agendamento.setProcessamento(new Long(1));
        }
        return controllerBean;
    }

    @Override
    protected ControllerBean afterSave(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        Agendamento agendamento = (Agendamento) controllerBean.getDto();
        agendamento.setDataAnteriorProximaExecucao(agendamento.getDataProximaExecucao());
        populaComboItensProcesso(controllerBean, agendamento);
        return controllerBean;
    }

    @Override
    protected ControllerBean afterEdit(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        Agendamento agendamento = (Agendamento) controllerBean.getDto();
        agendamento.setDataAnteriorProximaExecucao(agendamento.getDataProximaExecucao());
        populaComboItensProcesso(controllerBean, agendamento);
        return controllerBean;
    }

    @Override
    protected ControllerBean beforeModify(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        Agendamento agendamento = (Agendamento) controllerBean.getDto();
        IFacade facade = ModelService.getFacade();
        agendamento = (Agendamento) facade.executeGenericOperation("br.ufmg.lcc.pcollecta.model.AgendamentoBO", "recuperaAgendamento", new Object[] { agendamento });
        if (agendamento.getSituacao().equals(Agendamento.EM_EXECUCAO)) {
            controllerBean.setDto(agendamento);
            Message message = new Message();
            message.setType(Message.TYPE_ERROR);
            message.setKey("msgAgendamentoEmExecucao");
            controllerBean.getMessages().add(message);
        }
        return super.beforeModify(context, controllerBean);
    }

    @Override
    protected ControllerBean afterModify(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        Agendamento agendamento = (Agendamento) controllerBean.getDto();
        agendamento.setDataAnteriorProximaExecucao(agendamento.getDataProximaExecucao());
        populaComboItensProcesso(controllerBean, agendamento);
        return controllerBean;
    }

    private void populaComboItensProcesso(ControllerBean controllerBean, Agendamento agendamento) throws BasicException {
        SelectList itensProcessoSelList = (SelectList) controllerBean.getSelectListsMap().get("itensProcessoSelList");
        Processo proc = agendamento.getProcesso();
        SelectList processosSelList = (SelectList) controllerBean.getSelectListsMap().get("processoSelList");
        List selItems = processosSelList.getListData();
        for (int i = 0; i < selItems.size(); i++) {
            Processo p = (Processo) selItems.get(i);
            if (p.getId().equals(proc.getId())) {
                proc = p;
                break;
            }
        }
        criaNomesItens(controllerBean, itensProcessoSelList, proc);
    }

    private void criaNomesItens(ControllerBean controllerBean, SelectList itensProcessoSelList, Processo proc) throws BasicException {
        List<ItemProcesso> itensProc = proc.getDetalheItemProcesso();
        List<ItemProcesso> itensNova = new ArrayList<ItemProcesso>();
        for (ItemProcesso item : itensProc) {
            ItemProcesso novoItem = new ItemProcesso();
            novoItem.setId(item.getOrdem());
            novoItem.setName(item.getOrdem() + " - " + item.getEtc().getName());
            itensNova.add(novoItem);
        }
        itensProcessoSelList.buildDynamicByLocale(itensNova, controllerBean.getLocale());
    }

    @Override
    protected List<Message> validateDTO(IApplicationContext context, ControllerBean controllerBean, BasicDTO dto) throws BasicException {
        List<Message> lista = new ArrayList();
        Agendamento agendamento = (Agendamento) dto;
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - 5);
        Date dataAtual = cal.getTime();
        if (!agendamento.getAgendarAgora() && agendamento.getDataProximaExecucao() == null) {
            Message message = new Message();
            message.setType(Message.TYPE_ERROR);
            message.setKey("msgValidacaoObrigatoriosAgendamento");
            lista.add(message);
        }
        if (agendamento.getAgendarAgora()) {
            agendamento.setDataProximaExecucao(new Date());
            agendamento.setAgendarAgora(Boolean.FALSE);
            agendamento.setProcessarAgoraAtivado(Boolean.TRUE);
        }
        populaComboItensProcesso(controllerBean, agendamento);
        return lista;
    }

    public ControllerBean alteraItensProcesso(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        this.buildSelectLists(controllerBean);
        Agendamento agendamento = (Agendamento) controllerBean.getDto();
        populaComboItensProcesso(controllerBean, agendamento);
        return controllerBean;
    }

    /**
     * @see br.ufmg.lcc.arangi.controller.StandardApplicationController#afterNewObject(br.ufmg.lcc.arangi.controller.bean.LogicBean, br.ufmg.lcc.arangi.controller.IApplicationContext, br.ufmg.lcc.arangi.controller.bean.ControllerBean)
     */
    @Override
    protected ControllerBean afterNewObject(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        SelectList processosSelList = (SelectList) controllerBean.getSelectListsMap().get("processoSelList");
        List selItems = processosSelList.getListData();
        if (selItems != null && selItems.size() > 0) {
            Processo proc = (Processo) selItems.get(0);
            SelectList itensProcessoSelList = (SelectList) controllerBean.getSelectListsMap().get("itensProcessoSelList");
            criaNomesItens(controllerBean, itensProcessoSelList, proc);
        }
        return controllerBean;
    }

    @Override
    public ControllerBean save(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        Agendamento agendamento = (Agendamento) controllerBean.getDto();
        try {
            return super.save(context, controllerBean);
        } catch (BasicException e) {
            if (agendamento.getId() == null) {
                agendamento.setSituacao("");
            }
            String conteudoMsg = "org.hibernate.StaleObjectStateException";
            if (e.getMessage().indexOf(conteudoMsg) > 0) {
                throw BasicException.errorHandling("Agendamento em execu��o.", "msgAgendamentoEmExecucao", new String[] {}, log);
            } else {
                throw e;
            }
        }
    }
}
