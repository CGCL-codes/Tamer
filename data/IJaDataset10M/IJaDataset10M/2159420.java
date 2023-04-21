package se.ucr.openqregdemo.tag;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.TreeMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import se.minicom.util.ClassPopulater;
import se.ucr.db.SQLErrorCodes;
import se.ucr.openqregdemo.bean.EvregistrationBean;
import se.ucr.openqregdemo.bean.EvregistrationFinder;
import se.ucr.openqregdemo.bean.FollowupBean;
import se.ucr.openqregdemo.bean.MceBean;
import se.ucr.openqregdemo.bean.MceKey;
import se.ucr.openqregdemo.bean.PatientBean;
import se.ucr.openqregdemo.bean.PatientKey;
import se.ucr.openqregdemo.bean.RegistrationBean;
import se.ucr.openqregdemo.bean.RegistrationKey;
import se.ucr.openqregdemo.bean.base.MceFinderBase;
import se.ucr.openqregdemo.bean.base.PatientFinderBase;
import se.ucr.openqregdemo.bean.base.RegistrationFinderBase;
import se.ucr.tag.AbstractStepHandlerTag;
import se.ucr.util.Converter;
import se.ucr.util.Step;

/**
 * @author OlovF
 * @responsible Olov
 */
public class StepHandlerRegistration extends AbstractStepHandlerTag {

    /**
	 * log is a Log4J Logger
	 */
    private static Logger log = Logger.getLogger(StepHandlerRegistration.class);

    Long MCEID = null;

    RegistrationBean regBean = null;

    Collection<EvregistrationBean> evColl = null;

    MceBean mceBean = null;

    PatientBean patBean = null;

    FollowupBean folBean = null;

    Calendar controlDate = null;

    @Override
    protected Logger getLog() {
        return log;
    }

    @Override
    protected void populateKeysAndBeans(Connection con) {
        MCEID = fetch.getValueAsLong("MCEID");
        try {
            mceBean = (MceBean) MceFinderBase.findByPrimaryKey(con, new MceKey(MCEID));
            regBean = (RegistrationBean) RegistrationFinderBase.findByPrimaryKey(con, new RegistrationKey(MCEID));
            evColl = EvregistrationFinder.findByMceid(con, mceBean.getMceid());
            patBean = (PatientBean) PatientFinderBase.findByPrimaryKey(con, new PatientKey(mceBean.getPid()));
        } catch (Exception e) {
            log.log(Level.ERROR, "populateKeysAndBeans() SQLException:", e);
        }
    }

    @Override
    protected void fromOtherPage() {
        fetch.removeValue(Step.STEP);
        fetch.addValue(Step.READ_DATA, Step.STEP);
        readData();
    }

    @Override
    protected String getCheckVariableName() {
        return "REGISTRATION_DTMIN";
    }

    @Override
    protected void logToPatientLog() {
        patientLog.log(Level.INFO, user.getCentreid() + ";" + user.getId() + ";" + "StepHandlerRegistration" + ";" + fetch.getValueAsString("STEP") + ";" + patBean.getSsn() + ";" + mceBean.getPid() + ";" + regBean.getMceid() + ";");
    }

    @Override
    protected void populateWithRequestData() {
        fetch.removeValue("REGISTRATION_MCEID");
        fetch.removeValue("REGISTRATION_CENTREID");
        fetch.removeValue("REGISTRATION_TSCREATED");
        fetch.removeValue("REGISTRATION_CREATEDBY");
        fetch.removeValue("PATIENT_SSN");
        if ("1".equals(fetch.getValueAsString("REGISTRATION_STATUS")) && null != regBean.getAdmissiondate() && !regBean.getAdmissiondate().toString().equals(fetch.getValueAsString("REGISTRATION_ADMISSIONDATE"))) {
            fetch.removeValue("REGISTRATION_STATUS");
            message = lang.getTranslation(langId, "REGISTRATION_NOSTATUSWHENCONTROLLINGDATE_A");
        }
        ClassPopulater.populateFromMap(fetch.getTreeMap(), "REGISTRATION_", regBean);
        ClassPopulater.populateFromMap(fetch.getTreeMap(), "PATIENT_", patBean);
        regBean.setTsupdated(new Timestamp(new GregorianCalendar().getTimeInMillis()));
        regBean.setUpdatedby(user.getId());
        patBean.setTsupdated(new Timestamp(new GregorianCalendar().getTimeInMillis()));
        patBean.setUpdatedby(user.getId());
    }

    @Override
    protected void updateData(Connection con) throws SQLException {
        if (disp.checkAccess(user.getId(), "REGISTRATION_SAVE") && user.getCentreid().equals(regBean.getCentreid()) && !regBean.getStatus().equals(new Integer(1))) {
            populateWithRequestData();
            regBean.store(con);
            patBean.store(con);
            for (EvregistrationBean evRegBean : evColl) {
                evRegBean.remove(con);
            }
            evColl = Collections.synchronizedList(new ArrayList<EvregistrationBean>());
            TreeMap<String, Object> trExtra = fetch.createBeginsWithTree("EXTRAVAR_");
            EvregistrationBean evRegBean = null;
            for (String key : trExtra.keySet()) {
                if (!"".equals(fetch.getValueAsString(key))) {
                    evRegBean = new EvregistrationBean();
                    evRegBean.setMceid(MCEID);
                    evRegBean.setVar(key);
                    evRegBean.setVal(fetch.getValueAsString(key));
                    evRegBean.create(con);
                    evColl.add(evRegBean);
                }
            }
            fetch.removeValue(Step.STEP);
            fetch.addValue(Step.READ_DATA, Step.STEP);
        } else {
            abuseLog.log(Level.INFO, "doStartTag()SAVE_DATA no update right MCEID:" + MCEID + " userId:" + user.getId());
            message = lang.getTranslation(langId, "ABUSE_WARNING_A");
        }
        readData(con);
    }

    @Override
    protected void unlockData(Connection con) throws SQLException {
        if (user.getCentreid().equals(regBean.getCentreid()) && regBean.getStatus().equals(new Integer(1)) && disp.checkAccess(user.getId(), "REGISTRATION_UNLOCK")) {
            regBean.setTsupdated(new Timestamp(new GregorianCalendar().getTimeInMillis()));
            regBean.setUpdatedby(user.getId());
            regBean.setStatus(new Integer(0));
            regBean.store(con);
            fetch.removeValue(Step.STEP);
            fetch.addValue(Step.READ_DATA, Step.STEP);
        } else {
            abuseLog.log(Level.INFO, "doStartTag() UNLOCK_DATA unlock not allowed:" + MCEID + " userId:" + user.getId());
            message = lang.getTranslation(langId, "ABUSE_WARNING_A");
        }
        readData(con);
    }

    @Override
    protected void deleteData(Connection con) throws SQLException {
        if (user.getCentreid().equals(regBean.getCentreid()) && !regBean.getStatus().equals(new Integer(1)) && disp.checkAccess(user.getId(), "REGISTRATION_DELETE")) {
            for (EvregistrationBean evRegBean : evColl) {
                evRegBean.remove(con);
            }
            regBean.remove(con);
            mceBean.remove(con);
            try {
                patBean.remove(con);
            } catch (SQLException sqle) {
                if (sqle.getErrorCode() != SQLErrorCodes.FOREIGN_KEY_CONSTRAINTS_FAILS) {
                    log.log(Level.ERROR, "doStartTag() can't remove patient:", sqle);
                    throw sqle;
                }
            }
            forward("start.jsp");
        } else {
            abuseLog.log(Level.INFO, "doStartTag()DELETE_DATA unlock not allowed:" + MCEID + " userId:" + user.getId());
            message = lang.getTranslation(langId, "ABUSE_WARNING_A");
        }
    }

    @Override
    protected void readData(Connection con) throws SQLException {
        if ((user.getCentreid().equals(regBean.getCentreid()) || (null != folBean && user.getCentreid().equals(folBean.getCentreid())))) {
            fetch.addTreeMap(patBean.getTreeMap());
            fetch.addMapReplaceExistingValues(regBean.getTreeMap());
            fetch.addValue(MCEID.toString(), "MCEID");
            lastSavedBy = regBean.getUpdatedby();
            if (null == lastSavedBy) {
                lastSavedBy = regBean.getCreatedby();
            }
            lastSavedTS = regBean.getTsupdated().toString();
            if (null == lastSavedTS) {
                lastSavedTS = regBean.getTscreated().toString();
            }
            lastSavedTS = lastSavedTS.substring(0, 16);
            if (evColl != null) {
                for (EvregistrationBean evRegistration : evColl) {
                    fetch.addValue(evRegistration.getVal(), evRegistration.getVar());
                }
            }
        } else {
            abuseLog.log(Level.INFO, "doStartTag()DELETE_DATA no readright:" + " MCEID:" + MCEID + " userId:" + user.getId());
            message = lang.getTranslation(langId, "ABUSE_WARNING_A");
        }
    }

    @Override
    protected void returnAttributesToPage() {
        super.returnAttributesToPage();
        pageContext.setAttribute("patBean", patBean);
        pageContext.setAttribute("mceBean", mceBean);
        pageContext.setAttribute("regBean", regBean);
        pageContext.setAttribute("folBean", folBean);
        if (null != regBean && null != regBean.getAdmissiondate()) {
            controlDate = Converter.sqlDateToGC(regBean.getAdmissiondate());
        } else {
            controlDate = new GregorianCalendar();
        }
        pageContext.setAttribute("controlDate", controlDate);
    }

    @Override
    protected void createData(Connection con) throws SQLException {
    }
}
