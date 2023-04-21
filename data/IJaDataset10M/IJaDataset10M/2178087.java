package com.kescom.matrix.core.api;

import java.io.IOException;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.kescom.matrix.core.env.MatrixContext;
import com.kescom.matrix.core.user.IUser;

public class StatusApiVerbHandler implements IApiVerbHandler {

    public ApiDict process(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        ApiDict dict = new ApiDict();
        IUser user = (IUser) MatrixContext.getThreadUserdata("User");
        if (user != null) {
            dict.put("user", user.getIndex());
            dict.put("user-nickname", user.getNickname());
        } else dict.put("user-nickname", "anonymous");
        TimeZone timeZone = (TimeZone) MatrixContext.getThreadUserdata("TimeZone");
        if (timeZone == null) timeZone = TimeZone.getDefault();
        dict.put("timezone", timeZone.getID());
        dict.put("session", request.getSession().getId());
        return dict;
    }
}
