package com.fitso.model.dao.aop;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.math.BigDecimal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Before;
import org.junit.Test;

public class DaoProfileAspectTest {

    protected DaoProfileAspect aspect;

    protected ProceedingJoinPoint mockPjp;

    protected Signature mockSignature;

    @Before
    public void setUp() throws Exception {
        aspect = new DaoProfileAspect();
        mockPjp = createMock(ProceedingJoinPoint.class);
        mockSignature = createMock(Signature.class);
    }

    @Test
    public void test_provile() {
        try {
            expect(mockPjp.getSignature()).andReturn(mockSignature);
            expect(mockSignature.getName()).andReturn("test");
            expect(mockPjp.proceed()).andReturn(BigDecimal.valueOf(2000));
            replay(mockPjp);
            assertEquals(BigDecimal.valueOf(2000), aspect.profile(mockPjp));
        } catch (Throwable t) {
            fail();
        }
    }
}
