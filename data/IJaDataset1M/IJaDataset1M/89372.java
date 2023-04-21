package com.liferay.portlet.journal.service.http;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.journal.model.JournalFeed;
import java.util.Date;
import java.util.List;

/**
 * <a href="JournalFeedJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.liferay.portlet.journal.service.http.JournalFeedServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.journal.service.http.JournalFeedServiceJSON
 *
 */
public class JournalFeedJSONSerializer {

    public static JSONObject toJSONObject(JournalFeed model) {
        JSONObject jsonObj = JSONFactoryUtil.createJSONObject();
        jsonObj.put("uuid", model.getUuid());
        jsonObj.put("id", model.getId());
        jsonObj.put("groupId", model.getGroupId());
        jsonObj.put("companyId", model.getCompanyId());
        jsonObj.put("userId", model.getUserId());
        jsonObj.put("userName", model.getUserName());
        Date createDate = model.getCreateDate();
        String createDateJSON = StringPool.BLANK;
        if (createDate != null) {
            createDateJSON = String.valueOf(createDate.getTime());
        }
        jsonObj.put("createDate", createDateJSON);
        Date modifiedDate = model.getModifiedDate();
        String modifiedDateJSON = StringPool.BLANK;
        if (modifiedDate != null) {
            modifiedDateJSON = String.valueOf(modifiedDate.getTime());
        }
        jsonObj.put("modifiedDate", modifiedDateJSON);
        jsonObj.put("feedId", model.getFeedId());
        jsonObj.put("name", model.getName());
        jsonObj.put("description", model.getDescription());
        jsonObj.put("type", model.getType());
        jsonObj.put("structureId", model.getStructureId());
        jsonObj.put("templateId", model.getTemplateId());
        jsonObj.put("rendererTemplateId", model.getRendererTemplateId());
        jsonObj.put("delta", model.getDelta());
        jsonObj.put("orderByCol", model.getOrderByCol());
        jsonObj.put("orderByType", model.getOrderByType());
        jsonObj.put("targetLayoutFriendlyUrl", model.getTargetLayoutFriendlyUrl());
        jsonObj.put("targetPortletId", model.getTargetPortletId());
        jsonObj.put("contentField", model.getContentField());
        jsonObj.put("feedType", model.getFeedType());
        jsonObj.put("feedVersion", model.getFeedVersion());
        return jsonObj;
    }

    public static JSONArray toJSONArray(List<com.liferay.portlet.journal.model.JournalFeed> models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
        for (JournalFeed model : models) {
            jsonArray.put(toJSONObject(model));
        }
        return jsonArray;
    }
}
