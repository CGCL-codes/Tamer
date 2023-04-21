package com.hisham.parking.accountaccess;

import com.hisham.parking.accountaccess.util.*;
import com.hisham.parking.sql.*;
import com.hisham.util.*;
import com.hisham.powerpark.util.CustomerInfo;
import com.hisham.powerpark.util.*;
import com.hisham.powerpark.*;
import com.hisham.parking.util.*;
import java.util.*;
import org.apache.struts.action.*;
import org.apache.commons.validator.*;
import com.hisham.parking.accountaccess.sql.*;

/**
 * <p>Title: Parking Services Online</p>
 * <p>Description: Tools for online account access, sales, citations and appeals</p>
 * @author Ali Hisham Malik
 * @version 1.3
 */
public class PsAccountService {

    private DbAccountAccess database = new DbAccountAccess();

    public static final int CUSTOMER_NOT_EXISTS = -2;

    public static final int LOGIN_VERIFIED = 1;

    public static final int LOGIN_NOT_VERIFIED = 0;

    public static final int ONLINE_ACCOUNT_NOT_EXISTS = -1;

    /**
	 *
	 * @param customerId int
	 * @return VehicleRegistrationInfoList
	 */
    public static final VehicleCustomerInfo[] getVehicleRegistrationInfos(int customerId) {
        return new DbGeneralAccess().getVehicleCustomerInfoOwners(customerId);
    }

    /**
	 *
	 * @param customerId int
	 * @return VehicleRegistrationInfoList
	 */
    public static final VehicleCustomerInfo[] getVehicleCustomerInfos(int customerId) {
        return new DbGeneralAccess().getVehicleCustomerInfos(customerId);
    }

    /**
	 *
	 * @param customerId String customer who is responsible for adding the vehicle
	 * @param vehicleRegInfo VehicleRegistrationInfo
	 * @param sendVerificationRequest boolean
	 * @return boolean
	 */
    public final boolean createVehicleRegistrationInfo(int customerId, VehicleCustomerInfo vehicleRegInfo, boolean sendVerificationRequest) {
        if (vehicleRegInfo.getCustomerInfo().getCustomerId() <= 0) {
            if (vehicleRegInfo.getCustomerInfo().getCustomerName().equalsPartially(this.getDatabase().getDbGeneralAccess().getCustomerName(customerId))) {
                vehicleRegInfo.getCustomerInfo().setCustomerId(customerId);
            }
        }
        if (this.getDatabase().createVehicleRegistrationInfo(vehicleRegInfo)) {
            if (sendVerificationRequest) {
                this.sendVehicleAddedConfirmation(customerId, vehicleRegInfo);
                this.getDatabase().addCustomerNotes(customerId, "Customer added vehicle with license plate:" + vehicleRegInfo.getVehicleInfo().getLicensePlate() + ". Please verify information. - OS");
            }
            return true;
        }
        return false;
    }

    /**
	 *
	 * @return DbAccountAccess
	 */
    private DbAccountAccess getDatabase() {
        return database;
    }

    /**
	 * Creates the account in PP based on the information gathered
	 *
	 * @param customerInfo CustomerInfo
	 * @return boolean true if account created, false otherwise
	 */
    public final boolean createCustomerAccountPp(CustomerInfo customerInfo) {
        if (this.getDatabase().getDbGeneralAccess().getCustomerIdPp(customerInfo.getCampusId()) > 0) {
            return false;
        } else {
            customerInfo.setCustomerId(0);
            return this.getDatabase().updateCustomerAccountPp(customerInfo);
        }
    }

    /**
	 *
	 * @param customerId int
	 * @param vehicleRegInfo VehicleRegistrationInfo
	 * @return String
	 */
    public final String sendVehicleAddedConfirmation(int customerId, VehicleCustomerInfo vehicleRegInfo) {
        CustomerContactInfo contactInfo = this.getDatabase().getDbGeneralAccess().getCustomerContactInfo(customerId);
        StringBuffer body = new StringBuffer();
        body.append("Dear " + contactInfo.getCustomerName().getGreetingName() + ",");
        body.append("\r\n");
        body.append("This email is to confirm the vehicle you added with plate#:" + vehicleRegInfo.getVehicleInfo().getLicensePlate());
        body.append("\r\n");
        body.append("It is required that you mail/fax/bring in a copy of the vehicle registration to our office.");
        if (PsGeneralService.sendPlainEMail(contactInfo.getEmailAddress(), contactInfo.getCustomerName().getGreetingName(), "Vehicle Confirmation", body.toString())) return contactInfo.getEmailAddress();
        return null;
    }

    /**
	 *
	 * @param customerId int
	 * @param oldEmailAddress String
	 * @param newEmailAddress String
	 * @return boolean
	 */
    public static final boolean sendCustomerInfoUpdateEmail(int customerId, String oldEmailAddress, String newEmailAddress) {
        CustomerBasicInfo customerBasicInfo = new DbGeneralAccess().getCustomerBasicInfo(customerId);
        boolean sendDuplicate = false;
        StringBuffer body = new StringBuffer();
        body.append("Dear " + customerBasicInfo.getCustomerName().getGreetingName() + ",");
        body.append("\r\n");
        body.append("\r\n");
        body.append("Your account information has been updated.");
        body.append("\r\n");
        if (!oldEmailAddress.equalsIgnoreCase(newEmailAddress)) {
            sendDuplicate = true;
            body.append("This email is also being sent to both the original and the updated email addresses to warn against any unwanted update.");
            body.append("\r\n");
        }
        boolean allOk = PsGeneralService.sendPlainEMail(newEmailAddress, customerBasicInfo.getCustomerName().getGreetingName(), "Parking Services - Account Update", body.toString());
        if (sendDuplicate) allOk = allOk && PsGeneralService.sendPlainEMail(oldEmailAddress, customerBasicInfo.getCustomerName().getGreetingName(), "Parking Services - Account Update", body.toString());
        return allOk;
    }

    /**
	 *
	 * @param customerId int
	 * @return PermitInfo[]
	 */
    public static final com.hisham.parking.accountaccess.util.PermitInfo[] getPermitInfosUnexpired(int customerId) {
        DbAccountAccess db = new DbAccountAccess();
        com.hisham.powerpark.util.PermitInfo[] permitInfosDb = db.getDbGeneralAccess().getPermitInfosUnexpired(customerId);
        com.hisham.parking.accountaccess.util.PermitInfo[] permitInfos = new com.hisham.parking.accountaccess.util.PermitInfo[permitInfosDb.length];
        int printingDuration = ParkingResources.getPermitPrintingDuration();
        Calendar calIssueDateLimit = Calendar.getInstance();
        calIssueDateLimit.add(Calendar.DATE, -printingDuration);
        for (int i = 0; i < permitInfosDb.length; i++) {
            permitInfos[i] = new com.hisham.parking.accountaccess.util.PermitInfo(permitInfosDb[i]);
            if (calIssueDateLimit.getTime().compareTo(permitInfos[i].getPermitIssueDate()) < 0) {
                permitInfos[i].setAllowPermitPrinting(true);
            }
        }
        return permitInfos;
    }

    /**
	 *
	 * @param permitId int
	 * @return PermitInfo
	 */
    public static final com.hisham.parking.accountaccess.util.PermitInfo getPermitInfo(int permitId) {
        DbAccountAccess db = new DbAccountAccess();
        com.hisham.powerpark.util.PermitInfo permitInfosDb = db.getDbGeneralAccess().getPermitInfo(permitId);
        int printingDuration = ParkingResources.getPermitPrintingDuration();
        Calendar calIssueDateLimit = Calendar.getInstance();
        calIssueDateLimit.add(Calendar.DATE, -printingDuration);
        com.hisham.parking.accountaccess.util.PermitInfo permitInfo = new com.hisham.parking.accountaccess.util.PermitInfo(permitInfosDb);
        if (calIssueDateLimit.getTime().compareTo(permitInfo.getPermitIssueDate()) < 0) {
            permitInfo.setAllowPermitPrinting(true);
        }
        return permitInfo;
    }

    /**
	 * Addable email is editable when blank
	 * Updatable email is editable when not blank
	 * Insertable email is editable when blank and the only instance of that email type
	 *
	 * @param emailAddressInfo EmailAddressInfo
	 * @param emailAddressInfos EmailAddressInfoList
	 * @return boolean
	 */
    public static final boolean isEditableEmailId(EmailAddressInfo emailAddressInfo, EmailAddressInfoList emailAddressInfos) {
        boolean[] iuaFlags = ParkingResources.getEmailAccessTypes(emailAddressInfo.getMailTypeId());
        if (GenericValidator.isBlankOrNull(emailAddressInfo.getEmailAddress())) {
            if (iuaFlags[2]) return true; else if (iuaFlags[0]) {
                int emailTypeInstances = 0;
                for (int j = 0; j < emailAddressInfos.size(); j++) {
                    if (emailAddressInfos.getEmailAddressInfo(j).getMailTypeId() == emailAddressInfo.getMailTypeId()) emailTypeInstances++;
                }
                if (emailTypeInstances < 2) return true;
            }
        } else if (iuaFlags[1]) return true;
        return false;
    }

    /**
	 * Addable rmail is editable when partially/fully blank
	 * Updatable rmail is editable when partially/fully not blank
	 * Insertable rmail is editable when partially/fully blank and the only instance of that email type
	 *
	 * @param rmailAddressInfo RmailAddressInfo
	 * @param rmailAddressInfos RmailAddressInfoList
	 * @return boolean
	 */
    public static final boolean isEditableRmailId(RmailAddressInfo rmailAddressInfo, RmailAddressInfoList rmailAddressInfos) {
        boolean[] iuaFlags = ParkingResources.getRmailAccessTypes(rmailAddressInfo.getMailTypeId());
        if (GenericValidator.isBlankOrNull(rmailAddressInfo.getAddressLine1()) || GenericValidator.isBlankOrNull(rmailAddressInfo.getCity()) || GenericValidator.isBlankOrNull(rmailAddressInfo.getZipCode())) {
            if (iuaFlags[2]) return true; else if (iuaFlags[0]) {
                int rmailTypeInstances = 0;
                for (int j = 0; j < rmailAddressInfos.size(); j++) {
                    if (rmailAddressInfos.getRmailAddressInfo(j).getMailTypeId() == rmailAddressInfo.getMailTypeId()) rmailTypeInstances++;
                }
                if (rmailTypeInstances < 2) return true;
            }
        } else if (iuaFlags[1] && (!GenericValidator.isBlankOrNull(rmailAddressInfo.getAddressLine1()) || !GenericValidator.isBlankOrNull(rmailAddressInfo.getCity()) || !GenericValidator.isBlankOrNull(rmailAddressInfo.getZipCode()))) return true;
        return false;
    }

    /**
	 *
	 * @param customerId int
	 * @return CustomerInfo
	 */
    public static final CustomerInfo getCustomerInfo(int customerId) {
        DbGeneralAccess db = new DbGeneralAccess();
        CustomerInfo customerInfo = db.getCustomerInfo(customerId, ParkingResources.getEmailTypeIds(), ParkingResources.getRmailTypeIds());
        EmailAddressInfoList emailAddressInfos = customerInfo.getEmailAddressInfos();
        for (int i = 0; i < emailAddressInfos.size(); i++) {
            if (emailAddressInfos.getEmailAddressInfo(i).getEmailId() < 1) {
                if (!isEditableEmailId(emailAddressInfos.getEmailAddressInfo(i), emailAddressInfos)) emailAddressInfos.remove(i--);
            }
        }
        RmailAddressInfoList rmailAddressInfos = customerInfo.getRmailAddressInfos();
        ListIterator<MailingAddressInfo> iterator = rmailAddressInfos.listIterator();
        RmailAddressInfo rmailAddressInfo;
        while (iterator.hasNext()) {
            rmailAddressInfo = (RmailAddressInfo) iterator.next();
            if (rmailAddressInfo.getRmailId() < 1) {
                if (!isEditableRmailId(rmailAddressInfo, rmailAddressInfos)) {
                    iterator.remove();
                } else {
                    db.getRmailTypeInfo(rmailAddressInfo);
                }
            }
        }
        return customerInfo;
    }

    /**
	 *
	 * NOTE: An email would be sent even if no update is actually performed
	 * the customer is kind of given the impression that the update was
	 * performed. There is no real way to find out if an update was actually
	 * performed or not in the functionality used. It is assumed that
	 * this function is only called when the validation has passed an update.

	 * @param customerInfo CustomerInfo
	 * @return ActionMessages
	 */
    public static final ActionMessages updateCustomerInfo(CustomerInfo customerInfo) {
        ActionMessages errors = new ActionMessages();
        DbGeneralAccess db = new DbGeneralAccess();
        EmailAddressInfo origEmailAddress = db.getCustomerEmailAddressInfo(customerInfo.getCustomerId(), ParkingConstants.getInstance().getEmailAddressRegularId());
        errors.add(updateCustomerBasicInfo(customerInfo));
        errors.add(updateCustomerEmailAddressInfos(customerInfo.getCustomerId(), customerInfo.getEmailAddressInfos()));
        errors.add(updateCustomerRmailAddressInfos(customerInfo.getCustomerId(), customerInfo.getRmailAddressInfos()));
        EmailAddressInfo newEmailAddress = db.getCustomerEmailAddressInfo(customerInfo.getCustomerId(), ParkingConstants.getInstance().getEmailAddressRegularId());
        sendCustomerInfoUpdateEmail(customerInfo.getCustomerId(), origEmailAddress.getEmailAddress(), newEmailAddress.getEmailAddress());
        return errors;
    }

    /**
	 *
	 * @param customerBasicInfo CustomerBasicInfo
	 * @return ActionMessages
	 */
    public static final ActionMessages updateCustomerBasicInfo(CustomerBasicInfo customerBasicInfo) {
        ActionMessages errors = new ActionMessages();
        DbAccountAccess db = new DbAccountAccess();
        boolean updatePhones = false;
        boolean updateMain = false;
        CustomerBasicInfo origCustomerBasicInfo = db.getDbGeneralAccess().getCustomerBasicInfo(customerBasicInfo.getCustomerId());
        if (!GenericValidator.isBlankOrNull(customerBasicInfo.getDriversId()) && !customerBasicInfo.getDriversId().equalsIgnoreCase(origCustomerBasicInfo.getDriversId())) {
            updateMain = true;
            origCustomerBasicInfo.setDriversId(customerBasicInfo.getDriversId());
        }
        if (!GenericValidator.isBlankOrNull(customerBasicInfo.getFirstPhoneNo()) && !customerBasicInfo.getFirstPhoneNo().equalsIgnoreCase(origCustomerBasicInfo.getFirstPhoneNo())) {
            updatePhones = true;
            origCustomerBasicInfo.setFirstPhoneNo(customerBasicInfo.getFirstPhoneNo());
        }
        if (!GenericValidator.isBlankOrNull(customerBasicInfo.getThirdPhoneNo()) && !customerBasicInfo.getThirdPhoneNo().equalsIgnoreCase(origCustomerBasicInfo.getThirdPhoneNo())) {
            updatePhones = true;
            origCustomerBasicInfo.setThirdPhoneNo(customerBasicInfo.getThirdPhoneNo());
        }
        if (updatePhones && updateMain) {
            db.updateCustomerBasicInfo(origCustomerBasicInfo);
        } else if (updatePhones) {
            db.updateCustomerPhone(origCustomerBasicInfo.getCustomerId(), origCustomerBasicInfo.getFirstPhoneNo(), origCustomerBasicInfo.getSecondPhoneNo(), origCustomerBasicInfo.getThirdPhoneNo(), origCustomerBasicInfo.getFourthPhoneNo());
        } else if (updateMain) {
            db.updateCustomerBasicInfoMain(origCustomerBasicInfo);
        }
        return errors;
    }

    /**
	 *
	 * @param customerId int
	 * @param emailAddressInfos EmailAddressInfoList
	 * @return ActionMessages
	 */
    public static final ActionMessages updateCustomerEmailAddressInfos(int customerId, EmailAddressInfoList emailAddressInfos) {
        ActionMessages errors = new ActionMessages();
        boolean requiresUpdate, requiresInsertion, requiresAddition;
        DbAccountAccess db = new DbAccountAccess();
        EmailAddressInfoList origEmailAddressInfos;
        EmailAddressInfo emailAddressInfo;
        EmailAddressInfo origEmailAddressInfo;
        for (int i = 0; i < emailAddressInfos.size(); i++) {
            emailAddressInfo = emailAddressInfos.getEmailAddressInfo(i);
            if (!GenericValidator.isBlankOrNull(emailAddressInfo.getEmailAddress()) && GenericValidator.isEmail(emailAddressInfo.getEmailAddress())) {
                requiresUpdate = false;
                requiresAddition = true;
                requiresInsertion = true;
                origEmailAddressInfos = db.getDbGeneralAccess().getCustomerEmailAddressInfos(customerId);
                for (int j = 0; j < origEmailAddressInfos.size(); j++) {
                    origEmailAddressInfo = origEmailAddressInfos.getEmailAddressInfo(j);
                    if (emailAddressInfo.getEmailId() == origEmailAddressInfo.getEmailId()) {
                        requiresAddition = false;
                        requiresInsertion = false;
                        if (!emailAddressInfo.getEmailAddress().equalsIgnoreCase(origEmailAddressInfo.getEmailAddress())) {
                            requiresUpdate = true;
                        }
                    } else if (emailAddressInfo.getMailTypeId() == origEmailAddressInfo.getMailTypeId()) {
                        requiresInsertion = false;
                    }
                }
                if (requiresInsertion && ParkingResources.isInsertableEmailType(emailAddressInfo.getMailTypeId())) {
                    db.createCustomerEmailAddressInfo(customerId, emailAddressInfo, "Online Account Insertion");
                } else if (requiresAddition && ParkingResources.isAddableEmailType(emailAddressInfo.getMailTypeId())) {
                    db.createCustomerEmailAddressInfo(customerId, emailAddressInfo, "Online Account Addition");
                } else if (requiresUpdate && ParkingResources.isUpdatableEmailType(emailAddressInfo.getMailTypeId())) {
                    db.updateCustomerEmailAddressInfo(customerId, emailAddressInfo, "Online Account Update");
                } else if (requiresUpdate || requiresInsertion || requiresAddition) {
                    errors.add("errors.emailAddressInfos.emailAddress[" + i + "]", new ActionMessage("errors.emailAddress.update", new Object[] { emailAddressInfo.getMailTypeDescription() }));
                }
            } else {
            }
        }
        return errors;
    }

    /**
	 *
	 * @param customerId int
	 * @param rmailAddressInfos RmailAddressInfoList
	 * @return ActionMessages
	 */
    public static final ActionMessages updateCustomerRmailAddressInfos(int customerId, RmailAddressInfoList rmailAddressInfos) {
        ActionMessages errors = new ActionMessages();
        boolean requiresUpdate, requiresInsertion, requiresAddition;
        DbAccountAccess db = new DbAccountAccess();
        RmailAddressInfoList origRmailAddressInfos;
        RmailAddressInfo rmailAddressInfo;
        RmailAddressInfo origRmailAddressInfo;
        for (int i = 0; i < rmailAddressInfos.size(); i++) {
            rmailAddressInfo = rmailAddressInfos.getRmailAddressInfo(i);
            if (!GenericValidator.isBlankOrNull(rmailAddressInfo.getAddressLine1()) && !GenericValidator.isBlankOrNull(rmailAddressInfo.getCity()) && !GenericValidator.isBlankOrNull(rmailAddressInfo.getState()) && !GenericValidator.isBlankOrNull(rmailAddressInfo.getZipCode())) {
                requiresUpdate = false;
                requiresAddition = true;
                requiresInsertion = true;
                origRmailAddressInfos = db.getDbGeneralAccess().getCustomerRmailAddressInfos(customerId);
                for (int j = 0; j < origRmailAddressInfos.size(); j++) {
                    origRmailAddressInfo = origRmailAddressInfos.getRmailAddressInfo(j);
                    if (rmailAddressInfo.getRmailId() == origRmailAddressInfo.getRmailId()) {
                        requiresAddition = false;
                        requiresInsertion = false;
                        if (!rmailAddressInfo.getRmailingAddress().equalsIgnoreCase(origRmailAddressInfo.getRmailingAddress())) {
                            requiresUpdate = true;
                        }
                    } else if (rmailAddressInfo.getMailTypeId() == origRmailAddressInfo.getMailTypeId()) {
                        requiresInsertion = false;
                    }
                }
                if (requiresInsertion && ParkingResources.isInsertableRmailType(rmailAddressInfo.getMailTypeId())) {
                    db.createCustomerMailingAddressInfo(customerId, rmailAddressInfo, "Online Account Insertion");
                } else if (requiresAddition && ParkingResources.isAddableRmailType(rmailAddressInfo.getMailTypeId())) {
                    db.createCustomerMailingAddressInfo(customerId, rmailAddressInfo, "Online Account Addition");
                } else if (requiresUpdate && ParkingResources.isUpdatableRmailType(rmailAddressInfo.getMailTypeId())) {
                    db.updateCustomerMailingAddressInfo(customerId, rmailAddressInfo, "Online Account Update");
                } else if (requiresUpdate || requiresInsertion || requiresAddition) {
                    errors.add("errors.rmailAddressInfos.rmailAddress[" + i + "]", new ActionMessage("errors.rmailAddress.update", new Object[] { rmailAddressInfo.getMailTypeDescription() }));
                }
            } else {
            }
        }
        return errors;
    }

    /**
	 *
	 * @param customerIdPp int
	 * @return WaitlistInfo
	 public static com.hisham.powerpark.util.WaitlistInfo
	 getWaitlistInfo(int customerIdPp)
	 {
	 //    DbGeneralAccess database = DbGeneralAccess.getInstance();
	 return new DbGeneralAccess().getWaitlistInfo(customerIdPp);
	 }
	 */
    public static CustomerStatusInfo getCustomerStatusInfo(String loginId) {
        return new CustomerStatusInfo(new DbGeneralAccess().getCustomerIdPp(loginId), loginId);
    }
}
