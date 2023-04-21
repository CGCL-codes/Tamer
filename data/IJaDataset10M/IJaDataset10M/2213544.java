package com.aimluck.eip.category;

import java.util.List;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.services.TurbineServices;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.category.util.CommonCategoryUtils;
import com.aimluck.eip.cayenne.om.portlet.EipTCommonCategory;
import com.aimluck.eip.common.ALAbstractCheckList;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.orm.Database;
import com.aimluck.eip.orm.query.SelectQuery;
import com.aimluck.eip.services.accessctl.ALAccessControlConstants;
import com.aimluck.eip.services.accessctl.ALAccessControlFactoryService;
import com.aimluck.eip.services.accessctl.ALAccessControlHandler;
import com.aimluck.eip.services.eventlog.ALEventlogConstants;
import com.aimluck.eip.services.eventlog.ALEventlogFactoryService;
import com.aimluck.eip.util.ALEipUtils;

/**
 * 複数の共有カテゴリを削除するクラスです。 <br />
 * 
 */
public class CommonCategoryMultiDelete extends ALAbstractCheckList {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(CommonCategoryMultiDelete.class.getName());

    /**
   * 
   * @param rundata
   * @param context
   * @param values
   * @param msgList
   * @return
   */
    @Override
    protected boolean action(RunData rundata, Context context, List<String> values, List<String> msgList) {
        try {
            int loginuserid = ALEipUtils.getUserId(rundata);
            ALAccessControlFactoryService aclservice = (ALAccessControlFactoryService) ((TurbineServices) TurbineServices.getInstance()).getService(ALAccessControlFactoryService.SERVICE_NAME);
            ALAccessControlHandler aclhandler = aclservice.getAccessControlHandler();
            boolean hasAuthorityOtherDelete = aclhandler.hasAuthority(loginuserid, ALAccessControlConstants.POERTLET_FEATURE_MANHOUR_COMMON_CATEGORY_OTHER, ALAccessControlConstants.VALUE_ACL_DELETE);
            SelectQuery<EipTCommonCategory> query = Database.query(EipTCommonCategory.class);
            if (!hasAuthorityOtherDelete) {
                Expression exp1 = ExpressionFactory.matchExp(EipTCommonCategory.CREATE_USER_ID_PROPERTY, Integer.valueOf(loginuserid));
                query.andQualifier(exp1);
            }
            Expression exp2 = ExpressionFactory.inDbExp(EipTCommonCategory.COMMON_CATEGORY_ID_PK_COLUMN, values);
            query.andQualifier(exp2);
            List<EipTCommonCategory> commoncategory_list = query.fetchList();
            if (commoncategory_list == null || commoncategory_list.size() == 0) {
                return false;
            }
            for (EipTCommonCategory record : commoncategory_list) {
                CommonCategoryUtils.setDefaultCommonCategoryToSchedule(record);
            }
            Database.deleteAll(commoncategory_list);
            Database.commit();
            rundata.getParameters().add(ALEipConstants.MODE, ALEipConstants.MODE_MULTI_DELETE);
            for (EipTCommonCategory record : commoncategory_list) {
                ALEventlogFactoryService.getInstance().getEventlogHandler().log(record.getCommonCategoryId(), ALEventlogConstants.PORTLET_TYPE_COMMON_CATEGORY, record.getName());
            }
            String filtername = CommonCategorySelectData.class.getName() + ALEipConstants.LIST_FILTER;
            ALEipUtils.removeTemp(rundata, context, filtername);
        } catch (Throwable t) {
            Database.rollback();
            logger.error("[CommonCategoryMultiDelete]", t);
            return false;
        }
        return true;
    }

    /**
   * アクセス権限チェック用メソッド。<br />
   * アクセス権限を返します。
   * 
   * @return
   */
    @Override
    protected int getDefineAclType() {
        return ALAccessControlConstants.VALUE_ACL_DELETE;
    }

    /**
   * アクセス権限チェック用メソッド。<br />
   * アクセス権限の機能名を返します。
   * 
   * @return
   */
    @Override
    public String getAclPortletFeature() {
        return ALAccessControlConstants.POERTLET_FEATURE_MANHOUR_COMMON_CATEGORY;
    }
}
