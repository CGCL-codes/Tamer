package gu.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Mrkr4 {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Date date;

    @Persistent
    private String s1;

    @Persistent
    private String s2;

    @Persistent
    private String s3;

    @Persistent
    private String s4;

    @Persistent
    private String s5;

    @Persistent
    private String s6;

    public Mrkr4(Date date, String s1, String s2, String s3, String s4, String s5, String s6) {
        this.date = date;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
        this.s5 = s5;
        this.s6 = s6;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String get_s1() {
        return s1;
    }

    public String get_s2() {
        return s2;
    }

    public String get_s3() {
        return s3;
    }

    public String get_s4() {
        return s4;
    }

    public String get_s5() {
        return s5;
    }

    public String get_s6() {
        return s6;
    }

    public void set_s1(String s) {
        this.s1 = s;
    }

    public void set_s2(String s) {
        this.s2 = s;
    }

    public void set_s3(String s) {
        this.s3 = s;
    }

    public void set_s4(String s) {
        this.s4 = s;
    }

    public void set_s5(String s) {
        this.s5 = s;
    }

    public void set_s6(String s) {
        this.s6 = s;
    }
}
