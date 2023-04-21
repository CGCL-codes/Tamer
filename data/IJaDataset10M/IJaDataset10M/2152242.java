package com.aimluck.eip.account;

import java.util.jar.Attributes;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.account.util.AccountUtils;
import com.aimluck.eip.cayenne.om.account.EipMPosition;
import com.aimluck.eip.common.ALAbstractSelectData;
import com.aimluck.eip.common.ALDBErrorException;
import com.aimluck.eip.common.ALPageNotFoundException;
import com.aimluck.eip.modules.actions.common.ALAction;
import com.aimluck.eip.orm.Database;
import com.aimluck.eip.orm.query.ResultList;
import com.aimluck.eip.orm.query.SelectQuery;
import com.aimluck.eip.util.ALEipUtils;

/**
 * 役職の検索用データクラスです。
 * 
 */
public class AccountPositionSelectData extends ALAbstractSelectData<EipMPosition, EipMPosition> {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(AccountPositionSelectData.class.getName());

    /**
   * 
   * @param action
   * @param rundata
   * @param context
   */
    @Override
    public void init(ALAction action, RunData rundata, Context context) throws ALPageNotFoundException, ALDBErrorException {
        String sort = ALEipUtils.getTemp(rundata, context, LIST_SORT_STR);
        if (sort == null || sort.equals("")) {
            ALEipUtils.setTemp(rundata, context, LIST_SORT_STR, ALEipUtils.getPortlet(rundata, context).getPortletConfig().getInitParameter("p1b-sort"));
        }
        super.init(action, rundata, context);
    }

    /**
   * @param rundata
   * @param context
   * @return
   */
    @Override
    protected ResultList<EipMPosition> selectList(RunData rundata, Context context) {
        try {
            SelectQuery<EipMPosition> query = getSelectQuery(rundata, context);
            buildSelectQueryForListView(query);
            buildSelectQueryForListViewSort(query, rundata, context);
            return query.getResultList();
        } catch (Exception ex) {
            logger.error("Exception", ex);
            return null;
        }
    }

    /**
   * 検索条件を設定した SelectQuery を返します。 <BR>
   * 
   * @param rundata
   * @param context
   * @return
   */
    private SelectQuery<EipMPosition> getSelectQuery(RunData rundata, Context context) {
        return Database.query(EipMPosition.class);
    }

    /**
   * @param rundata
   * @param context
   * @return
   */
    @Override
    protected EipMPosition selectDetail(RunData rundata, Context context) {
        return AccountUtils.getEipMPosition(rundata, context);
    }

    /**
   * @param obj
   * @return
   */
    @Override
    protected Object getResultData(EipMPosition record) {
        AccountPositionResultData rd = new AccountPositionResultData();
        rd.initField();
        rd.setPositionId(record.getPositionId().intValue());
        rd.setPositionName(record.getPositionName());
        return rd;
    }

    /**
   * @param obj
   * @return
   */
    @Override
    protected Object getResultDataDetail(EipMPosition record) {
        AccountPositionResultData rd = new AccountPositionResultData();
        rd.initField();
        rd.setPositionId(record.getPositionId().intValue());
        rd.setPositionName(record.getPositionName());
        return rd;
    }

    /**
   * @return
   */
    @Override
    protected Attributes getColumnMap() {
        Attributes map = new Attributes();
        map.putValue("position_name", EipMPosition.POSITION_NAME_PROPERTY);
        return map;
    }
}
