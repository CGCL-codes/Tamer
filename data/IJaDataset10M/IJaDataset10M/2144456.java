package org.alfresco.extension.authentication.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import org.alfresco.extension.authentication.constraint.CredentialConstraint;
import org.alfresco.repo.security.authentication.AuthenticationException;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationServiceAdviceTest {

    private AuthenticationServiceAdvice advice;

    @Before
    public void setUp() throws Exception {
        advice = new AuthenticationServiceAdvice();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void invoke() throws Throwable {
        MethodInvocation invocation = mock(MethodInvocation.class);
        CredentialConstraint allowMock = mock(CredentialConstraint.class);
        advice.setConstraints(createAllowConstraintsList(allowMock));
        when(invocation.getArguments()).thenReturn(new Object[] { "user", "oldPw".toCharArray(), "newPw".toCharArray() });
        advice.invoke(invocation);
        verify(allowMock, times(1)).process(any(Credentials.class));
        verify(invocation, times(1)).proceed();
    }

    @Test
    public void invokeAndDoNotProceed() throws Throwable {
        MethodInvocation invocation = mock(MethodInvocation.class);
        CredentialConstraint allowMock = mock(CredentialConstraint.class);
        advice.setConstraints(createAllowConstraintsList(allowMock));
        when(invocation.getArguments()).thenReturn(new Object[] { "user", "oldPw".toCharArray(), "newPw".toCharArray() });
        doThrow(new AuthenticationException("")).when(allowMock).process(any(Credentials.class));
        try {
            advice.invoke(invocation);
        } catch (Exception e) {
            verify(invocation, never()).proceed();
        }
    }

    private List<CredentialConstraint> createAllowConstraintsList(CredentialConstraint... constraints) {
        List<CredentialConstraint> list = new ArrayList<CredentialConstraint>();
        list.addAll(Arrays.asList(constraints));
        return list;
    }
}
