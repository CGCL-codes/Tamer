package project.cn.dataType;

public class RegisterData extends Data {

    private static final long serialVersionUID = 7370529053647806163L;

    private String Name = "";

    private String PW1 = "";

    private String PW2 = "";

    private String EmailAddress = "";

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPW1() {
        return PW1;
    }

    public void setPW1(String pW1) {
        this.PW1 = pW1;
    }

    public String getPW2() {
        return PW2;
    }

    public void setPW2(String pW2) {
        this.PW2 = pW2;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.EmailAddress = emailAddress;
    }
}
