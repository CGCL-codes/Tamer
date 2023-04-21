package com.gorillalogic.gxml;

import org.apache.log4j.Logger;
import com.gorillalogic.dal.AccessException;

public class GxmlException extends AccessException {

    public GxmlException(String msg) {
        super(msg);
    }

    public GxmlException(String msg, Throwable e) {
        super(msg, e);
    }

    public GxmlException(Logger logger, String msg) {
        super(msg);
        logger.error(msg);
    }

    public GxmlException(Logger logger, String msg, Throwable e) {
        super(msg, e);
        logger.error(msg, this);
    }
}
