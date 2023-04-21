package br.com.linkcom.neo.controller;

import br.com.linkcom.neo.authorization.AuthorizationModule;

/**
 * Representa a configura��o de um Control
 * @author rogelgarcia
 */
public class ControlMapping {

    protected String name;

    protected AuthorizationModule authorizationModule;

    public ControlMapping() {
    }

    public String toString() {
        return super.toString() + " (name=" + name + ")";
    }

    public AuthorizationModule getAuthorizationModule() {
        return authorizationModule;
    }

    public String getName() {
        return name;
    }

    public void setAuthorizationModule(AuthorizationModule moduloAutorizacao) {
        this.authorizationModule = moduloAutorizacao;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (ControlMapping.class.isAssignableFrom(obj.getClass())) {
            ControlMapping that = (ControlMapping) obj;
            return this.getName().equals(that.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
