package br.ufmg.lcc.eid.controller;

import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.controller.IApplicationContext;
import br.ufmg.lcc.arangi.controller.Message;
import br.ufmg.lcc.arangi.controller.SearchController;
import br.ufmg.lcc.arangi.controller.bean.ControllerBean;
import br.ufmg.lcc.arangi.model.IFacade;
import br.ufmg.lcc.arangi.model.ModelService;
import br.ufmg.lcc.eid.dto.ClassDef;
import br.ufmg.lcc.eid.model.ClassDefBO;
import br.ufmg.lcc.eid.model.IEidFacade;

public class ClassDefSelController extends SearchController {

    @SuppressWarnings("unchecked")
    protected ControllerBean beforeDelete(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        int j = 0;
        List listClassDef = (List) controllerBean.getSearchList();
        for (int i = 0; i < listClassDef.size(); i++) {
            ClassDef cDef = (ClassDef) listClassDef.get(i);
            if (cDef.isCheckDelete()) {
                j++;
            }
        }
        if (j == 0) {
        }
        return controllerBean;
    }

    @Override
    protected void afterRenderButtons(IApplicationContext context, ControllerBean controllerBean) {
        EidButtonBarController buttonBarController = (EidButtonBarController) controllerBean.getButtonBarController();
        buttonBarController.setButtonSearchVisible(false);
        buttonBarController.setButtonPrintVisible(true);
    }

    /**
	 * Recompile all classes registered in database
	 */
    public ControllerBean rebuildClass(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        IFacade facade = ModelService.getFacade();
        ClassDef classDef = (ClassDef) controllerBean.getDto();
        classDef = (ClassDef) facade.executeGenericOperation(ClassDefBO.class.getName(), "rebuildAllClasses", new Object[] { classDef });
        controllerBean.addMessage("ClassDefSelController.rebuldClass.sucess", new String[] {}, Message.TYPE_INFO, "EidApplicationResources");
        return controllerBean;
    }

    /**
	 * This method calls the Facade method to delete all instances of a class from the database.
	 * @throws BasicException 
	 */
    public ControllerBean removeInstancesOfEidClass(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        FacesContext faces = FacesContext.getCurrentInstance();
        Map<String, String> map = faces.getExternalContext().getRequestParameterMap();
        String entidadeDestino = map.get("name");
        entidadeDestino = "TBL_SVC_" + entidadeDestino.toUpperCase();
        IEidFacade facade = (IEidFacade) ModelService.getFacade();
        facade.deleteInstancesOfEidClass(entidadeDestino, controllerBean);
        return controllerBean;
    }
}
