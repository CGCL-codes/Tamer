package br.gov.mec.pingifesManager.controle;

import java.util.List;
import br.gov.mec.pingifesManager.dto.PingifesControle;
import br.gov.mec.pingifesManager.modelo.PingIfesManagerDAO;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.controller.BasicButtonBarController;
import br.ufmg.lcc.arangi.controller.IApplicationContext;
import br.ufmg.lcc.arangi.controller.StandardApplicationController;
import br.ufmg.lcc.arangi.controller.bean.ControllerBean;
import br.ufmg.lcc.arangi.model.IFacade;
import br.ufmg.lcc.arangi.model.ModelService;

public class TabelaSelController extends StandardApplicationController {

    @Override
    public ControllerBean search(IApplicationContext context, ControllerBean appDto) throws BasicException {
        IFacade facade = ModelService.getFacade();
        PingifesControle pingifesControle = (PingifesControle) facade.executeGenericOperation("br.gov.mec.pingifesManager.modelo.PingifesControleBO", "recuperaPingifesControle", new Object[] {});
        PingIfesManagerDAO pingIfesManagerDAO = null;
        if (pingifesControle != null && pingifesControle.getId() != null) {
            pingIfesManagerDAO = new PingIfesManagerDAO(pingifesControle.getUrl(), pingifesControle.getDriver(), pingifesControle.getUsuario(), pingifesControle.getSenha());
            pingIfesManagerDAO.openConnection();
        } else {
            throw BasicException.errorHandling("Erro de conex�o: N�o foi poss�vel criar conex�o ao banco de dados", "erroCriarConexaoParametro", new String[] {}, log);
        }
        if (pingIfesManagerDAO != null && pingIfesManagerDAO.getCon() != null) {
            try {
                pingIfesManagerDAO.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
            List lista = (List) facade.executeGenericOperation("br.gov.mec.pingifesManager.modelo.TabelaBO", "recuperaListaTabelas", new Object[] { pingifesControle });
            appDto.setSearchList(lista);
        } else {
            throw BasicException.errorHandling("Erro de conex�o: N�o foi poss�vel criar conex�o ao banco de dados", "erroCriarConexaoParametro", new String[] {}, log);
        }
        return appDto;
    }

    @Override
    protected void afterRenderButtons(IApplicationContext context, ControllerBean appDto) {
        BasicButtonBarController btc = appDto.getButtonBarController();
        btc.setButtonNewVisible(false);
        btc.setButtonSearchVisible(false);
        btc.setButtonDeleteVisible(false);
        PingifesButtonBarController pbc = (PingifesButtonBarController) appDto.getButtonBarController();
        pbc.reset();
    }
}
