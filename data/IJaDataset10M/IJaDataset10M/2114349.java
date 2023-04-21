package org.fb4j.friends.impl;

import java.util.HashSet;
import java.util.Set;
import net.sf.json.JSONArray;
import org.fb4j.impl.ConversionUtils;
import org.fb4j.impl.InvalidResponseException;
import org.fb4j.impl.JsonMethodCallBase;

/**
 * 
 * @author Gino Miceli
 * @author Mino Togna
 */
public class GetFriendsMethodCall extends JsonMethodCallBase<Set<Long>> {

    public GetFriendsMethodCall() {
        super("friends.get");
    }

    public GetFriendsMethodCall(long user) {
        super("friends.get");
        setParameter("uid", String.valueOf(user));
    }

    public GetFriendsMethodCall(long user, long friendsList) {
        super("friends.get");
        setParameter("uid", String.valueOf(user));
        setParameter("flid", String.valueOf(friendsList));
    }

    @Override
    protected Set<Long> processJsonResponse(String responseData) throws InvalidResponseException {
        if (isValidJsonArray(responseData)) {
            JSONArray jsonArray = JSONArray.fromObject(responseData);
            return ConversionUtils.convertJsonArray(jsonArray, new HashSet<Long>(jsonArray.size()));
        }
        return new HashSet<Long>();
    }
}
