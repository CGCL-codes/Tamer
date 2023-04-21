package net.woodstock.rockapi.domain.business.validation.local.validator;

import net.woodstock.rockapi.domain.business.validation.local.InitializableValidator;
import net.woodstock.rockapi.domain.message.DomainMessage;
import net.woodstock.rockapi.sys.SysLogger;
import org.apache.commons.logging.Log;

public abstract class AbstractObjectValidator implements InitializableValidator {

    public void init() {
        this.getLogger().info("Initializing validator " + this.getClass().getCanonicalName());
    }

    protected Log getLogger() {
        return SysLogger.getLogger();
    }

    protected String getMessage(String message, Object... args) {
        return DomainMessage.getMessage(message, args);
    }
}
