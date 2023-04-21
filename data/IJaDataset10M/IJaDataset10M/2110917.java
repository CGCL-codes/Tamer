package com.aimluck.eip.modules.screens;

import net.sf.json.JSONArray;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.todo.ToDoFormData;
import com.aimluck.eip.todo.ToDoMultiDelete;
import com.aimluck.eip.todo.ToDoMultiStateUpdate;

/**
 * ToDoをJSONデータとして出力するクラスです。 <br />
 * 
 */
public class ToDoFormJSONScreen extends ALJSONScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(ToDoFormJSONScreen.class.getName());

    @Override
    protected String getJSONString(RunData rundata, Context context) throws Exception {
        String result = new JSONArray().toString();
        String mode = this.getMode();
        try {
            if (ALEipConstants.MODE_INSERT.equals(mode)) {
                ToDoFormData formData = new ToDoFormData();
                formData.initField();
                formData.loadCategoryList(rundata);
                if (formData.doInsert(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            } else if (ALEipConstants.MODE_UPDATE.equals(mode)) {
                ToDoFormData formData = new ToDoFormData();
                formData.initField();
                formData.loadCategoryList(rundata);
                if (formData.doUpdate(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            } else if (ALEipConstants.MODE_DELETE.equals(mode)) {
                ToDoFormData formData = new ToDoFormData();
                formData.initField();
                formData.loadCategoryList(rundata);
                if (formData.doDelete(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            } else if ("multi_delete".equals(mode)) {
                ToDoMultiDelete delete = new ToDoMultiDelete();
                if (delete.doMultiAction(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            } else if ("multi_complete".equals(mode)) {
                ToDoMultiStateUpdate delete = new ToDoMultiStateUpdate();
                if (delete.doMultiAction(this, rundata, context)) {
                } else {
                    JSONArray json = JSONArray.fromObject(context.get(ALEipConstants.ERROR_MESSAGE_LIST));
                    result = json.toString();
                }
            }
        } catch (Exception e) {
            logger.error("[ToDoFormJSONScreen]", e);
        }
        return result;
    }
}
