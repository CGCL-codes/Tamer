package com.meterware.httpunit;

/**
 *
 * @author <a href="mailto:russgold@acm.org">Russell Gold</a>
 **/
class NoSuchFrameException extends RuntimeException {

    NoSuchFrameException(String frameName) {
        _frameName = frameName;
    }

    public String getMessage() {
        return "No frame named " + _frameName + " is currently active";
    }

    private String _frameName;
}
