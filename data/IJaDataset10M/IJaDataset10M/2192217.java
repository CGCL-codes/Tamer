package com.peterhi.servlet.persist;

import java.util.Date;
import org.prevayler.TransactionWithQuery;

public class AddClassroomTx implements TransactionWithQuery {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2533476300909604429L;

    private String name;

    public AddClassroomTx(String name) {
        this.name = name;
    }

    public Object executeAndQuery(Object object, Date timestamp) throws Exception {
        World world = (World) object;
        return world.addClassroom(name);
    }
}
