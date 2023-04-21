package br.ufmg.lcc.arangi.controller;

import java.util.Iterator;
import java.util.List;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.commons.NVLHelper;
import br.ufmg.lcc.arangi.controller.bean.ControllerBean;
import br.ufmg.lcc.arangi.controller.bean.LogicBean;
import br.ufmg.lcc.arangi.controller.bean.TabularLogicBean;
import br.ufmg.lcc.arangi.dto.BasicDTO;
import br.ufmg.lcc.arangi.model.IFacade;
import br.ufmg.lcc.arangi.model.ModelService;

/**
 * 
 * @author Cesar Correia
 *
 */
public class TabularController extends SavingController {

    @Override
    protected ControllerBean logicNewObject(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        controllerBean = super.logicNewObject(context, controllerBean);
        this.addItem(context, controllerBean);
        TabularLogicBean tabularLogic = (TabularLogicBean) controllerBean.getCurrentLogicBean();
        if (tabularLogic.isEditOnePerTime()) {
            if (tabularLogic.isInsertAtEnd()) {
                tabularLogic.setActiveItemIndex(controllerBean.getTabularList().size() - 1);
            } else {
                tabularLogic.setActiveItemIndex(0);
            }
        }
        return controllerBean;
    }

    @Override
    protected ControllerBean addItem(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        TabularLogicBean tabularLogic = (TabularLogicBean) controllerBean.getCurrentLogicBean();
        this.insertItem(context, controllerBean, controllerBean.getTabularList(), tabularLogic.isInsertAtEnd(), tabularLogic.getNumberOfItems(), tabularLogic.getDtoClassName());
        return controllerBean;
    }

    @Override
    protected ControllerBean logicCancel(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        TabularLogicBean tabularLogic = (TabularLogicBean) controllerBean.getCurrentLogicBean();
        if (tabularLogic.isEditOnePerTime()) {
            BasicDTO dto = (BasicDTO) controllerBean.getTabularList().get(tabularLogic.getActiveItemIndex());
            if (dto.getId() == null) {
                controllerBean.getTabularList().remove(tabularLogic.getActiveItemIndex());
            }
            tabularLogic.setActiveItemIndex(-1);
        }
        return controllerBean;
    }

    @Override
    protected ControllerBean logicModify(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        TabularLogicBean tabularLogic = (TabularLogicBean) controllerBean.getCurrentLogicBean();
        if (tabularLogic.isEditOnePerTime()) {
            String index = context.getRequestParameter("index");
            if (NVLHelper.nvl(index, "").equals("")) {
                index = "-1";
            }
            tabularLogic.setActiveItemIndex(Integer.parseInt(index));
        }
        List list = controllerBean.getTabularList();
        for (int k = 0; k < list.size(); k++) {
            BasicDTO dtoDetail = (BasicDTO) list.get(k);
            dtoDetail.setCheckDelete(false);
        }
        return controllerBean;
    }

    @Override
    protected ControllerBean logicSave(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        IFacade facade = ModelService.getFacade();
        TabularLogicBean tabularLogic = (TabularLogicBean) controllerBean.getCurrentLogicBean();
        List list = controllerBean.getTabularList();
        if (tabularLogic.isEditOnePerTime()) {
            if (tabularLogic.getActiveItemIndex() >= 0) {
                BasicDTO dto = (BasicDTO) list.get(tabularLogic.getActiveItemIndex());
                dto.setDiscard(verifyDiscard(dto, (TabularLogicBean) controllerBean.getCurrentLogicBean()));
                if (!dto.isDiscard()) {
                    populateAudit(dto, context);
                    if (dto.isCheckDelete()) {
                        if (dto.getId() != null) {
                            facade.delete(dto);
                        }
                        list.remove(tabularLogic.getActiveItemIndex());
                    } else {
                        try {
                            List messages = this.basicValidateDTO(context, controllerBean, dto);
                            if (messages != null && messages.size() > 0) {
                                controllerBean.getMessages().addAll(messages);
                                controllerBean.setHasValidationErros(true);
                            }
                        } catch (Exception e) {
                            throw BasicException.basicErrorHandling("Error validating data", "msgErrorValidatingData", e, log);
                        }
                        if (controllerBean.isHasValidationErros()) {
                            throw BasicException.basicErrorHandling("Error validating data", "msgValidatingErrorsFound", new String[] {}, log);
                        }
                        this.beforeSave(context, controllerBean);
                        if (dto.getId() != null) {
                            facade.update(dto, null);
                        } else {
                            facade.insert(dto);
                        }
                    }
                    controllerBean.setHasModified(true);
                }
            }
            tabularLogic.setActiveItemIndex(-1);
        } else {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                BasicDTO dto = (BasicDTO) it.next();
                dto.setDiscard(verifyDiscard(dto, (TabularLogicBean) controllerBean.getCurrentLogicBean()));
                if (dto.isDiscard()) {
                    continue;
                }
                populateAudit(dto, context);
                if (dto.isCheckDelete()) {
                    if (dto.getId() != null) {
                        facade.delete(dto);
                    }
                    it.remove();
                    controllerBean.setHasModified(true);
                } else {
                    try {
                        List messages = this.basicValidateDTO(context, controllerBean, dto);
                        if (messages != null && messages.size() > 0) {
                            controllerBean.getMessages().addAll(messages);
                            controllerBean.setHasValidationErros(true);
                        }
                    } catch (Exception e) {
                        throw BasicException.errorHandling("Error validating data", "errorValidatingData", e, log);
                    }
                    if (controllerBean.isHasValidationErros()) {
                        throw BasicException.basicErrorHandling("Error validating data", "msgValidatingErrorsFound", new String[] {}, log);
                    }
                    this.beforeSave(context, controllerBean);
                    if (dto.getId() != null) {
                        facade.update(dto, null);
                    } else {
                        facade.insert(dto);
                        controllerBean.setHasModified(true);
                    }
                }
            }
        }
        controllerBean = this.search(context, controllerBean);
        List dynamicLists = tabularLogic.getDynamicListsRelated();
        if (dynamicLists != null) {
            for (int i = 0; i < dynamicLists.size(); i++) {
                String listName = (String) dynamicLists.get(i);
                this.buildSelectList(controllerBean, listName, controllerBean.getTabularList(), false);
            }
        }
        return controllerBean;
    }

    @Override
    protected ControllerBean logicDelete(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        LogicBean logic = controllerBean.getCurrentLogicBean();
        List dynamicLists = logic.getDynamicListsRelated();
        IFacade facade = ModelService.getFacade();
        List list = controllerBean.getTabularList();
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                BasicDTO dto = (BasicDTO) it.next();
                populateAudit(dto, context);
                if (dto.isCheckDelete()) {
                    facade.delete(dto);
                    it.remove();
                    controllerBean.setHasModified(true);
                }
            }
        }
        if (dynamicLists != null) {
            for (int i = 0; i < dynamicLists.size(); i++) {
                String listName = (String) dynamicLists.get(i);
                this.buildSelectList(controllerBean, listName, list, false);
            }
        }
        return controllerBean;
    }

    @Override
    protected ControllerBean logicSearch(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        IFacade facade = ModelService.getFacade();
        this.beforeSearch(context, controllerBean);
        LogicBean logic = controllerBean.getCurrentLogicBean();
        List list = facade.loadAll(controllerBean.getDto(), logic.getDtoClassName());
        if (logic.isShowNothingFoundMessage() && (list == null || list.size() == 0)) {
            controllerBean.addMessage("messageNothingFound", new String[] {}, Message.TYPE_WARN, "ArangiResources");
        }
        TabularLogicBean tabularLogic = (TabularLogicBean) logic;
        tabularLogic.setDataReadOnly(false);
        tabularLogic.setActiveItemIndex(-1);
        controllerBean.setTabularList(list);
        return controllerBean;
    }

    @Override
    protected void logicRenderButtons(IApplicationContext context, ControllerBean controllerBean, BasicButtonBarController buttonBarController) {
        TabularLogicBean tabularLogic = (TabularLogicBean) controllerBean.getCurrentLogicBean();
        buttonBarController.setButtonPrintVisible(true);
        buttonBarController.setButtonSearchVisible(false);
        if (tabularLogic.isEditOnePerTime()) {
            if (tabularLogic.getActiveItemIndex() < 0) {
                buttonBarController.setButtonNewVisible(true);
                if (controllerBean.getTabularList() != null && controllerBean.getTabularList().size() > 0) {
                    buttonBarController.setButtonDeleteVisible(true);
                }
            } else {
                buttonBarController.setButtonSaveVisible(true);
                buttonBarController.setButtonCancelVisible(true);
            }
        } else {
            buttonBarController.setButtonNewVisible(true);
            buttonBarController.setButtonSaveVisible(true);
        }
    }
}
