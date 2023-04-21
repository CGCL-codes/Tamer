package br.net.woodstock.rockframework.web.faces.security;

import javax.interceptor.InvocationContext;

public interface LogonValidator extends SecurityValidator {

    boolean isValid(InvocationContext context);
}
