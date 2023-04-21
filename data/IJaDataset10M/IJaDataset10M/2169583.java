package cn.chengdu.in.parser;

import org.json.JSONException;
import org.json.JSONObject;
import cn.chengdu.in.type.Mayor;

/**
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-3-9
 */
public class MayorParser extends AbstractParser<Mayor> {

    @Override
    public Mayor parse(JSONObject json) throws JSONException {
        Mayor obj = new Mayor();
        if (json.has("username")) {
            obj.setUsername(json.getString("username"));
        }
        if (json.has("userId")) {
            obj.setUserId(json.getString("userId"));
        }
        if (json.has("avatarUri")) {
            obj.setAvatarUri(json.getString("avatarUri"));
        }
        if (json.has("advantage")) {
            obj.setAdvantage(json.getString("advantage"));
        }
        return obj;
    }
}
