package org.eaasyst.eaa.forms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.ValidatorActionForm;
import org.eaasyst.eaa.data.impl.SecurityConfigurationFactory;
import org.eaasyst.eaa.security.User;
import org.eaasyst.eaa.syst.data.persistent.Contact;
import org.eaasyst.eaa.syst.data.persistent.Person;
import org.eaasyst.eaa.syst.data.persistent.SecurityQuestion;
import org.eaasyst.eaa.syst.data.transients.SecurityConfiguration;
import org.eaasyst.eaa.utils.StringUtils;

/**
 * <p>A Struts action form for the user profile edit application.</p>
 *
 * @version 2.9.1
 * @author Jeff Chilton
 */
public class UserProfileEditForm extends ValidatorActionForm {

    private static final long serialVersionUID = 1;

    private String userId = null;

    private String personId = null;

    private String useName = null;

    private String fullName = null;

    private String namePrefix = null;

    private String firstName = null;

    private String middleName = null;

    private String lastName = null;

    private String nameSuffix = null;

    private String primaryEmail = null;

    private List contacts = new ArrayList();

    private List personAttributes = new ArrayList();

    private List userAttributes = new ArrayList();

    private String signature = null;

    private List securityQuestions = new ArrayList();

    private List questionOptions = new ArrayList();

    private String newUser = "true";

    private String questionEditable = "false";

    private String passwordManageable = "false";

    /**
	 * <p>Constructs a new <code>UserProfileEditForm</code>.</p>
	 *
	 * @since Eaasy Street 2.0.4
	 */
    public UserProfileEditForm() {
        super();
    }

    /**
	 * <p>Constructs a new <code>UserProfileEditForm</code> using the
	 * parameters provided.</p>
	 *
	 * @since Eaasy Street 2.0.4
	 */
    public UserProfileEditForm(User user) {
        super();
        userId = user.getUserId();
        Person person = user.getPerson();
        if (person != null) {
            personId = person.getPersonId() + "";
            useName = person.getUseName();
            fullName = person.getFullName();
            namePrefix = person.getNamePrefix();
            firstName = person.getFirstName();
            middleName = person.getMiddleName();
            lastName = person.getLastName();
            nameSuffix = person.getNameSuffix();
            if (!StringUtils.nullOrBlank(person.getPrimaryEmailContactId())) {
                Contact contact = person.getContact(person.getPrimaryEmailContactId());
                primaryEmail = contact.getDisplayValue();
            }
            newUser = "false";
        }
        contacts = getContacts(person);
        personAttributes = getPersonAttributes(person);
        signature = user.getSignature();
        userAttributes = getUserAttributes(user);
        if ("true".equalsIgnoreCase(passwordManageable)) {
            securityQuestions = getSecurityQuestions(user);
        }
    }

    /**
	 * <p>Builds the person contacts from the source data.</p>
	 *
	 * @param person the person object
	 * @return the person contacts
	 * @since Eaasy Street 2.0.4
	 */
    private static List getContacts(Person person) {
        List contacts = new ArrayList();
        SecurityConfiguration config = SecurityConfigurationFactory.getConfiguration();
        List configured = config.getContactIds();
        if (configured != null && !configured.isEmpty()) {
            Iterator i = configured.iterator();
            while (i.hasNext()) {
                String contactId = (String) i.next();
                Contact contact = null;
                if (person != null) {
                    contact = person.getContact(contactId);
                }
                if (contact != null) {
                    contacts.add(contact);
                } else {
                    contacts.add(new Contact(null, contactId));
                }
            }
        }
        return contacts;
    }

    /**
	 * <p>Builds the person attributes from the source data.</p>
	 *
	 * @param person the person object
	 * @return the person attributes
	 * @since Eaasy Street 2.0.4
	 */
    private static List getPersonAttributes(Person person) {
        List attributes = new ArrayList();
        SecurityConfiguration config = SecurityConfigurationFactory.getConfiguration();
        List configured = config.getPersonAttributeNames();
        if (configured != null && !configured.isEmpty()) {
            Iterator i = configured.iterator();
            while (i.hasNext()) {
                String name = (String) i.next();
                LabelValueBean lvb = new LabelValueBean("label." + name, null);
                if (person != null) {
                    lvb.setValue((String) person.getAttribute(name));
                }
                attributes.add(lvb);
            }
        }
        return attributes;
    }

    /**
	 * <p>Builds the user attributes from the source data.</p>
	 *
	 * @param user the user object
	 * @return the user attributes
	 * @since Eaasy Street 2.0.4
	 */
    private List getUserAttributes(User user) {
        List attributes = new ArrayList();
        SecurityConfiguration config = SecurityConfigurationFactory.getConfiguration();
        passwordManageable = config.isPasswordManageable() + "";
        List configured = config.getEditAttributeNames();
        if (configured != null && !configured.isEmpty()) {
            Iterator i = configured.iterator();
            while (i.hasNext()) {
                String name = (String) i.next();
                LabelValueBean lvb = new LabelValueBean("label." + name, null);
                if (user != null) {
                    lvb.setValue((String) user.getAttribute(name));
                }
                attributes.add(lvb);
            }
        }
        return attributes;
    }

    /**
	 * <p>Builds the security questions from the source data.</p>
	 *
	 * @param user the user object
	 * @return the security questions
	 * @since Eaasy Street 2.0.4
	 */
    private List getSecurityQuestions(User user) {
        List questions = new ArrayList();
        SecurityConfiguration config = SecurityConfigurationFactory.getConfiguration();
        int totalQuestions = config.getPromptQuestionsUsed();
        if (totalQuestions > 0) {
            List availableQuestions = config.getAvailablePromptQuestions();
            int availableCt = 0;
            if (availableQuestions != null) {
                availableCt = availableQuestions.size();
            }
            if (totalQuestions == availableCt) {
                for (int i = 0; i < totalQuestions; i++) {
                    String thisQuestion = (String) availableQuestions.get(i);
                    SecurityQuestion sq = new SecurityQuestion(thisQuestion, getAnswer(thisQuestion, user));
                    questions.add(sq);
                }
            } else {
                questionEditable = "true";
                if (availableCt > 0) {
                    questionOptions = new ArrayList();
                    Iterator i = availableQuestions.iterator();
                    while (i.hasNext()) {
                        String thisQuestion = (String) i.next();
                        LabelValueBean thisOption = new LabelValueBean(thisQuestion, thisQuestion);
                        questionOptions.add(thisOption);
                    }
                }
                int profileCt = 0;
                List profileQuestions = new ArrayList(user.getSecurityQuestions());
                if (profileQuestions != null) {
                    profileCt = profileQuestions.size();
                }
                for (int i = 0; i < totalQuestions; i++) {
                    SecurityQuestion sq = new SecurityQuestion();
                    if (i < profileCt) {
                        sq = (SecurityQuestion) profileQuestions.get(i);
                    }
                    questions.add(sq);
                }
            }
        }
        return questions;
    }

    /**
	 * <p>Finds the answer to the question provided.</p>
	 *
	 * @param question the specific question
	 * @param profile the UserProfile object
	 * @return the answer to the question
	 * @since Eaasy Street 2.0.4
	 */
    private static String getAnswer(String question, User profile) {
        String answer = null;
        if (profile.getSecurityQuestions() != null) {
            List questions = new ArrayList(profile.getSecurityQuestions());
            Iterator i = questions.iterator();
            while (i.hasNext()) {
                SecurityQuestion thisQuestion = (SecurityQuestion) i.next();
                if (question.equals(thisQuestion.getQuestion())) {
                    answer = thisQuestion.getAnswer();
                }
            }
        }
        return answer;
    }

    /**
	 * Returns the contacts.
	 * @return List
	 */
    public List getContacts() {
        return contacts;
    }

    /**
	 * Returns the firstName.
	 * @return String
	 */
    public String getFirstName() {
        return firstName;
    }

    /**
	 * Returns the fullName.
	 * @return String
	 */
    public String getFullName() {
        return fullName;
    }

    /**
	 * Returns the lastName.
	 * @return String
	 */
    public String getLastName() {
        return lastName;
    }

    /**
	 * Returns the middleName.
	 * @return String
	 */
    public String getMiddleName() {
        return middleName;
    }

    /**
	 * Returns the namePrefix.
	 * @return String
	 */
    public String getNamePrefix() {
        return namePrefix;
    }

    /**
	 * Returns the nameSuffix.
	 * @return String
	 */
    public String getNameSuffix() {
        return nameSuffix;
    }

    /**
	 * Returns the newUser.
	 * @return String
	 */
    public String getNewUser() {
        return newUser;
    }

    /**
	 * Returns the passwordManageable.
	 * @return String
	 */
    public String getPasswordManageable() {
        return passwordManageable;
    }

    /**
	 * Returns the personAttributes.
	 * @return List
	 */
    public List getPersonAttributes() {
        return personAttributes;
    }

    /**
	 * Returns the personId.
	 * @return String
	 */
    public String getPersonId() {
        return personId;
    }

    /**
	 * Returns the primaryEmail.
	 * @return String
	 */
    public String getPrimaryEmail() {
        return primaryEmail;
    }

    /**
	 * Returns the questionEditable.
	 * @return String
	 */
    public String getQuestionEditable() {
        return questionEditable;
    }

    /**
	 * Returns the questionOptions.
	 * @return List
	 */
    public List getQuestionOptions() {
        return questionOptions;
    }

    /**
	 * Returns the securityQuestions.
	 * @return List
	 */
    public List getSecurityQuestions() {
        return securityQuestions;
    }

    /**
	 * Returns the signature.
	 * @return String
	 */
    public String getSignature() {
        return signature;
    }

    /**
	 * Returns the useName.
	 * @return String
	 */
    public String getUseName() {
        return useName;
    }

    /**
	 * Returns the userAttributes.
	 * @return List
	 */
    public List getUserAttributes() {
        return userAttributes;
    }

    /**
	 * Returns the userId.
	 * @return String
	 */
    public String getUserId() {
        return userId;
    }

    /**
	 * Sets the contacts.
	 * @param contacts The contacts to set
	 */
    public void setContacts(List contacts) {
        this.contacts = contacts;
    }

    /**
	 * Sets the firstName.
	 * @param firstName The firstName to set
	 */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
	 * Sets the fullName.
	 * @param fullName The fullName to set
	 */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
	 * Sets the lastName.
	 * @param lastName The lastName to set
	 */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
	 * Sets the middleName.
	 * @param middleName The middleName to set
	 */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
	 * Sets the namePrefix.
	 * @param namePrefix The namePrefix to set
	 */
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    /**
	 * Sets the nameSuffix.
	 * @param nameSuffix The nameSuffix to set
	 */
    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    /**
	 * Sets the newUser.
	 * @param newUser The newUser to set
	 */
    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    /**
	 * Sets the passwordManageable.
	 * @param passwordManageable The passwordManageable to set
	 */
    public void setPasswordManageable(String passwordManageable) {
        this.passwordManageable = passwordManageable;
    }

    /**
	 * Sets the personAttributes.
	 * @param personAttributes The personAttributes to set
	 */
    public void setPersonAttributes(List personAttributes) {
        this.personAttributes = personAttributes;
    }

    /**
	 * Sets the personId.
	 * @param personId The personId to set
	 */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
	 * Sets the primaryEmail.
	 * @param primaryEmail The primaryEmail to set
	 */
    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    /**
	 * Sets the questionEditable.
	 * @param questionEditable The questionEditable to set
	 */
    public void setQuestionEditable(String questionEditable) {
        this.questionEditable = questionEditable;
    }

    /**
	 * Sets the questionOptions.
	 * @param questionOptions The questionOptions to set
	 */
    public void setQuestionOptions(List questionOptions) {
        this.questionOptions = questionOptions;
    }

    /**
	 * Sets the securityQuestions.
	 * @param securityQuestions The securityQuestions to set
	 */
    public void setSecurityQuestions(List securityQuestions) {
        this.securityQuestions = securityQuestions;
    }

    /**
	 * Sets the signature.
	 * @param signature The signature to set
	 */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
	 * Sets the useName.
	 * @param useName The useName to set
	 */
    public void setUseName(String useName) {
        this.useName = useName;
    }

    /**
	 * Sets the userAttributes.
	 * @param userAttributes The userAttributes to set
	 */
    public void setUserAttributes(List userAttributes) {
        this.userAttributes = userAttributes;
    }

    /**
	 * Sets the userId.
	 * @param userId The userId to set
	 */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
