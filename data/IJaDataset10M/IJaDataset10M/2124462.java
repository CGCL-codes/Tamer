package com.aimluck.eip.modules.screens;

import net.sf.json.JSONArray;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.addressbook.AddressBookCompanyFormData;
import com.aimluck.eip.addressbook.AddressBookCompanyMultiDelete;
import com.aimluck.eip.common.ALEipConstants;

/**
 * アドレス帳の会社情報をJSONデータとして出力するクラスです。
 * 
 */
public class AddressBookCompanyFormJSONScreen extends ALJSONScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(AddressBookCompanyFormJSONScreen.class.getName());

    @Override
    protected String getJSONString(RunData rundata, Context context) throws Exception {
        String result = new JSONArray().toString();
        String mode = this.getMode();
        try {
            if (ALEipConstants.MODE_INSERT.equals(mode)) {
                AddressBookCompanyFormData formData = new AddressBookCompanyFormData();
                formData.initField();
                if (formData.doInsert(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            } else if (ALEipConstants.MODE_UPDATE.equals(mode)) {
                AddressBookCompanyFormData formData = new AddressBookCompanyFormData();
                formData.initField();
                if (formData.doUpdate(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            } else if (ALEipConstants.MODE_DELETE.equals(mode)) {
                AddressBookCompanyFormData formData = new AddressBookCompanyFormData();
                formData.initField();
                if (formData.doDelete(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            } else if ("multi_delete".equals(mode)) {
                AddressBookCompanyMultiDelete delete = new AddressBookCompanyMultiDelete();
                if (delete.doMultiAction(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            }
        } catch (Exception e) {
            logger.error("[AddressBookCompanyFormJSONScreen]", e);
        }
        return result;
    }
}
