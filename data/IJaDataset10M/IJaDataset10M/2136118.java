package com.peterhi.servlet.persist.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ClassroomBean implements Serializable, Cloneable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2711477967478584736L;

    private String name;

    private Set memberBeans = new HashSet();

    public ClassroomBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean addMemberBean(String name, RoleBean role) {
        System.out.println("ADDING");
        boolean result = memberBeans.add(new MemberBean(name, role));
        System.out.println("ADDED, NOW: " + memberBeans.size());
        return result;
    }

    public boolean removeMemberBean(MemberBean memberBean) {
        return memberBeans.remove(memberBean);
    }

    public boolean removeMemberBean(String name) {
        return memberBeans.remove(new MemberBean(name));
    }

    public synchronized MemberBean getMemberBean(String name) {
        MemberBean temp = new MemberBean(name);
        for (Iterator itor = memberBeans.iterator(); itor.hasNext(); ) {
            Object next = itor.next();
            if (next.equals(temp)) {
                return (MemberBean) temp;
            }
        }
        return null;
    }

    public List getMemberBeans() {
        Object[] array = memberBeans.toArray();
        return Arrays.asList(array);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ClassroomBean other = (ClassroomBean) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
