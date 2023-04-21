package com.peterhi.server.command;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import com.peterhi.persist.bean.Root;
import com.peterhi.persist.bean.Classroom;
import org.prevayler.Query;

class ListClassroom implements Query {

    public Object query(Object o, Date ts) throws Exception {
        Root root = (Root) o;
        if (root.classrooms.size() > 0) {
            List<String> list = new ArrayList<String>();
            for (Classroom item : root.classrooms) {
                list.add(item.channel);
            }
            return list;
        } else {
            return null;
        }
    }
}
