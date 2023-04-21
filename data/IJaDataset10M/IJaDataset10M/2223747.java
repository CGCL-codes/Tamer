package br.gov.mec.pingifesManager.controle;

import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.controller.BasicButtonBarController;
import br.ufmg.lcc.arangi.controller.IApplicationContext;
import br.ufmg.lcc.arangi.controller.bean.ControllerBean;
import br.ufmg.lcc.arangi.controller.bean.LogicBean;

public class DisciplinaSelController extends BasicSelController {

    @Override
    protected ControllerBean beforeDelete(IApplicationContext context, ControllerBean appDto) throws BasicException {
        if (!validaNenhumAExcluir(appDto.getSearchList())) {
            throw BasicException.errorHandling("O bot�o Excluir foi acionado sem um ou mais Disicplina estarem selecionados.", "msgDisciplinaNaoSelecionado", new String[] {}, log);
        }
        return appDto;
    }

    @Override
    protected void afterRenderButtons(IApplicationContext context, ControllerBean appDto) {
        BasicButtonBarController btc = appDto.getButtonBarController();
        LogicBean logic = appDto.getCurrentLogicBean();
        if (logic.getViewID().equals("TURMA")) {
            btc.setButtonCloseVisible(true);
            btc.setButtonNewVisible(false);
            btc.setButtonPrintVisible(false);
            btc.setButtonDeleteVisible(false);
        }
    }
}
