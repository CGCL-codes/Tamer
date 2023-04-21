package com.liferay.portlet.wiki.service.http;

import com.liferay.portlet.wiki.model.WikiNode;
import com.liferay.util.JSONUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

/**
 * <a href="WikiNodeJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by <code>com.liferay.portlet.wiki.service.http.WikiNodeServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.wiki.service.http.WikiNodeServiceJSON
 *
 */
public class WikiNodeJSONSerializer {

    public static JSONObject toJSONObject(WikiNode model) {
        JSONObject jsonObj = new JSONObject();
        JSONUtil.put(jsonObj, "nodeId", model.getNodeId());
        JSONUtil.put(jsonObj, "groupId", model.getGroupId());
        JSONUtil.put(jsonObj, "companyId", model.getCompanyId());
        JSONUtil.put(jsonObj, "userId", model.getUserId());
        JSONUtil.put(jsonObj, "userName", model.getUserName());
        JSONUtil.put(jsonObj, "createDate", model.getCreateDate());
        JSONUtil.put(jsonObj, "modifiedDate", model.getModifiedDate());
        JSONUtil.put(jsonObj, "name", model.getName());
        JSONUtil.put(jsonObj, "description", model.getDescription());
        JSONUtil.put(jsonObj, "lastPostDate", model.getLastPostDate());
        return jsonObj;
    }

    public static JSONArray toJSONArray(List models) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < models.size(); i++) {
            WikiNode model = (WikiNode) models.get(i);
            jsonArray.put(toJSONObject(model));
        }
        return jsonArray;
    }
}
