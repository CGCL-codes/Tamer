package net.sf.revolver.sample.service.roledata.search;

import java.util.List;
import java.util.Map;
import net.sf.bulletlib.authentication.s2jdbc.RoleData;
import net.sf.bulletlib.core.logging.Logging;
import net.sf.revolver.core.BusinessLogicException;
import net.sf.revolver.core.Result;
import net.sf.revolver.core.RvContext;
import net.sf.revolver.s2.RvCtrlS2;
import net.sf.revolver.sample.common.Globals;
import net.sf.revolver.sample.dao.RoleDataDao;
import net.sf.revolver.sample.dto.SearchRoleDataConditionDto;

/**
 * RoleData検索ビジネスロジック処理Ctrl.
 *
 * @author bose999
 */
public class SearchRoleDataCtrl extends RvCtrlS2 {

    /**
     * 1ページあたりのページ数.
     */
    private Long pageCount = Globals.PAGE_COUNT;

    /**
     * RoleDataDao.
     */
    public RoleDataDao roleDataDao;

    /**
     * SearchBookConditionDtoのValidate処理.
     *
     * @param rvContext RvContext
     * @return Result Result
     * @throws BusinessLogicException Exception
     */
    @Override
    protected Result doValidate(RvContext rvContext) throws BusinessLogicException {
        executeValidator(rvContext, SearchRoleDataConditionDto.class);
        return returnValidateResult(rvContext);
    }

    /**
     * RoleData検索処理.
     *
     * @param rvContext
     * @return
     * @throws BusinessLogicException
     */
    @Override
    protected Result doLogic(RvContext rvContext) throws BusinessLogicException {
        SearchRoleDataConditionDto searchRoleDataConditionDto = converter.toBean(SearchRoleDataConditionDto.class, rvContext.getInValues(), true);
        setFunctionSessionAttribute(rvContext, "searchRoleDataConditionDto", searchRoleDataConditionDto);
        Map<String, Object> whereMap = searchRoleDataConditionDto.getWhereMap();
        Logging.debug(this.getClass(), "999999999", rvContext.getLoginId(), "whereMap:" + whereMap);
        Long maxPage = roleDataDao.searchMaxPageWhereMap(pageCount, whereMap);
        List<RoleData> roleDatas = roleDataDao.searchPageWhereMap(Long.valueOf(1), pageCount, whereMap);
        if (roleDatas.size() == 0) {
            Logging.debug(this.getClass(), "999999999", rvContext.getLoginId(), "Search result is empty.");
            addErrorMessage(rvContext, "viewValues.search", "searchRoleDataCtrl.result01");
        }
        rvContext.setResultValue("maxPage", maxPage);
        rvContext.setResultValue("nowPage", Long.valueOf(1));
        rvContext.setResultValue("roleDatas", roleDatas);
        return Result.SUCCESS;
    }

    /**
     * 画面への戻り値セット処理.
     *
     * @param rvContext RvContext
     */
    @Override
    protected void setReturnViewValues(RvContext rvContext) {
        Map<String, Object> viewValues = getViewValues(rvContext);
        viewValues.put("roleDatas", rvContext.getResultValue("roleDatas"));
        viewValues.put("maxPage", rvContext.getResultValue("maxPage"));
        viewValues.put("nowPage", rvContext.getResultValue("nowPage"));
        setRetrunViewValues(rvContext, viewValues);
    }
}
