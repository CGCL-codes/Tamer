package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.addressbook.AddressBookCompanySelectData;
import com.aimluck.eip.addressbook.AddressBookCompanyWordSelectData;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.util.ALEipUtils;

/**
 * アドレス帳の会社情報の一覧を処理するクラスです。
 *
 */
public class AddressBookCompanyListScreen extends AddressBookScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(AddressBookCompanyListScreen.class.getName());

    /**
   *
   * @param rundata
   * @param context
   * @throws Exception
   */
    @Override
    protected void doOutput(RunData rundata, Context context) throws Exception {
        try {
            AddressBookCompanyWordSelectData listData = new AddressBookCompanyWordSelectData();
            listData.setRowsNum(Integer.parseInt(ALEipUtils.getPortlet(rundata, context).getPortletConfig().getInitParameter("p1b-rows")));
            listData.setStrLength(Integer.parseInt(ALEipUtils.getPortlet(rundata, context).getPortletConfig().getInitParameter("p3a-strlen")));
            listData.doViewList(this, rundata, context);
            setTemplate(rundata, context, "portlets/html/ja/ajax-addressbook-company-list.vm");
        } catch (Exception ex) {
            logger.error("[AddressBookCompanyListScreen] Exception.", ex);
            ALEipUtils.redirectDBError(rundata);
        }
    }
}
