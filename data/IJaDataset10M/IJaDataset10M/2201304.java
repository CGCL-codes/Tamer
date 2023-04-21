package com.volantis.mcs.project;

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.impl.ValidatingPolicyBuilderAccessor;
import com.volantis.mcs.repository.RepositoryException;

/**
 * Test the {@link ValidatingPolicyBuilderAccessor#updatePolicyBuilder} method.
 */
public class UpdateMethodTestCase extends ValidateBuilderTestAbstract {

    protected void invokeFailingMethod(ValidatingPolicyBuilderAccessor accessor, String name, PolicyType policyType) throws RepositoryException {
        policyBuilderMock.expects.getName().returns(name).any();
        policyBuilderMock.expects.getPolicyType().returns(policyType).any();
        accessor.updatePolicyBuilder(connectionMock, projectMock, policyBuilderMock);
    }

    protected void invokeOkMethod(ValidatingPolicyBuilderAccessor accessor, String name, PolicyType policyType) throws RepositoryException {
        policyBuilderMock.expects.getName().returns(name).any();
        policyBuilderMock.expects.getPolicyType().returns(policyType).any();
        accessorMock.expects.updatePolicyBuilder(connectionMock, projectMock, policyBuilderMock).returns(true);
        boolean updated = accessor.updatePolicyBuilder(connectionMock, projectMock, policyBuilderMock);
        assertTrue(updated);
    }
}
