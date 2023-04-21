package org.zxframework.misc;

import org.zxframework.ZXBO;
import org.zxframework.ZXException;
import org.zxframework.zXType;
import org.zxframework.datasources.DSHandler;
import org.zxframework.datasources.DSRS;
import org.zxframework.util.StringUtil;
import org.zxframework.logging.Log;
import org.zxframework.logging.LogFactory;

/**
 * Used for zX Text Messages.
 * 
 * <pre>
 * 
 * Change    : DGS07MAR2005 - V1.4:56
 * Why       : In handleNew, when mandatory text message attributes not given, the error gets
 *              shown twice.
 *              
 * Change    : BD5APR05 - V1:5.1
 * Why       : Added support for data-sources
 * 
 * </pre>
 * 
 * @author Michael Brewer
 * @author Bertus Dispa
 * @author David Swann
 * 
 * @version 0.0.1
 */
public class Text extends ZXBO {

    private static Log log = LogFactory.getLog(Text.class);

    /**
     * Default constructor.
     */
    public Text() {
        super();
    }

    /**
     * Count the number of unread messages available for this user.
     *
     * @return Returns the number of unread messages for this user. 
     * @throws ZXException Thrown if countUnread fails. 
     */
    public int countUnread() throws ZXException {
        return countUnread(null);
    }

    /**
     * Count the number of unread messages available for this user.
     *
     * @param pstrUser The user id for whom the messages belong. Optional. 
     * @return Returns the number of unread messages for this user. 
     * @throws ZXException Thrown if countUnread fails. 
     */
    public int countUnread(String pstrUser) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrUser", pstrUser);
        }
        int countUnread = 0;
        try {
            if (StringUtil.len(pstrUser) == 0) {
                pstrUser = getZx().getUserProfile().getValue("id").getStringValue();
            }
            setValue("rcvr", pstrUser);
            countUnread = countByGroup("rcvr,rdWhn");
            if (countUnread < -1) {
                throw new Exception("Unable to count instances of zXTxt");
            }
            return countUnread;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Count the number of unread messages available for this user.", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : pstrUser = " + pstrUser);
            }
            if (getZx().throwException) throw new ZXException(e);
            return countUnread;
        } finally {
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(countUnread);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Handle new text message. 
     * 
     * <pre>
     * 
     * This uses a dummy business object that includes attributes for
     * both user and group. We have to create a true zXTxt object and then call the 'send'
     * method to perform the insert(s).
     * 
     * Assumes   :
     *    Required data of me set or loaded
     *    DB transaction is handled outside of here
     * </pre>
     *
     * @return  Returns the return code of the method. @see zXType#rc 
     * @throws ZXException  Thrown if handleNew fails. 
     */
    private zXType.rc handleNew() throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
        }
        zXType.rc handleNew = zXType.rc.rcOK;
        try {
            Text objTxt = (Text) getZx().createBO("zXTxt");
            if (objTxt == null) {
                throw new Exception("Failed to created the zXTxt object.");
            }
            objTxt.resetBO("*");
            if (!getValue("tpe").isNull) {
                if (bo2bo(objTxt, "tpe").pos != zXType.rc.rcOK.pos) {
                    handleNew = zXType.rc.rcError;
                    return handleNew;
                }
            }
            if (!getValue("smmry").isNull) {
                if (bo2bo(objTxt, "smmry").pos != zXType.rc.rcOK.pos) {
                    handleNew = zXType.rc.rcError;
                    return handleNew;
                }
            }
            if (bo2bo(objTxt, "bdy").pos != zXType.rc.rcOK.pos) {
                handleNew = zXType.rc.rcError;
                return handleNew;
            }
            if (!getValue("rcvr").isNull) {
                if (bo2bo(objTxt, "rcvr").pos != zXType.rc.rcOK.pos) {
                    handleNew = zXType.rc.rcOK;
                    return handleNew;
                }
            }
            if ((getValue("usrGrp").isNull && getValue("rcvr").isNull) || getValue("tpe").isNull) {
                return handleNew;
            }
            handleNew = objTxt.send(getValue("usrGrp").getStringValue());
            return handleNew;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Handle new text message.", e);
            if (getZx().throwException) throw new ZXException(e);
            handleNew = zXType.rc.rcError;
            return handleNew;
        } finally {
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(handleNew);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Insert a new text message. 
     * 
     * <pre>
     * 
     * If the group is passed in, insert it for every member.
     * 
     * Assumes   :
     *    Required data of me set or loaded
     *    DB transaction is handled outside of here
     * 
     * Reviewed for V1.5:1
     * </pre>
     *
     * @param pstrGroup Optional. 
     * @return Returns the return code of the method. 
     * @throws ZXException Thrown if send fails. 
     */
    public zXType.rc send(String pstrGroup) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrGroup", pstrGroup);
        }
        zXType.rc send = zXType.rc.rcOK;
        DSRS objRS = null;
        DSHandler objDSHandler = null;
        try {
            if (StringUtil.len(pstrGroup) == 0) {
                setAutomatics("+");
                send = insertBO();
                return send;
            }
            ZXBO objIBOUsrGrpMmbr = getZx().createBO("zXUsrGrpMmbr");
            if (objIBOUsrGrpMmbr == null) {
                throw new Exception("Failed to create zXUsrGrpMmbr");
            }
            objDSHandler = getDS();
            objRS = objDSHandler.boRS(objIBOUsrGrpMmbr, "usrPrf", ":usrGrp='" + pstrGroup + "'");
            if (objRS == null) {
                throw new Exception("Unable to execute query to get user group members");
            }
            while (!objRS.eof()) {
                objRS.rs2obj(objIBOUsrGrpMmbr, "usrPrf");
                setAutomatics("+");
                setValue("rcvr", objIBOUsrGrpMmbr.getValue("usrPrf"));
                insertBO();
                objRS.moveNext();
            }
            return send;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Insert a new text message.", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : pstrGroup = " + pstrGroup);
            }
            if (getZx().throwException) throw new ZXException(e);
            send = zXType.rc.rcError;
            return send;
        } finally {
            if (objRS != null) objRS.RSClose();
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(send);
                getZx().trace.exitMethod();
            }
        }
    }

    /** 
     * Allow developer to validate / massage entity before something happens.
     * 
     * @see org.zxframework.ZXBO#postPersist(org.zxframework.zXType.persistAction, String, String, String)
     **/
    public zXType.rc postPersist(zXType.persistAction penmPersistAction, String pstrGroup, String pstrWhereGroup, String pstrId) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("penmPersistAction", penmPersistAction);
            getZx().trace.traceParam("pstrGroup", pstrGroup);
            getZx().trace.traceParam("pstrWhereGroup", pstrWhereGroup);
            getZx().trace.traceParam("pstrId", pstrId);
        }
        zXType.rc postPersist = zXType.rc.rcOK;
        if (penmPersistAction == null) {
            penmPersistAction = zXType.persistAction.paProcess;
        }
        try {
            postPersist = super.postPersist(penmPersistAction, pstrGroup, pstrWhereGroup, pstrId);
            if (postPersist.pos != zXType.rc.rcError.pos) {
                String strWhoIs = getDescriptor().getName().toLowerCase();
                if (strWhoIs.equals("zxtxtnew")) {
                    if (penmPersistAction.equals(zXType.persistAction.paProcessEditForm)) {
                        postPersist = handleNew();
                    }
                }
            }
            return postPersist;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Do post persist action ", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : penmPersistAction = " + penmPersistAction);
                log.error("Parameter : pstrGroup = " + pstrGroup);
                log.error("Parameter : pstrWhereGroup = " + pstrWhereGroup);
                log.error("Parameter : pstrId = " + pstrId);
            }
            if (getZx().throwException) throw new ZXException(e);
            return postPersist;
        } finally {
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.exitMethod();
            }
        }
    }
}
