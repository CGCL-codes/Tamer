package weibo4j.model;

import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class ApiRateLimits implements java.io.Serializable {

    private static final long serialVersionUID = 8550645887134692311L;

    private String api;

    private int limit;

    private String limitTimeUnit;

    private long remainingHits;

    ApiRateLimits(JSONObject json) throws WeiboException {
        try {
            api = json.getString("api");
            limit = json.getInt("limit");
            limitTimeUnit = json.getString("limit_time_unit");
            remainingHits = json.getLong("remainingHits");
        } catch (JSONException jsone) {
            throw new WeiboException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLimitTimeUnit() {
        return limitTimeUnit;
    }

    public void setLimitTimeUnit(String limitTimeUnit) {
        this.limitTimeUnit = limitTimeUnit;
    }

    public long getRemainingHits() {
        return remainingHits;
    }

    public void setRemainingHits(long remainingHits) {
        this.remainingHits = remainingHits;
    }

    @Override
    public String toString() {
        return "api_rate_limits [api=" + api + ", limit=" + limit + ", limitTimeUnit=" + limitTimeUnit + ", remainingHits=" + remainingHits + "]";
    }
}
