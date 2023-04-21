package net.sourceforge.purrpackage.reporting.json;

import net.sourceforge.purrpackage.reporting.json.org.json.JSONObject;

public interface JsonFormatterElement<T> {

    public JSONObject format(T x, JsonFormatter utility) throws Exception;
}
