package br.ufmg.lcc.eid2ldap.controller;

import java.util.List;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.controller.BasicButtonBarController;
import br.ufmg.lcc.arangi.controller.IApplicationContext;
import br.ufmg.lcc.arangi.controller.SearchController;
import br.ufmg.lcc.arangi.controller.bean.ControllerBean;
import br.ufmg.lcc.eid2ldap.dto.Xslt;

public class XsltSelController extends SearchController {

    @Override
    protected ControllerBean beforeDelete(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        int j = 0;
        List listXslt = (List) controllerBean.getSearchList();
        for (int i = 0; i < listXslt.size(); i++) {
            Xslt xslt = (Xslt) listXslt.get(i);
            if (xslt.isCheckDelete()) {
                j++;
            }
        }
        if (j == 0) {
            throw BasicException.errorHandling("O bot�o Excluir foi acionado sem um ou mais Xslt estarem selecionados.", "msgXsltNaoSelecionado", new String[] {}, log);
        }
        return controllerBean;
    }

    @Override
    protected void afterRenderButtons(IApplicationContext context, ControllerBean controllerBean) {
        BasicButtonBarController buttonBarController = controllerBean.getButtonBarController();
        buttonBarController.setButtonSearchVisible(true);
        buttonBarController.setButtonPrintVisible(true);
    }
}
