package net.sf.revolver.sample.service.userdata.deleteroledata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.bulletlib.authentication.s2jdbc.UserRoleData;
import net.sf.bulletlib.core.logging.Logging;
import net.sf.bulletlib.validate.util.ValidateUtil;
import net.sf.revolver.core.BusinessLogicException;
import net.sf.revolver.core.Result;
import net.sf.revolver.core.RvContext;
import net.sf.revolver.s2.RvCtrlS2;
import net.sf.revolver.sample.dao.UserRoleDataDao;

/**
 * userRoleData Delete処理Ctrl.
 *
 * @author bose999
 */
public class DeleteUserRoleDataCtrl extends RvCtrlS2 {

    /**
     * UserRoleDataDao.
     */
    public UserRoleDataDao userRoleDataDao;

    /**
     * userData delete validate処理.
     *
     * @param rvContext RvContext
     * @return Result
     * @throws BusinessLogicException
     */
    @Override
    protected Result doValidate(RvContext rvContext) throws BusinessLogicException {
        String userDataId = (String) rvContext.getInValue("userDataId");
        if (!ValidateUtil.validateIsNotEmpty(userDataId)) {
            Logging.debug(this.getClass(), "RVS999999", rvContext.getLoginId(), "ViewValues.userDataId is empty.");
            addErrorMessage(rvContext, "viewValues.search", "deleteUserRoleDataCtrl.userDataId01");
        } else if (!ValidateUtil.validateIsInteger(userDataId)) {
            Logging.debug(this.getClass(), "RVS999999", rvContext.getLoginId(), "ViewValues.userDataId isn't Integer.");
            addErrorMessage(rvContext, "viewValues.search", "deleteUserRoleDataCtrl.userDataId02");
        }
        String roleDataId = (String) rvContext.getInValue("roleDataId");
        if (!ValidateUtil.validateIsNotEmpty(roleDataId)) {
            Logging.debug(this.getClass(), "RVS999999", rvContext.getLoginId(), "ViewValues.roleDataId is empty.");
            addErrorMessage(rvContext, "viewValues.search", "deleteUserRoleDataCtrl.roleDataId01");
        } else if (!ValidateUtil.validateIsInteger(roleDataId)) {
            Logging.debug(this.getClass(), "RVS999999", rvContext.getLoginId(), "ViewValues.roleDataId isn't Integer.");
            addErrorMessage(rvContext, "viewValues.search", "deleteUserRoleDataCtrl.roleDataId02");
        }
        return returnValidateResult(rvContext);
    }

    /**
     * userRoleData Delete処理.
     *
     * @param rvContext RvContext
     * @return Result
     * @throws BusinessLogicException Exception
     */
    @Override
    protected Result doLogic(RvContext rvContext) throws BusinessLogicException {
        Map<String, Object> whereMap = new HashMap<String, Object>();
        whereMap.put("userDataId", rvContext.getInValue("userDataId"));
        whereMap.put("roleDataId", rvContext.getInValue("roleDataId"));
        Logging.debug(this.getClass(), "RVS999999", rvContext.getLoginId(), "whereMap:" + whereMap);
        List<UserRoleData> userRoleData = userRoleDataDao.searchWhereMap(whereMap);
        Integer count = userRoleDataDao.delete(userRoleData.get(0));
        Logging.debug(this.getClass(), "RVS999999", rvContext.getLoginId(), "count:" + count);
        return Result.SUCCESS;
    }
}
