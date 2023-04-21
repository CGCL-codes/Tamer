package eu.mpower.framework.management.usermedicaldata.soap;

import eu.mpower.framework.security.authorization.soap.AuthorizationWServiceService;
import eu.mpower.framework.security.types.soap.OperationStatus;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.xml.ws.WebServiceRef;
import mpower_hibernate.MedicalDataPoint;
import mpower_hibernate.MedicalDataPointFacade;
import mpower_hibernate.MedicalDataType;
import mpower_hibernate.MedicalDataTypeFacade;
import mpower_hibernate.User;
import mpower_hibernate.UserFasade;

/**
 *
 * @author Grabadora
 */
@WebService(serviceName = "UserMedicalDataManagement", portName = "iUserMedicalDataManagement", endpointInterface = "eu.mpower.framework.management.usermedicaldata.soap.PortTypeUserMedicalDataManagement", targetNamespace = "http://soap.usermedicaldata.management.framework.mpower.eu", wsdlLocation = "WEB-INF/wsdl/UserMedicalDataManagement/UserMedicalDataManagement.wsdl")
public class UserMedicalDataManagement implements PortTypeUserMedicalDataManagement {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/WSDLsecurity.wsdl")
    private AuthorizationWServiceService service;

    private int identificator = 0;

    private static Logger logger = Logger.getLogger(UserMedicalDataManagement.class.toString());

    private static final String serviceUID = "UserMedicalDataManagement";

    public eu.mpower.framework.management.usermedicaldata.soap.AddMedicalDataTypeResponse addMedicalDataType(eu.mpower.framework.management.usermedicaldata.soap.AddMedicalDataTypeRequest input) {
        AddMedicalDataTypeResponse response = new AddMedicalDataTypeResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "addMedicalDataType");
            if (result.isBoolValue()) {
                String name = input.getName();
                if (name == null) {
                    status.setErrorCause("ERROR:addMedicalDataType error because parameter name is incorrect.");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                String description = input.getDescription();
                if (description.contentEquals("")) {
                    status.setErrorCause("ERROR:addMedicalDataType error because parameter description is incorrect.");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                try {
                    MedicalDataTypeFacade mdFacade = new MedicalDataTypeFacade();
                    MedicalDataType mdt = mdFacade.findByName(name);
                    if (mdt != null) {
                        logger.warning("ERROR:addMedicalDataType medical data type with name " + "" + name + " already exists in database!");
                        status.setErrorCause("ERROR:addMedicalDataType medical data type with name " + "" + name + " already exists in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    mdt = new MedicalDataType();
                    mdt.setDescription(description);
                    mdt.setName(name);
                    mdFacade.save(mdt);
                    logger.warning("OK:addMedicalDataType with name " + name + " inserted in database!");
                    status.setErrorCause("OK:addMedicalDataType with name " + name + " inserted in database!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:addMedicalDataType hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Hibernate Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:addMedicalDataType security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.AddMedicalDataPointResponse addMedicalDataPoint(eu.mpower.framework.management.usermedicaldata.soap.AddMedicalDataPointRequest input) {
        AddMedicalDataPointResponse response = new AddMedicalDataPointResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            if (true) {
                String point = input.getPoint();
                if (point == null) {
                    status.setErrorCause("ERROR:addMedicalDataPoint error because parameter point is incorrect.");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                long time = input.getTime();
                Date d = new Date();
                d.setTime(time);
                if (time == 0) {
                    status.setErrorCause("ERROR:addMedicalDataPoint error because parameter time is incorrect.");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                long idUser = input.getIdUser();
                long idType = input.getIdType();
                try {
                    MedicalDataTypeFacade mdFacade = new MedicalDataTypeFacade();
                    MedicalDataType mdt = mdFacade.findById(idType);
                    if (mdt == null) {
                        logger.warning("ERROR:addMedicalDataPoint medical data type with id " + "" + idType + " doesn't exist in database!");
                        status.setErrorCause("ERROR:addMedicalDataPoint, medical data type with id " + "" + idType + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    UserFasade userFac = new UserFasade();
                    User user = userFac.findById(idUser);
                    if (user == null) {
                        logger.warning("ERROR:addMedicalDataPoint user with id " + "" + idUser + " doesn't exist in database!");
                        status.setErrorCause("ERROR:addMedicalDataPoint user with id " + "" + idUser + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_USER_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataPointFacade mdpFacade = new MedicalDataPointFacade();
                    MedicalDataPoint mdp = new MedicalDataPoint();
                    mdp.setMedicaltype(mdt);
                    mdp.setUser(user);
                    mdp.setPoint(point);
                    mdp.setTime(d);
                    mdpFacade.save(mdp);
                    logger.warning("OK:addMedicalDataPoint with point " + point + " inserted in database!");
                    status.setErrorCause("OK:addMedicalDataPoint with point " + point + " inserted in database!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:addMedicalDataPoint hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException("");
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:addMedicalDataPoint security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.GetMedicalDataTypeByIDResponse getMedicalDataTypeByID(eu.mpower.framework.management.usermedicaldata.soap.GetMedicalDataTypeByIDRequest input) {
        GetMedicalDataTypeByIDResponse response = new GetMedicalDataTypeByIDResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "getMedicalDataTypeByID");
            if (result.isBoolValue()) {
                long id = input.getId();
                try {
                    MedicalDataTypeFacade mdFacade = new MedicalDataTypeFacade();
                    MedicalDataType mdt = mdFacade.findById(id);
                    if (mdt == null) {
                        logger.warning("ERROR:getMedicalDataTypeByID medical data type with name " + "" + id + " doesn't exist in database!");
                        status.setErrorCause("ERROR:getMedicalDataTypeByID medical data type with name " + "" + id + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataTypeSOAP mdtSOAP = new MedicalDataTypeSOAP();
                    mdtSOAP.setId(mdt.getId());
                    mdtSOAP.setDescription(mdt.getDescription());
                    mdtSOAP.setName(mdt.getName());
                    logger.warning("OK:getMedicalDataTypeByID with id " + id + " obtained!");
                    status.setErrorCause("OK:getMedicalDataTypeByID with id " + id + " obtained!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    response.setMedicalDataTypeSOAP(mdtSOAP);
                    return response;
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "ERROR:getMedicalDataTypeByID hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:getMedicalDataTypeByID security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.GetMedicalDataTypeByNameResponse getMedicalDataTypeByName(eu.mpower.framework.management.usermedicaldata.soap.GetMedicalDataTypeByNameRequest input) {
        GetMedicalDataTypeByNameResponse response = new GetMedicalDataTypeByNameResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "getMedicalDataTypeByName");
            if (result.isBoolValue()) {
                String name = input.getName();
                try {
                    MedicalDataTypeFacade mdFacade = new MedicalDataTypeFacade();
                    MedicalDataType mdt = mdFacade.findByName(name);
                    if (mdt == null) {
                        logger.warning("ERROR:getMedicalDataTypeByName, medical data type  with name " + "" + name + " doesn't exist in database!");
                        status.setErrorCause("ERROR:getMedicalDataTypeByName, medical data type  with name " + "" + name + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataTypeSOAP mdtSOAP = new MedicalDataTypeSOAP();
                    mdtSOAP.setId(mdt.getId());
                    mdtSOAP.setDescription(mdt.getDescription());
                    mdtSOAP.setName(mdt.getName());
                    logger.warning("OK:getMedicalDataTypeByName with name " + name + " obtained!");
                    status.setErrorCause("OK:getMedicalDataTypeByName with name " + name + " obtained!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    response.setMedicalDataTypeSOAP(mdtSOAP);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:getMedicalDataTypeByName hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:getMedicalDataTypeByName security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.GetMedicalDataPointByIDResponse getMedicalDataPointByID(eu.mpower.framework.management.usermedicaldata.soap.GetMedicalDataPointByIDRequest input) {
        GetMedicalDataPointByIDResponse response = new GetMedicalDataPointByIDResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "getMedicalDataPointByID");
            if (result.isBoolValue()) {
                long id = input.getId();
                try {
                    MedicalDataPointFacade mdpFacade = new MedicalDataPointFacade();
                    MedicalDataPoint mdp = mdpFacade.findById(id);
                    if (mdp == null) {
                        logger.warning("ERROR:getMedicalDataPointByID, medical data point  with id " + "" + id + " doesn't exist in database!");
                        status.setErrorCause("ERROR:getMedicalDataPointByID, medical data type  with id " + "" + id + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_POINT_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataPointSOAP mdpSOAP = new MedicalDataPointSOAP();
                    mdpSOAP.setId(mdp.getIdpoint());
                    mdpSOAP.setIdType(mdp.getMedicaltype().getId());
                    mdpSOAP.setIdUser(mdp.getUser().getUserID());
                    mdpSOAP.setPoint(mdp.getPoint());
                    mdpSOAP.setTime(mdp.getTime().getTime());
                    logger.warning("OK:getMedicalDataPointByID with id " + id + " obtained!");
                    status.setErrorCause("OK:getMedicalDataPointByID with id " + id + " obtained!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    response.setMedicalDataPointSOAP(mdpSOAP);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:getMedicalDataPointByID hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:getMedicalDataPointByID security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.GetAllMedicalDataPointByUserIDResponse getAllMedicalDataPointByUserID(eu.mpower.framework.management.usermedicaldata.soap.GetAllMedicalDataPointByUserIDRequest input) {
        GetAllMedicalDataPointByUserIDResponse response = new GetAllMedicalDataPointByUserIDResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "getAllMedicalDataPointByUserID");
            if (result.isBoolValue()) {
                long iduser = input.getIdUser();
                try {
                    MedicalDataPointFacade mdpFacade = new MedicalDataPointFacade();
                    List list = mdpFacade.findByUser(iduser);
                    if (list == null || list.isEmpty()) {
                        logger.warning("ERROR:getAllMedicalDataPointByUserID medical data point with iduser " + "" + iduser + " doesn't exist in database!");
                        status.setErrorCause("ERROR:getAllMedicalDataPointByUserID medical data type with iduser " + "" + iduser + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_POINT_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataPointSOAP mdpSOAP;
                    Iterator e = list.iterator();
                    MedicalDataPoint mdp;
                    while (e.hasNext()) {
                        mdp = (MedicalDataPoint) e.next();
                        mdpSOAP = new MedicalDataPointSOAP();
                        mdpSOAP.setId(mdp.getIdpoint());
                        mdpSOAP.setIdType(mdp.getMedicaltype().getId());
                        mdpSOAP.setIdUser(mdp.getUser().getUserID());
                        mdpSOAP.setPoint(mdp.getPoint());
                        mdpSOAP.setTime(mdp.getTime().getTime());
                        response.getMedicalDataPointSOAP().add(mdpSOAP);
                    }
                    logger.warning("OK:getAllMedicalDataPointByUserID with id user " + iduser + " obtained!");
                    status.setErrorCause("OK:getAllMedicalDataPointByUserID with id user " + iduser + " obtained!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:getAllMedicalDataPointByUserID hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:getAllMedicalDataPointByUserID security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.GetAllMedicalDataPointByTypeIDResponse getAllMedicalDataPointByTypeID(eu.mpower.framework.management.usermedicaldata.soap.GetAllMedicalDataPointByTypeIDRequest input) {
        GetAllMedicalDataPointByTypeIDResponse response = new GetAllMedicalDataPointByTypeIDResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "getAllMedicalDataPointByTypeID");
            if (result.isBoolValue()) {
                long idtype = input.getIdType();
                try {
                    MedicalDataTypeFacade mdtFacade = new MedicalDataTypeFacade();
                    MedicalDataType mdt = mdtFacade.findById(idtype);
                    if (mdt == null) {
                        logger.warning("ERROR:getAllMedicalDataPointByTypeID medical data type with id " + "" + idtype + " doesn't exist in database!");
                        status.setErrorCause("ERROR:getAllMedicalDataPointByTypeID medical data type with id " + "" + idtype + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataPointFacade mdpFacade = new MedicalDataPointFacade();
                    List list = mdpFacade.findByType(idtype);
                    if (list == null) {
                        logger.warning("ERROR:getAllMedicalDataPointByTypeID medical data point with idtype " + "" + idtype + " doesn't exist in database!");
                        status.setErrorCause("ERROR:getAllMedicalDataPointByTypeID medical data type with idtype " + "" + idtype + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_POINT_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataPointSOAP mdpSOAP;
                    Iterator e = list.iterator();
                    MedicalDataPoint mdp;
                    while (e.hasNext()) {
                        mdp = (MedicalDataPoint) e.next();
                        mdpSOAP = new MedicalDataPointSOAP();
                        mdpSOAP.setId(mdp.getIdpoint());
                        mdpSOAP.setIdType(mdp.getMedicaltype().getId());
                        mdpSOAP.setIdUser(mdp.getUser().getUserID());
                        mdpSOAP.setPoint(mdp.getPoint());
                        mdpSOAP.setTime(mdp.getTime().getTime());
                        response.getMedicalDataPointSOAP().add(mdpSOAP);
                    }
                    logger.warning("OK:getAllMedicalDataPointByTypeID with id type " + idtype + " obtained!");
                    status.setErrorCause("OK:getAllMedicalDataPointByTypeID with id type " + idtype + " obtained!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:getAllMedicalDataPointByTypeID hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:getAllMedicalDataPointByTypeID security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.GetAllMedicalDataPointByTypeNameResponse getAllMedicalDataPointByTypeName(eu.mpower.framework.management.usermedicaldata.soap.GetAllMedicalDataPointByTypeNameRequest input) {
        GetAllMedicalDataPointByTypeNameResponse response = new GetAllMedicalDataPointByTypeNameResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "getAllMedicalDataPointByTypeName");
            if (result.isBoolValue()) {
                String name = input.getTypeName();
                try {
                    MedicalDataTypeFacade mdtFacade = new MedicalDataTypeFacade();
                    MedicalDataType mdt = mdtFacade.findByName(name);
                    if (mdt == null) {
                        logger.warning("ERROR:getAllMedicalDataPointByTypeName medical data type with name " + "" + name + " doesn't exist in database!");
                        status.setErrorCause("ERROR:getAllMedicalDataPointByTypeName medical data type with name " + "" + name + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataPointFacade mdpFacade = new MedicalDataPointFacade();
                    List list = mdpFacade.findByType(mdt.getId());
                    if (list == null) {
                        logger.warning("ERROR:getAllMedicalDataPointByTypeName, medical data point  with type name " + "" + name + " doesn't exist in database!");
                        status.setErrorCause("ERROR:getAllMedicalDataPointByTypeName, medical data type  with type name " + "" + name + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_POINT_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataPointSOAP mdpSOAP;
                    Iterator e = list.iterator();
                    MedicalDataPoint mdp;
                    while (e.hasNext()) {
                        mdp = (MedicalDataPoint) e.next();
                        mdpSOAP = new MedicalDataPointSOAP();
                        mdpSOAP.setId(mdp.getIdpoint());
                        mdpSOAP.setIdType(mdp.getMedicaltype().getId());
                        mdpSOAP.setIdUser(mdp.getUser().getUserID());
                        mdpSOAP.setPoint(mdp.getPoint());
                        mdpSOAP.setTime(mdp.getTime().getTime());
                        response.getMedicalDataPointSOAP().add(mdpSOAP);
                    }
                    logger.warning("OK:getAllMedicalDataPointByTypeName with type name " + name + " obtained!");
                    status.setErrorCause("OK:getAllMedicalDataPointByTypeName with type name " + name + " obtained!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:getAllMedicalDataPointByTypeName hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:getAllMedicalDataPointByTypeID security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.DeleteMedicalDataTypeByIDResponse deleteMedicalDataTypeByID(eu.mpower.framework.management.usermedicaldata.soap.DeleteMedicalDataTypeByIDRequest input) {
        DeleteMedicalDataTypeByIDResponse response = new DeleteMedicalDataTypeByIDResponse();
        response.setDeleted(false);
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "deleteMedicalDataTypeByID");
            if (result.isBoolValue()) {
                long idtype = input.getId();
                try {
                    MedicalDataTypeFacade mdtFacade = new MedicalDataTypeFacade();
                    MedicalDataType mdt = mdtFacade.findById(idtype);
                    if (mdt == null) {
                        logger.warning("ERROR:getAllMedicalDataPointByTypeName, medical data type  with id " + "" + idtype + " doesn't exist in database!");
                        status.setErrorCause("ERROR:getAllMedicalDataPointByTypeName, medical data type  with id " + "" + idtype + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    mdtFacade.delete(mdt);
                    response.setDeleted(true);
                    logger.warning("OK:deleteMedicalDataTypeByID with type id " + idtype + " deleted!");
                    status.setErrorCause("OK:deleteMedicalDataTypeByID with type id " + idtype + " deleted!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:deleteMedicalDataTypeByID hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:deleteMedicalDataTypeByID security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.DeleteMedicalDataTypeByNameResponse deleteMedicalDataTypeByName(eu.mpower.framework.management.usermedicaldata.soap.DeleteMedicalDataTypeByNameRequest input) {
        DeleteMedicalDataTypeByNameResponse response = new DeleteMedicalDataTypeByNameResponse();
        response.setDeleted(false);
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "deleteMedicalDataTypeByName");
            if (result.isBoolValue()) {
                String name = input.getName();
                try {
                    MedicalDataTypeFacade mdtFacade = new MedicalDataTypeFacade();
                    MedicalDataType mdt = mdtFacade.findByName(name);
                    if (mdt == null) {
                        logger.warning("ERROR:deleteMedicalDataTypeByName medical data type with name " + "" + name + " doesn't exist in database!");
                        status.setErrorCause("ERROR:deleteMedicalDataTypeByName medical data type with name " + "" + name + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    mdtFacade.delete(mdt);
                    response.setDeleted(true);
                    logger.warning("OK:deleteMedicalDataTypeByName with type name " + name + " deleted!");
                    status.setErrorCause("OK:deleteMedicalDataTypeByName with type name " + name + " deleted!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "ERROR:deleteMedicalDataTypeByName hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:deleteMedicalDataTypeByName security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.DeleteAllMedicalDataTypeResponse deleteAllMedicalDataType(eu.mpower.framework.management.usermedicaldata.soap.DeleteAllMedicalDataTypeRequest input) {
        DeleteAllMedicalDataTypeResponse response = new DeleteAllMedicalDataTypeResponse();
        response.setDeleted(false);
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "deleteAllMedicalDataType");
            if (result.isBoolValue()) {
                try {
                    MedicalDataTypeFacade mdtFacade = new MedicalDataTypeFacade();
                    List list = mdtFacade.getAll();
                    if (list == null) {
                        logger.warning("ERROR:deleteAllMedicalDataType any medical data type exists in database!");
                        status.setErrorCause("ERROR:deleteAllMedicalDataType any medical data type exists in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    Iterator iter = list.iterator();
                    MedicalDataType aux;
                    while (iter.hasNext()) {
                        aux = (MedicalDataType) iter.next();
                        mdtFacade.delete(aux);
                    }
                    response.setDeleted(true);
                    logger.warning("OK:deleteAllMedicalDataType all medical data type deleted!");
                    status.setErrorCause("OK:deleteAllMedicalDataType all medical data type deleted!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:deleteAllMedicalDataType hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:deleteAllMedicalDataType security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.DeleteMedicalDataPointByIDResponse deleteMedicalDataPointByID(eu.mpower.framework.management.usermedicaldata.soap.DeleteMedicalDataPointByIDRequest input) {
        DeleteMedicalDataPointByIDResponse response = new DeleteMedicalDataPointByIDResponse();
        response.setDeleted(false);
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "deleteMedicalDataPointByID");
            if (result.isBoolValue()) {
                long id = input.getId();
                try {
                    MedicalDataPointFacade mdpFacade = new MedicalDataPointFacade();
                    MedicalDataPoint mdp = mdpFacade.findById(id);
                    if (mdp == null) {
                        logger.warning("ERROR:deleteMedicalDataPointByID medical data point with id " + "" + id + " doesn't exist in database!");
                        status.setErrorCause("ERROR:deleteMedicalDataPointByID medical data point with id " + "" + id + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    mdpFacade.delete(mdp);
                    response.setDeleted(true);
                    logger.warning("OK:deleteMedicalDataPointByID  medical data point with id " + "" + id + " deleted!");
                    status.setErrorCause("OK:deleteMedicalDataPointByID  medical data point with id " + "" + id + " deleted!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:deleteMedicalDataPointByID hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:deleteMedicalDataPointByID security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.DeleteMedicalDataPointByIDUserResponse deleteMedicalDataPointByIDUser(eu.mpower.framework.management.usermedicaldata.soap.DeleteMedicalDataPointByIDUserRequest input) {
        DeleteMedicalDataPointByIDUserResponse response = new DeleteMedicalDataPointByIDUserResponse();
        response.setDeleted(false);
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "deleteMedicalDataPointByIDUser");
            if (result.isBoolValue()) {
                long iduser = input.getIdUser();
                UserFasade userFacade = new UserFasade();
                User user = userFacade.findById(iduser);
                try {
                    if (user == null) {
                        logger.warning("ERROR:deleteMedicalDataPointByIDUser user with id " + "" + iduser + " doesn't exist in database!");
                        status.setErrorCause("ERROR:deleteMedicalDataPointByIDUser user with id " + "" + iduser + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_USER_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataPointFacade mdpFacade = new MedicalDataPointFacade();
                    List list = mdpFacade.findByUser(iduser);
                    if (list == null) {
                        logger.warning("ERROR:deleteMedicalDataPointByIDUser any medical data type with id user " + iduser + " exists in database!");
                        status.setErrorCause("ERROR:deleteMedicalDataPointByIDUser any medical data type with id user " + iduser + " exists in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_POINT_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    Iterator iter = list.iterator();
                    MedicalDataPoint aux;
                    while (iter.hasNext()) {
                        aux = (MedicalDataPoint) iter.next();
                        mdpFacade.delete(aux);
                    }
                    response.setDeleted(true);
                    logger.warning("OK:deleteMedicalDataPointByIDUser all medical data point deleted!");
                    status.setErrorCause("OK:deleteMedicalDataPointByIDUser all medical data point deleted!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:deleteMedicalDataPointByIDUser hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:deleteMedicalDataPointByIDUser security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.DeleteMedicalDataPointByIDTypeResponse deleteMedicalDataPointByIDType(eu.mpower.framework.management.usermedicaldata.soap.DeleteMedicalDataPointByIDTypeRequest input) {
        DeleteMedicalDataPointByIDTypeResponse response = new DeleteMedicalDataPointByIDTypeResponse();
        response.setDeleted(false);
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "deleteMedicalDataPointByIDType");
            if (result.isBoolValue()) {
                long idtype = input.getIdType();
                MedicalDataTypeFacade mdtFacade = new MedicalDataTypeFacade();
                MedicalDataType type = mdtFacade.findById(idtype);
                try {
                    if (type == null) {
                        logger.warning("ERROR:deleteMedicalDataPointByIDUser type with id " + "" + idtype + " doesn't exist in database!");
                        status.setErrorCause("ERROR:deleteMedicalDataPointByIDUser type with id " + "" + idtype + " doesn't exist in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_USER_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataPointFacade mdpFacade = new MedicalDataPointFacade();
                    List list = mdpFacade.findByType(idtype);
                    if (list == null) {
                        logger.warning("ERROR:deleteMedicalDataPointByIDType, any medical data type with id type " + idtype + " exists in database!");
                        status.setErrorCause("ERROR:deleteMedicalDataPointByIDType, any medical data type with id type " + idtype + " exists in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    Iterator iter = list.iterator();
                    MedicalDataPoint aux;
                    while (iter.hasNext()) {
                        aux = (MedicalDataPoint) iter.next();
                        mdpFacade.delete(aux);
                    }
                    response.setDeleted(true);
                    logger.warning("OK:deleteMedicalDataPointByIDType all medical data type deleted!");
                    status.setErrorCause("OK:deleteMedicalDataPointByIDType all medical data type deleted!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:deleteMedicalDataPointByIDType hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:deleteMedicalDataPointByIDType security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.DeleteAllMedicalDataPointResponse deleteAllMedicalDataPoint(eu.mpower.framework.management.usermedicaldata.soap.DeleteAllMedicalDataPointRequest input) {
        DeleteAllMedicalDataPointResponse response = new DeleteAllMedicalDataPointResponse();
        response.setDeleted(false);
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "deleteAllMedicalDataPoint");
            if (result.isBoolValue()) {
                try {
                    MedicalDataPointFacade mdpFacade = new MedicalDataPointFacade();
                    List list = mdpFacade.getAll();
                    if (list == null) {
                        logger.warning("ERROR:deleteAllMedicalDataPoint, any medical data point exists in database!");
                        status.setErrorCause("ERROR:deleteAllMedicalDataPoint, any medical data point exists in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    Iterator iter = list.iterator();
                    MedicalDataPoint aux;
                    while (iter.hasNext()) {
                        aux = (MedicalDataPoint) iter.next();
                        mdpFacade.delete(aux);
                    }
                    response.setDeleted(true);
                    logger.warning("OK:deleteAllMedicalDataPoint all medical data type deleted!");
                    status.setErrorCause("OK:deleteAllMedicalDataPoint all medical data type deleted!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:deleteAllMedicalDataPoint hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:deleteAllMedicalDataPoint security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.GetAllMedicalDataTypeResponse getAllMedicalDataType(eu.mpower.framework.management.usermedicaldata.soap.GetAllMedicalDataTypeRequest input) {
        GetAllMedicalDataTypeResponse response = new GetAllMedicalDataTypeResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "getAllMedicalDataType");
            if (result.isBoolValue()) {
                try {
                    MedicalDataTypeFacade mdtFacade = new MedicalDataTypeFacade();
                    List list = mdtFacade.getAll();
                    if (list == null) {
                        logger.warning("ERROR:getAllMedicalDataType, any medical data point exists in database!");
                        status.setErrorCause("ERROR:getAllMedicalDataType, any medical data type exists in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_TYPE_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataTypeSOAP mdtSOAP;
                    Iterator e = list.iterator();
                    MedicalDataType mdt;
                    while (e.hasNext()) {
                        mdt = (MedicalDataType) e.next();
                        mdtSOAP = new MedicalDataTypeSOAP();
                        mdtSOAP.setId(mdt.getId());
                        mdtSOAP.setDescription(mdt.getDescription());
                        mdtSOAP.setName(mdt.getName());
                        response.getMedicalDataTypeSOAP().add(mdtSOAP);
                    }
                    logger.warning("OK:getAllMedicalDataType all medicat data type obtained!");
                    status.setErrorCause("OK:getAllMedicalDataType all medicat data type obtained!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:getAllMedicalDataType hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:getAllMedicalDataType security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    public eu.mpower.framework.management.usermedicaldata.soap.GetAllMedicalDataPointResponse getAllMedicalDataPoint(eu.mpower.framework.management.usermedicaldata.soap.GetAllMedicalDataPointRequest input) {
        GetAllMedicalDataPointResponse response = new GetAllMedicalDataPointResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "getAllMedicalDataPoint");
            if (result.isBoolValue()) {
                try {
                    MedicalDataPointFacade mdpFacade = new MedicalDataPointFacade();
                    List list = mdpFacade.getAll();
                    if (list == null) {
                        logger.warning("ERROR:getAllMedicalDataPoint, any medical data point  with type name " + "exists in database!");
                        status.setErrorCause("ERROR:getAllMedicalDataPoint, any medical data point  with type name " + "exists in database!");
                        status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_POINT_NOT_FOUND.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                        response.setStatus(status);
                        return response;
                    }
                    MedicalDataPointSOAP mdpSOAP;
                    Iterator e = list.iterator();
                    MedicalDataPoint mdp;
                    while (e.hasNext()) {
                        mdp = (MedicalDataPoint) e.next();
                        mdpSOAP = new MedicalDataPointSOAP();
                        mdpSOAP.setId(mdp.getIdpoint());
                        mdpSOAP.setIdType(mdp.getMedicaltype().getId());
                        mdpSOAP.setIdUser(mdp.getUser().getUserID());
                        mdpSOAP.setPoint(mdp.getPoint());
                        mdpSOAP.setTime(mdp.getTime().getTime());
                        response.getMedicalDataPointSOAP().add(mdpSOAP);
                    }
                    logger.warning("OK:getAllMedicalDataPoint all medical data point obtained!");
                    status.setErrorCause("OK:getAllMedicalDataPoint all medical data point obtained!");
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_OK.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "ERROR:getAllMedicalDataPoint hibernate error", e);
                    status.setMessageID(getIncMessageID());
                    status.setErrorCause("Security Error: " + e.getMessage());
                    status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_HIBERNATE.ordinal());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "ERROR:getAllMedicalDataPoint security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.USERMEDICALDATAMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            response.setStatus(status);
            return response;
        }
    }

    private int getIncMessageID() {
        if (identificator == Integer.MAX_VALUE) {
            identificator = 0;
        }
        identificator++;
        return identificator;
    }
}
