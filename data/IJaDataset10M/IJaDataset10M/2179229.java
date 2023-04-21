package aya.interfejs.session;

import java.io.Serializable;
import javax.persistence.EntityManager;
import aya.interfejs.config.EMF;
import aya.interfejs.entity.Member;

public class IdentityBean implements Serializable {

    private static final long serialVersionUID = 6653979533557327223L;

    private Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
