package cn.vlabs.duckling.vwb.ui.rsi.api;

/**
 * Introduction Here.
 * @date 2010-3-24
 * @author Fred Zhang (fred@cnic.cn)
 */
public class LoginException extends DCTRsiServiceException {

    public LoginException(int code, String message) {
        super(code, message);
    }
}
