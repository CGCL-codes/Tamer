package net.sf.brightside.mockfantasydrafts.exceptions;

import net.sf.brightside.mockfantasydrafts.core.exception.BusinessException;

public class NotEnoughtTeamsException extends BusinessException {

    @Override
    public String getMessage() {
        return "You must wait until league is full to start the draft!";
    }
}
