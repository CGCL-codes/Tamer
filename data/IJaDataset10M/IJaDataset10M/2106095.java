package com.shine.framework.DBUtil.threadModel;

import java.util.ArrayList;
import java.util.List;
import com.shine.framework.DBUtil.DBUtil;
import com.shine.framework.ThreadPoolUtil.model.ThreadModel;

public class UpdateThreadModel extends ThreadModel {

    public UpdateThreadModel() {
        this.setType("dbUpdate");
        this.setTimeOut(1000);
    }

    @Override
    public void excute(Object... args) {
        try {
            if (args.length != 0) {
                String jndi = (String) args[0];
                ArrayList<String> list = (ArrayList<String>) args[1];
                if (list.size() != 0) {
                    DBUtil.getInstance().executeBatchUpdate(jndi, list);
                    list.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
