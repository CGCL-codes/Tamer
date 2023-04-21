package org.tolven.security;

import java.util.List;
import org.tolven.security.entity.SecurityQuestion;

public interface PasswordRecoveryRemote {

    public byte[] backupLoginPassword(TolvenPerson tolvenPerson, char[] password, String securityQuestion, char[] securityQuestionAnswer);

    public void addSecurityQuestion(SecurityQuestion securityQuestion);

    /**
     * Return the tolven persistent securityQuestions
     * @return SecurityQuestion collection
     */
    public List<SecurityQuestion> findSecurityQuestions(String purpose);

    public SecurityQuestion findSecurityQuestion(String question, String purpose);

    /**
     * Clear the supplied securityQuestions from the database.
     */
    public void clearSecurityQuestions(List<SecurityQuestion> securityQuestions);

    /**
     * Update (persist or merge) the supplied securityQuestions into the database
     */
    public List<SecurityQuestion> updateSecurityQuestions(List<SecurityQuestion> securityQuestions, List<SecurityQuestion> securityQuestionsToDelete);
}
