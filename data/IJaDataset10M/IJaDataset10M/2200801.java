package com.organizadordeeventos.core.exception;

import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.flex.core.ExceptionTranslator;
import flex.messaging.MessageException;

public class BFBusinessExceptionTranslator implements ExceptionTranslator {

    private static final Log LOG = LogFactory.getLog(BFBusinessExceptionTranslator.class);

    @Override
    public boolean handles(final Class<?> arg0) {
        return true;
    }

    @Override
    public MessageException translate(final Throwable error) {
        final MessageException messageException = new MessageException();
        messageException.setCode("Application.Service");
        messageException.setRootCause(error);
        if (error instanceof BFException) {
            messageException.setMessage(error.getMessage());
            return messageException;
        }
        final String codigoError = String.valueOf(Math.abs(new Random().nextInt()));
        LOG.error("ERROR CODE: " + codigoError + " Exception grave lanzada en BusinessObject", error);
        messageException.setMessage("Errores internos, por favor consulte al �rea de sistemas\n\nC�digo de error: " + codigoError);
        return messageException;
    }
}
