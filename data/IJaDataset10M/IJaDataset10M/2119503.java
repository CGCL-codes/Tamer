package com.aimluck.eip.blog;

import java.util.ArrayList;
import java.util.List;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.blog.util.BlogUtils;
import com.aimluck.eip.cayenne.om.portlet.EipTBlogEntry;
import com.aimluck.eip.cayenne.om.portlet.EipTBlogThema;
import com.aimluck.eip.common.ALAbstractCheckList;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.orm.Database;
import com.aimluck.eip.orm.query.SelectQuery;
import com.aimluck.eip.services.accessctl.ALAccessControlConstants;
import com.aimluck.eip.services.eventlog.ALEventlogConstants;
import com.aimluck.eip.services.eventlog.ALEventlogFactoryService;
import com.aimluck.eip.util.ALEipUtils;

/**
 * ブログテーマの複数削除を行うためのクラスです。 <BR>
 * 
 */
public class BlogThemaMultiDelete extends ALAbstractCheckList {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(BlogThemaMultiDelete.class.getName());

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
            List<Integer> intValues = new ArrayList<Integer>();
            int valuesize = values.size();
            for (int i = 0; i < valuesize; i++) {
                intValues.add(Integer.valueOf(values.get(i)));
            }
            SelectQuery<EipTBlogThema> query = Database.query(EipTBlogThema.class);
            Expression exp2 = ExpressionFactory.inDbExp(EipTBlogThema.THEMA_ID_PK_COLUMN, values);
            query.setQualifier(exp2);
            List<EipTBlogThema> themalist = query.fetchList();
            if (themalist == null || themalist.size() == 0) {
                return false;
            }
            List<Integer> themaIds = new ArrayList<Integer>();
            EipTBlogThema thema = null;
            int themasize = themalist.size();
            for (int i = 0; i < themasize; i++) {
                thema = themalist.get(i);
                themaIds.add(thema.getThemaId());
            }
            SelectQuery<EipTBlogEntry> reqquery = Database.query(EipTBlogEntry.class);
            Expression reqexp1 = ExpressionFactory.inDbExp(EipTBlogEntry.EIP_TBLOG_THEMA_PROPERTY + "." + EipTBlogThema.THEMA_ID_PK_COLUMN, values);
            reqquery.setQualifier(reqexp1);
            List<EipTBlogEntry> requests = reqquery.fetchList();
            if (requests != null && requests.size() > 0) {
                EipTBlogEntry request = null;
                EipTBlogThema defaultThema = BlogUtils.getEipTBlogThema(Long.valueOf(1));
                int size = requests.size();
                for (int i = 0; i < size; i++) {
                    request = requests.get(i);
                    request.setEipTBlogThema(defaultThema);
                }
            }
            Database.commit();
            int themalistsize = themalist.size();
            for (int i = 0; i < themalistsize; i++) {
                EipTBlogThema delete_thema = themalist.get(i);
                Integer entityId = delete_thema.getThemaId();
                String themaName = delete_thema.getThemaName();
                Database.delete(delete_thema);
                Database.commit();
                ALEventlogFactoryService.getInstance().getEventlogHandler().log(entityId, ALEventlogConstants.PORTLET_TYPE_BLOG_THEMA, themaName);
            }
            String filtername = BlogEntrySelectData.class.getName() + ALEipConstants.LIST_FILTER;
            ALEipUtils.removeTemp(rundata, context, filtername);
        } catch (Exception ex) {
            Database.rollback();
            logger.error("Exception", ex);
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
        return ALAccessControlConstants.POERTLET_FEATURE_BLOG_THEME;
    }
}
