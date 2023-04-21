package com.peterhi.persist.tx;

import com.peterhi.beans.Role;
import com.peterhi.persist.beans.Classroom;
import com.peterhi.persist.beans.Member;
import com.peterhi.persist.beans.Root;
import java.util.Date;
import org.prevayler.TransactionWithQuery;

/**
 *
 * @author YUN TAO
 */
public class AddMemberTx implements TransactionWithQuery {

    private String classroomName;

    private String memberName;

    private Role role;

    public AddMemberTx(String classroomName, String memberName, Role role) {
        this.classroomName = classroomName;
        this.memberName = memberName;
        this.role = role;
    }

    public Object executeAndQuery(Object o, Date ts) throws Exception {
        Root root = (Root) o;
        Classroom classroom = root.getClassroom(classroomName);
        if (classroom == null) {
            return null;
        }
        Member member = new Member();
        member.setMemberName(memberName);
        member.setRole(role);
        return classroom.addMember(member);
    }
}
