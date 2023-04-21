package br.ufmg.lcc.arangi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.math.NumberUtils;
import br.ufmg.lcc.arangi.commons.BeanHelper;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.controller.bean.ControllerBean;
import br.ufmg.lcc.arangi.controller.bean.LogicBean;
import br.ufmg.lcc.arangi.controller.bean.SavingLogicBean;
import br.ufmg.lcc.arangi.controller.bean.ValidationBean;
import br.ufmg.lcc.arangi.dto.BasicDTO;
import br.ufmg.lcc.arangi.model.IFacade;
import br.ufmg.lcc.arangi.model.ModelService;

/**
 * 
 * @author Cesar Correia
 *
 */
public class SavingController extends SearchController {

    @Override
    protected ControllerBean logicNewObject(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        LogicBean logic = controllerBean.getCurrentLogicBean();
        BasicDTO dto = (BasicDTO) newInstance(logic.getDtoClassName());
        controllerBean.setDto(dto);
        SavingLogicBean savingLogic = (SavingLogicBean) logic;
        savingLogic.setDataReadOnly(false);
        return controllerBean;
    }

    /**
	 * The save event. This event is fired when the user clicks on save button. It's used
	 * for store changes of user in database. For master-detail logic the bean and all your 
	 * collections are sended to Model tier to be persisted (or deleted, case the user the ceckToDelete
	 * bean property is setted to "true"). For tabular if the editOnePerTime
	 * logic property is signed to "true", only the bean whose index is active is sended to Model tier.
	 * Else, all beans are sended to Model tier to be persisted (or deleted).
	 * <p><p>
	 * If the logic signed dynamic combobox list, the list is refreshed.
	 * <p><p>
	 * For master-detail logic the detail collections are sorted at end.
	 * <p><p>
	 * For tabular logic the list with all elements of database are fetched at end by
	 * a call to search event.
	 * <p><p>
	 * @see br.ufmg.lcc.arangi.controller.bean.SavingLogicBean for more options.
	 * <p><p>
	 * This method make one Model call.
	 * <p><p>
	 * IMPORTANT: To avoid F5 (refresh browser action) pushes that realizes one more (and illegal) save action,
	 * if logic read only property is true, no action is performed.
	 * <p><p>
	 * The string forward directive is setted to "save".
	 * @param logic
	 * @param context
	 * @param controllerBean
	 * @return
	 * @throws BasicException
	 */
    public ControllerBean save(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        this.beforeProcess(context, controllerBean, SAVE_EVENT);
        LogicBean logic = controllerBean.getCurrentLogicBean();
        this.buildSelectLists(controllerBean);
        controllerBean.setForward("save");
        controllerBean.setHasModified(false);
        controllerBean.setHasValidationErros(false);
        SavingLogicBean savingLogic = (SavingLogicBean) logic;
        if (savingLogic.isDataReadOnly()) {
            return controllerBean;
        }
        controllerBean = this.logicSave(context, controllerBean);
        if (savingLogic.isReadOnlyAfterSaving()) {
            savingLogic.setDataReadOnly(true);
        } else {
            savingLogic.setDataReadOnly(false);
        }
        if (controllerBean.isHasModified()) {
            controllerBean.addMessage("messageSaveSuccess", new String[] {}, Message.TYPE_INFO, "ArangiResources");
        } else {
            controllerBean.addMessage("messageNothingSaved", new String[] {}, Message.TYPE_WARN, "ArangiResources");
        }
        controllerBean = this.afterSave(context, controllerBean);
        this.afterProcess(context, controllerBean, SAVE_EVENT);
        return controllerBean;
    }

    /**
	 * Extension point for developer use.
	 * @param logic
	 * @param context
	 * @param controllerBean
	 * @return
	 * @throws BasicException
	 */
    protected ControllerBean beforeSave(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        return controllerBean;
    }

    /**
	 * Extension point for developer use.
	 * @param logic
	 * @param context
	 * @param controllerBean
	 * @return
	 * @throws BasicException
	 */
    protected ControllerBean afterSave(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        return controllerBean;
    }

    /**
	 * Logic specific code.
	 * @param logic
	 * @param context
	 * @param controllerBean
	 * @return
	 * @throws BasicException
	 */
    protected ControllerBean logicSave(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        return controllerBean;
    }

    /**
	 * The edit event. This event is fired when the user clicks in one link that points to 
	 * id of bean. It's used for retrieve on object from database. The bean class is signed
	 * by dtoClassName logic property and the id is retrieved by request parameter named "id".
	 * <p><p>
	 * For master-detail logic the bean and all your detail collections are fetched together.
	 * <p><p>
	 * This method make one Model call.
	 * <p><p>
	 * The string forward directive is setted to "this".
	 * @param logic
	 * @param context
	 * @param controllerBean
	 * @return
	 * @throws BasicException
	 */
    public ControllerBean edit(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        LogicBean logic = controllerBean.getCurrentLogicBean();
        this.beforeProcess(context, controllerBean, EDIT_EVENT);
        IFacade facade = ModelService.getFacade();
        controllerBean.setForward("edit");
        String idAux = (String) context.getRequestParameter("id");
        this.buildSelectLists(controllerBean);
        Long id = this.beforeEdit(context, controllerBean);
        BasicDTO dto = controllerBean.getDto();
        populateAudit(dto, context);
        if (NumberUtils.isNumber(idAux)) {
            id = new Long(idAux);
            dto.setId(id);
        } else {
            dto.setStringID(idAux);
        }
        dto = facade.load(dto);
        SavingLogicBean savingLogic = (SavingLogicBean) logic;
        savingLogic.setDataReadOnly(false);
        controllerBean.setDto(dto);
        this.logicEdit(context, controllerBean);
        if (logic.isShowNothingFoundMessage() && controllerBean.getDto() == null) {
            controllerBean.setDto((BasicDTO) this.newInstance(logic.getDtoClassName()));
            controllerBean.addMessage("messageObjectNotFound", new String[] {}, Message.TYPE_WARN, "ArangiResources");
        }
        controllerBean = this.afterEdit(context, controllerBean);
        this.afterProcess(context, controllerBean, EDIT_EVENT);
        return controllerBean;
    }

    /**
	 * Extension point for developer use.
	 * @param logic
	 * @param context
	 * @param controllerBean
	 * @return
	 */
    protected Long beforeEdit(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        return null;
    }

    /**
	 * Extension point for developer use.
	 * @param logic
	 * @param context
	 * @param controllerBean
	 * @return
	 * @throws BasicException
	 */
    protected ControllerBean afterEdit(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        return controllerBean;
    }

    /**
	 * Logic specific code.
	 * @param logic
	 * @param context
	 * @param controllerBean
	 * @return
	 * @throws BasicException
	 */
    protected ControllerBean logicEdit(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        return controllerBean;
    }

    /**
	 * The view event. This event call edit event and after set dataReadOnly logic flag
	 * to "true".
	 * <p><p>
	 * The string forward directive is setted to "this".
	 * @param logic
	 * @param context
	 * @param controllerBean
	 * @return
	 * @throws BasicException
	 */
    public ControllerBean view(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        LogicBean logic = controllerBean.getCurrentLogicBean();
        this.beforeProcess(context, controllerBean, VIEW_EVENT);
        this.buildSelectLists(controllerBean);
        controllerBean = this.beforeView(context, controllerBean);
        controllerBean.setForward("view");
        controllerBean = this.edit(context, controllerBean);
        SavingLogicBean savingLogic = (SavingLogicBean) logic;
        savingLogic.setDataReadOnly(true);
        controllerBean = this.afterView(context, controllerBean);
        this.beforeProcess(context, controllerBean, VIEW_EVENT);
        return controllerBean;
    }

    /**
	 * Extension point for developer use.
	 * @param logic
	 * @param context
	 * @param controllerBean
	 * @return
	 * @throws BasicException
	 */
    protected ControllerBean beforeView(IApplicationContext context, ControllerBean controllerBean) {
        return controllerBean;
    }

    /**
	 * Extension point for developer use.
	 * @param logic
	 * @param context
	 * @param controllerBean
	 * @return
	 * @throws BasicException
	 */
    protected ControllerBean afterView(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        return controllerBean;
    }

    @Override
    protected ControllerBean logicCancel(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        LogicBean logic = controllerBean.getCurrentLogicBean();
        SavingLogicBean savingLogic = (SavingLogicBean) logic;
        savingLogic.setDataReadOnly(true);
        return controllerBean;
    }

    @Override
    protected ControllerBean logicClone(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        LogicBean logic = controllerBean.getCurrentLogicBean();
        SavingLogicBean savingLogic = (SavingLogicBean) logic;
        savingLogic.setDataReadOnly(false);
        return controllerBean;
    }
}
