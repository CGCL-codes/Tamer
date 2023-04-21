package net.sf.brightside.instantevents.service.usecases.exceptions;

import net.sf.brightside.instantevents.core.exception.BusinessException;

public class NoEventException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public String toString() {
        return "No event found!";
    }
}
