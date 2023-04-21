package com.peterhi.servlet.persist.tx;

import java.util.Date;
import org.prevayler.TransactionWithQuery;
import com.peterhi.servlet.persist.beans.Classroom;
import com.peterhi.servlet.persist.beans.World;

public class RemoveMemberTx implements TransactionWithQuery {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2031687058078304400L;

    private String classroom;

    private String member;

    public RemoveMemberTx(String classroomBeanName, String memberBeanName) {
        this.classroom = classroomBeanName;
        this.member = memberBeanName;
    }

    @Override
    public Object executeAndQuery(Object object, Date timestamp) throws Exception {
        World world = (World) object;
        Classroom room = world.getClassroom(classroom);
        if (room == null) {
            return false;
        }
        return room.removeMember(member);
    }
}
