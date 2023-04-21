package com.medics.action;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import com.medics.dao.AuthoritiesDao;
import com.medics.dao.AuthoritiesDaoImpl;
import com.medics.dao.MedicHistoryDao;
import com.medics.dao.MedicineReceivedDao;
import com.medics.dao.PatientDao;
import com.medics.dao.TreatmentReceivedDao;
import com.medics.dao.UsersDao;
import com.medics.dao.UsersDaoImpl;
import com.medics.enterprise.EmailRegisterNotify;
import com.medics.entities.Authorities;
import com.medics.entities.Patient;
import com.medics.entities.TreatmentReceived;
import com.medics.entities.Users;
import com.medics.utils.SimpleMD5;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class Register extends ActionSupport {

    public Users user;

    public Authorities authority;

    public Patient patient;

    public Patient patient1;

    public PatientDao patientDao;

    public UsersDaoImpl userDao;

    public AuthoritiesDao authoritiesDao;

    public MedicHistoryDao medicHistoryDao;

    public MedicineReceivedDao medicineReceivedDao;

    public TreatmentReceivedDao treatmentReceivedDao;

    Map session;

    public String username;

    private InputStream inputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public String show() {
        return "SHOW";
    }

    public String usernameExists() {
        String exists = "false";
        if (userDao.findByUsername(username) != null) exists = "true";
        inputStream = new StringBufferInputStream(exists);
        return "EXISTS";
    }

    public String register() {
        authority = new Authorities();
        authority.setUser(user);
        authority.setAuthority("ROLE_PATIENT");
        user.setPassword(SimpleMD5.MD5(user.password));
        user.setEnabled(1);
        String filePath = ServletActionContext.getServletContext().getRealPath("/");
        user.setPhotoName(filePath + "images\\default.png");
        patient.setUser(user);
        userDao.save(user);
        authoritiesDao.save(authority);
        patientDao.save(patient);
        session = ActionContext.getContext().getSession();
        session.put("username", user.username);
        session.put("patientid", patient.id);
        return "SUCCESS";
    }

    public String showPatientDetails() {
        return "PATIENTDETAILS";
    }

    public String patientDetails() {
        session = ActionContext.getContext().getSession();
        Integer patientid = (Integer) session.get("patientid");
        patient = patientDao.findById(patientid);
        patient.medHistory = patient1.medHistory;
        List<TreatmentReceived> treatmentReceived = patient.medHistory.treatmentReceived;
        treatmentReceivedDao.save(treatmentReceived.get(0));
        medicHistoryDao.save(patient.medHistory);
        patientDao.save(patient);
        return "PATIENTHOME";
    }

    public boolean isCaptchaValid() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String remoteAddr = request.getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey("6Leia70SAAAAABpUHTUto3OUuX5ckVkZnaupmgVC ");
        String challenge = request.getParameter("recaptcha_challenge_field");
        String uresponse = request.getParameter("recaptcha_response_field");
        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
        return reCaptchaResponse.isValid();
    }

    public String toProfile() {
        return "PROFILE";
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Authorities getAuthority() {
        return authority;
    }

    public void setAuthority(Authorities authority) {
        this.authority = authority;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PatientDao getPatientDao() {
        return patientDao;
    }

    public void setPatientDao(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    public UsersDaoImpl getUserDao() {
        return userDao;
    }

    public void setUserDao(UsersDaoImpl userDao) {
        this.userDao = userDao;
    }

    public AuthoritiesDao getAuthoritiesDao() {
        return authoritiesDao;
    }

    public void setAuthoritiesDao(AuthoritiesDao authoritiesDao) {
        this.authoritiesDao = authoritiesDao;
    }

    public Patient getPatient1() {
        return patient1;
    }

    public void setPatient1(Patient patient1) {
        this.patient1 = patient1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MedicHistoryDao getMedicHistoryDao() {
        return medicHistoryDao;
    }

    public void setMedicHistoryDao(MedicHistoryDao medicHistoryDao) {
        this.medicHistoryDao = medicHistoryDao;
    }

    public MedicineReceivedDao getMedicineReceivedDao() {
        return medicineReceivedDao;
    }

    public void setMedicineReceivedDao(MedicineReceivedDao medicineReceivedDao) {
        this.medicineReceivedDao = medicineReceivedDao;
    }

    public TreatmentReceivedDao getTreatmentReceivedDao() {
        return treatmentReceivedDao;
    }

    public void setTreatmentReceivedDao(TreatmentReceivedDao treatmentReceivedDao) {
        this.treatmentReceivedDao = treatmentReceivedDao;
    }
}
