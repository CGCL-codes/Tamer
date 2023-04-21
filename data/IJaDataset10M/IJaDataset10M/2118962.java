package org.didicero.base.service;

import org.didicero.base.entity.Request;
import org.didicero.base.entity.Test;

/**
 * @see org.didicero.base.service.TestServiceBase
 *
 * Remember to manually configure the local business interface this bean implements if originally you only
 * defined the remote business interface.  However, this change is automatically reflected in the ejb-jar.xml.
 *
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, the session bean is defined in the ejb-jar.xml descriptor.
 */
@javax.jws.WebService(endpointInterface = "org.didicero.base.service.TestServiceWSInterface")
public class TestServiceBean extends org.didicero.base.service.TestServiceBase implements org.didicero.base.service.TestServiceRemote {

    /**
     * Default constructor extending base class default constructor
     */
    public TestServiceBean() {
        super();
    }

    /**
     * @see org.didicero.base.service.TestServiceBase#createModuleTest(java.lang.String, org.didicero.base.entity.ModuleAttendance)
     */
    protected org.didicero.base.entity.Test handleCreateModuleTest(java.lang.String aName, org.didicero.base.entity.ModuleAttendance aModule) throws java.lang.Exception {
        return null;
    }

    /**
     * @see org.didicero.base.service.TestServiceBase#assignQuestion(org.didicero.base.entity.Test, org.didicero.base.entity.Question, org.didicero.base.entity.ValidationSchema, int, int)
     */
    protected org.didicero.base.entity.TestQuestion handleAssignQuestion(org.didicero.base.entity.Test aTest, org.didicero.base.entity.Question aQuestion, org.didicero.base.entity.ValidationSchema validation, int weight, int severity) throws java.lang.Exception {
        return null;
    }

    /**
     * @see org.didicero.base.service.TestServiceBase#removeQuestion(org.didicero.base.entity.TestQuestion)
     */
    protected void handleRemoveQuestion(org.didicero.base.entity.TestQuestion aQuestion) throws java.lang.Exception {
        throw new java.lang.UnsupportedOperationException("org.didicero.base.service.TestServiceBean.handleRemoveQuestion(org.didicero.base.entity.TestQuestion aQuestion) Not implemented!");
    }

    /**
     * @see org.didicero.base.service.TestServiceBase#validateResponse(org.didicero.base.entity.TestQuestion, org.didicero.base.entity.Response)
     */
    protected org.didicero.base.entity.ValidatedQuestion handleValidateResponse(org.didicero.base.entity.TestQuestion aQuestion, org.didicero.base.entity.Response aResponse) throws java.lang.Exception {
        return null;
    }

    /**
     * @see org.didicero.base.service.TestServiceBase#calculateModuleResult(org.didicero.base.entity.Test, org.didicero.base.entity.ModuleAttendance, org.didicero.base.entity.TestConfiguration)
     */
    protected org.didicero.base.entity.TestResult handleCalculateModuleResult(org.didicero.base.entity.Test aTest, org.didicero.base.entity.ModuleAttendance aModule, org.didicero.base.entity.TestConfiguration aConfig) throws java.lang.Exception {
        return null;
    }

    /**
     * @see org.didicero.base.service.TestServiceBase#deleteTest(org.didicero.base.entity.Test)
     */
    protected void handleDeleteTest(org.didicero.base.entity.Test aTest) throws java.lang.Exception {
        throw new java.lang.UnsupportedOperationException("org.didicero.base.service.TestServiceBean.handleDeleteTest(org.didicero.base.entity.Test aTest) Not implemented!");
    }

    /**
     * @see org.didicero.base.service.TestServiceBase#getRandomQuestion(org.didicero.base.entity.Test)
     */
    protected org.didicero.base.entity.Request handleGetRandomQuestion(org.didicero.base.entity.Test aTest) throws java.lang.Exception {
        return null;
    }

    /**
     * @see org.didicero.base.service.TestServiceBase#listTestModules()
     */
    protected org.didicero.base.entity.ModuleAttendance[] handleListTestModules() throws java.lang.Exception {
        return null;
    }
}
