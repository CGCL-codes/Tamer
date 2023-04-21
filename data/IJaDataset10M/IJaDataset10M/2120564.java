package weibo4andriod;

import weibo4andriod.org.json.JSONException;
import weibo4andriod.org.json.JSONObject;

/**
 * A data class representing Treand.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Weibo4J 2.0.2
 */
public class Trend implements java.io.Serializable {

    private String name;

    private String url = null;

    private String query = null;

    private static final long serialVersionUID = 1925956704460743946L;

    public Trend(JSONObject json) throws JSONException {
        this.name = json.getString("name");
        if (!json.isNull("url")) {
            this.url = json.getString("url");
        }
        if (!json.isNull("query")) {
            this.query = json.getString("query");
        }
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trend)) return false;
        Trend trend = (Trend) o;
        if (!name.equals(trend.name)) return false;
        if (query != null ? !query.equals(trend.query) : trend.query != null) return false;
        if (url != null ? !url.equals(trend.url) : trend.url != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (query != null ? query.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Trend{" + "name='" + name + '\'' + ", url='" + url + '\'' + ", query='" + query + '\'' + '}';
    }
}
