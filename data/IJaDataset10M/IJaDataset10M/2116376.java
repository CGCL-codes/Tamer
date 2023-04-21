package com.peterhi.servlet.persist.tx;

import java.util.Date;
import org.prevayler.TransactionWithQuery;
import com.peterhi.servlet.persist.beans.Classroom;
import com.peterhi.servlet.persist.beans.World;

public class RemoveElementTx implements TransactionWithQuery {

    private static final long serialVersionUID = -4035886373827119992L;

    private String classroom;

    private String name;

    public RemoveElementTx(String classroom, String name) {
        this.classroom = classroom;
        this.name = name;
    }

    public Object executeAndQuery(Object object, Date timestamp) throws Exception {
        World world = (World) object;
        Classroom cls = world.getClassroom(classroom);
        if (cls == null) return null;
        return cls.removeElement(name);
    }
}
