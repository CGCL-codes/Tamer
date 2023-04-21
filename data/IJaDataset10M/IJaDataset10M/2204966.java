package scp.web.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import scp.kernel.persistent.Function;
import scp.kernel.persistent.Persistent;
import scp.kernel.sa.BusinessException;
import scp.kernel.sa.MaintainFunction;

/**
 * Action respons�vel pelas telas de administra��o de Fun��es.
 * 
 * @author Rafael Abreu
 * 
 */
public class MaintainFunctionAction extends MaintainSCPAction {

    @Override
    protected void loadDefault(ActionForm actionForm, HttpServletRequest request) throws BusinessException {
    }

    @Override
    protected List getListAllPersistents() throws BusinessException {
        return new MaintainFunction().listFunctions();
    }

    @Override
    protected String getMessageListEmpty() {
        return "maintainfunction.list.empty";
    }

    @Override
    protected Persistent getPersistent(long id) throws BusinessException {
        return new MaintainFunction().findFunction(id);
    }

    @Override
    protected void deletePersistent(long id) throws BusinessException {
        new MaintainFunction().deleteFunction(id);
    }

    @Override
    protected String getMessageDeleteSuccesful() {
        return "maintainfunction.delete.succesful";
    }

    @Override
    protected Persistent newPersistent() {
        return new Function();
    }

    @Override
    protected String getMessageSaveSuccesful() {
        return "maintainfunction.save.succesful";
    }

    @Override
    public void clearDynaForm(DynaActionForm form) {
        form.set("id", null);
        form.set("name", null);
        form.set("description", null);
    }

    @Override
    protected void savePersistent(DynaActionForm form, Persistent persistent) throws BusinessException {
        new MaintainFunction().saveFunction((Function) persistent);
    }

    @Override
    protected void loadPersistent(DynaActionForm form, Persistent persistent) throws Exception {
        BeanUtils.copyProperties(form, persistent);
    }
}
