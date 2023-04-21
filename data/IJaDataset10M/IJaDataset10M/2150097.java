package hambo.mywappage;

import java.util.*;

public class CvPageDO {

    String title = null;

    String firstname = null;

    String lastname = null;

    String phone = null;

    String email = null;

    String homepage = null;

    String carrier = null;

    String firmname1 = null;

    String jobyears1 = null;

    String firmname2 = null;

    String jobyears2 = null;

    String firmname3 = null;

    String jobyears3 = null;

    String schoolname1 = null;

    String schoolyears1 = null;

    String degree1 = null;

    String schoolname2 = null;

    String schoolyears2 = null;

    String degree2 = null;

    String schoolname3 = null;

    String schoolyears3 = null;

    String degree3 = null;

    public CvPageDO(Hashtable tab) {
        this.title = (String) tab.get("title");
        this.firstname = (String) tab.get("firstname");
        this.lastname = (String) tab.get("lastname");
        this.phone = (String) tab.get("phone");
        this.email = (String) tab.get("email");
        this.homepage = (String) tab.get("homepage");
        this.carrier = (String) tab.get("carrier");
        this.firmname1 = (String) tab.get("firmname1");
        this.jobyears1 = (String) tab.get("jobyears1");
        this.firmname2 = (String) tab.get("firmname2");
        this.jobyears2 = (String) tab.get("jobyears2");
        this.firmname3 = (String) tab.get("firmname3");
        this.jobyears3 = (String) tab.get("jobyears3");
        this.schoolname1 = (String) tab.get("schoolname1");
        this.schoolyears1 = (String) tab.get("schoolyears1");
        this.degree1 = (String) tab.get("degree1");
        this.schoolname2 = (String) tab.get("schoolname2");
        this.schoolyears2 = (String) tab.get("schoolyears2");
        this.degree2 = (String) tab.get("degree2");
        this.schoolname3 = (String) tab.get("schoolname3");
        this.schoolyears3 = (String) tab.get("schoolyears3");
        this.degree3 = (String) tab.get("degree3");
    }

    public CvPageDO() {
    }

    public String gettitle() {
        return title;
    }

    public String getfirstname() {
        return firstname;
    }

    public String getlastname() {
        return lastname;
    }

    public String getphone() {
        return phone;
    }

    public String getemail() {
        return email;
    }

    public String gethomepage() {
        return homepage;
    }

    public String getcarrier() {
        return carrier;
    }

    public String getfirmname1() {
        return firmname1;
    }

    public String getjobyears1() {
        return jobyears1;
    }

    public String getfirmname2() {
        return firmname2;
    }

    public String getjobyears2() {
        return jobyears2;
    }

    public String getfirmname3() {
        return firmname3;
    }

    public String getjobyears3() {
        return jobyears3;
    }

    public String getschoolname1() {
        return schoolname1;
    }

    public String getschoolyears1() {
        return schoolyears1;
    }

    public String getdegree1() {
        return degree1;
    }

    public String getschoolname2() {
        return schoolname2;
    }

    public String getschoolyears2() {
        return schoolyears2;
    }

    public String getdegree2() {
        return degree2;
    }

    public String getschoolname3() {
        return schoolname3;
    }

    public String getschoolyears3() {
        return schoolyears3;
    }

    public String getdegree3() {
        return degree3;
    }

    public void settitle(String in) {
        title = in;
    }

    public void setfirstname(String in) {
        firstname = in;
    }

    public void setlastname(String in) {
        lastname = in;
    }

    public void setphone(String in) {
        phone = in;
    }

    public void setemail(String in) {
        email = in;
    }

    public void sethomepage(String in) {
        homepage = in;
    }

    public void setcarrier(String in) {
        carrier = in;
    }

    public void setfirmname1(String in) {
        firmname1 = in;
    }

    public void setjobyears1(String in) {
        jobyears1 = in;
    }

    public void setfirmname2(String in) {
        firmname2 = in;
    }

    public void setjobyears2(String in) {
        jobyears2 = in;
    }

    public void setfirmname3(String in) {
        firmname3 = in;
    }

    public void setjobyears3(String in) {
        jobyears3 = in;
    }

    public void setschoolname1(String in) {
        schoolname1 = in;
    }

    public void setschoolyears1(String in) {
        schoolyears1 = in;
    }

    public void setdegree1(String in) {
        degree1 = in;
    }

    public void setschoolname2(String in) {
        schoolname2 = in;
    }

    public void setschoolyears2(String in) {
        schoolyears2 = in;
    }

    public void setdegree2(String in) {
        degree2 = in;
    }

    public void setschoolname3(String in) {
        schoolname3 = in;
    }

    public void setschoolyears3(String in) {
        schoolyears3 = in;
    }

    public void setdegree3(String in) {
        degree3 = in;
    }
}
