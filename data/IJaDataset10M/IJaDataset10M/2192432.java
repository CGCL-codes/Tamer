package org.didicero.base.service;

/**
 * Web service delegator for {@link org.didicero.base.service.ContentServiceBean}.
 *
 * @see org.didicero.base.service.ContentServiceBean
 */
public class ContentServiceDelegate extends org.didicero.base.ServiceDelegateBase {

    /**
     * Default constructor
     */
    public ContentServiceDelegate() {
        super();
    }

    /**
     * Constructor setting the envirinment properties.
     *
     * @param properties
     */
    public ContentServiceDelegate(java.util.Properties properties) {
        super(properties);
    }

    /**
     * Gets an instance of {@link org.didicero.base.service.ContentServiceRemote}
     */
    private final org.didicero.base.service.ContentServiceRemote getContentServiceRemote() throws javax.naming.NamingException {
        return org.didicero.base.ServiceLocator.getInstance().get_org_didicero_base_service_ContentServiceBean_Remote(getProperties());
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#createQuestion(org.didicero.base.entity.Question)
     *
     * Call the session bean operation using appropriate view type
     */
    public org.didicero.base.entity.Question createQuestion(org.didicero.base.entity.Question aQuestion) throws org.didicero.base.service.ContentServiceException {
        try {
            return getContentServiceRemote().createQuestion(aQuestion);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.createQuestion(org.didicero.base.entity.Question aQuestion)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#addAnswerToQuestion(org.didicero.base.entity.Question, org.didicero.base.entity.Answer, org.didicero.base.entity.Reason)
     *
     * Call the session bean operation using appropriate view type
     */
    public org.didicero.base.entity.Question addAnswerToQuestion(org.didicero.base.entity.Question aQuestion, org.didicero.base.entity.Answer anAnswer, org.didicero.base.entity.Reason aReason) throws org.didicero.base.service.ContentServiceException {
        try {
            return getContentServiceRemote().addAnswerToQuestion(aQuestion, anAnswer, aReason);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.addAnswerToQuestion(org.didicero.base.entity.Question aQuestion, org.didicero.base.entity.Answer anAnswer, org.didicero.base.entity.Reason aReason)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#deleteQuestion(org.didicero.base.entity.Question)
     *
     * Call the session bean operation using appropriate view type
     */
    public void deleteQuestion(org.didicero.base.entity.Question aQuestion) throws org.didicero.base.service.ContentServiceException {
        try {
            getContentServiceRemote().deleteQuestion(aQuestion);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.deleteQuestion(org.didicero.base.entity.Question aQuestion)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#findQuestions(org.didicero.base.entity.Question)
     *
     * Call the session bean operation using appropriate view type
     */
    public org.didicero.base.entity.Question[] findQuestions(org.didicero.base.entity.Question questionCriteria) throws org.didicero.base.service.ContentServiceException {
        try {
            return getContentServiceRemote().findQuestions(questionCriteria);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.findQuestions(org.didicero.base.entity.Question questionCriteria)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#getQuestionById(java.lang.Long)
     *
     * Call the session bean operation using appropriate view type
     */
    public org.didicero.base.entity.Question getQuestionById(java.lang.Long key) throws org.didicero.base.service.ContentServiceException {
        try {
            return getContentServiceRemote().getQuestionById(key);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.getQuestionById(java.lang.Long key)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#removeAnswerFromQuestion(org.didicero.base.entity.Answer, org.didicero.base.entity.Question)
     *
     * Call the session bean operation using appropriate view type
     */
    public org.didicero.base.entity.Question removeAnswerFromQuestion(org.didicero.base.entity.Answer anAnswer, org.didicero.base.entity.Question aQuestion) throws org.didicero.base.service.ContentServiceException {
        try {
            return getContentServiceRemote().removeAnswerFromQuestion(anAnswer, aQuestion);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.removeAnswerFromQuestion(org.didicero.base.entity.Answer anAnswer, org.didicero.base.entity.Question aQuestion)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#updateQuestion(org.didicero.base.entity.Question)
     *
     * Call the session bean operation using appropriate view type
     */
    public void updateQuestion(org.didicero.base.entity.Question aQuestion) throws org.didicero.base.service.ContentServiceException {
        try {
            getContentServiceRemote().updateQuestion(aQuestion);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.updateQuestion(org.didicero.base.entity.Question aQuestion)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#createLearningTarget(org.didicero.base.entity.LearningTarget)
     *
     * Call the session bean operation using appropriate view type
     */
    public org.didicero.base.entity.LearningTarget createLearningTarget(org.didicero.base.entity.LearningTarget aTarget) throws org.didicero.base.service.ContentServiceException {
        try {
            return getContentServiceRemote().createLearningTarget(aTarget);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.createLearningTarget(org.didicero.base.entity.LearningTarget aTarget)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#deleteLearningTarget(org.didicero.base.entity.LearningTarget)
     *
     * Call the session bean operation using appropriate view type
     */
    public void deleteLearningTarget(org.didicero.base.entity.LearningTarget aTarget) throws org.didicero.base.service.ContentServiceException {
        try {
            getContentServiceRemote().deleteLearningTarget(aTarget);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.deleteLearningTarget(org.didicero.base.entity.LearningTarget aTarget)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#createHeader(org.didicero.base.entity.Header)
     *
     * Call the session bean operation using appropriate view type
     */
    public org.didicero.base.entity.Header createHeader(org.didicero.base.entity.Header aHeader) throws org.didicero.base.service.ContentServiceException {
        try {
            return getContentServiceRemote().createHeader(aHeader);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.createHeader(org.didicero.base.entity.Header aHeader)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#deleteHeader(org.didicero.base.entity.Header)
     *
     * Call the session bean operation using appropriate view type
     */
    public void deleteHeader(org.didicero.base.entity.Header aHeader) throws org.didicero.base.service.ContentServiceException {
        try {
            getContentServiceRemote().deleteHeader(aHeader);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.deleteHeader(org.didicero.base.entity.Header aHeader)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#createTerm(org.didicero.base.entity.Term)
     *
     * Call the session bean operation using appropriate view type
     */
    public org.didicero.base.entity.Term createTerm(org.didicero.base.entity.Term aTerm) throws org.didicero.base.service.ContentServiceException {
        try {
            return getContentServiceRemote().createTerm(aTerm);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.createTerm(org.didicero.base.entity.Term aTerm)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#deleteTerm(org.didicero.base.entity.Term)
     *
     * Call the session bean operation using appropriate view type
     */
    public void deleteTerm(org.didicero.base.entity.Term aTerm) throws org.didicero.base.service.ContentServiceException {
        try {
            getContentServiceRemote().deleteTerm(aTerm);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.deleteTerm(org.didicero.base.entity.Term aTerm)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#createHtmlFromContent(org.didicero.base.entity.Content, java.lang.String)
     *
     * Call the session bean operation using appropriate view type
     */
    public java.lang.String createHtmlFromContent(org.didicero.base.entity.Content aContent, java.lang.String transform) throws org.didicero.base.service.ContentServiceException {
        try {
            return getContentServiceRemote().createHtmlFromContent(aContent, transform);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.createHtmlFromContent(org.didicero.base.entity.Content aContent, java.lang.String transform)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#createContentFromHtml(java.lang.String, org.didicero.base.entity.Header)
     *
     * Call the session bean operation using appropriate view type
     */
    public org.didicero.base.entity.Content createContentFromHtml(java.lang.String aHtmlCode, org.didicero.base.entity.Header aSubtitel) throws org.didicero.base.service.ContentServiceException {
        try {
            return getContentServiceRemote().createContentFromHtml(aHtmlCode, aSubtitel);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.createContentFromHtml(java.lang.String aHtmlCode, org.didicero.base.entity.Header aSubtitel)' --> " + ex, ex);
        }
    }

    /**
     * @see org.didicero.base.service.ContentServiceBean#deleteContent(org.didicero.base.entity.Content)
     *
     * Call the session bean operation using appropriate view type
     */
    public void deleteContent(org.didicero.base.entity.Content aContent) throws org.didicero.base.service.ContentServiceException {
        try {
            getContentServiceRemote().deleteContent(aContent);
        } catch (org.didicero.base.service.ContentServiceException ex) {
            throw ex;
        } catch (javax.naming.NamingException ex) {
            throw new org.didicero.base.service.ContentServiceException("Error performing 'org.didicero.base.service.ContentService.deleteContent(org.didicero.base.entity.Content aContent)' --> " + ex, ex);
        }
    }
}
