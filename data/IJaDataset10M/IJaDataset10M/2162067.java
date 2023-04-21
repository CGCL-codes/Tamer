package com.peterhi.server.commands;

import java.util.Date;
import org.prevayler.TransactionWithQuery;
import com.peterhi.persist.beans.Root;
import com.peterhi.persist.beans.Classroom;
import com.peterhi.persist.beans.Member;

class RemoveMember implements TransactionWithQuery {

    private String channel;

    private String email;

    public RemoveMember(String channel, String email) {
        this.channel = channel;
        this.email = email;
    }

    public Object executeAndQuery(Object o, Date ts) throws Exception {
        Root root = (Root) o;
        Classroom classroom = root.getClassroom(channel);
        if (classroom == null) {
            return false;
        }
        Member member = classroom.getMember(email);
        if (member == null) {
            return false;
        }
        return classroom.members.remove(member);
    }
}
