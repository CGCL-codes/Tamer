package eu.mpower.framework.management.accessdoorcontrolmanagement.soap;

import eu.mpower.framework.security.authorization.soap.AuthorizationWServiceService;
import eu.mpower.framework.security.types.soap.OperationStatus;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import mpower_hibernate.Accessinterval;
import mpower_hibernate.AccessintervalFacade;
import mpower_hibernate.Accesslog;
import mpower_hibernate.AccesslogFacade;
import mpower_hibernate.Accesspermissions;
import mpower_hibernate.AccesspermissionsFacade;
import mpower_hibernate.Accessusers;
import mpower_hibernate.AccessusersFacade;
import mpower_hibernate.DeviceTypes;
import mpower_hibernate.DeviceTypesFacade;
import mpower_hibernate.Devices;
import mpower_hibernate.DevicesFacade;
import mpower_hibernate.Protocols;
import mpower_hibernate.ProtocolsFacade;
import mpower_hibernate.User;
import mpower_hibernate.UserFasade;

/**
 *
 * @author Grabadora
 */
@WebService(serviceName = "AccessDoorControlManager", portName = "iAccessDoorControlManagement", endpointInterface = "eu.mpower.framework.management.accessdoorcontrolmanagement.soap.PortTypeAccesDoorControlManager", targetNamespace = "http://soap.accessdoorcontrolmanagement.management.framework.mpower.eu", wsdlLocation = "WEB-INF/wsdl/AccessDoorControlManagement/AccessDoorControlManagement.wsdl")
public class AccessDoorControlManagement implements PortTypeAccesDoorControlManager {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/WSDLsecurity.wsdl")
    private AuthorizationWServiceService service;

    private int identificator = 0;

    private static Logger logger = Logger.getLogger(AccessDoorControlManagement.class.toString());

    private static final String serviceUID = "AccessDoorControlManagement";

    public static void main(String[] args) {
        DeviceTypesFacade tFacade = new DeviceTypesFacade();
        DeviceTypes dtype = new DeviceTypes();
        dtype.setName("RFID Door");
        tFacade.save(dtype);
        ProtocolsFacade pFacade = new ProtocolsFacade();
        Protocols pr = new Protocols();
        pr.setName("UDP");
        pFacade.save(pr);
        DevicesFacade facade = new DevicesFacade();
        Devices d = new Devices();
        d.setDescription("Door entrance");
        d.setDeviceName("KBIO2");
        d.setPhysicalAddress("2");
        d.setDeviceTypes(dtype);
        d.setProtocols(pr);
        d.setDeviceState('c');
        facade.save(d);
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.AddAccessUserResponse addAccessUser(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.AddAccessUser input) {
        AddAccessUserResponse response = new AddAccessUserResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "addAccessUser");
            if (result.isBoolValue()) {
                long idTag = input.getRfidID();
                if (idTag == 0) {
                    status.setErrorCause("ERROR:AddAccessUser error because parameter rfid_tag  is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                } else {
                    try {
                        AccessusersFacade user_facade = new AccessusersFacade();
                        Accessusers user = new Accessusers();
                        Accessusers aux = user_facade.findById(idTag);
                        if (aux == null) {
                            user.setIdtag(idTag);
                            UserFasade uf = new UserFasade();
                            long userId = input.getIdUser();
                            User mpower_user = uf.findById(userId);
                            if (mpower_user != null) {
                                user.setUser(mpower_user);
                                user_facade.save(user);
                                logger.warning("OK:AccessUser with rfid tag " + "" + idTag + " inserted in database!");
                                status.setErrorCause("OK:AccessUser with rfid tag " + "" + idTag + " inserted in database!");
                                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                                status.setMessageID(getIncMessageID());
                                status.setTimeStamp(System.currentTimeMillis());
                                AccessUser acc = new AccessUser();
                                acc.setIdRfidTag(idTag);
                                acc.setIdUser(userId);
                                response.setAccessUser(acc);
                            } else {
                                logger.warning("ERROR:AddAccessUser, user id " + "" + userId + " doesn't exist in database!");
                                status.setErrorCause("ERROR:AddAccessUser, user id " + "" + userId + " doesn't exist in database!");
                                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR.ordinal());
                                status.setMessageID(getIncMessageID());
                                status.setTimeStamp(System.currentTimeMillis());
                            }
                        } else {
                            logger.warning("ERROR:AddAccessUser with rfid tag " + "" + idTag + " already exists in database!");
                            status.setErrorCause("ERROR:AddAccessUser with rfid tag " + "" + idTag + " already exists in database!");
                            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR.ordinal());
                            status.setMessageID(getIncMessageID());
                            status.setTimeStamp(System.currentTimeMillis());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.setStatus(status);
                        return response;
                    }
                }
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:AddAccessUser security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.AddAccessPermissionResponse addAccessPermission(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.AddAccessPermission input) {
        AddAccessPermissionResponse response = new AddAccessPermissionResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus result = port.isAuthorized(token, serviceUID, "addAccessPermission");
            if (result.isBoolValue()) {
                long idDoor = input.getIdDoor();
                long idUser = input.getIdUser();
                if (idDoor == 0) {
                    logger.warning("ERROR:AddAccessPermission error because parameter idDoor  is incorrect.");
                    status.setErrorCause("ERROR:AddAccessPermission error because parameter idDoor  is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                } else {
                    if (idUser == 0) {
                        logger.warning("ERROR:AddAccessPermission error because parameter idUser is incorrect.");
                        status.setErrorCause("ERROR:AddAccessPermission error because parameter idUser is incorrect.");
                        status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                        status.setMessageID(getIncMessageID());
                        status.setTimeStamp(System.currentTimeMillis());
                    } else {
                        UserFasade uf = new UserFasade();
                        User mpower_user = uf.findById(idUser);
                        if (mpower_user != null) {
                            DevicesFacade deviceFacade = new DevicesFacade();
                            Devices door = deviceFacade.getById(idDoor);
                            if (door != null) {
                                AccesspermissionsFacade apFacade = new AccesspermissionsFacade();
                                Accesspermissions apermission = new Accesspermissions();
                                Accesspermissions aux = apFacade.findByUserAndDoor(idUser, idDoor);
                                if (aux == null) {
                                    apermission.setUser(mpower_user);
                                    apermission.setDoor(door);
                                    apFacade.save(apermission);
                                    logger.warning("OK:AccessPermission with id " + "" + apermission.getIdpermission() + " inserted in database!");
                                    status.setErrorCause("OK:AccessPermission with id " + "" + apermission.getIdpermission() + " inserted in database!");
                                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                                    status.setMessageID(getIncMessageID());
                                    status.setTimeStamp(System.currentTimeMillis());
                                    AccessPermission result_aux = new AccessPermission();
                                    result_aux.setIdDoor(apermission.getDoor().getId());
                                    result_aux.setIdPermission(apermission.getIdpermission());
                                    result_aux.setIdUser(apermission.getUser().getUserID());
                                    response.setAccessPermission(result_aux);
                                } else {
                                    logger.warning("ERROR:AddAccessPermission with the same door id " + "" + idDoor + " and  same user id " + idUser + " exist in database!");
                                    status.setErrorCause("ERROR:AddAccessPermission with the same door id " + "" + idDoor + " and  same user id " + idUser + " exist in database!");
                                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR.ordinal());
                                    status.setMessageID(getIncMessageID());
                                    status.setTimeStamp(System.currentTimeMillis());
                                }
                            } else {
                                logger.warning("ERROR:AddAccessPermission, door with id " + "" + idDoor + " doesn't exist in database!");
                                status.setErrorCause("ERROR:AddAccessPermission, door with id " + "" + idDoor + " doesn't exist in database!");
                                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR.ordinal());
                                status.setMessageID(getIncMessageID());
                                status.setTimeStamp(System.currentTimeMillis());
                            }
                        } else {
                            logger.warning("ERROR:AddAccessPermission, user with id " + "" + idUser + " doesn't exist in database!");
                            status.setErrorCause("ERROR:AddAccessPermission, user with id " + "" + idUser + " doesn't exist in database!");
                            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR.ordinal());
                            status.setMessageID(getIncMessageID());
                            status.setTimeStamp(System.currentTimeMillis());
                        }
                    }
                }
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(result.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:AddAccessPermission security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.AddAccessIntervalResponse addAccessInterval(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.AddAccessInterval input) {
        AddAccessIntervalResponse response = new AddAccessIntervalResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "addAccessInterval");
            if (os.isBoolValue()) {
                long idPermission = input.getIdPermission();
                AccessTypeCodes access_type_code = input.getAccesType();
                long hourStart = input.getIntervalHourStart();
                long hourFinish = input.getIntervalHourFinish();
                long minutesStart = input.getIntervalMinutesStart();
                long minutesFinish = input.getIntervalMinutesFinish();
                int access_type;
                if (access_type_code == null) {
                    logger.warning("ERROR:AddAccessInterval error because parameter accessType is incorrect.");
                    status.setErrorCause("ERROR:AddAccessInterval error because parameter accessType is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                if (access_type_code.equals(AccessTypeCodes.IN)) {
                    access_type = 0;
                } else {
                    access_type = 1;
                }
                if (hourStart < 0 || hourStart > 23) {
                    logger.warning("ERROR:AddAccessInterval error because parameter hourStart is incorrect.Has to be a number between 0 and 23.");
                    status.setErrorCause("ERROR:AddAccessInterval error because parameter hourStart is incorrect.Has to be a number between 0 and 23.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                if (hourFinish < 0 || hourFinish > 23) {
                    logger.warning("ERROR:AddAccessInterval error because parameter hourFinish is incorrect.Has to be a number between 0 and 23.");
                    status.setErrorCause("ERROR:AddAccessInterval error because parameter hourFinish is incorrect.Has to be a number between 0 and 23.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                if (minutesStart < 0 || minutesStart > 59) {
                    logger.warning("ERROR:AddAccessInterval error because parameter minutesStart is incorrect.Has to be a number between 0 and 59.");
                    status.setErrorCause("ERROR:AddAccessInterval error because parameter minutesStart is incorrect.Has to be a number between 0 and 59.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                if (minutesFinish < 0 || minutesFinish > 59) {
                    logger.warning("ERROR:AddAccessInterval error because parameter minutesFinish is incorrect.Has to be a number between 0 and 59.");
                    status.setErrorCause("ERROR:AddAccessInterval error because parameter minutesFinish is incorrect.Has to be a number between 0 and 59.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                if (idPermission == 0) {
                    logger.warning("ERROR:AddAccessInterval error because parameter idpermission is incorrect.");
                    status.setErrorCause("ERROR:AddAccessInterval error because parameter idPermission is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccesspermissionsFacade apFacade = new AccesspermissionsFacade();
                Accesspermissions permi = apFacade.findById(idPermission);
                if (permi == null) {
                    logger.warning("ERROR:AddAccessInterval error because permission with id " + idPermission + " doesn't exist in database!");
                    status.setErrorCause("ERROR:AddAccessInterval error because permission with id " + idPermission + " doesn't exist in database!");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccessintervalFacade aiFacade = new AccessintervalFacade();
                Accessinterval interval = new Accessinterval();
                interval.setPermissions(permi);
                interval.setAccestype(access_type);
                long interval_time = (hourStart * 1000000) + (minutesStart * 10000) + (hourFinish * 100) + (minutesFinish);
                String inter = String.valueOf(interval_time);
                int size = inter.length();
                int remaining = 8 - size;
                if (remaining != 0) {
                    String aux = "";
                    while (remaining != 0) {
                        aux = "0";
                        inter = aux.concat(inter);
                        remaining--;
                    }
                }
                interval.setInterval(inter);
                aiFacade.save(interval);
                logger.warning("OK:AddAccessInterval with id " + idPermission + " inserted in database!");
                status.setErrorCause("OK:AddAccessInterval with id " + idPermission + " inserted in database!");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                AccessInterval result = new AccessInterval();
                result.setAccesType(access_type_code);
                result.setIdInterval(interval_time);
                AccessPermission permission = new AccessPermission();
                permission.setIdDoor(permi.getDoor().getId());
                permission.setIdPermission(idPermission);
                permission.setIdUser(permi.getUser().getUserID());
                result.setPermission(permission);
                response.setAccessInterval(result);
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:AddAccessInterval security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.DeleteAccessPermissionByIDResponse deleteAccessPermissionByID(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.DeleteAccessPermissionByID input) {
        DeleteAccessPermissionByIDResponse response = new DeleteAccessPermissionByIDResponse();
        response.setIsDeleted(false);
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "deleteAccessPermissionByID");
            if (os.isBoolValue()) {
                long idPermission = input.getIdPermission();
                if (idPermission == 0) {
                    logger.warning("ERROR:DeleteAccessPermissionByID error because parameter idPermission is incorrect.");
                    status.setErrorCause("ERROR:DeleteAccessPermissionByID error because parameter idPermission is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccesspermissionsFacade apFacade = new AccesspermissionsFacade();
                Accesspermissions permission = apFacade.findById(idPermission);
                if (permission == null) {
                    logger.warning("ERROR:DeleteAccessPermissionByID error because Accesspermission with idPermission " + idPermission + " doesn't exist in database.");
                    status.setErrorCause("ERROR:DeleteAccessPermissionByID error because Accesspermission with idPermission " + idPermission + " doesn't exist in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                apFacade.delete(permission);
                logger.warning("OK:DeleteAccessPermissionByID Accesspermission with idPermission " + idPermission + " deleted.");
                status.setErrorCause("OK:DeleteAccessPermissionByID  Accesspermission with idPermission " + idPermission + " deleted.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setIsDeleted(true);
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:DeleteAccessPermissionByID security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.DeleteAccessUserResponse deleteAccessUser(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.DeleteAccessUser input) {
        DeleteAccessUserResponse response = new DeleteAccessUserResponse();
        response.setIsDeleted(false);
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "deleteAccessUser");
            if (os.isBoolValue()) {
                long idRfidTag = input.getIdRfidTag();
                if (idRfidTag == 0) {
                    logger.warning("ERROR:DeleteAccessUser error because parameter idRfidTag is incorrect.");
                    status.setErrorCause("ERROR:DeleteAccessUser error because parameter idRfidTag is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccessusersFacade auFacade = new AccessusersFacade();
                Accessusers user = auFacade.findById(idRfidTag);
                if (user == null) {
                    logger.warning("ERROR:DeleteAccessUser error because Accesspermission with idRfidTag " + idRfidTag + " doesn't exist in database.");
                    status.setErrorCause("ERROR:DeleteAccessUser error because Accesspermission with idRfidTag " + idRfidTag + " doesn't exist in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                auFacade.delete(user);
                logger.warning("OK:DeleteAccessUser Accessuser with idRfidTag " + idRfidTag + " deleted.");
                status.setErrorCause("OK:DeleteAccessUser  Accessuser with idRfidTag " + idRfidTag + " deleted.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setIsDeleted(true);
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:DeleteAccessUser security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.DeleteAccessPermissionByIDUserResponse deleteAccessPermissionByIDUser(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.DeleteAccessPermissionByIDUser input) {
        DeleteAccessPermissionByIDUserResponse response = new DeleteAccessPermissionByIDUserResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "deleteAccessPermissionByIDUser");
            if (os.isBoolValue()) {
                long idUser = input.getIdUser();
                if (idUser == 0) {
                    logger.warning("ERROR:DeleteAccessPermissionByIDUser error because parameter idUser is incorrect.");
                    status.setErrorCause("ERROR:DeleteAccessPermissionByIDUser error because parameter idUser is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccesspermissionsFacade apFacade = new AccesspermissionsFacade();
                List list_perm = apFacade.findAllByIdUser(idUser);
                if (list_perm.size() == 0) {
                    logger.warning("ERROR:DeleteAccessPermissionByIDUser error because Accesspermission with idUser " + idUser + " doesn't exist in database.");
                    status.setErrorCause("ERROR:DeleteAccessPermissionByIDUser error because Accesspermission with idUser " + idUser + " doesn't exist in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Iterator ite = list_perm.iterator();
                Accesspermissions perm;
                while (ite.hasNext()) {
                    perm = (Accesspermissions) ite.next();
                    apFacade.delete(perm);
                }
                logger.warning("OK:DeleteAccessPermissionByIDUser AccessPermissions with idUser " + idUser + " deleted.");
                status.setErrorCause("OK:DeleteAccessPermissionByIDUser  AccessPermissions with idUser " + idUser + " deleted.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:DeleteAccessPermissionByIDUser security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.DeleteAccessIntervalByIDResponse deleteAccessIntervalByID(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.DeleteAccessIntervalByID input) {
        DeleteAccessIntervalByIDResponse response = new DeleteAccessIntervalByIDResponse();
        response.setIsDeleted(false);
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "deleteAccessIntervalByID");
            if (os.isBoolValue()) {
                long idAccessInterval = input.getIdAccessInterval();
                if (idAccessInterval == 0) {
                    logger.warning("ERROR:DeleteAccessIntervalByID error because parameter idAccessInterval is incorrect.");
                    status.setErrorCause("ERROR:DeleteAccessIntervalByID error because parameter idAccessInterval is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccessintervalFacade aiFacade = new AccessintervalFacade();
                Accessinterval interval = aiFacade.findById(idAccessInterval);
                if (interval == null) {
                    logger.warning("ERROR:DeleteAccessIntervalByID error because Accessinterval with idAccessInterval " + idAccessInterval + " doesn't exist in database.");
                    status.setErrorCause("ERROR:DeleteAccessIntervalByID error because Accessinterval with idAccessInterval " + idAccessInterval + " doesn't exist in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                aiFacade.delete(interval);
                logger.warning("OK:DeleteAccessIntervalByID Accessinterval with idAccessInterval " + idAccessInterval + " deleted.");
                status.setErrorCause("OK:DeleteAccessIntervalByID  Accessinterval with idAccessInterval " + idAccessInterval + " deleted.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setIsDeleted(true);
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:DeleteAccessIntervalByID security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.DeleteAllAccessIntervalByIDPermissionResponse deleteAllAccessIntervalByIDPermission(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.DeleteAllAccessIntervalByIDPermission input) {
        DeleteAllAccessIntervalByIDPermissionResponse response = new DeleteAllAccessIntervalByIDPermissionResponse();
        response.setIsDeleted(false);
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "deleteAllAccessIntervalByIDPermission");
            if (os.isBoolValue()) {
                long idPermission = input.getIdpermission();
                AccessintervalFacade aiFacade = new AccessintervalFacade();
                if (idPermission == 0) {
                    logger.warning("ERROR:DeleteAllAccessIntervalByIDPermission error because parameter idPermission is incorrect.");
                    status.setErrorCause("ERROR:DeleteAllAccessIntervalByIDPermission error because parameter idPermission is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                List list_int = aiFacade.findAllByIdPermission(idPermission);
                if (list_int.size() == 0) {
                    logger.warning("ERROR:DeleteAllAccessIntervalByIDPermission error because Accessinterval with idPermission " + idPermission + " doesn't exist in database.");
                    status.setErrorCause("ERROR:DeleteAllAccessIntervalByIDPermission error because Accessinterval with idPermission " + idPermission + " doesn't exist in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Iterator ite = list_int.iterator();
                Accessinterval inter;
                while (ite.hasNext()) {
                    inter = (Accessinterval) ite.next();
                    aiFacade.delete(inter);
                }
                logger.warning("OK:DeleteAllAccessIntervalByIDPermission Accessinterval with idPermission " + idPermission + " deleted.");
                status.setErrorCause("OK:DeleteAllAccessIntervalByIDPermission  Accessinterval with idPermission " + idPermission + " deleted.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setIsDeleted(true);
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:DeleteAllAccessIntervalByIDPermission security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.GetAccessUserByIdRfidTagResponse getAccessUserByIdRfidTag(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.GetAccessUserByIdRfidTag input) {
        GetAccessUserByIdRfidTagResponse response = new GetAccessUserByIdRfidTagResponse();
        Status status = new Status();
        long idRfidTag = input.getIdRfidTag();
        AccessusersFacade aiFacade = new AccessusersFacade();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "getAccessUserByIdRfidTag");
            if (os.isBoolValue()) {
                if (idRfidTag == 0) {
                    logger.warning("ERROR:GetAccessUserByIdRfidTag error because parameter idRfidTag is incorrect.");
                    status.setErrorCause("ERROR:GetAccessUserByIdRfidTag error because parameter idRfidTag is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Accessusers user = aiFacade.findById(idRfidTag);
                if (user == null) {
                    logger.warning("ERROR:GetAccessUserByIdRfidTag error because Accessuser with idRfidTag " + idRfidTag + " doesn't exist in database.");
                    status.setErrorCause("ERROR:GetAccessUserByIdRfidTag error because Accessuser with idRfidTag " + idRfidTag + " doesn't exist in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccessUser aUser = new AccessUser();
                aUser.setIdRfidTag(user.getIdtag());
                aUser.setIdUser(user.getUser().getUserID());
                logger.warning("OK:GetAccessUserByIdRfidTag Accessinterval with idRfidTag " + idRfidTag + " obtained.");
                status.setErrorCause("OK:GetAccessUserByIdRfidTag  Accessinterval with idRfidTag " + idRfidTag + " obtained.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setAccessUser(aUser);
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:GetAccessUserByIdRfidTag security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public GetAllAccessUserResponse getAllAccessUser(GetAllAccessUser input) {
        GetAllAccessUserResponse response = new GetAllAccessUserResponse();
        Status status = new Status();
        AccessusersFacade aiFacade = new AccessusersFacade();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "getAllAccessUser");
            if (os.isBoolValue()) {
                List user_list = aiFacade.findAllBy();
                if (user_list.size() == 0) {
                    logger.warning("ERROR:getAllAccessUser error because doesn't exist any accessuser in database.");
                    status.setErrorCause("ERROR:getAllAccessUser error because doesn't exist any accessuser in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Iterator it = user_list.iterator();
                Accessusers user;
                AccessUser aUser;
                while (it.hasNext()) {
                    user = (Accessusers) it.next();
                    aUser = new AccessUser();
                    aUser.setIdRfidTag(user.getIdtag());
                    aUser.setIdUser(user.getUser().getUserID());
                    response.getAccessUser().add(aUser);
                }
                logger.warning("OK:getAllAccessUser " + user_list.size() + " Accessusers obtained.");
                status.setErrorCause("OK:getAllAccessUser " + user_list.size() + " Accessusers obtained.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setStatus(status);
                return response;
            } else {
                System.out.println("Token " + os.isBoolValue());
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:getAllAccessUser security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public GetAllAccessUserByIDUserResponse getAllAccessUserByIDUser(GetAllAccessUserByIDUser input) {
        GetAllAccessUserByIDUserResponse response = new GetAllAccessUserByIDUserResponse();
        Status status = new Status();
        long idUser = input.getIdUser();
        AccessusersFacade aiFacade = new AccessusersFacade();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "getAllAccessUserTypeByIDUser");
            if (os.isBoolValue()) {
                if (idUser == 0) {
                    logger.warning("ERROR:GetAllAccessUserTypeByIDUser error because parameter idUser is incorrect.");
                    status.setErrorCause("ERROR:GetAllAccessUserTypeByIDUser error because parameter idUser is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                List user_list = aiFacade.findAllByIdUser(idUser);
                if (user_list.size() == 0) {
                    logger.warning("ERROR:GetAllAccessUserTypeByIDUser error because Accessuser with idUser " + idUser + " doesn't exist in database.");
                    status.setErrorCause("ERROR:GetAllAccessUserTypeByIDUser error because Accessuser with idUser " + idUser + " doesn't exist in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Iterator it = user_list.iterator();
                Accessusers user;
                AccessUser aUser;
                while (it.hasNext()) {
                    user = (Accessusers) it.next();
                    aUser = new AccessUser();
                    aUser.setIdRfidTag(user.getIdtag());
                    aUser.setIdUser(user.getUser().getUserID());
                    response.getAccessUser().add(aUser);
                }
                logger.warning("OK:GetAllAccessUserTypeByIDUser Accessuser with idUser " + idUser + " obtained.");
                status.setErrorCause("OK:GetAllAccessUserTypeByIDUser  Accessuser with idUser " + idUser + " obtained.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:GetAllAccessUserTypeByIDUser security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.GetAccessPermissionByIDResponse getAccessPermissionByID(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.GetAccessPermissionByID input) {
        GetAccessPermissionByIDResponse response = new GetAccessPermissionByIDResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "getAccessPermissionByID");
            if (os.isBoolValue()) {
                long idPermission = input.getIdPermission();
                if (idPermission == 0) {
                    logger.warning("ERROR:GetAccessPermissionByID error because parameter idPermission is incorrect.");
                    status.setErrorCause("ERROR:GetAccessPermissionByID error because parameter idPermission is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccesspermissionsFacade apFacade = new AccesspermissionsFacade();
                Accesspermissions permission = apFacade.findById(idPermission);
                if (permission == null) {
                    logger.warning("ERROR:GetAccessPermissionByID error because Accesspermission with idPermission " + idPermission + " doesn't exist in database.");
                    status.setErrorCause("ERROR:GetAccessPermissionByID error because Accesspermission with idPermission " + idPermission + " doesn't exist in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccessPermission aPermission = new AccessPermission();
                aPermission.setIdDoor(permission.getDoor().getId());
                aPermission.setIdUser(permission.getUser().getUserID());
                aPermission.setIdPermission(permission.getIdpermission());
                logger.warning("OK:GetAccessPermissionByID Accesspermission with idPermission " + idPermission + " obtained.");
                status.setErrorCause("OK:GetAccessPermissionByID  Accesspermission with idPermission " + idPermission + " obtained.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setAccesPermissions(aPermission);
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:GetAccessPermissionByID security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.GetAllAccessPermissionByIDDoorResponse getAllAccessPermissionByIDDoor(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.GetAllAccessPermissionByIDDoor input) {
        GetAllAccessPermissionByIDDoorResponse response = new GetAllAccessPermissionByIDDoorResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "getAllAccessPermissionByIDDoor");
            if (os.isBoolValue()) {
                long idDoor = input.getIdDoor();
                if (idDoor == 0) {
                    logger.warning("ERROR:GetAllAccessPermissionByIDDoor error because parameter idDoor is incorrect.");
                    status.setErrorCause("ERROR:GetAllAccessPermissionByIDDoor error because parameter idDoor is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccesspermissionsFacade apFacade = new AccesspermissionsFacade();
                List list_perm = apFacade.findAllByIdDoor(idDoor);
                if (list_perm.size() == 0) {
                    logger.warning("ERROR:GetAllAccessPermissionByIDDoor error because Accesspermission with idDoor " + idDoor + " doesn't exist in database.");
                    status.setErrorCause("ERROR:GetAllAccessPermissionByIDDoor error because Accesspermission with idDoor " + idDoor + " doesn't exist in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Iterator ite = list_perm.iterator();
                Accesspermissions perm;
                AccessPermission access_permission = new AccessPermission();
                while (ite.hasNext()) {
                    perm = (Accesspermissions) ite.next();
                    access_permission.setIdDoor(perm.getDoor().getId());
                    access_permission.setIdPermission(perm.getIdpermission());
                    access_permission.setIdUser(perm.getUser().getUserID());
                    response.getAccesspermissions().add(access_permission);
                }
                logger.warning("OK:GetAllAccessPermissionByIDDoor Accesspermission with idDoor " + idDoor + " obtained.");
                status.setErrorCause("OK:GetAllAccessPermissionByIDDoor  Accesspermission with idDoor " + idDoor + " obtained.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:GetAllAccessPermissionByIDDoor security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public GetAllAccessPermissionByIDUserResponse getAllAccessPermissionByIDUser(GetAllAccessPermissionByIDUser input) {
        GetAllAccessPermissionByIDUserResponse response = new GetAllAccessPermissionByIDUserResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "getAllAccessPermissionByIDUser");
            if (os.isBoolValue()) {
                long idUser = input.getIdUser();
                if (idUser == 0) {
                    logger.warning("ERROR:GetAllAccessPermissionByIDUser error because parameter idUser is incorrect.");
                    status.setErrorCause("ERROR:GetAllAccessPermissionByIDUser error because parameter idUser is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccesspermissionsFacade apFacade = new AccesspermissionsFacade();
                List list_perm = apFacade.findAllByIdUser(idUser);
                if (list_perm.size() == 0) {
                    logger.warning("ERROR:GetAllAccessPermissionByIDUser error because Accesspermission with idUser " + idUser + " doesn't exist in database.");
                    status.setErrorCause("ERROR:GetAllAccessPermissionByIDUser error because Accesspermission with idUser " + idUser + " doesn't exist in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Iterator ite = list_perm.iterator();
                Accesspermissions perm;
                AccessPermission access_permission = new AccessPermission();
                while (ite.hasNext()) {
                    perm = (Accesspermissions) ite.next();
                    access_permission = new AccessPermission();
                    access_permission.setIdDoor(perm.getDoor().getId());
                    access_permission.setIdPermission(perm.getIdpermission());
                    access_permission.setIdUser(perm.getUser().getUserID());
                    response.getAccesspermissions().add(access_permission);
                }
                logger.warning("OK:GetAllAccessPermissionByIDUser Accesspermission with idUser " + idUser + " obtained.");
                status.setErrorCause("OK:GetAllAccessPermissionByIDUser  Accesspermission with idUser " + idUser + " obtained.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:GetAllAccessPermissionByIDUser security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.GetAccessLogByIntervalTimeResponse getAccessLogByIntervalTime(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.GetAccessLogByIntervalTime input) {
        GetAccessLogByIntervalTimeResponse response = new GetAccessLogByIntervalTimeResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "getLogIntervalTime");
            if (os.isBoolValue()) {
                XMLGregorianCalendar intervalStart = input.getInitialTime();
                XMLGregorianCalendar intervalEnd = input.getEndTime();
                if (intervalStart == null) {
                    logger.warning("ERROR:GetLogIntervalTime error because parameter intervalStart is incorrect.");
                    status.setErrorCause("ERROR:GetLogIntervalTime error because parameter intervalStart is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Date initial = intervalStart.toGregorianCalendar().getTime();
                System.out.println("Fecha inicial: " + initial.toString());
                if (intervalEnd == null) {
                    logger.warning("ERROR:GetLogIntervalTime error because parameter intervalEnd is incorrect.");
                    status.setErrorCause("ERROR:GetLogIntervalTime error because parameter intervalEnd is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                GregorianCalendar en = intervalEnd.toGregorianCalendar();
                en.add(GregorianCalendar.DATE, 1);
                Date end = en.getTime();
                System.out.println("Fecha final: " + end.toString());
                AccesslogFacade alogfacade = new AccesslogFacade();
                List list_alog = alogfacade.findAllByIdPermission(initial, end);
                if (list_alog.size() == 0) {
                    logger.warning("ERROR:GetLogIntervalTime error because doesn't exist in database any log betwwen " + initial.toString() + " and " + end.toString() + ".");
                    status.setErrorCause("ERROR:GetLogIntervalTime error because doesn't exist in database any log betwwen " + initial.toString() + " and " + end.toString() + ".");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Iterator ite = list_alog.iterator();
                Accesslog log;
                AccessLog access_log = new AccessLog();
                while (ite.hasNext()) {
                    log = (Accesslog) ite.next();
                    access_log = new AccessLog();
                    access_log.setIdDoor(log.getDoor());
                    if (log.getAccestype() == 0) {
                        access_log.setAccesType(AccessTypeCodes.IN);
                    } else {
                        access_log.setAccesType(AccessTypeCodes.OUT);
                    }
                    access_log.setIdLog(log.getId());
                    access_log.setIdTag(log.getIdtag());
                    access_log.setObservation(log.getObservations());
                    access_log.setTimeStamp(log.getTime().getTime());
                    if (log.getStatus().equalsIgnoreCase("OK")) {
                        access_log.setStatus(StatusCodes.OK);
                    } else {
                        access_log.setStatus(StatusCodes.ERROR);
                    }
                    response.getAccesslog().add(access_log);
                }
                logger.warning("OK:GetLogIntervalTime accesslogs obtained.");
                status.setErrorCause("OK:GetLogIntervalTime accesslogs obtained.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:GetLogIntervalTime security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public eu.mpower.framework.management.accessdoorcontrolmanagement.soap.GetAccessLogByParameterResponse getAccessLogByParameter(eu.mpower.framework.management.accessdoorcontrolmanagement.soap.GetAccessLogByParameter input) {
        GetAccessLogByParameterResponse response = new GetAccessLogByParameterResponse();
        Status status = new Status();
        LogAttribute atribute = input.getAttribute();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "getAccessLogByParameter");
            if (os.isBoolValue()) {
                if (atribute == null) {
                    logger.warning("ERROR:GetAccessLogByParameter error because parameter attribute is incorrect.");
                    status.setErrorCause("ERROR:GetAccessLogByParameter error because parameter attribute is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Long value = input.getValue();
                if (value == null) {
                    logger.warning("ERROR:GetAccessLogByParameter error because parameter value is incorrect.");
                    status.setErrorCause("ERROR:GetAccessLogByParameter error because parameter value is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccesslogFacade alogfacade = new AccesslogFacade();
                List list_alog = null;
                if (atribute.compareTo(LogAttribute.ACCESSTYPE) == 0) {
                    list_alog = alogfacade.getLogbyAccessType(value);
                } else {
                    if (atribute.compareTo(LogAttribute.IDDOOR) == 0) {
                        list_alog = alogfacade.getLogbyIdDoor(value);
                    } else {
                        if (atribute.compareTo(LogAttribute.IDTAG) == 0) {
                            list_alog = alogfacade.getLogbyIdRFID(value);
                        } else {
                            if (atribute.compareTo(LogAttribute.IDUSER) == 0) {
                                list_alog = alogfacade.getLogbyIdUser(value);
                            }
                        }
                    }
                }
                if (list_alog.size() == 0) {
                    logger.warning("ERROR:GetAccessLogByParameter error because doesn't exist in database any log with " + atribute.name() + " " + value + " .");
                    status.setErrorCause("ERROR:GetAccessLogByParameter error because doesn't exist in database any log with " + atribute.name() + " " + value + " .");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Iterator ite = list_alog.iterator();
                Accesslog log;
                AccessLog access_log = new AccessLog();
                while (ite.hasNext()) {
                    log = (Accesslog) ite.next();
                    access_log = new AccessLog();
                    access_log.setIdDoor(log.getDoor());
                    if (log.getAccestype() == 0) {
                        access_log.setAccesType(AccessTypeCodes.IN);
                    } else {
                        access_log.setAccesType(AccessTypeCodes.OUT);
                    }
                    access_log.setIdLog(log.getId());
                    access_log.setIdTag(log.getIdtag());
                    access_log.setObservation(log.getObservations());
                    access_log.setTimeStamp(log.getTime().getTime());
                    if (log.getStatus().equalsIgnoreCase("OK")) {
                        access_log.setStatus(StatusCodes.OK);
                    } else {
                        access_log.setStatus(StatusCodes.ERROR);
                    }
                    response.getAccesslog().add(access_log);
                }
                logger.warning("OK:GetAccessLogByParameter accesslogs obtained.");
                status.setErrorCause("OK:GetAccessLogByParameter accesslogs obtained.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:GetAccessLogByParameter security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
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

    public GetAllAccessIntervalByIDPermissionResponse getAllAccessIntervalByIDPermission(GetAllAccessIntervalByIDPermission input) {
        GetAllAccessIntervalByIDPermissionResponse response = new GetAllAccessIntervalByIDPermissionResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "getAllAccessIntervalByIDPermission");
            if (os.isBoolValue()) {
                long idAccessPermission = input.getIdPermission();
                if (idAccessPermission == 0) {
                    logger.warning("ERROR:GetAllAccessIntervalByIDPermission error because parameter idAccessPermission is incorrect.");
                    status.setErrorCause("ERROR:GetAllAccessIntervalByIDPermission error because parameter idAccessPermission is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccessintervalFacade aiFacade = new AccessintervalFacade();
                List interval_list = aiFacade.findAllByIdPermission(idAccessPermission);
                if (interval_list.size() == 0) {
                    logger.warning("ERROR:GetAllAccessIntervalByIDPermission error because Accessinterval with idAccessPermission " + idAccessPermission + " doesn't exist in database.");
                    status.setErrorCause("ERROR:GetAllAccessIntervalByIDPermission error because Accessinterval with idAccessPermission " + idAccessPermission + " doesn't exist in database.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Iterator it = interval_list.iterator();
                Accessinterval interval;
                AccessInterval aInterval;
                while (it.hasNext()) {
                    interval = (Accessinterval) it.next();
                    aInterval = new AccessInterval();
                    if (interval.getAccestype() == 0) {
                        aInterval.setAccesType(AccessTypeCodes.IN);
                    } else {
                        aInterval.setAccesType(AccessTypeCodes.OUT);
                    }
                    GetAccessPermissionByID getAP = new GetAccessPermissionByID();
                    getAP.setIdPermission(idAccessPermission);
                    getAP.setSecurityToken(input.getSecurityToken());
                    AccessPermission permission = getAccessPermissionByID(getAP).getAccesPermissions();
                    aInterval.setPermission(permission);
                    aInterval.setInterval(interval.getInterval());
                    aInterval.setIdInterval(interval.getId());
                    response.getAccessintervals().add(aInterval);
                }
                logger.warning("OK:GetAllAccessIntervalByIDPermission Accessinterval with idAccessPermission " + idAccessPermission + " obtained.");
                status.setErrorCause("OK:GetAllAccessIntervalByIDPermission  Accessinterval with idAccessPermission " + idAccessPermission + " obtained.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:GetAllAccessIntervalByIDPermission security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public GetAccessLogByStatusResponse getAccessLogByStatus(GetAccessLogByStatus input) {
        GetAccessLogByStatusResponse response = new GetAccessLogByStatusResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "getAccessLogByStatus");
            if (os.isBoolValue()) {
                StatusCodes value = input.getStatus();
                if (value == null) {
                    logger.warning("ERROR:getAccessLogByStatus error because parameter status is incorrect.");
                    status.setErrorCause("ERROR:getAccessLogByStatus error because parameter status is incorrect.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                AccesslogFacade alogfacade = new AccesslogFacade();
                List list_alog = alogfacade.getLogbyStatusCode(value.name());
                if (list_alog.size() == 0) {
                    logger.warning("ERROR:getAccessLogByStatus error because doesn't exist in database any log with status " + value + " .");
                    status.setErrorCause("ERROR:getAccessLogByStatus error because doesn't exist in database any log with status " + value + " .");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Iterator ite = list_alog.iterator();
                Accesslog log;
                AccessLog access_log = new AccessLog();
                while (ite.hasNext()) {
                    log = (Accesslog) ite.next();
                    access_log = new AccessLog();
                    access_log.setIdDoor(log.getDoor());
                    if (log.getAccestype() == 0) {
                        access_log.setAccesType(AccessTypeCodes.IN);
                    } else {
                        access_log.setAccesType(AccessTypeCodes.OUT);
                    }
                    access_log.setIdUser(log.getIduser());
                    access_log.setIdLog(log.getId());
                    access_log.setIdTag(log.getIdtag());
                    access_log.setObservation(log.getObservations());
                    access_log.setTimeStamp(log.getTime().getTime());
                    if (log.getStatus().equalsIgnoreCase("OK")) {
                        access_log.setStatus(StatusCodes.OK);
                    } else {
                        access_log.setStatus(StatusCodes.ERROR);
                    }
                    response.getAccesslog().add(access_log);
                }
                logger.warning("OK:getAccessLogByStatus accesslogs obtained.");
                status.setErrorCause("OK:getAccessLogByStatus accesslogs obtained.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:getAccessLogByStatus security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }

    public GetAllAccessLogResponse getAllAccessLog(GetAllAccessLog input) {
        GetAllAccessLogResponse response = new GetAllAccessLogResponse();
        Status status = new Status();
        try {
            eu.mpower.framework.security.authorization.soap.PortTypeAuthorizationWService port = service.getAuthorizationWServicePort();
            eu.mpower.framework.security.types.soap.SecurityToken token = input.getSecurityToken();
            OperationStatus os = port.isAuthorized(token, serviceUID, "getAllAccessLog");
            if (os.isBoolValue()) {
                AccesslogFacade alogfacade = new AccesslogFacade();
                List list_alog = alogfacade.getAllLog();
                if (list_alog.size() == 0) {
                    logger.warning("ERROR:getAllAccessLog error because doesn't exist in database any log.");
                    status.setErrorCause("ERROR:getAllAccessLog error because doesn't exist in database any log.");
                    status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_PARAMETER.ordinal());
                    status.setMessageID(getIncMessageID());
                    status.setTimeStamp(System.currentTimeMillis());
                    response.setStatus(status);
                    return response;
                }
                Iterator ite = list_alog.iterator();
                Accesslog log;
                AccessLog access_log = new AccessLog();
                while (ite.hasNext()) {
                    log = (Accesslog) ite.next();
                    access_log = new AccessLog();
                    access_log.setIdDoor(log.getDoor());
                    if (log.getAccestype() == 0) {
                        access_log.setAccesType(AccessTypeCodes.IN);
                    } else {
                        access_log.setAccesType(AccessTypeCodes.OUT);
                    }
                    access_log.setIdUser(log.getIduser());
                    access_log.setIdLog(log.getId());
                    access_log.setIdTag(log.getIdtag());
                    access_log.setObservation(log.getObservations());
                    access_log.setTimeStamp(log.getTime().getTime());
                    if (log.getStatus().equalsIgnoreCase("OK")) {
                        access_log.setStatus(StatusCodes.OK);
                    } else {
                        access_log.setStatus(StatusCodes.ERROR);
                    }
                    response.getAccesslog().add(access_log);
                }
                logger.warning("OK:getAllAccessLog accesslogs obtained.");
                status.setErrorCause("OK:getAllAccessLog accesslogs obtained.");
                status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_OK.ordinal());
                status.setMessageID(getIncMessageID());
                status.setTimeStamp(System.currentTimeMillis());
                response.setStatus(status);
                return response;
            } else {
                throw new SecurityException(os.getMessage());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "ERROR:getAllAccessLog security error", ex);
            status.setMessageID(getIncMessageID());
            status.setErrorCause("Security Error: " + ex.getMessage());
            status.setResult(ErrorCodes.ACCESSDOORCONTROLMANAGEMENT_ERROR_CALLING_AUTHORIZATION_PROCESS.ordinal());
            status.setTimeStamp(System.currentTimeMillis());
            return response;
        }
    }
}
