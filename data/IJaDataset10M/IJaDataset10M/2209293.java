package com.redtwitch.net.jemval;

import java.util.*;
import java.awt.event.*;

/**
 * This class is a wrapper for the validation factories.
 *  It is basically just a main that tests for one argument, the email
 *  to validate.  This class is intended to be extended and updated to 
 *  contain methods to scrub email lists, validate in batches, etc.
 *
 *  $Id: EmailValidator.java,v 1.4 2004/07/01 02:14:44 wilsong123 Exp $
 *  $Revision: 1.4 $
 * <br><br>Author:  <a href="mailto:glenn@redtwitch.com">Glenn Wilson</a>
 */
public class EmailValidator {

    private int percentComplete = 0;

    private List emailValidatorListeners;

    public EmailValidator() {
        percentComplete = 0;
        emailValidatorListeners = new ArrayList();
    }

    public void addEmailValidatorListener(EmailValidatorListener listener) {
        if (emailValidatorListeners == null) emailValidatorListeners = new ArrayList();
        emailValidatorListeners.add(listener);
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int in) {
        percentComplete = in;
        alertListeners();
    }

    private void alertListeners() {
        if (emailValidatorListeners != null) {
            for (Iterator i = emailValidatorListeners.iterator(); i.hasNext(); ) {
                EmailValidatorListener listener = (EmailValidatorListener) i.next();
                listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, EmailValidatorListener.PERCENT_COMPLETED_UPDATED));
            }
        }
    }

    /** returns a List of EmailCheck objects based on the
     *	List of Strings (emails) passed in.
     */
    public List validateEmails(List emails) {
        ArrayList results = new ArrayList();
        int current = 0;
        try {
            int max = emails.size();
            EmailValidationFactory evf = new EmailValidationFactory();
            EmailValidation validation = evf.getEmailValidation();
            if (emails != null) for (Iterator i = emails.iterator(); i.hasNext(); ) {
                String email = (String) i.next();
                EmailCheck check = new EmailCheck(email);
                validation.validate(check);
                current++;
                setPercentComplete((new Double((100 * current) / max)).intValue());
                results.add(check);
            }
        } catch (Exception e) {
            System.out.println("Error checking emails: " + e.toString());
        }
        return results;
    }

    /** given a list of emails as strings, this function will return a
     *	list of Strings that are the emails that passed ALL THREE TESTS
     */
    public List scrubEmails(List emails) {
        List checks = validateEmails(emails);
        ArrayList valid_emails = new ArrayList();
        if (checks != null) for (Iterator i = checks.iterator(); i.hasNext(); ) {
            EmailCheck check = (EmailCheck) i.next();
            if (check.getFormCheck() && check.getDNSCheck() && check.getSendCheck()) valid_emails.add(check.getEmail());
        }
        return valid_emails;
    }

    /** 
     *  This is the method that is given a single String, an email
     *  address, and that is passed to the validation factories for validation.
     */
    public static void main(String[] args) {
        System.out.println("******************************");
        String checkme1 = "glenn@redtwitch.com";
        String checkme2 = "noawiejf@oaiwefojijw.com";
        String checkme3 = "i don't exist@nowhere com";
        String checkme4 = "aOWieg8927";
        ArrayList checker = new ArrayList();
        checker.add(checkme1);
        checker.add(checkme2);
        checker.add(checkme3);
        checker.add(checkme4);
        EmailValidator v = new EmailValidator();
        List scrubbed = v.scrubEmails(checker);
        System.out.println("Before scrubbing:");
        for (Iterator i = checker.iterator(); i.hasNext(); ) {
            System.out.println("" + (String) i.next());
        }
        System.out.println("After scrubbing:");
        for (Iterator j = scrubbed.iterator(); j.hasNext(); ) {
            System.out.println("" + (String) j.next());
        }
    }
}
