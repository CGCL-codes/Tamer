package org.jboss.resteasy.security.doseta;

import org.jboss.resteasy.annotations.interception.ClientInterceptor;
import org.jboss.resteasy.annotations.interception.HeaderDecoratorPrecedence;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.annotations.security.doseta.After;
import org.jboss.resteasy.annotations.security.doseta.Signed;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;
import org.jboss.resteasy.spi.interception.MessageBodyWriterContext;
import org.jboss.resteasy.spi.interception.MessageBodyWriterInterceptor;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Provider
@ServerInterceptor
@ClientInterceptor
@HeaderDecoratorPrecedence
public class DigitalSigningHeaderDecorator implements MessageBodyWriterInterceptor, AcceptedByMethod {

    protected Signed signed;

    public boolean accept(Class declaring, Method method) {
        signed = method.getAnnotation(Signed.class);
        if (signed == null) {
            signed = (Signed) declaring.getAnnotation(Signed.class);
        }
        return signed != null;
    }

    public void write(MessageBodyWriterContext context) throws IOException, WebApplicationException {
        DKIMSignature header = new DKIMSignature();
        KeyRepository repository = (KeyRepository) context.getAttribute(KeyRepository.class.getName());
        if (repository == null) {
            repository = ResteasyProviderFactory.getContextData(KeyRepository.class);
        }
        if (signed.domain() != null && !signed.domain().equals("")) {
            header.setDomain(signed.domain());
        } else {
            if (repository != null) {
                header.setDomain(repository.getDefaultPrivateDomain());
            }
        }
        if (signed.algorithm() != null && !signed.algorithm().equals("")) {
            header.setAlgorithm(signed.algorithm());
        }
        if (signed.selector() != null && !signed.selector().equals("")) {
            header.setSelector(signed.selector());
        } else {
            if (repository != null) {
                header.setSelector(repository.getDefaultPrivateSelector());
            }
        }
        if (signed.timestamped()) {
            header.setTimestamp();
        }
        After after = signed.expires();
        if (after.seconds() > 0 || after.minutes() > 0 || after.hours() > 0 || after.days() > 0 || after.months() > 0 || after.years() > 0) {
            header.setExpiration(after.seconds(), after.minutes(), after.hours(), after.days(), after.months(), after.years());
        }
        context.getHeaders().add(DKIMSignature.DKIM_SIGNATURE, header);
        context.proceed();
        return;
    }
}
