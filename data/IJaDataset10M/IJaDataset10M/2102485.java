package jvc.web.action.utils;

import jvc.util.db.MyDB;
import jvc.web.action.*;

public class PoolClearAction implements BaseAction {

    public String Excute(ActionContent input, ActionContent output, MyDB mydb) {
        jvc.util.cache.ObjectPool.clear();
        return input.getParam("success");
    }
}
