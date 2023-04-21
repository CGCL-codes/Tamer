package com.aimluck.eip.modules.screens;

import net.sf.json.JSONArray;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.exttimecard.ExtTimecardFormData;

/**
 * タイムカードをJSONデータとして出力するクラスです。 <br />
 * 
 */
public class ExtTimecardFormJSONScreen extends ALJSONScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(ExtTimecardFormJSONScreen.class.getName());

    @Override
    protected String getJSONString(RunData rundata, Context context) throws Exception {
        String result = new JSONArray().toString();
        String mode = this.getMode();
        try {
            if (ALEipConstants.MODE_INSERT.equals(mode)) {
                ExtTimecardFormData formData = new ExtTimecardFormData();
                formData.initField();
                formData.setAltMode("alt_insert");
                if (formData.doInsert(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            } else if (ALEipConstants.MODE_UPDATE.equals(mode)) {
                ExtTimecardFormData formData = new ExtTimecardFormData();
                formData.initField();
                if (formData.doUpdate(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            } else if (ALEipConstants.MODE_DELETE.equals(mode)) {
                ExtTimecardFormData formData = new ExtTimecardFormData();
                formData.initField();
                if (formData.doDelete(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            } else if ("punchin".equals(mode) || "punchout".equals(mode) || "outgoing".equals(mode) || "comeback".equals(mode)) {
                ExtTimecardFormData formData = new ExtTimecardFormData();
                formData.initField();
                if (formData.doPunch(this, rundata, context, mode)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            }
        } catch (Exception e) {
            logger.error("[ExtTimecardFormJSONScreen]", e);
        }
        return result;
    }
}
