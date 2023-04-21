package org.domain.analyticalcrm.session;

import org.domain.analyticalcrm.entity.Membershipuser;
import org.domain.analyticalcrm.exceptions.SecurityException;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.jboss.seam.international.StatusMessages;

/**
 * 
 * @author Daniel Mazur i Marcin Szyd�owski grupa 432 IDM
 */
@Name("register")
public class Register {

    @Logger
    private Log log;

    @In
    StatusMessages statusMessages;

    private String user;

    private String password;

    private String confpassword;

    private String email;

    private String question;

    private String answer;

    private MembershipProvider provider = null;

    private boolean RegistredIn = false;

    public boolean createuser() {
        log.info("Tworzenie uzytkownika {0}", this.getUser());
        setRegistredIn(false);
        if (getPassword().equals(getConfpassword())) {
            String hashed = Authenticator.hashCode(getPassword());
            Membershipuser user = new Membershipuser();
            user.setLogin(getUser());
            user.setPassword(hashed);
            user.setEmail(getEmail());
            user.setQuestion(getQuestion());
            user.setAnswer(getAnswer());
            try {
                provider = new MembershipProvider();
                provider.addUser(user, "user");
                this.clearFields();
                statusMessages.add("Uzytkownik utworzony");
                setRegistredIn(true);
                return true;
            } catch (SecurityException ex) {
                log.info("EXCEPTION {0}", ex.getMessage());
            } finally {
                try {
                    provider.close();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        } else {
            statusMessages.add("Nie prawidlowy login lub haslo!");
        }
        return false;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setConfpassword(String confpassword) {
        this.confpassword = confpassword;
    }

    public String getConfpassword() {
        return confpassword;
    }

    private void clearFields() {
        this.user = "";
        this.password = "";
        this.confpassword = "";
        this.email = "";
        this.question = "";
        this.answer = "";
    }

    public void setRegistredIn(boolean registredIn) {
        RegistredIn = registredIn;
    }

    public boolean isRegistredIn() {
        return RegistredIn;
    }
}
