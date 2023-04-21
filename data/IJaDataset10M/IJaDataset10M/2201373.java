package br.com.arsmachina.authentication.exception;

/**
 * Exception thrown when the provided credentials (typically, a login/password pair) does
 * not match any user.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class BadCredentialsException extends AuthenticationException {

    private static final long serialVersionUID = 1L;
}
