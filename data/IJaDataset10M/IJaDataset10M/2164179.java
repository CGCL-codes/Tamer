package br.gov.frameworkdemoiselle.internal.producer;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import java.lang.reflect.Member;
import javax.enterprise.inject.spi.InjectionPoint;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

public class LoggerProducerTest {

    private Logger logger;

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testCreateInjectionPoint() {
        Member member = createMock(Member.class);
        expect(member.getDeclaringClass()).andReturn((Class) this.getClass());
        replay(member);
        InjectionPoint injectionPoint = createMock(InjectionPoint.class);
        expect(injectionPoint.getMember()).andReturn(member);
        replay(injectionPoint);
        logger = LoggerProducer.create(injectionPoint);
        Assert.assertNotNull(logger);
    }

    @Test
    public void testCreateClass() {
        logger = LoggerProducer.create(this.getClass());
        Assert.assertNotNull(logger);
    }

    @Test
    public void testLoggerFactoryDiferentNull() {
        @SuppressWarnings("unused") LoggerProducer loggerProducer = new LoggerProducer();
    }
}
