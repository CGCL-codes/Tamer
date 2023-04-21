package com.liferay.portal.service.http;

import com.liferay.portal.model.Company;
import com.liferay.util.JSONUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

/**
 * <a href="CompanyJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by <code>com.liferay.portal.service.http.CompanyServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.http.CompanyServiceJSON
 *
 */
public class CompanyJSONSerializer {

    public static JSONObject toJSONObject(Company model) {
        JSONObject jsonObj = new JSONObject();
        JSONUtil.put(jsonObj, "companyId", model.getCompanyId());
        JSONUtil.put(jsonObj, "accountId", model.getAccountId());
        JSONUtil.put(jsonObj, "webId", model.getWebId());
        JSONUtil.put(jsonObj, "key", model.getKey());
        JSONUtil.put(jsonObj, "virtualHost", model.getVirtualHost());
        JSONUtil.put(jsonObj, "mx", model.getMx());
        JSONUtil.put(jsonObj, "logoId", model.getLogoId());
        return jsonObj;
    }

    public static JSONArray toJSONArray(List models) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < models.size(); i++) {
            Company model = (Company) models.get(i);
            jsonArray.put(toJSONObject(model));
        }
        return jsonArray;
    }
}
