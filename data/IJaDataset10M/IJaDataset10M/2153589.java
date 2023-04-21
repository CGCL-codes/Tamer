package managedbeans;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import entitybeans.Lists;
import entitybeans.Users;
import java.io.IOException;
import util.*;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Date;

public class RegisterActionMB {

    private String email;

    private String password;

    private String password2;

    private String nickName;

    private String birthDate;

    private String address;

    private String aboutMe;

    private Blob picture;

    private int uploadsAvailable = 1;

    private boolean autoUpload = false;

    private boolean useFlash = false;

    public RegisterActionMB() {
    }

    public String addMember() {
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            if (getEmail() == null) {
                Contexts.getSession().setAttribute("errMsg", "email is mandatory. please enter one");
                return "fail";
            }
            if ((getEmail() != null)) {
                String regex = "(\\w+)(\\.\\w+)*@(\\w+\\.)(\\w+)(\\.\\w+)*";
                if (!(getEmail().matches(regex))) {
                    Contexts.getSession().setAttribute("errMsg", "email is not valid. please try again");
                    return "fail";
                }
            }
            if (getPassword() == null) {
                Contexts.getSession().setAttribute("errMsg", "password is mandatory. please enter both fields");
                return "fail";
            }
            if (getPassword2() == null) {
                Contexts.getSession().setAttribute("errMsg", "please re-enter your password for validation");
                return "fail";
            }
            if (getNickName() == null) {
                Contexts.getSession().setAttribute("errMsg", "nick name is mandatory. please enter one");
                return "fail";
            }
            if (getPassword().length() < 5) {
                Contexts.getSession().setAttribute("errMsg", "password must be at least 5 chars long. please enter one");
                return "fail";
            }
            if (!(getPassword().equals(getPassword2()))) {
                Contexts.getSession().setAttribute("errMsg", "Two passwrods don't match. Please try again.");
                return "fail";
            }
            Query query = session.getNamedQuery("Users.checkEmail");
            query.setParameter("email", getEmail());
            List<Lists> results = query.list();
            System.out.println("results.size: " + results.size());
            if (results.size() != 0) {
                Contexts.getSession().setAttribute("errMsg", "The email you chose already exists. Please try again.");
                return "fail";
            }
            query = session.getNamedQuery("Users.checkNick");
            query.setParameter("nickName", getNickName());
            results = query.list();
            System.out.println("results.size: " + results.size());
            if (results.size() != 0) {
                Contexts.getSession().setAttribute("errMsg", "The nick name you chose already exists. Please try again.");
                return "fail";
            }
            Users newUser = new Users();
            newUser.setEmail(getEmail());
            newUser.setPassword(getPassword());
            newUser.setNickName(getNickName());
            if (getBirthDate().length() != 0) {
                try {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    Date d = df.parse(getBirthDate());
                    newUser.setBirthDate(d);
                } catch (ParseException e) {
                    Contexts.getSession().setAttribute("errMsg", "Date is not in correct format (dd/mm/yyyy). Please try again.");
                    return "fail";
                }
            }
            if (getAddress() != null) {
                newUser.setAddress(getAddress());
            }
            if (getAboutMe() != null) {
                newUser.setAboutMe(getAboutMe());
            }
            if (Contexts.getSession().getAttribute("pic") != null) {
                try {
                    newUser.setPicture((Blob) Contexts.getSession().getAttribute("pic"));
                    Contexts.getSession().removeAttribute("pic");
                } catch (Exception e) {
                    Contexts.getSession().setAttribute("errMsg", "Having problems uploading this pic.. Please try again.");
                }
            }
            newUser.setRole("regular");
            newUser.setCancelled(false);
            Contexts.getSession().setAttribute("user", newUser);
            session.save(newUser);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        } finally {
            session.flush();
            session.close();
        }
        Contexts.getSession().setAttribute("errMsg", "");
        return "success";
    }

    public void listener(UploadEvent event) throws Exception {
        UploadItem item = event.getUploadItem();
        setUploadsAvailable(getUploadsAvailable() - 1);
        picture = Hibernate.createBlob(item.getData());
        Contexts.getSession().setAttribute("pic", picture);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthDate() {
        return (birthDate);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPicture(Blob picture) {
        this.picture = picture;
    }

    public Blob getPicture() {
        return picture;
    }

    public void setUploadsAvailable(int uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }

    public int getUploadsAvailable() {
        return uploadsAvailable;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
    }

    public boolean isUseFlash() {
        return useFlash;
    }
}
