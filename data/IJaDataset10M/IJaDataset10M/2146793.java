package org.openxava.qamanager.validators;

import org.openxava.util.Messages;
import org.openxava.util.XavaPreferences;
import org.openxava.validators.IValidator;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Janesh Kodikara
 */
public class ResourceDetailValidator implements IValidator {

    private Date dateJoined;

    private Date dateResigned;

    private Date dateRejoined;

    private Date dateOfBirth;

    private Date dateConfirmation;

    private Date dateLastPromotion;

    private String telephoneHome;

    private String telephoneMobile;

    private int legalMinimumAgeLimit;

    private int legalMaximumAgeLimit;

    public ResourceDetailValidator() {
        legalMaximumAgeLimit = XavaPreferences.getInstance().getMaximumAgeLimit();
        legalMinimumAgeLimit = XavaPreferences.getInstance().getMinimumAgeLimit();
    }

    public void validate(Messages errors) throws Exception {
        XavaDateValidator validator = new XavaDateValidator();
        validator.isStartDateAfter(errors, "dateOfBirth", dateOfBirth, "dateJoined", dateJoined);
        validator.isStartDateAfter(errors, "dateOfBirth", dateOfBirth, "dateResigned", dateResigned);
        validator.isStartDateAfter(errors, "dateOfBirth", dateOfBirth, "dateRejoined", dateRejoined);
        validator.isStartDateAfter(errors, "dateOfBirth", dateOfBirth, "dateConfirmation", dateConfirmation);
        validator.isStartDateAfter(errors, "dateOfBirth", dateOfBirth, "dateLastPromotion", dateLastPromotion);
        validator.isStartDateAfter(errors, "dateJoined", dateJoined, "dateResigned", dateResigned);
        validator.isStartDateAfter(errors, "dateJoined", dateJoined, "dateRejoined", dateRejoined);
        validator.isStartDateAfter(errors, "dateJoined", dateJoined, "dateConfirmation", dateConfirmation);
        validator.isStartDateAfter(errors, "dateJoined", dateJoined, "dateLastPromotion", dateLastPromotion);
        validator.isStartDateAfter(errors, "dateResigned", dateResigned, "dateRejoined", dateRejoined);
        validator.isSameDate(errors, "dateResigned", dateResigned, "dateRejoined", dateRejoined);
        validator.isSameDate(errors, "dateJoined", dateJoined, "dateConfirmation", dateConfirmation);
        validator.isSameDate(errors, "dateJoined", dateJoined, "dateLastPromotion", dateLastPromotion);
        validator.isFutureDate(errors, "dateJoined", dateJoined);
        validator.isFutureDate(errors, "dateResigned", dateResigned);
        validator.isFutureDate(errors, "dateRejoined", dateRejoined);
        validator.isFutureDate(errors, "dateConfirmation", dateConfirmation);
        validator.isFutureDate(errors, "dateLastPromotion", dateLastPromotion);
        checkAvailability(errors, "dateResigned", dateResigned, "dateJoined", dateJoined);
        checkAvailability(errors, "dateRejoined", dateRejoined, "dateJoined", dateJoined);
        checkAvailability(errors, "dateRejoined", dateRejoined, "dateResigned", dateResigned);
        checkAvailability(errors, "dateConfirmation", dateConfirmation, "dateJoined", dateJoined);
        checkAvailability(errors, "dateLastPromotion", dateLastPromotion, "dateJoined", dateJoined);
        if (dateJoined == null && dateRejoined == null && dateResigned == null) {
            validateAge(errors, "dateOfBirth", dateOfBirth);
        }
        if (dateResigned != null && dateRejoined == null) {
            validator.isStartDateAfter(errors, "dateResigned", dateResigned, "dateLastPromotion", dateLastPromotion);
            validator.isStartDateAfter(errors, "dateResigned", dateResigned, "dateConfirmation", dateConfirmation);
        }
        validateAge(errors, dateOfBirth, "dateJoined", dateJoined);
        validateAge(errors, dateOfBirth, "dateResigned", dateResigned);
        validateAge(errors, dateOfBirth, "dateRejoined", dateRejoined);
        validateAge(errors, dateOfBirth, "dateConfirmation", dateConfirmation);
        validateAge(errors, dateOfBirth, "dateLastPromotion", dateLastPromotion);
        validateDuplicate(errors, "telephoneHome", telephoneHome, "telephoneMobile", telephoneMobile);
    }

    private void validateAge(Messages errors, Date dateOfBirth, String fieldName, Date lowerDate) {
        Calendar upperCalendar = Calendar.getInstance();
        Calendar lowerCalendar = Calendar.getInstance();
        if (dateOfBirth == null || lowerDate == null) return;
        lowerCalendar.setTime(lowerDate);
        upperCalendar.setTime(dateOfBirth);
        upperCalendar.add(Calendar.YEAR, legalMinimumAgeLimit);
        if (upperCalendar.getTimeInMillis() - lowerCalendar.getTimeInMillis() > 0) {
            errors.add("min_age_limit_error", legalMinimumAgeLimit, fieldName);
            return;
        }
        upperCalendar.setTime(dateOfBirth);
        upperCalendar.add(Calendar.YEAR, legalMaximumAgeLimit);
        if (upperCalendar.getTimeInMillis() - lowerCalendar.getTimeInMillis() < 0) {
            errors.add("max_age_limit_error", legalMaximumAgeLimit, fieldName);
            return;
        }
    }

    private void validateAge(Messages errors, String fieldName, Date dateOfBirth) {
        Calendar upperCalendar = Calendar.getInstance();
        Calendar lowerCalendar = Calendar.getInstance();
        Date today = new Date();
        if (dateOfBirth == null) return;
        lowerCalendar.setTime(today);
        upperCalendar.setTime(dateOfBirth);
        upperCalendar.add(Calendar.YEAR, legalMinimumAgeLimit);
        if (upperCalendar.getTimeInMillis() - lowerCalendar.getTimeInMillis() > 0) {
            errors.add("min_age_limit_error", legalMinimumAgeLimit, fieldName);
            return;
        }
        upperCalendar.setTime(dateOfBirth);
        upperCalendar.add(Calendar.YEAR, legalMaximumAgeLimit);
        if (upperCalendar.getTimeInMillis() - lowerCalendar.getTimeInMillis() < 0) {
            errors.add("max_age_limit_error", legalMaximumAgeLimit, fieldName);
            return;
        }
    }

    private void validateDuplicate(Messages errors, String firstFieldName, String firstNumber, String secondFieldName, String secondNumber) {
        if (firstNumber == null) return;
        if (secondNumber == null) return;
        if (firstNumber.trim().length() == 0 || secondNumber.trim().length() == 0) return;
        if (firstNumber.equalsIgnoreCase(secondNumber)) {
            errors.add("duplicate_phone_error", firstFieldName, secondFieldName);
        }
    }

    private void checkAvailability(Messages errors, String firstFieldName, Date firstDate, String seconddFieldName, Date secondDate) {
        if (firstDate != null && secondDate == null) {
            errors.add("cannot_set_without_date_error", firstFieldName, seconddFieldName);
        }
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Date getDateResigned() {
        return dateResigned;
    }

    public void setDateResigned(Date dateResigned) {
        this.dateResigned = dateResigned;
    }

    public Date getDateRejoined() {
        return dateRejoined;
    }

    public void setDateRejoined(Date dateRejoined) {
        this.dateRejoined = dateRejoined;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateConfirmation() {
        return dateConfirmation;
    }

    public void setDateConfirmation(Date dateConfirmation) {
        this.dateConfirmation = dateConfirmation;
    }

    public Date getDateLastPromotion() {
        return dateLastPromotion;
    }

    public void setDateLastPromotion(Date dateLastPromotion) {
        this.dateLastPromotion = dateLastPromotion;
    }

    public String getTelephoneHome() {
        return telephoneHome;
    }

    public void setTelephoneHome(String telephoneHome) {
        this.telephoneHome = telephoneHome;
    }

    public String getTelephoneMobile() {
        return telephoneMobile;
    }

    public void setTelephoneMobile(String telephoneMobile) {
        this.telephoneMobile = telephoneMobile;
    }

    public int getLegalMinimumAgeLimit() {
        return legalMinimumAgeLimit;
    }

    public void setLegalMinimumAgeLimit(int legalMinimumAgeLimit) {
        this.legalMinimumAgeLimit = legalMinimumAgeLimit;
    }

    public int getLegalMaximumAgeLimit() {
        return legalMaximumAgeLimit;
    }

    public void setLegalMaximumAgeLimit(int legalMaximumAgeLimit) {
        this.legalMaximumAgeLimit = legalMaximumAgeLimit;
    }
}
