package org.sepp.datatypes;

import java.util.ArrayList;
import java.util.List;

public class Members {

    private ArrayList members = new ArrayList();

    public void addMember(String member) {
        if (!members.contains(member)) members.add(member);
    }

    public void setMembers(List members) {
        this.members = (ArrayList) members;
    }

    public ArrayList getMembers() {
        return members;
    }

    public String getMember(int index) {
        return (String) members.get(index);
    }

    public void removeMember(String member) {
        members.remove(member);
    }

    public void removeMember(int index) {
        members.remove(index);
    }

    public int size() {
        return members.size();
    }

    public boolean containsMember(String peerId) {
        return members.contains(peerId);
    }

    public void clear() {
        members.clear();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < members.size(); index++) {
            buffer.append("Member " + index + " : " + members.get(index));
        }
        return buffer.toString();
    }
}
