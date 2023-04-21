package br.ufmg.lcc.pcollecta.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.commons.MessageFactory;
import br.ufmg.lcc.arangi.commons.StringHelper;
import br.ufmg.lcc.arangi.controller.IApplicationContext;
import br.ufmg.lcc.arangi.controller.Message;
import br.ufmg.lcc.arangi.controller.StandardApplicationController;
import br.ufmg.lcc.arangi.controller.StandardControllerHelper;
import br.ufmg.lcc.arangi.controller.bean.ControllerBean;
import br.ufmg.lcc.arangi.controller.bean.SelectItem;
import br.ufmg.lcc.arangi.controller.bean.SelectList;
import br.ufmg.lcc.pcollecta.dto.PCollectaWS;
import br.ufmg.lcc.pcollecta.dto.PCollectaWSArg;
import br.ufmg.lcc.pcollecta.ws.PCollectaServiceException;
import br.ufmg.lcc.pcollecta.ws.beans.DatabaseInfo;
import br.ufmg.lcc.pcollecta.ws.client.PCollectaWebServiceClient;

/**
 * Controller class for WS Databases Search page
 * 
 * @author lukasmeirelles
 *
 */
public class WSDatabasesQueryController extends StandardApplicationController {

    @Override
    protected void afterRenderButtons(IApplicationContext context, ControllerBean controllerBean) {
        PCollectaButtonBarController barController = (PCollectaButtonBarController) controllerBean.getButtonBarController();
        barController.setButtonNewVisible(false);
        barController.setButtonDeleteVisible(false);
    }

    @Override
    public ControllerBean search(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        StandardControllerHelper.populateAudit(controllerBean.getDto(), context);
        this.beforeProcess(context, controllerBean, SEARCH_EVENT);
        controllerBean.setForward("search");
        this.buildSelectLists(context, controllerBean);
        try {
            controllerBean.setHasValidationErros(false);
            List messages = this.basicValidateDTO(context, controllerBean, controllerBean.getDto());
            if (messages != null && messages.size() > 0) {
                controllerBean.getMessages().addAll(messages);
                controllerBean.setHasValidationErros(true);
            }
        } catch (Exception e) {
            throw BasicException.basicErrorHandling("Error validating data", "msgErrorValidatingData", e, log);
        }
        if (controllerBean.isHasValidationErros()) {
            throw BasicException.basicErrorHandling("Error validating data", "msgValidatingErrorsFound", StringHelper.EMPTY_STRING_VECTOR, log);
        }
        controllerBean.setForward("search");
        PCollectaWSArg pcollectaWSArg = (PCollectaWSArg) controllerBean.getDto();
        SelectList selectList = (SelectList) controllerBean.getSelectListsMap().get("pcollectaWSList");
        List<PCollectaWS> pcollectaWSSelected = recoverWSSelected(selectList.getListData(), pcollectaWSArg.getPcollectaWSIds());
        for (PCollectaWS ws : pcollectaWSSelected) {
            searchWSDatabases(controllerBean, ws);
        }
        controllerBean.getCurrentLogicBean().setSearchList(pcollectaWSSelected);
        return controllerBean;
    }

    /**
	 * This method recovers all databases available from a PcollectaWS
	 * @param ws PcollectaWS 
	 * @throws BasicException
	 */
    private void searchWSDatabases(ControllerBean controllerBean, PCollectaWS ws) throws BasicException {
        ResourceBundle resource = ResourceBundle.getBundle("ApplicationResources");
        SelectList visibilityList = (SelectList) controllerBean.getSelectListsMap().get("visibilityList");
        try {
            PCollectaWebServiceClient webServiceClient = new PCollectaWebServiceClient(ws.getWebServiceEndPoint());
            DatabaseInfo[] dbs = webServiceClient.getAvaillableDatabases();
            for (DatabaseInfo database : dbs) {
                for (Object item : visibilityList.getSelectItems()) {
                    SelectItem visibilityItem = (SelectItem) item;
                    if (visibilityItem.getCode().equals(database.getDatabaseType())) {
                        database.setDatabaseType(resource.getString(visibilityItem.getKey()));
                    }
                }
            }
            ws.setDatabases(dbs);
        } catch (PCollectaServiceException e) {
            String msg = MessageFactory.composeMessageFromException(e);
            controllerBean.addMessage("errorWSDatabasesQuery", new String[] { ws.getName(), msg }, Message.TYPE_ERROR);
        }
    }

    /**
	 * This method recovers all selected Pcollecta WS by id 
	 * @param items List of PcollectaWS
	 * @param ids List of selected ids
	 * @return List of PcollectaWS objects selected
	 */
    private List<PCollectaWS> recoverWSSelected(List items, List<Long> ids) {
        List<PCollectaWS> wsSelected = new ArrayList<PCollectaWS>();
        List<PCollectaWS> wsItems = items;
        for (PCollectaWS ws : wsItems) {
            for (Long id : ids) {
                if (ws.getId().equals(id)) {
                    wsSelected.add(ws);
                    break;
                }
            }
        }
        return wsSelected;
    }

    @Override
    public ControllerBean open(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        return super.open(context, controllerBean);
    }
}
