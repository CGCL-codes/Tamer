package org.jpublish.example;

import org.jpublish.RequestContext;
import org.jpublish.action.Action;
import com.anthonyeden.lib.config.Configuration;

public class TestAction implements Action {

    public void execute(RequestContext context, Configuration configuration) {
        context.put("test", "This is a test.");
    }
}
