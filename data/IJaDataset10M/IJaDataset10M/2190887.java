package net.sf.revolver.sample.service.userdata.insert;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import net.sf.bulletlib.authentication.core.LoginUser;
import net.sf.bulletlib.authentication.s2jdbc.UserData;
import net.sf.bulletlib.core.logging.Logging;
import net.sf.bulletlib.dbutil.s2jdbc.time.TimeUtilityDao;
import net.sf.revolver.core.BusinessLogicException;
import net.sf.revolver.core.Result;
import net.sf.revolver.core.RvContext;
import net.sf.revolver.s2.RvCtrlS2;
import net.sf.revolver.sample.dao.UserDataDao;
import net.sf.revolver.sample.dto.UserDataDto;

/**
 * userData insertビジネスロジック処理Ctrlクラス.
 *
 * @author bose999
 */
public class InsertUserDataCtrl extends RvCtrlS2 {

    /**
     * UserDataDao.
     */
    public UserDataDao userDataDao;

    /**
     * TimeUtilityDao.
     */
    public TimeUtilityDao timeUtilityDao;

    /**
     * userData insertビジネスロジックValidate処理.
     *
     * @param rvContext RvContext
     * @return Result
     * @throws BusinessLogicException Exception
     */
    @Override
    protected Result doValidate(RvContext rvContext) throws BusinessLogicException {
        executeValidator(rvContext, UserDataDto.class);
        return returnValidateResult(rvContext);
    }

    /**
     * userData insert処理.
     *
     * @param rvContext RvContext
     * @return Result
     * @throws BusinessLogicException Exception
     */
    @Override
    protected Result doLogic(RvContext rvContext) throws BusinessLogicException {
        Timestamp nowTimestamp = timeUtilityDao.getCurrentTimestamp();
        LoginUser loginUser = getSessionLoginUser(rvContext);
        UserData userData = converter.toBean(UserData.class, rvContext.getInValues(), true);
        userData.deleteFlag = 0;
        userData.createId = loginUser.userTableId;
        userData.createDate = nowTimestamp;
        userData.editId = loginUser.userTableId;
        userData.editDate = nowTimestamp;
        Integer count = userDataDao.insert(userData);
        Logging.debug(this.getClass(), "999999999", rvContext.getLoginId(), "userData insert:" + count.toString());
        return Result.SUCCESS;
    }

    /**
     * 画面への戻り値セット処理.
     *
     * @param rvContext RvContext
     */
    @Override
    protected void setReturnViewValues(RvContext rvContext) {
        if (rvContext.hasErrorMessage()) {
            Map<String, Object> userData = new HashMap<String, Object>();
            userData.put("userId", rvContext.getInValue("userId"));
            userData.put("password", rvContext.getInValue("password"));
            userData.put("locale", rvContext.getInValue("locale"));
            Map<String, Object> viewValues = getViewValues(rvContext);
            viewValues.put("userData", userData);
            Logging.debug(this.getClass(), "RVS999999", rvContext.getLoginId(), "viewValues:" + viewValues);
            setRetrunViewValues(rvContext, viewValues);
        }
    }
}
