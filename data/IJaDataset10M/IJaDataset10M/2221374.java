package org.warko.dapp;

import org.warko.app.Application;
import org.warko.app.ApplicationAction;
import org.warko.app.ApplicationActionParser;

public class GenericActionParser implements ApplicationActionParser {

    private Application app;

    private ApplicationAction action;

    public GenericActionParser(Application app, ApplicationAction action) {
        this.app = app;
        this.action = action;
    }

    public ApplicationAction getAction() {
        return this.action;
    }

    public String[] getParenthesisParameters() {
        String[] params = null;
        String a = action.getName();
        int b = a.indexOf('(');
        if (b >= 0) {
            int e = a.indexOf(')', b + 1);
            if (e > 0) {
                String paramStr = a.substring(b + 1, e);
                if (paramStr.indexOf(',') >= 0) {
                    params = paramStr.split(",");
                } else {
                    params = new String[] { paramStr };
                }
            }
        }
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                params[i] = params[i].replaceAll("'", "");
            }
        }
        return params;
    }

    public Application getApplication() {
        return this.app;
    }

    public String getShortActionName() {
        String a = action.getName();
        int p = -1;
        if ((p = a.indexOf('(')) >= 0) {
            a = a.substring(0, p);
        }
        return a;
    }
}
