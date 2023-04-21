package cn.chengdu.in.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cn.chengdu.in.type.IcdList;
import cn.chengdu.in.type.IcdType;
import cn.chengdu.in.type.Post;

/**
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-2-16
 */
public class ListParser<T extends IcdType> extends AbstractParser<IcdList<T>> {

    private Parser<T> mSubParser;

    public ListParser(Parser<T> subParser) {
        mSubParser = subParser;
    }

    public IcdList<T> parse(JSONObject json) throws JSONException {
        IcdList<T> list = new IcdList<T>();
        if (json.has("categoryName")) {
            list.setTitle(json.getString("categoryName"));
        }
        if (json.has("resultList")) {
            parse(list, json.getJSONArray("resultList"));
        }
        if (json.has("list")) {
            parse(list, json.getJSONArray("list"));
        }
        if (json.has("hottest")) {
            parse(list, json.getJSONArray("hottest"));
            for (T obj : list) {
                Post post = (Post) obj;
                post.setCategory(Post.CATEGORY_HOT);
            }
        }
        if (json.has("newest")) {
            parse(list, json.getJSONArray("newest"));
        }
        return list;
    }

    public IcdList<T> parse(JSONArray array) throws JSONException {
        IcdList<T> list = new IcdList<T>();
        parse(list, array);
        return list;
    }

    private void parse(IcdList<T> list, JSONArray array) throws JSONException {
        for (int i = 0, m = array.length(); i < m; i++) {
            T item = mSubParser.parse(array.getJSONObject(i));
            list.add(item);
        }
    }
}
